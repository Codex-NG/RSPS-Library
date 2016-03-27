package com.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.net.codec.RuneScapeMessageEncoder;
import com.net.codec.handshake.RuneScapeHandshakeDecoder;

public class RuneScapeBootstrap {

	public static void initialize(int port) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		ServerSocketChannelFactory socketFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), Runtime.getRuntime().availableProcessors());

		bootstrap.setOption("localAddress", new InetSocketAddress(port));
		bootstrap.setOption("child.tcpNoDelay", true);

		bootstrap.setFactory(socketFactory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = new DefaultChannelPipeline();
				pipeline.addLast("encoder", new RuneScapeMessageEncoder());
				pipeline.addLast("decoder", new RuneScapeHandshakeDecoder());
				pipeline.addLast("handler", new RuneScapeChannelHandler());
				return pipeline;
			}

		});
		bootstrap.bind();

		System.err.println("Server bount to port " + port);
	}

}
