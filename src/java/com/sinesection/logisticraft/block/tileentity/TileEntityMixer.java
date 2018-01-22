package com.sinesection.logisticraft.block.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.sinesection.logisticraft.block.BlockFractionator;
import com.sinesection.logisticraft.crafting.DryDistillerCraftingRecipe;
import com.sinesection.logisticraft.crafting.LogisticraftDryDistillerCrafting;
import com.sinesection.logisticraft.crafting.LogisticraftMixerCrafting;
import com.sinesection.logisticraft.crafting.MixerCraftingRecipe;
import com.sinesection.logisticraft.fluid.LogisticraftFluidTank;
import com.sinesection.logisticraft.power.IHeatable;

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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityMixer extends LogisticraftTileEntity implements ISidedInventory, IHeatable {

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
	private static final int[][] accesibleSlotsFromSide = new int[][] {
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

	/** Time to process the item in slot 0. (in ticks) */
	private int processSpeed = 0;

	public float tempature, energy;

	@Override
	public int getSizeInventory() {
		return NUM_SLOTS;
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
		boolean blockUpdate = isBurning();
		boolean invChanged = false;
		if (!this.worldObj.isRemote) {

			for (int i = 0; i < this.inputTanks.length + 1; i++) {
				int inputSlotNum = SLOT_INPUT_TANK_1_I + (i * 2);
				int outputSlotNum = inputSlotNum + 1;
				if (this.getStackInSlot(inputSlotNum) == null)
					continue;
				ItemStack inputSlot = ItemStack.copyItemStack(this.getStackInSlot(inputSlotNum));
				inputSlot.stackSize = 1;
				ItemStack outputSlot = ItemStack.copyItemStack(this.getStackInSlot(outputSlotNum));
				LogisticraftFluidTank tank = this.getOutputTank();
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
					this.setOutputTank(tank);
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

	private void process() {
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
	}

	private boolean canProcess() {
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
		return slot == SLOT_TANK_INPUT ? FluidContainerRegistry.isEmptyContainer(itemStack) : (slot > SLOT_FUEL ? false : (slot == SLOT_FUEL ? this.isItemFuel(itemStack) : true));
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
		return (slot == SLOT_FUEL && itemStack.isItemEqual(new ItemStack(Items.bucket))) || (slot == SLOT_TANK_OUTPUT && FluidContainerRegistry.isFilledContainer(itemStack)) || (slot != SLOT_INPUT);
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

	public LogisticraftFluidTank getOutputTank() {
		return this.outputTank;
	}

	public void setOutputTank(LogisticraftFluidTank tank) {
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

	@Override
	public float getCurrentTemp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRequiredTemp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCurrentEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isReceivingEnergy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLosingEnergy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean takeEnergy(float tempRequested, boolean changeTemp, ForgeDirection side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addEnergy(float tempIn, boolean changeTemp, ForgeDirection side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canInputFrom(ForgeDirection dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canOutputTo(ForgeDirection dir) {
		// TODO Auto-generated method stub
		return false;
	}

}
