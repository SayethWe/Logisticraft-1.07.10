package com.sinesection.logisticraft.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.sinesection.logisticraft.block.BlockDryDistiller;
import com.sinesection.logisticraft.crafting.DryDistillerCraftingRecipe;
import com.sinesection.logisticraft.crafting.LogisticraftDryDistillerCrafting;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityDryDistiller extends LogisticraftTileEntity implements ISidedInventory {

	private String localizedName;

	// Slot 0 = input
	// Slot 1 = fuel
	// Slot 2-5 = output
	private ItemStack[] slots = new ItemStack[6];

	private float efficiency = 0.5f * 1.5f;

	// Side 0 = bottom
	// Side 1 = top
	// Side 2-5 = n,s,e,w
	private int[][] accesibleSlotsFromSide = new int[][] {
			{
					1, 2, 3, 4, 5
			}, {
					0
			}, {
					1
			}, {
					1
			}, {
					1
			}, {
					1
			}
	};

	/** Time to process the item in slot 0. (in ticks) */
	private int processSpeed = 150;

	/** Time left for fuel to be used up. (in ticks) */
	public int burnTime;
	/** Time it takes to burn the current item in slot 1. (in ticks) */
	public int currentItemBurnTime;

	/** Time left to process the item in slot 0. (in ticks) */
	public int processTime;

	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localizedName : "container.guiDryDistiller.name";
	}

	public void setGuiDisplayName(String displayName) {
		this.localizedName = displayName;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		if(this.slots[slot] != null) {
			ItemStack itemStack;
			if(this.slots[slot].stackSize <= amt) {
				itemStack = this.slots[slot];
				this.slots[slot] = null;
				return itemStack;
			} else {
				itemStack = this.slots[slot].splitStack(amt);
				if(this.slots[slot].stackSize == 0) {
					this.slots[slot] = null;
				}
				return itemStack;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(this.slots[slot] != null) {
			ItemStack itemStack = this.slots[slot];
			this.slots[slot] = null;
			return itemStack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.slots[slot] = itemStack;
		
		if(itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.localizedName != null && this.localizedName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public void updateEntity() {
		
		boolean blockUpdate = isBurning();
		boolean invChanged = false;

		if (!this.worldObj.isRemote) {
			if (isBurning()) {
				this.burnTime--;
			}
			if (this.burnTime == 0 && this.canProcess()) {
				this.currentItemBurnTime = this.burnTime = this.getItemBurnTime(slots[1]);
				if (this.burnTime > 0) {
					if (this.slots[1] != null) {
						this.slots[1].stackSize--;
						if (this.slots[1].stackSize == 0) {
							this.slots[1] = this.slots[1].getItem().getContainerItem(this.slots[1]);
						}
						invChanged = true;
					}
				}
			}

			if (isBurning() && canProcess()) {
				this.processTime++;
				if (this.processTime == this.processSpeed) {
					this.processTime = 0;
					this.process();
				}
			} else {
				this.processTime = 0;
			}

			if (blockUpdate != isBurning()) {
				invChanged = true;
				BlockDryDistiller.updateDryDistillerState(isBurning(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}

			if (invChanged) {
				this.markDirty();
			}
		}
	}

	private void process() {
		if (this.canProcess()) {
			DryDistillerCraftingRecipe recipe = LogisticraftDryDistillerCrafting.getRecipeFromInput(slots[0]);
			for(int i = 0; i < recipe.outputs.length; i++) {
				ItemStack result = recipe.outputs[i];
				if(this.slots[i + 2] == null) {
					this.slots[i + 2] = result.copy();
				} else if(this.slots[i + 2].isItemEqual(recipe.outputs[i])) {
					this.slots[i + 2].stackSize += recipe.outputs[i].stackSize;
				}
				this.slots[0].stackSize -= recipe.input.stackSize;
				if(this.slots[0].stackSize <= 0) {
					this.slots[0] = null;
				}
			}
		}
	}

	private boolean canProcess() {
		if (slots[0] == null)
			return false;
		DryDistillerCraftingRecipe recipe = LogisticraftDryDistillerCrafting.getRecipeFromInput(slots[0]);
		return recipe != null && !recipe.fractionatorRequired && canOutput(recipe) && (slots[0].stackSize - recipe.input.stackSize) >= 0;
	}

	private boolean canOutput(DryDistillerCraftingRecipe recipe) {
		boolean canOutput = false;
		for(int i = 0; i < recipe.outputs.length; i++) {
			if(slots[i + 2] == null) {
				canOutput = true;
			} else {
				int resultAmt = slots[i + 2].stackSize + recipe.outputs[i].stackSize;
				if(slots[i + 2].isItemEqual(recipe.outputs[i])) {
					if(resultAmt <= slots[i + 2].getMaxStackSize() && resultAmt <= getInventoryStackLimit()) {
						canOutput = true;
					}
				}
			}
		}
		return canOutput;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public boolean isRunning() {
		return this.processTime > 0;
	}

	public int getItemBurnTime(ItemStack itemStack) {
		int burnTime = 0;
		if (itemStack == null) {
			burnTime = 0;
		} else {
			List<String> oreDictNames = new ArrayList<String>();
			for (int i : OreDictionary.getOreIDs(itemStack)) {
				oreDictNames.add(OreDictionary.getOreName(i));
			}
			boolean isWood = false;
			for (String oreDictName : oreDictNames) {
				if (oreDictName.toLowerCase().contains("wood")) {
					isWood = true;
					break;
				}
			}

			if (itemStack.getItem() instanceof ItemTool)
				isWood = ((ItemTool) itemStack.getItem()).getToolMaterialName().equalsIgnoreCase("WOOD");
			if (itemStack.getItem() instanceof ItemSword)
				isWood = ((ItemSword) itemStack.getItem()).getToolMaterialName().equalsIgnoreCase("WOOD");
			if (itemStack.getItem() instanceof ItemHoe)
				isWood = ((ItemHoe) itemStack.getItem()).getToolMaterialName().equalsIgnoreCase("WOOD");
			if (itemStack.getItem() == Items.stick)
				isWood = true;
			if (itemStack.getItem() instanceof ItemBlock)
				isWood = ((ItemBlock) itemStack.getItem()).field_150939_a.getMaterial() == Material.wood;
			
			if (isWood) {
				burnTime = 0;
			} else {
				burnTime = GameRegistry.getFuelValue(itemStack);
			}
			
			if (itemStack.getItem() == Items.coal)
				burnTime = 200 * 8;
		}
		return (int) Math.round((float) burnTime * efficiency);
	}

	public boolean isItemFuel(ItemStack itemStack) {
		return getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot > 1 ? false : (slot == 1 ? isItemFuel(itemStack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return accesibleSlotsFromSide[side];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return side != 0 || slot != 1 || itemStack.isItemEqual(new ItemStack(Items.bucket));
	}

}
