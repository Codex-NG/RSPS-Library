package com.module;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import co.paralleluniverse.fibers.instrument.QuasarURLClassLoader;

import com.core.Core;
import com.lib.config.ConfigSection;
import com.lib.config.FileConfig;

/**
 * @author netherfoam
 */
public class ModuleLoader {

	/** The folder where modules are loaded from */

	/** Currently loaded modules */
	private HashMap<String, Module> modules;
	private final File moduleFolder;

	/**
	 * Constructs a new Module loader class. This class loads modules from the
	 * modules/ folder. This does not call load(), which can be done later.
	 */
	public ModuleLoader(File moduleFolder) {
		this.moduleFolder = moduleFolder;
		this.modules = new HashMap<String, Module>();
	}

	/**
	 * Loads all modules from the module folder.
	 */
	public void load() {
		long startTime = System.currentTimeMillis();

		Runtime r = Runtime.getRuntime();

		long startMem = r.totalMemory() - r.freeMemory();

		if (moduleFolder.exists() == false) {
			moduleFolder.mkdirs();
		}

		File[] files = moduleFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File parent, String name) {
				if (name.endsWith(".jar")) {
					return true;
				}

				return false;
			}
		});

		for (File f : files) {
			System.out.println("Loading module " + f.getName());
			try {
				load(f);
			} catch (Throwable e) {
				System.out.println("Exception loading module " + f.getName());
				e.printStackTrace();
			}
		}

		//This is just a total stab in the dark guess.
		long endMem = r.totalMemory() - r.freeMemory();

		long endTime = System.currentTimeMillis();
		System.out.println("Modules Loaded! Utilising ~" + (endMem - startMem) / (1024 * 1024) + "MB. Took " + (endTime - startTime) + "ms.");
	}

	/**
	 * Loads the given file as a module and returns it. This method supresses
	 * and logs any error messages and if it returns null, there was an error.
	 * Otherwise it returns the loaded module.
	 * 
	 * @param f
	 *            the file to load from
	 * @return the module.
	 * @throws Throwable
	 */
	public Module load(File f) throws Throwable {
		JarFile jar = null;
		try {
			jar = new JarFile(f);

			//If the URL is already added, we want to unload all of the classes loaded from it. That is done in unload(),
			//so we load the classes here, possibly again.
			QuasarURLClassLoader cl = new QuasarURLClassLoader(new URL[] { f.toURI().toURL() }, Core.CLASS_LOADER);

			ZipEntry e = jar.getEntry("module.yml");
			if (e == null) {
				throw new IOException(f.getName() + ": JAR file does not contain a module.yml file in the root!");
			}

			InputStream in = jar.getInputStream(e);
			ConfigSection cfg = new ConfigSection(in);

			String name = cfg.getString("name");
			if (name == null) {
				throw new IOException(f.getName() + ": JAR module.yml must contain a valid field 'name' to load..");
			}

			in.close();
			String str = cfg.getString("class");
			if (str == null) {
				throw new IOException(f.getName() + ": JAR module.yml must contain a valid field 'class' to load.");
			}

			Class<?> clazz = cl.loadClass(str);

			Class<?> parent = clazz.getSuperclass();
			while (parent != null && parent != Module.class) {
				parent = parent.getSuperclass();
			}

			if (parent != Module.class) {
				throw new IOException(f.getName() + ": Class specified in module.yml in JAR file must extend Module class.");
			}

			Module m;
			try {
				m = (Module) clazz.newInstance();
			} catch (Exception ex) {
				throw new IOException(f.getName() + ": Class specified in module.yml in JAR file must have a zero-args constructor.");
			}

			ModuleMeta meta = new ModuleMeta(f, cfg, cl);
			m.setMeta(meta);

			FileConfig modCfg = new FileConfig(new File(m.getFolder(), "config.yml"));
			modCfg.reload(); //Initial load
			meta.setConfig(modCfg);

			// We add it now, so that if loading calls unload() during load(), then we don't add the module to the set of loaded modules
			this.modules.put(m.getName().toLowerCase(), m);

			try {
				m.load();
			} catch (Throwable t) {
				System.out.println("Error calling module load() on " + m.getName() + " (" + f.getName() + ").");
				try {
					m.unload();
					m.getMeta().getLoader().close();
				} catch (Throwable t2) {
					//Ignored, failed to unload.
				}
				this.modules.remove(m.getName().toLowerCase());
				throw t; //Caught later
			}

			return m;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load module " + f.getName() + ". Invalid/Corrupt JAR File?");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load module " + f.getName() + ".");
		} finally {
			try {
				jar.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Fetches a module by name. This is case insensitive and uses a hashmap
	 * lookup. The name this is matched against is the result of the
	 * module.getName() call.
	 * 
	 * @param name
	 *            the name of the module
	 * @return the module or null if not found
	 */
	public Module getModule(String name) {
		return modules.get(name.toLowerCase());
	}

	/**
	 * Fetches a module by file. This is a linear lookup, and returns the module
	 * which was loaded from the given file (for example, getJar() == file).
	 * 
	 * @param file
	 *            The file a module was loaded from
	 * @return the module or null if there is no module from that file
	 */
	public Module getModule(File file) {
		for (Module m : modules.values()) {
			if (m.getJar().equals(file)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Returns an unmodifiable list of modules which are currently loaded on
	 * this server.
	 * 
	 * @return the list of modules loaded
	 */
	public Collection<Module> getModules() {
		return Collections.unmodifiableCollection(modules.values());
	}

	/**
	 * Unloads the given module
	 * 
	 * @param m
	 *            The module
	 * @return true if successful, false if the unload method throws an
	 *         exception.
	 */
	public boolean unload(Module m) {
		try {
			m.unload();
			m.getMeta().getLoader().close();
			return true;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		} finally {
			modules.remove(m.getName().toLowerCase());
		}
	}

	/**
	 * Unloads all currently active modules.
	 */
	public void unload() {
		Iterator<Module> mit = modules.values().iterator();
		while (mit.hasNext()) {
			Module m = mit.next();
			try {
				m.unload();
				m.getMeta().getLoader().close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			mit.remove();
		}
	}
}