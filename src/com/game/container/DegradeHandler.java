package com.game.container;

import java.util.Objects;

/**
 * This class should be used for degrading {@code ItemStakc} to another state.
 * 
 * @author Albert Beaupre
 */
public final class DegradeHandler {

	/**
	 * Degrades this {@code DegradableItem} from the starting specified
	 * {@code degradeFrom} item and removes the item from the specified
	 * {@code from} container and adds the next item in the degrade sequence to
	 * the specified {@code to} container. After the item is degraded,
	 * {@link #onDegrade(Item)} is called with the previously degraded and newly
	 * replaced item as parameters.
	 * 
	 * @param degradeFrom
	 *            the item to degrade from
	 * @param from
	 *            the container to degrade the item from
	 * @param to
	 *            the container to place the next degradable item in
	 * @return true if the item was degraded, return false otherwise
	 * 
	 * @see #getDegradeSequence()
	 * @see #onDegrade(Item)
	 */
	public static final boolean degrade(Degradable degradable, Item degradeFrom, Container<Item> from, Container<Item> to) {
		degradeFrom = Objects.requireNonNull(degradeFrom, "The item to degrade from cannot be NULL");
		from = Objects.requireNonNull(from, "The container to remove the degrading item cannot be NULL");
		to = Objects.requireNonNull(to, "The container to add the new item cannot be NULL");

		from.remove(degradeFrom);
		from.add(degradeFrom = degradeFrom.health(Math.max(0, degradeFrom.getHealth() - 1)));
		if (degradeFrom.getHealth() <= 0) {
			int itemIndex = -1;
			Item[] sequence = degradable.getDegradeSequence();
			for (int i = 0; i < sequence.length; i++) {
				if (sequence[i] != null && sequence[i].getId() == degradeFrom.getId()) {
					itemIndex = i;
					break;
				}
			}
			if (itemIndex == -1)
				return false;
			Item currentItem = sequence[itemIndex];
			currentItem = currentItem.health(Math.max(0, currentItem.getHealth() - 1));
			if (itemIndex + 1 < sequence.length) {
				from.remove(degradeFrom);
				degradable.onDegrade(currentItem, currentItem = sequence[itemIndex + 1]);
				if (currentItem != null)
					to.add(currentItem);
				return true;
			}
		}
		return false;
	}

	/**
	 * Degrades this {@code DegradableItem} from the starting specified
	 * {@code degradeFrom} item and removes the item from the specified
	 * container and replaces it with the next item in the degrade sequence.
	 * After the item is degraded, {@link #onDegrade(Item)} is called with the
	 * previously degraded and newly replaced item as parameters.
	 * 
	 * @param degradeFrom
	 *            the item to degrade from
	 * @param container
	 *            the container to degrade the item
	 * @return true if the item was degraded, return false otherwise
	 * 
	 * @see #getDegradeSequence()
	 * @see #onDegrade(Item)
	 */
	public static final boolean degrade(Degradable degradable, Item degradeFrom, Container<Item> container) {
		return degrade(degradable, degradeFrom, container, container);
	}

	/**
	 * Checks if the specified {@code container} contains any of the items
	 * within the specified {@code degradable}'s item sequence.
	 * 
	 * @param degradable
	 *            the degradable containing the item sequence
	 * @param container
	 *            the container to search for an item in the sequence
	 * @return true if the container contains an item from the sequence
	 */
	public static boolean hasItem(Degradable degradable, Container<Item> container) {
		for (Item i : degradable.getDegradeSequence())
			if (container.contains(i.getId(), 1))
				return true;
		return false;
	}

}
