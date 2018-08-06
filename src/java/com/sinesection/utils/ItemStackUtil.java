package com.sinesection.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class ItemStackUtil {

	public static final ItemStack[] EMPTY_STACK_ARRAY = new ItemStack[0];

	/**
	 * Compares item id, damage and NBT. Accepts wildcard damage.
	 */
	public static boolean isIdenticalItem(ItemStack lhs, ItemStack rhs) {
		if (lhs == rhs) {
			return true;
		}

		if (lhs == null || rhs == null) {
			return false;
		}

		if (lhs.getItem() != rhs.getItem()) {
			return false;
		}

		if (lhs.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
			if (lhs.getItemDamage() != rhs.getItemDamage()) {
				return false;
			}
		}

		return ItemStack.areItemStackTagsEqual(lhs, rhs);
	}

	/**
	 * Merges the giving stack into the receiving stack as far as possible
	 */
	public static void mergeStacks(ItemStack giver, ItemStack receptor) {
		if (receptor.stackSize >= receptor.getMaxStackSize()) {
			return;
		}

		if (!receptor.isItemEqual(giver)) {
			return;
		}

		if (giver.stackSize <= receptor.getMaxStackSize() - receptor.stackSize) {
			receptor.stackSize += giver.stackSize;
			giver.stackSize = 0;
			return;
		}

		ItemStack temp = giver.splitStack(receptor.getMaxStackSize() - receptor.stackSize);
		receptor.stackSize += temp.stackSize;
		temp.stackSize = 0;
	}

	/**
	 * Creates a copy stack of the specified amount, preserving NBT data,
	 * without decreasing the source stack.
	 */

	public static ItemStack createCopyWithCount(ItemStack stack, int count) {
		ItemStack copy = stack.copy();
		copy.stackSize = count;
		return copy;
	}

	public static List<ItemStack> condenseStacks(List<ItemStack> stacks) {
		List<ItemStack> condensed = new ArrayList<ItemStack>();

		for (ItemStack stack : stacks) {
			if (stack == null) {
				continue;
			}

			boolean matched = false;
			for (ItemStack cached : condensed) {
				if (cached.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(cached, stack)) {
					cached.stackSize += stack.stackSize;
					matched = true;
				}
			}

			if (!matched) {
				ItemStack cached = stack.copy();
				condensed.add(cached);
			}

		}

		return condensed;
	}
	
	public static Pair<List<ItemStack>, List<String>> condenseStacks(List<ItemStack> stacks, List<String> dicts) {
		List<ItemStack> condensed = new ArrayList<ItemStack>();
		List<String> condensedDicts = new ArrayList<String>();

		for (int i = 0;i < stacks.size();i++) {
			ItemStack stack = stacks.get(i);
			if (stack == null) {
				continue;
			}

			boolean matched = false;
			for (ItemStack cached : condensed) {
				if (cached.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(cached, stack)) {
					cached.stackSize += stack.stackSize;
					matched = true;
				}
			}

			if (!matched) {
				ItemStack cached = stack.copy();
				condensed.add(cached);
				condensedDicts.add(dicts.get(i));
			}

		}

		return Pair.of(condensed, condensedDicts);
	}

	public static boolean containsItemStack(Iterable<ItemStack> list, ItemStack itemStack) {
		for (ItemStack listStack : list) {
			if (isIdenticalItem(listStack, itemStack)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Counts how many full sets are contained in the passed stock
	 */
	public static int containsSets(List<ItemStack> set, List<ItemStack> stock) {
		return containsSets(set, stock, false, false);
	}

	/**
	 * Counts how many full sets are contained in the passed stock
	 */
	public static int containsSets(List<ItemStack> set, List<ItemStack> stock, boolean oreDictionary, boolean craftingTools) {
		int totalSets = 0;

		List<ItemStack> condensedRequired = ItemStackUtil.condenseStacks(set);
		List<ItemStack> condensedOffered = ItemStackUtil.condenseStacks(stock);

		for (ItemStack req : condensedRequired) {

			int reqCount = 0;
			for (ItemStack offer : condensedOffered) {
				if (isCraftingEquivalent(req, offer, oreDictionary, craftingTools)) {
					int stackCount = (int) Math.floor(offer.stackSize / req.stackSize);
					reqCount = Math.max(reqCount, stackCount);
				}
			}

			if (reqCount == 0) {
				return 0;
			} else if (totalSets == 0) {
				totalSets = reqCount;
			} else if (totalSets > reqCount) {
				totalSets = reqCount;
			}
		}

		return totalSets;
	}
	
	/**
	 * Counts how many full sets are contained in the passed stock
	 */
	public static int containsSets(List<ItemStack> set, List<ItemStack> stock, List<String> oreDicts, boolean craftingTools) {
		int totalSets = 0;

		Pair<List<ItemStack>, List<String>> condensedRequired = ItemStackUtil.condenseStacks(set, oreDicts);
		List<String> condensedRequiredDicts= condensedRequired.getRight();
		List<ItemStack> condensedRequiredStacks = condensedRequired.getLeft();
		List<ItemStack> condensedOfferedStacks = ItemStackUtil.condenseStacks(stock);

		for (int y = 0;y < condensedRequiredStacks.size();y++) {
			ItemStack req = condensedRequiredStacks.get(y);
			String offerDict = condensedRequiredDicts.get(y);
			int reqCount = 0;
			for (ItemStack offer : condensedOfferedStacks) {
				if (isCraftingEquivalent(req, offer, offerDict, craftingTools)) {
					int stackCount = (int) Math.floor(offer.stackSize / req.stackSize);
					reqCount = Math.max(reqCount, stackCount);
				}
			}

			if (reqCount == 0) {
				return 0;
			} else if (totalSets == 0) {
				totalSets = reqCount;
			} else if (totalSets > reqCount) {
				totalSets = reqCount;
			}
		}

		return totalSets;
	}

	public static boolean equalSets(List<ItemStack> set1, List<ItemStack> set2) {
		if (set1 == set2) {
			return true;
		}

		if (set1.size() != set2.size()) {
			return false;
		}

		for (int i = 0; i < set1.size(); i++) {
			if (!isIdenticalItem(set1.get(i), set2.get(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Compare two item stacks for crafting equivalency without oreDictionary or craftingTools
	 */
	public static boolean isCraftingEquivalent(ItemStack base, ItemStack comparison) {
		if (base == null || comparison == null) {
			return false;
		}

		if (base.getItem() != comparison.getItem()) {
			return false;
		}

		if (base.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
			if (base.getItemDamage() != comparison.getItemDamage()) {
				return false;
			}
		}

		// When the base stackTagCompound is null or empty, treat it as a wildcard for crafting
		if (base.getTagCompound() == null || base.getTagCompound().hasNoTags()) {
			return true;
		} else {
			return ItemStack.areItemStackTagsEqual(base, comparison);
		}
	}
	
	/**
	 * Compare two item stacks for crafting equivalency.
	 */
	public static boolean isCraftingEquivalent(ItemStack base, ItemStack comparison, boolean oreDictionary, boolean craftingTools) {
		if (isCraftingEquivalent(base, comparison, craftingTools)) {
			return true;
		}

		if (oreDictionary) {
			int[] idsBase = OreDictionary.getOreIDs(base);
			Arrays.sort(idsBase);
			int[] idsComp = OreDictionary.getOreIDs(comparison);
			Arrays.sort(idsComp);

			// check if the sorted arrays "idsBase" and "idsComp" have any ID in common.
			int iBase = 0;
			int iComp = 0;
			while (iBase < idsBase.length && iComp < idsComp.length) {
				if (idsBase[iBase] < idsComp[iComp]) {
					iBase++;
				} else if (idsBase[iBase] > idsComp[iComp]) {
					iComp++;
				} else {
					return true;
				}
			}
			return false;
		}

		return false;
	}
	
	/**
	 * Compare two item stacks for crafting equivalency.
	 */
	public static boolean isCraftingEquivalent(ItemStack base, ItemStack comparison, @Nullable String oreDict, boolean craftingTools) {
		if(isCraftingEquivalent(base, comparison, craftingTools)){
			return true;
		}

		if (oreDict != null && !oreDict.isEmpty()) {
			int[] validIds = OreDictionary.getOreIDs(comparison);
			int validID = OreDictionary.getOreID(oreDict);

			for(int id : validIds){
				if (id == validID) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isCraftingEquivalent(ItemStack base, ItemStack comparison, boolean craftingTools) {
		if (base == null || comparison == null) {
			return false;
		}

		if (craftingTools && isCraftingToolEquivalent(base, comparison)) {
			return true;
		}

		if (isCraftingEquivalent(base, comparison)) {
			return true;
		}

		if (base.getTagCompound() != null && !base.getTagCompound().hasNoTags()) {
			if (!ItemStack.areItemStacksEqual(base, comparison)) {
				return false;
			}
		}

		return false;
	}

	public static boolean isCraftingToolEquivalent(ItemStack base, ItemStack comparison) {
		if (base == null || comparison == null) {
			return false;
		}

		Item baseItem = base.getItem();

		if (baseItem != comparison.getItem()) {
			return false;
		}

		if (base.getTagCompound() == null || base.getTagCompound().hasNoTags()) {
			// tool uses meta for damage
			return true;
		} else {
			// tool uses NBT for damage
			if (base.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
				return true;
			}
			return base.getItemDamage() == comparison.getItemDamage();
		}
	}

	public static void dropItemStackAsEntity(ItemStack items, World world, double x, double y, double z) {
		dropItemStackAsEntity(items, world, x, y, z, 10);
	}

	public static void dropItemStackAsEntity(ItemStack items, World world, int x, int y, int z) {
		dropItemStackAsEntity(items, world, x, y, z, 10);
	}

	public static void dropItemStackAsEntity(ItemStack items, World world, double x, double y, double z, int delayForPickup) {
		if (items == null || world.isRemote) {
			return;
		}

		float f1 = 0.7F;
		double d = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5D;
		double d1 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5D;
		double d2 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5D;
		EntityItem entityitem = new EntityItem(world, x + d, y + d1, z + d2, items);
		entityitem.delayBeforeCanPickup = delayForPickup;

		world.spawnEntityInWorld(entityitem);
	}


	public static ItemStack copyWithRandomSize(ItemStack template, int max, Random rand) {
		int size = max <= 0 ? 0 : rand.nextInt(max);
		ItemStack created = template.copy();
		if (size <= 0) {
			created.stackSize = 0;
		} else if (size > created.getMaxStackSize()) {
			created.stackSize = created.getMaxStackSize();
		} else {
			created.stackSize = size;
		}
		return created;
	}

	@Nullable
	public static Block getBlock(ItemStack stack) {
		Item item = stack.getItem();

		if (item instanceof ItemBlock) {
			return Block.getBlockFromItem(item);
		} else {
			return null;
		}
	}

	public static boolean equals(Block block, ItemStack stack) {
		return block == getBlock(stack);
	}

	public static boolean equals(Block block, int meta, ItemStack stack) {
		return block == getBlock(stack) && meta == stack.getItemDamage();
	}

	
	/**
	 * Checks like {@link ItemStack#areItemStacksEqual(ItemStack, ItemStack)}
	 * but ignores stack size (count).
	 */
	public static boolean areItemStacksEqualIgnoreCount(ItemStack a, ItemStack b) {
		if (a == null && b == null) {
			return true;
		} else if (a == null || b == null ) {
			return false;
		} else if (a.getItem() != b.getItem()) {
			return false;
		} else if (a.getItemDamage() != b.getItemDamage()) {
			return false;
		} else if (a.getTagCompound() == null && b.getTagCompound() != null) {
			return false;
		} else {
			return (a.getTagCompound() == null || a.getTagCompound().equals(b.getTagCompound()));
		}
	}
}