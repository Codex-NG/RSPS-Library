package com.game.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.lib.ArrayUtility;

/**
 * @author Albert Beaupre
 *
 * @param <E>
 *            The item element.
 */
@SuppressWarnings("unchecked")
public abstract class Container<E extends Item> implements Collection<E>, Iterable<E> {

	private final ContainerType type;
	private final byte capacity;
	private int maxStack, minStack;
	private Item[] data;
	private int size;

	public Container(ContainerType type, int capacity) {
		this(type, capacity, 1, Integer.MAX_VALUE);
	}

	public Container(ContainerType type, int capacity, int minStack, int maxStack) {
		this.data = new Item[this.capacity = (byte) capacity];
		this.maxStack = maxStack;
		this.minStack = minStack;
		this.type = type;
	}

	public abstract void refresh();

	public Container<E> swap(int fromIndex, int toIndex) {
		Item old = get(toIndex);
		set(toIndex, get(fromIndex));
		set(fromIndex, old);
		return this;
	}

	public Container<E> add(Item... items) {
		for (Item item : items)
			if (item != null)
				add(item);
		return this;
	}

	@Override
	public boolean add(Item e) {
		if (e == null)
			return false;
		int id = e.getId();
		int amount = e.getAmount();
		int freeIndex = this.getFreeIndex();
		if (freeIndex == -1)
			return false;
		int index = indexOf(id);
		switch (type) {
		case ALWAYS_STACK:
			if (index != -1 && healthStackable(e, get(index))) {
				int there = get(index).getAmount();
				int total = there + amount > maxStack ? maxStack : there + amount;
				set(index, new Item(id, total));
			} else {
				if (getFreeSlots() == 0)
					return false;
				size++;
				data[freeIndex] = new Item(id, amount > maxStack ? maxStack : amount);
			}
			refresh();
			return true;
		case AVAILABLE_STACK:
			if (e.isStackable()) {
				if (index != -1 && healthStackable(e, get(index))) {
					int there = get(index).getAmount();
					int total = there + amount > maxStack ? maxStack : there + amount;
					set(index, new Item(id, total));
				} else {
					if (getFreeSlots() == 0)
						return false;
					size++;
					data[freeIndex] = new Item(id, amount > maxStack ? maxStack : amount);
				}
				refresh();
			} else {
				if (getFreeSlots() == 0)
					return false;
				for (int i = 0; i < amount; i++) {
					freeIndex = this.getFreeIndex();
					if (freeIndex != -1) {
						size++;
						data[freeIndex] = new Item(id, 1);
					} else {
						refresh();
						return false;
					}
				}
				refresh();
			}
			return true;
		case NEVER_STACK:
			if (getFreeSlots() == 0)
				return false;
			for (int i = 0; i < amount; i++) {
				freeIndex = this.getFreeIndex();
				if (freeIndex != -1) {
					size++;
					data[freeIndex] = new Item(id, 1);
				} else {
					refresh();
					return false;
				}
			}
			refresh();
			return true;
		}
		return false;
	}

	public boolean insert(int index, Item element) {
		if (getFreeSlots() == 0)
			return false;
		if (element != null)
			size++;
		Item[] items = Arrays.copyOf(data, data.length);
		for (int i = index + 1; i < capacity; i++)
			set(i, items[i - 1]);
		data[index] = element;
		refresh();
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (Item item : c)
			if (item != null && !add(item))
				return false;
		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		if (getFreeSlots() > 0) {
			c.stream().forEach(n -> add(n));
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		size = 0;
		for (int i = 0; i < capacity; i++)
			data[i] = null;
		refresh();
	}

	public boolean contains(Object... objects) {
		for (Object o : objects)
			if (!contains(o))
				return false;
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object object : c)
			if (!contains(object))
				return false;
		return true;
	}

	public E get(int index) {
		return (E) data[index];
	}

	public int indexOf(Object o) {
		if (o instanceof Item) {
			Item item = (Item) o;
			for (int i = 0; i < capacity; i++)
				if (data[i] != null && data[i].getId() == item.getId() && data[i].getAmount() >= item.getAmount())
					return i;
		} else if (o instanceof Integer) {
			for (int i = 0; i < capacity; i++)
				if (data[i] != null && data[i].getId() == (Integer) o)
					return i;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			int index;

			@Override
			public boolean hasNext() {
				return index < capacity;
			}

			public E next() {
				return (E) data[index++];
			}
		};
	}

