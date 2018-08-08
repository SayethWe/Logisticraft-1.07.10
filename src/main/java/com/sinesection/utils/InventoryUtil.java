package com.sinesection.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public abstract class InventoryUtil {

	/* REMOVAL */

	/**
	 * Removes a set of items from an inventory. Removes the exact items first
	 * if they exist, and then removes crafting equivalents. If the inventory
	 * doesn't have all the required items, returns false without removing
	 * anything. If stowContainer is true, items with containers will have their
	 * container stowed.
	 */
	public static boolean removeSets(IInventory inventory, int count, List<ItemStack> set, @Nullable EntityPlayer player, boolean stowContainer, boolean oreDictionary, boolean craftingTools, boolean doRemove) {
		List<ItemStack> stock = getStacks(inventory);

		if (doRemove) {
			List<ItemStack> removed = removeSets(inventory, count, set, player, stowContainer, oreDictionary, craftingTools);
			return removed != null && removed.size() >= count;
		} else {
			return ItemStackUtil.containsSets(set, stock, oreDictionary, craftingTools) >= count;
		}
	}

	public static boolean removeSets(IInventory inventory, int count, List<ItemStack> set, List<String> oreDicts, @Nullable EntityPlayer player, boolean stowContainer, boolean craftingTools, boolean doRemove) {
		List<ItemStack> stock = getStacks(inventory);

		if (doRemove) {
			List<ItemStack> removed = removeSets(inventory, count, set, oreDicts, player, stowContainer, craftingTools);
			return removed != null && removed.size() >= count;
		} else {
			return ItemStackUtil.containsSets(set, stock, oreDicts, craftingTools) >= count;
		}
	}

	public static boolean deleteExactSet(IInventory inventory, List<ItemStack> required) {
		List<ItemStack> offered = getStacks(inventory);
		List<ItemStack> condensedRequired = ItemStackUtil.condenseStacks(required);
		List<ItemStack> condensedOffered = ItemStackUtil.condenseStacks(offered);

		for (ItemStack req : condensedRequired) {
			if (!containsExactStack(req, condensedOffered)) {
				return false;
			}
		}

		for (ItemStack itemStack : condensedRequired) {
			deleteExactStack(inventory, itemStack);
		}
		return true;
	}

	private static boolean containsExactStack(ItemStack req, List<ItemStack> condensedOffered) {
		for (ItemStack offer : condensedOffered) {
			if (offer.stackSize >= req.stackSize && ItemStackUtil.areItemStacksEqualIgnoreCount(req, offer)) {
				return true;
			}
		}
		return false;
	}

	private static void deleteExactStack(IInventory inventory, ItemStack itemStack) {
		int count = itemStack.stackSize;
		for (int j = 0; j < inventory.getSizeInventory(); j++) {
			ItemStack stackInSlot = inventory.getStackInSlot(j);
			if (stackInSlot != null) {
				if (ItemStackUtil.areItemStacksEqualIgnoreCount(itemStack, stackInSlot)) {
					ItemStack removed = inventory.decrStackSize(j, count);
					count -= removed.stackSize;
					if (count == 0) {
						return;
					}
				}
			}
		}
	}

	@Nullable
	public static List<ItemStack> removeSets(IInventory inventory, int count, List<ItemStack> set, @Nullable EntityPlayer player, boolean stowContainer, boolean oreDictionary, boolean craftingTools) {
		List<ItemStack> removed = new ArrayList<ItemStack>();
		List<ItemStack> stock = getStacks(inventory);

		if (ItemStackUtil.containsSets(set, stock, oreDictionary, craftingTools) < count) {
			return null;
		}

		for (int i = 0; i < set.size(); i++) {
			ItemStack itemStack = set.get(i);
			if (itemStack != null) {
				ItemStack stackToRemove = itemStack.copy();
				stackToRemove.stackSize = stackToRemove.stackSize * count;

				// try to remove the exact stack first
				ItemStack removedStack = removeStack(inventory, stackToRemove, player, stowContainer, false, false);
				if (removedStack == null) {
					// remove crafting equivalents next
					removedStack = removeStack(inventory, stackToRemove, player, stowContainer, oreDictionary, craftingTools);
				}

				removed.set(i, removedStack);
			}
		}
		return removed;
	}

	@Nullable
	public static List<ItemStack> removeSets(IInventory inventory, int count, List<ItemStack> set, List<String> oreDicts, @Nullable EntityPlayer player, boolean stowContainer, boolean craftingTools) {
		List<ItemStack> removed = new ArrayList<ItemStack>();
		List<ItemStack> stock = getStacks(inventory);

		if (ItemStackUtil.containsSets(set, stock, oreDicts, craftingTools) < count) {
			return null;
		}

		for (int i = 0; i < set.size(); i++) {
			ItemStack itemStack = set.get(i);
			if (itemStack != null) {
				ItemStack stackToRemove = itemStack.copy();
				stackToRemove.stackSize = stackToRemove.stackSize * count;

				// try to remove the exact stack first
				ItemStack removedStack = removeStack(inventory, stackToRemove, null, player, stowContainer, false);
				if (removedStack == null) {
					// remove crafting equivalents next
					removedStack = removeStack(inventory, stackToRemove, oreDicts.get(i), player, stowContainer, craftingTools);
				}

				removed.set(i, removedStack);
			}
		}
		return removed;
	}

	/**
	 * Private Helper for removeSetsFromInventory. Assumes removal is possible.
	 */
	private static ItemStack removeStack(IInventory inventory, ItemStack stackToRemove, @Nullable EntityPlayer player, boolean stowContainer, boolean oreDictionary, boolean craftingTools) {
		for (int j = 0; j < inventory.getSizeInventory(); j++) {
			ItemStack stackInSlot = inventory.getStackInSlot(j);
			if (stackInSlot != null) {
				if (ItemStackUtil.isCraftingEquivalent(stackToRemove, stackInSlot, oreDictionary, craftingTools)) {
					ItemStack removed = inventory.decrStackSize(j, stackToRemove.stackSize);
					stackToRemove.stackSize -= removed.stackSize;

					if (stowContainer && stackToRemove.getItem().hasContainerItem(stackToRemove)) {
						stowContainerItem(removed, inventory, j, player);
					}

					if (stackToRemove.stackSize == 0) {
						return removed;
					}
				}
			}
		}
		return null;
	}

	private static ItemStack removeStack(IInventory inventory, ItemStack stackToRemove, @Nullable String oreDictOfStack, @Nullable EntityPlayer player, boolean stowContainer, boolean craftingTools) {
		for (int j = 0; j < inventory.getSizeInventory(); j++) {
			ItemStack stackInSlot = inventory.getStackInSlot(j);
			if (stackInSlot != null) {
				if (ItemStackUtil.isCraftingEquivalent(stackToRemove, stackInSlot, oreDictOfStack, craftingTools)) {
					ItemStack removed = inventory.decrStackSize(j, stackToRemove.stackSize);
					stackToRemove.stackSize -= removed.stackSize;

					if (stowContainer && stackToRemove.getItem().hasContainerItem(stackToRemove)) {
						stowContainerItem(removed, inventory, j, player);
					}

					if (stackToRemove.stackSize == 0) {
						return removed;
					}
				}
			}
		}
		return null;
	}

	/* CONTAINS */

	public static boolean contains(IInventory inventory, List<ItemStack> query) {
		return contains(inventory, query, 0, inventory.getSizeInventory());
	}

	public static boolean contains(IInventory inventory, List<ItemStack> query, int startSlot, int slots) {
		List<ItemStack> stock = getStacks(inventory, startSlot, slots);
		return ItemStackUtil.containsSets(query, stock) > 0;
	}

	public static boolean containsPercent(IInventory inventory, float percent) {
		return containsPercent(inventory, percent, 0, inventory.getSizeInventory());
	}

	public static boolean containsPercent(IInventory inventory, float percent, int slot1, int length) {
		int amount = 0;
		int stackMax = 0;
		for (ItemStack itemStack : getStacks(inventory, slot1, length)) {
			if (itemStack == null) {
				stackMax += 64;
				continue;
			}

			amount += itemStack.stackSize;
			stackMax += itemStack.getMaxStackSize();
		}
		if (stackMax == 0) {
			return false;
		}
		return (float) amount / (float) stackMax >= percent;
	}

	public static boolean isEmpty(IInventory inventory, int slotStart, int slotCount) {
		for (int i = slotStart; i < slotStart + slotCount; i++) {
			if (inventory.getStackInSlot(i) != null) {
				return false;
			}
		}
		return true;
	}

	public static List<ItemStack> getStacks(IInventory inventory) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			stacks.set(i, inventory.getStackInSlot(i));
		}
		return stacks;
	}

	public static List<ItemStack> getStacks(IInventory inventory, int slot1, int length) {
		List<ItemStack> result = new ArrayList<ItemStack>();
		for (int i = slot1; i < slot1 + length; i++) {
			result.set(i - slot1, inventory.getStackInSlot(i));
		}
		return result;
	}

	public static List<String> getOreDictAsList(String[][] oreDicts) {
		List<String> result = new ArrayList<String>();
		if (oreDicts == null || oreDicts.length == 0) {
			return result;
		}
		for (int i = 0; i < oreDicts.length; i++) {
			for (int d = 0; d < oreDicts[i].length; d++) {
				result.set(i * 3 + d, oreDicts[d][i]);
			}
		}
		return result;
	}

	public static boolean tryAddStacksCopy(IInventory inventory, List<ItemStack> stacks, int startSlot, int slots, boolean all) {

		for (ItemStack stack : stacks) {
			if (stack == null || stack.stackSize == 0) {
				continue;
			}

			if (!tryAddStack(inventory, stack.copy(), startSlot, slots, all)) {
				return false;
			}
		}

		return true;
	}

	public static boolean tryAddStack(IInventory inventory, ItemStack stack, boolean all) {
		return tryAddStack(inventory, stack, 0, inventory.getSizeInventory(), all, true);
	}

	public static boolean tryAddStack(IInventory inventory, ItemStack stack, boolean all, boolean doAdd) {
		return tryAddStack(inventory, stack, 0, inventory.getSizeInventory(), all, doAdd);
	}

	/**
	 * Tries to add a stack to the specified slot range.
	 */
	public static boolean tryAddStack(IInventory inventory, ItemStack stack, int startSlot, int slots, boolean all) {
		return tryAddStack(inventory, stack, startSlot, slots, all, true);
	}

	public static boolean tryAddStack(IInventory inventory, ItemStack stack, int startSlot, int slots, boolean all, boolean doAdd) {
		int added = addStack(inventory, stack, startSlot, slots, false);
		boolean success = all ? added == stack.stackSize : added > 0;

		if (success && doAdd) {
			addStack(inventory, stack, startSlot, slots, true);
		}

		return success;
	}

	public static int addStack(IInventory inventory, ItemStack stack, boolean doAdd) {
		return addStack(inventory, stack, 0, inventory.getSizeInventory(), doAdd);
	}

	public static int addStack(IInventory inventory, ItemStack stack, int startSlot, int slots, boolean doAdd) {
		if (stack == null) {
			return 0;
		}

		int added = 0;
		// Add to existing stacks first
		for (int i = startSlot; i < startSlot + slots; i++) {

			ItemStack inventoryStack = inventory.getStackInSlot(i);
			// Empty slot. Add
			if (inventoryStack == null) {
				continue;
			}

			// Already occupied by different item, skip this slot.
			if (!inventoryStack.isStackable()) {
				continue;
			}
			if (!inventoryStack.isItemEqual(stack)) {
				continue;
			}
			if (!ItemStack.areItemStackTagsEqual(inventoryStack, stack)) {
				continue;
			}

			int remain = stack.stackSize - added;
			int space = inventoryStack.getMaxStackSize() - inventoryStack.stackSize;
			// No space left, skip this slot.
			if (space <= 0) {
				continue;
			}
			// Enough space
			if (space >= remain) {
				if (doAdd) {
					inventoryStack.stackSize += remain;
				}
				return stack.stackSize;
			}

			// Not enough space
			if (doAdd) {
				inventoryStack.stackSize = inventoryStack.getMaxStackSize();
			}

			added += space;
		}

		if (added >= stack.stackSize) {
			return added;
		}

		for (int i = startSlot; i < startSlot + slots; i++) {
			if (inventory.getStackInSlot(i) == null) {
				if (doAdd) {
					inventory.setInventorySlotContents(i, stack.copy());
					inventory.getStackInSlot(i).stackSize -= added;
				}
				return stack.stackSize;
			}
		}

		return added;
	}

	public static boolean stowInInventory(ItemStack itemstack, IInventory inventory, boolean doAdd) {
		return stowInInventory(itemstack, inventory, doAdd, 0, inventory.getSizeInventory());
	}

	public static boolean stowInInventory(ItemStack itemstack, IInventory inventory, boolean doAdd, int slot1, int count) {

		boolean added = false;

		for (int i = slot1; i < slot1 + count; i++) {
			ItemStack inventoryStack = inventory.getStackInSlot(i);

			// Grab those free slots
			if (inventoryStack == null) {
				if (doAdd) {
					inventory.setInventorySlotContents(i, itemstack.copy());
					itemstack.stackSize = 0;
				}
				return true;
			}

			// Already full
			if (inventoryStack.stackSize >= inventoryStack.getMaxStackSize()) {
				continue;
			}

			// Not same type
			if (!inventoryStack.isItemEqual(itemstack)) {
				continue;
			}
			if (!ItemStack.areItemStackTagsEqual(inventoryStack, itemstack)) {
				continue;
			}

			int space = inventoryStack.getMaxStackSize() - inventoryStack.stackSize;

			// Enough space to add all
			if (space > itemstack.stackSize) {
				if (doAdd) {
					inventoryStack.stackSize += itemstack.stackSize;
					itemstack.stackSize = 0;
				}
				return true;
				// Only part can be added
			} else {
				if (doAdd) {
					inventoryStack.stackSize = inventoryStack.getMaxStackSize();
					itemstack.stackSize -= space;
				}
				added = true;
			}

		}

		return added;
	}

	public static void stowContainerItem(ItemStack itemstack, IInventory stowing, int slotIndex, @Nullable EntityPlayer player) {
		if (!itemstack.getItem().hasContainerItem(itemstack)) {
			return;
		}

		ItemStack container = itemstack.getItem().getContainerItem(itemstack);
		if (container != null) {
			if (!tryAddStack(stowing, container, slotIndex, 1, true)) {
				if (!tryAddStack(stowing, container, true) && player != null) {
					player.dropItem(container.getItem(), container.stackSize);
				}
			}
		}
	}

	public static void deepCopyInventoryContents(IInventory source, IInventory destination) {
		if (source.getSizeInventory() != destination.getSizeInventory()) {
			throw new IllegalArgumentException("Inventory sizes do not match. Source: " + source + ", Destination: " + destination);
		}

		for (int i = 0; i < source.getSizeInventory(); i++) {
			ItemStack stack = source.getStackInSlot(i);
			if (stack != null) {
				stack = stack.copy();
			}
			destination.setInventorySlotContents(i, stack);
		}
	}

	public static void dropInventory(IInventory inventory, World world, double x, double y, double z) {
		// Release inventory
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack itemstack = inventory.getStackInSlot(slot);
			dropItemStackFromInventory(itemstack, world, x, y, z);
			inventory.setInventorySlotContents(slot, null);
		}
	}

	public static void dropInventory(IInventory inventory, World world, int x, int y, int z) {
		dropInventory(inventory, world, x, y, z);
	}

	public static void dropItemStackFromInventory(ItemStack itemStack, World world, double x, double y, double z) {
		if (itemStack == null) {
			return;
		}

		float f = world.rand.nextFloat() * 0.8F + 0.1F;
		float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
		float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

		while (itemStack != null) {
			int stackPartial = world.rand.nextInt(21) + 10;
			if (stackPartial > itemStack.stackSize) {
				stackPartial = itemStack.stackSize;
			}
			ItemStack drop = itemStack.splitStack(stackPartial);
			EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, drop);
			float accel = 0.05F;
			entityitem.motionX = (float) world.rand.nextGaussian() * accel;
			entityitem.motionY = (float) world.rand.nextGaussian() * accel + 0.2F;
			entityitem.motionZ = (float) world.rand.nextGaussian() * accel;
			world.spawnEntityInWorld(entityitem);
		}
	}

	/* NBT */
	public static void readFromNBT(IInventory inventory, NBTTagCompound nbttagcompound) {
		if (!nbttagcompound.hasKey(inventory.getInventoryName())) {
			return;
		}

		NBTTagList nbttaglist = nbttagcompound.getTagList(inventory.getInventoryName(), 10);

		for (int j = 0; j < nbttaglist.tagCount(); ++j) {
			NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(j);
			int index = nbttagcompound2.getByte("Slot");
			inventory.setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(nbttagcompound2));
		}
	}

	public static void writeToNBT(IInventory inventory, NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				NBTTagCompound nbttagcompound2 = new NBTTagCompound();
				nbttagcompound2.setByte("Slot", (byte) i);
				inventory.getStackInSlot(i).writeToNBT(nbttagcompound2);
				nbttaglist.appendTag(nbttagcompound2);
			}
		}
		nbttagcompound.setTag(inventory.getInventoryName(), nbttaglist);
	}
}