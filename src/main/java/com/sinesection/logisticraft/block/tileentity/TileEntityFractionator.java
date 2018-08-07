package com.sinesection.logisticraft.block.tileentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sinesection.logisticraft.api.crafting.IFractionatorRecipe;
import com.sinesection.logisticraft.block.BlockFractionator;
import com.sinesection.logisticraft.crafting.DryDistillerRecipeManager;
import com.sinesection.logisticraft.crafting.FractionatorRecipeManager;
import com.sinesection.logisticraft.fluid.LogisticraftFluidTank;
import com.sinesection.utils.Log;

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

public class TileEntityFractionator extends LogisticraftTileEntity implements ISidedInventory {

	private String localizedName;

	public static final int NUM_SLOTS = 8;
	/**
	 * In MilliBuckets;
	 */
	public static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 4;

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int[] SLOT_OUTPUTS = {
			2, 3, 4, 5
	};
	public static final int SLOT_TANK_INPUT = 6;
	public static final int SLOT_TANK_OUTPUT = 7;

	// Slot 0 = input
	// Slot 1 = fuel
	// Slot 2-5 = output
	// Slot 6 = tank input slot
	// Slot 7 = tank output slot
	private ItemStack[] slots = new ItemStack[NUM_SLOTS];
	private LogisticraftFluidTank outputTank = new LogisticraftFluidTank(TANK_CAPACITY);

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

	/** Default time to process the item in slot 0. (in ticks) */
	public static final int DEFAULT_PROCESS_SPEED = 150;

	/** Time left for fuel to be used up. (in ticks) */
	public int burnTime;
	/** Time it takes to burn the current item in slot 1. (in ticks) */
	public int currentItemBurnTime;
	/** Total time to process the item in slot 0. (in ticks) */
	public int currentItemProcessTime;
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

		NBTTagList list = nbt.getTagList("items", 10);
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
		this.currentItemBurnTime = this.getItemBurnTime(this.getStackInSlot(SLOT_FUEL));
		this.currentItemProcessTime = 0;
		IFractionatorRecipe recipe = getCurrentRecipe();
		if(recipe != null)
			this.currentItemProcessTime = recipe.getProcessTime();

		this.outputTank.readFromNBT(nbt);