	public int lastIndexOf(Object o) {
		for (int i = capacity - 1; i >= 0; i--)
			if (data[i] != null && data[i].equals(o))
				return i;
		return -1;
	}

	@Override
	public boolean remove(Object o) {
		boolean is_item = o instanceof Item;
		int index = indexOf(is_item ? ((Item) o).getId() : (int) o);
		if (index != -1) {
			Item item = data[index];
			switch (type) {
			case ALWAYS_STACK:
				if (is_item) {
					Item it = (Item) o;
					item.amount(item.getAmount() - it.getAmount());
					if (item.getAmount() < minStack) {
						data[index] = null;
						size--;
					}
				} else {
					data[index] = null;
					size--;
				}
				refresh();
				return true;
			case AVAILABLE_STACK:
				if (item.isStackable()) {
					if (is_item) {
						Item it = (Item) o;
						item.amount(item.getAmount() - it.getAmount());
						if (item.getAmount() < minStack) {
							data[index] = null;
							size--;
						}
					} else {
						data[index] = null;
						size--;
					}
				} else {
					if (is_item) {
						Item it = (Item) o;
						for (int i = 0; i < it.getAmount(); i++) {
							int index_2 = indexOf(it.getId());
							if (index_2 != -1) {
								data[index_2] = null;
								size--;
							} else {
								refresh();
								return false;
							}
						}
					} else {
						data[index] = null;
						size--;
					}
				}
				refresh();
				return true;
			case NEVER_STACK:
				if (is_item) {
					Item it = (Item) o;
					for (int i = 0; i < it.getAmount(); i++) {
						int index_2 = indexOf(it.getId());
						if (index_2 != -1) {
							data[index_2] = null;
							size--;
						} else {
							refresh();
							return false;
						}
					}
				} else {
					data[index] = null;
					size--;
				}
				refresh();
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean remove(int index, int amount) {
		if (data[index] != null) {
			data[index].amount(data[index].getAmount() - amount);
			if (data[index].getAmount() < minStack) {
				data[index] = null;
				size--;
			}
			refresh();
			return true;
		}
		return false;
	}

	public E remove(int index) {
		if (index == -1)
			throw new IllegalArgumentException("Value less than 0: " + index);
		E e = (E) data[index];
		if (e != null) {
			data[index] = null;
			size--;
		}
		refresh();
		return e;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object item : c)
			if (!remove(item))
				return false;
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		c.stream().filter(n -> !contains(n)).forEach(n -> remove(n));
		return this.containsAll(c);
	}

	public E set(int index, Item element) {
		if (data[index] == null && element != null)
			size++;
		if (data[index] != null && element == null)
			size--;
		E e = (E) (data[index] = element);
		refresh();
		return e;
	}

	private boolean healthStackable(Item item, Item item2) {
		if ((item.getHealth() == 0 && item2.getHealth() == 0) || (item.getHealth() == 100 && item2.getHealth() == 100))
			return true;
		return false;
	}

	public boolean isAddable(Item item) {
		if (type == ContainerType.ALWAYS_STACK) {
			if (getFreeSlots() == 0) {
				return contains(item.getId()) && healthStackable(item, get(indexOf(item.getId())));
			} else {
				return true;
			}
		} else if (type == ContainerType.NEVER_STACK) {
			return getFreeSlots() > 0;
		} else {
			if (getFreeSlots() == 0) {
				return contains(item.getId()) && healthStackable(item, get(indexOf(item.getId()))) && item.isStackable();
			} else
				return getFreeSlots() > 0;
		}
	}

	public int getFreeIndex() {
		for (int i = 0; i < capacity; i++)
			if (data[i] == null)
				return i;
		return -1;
	}

	public int getFreeSlots() {
		return capacity - size();
	}

	public Container<E> shift() {
		ArrayList<Item> shifted = new ArrayList<Item>(size);
		Arrays.asList(data).stream().filter(n -> n != null).forEach(n -> shifted.add(n));
		this.data = shifted.toArray(new Item[capacity]);
		refresh();
		return this;
	}

	public Container<E> shuffle() {
		ArrayUtility.shuffle(data);
		return this;
	}

	public int amountOf(int itemId) {
		int amount = 0;
		for (int i = 0; i < capacity; i++)
			if (data[i] != null && data[i].getId() == itemId)
				amount += data[i].getAmount();
		return amount;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(data, data.length);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return (T[]) Arrays.copyOf(data, size(), a.getClass());
	}

	public ContainerType getType() {
		return type;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.data);
	}
}