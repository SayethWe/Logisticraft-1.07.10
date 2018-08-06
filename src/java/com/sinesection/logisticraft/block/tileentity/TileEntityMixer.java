package com.sinesection.logisticraft.block.tileentity;

import com.sinesection.logisticraft.fluid.LogisticraftFluidTank;
import com.sinesection.logisticraft.power.IHeatable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMixer extends LogisticraftTileEntity implements ISidedInventory, IFluidHandler, IHeatable {

	private String localizedName;

	public static final int NUM_SLOTS = 12;
	public static final int NUM_INPUT_TANKS = 4;
	/**
	 * In MilliBuckets;
	 */
	public static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 4;

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_OUTPUT = 1;
	public static final int SLOT_INPUT_TANK_1_I = 2;
	public static final int SLOT_INPUT_TANK_1_O = 3;
	public static final int SLOT_INPUT_TANK_2_I = 4;
	public static final int SLOT_INPUT_TANK_2_O = 5;
	public static final int SLOT_INPUT_TANK_3_I = 6;
	public static final int SLOT_INPUT_TANK_3_O = 7;
	public static final int SLOT_INPUT_TANK_4_I = 8;
	public static final int SLOT_INPUT_TANK_4_O = 9;
	public static final int SLOT_OUTPUT_TANK_I = 10;
	public static final int SLOT_OUTPUT_TANK_O = 11;

	public static final int TANK_INDEX_OUTPUT = 0;
	public static final int TANK_INDEX_INPUT_1 = 1;
	public static final int TANK_INDEX_INPUT_2 = 2;
	public static final int TANK_INDEX_INPUT_3 = 3;
	public static final int TANK_INDEX_INPUT_4 = 4;

	// Slot 0 = input
	// Slot 1 = fuel
	// Slot 2-5 = output
	// Slot 6 = tank input slot
	// Slot 7 = tank output slot
	private ItemStack[] slots = new ItemStack[NUM_SLOTS];
	private LogisticraftFluidTank[] inputTanks = new LogisticraftFluidTank[NUM_INPUT_TANKS];
	private LogisticraftFluidTank outputTank = new LogisticraftFluidTank(TANK_CAPACITY);

	// Side 0 = bottom
	// Side 1 = top
	// Side 2-5 = n,s,e,w
	private final int[][] accesibleSlotsFromSide = new int[][] {
			{
					SLOT_OUTPUT, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_2_O, SLOT_INPUT_TANK_3_O, SLOT_INPUT_TANK_4_O, SLOT_OUTPUT_TANK_O
			}, {
					SLOT_INPUT, SLOT_OUTPUT, SLOT_INPUT_TANK_1_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_2_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_3_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_4_I, SLOT_INPUT_TANK_1_O, SLOT_OUTPUT_TANK_I, SLOT_INPUT_TANK_1_O
			}, {
					SLOT_INPUT, SLOT_OUTPUT, SLOT_INPUT_TANK_1_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_2_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_3_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_4_I, SLOT_INPUT_TANK_1_O, SLOT_OUTPUT_TANK_I, SLOT_INPUT_TANK_1_O
			}, {
					SLOT_INPUT, SLOT_OUTPUT, SLOT_INPUT_TANK_1_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_2_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_3_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_4_I, SLOT_INPUT_TANK_1_O, SLOT_OUTPUT_TANK_I, SLOT_INPUT_TANK_1_O
			}, {
					SLOT_INPUT, SLOT_OUTPUT, SLOT_INPUT_TANK_1_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_2_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_3_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_4_I, SLOT_INPUT_TANK_1_O, SLOT_OUTPUT_TANK_I, SLOT_INPUT_TANK_1_O
			}, {
					SLOT_INPUT, SLOT_OUTPUT, SLOT_INPUT_TANK_1_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_2_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_3_I, SLOT_INPUT_TANK_1_O, SLOT_INPUT_TANK_4_I, SLOT_INPUT_TANK_1_O, SLOT_OUTPUT_TANK_I, SLOT_INPUT_TANK_1_O
			}
	};

	// TODO Have all tanks fillable from all sides, for now
	private final int[][] fillableTanksFromSide = new int[][] {
			{
					TANK_INDEX_INPUT_1, TANK_INDEX_INPUT_2, TANK_INDEX_INPUT_3, TANK_INDEX_INPUT_4
			}, {
					TANK_INDEX_INPUT_1, TANK_INDEX_INPUT_2, TANK_INDEX_INPUT_3, TANK_INDEX_INPUT_4
			}, {
					TANK_INDEX_INPUT_1, TANK_INDEX_INPUT_2, TANK_INDEX_INPUT_3, TANK_INDEX_INPUT_4
			}, {
					TANK_INDEX_INPUT_1, TANK_INDEX_INPUT_2, TANK_INDEX_INPUT_3, TANK_INDEX_INPUT_4
			}, {
					TANK_INDEX_INPUT_1, TANK_INDEX_INPUT_2, TANK_INDEX_INPUT_3, TANK_INDEX_INPUT_4
			}, {
					TANK_INDEX_INPUT_1, TANK_INDEX_INPUT_2, TANK_INDEX_INPUT_3, TANK_INDEX_INPUT_4
			}
	};

	// TODO Have output tank drainable from all sides, for now
	private final int[][] drainableTanksFromSide = new int[][] {
			{
					TANK_INDEX_OUTPUT
			}, {
					TANK_INDEX_OUTPUT
			}, {
					TANK_INDEX_OUTPUT
			}, {
					TANK_INDEX_OUTPUT
			}, {
					TANK_INDEX_OUTPUT
			}, {
					TANK_INDEX_OUTPUT
			}
	};

	/** Time to process the item in slot 0. (in ticks) */
	public int processSpeed = 1000;
	public int processTime = 0;

	public float tempature, energy, maxTemperature;

	@Override
	public int getSizeInventory() {
		return NUM_SLOTS;
	}

	public int getNumTanks() {
		return NUM_INPUT_TANKS + 1;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.localizedName : "container.guiMixer.name";
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

		NBTTagList list = nbt.getTagList("items", 10);
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte slot = compound.getByte("slot");
			if (slot >= 0 && slot < this.getSizeInventory()) {
				this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(compound));
			}
		}

		this.outputTank.readFromNBT(nbt);

		list = nbt.getTagList("tanks", 10);
		this.inputTanks = new LogisticraftFluidTank[NUM_INPUT_TANKS];

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte index = compound.getByte("index");
			if (index >= 0 && index < this.getSizeInventory()) {
				this.inputTanks[index].readFromNBT(compound);
			}
		}

		if (!nbt.hasKey("customName")) {
			this.localizedName = nbt.getString("customName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < this.slots.length; i++) {
			if (this.slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("slot", (byte) i);
				this.slots[i].writeToNBT(compound);
				itemList.appendTag(compound);
			}
		}
		nbt.setTag("items", itemList);

		this.outputTank.writeToNBT(nbt);

		NBTTagList tankList = new NBTTagList();
		for (int i = 0; i < this.inputTanks.length; i++) {
			if (this.inputTanks[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("index", (byte) i);
				this.inputTanks[i].writeToNBT(compound);
				tankList.appendTag(compound);
			}
		}
		nbt.setTag("tanks", tankList);

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
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.func_148857_g();
		readFromNBT(compound);
		this.markDirty();
		this.markForUpdate();
	}

	@Override
	public void updateEntity() {
		//boolean blockUpdate = isRunning();
		boolean invChanged = false;
		if (!this.worldObj.isRemote) {

			for (int i = 0; i < this.getNumTanks(); i++) {
				int inputSlotNum = SLOT_INPUT_TANK_1_I + (i * 2);
				int outputSlotNum = inputSlotNum + 1;
				if (this.getStackInSlot(inputSlotNum) == null)
					continue;
				ItemStack inputSlot = ItemStack.copyItemStack(this.getStackInSlot(inputSlotNum));
				inputSlot.stackSize = 1;
				ItemStack outputSlot = ItemStack.copyItemStack(this.getStackInSlot(outputSlotNum));
				LogisticraftFluidTank tank = this.getTank(i);
				boolean success = false;
				if (FluidContainerRegistry.isEmptyContainer(inputSlot)) {
					if (tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {
						if (!addStackToOutput(FluidContainerRegistry.fillFluidContainer(tank.getFluid(), inputSlot), outputSlotNum, false))
							continue;
						outputSlot = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), inputSlot);
						tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
						success = true;
					}
				}
				if (success) {
					this.getStackInSlot(inputSlotNum).stackSize--;
					if (this.getStackInSlot(inputSlotNum).stackSize == 0)
						this.setInventorySlotContents(inputSlotNum, null);
					this.setTank(i, tank);
					addStackToOutput(outputSlot, outputSlotNum, true);
					invChanged = true;
				}
			}

			if (invChanged) {
				this.markForUpdate();
				this.markDirty();
			}
		}
	}

	private boolean addStackToOutput(ItemStack stack, int slot, boolean doPut) {
		ItemStack output = this.getStackInSlot(slot);
		if (stack == null) {
			if (doPut)
				this.markDirty();
			return true;
		}
		if (output == null) {
			if (doPut) {
				this.setInventorySlotContents(slot, stack);
			}
			return true;
		} else if (stack.isItemEqual(output) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()) {
			if (doPut) {
				this.incrStackSize(slot, stack.stackSize > 0 ? stack.stackSize : 1);
			}
			return true;
		} else {
			return false;
		}
	}