		if (!nbt.hasKey("customName")) {
			this.localizedName = nbt.getString("customName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

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

		nbt.setShort("processTime", (short) this.processTime);
		nbt.setShort("burnTime", (short) this.burnTime);

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
				if(this.currentItemProcessTime == 0)
					this.currentItemProcessTime = getCurrentRecipe().getProcessTime();
				this.processTime++;
				if (this.processTime == this.currentItemProcessTime) {
					this.processTime = 0;
					this.process();
					invChanged = true;
				}
			} else {
				this.processTime = 0;
				this.currentItemProcessTime = 0;
			}

			if (blockUpdate != isBurning()) {
				invChanged = true;
				BlockFractionator.updateState(isBurning(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}

			if (this.getStackInSlot(SLOT_TANK_INPUT) == null)
				return;
			ItemStack inputSlot = ItemStack.copyItemStack(this.getStackInSlot(SLOT_TANK_INPUT));
			inputSlot.stackSize = 1;
			ItemStack outputSlot = ItemStack.copyItemStack(this.getStackInSlot(SLOT_TANK_OUTPUT));
			LogisticraftFluidTank tank = this.getOutputTank();
			boolean success = false;
			if (FluidContainerRegistry.isEmptyContainer(inputSlot)) {
				if (tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false) != null && tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {
					if (!addStackToOutput(FluidContainerRegistry.fillFluidContainer(tank.getFluid(), inputSlot), false))
						return;
					outputSlot = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), inputSlot);
					tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
					success = true;
				}

			}
			if (success) {
				this.getStackInSlot(SLOT_TANK_INPUT).stackSize--;
				if (this.getStackInSlot(SLOT_TANK_INPUT).stackSize == 0)
					this.setInventorySlotContents(SLOT_TANK_INPUT, null);
				this.setOutputTank(tank);
				addStackToOutput(outputSlot, true);
				invChanged = true;
			}

			if (invChanged) {
				this.markForUpdate();
				this.markDirty();
			}
		}
	}

	private boolean addStackToOutput(ItemStack stack, boolean doPut) {
		ItemStack output = this.getStackInSlot(SLOT_TANK_OUTPUT);
		if (stack == null) {
			if (doPut)
				this.markDirty();
			return true;
		}
		if (output == null) {
			if (doPut) {
				this.setInventorySlotContents(SLOT_TANK_OUTPUT, stack);
			}
			return true;
		} else if (stack.isItemEqual(output) && (output.stackSize + stack.stackSize) <= output.getMaxStackSize()) {
			if (doPut) {
				this.incrStackSize(SLOT_TANK_OUTPUT, stack.stackSize > 0 ? stack.stackSize : 1);
			}
			return true;
		} else {
			return false;
		}
	}

	private void process() {
		if (this.canProcess()) {
			IFractionatorRecipe recipe = getCurrentRecipe();
			if (recipe instanceof IFractionatorRecipe)
				if (recipe.getFluidProduct() != null)
					if (this.getOutputTank().fill(recipe.getFluidProduct(), false) == recipe.getFluidProduct().amount)
						this.getOutputTank().fill(recipe.getFluidProduct(), true);
			insertStacksIntoOutputSlots(recipe.getProducts(this.worldObj.rand), true);
			this.getStackInSlot(SLOT_INPUT).stackSize -= recipe.getInput().stackSize;
			if (this.getStackInSlot(SLOT_INPUT).stackSize <= 0) {
				this.setInventorySlotContents(SLOT_INPUT, null);
			}
		}
	}

	private boolean canProcess() {
		if (getStackInSlot(SLOT_INPUT) == null)
			return false;
		IFractionatorRecipe recipe = getCurrentRecipe();
		if (recipe instanceof IFractionatorRecipe)
			if (recipe.getFluidProduct() != null)
				if (this.getOutputTank().fill(recipe.getFluidProduct(), false) != recipe.getFluidProduct().amount)
					return false;
		return insertStacksIntoOutputSlots(recipe.getAllProducts().keySet(), false) && (getStackInSlot(SLOT_INPUT).stackSize - recipe.getInput().stackSize) >= 0;
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

	private boolean insertStacksIntoOutputSlots(Collection<ItemStack> itemStacks, boolean doInsert) {
		int yetToInsert = itemStacks.size();
		ItemStack carryOver = null;
		for (ItemStack stack : itemStacks) {
			for (int i = 0; i < SLOT_OUTPUTS.length; i++) {
				int slotNum = SLOT_OUTPUTS[i];
				ItemStack slot = getStackInSlot(slotNum);
				// CARRY OVER
				if(carryOver != null) {
					if (slot == null) {
						if (doInsert)
							setInventorySlotContents(slotNum, carryOver);
						carryOver = null;
						continue;
					} else if (slot.isItemEqual(carryOver)) {
						int result = slot.stackSize + carryOver.stackSize;
						if (result <= slot.getMaxStackSize() && result <= getInventoryStackLimit()) {
							if (doInsert)
								incrStackSize(slotNum, carryOver.stackSize);
							carryOver = null;
							continue;
						} else if (result > slot.getMaxStackSize() && result > getInventoryStackLimit()) {
							int itemsLeft = 0;
							if(slot.getMaxStackSize() >= getInventoryStackLimit()) {
								itemsLeft = result - slot.getMaxStackSize();
							} else {
								itemsLeft = result - getInventoryStackLimit();
							}
							carryOver = new ItemStack(carryOver.getItem(), itemsLeft, carryOver.getItemDamage());
							continue;
						}
					}
				}
				// ITEM INSERTION
				if (slot == null) {
					if (doInsert)
						setInventorySlotContents(slotNum, stack);
					yetToInsert--;
					break;
				} else if (slot.isItemEqual(stack)) {
					int result = slot.stackSize + stack.stackSize;
					Log.info("" + result);
					if (result <= slot.getMaxStackSize() && result <= getInventoryStackLimit()) {
						if (doInsert)
							incrStackSize(slotNum, stack.stackSize);
						yetToInsert--;
						break;
					} else if (result > slot.getMaxStackSize() || result > getInventoryStackLimit()) {
						int itemsLeft = 0;
						if(slot.getMaxStackSize() <= getInventoryStackLimit()) {
							itemsLeft = result - slot.getMaxStackSize();
							if (doInsert)
								setInventorySlotContents(slotNum, new ItemStack(stack.getItem(), slot.getMaxStackSize(), stack.getItemDamage()));
							yetToInsert--;
						} else {
							itemsLeft = result - getInventoryStackLimit();
							if (doInsert)
								setInventorySlotContents(slotNum, new ItemStack(stack.getItem(), getInventoryStackLimit(), stack.getItemDamage()));
							yetToInsert--;
						}
						carryOver = new ItemStack(stack.getItem(), itemsLeft, stack.getItemDamage());
						continue;
					}
				}
			}
		}
		return yetToInsert == 0 && carryOver == null;
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

	private IFractionatorRecipe getCurrentRecipe() {
		ItemStack slot = getStackInSlot(SLOT_INPUT);
		IFractionatorRecipe recipe = FractionatorRecipeManager.findMatchingRecipe(slot);
		if (recipe == null) {
			recipe = (IFractionatorRecipe) DryDistillerRecipeManager.findMatchingRecipe(slot);
			if (recipe == null)
				return null;
		}
		return recipe;
	}

	public int getBurnTimeScaled(int i) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = this.currentItemProcessTime;
		}

		return (this.burnTime * i) / this.currentItemBurnTime;
	}

	public int getProgressScaled(int i) {
		return (this.processTime * i) / this.currentItemProcessTime;
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

}
