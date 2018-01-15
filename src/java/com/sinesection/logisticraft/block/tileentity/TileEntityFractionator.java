package com.sinesection.logisticraft.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.sinesection.logisticraft.block.BlockFractionator;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityFractionator extends LogisticraftTileEntity implements ISidedInventory {

	private String localizedName;

	public static final int NUM_SLOTS = 8;
	/**
	 * In MilliBuckets;
	 */
	public static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 4;

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT1 = 2;
	public static final int SLOT_OUTPUT2 = 3;
	public static final int SLOT_OUTPUT3 = 4;
	public static final int SLOT_OUTPUT4 = 5;
	public static final int SLOT_TANK_INPUT = 6;
	public static final int SLOT_TANK_OUTPUT = 7;

	// Slot 0 = input
	// Slot 1 = fuel
	// Slot 2-5 = output
	// Slot 6 = tank input slot
	// Slot 7 = tank output slot
	private ItemStack[] slots = new ItemStack[NUM_SLOTS];
	private FluidTank outputTank = new FluidTank(TANK_CAPACITY);

	private float efficiency = 0.5f;

	// Side 0 = bottom
	// Side 1 = top
	// Side 2-5 = n,s,e,w
	private int[][] accesibleSlotsFromSide = new int[][] {
			{
					1, 2, 3, 4, 5, 7
			}, {
					0, 6
			}, {
					1, 6
			}, {
					1, 6
			}, {
					1, 6
			}, {
					1, 6
			}
	};

	/** Time to process the item in slot 0. (in ticks) */
	private int processSpeed = 200;

	/** Time left for fuel to be used up. (in ticks) */
	public int burnTime;
	/** Time it takes to burn the current item in slot 1. (in ticks) */
	public int currentItemBurnTime;

	/** Time left to process the item in slot 0. (in ticks) */
	public int processTime;

	@Override
	public int getSizeInventory() {
		return NUM_SLOTS;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localizedName : "container.guiFractionator.name";
	}

	public void setGuiDisplayName(String displayName) {
		this.localizedName = displayName;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 0 || slot > getSizeInventory())
			return null;
		return this.slots[slot];
	}

	public boolean incrStackSize(int slot, int amount) {
		ItemStack itemstack = getStackInSlot(slot);

		if (itemstack != null) {
			if (itemstack.stackSize >= itemstack.getMaxStackSize()) {
				itemstack.stackSize = itemstack.getMaxStackSize();
				this.setInventorySlotContents(slot, itemstack);
			} else {
				itemstack.stackSize += amount;
				this.setInventorySlotContents(slot, itemstack);
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		if (this.getStackInSlot(slot) != null) {
			ItemStack slotContents;
			if (this.getStackInSlot(slot).stackSize <= amt) {
				slotContents = this.getStackInSlot(slot);
				this.setInventorySlotContents(slot, null);
				return slotContents;
			} else {
				slotContents = this.getStackInSlot(slot).splitStack(amt);
				if (this.getStackInSlot(slot).stackSize == 0) {
					this.setInventorySlotContents(slot, null);
				}
				return slotContents;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack slotContents = this.getStackInSlot(slot);
		if (slotContents != null) {
			this.setInventorySlotContents(slot, null);
			return slotContents;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot < 0 || slot > getSizeInventory())
			return;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
			itemStack.stackSize = this.getInventoryStackLimit();
		if (itemStack != null && itemStack.stackSize == 0)
			itemStack = null;

		this.slots[slot] = itemStack;
		this.markDirty();
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
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagList list = nbt.getTagList("items", 0);
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte slot = compound.getByte("slot");
			if (slot >= 0 && slot < this.getSizeInventory()) {
				this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(compound));
			}
		}

		this.processTime = nbt.getShort("processTime");
		this.burnTime = nbt.getShort("burnTime");
		this.currentItemBurnTime = this.getItemBurnTime(this.getStackInSlot(1));

		this.outputTank = this.outputTank.readFromNBT(nbt);

		if (!nbt.hasKey("customName")) {
			this.localizedName = nbt.getString("customName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setShort("processTime", (short) this.processTime);
		nbt.setShort("burnTime", (short) this.burnTime);

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.slots.length; i++) {
			if (this.slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("slot", (byte) i);
				this.slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}
		nbt.setTag("items", list);

		this.getOutputTank().writeToNBT(nbt);

		if (this.hasCustomInventoryName()) {
			nbt.setString("customName", this.localizedName);
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public void updateEntity() {
		boolean blockUpdate = isBurning();
		boolean invChanged = false;
		if (!this.worldObj.isRemote) {
			if (isBurning()) {
				this.burnTime--;
			}
			if (this.burnTime == 0 && this.canProcess()) {
				this.currentItemBurnTime = this.burnTime = this.getItemBurnTime(getStackInSlot(1));
				if (this.burnTime > 0) {
					if (this.getStackInSlot(SLOT_FUEL) != null) {
						this.getStackInSlot(SLOT_FUEL).stackSize--;
						if (this.getStackInSlot(SLOT_FUEL).stackSize == 0) {
							this.setInventorySlotContents(SLOT_FUEL, this.getStackInSlot(SLOT_FUEL).getItem().getContainerItem(this.getStackInSlot(SLOT_FUEL)));
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
					invChanged = true;
				}
			} else {
				this.processTime = 0;
			}

			if (blockUpdate != isBurning()) {
				invChanged = true;
				BlockFractionator.updateState(isBurning(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}

			if (invChanged) {
				this.markDirty();
			}
		}
	}

	private void process() {
		if (this.canProcess()) {
			DryDistillerCraftingRecipe recipe = LogisticraftDryDistillerCrafting.getRecipeFromInput(getStackInSlot(0));
			for (int i = 0; i < recipe.outputs.length; i++) {
				ItemStack result = recipe.outputs[i];
				if (this.getStackInSlot(i + 2) == null) {
					this.setInventorySlotContents(i + 2, result.copy());
				} else if (this.getStackInSlot(i + 2).isItemEqual(recipe.outputs[i])) {
					this.getStackInSlot(i + 2).stackSize += recipe.outputs[i].stackSize;
				}
			}
			if (this.getOutputTank().fill(recipe.fluidOutput, false) == recipe.fluidOutput.amount)
				this.getOutputTank().fill(recipe.fluidOutput, true);
			this.getStackInSlot(SLOT_INPUT).stackSize -= recipe.input.stackSize;
			if (this.getStackInSlot(SLOT_INPUT).stackSize <= 0) {
				this.setInventorySlotContents(SLOT_INPUT, null);
			}
		}
	}

	private boolean canProcess() {
		if (getStackInSlot(SLOT_INPUT) == null)
			return false;
		DryDistillerCraftingRecipe recipe = LogisticraftDryDistillerCrafting.getRecipeFromInput(getStackInSlot(SLOT_INPUT));
		if (this.getOutputTank().fill(recipe.fluidOutput, false) != recipe.fluidOutput.amount)
			return false;
		return recipe != null && canOutput(recipe) && (getStackInSlot(SLOT_INPUT).stackSize - recipe.input.stackSize) >= 0;
	}

	private boolean canOutput(DryDistillerCraftingRecipe recipe) {
		boolean canOutput = false;
		if (recipe.outputs.length == 0)
			canOutput = true;
		else
			for (int i = 0; i < recipe.outputs.length; i++) {
				if (getStackInSlot(i + 2) == null) {
					canOutput = true;
				} else {
					int resultAmt = getStackInSlot(i + 2).stackSize + recipe.outputs[i].stackSize;
					if (getStackInSlot(i + 2).isItemEqual(recipe.outputs[i])) {
						if (resultAmt <= getStackInSlot(i + 2).getMaxStackSize() && resultAmt <= getInventoryStackLimit()) {
							canOutput = true;
						}
					}
				}
			}
		if (recipe.fluidOutput != null)
			if (this.getOutputTank().fill(recipe.fluidOutput, false) != recipe.fluidOutput.amount)
				canOutput = false;
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
		return this.getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 6 ? FluidContainerRegistry.isEmptyContainer(itemStack) : (slot > 1 ? false : (slot == 1 ? this.isItemFuel(itemStack) : true));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return this.accesibleSlotsFromSide[side];
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return (slot == 1 && itemStack.isItemEqual(new ItemStack(Items.bucket))) || (slot == 6 && FluidContainerRegistry.isFilledContainer(itemStack)) || (slot == 0);
	}

	public int getBurnTimeScaled(int i) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = this.processSpeed;
		}

		return (this.burnTime * i) / this.currentItemBurnTime;
	}

	public int getProgressScaled(int i) {
		return (this.processTime * i) / this.processSpeed;
	}

	public FluidTank getOutputTank() {
		return this.outputTank;
	}

	public void setOutputTank(FluidTank tank) {
		this.outputTank = tank;
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.getOutputTank().fill(resource, doFill);
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.getOutputTank().drain(resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.getOutputTank().drain(maxDrain, doDrain);
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return this.getOutputTank().getFluidAmount() > 0;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] {
				this.getOutputTank().getInfo()
		};
	}

}