/** TODO	private void process() {
		
		if (this.canProcess()) {
			MixerCraftingRecipe recipe = LogisticraftMixerCrafting.getRecipeFromInput(null, getStackInSlot(0));
			for (int i = 0; i < recipe.outputs.length; i++) {
				ItemStack result = recipe.outputs[i];
				if (this.getStackInSlot(i + 2) == null) {
					this.setInventorySlotContents(i + 2, result.copy());
				} else if (this.getStackInSlot(i + 2).isItemEqual(recipe.outputs[i])) {
					this.getStackInSlot(i + 2).stackSize += recipe.outputs[i].stackSize;
				}
			}
			if (recipe.hasLiquidOutput())
				if (this.getOutputTank().fill(recipe.fluidOutput, false) == recipe.fluidOutput.amount)
					this.getOutputTank().fill(recipe.fluidOutput, true);
			this.getStackInSlot(SLOT_INPUT).stackSize -= recipe.input.stackSize;
			if (this.getStackInSlot(SLOT_INPUT).stackSize <= 0) {
				this.setInventorySlotContents(SLOT_INPUT, null);
			}
		} 
	}*/

/** TODO	private boolean canProcess() {
		
		if (getStackInSlot(SLOT_INPUT) == null)
			return false;
		DryDistillerCraftingRecipe recipe = LogisticraftDryDistillerCrafting.getRecipeFromInput(getStackInSlot(SLOT_INPUT));
		if (recipe == null)
			return false;
		if (recipe.hasLiquidOutput())
			if (this.getOutputTank().fill(recipe.fluidOutput, false) != recipe.fluidOutput.amount)
				return false;
		return canOutput(recipe) && (getStackInSlot(SLOT_INPUT).stackSize - recipe.input.stackSize) >= 0;
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
		if (recipe.fluidOutput != null && recipe.fluidOutput.amount > 0)
			if (this.getOutputTank().fill(recipe.fluidOutput, false) != recipe.fluidOutput.amount)
				canOutput = false;
		return canOutput;
		
	}*/

	public boolean isRunning() {
		return this.processTime > 0;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return false; // TODO slot == SLOT_TANK_INPUT ? FluidContainerRegistry.isEmptyContainer(itemStack) : (slot > SLOT_FUEL ? false : (slot == SLOT_FUEL ? this.isItemFuel(itemStack) : true));
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
		return false; //(slot == SLOT_FUEL && itemStack.isItemEqual(new ItemStack(Items.bucket))) || (slot == SLOT_TANK_OUTPUT && FluidContainerRegistry.isFilledContainer(itemStack)) || (slot != SLOT_INPUT);
	}

	public int getTempatureScaled(int i) {
		if (this.maxTemperature == 0) 
			return 0;

		return Math.round((this.tempature * i) / this.maxTemperature);
	}

	public int getProgressScaled(int i, int j) {
		if(this.processSpeed == 0)
			return 0;
		
		return (this.processTime * i) / this.processSpeed;
	}

	public LogisticraftFluidTank getOutputTank() {
		return this.outputTank;
	}

	public void setOutputTank(LogisticraftFluidTank tank) {
		this.outputTank = tank;
	}

	public LogisticraftFluidTank getTank(int index) {
		if (getNumTanks() == 0)
			return this.outputTank;
		return this.inputTanks[getNumTanks() - 1];
	}

	public void setTank(int i, LogisticraftFluidTank tank) {
		this.outputTank = tank;
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int side = from.ordinal();
		int[] tankIndexes = drainableTanksFromSide[side];
		for (int j = 0; j < tankIndexes.length; j++) {
			int tankIndex = tankIndexes[j];
			LogisticraftFluidTank ft = getTank(tankIndex);
			if(resource.amount + resource.amount <= ft.getCapacity())
				return ft.fill(resource, doFill);
		}
		return 0;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		int side = from.ordinal();
		int[] tankIndexes = drainableTanksFromSide[side];
		for (int j = 0; j < tankIndexes.length; j++) {
			int tankIndex = tankIndexes[j];
			LogisticraftFluidTank ft = getTank(tankIndex);
			if (ft.getFluidAmount() > 0)
				return ft.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		int side = from.ordinal();
		int[] tankIndexes = fillableTanksFromSide[side];
		for (int j = 0; j < tankIndexes.length; j++) {
			int tankIndex = tankIndexes[j];
			LogisticraftFluidTank ft = getTank(tankIndex);
			if (ft.getFluid() == null || ft.getFluid().getFluid() == fluid)
				return true;
		}
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		int side = from.ordinal();
		int[] tankIndexes = drainableTanksFromSide[side];
		for (int j = 0; j < tankIndexes.length; j++) {
			int tankIndex = tankIndexes[j];
			LogisticraftFluidTank ft = getTank(tankIndex);
			if (ft.getFluidAmount() > 0)
				if (ft.getFluid() == null || ft.getFluid().getFluid() == fluid)
					return true;
		}
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] fti = new FluidTankInfo[getNumTanks()];
		for (int i = 0; i < getNumTanks(); i++) {
			fti[i] = new FluidTankInfo(getTank(i));
		}
		return fti;
	}

	@Override
	public float getCurrentTemp() {
		return tempature;
	}

	@Override
	public float getRequiredTemp() {
		return 0;
	}

	@Override
	public float getCurrentEnergy() {
		return energy;
	}

	@Override
	public float getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean isReceivingEnergy() {
		return false;
	}

	@Override
	public boolean isLosingEnergy() {
		return false;
	}

	@Override
	public boolean takeEnergy(float tempRequested, boolean changeTemp, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean addEnergy(float tempIn, boolean changeTemp, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean canInputFrom(ForgeDirection side) {
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection dir) {
		return false;
	}

}
