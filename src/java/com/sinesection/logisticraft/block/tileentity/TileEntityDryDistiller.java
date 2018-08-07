package com.sinesection.logisticraft.block.tileentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sinesection.logisticraft.api.crafting.IDryDistillerRecipe;
import com.sinesection.logisticraft.block.BlockDryDistiller;
import com.sinesection.logisticraft.crafting.FractionatorRecipeManager;

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
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityDryDistiller extends LogisticraftTileEntity implements ISidedInventory {

	private String localizedName;

	public static final int NUM_SLOTS = 6;

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int[] SLOT_OUTPUTS = {
			2, 3, 4, 5
	};

	// Slot 0 = input
	// Slot 1 = fuel
	// Slot 2-5 = output
	private ItemStack[] slots = new ItemStack[NUM_SLOTS];

	private float efficiency = 0.5f;

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
	public static final int DEFAULT_PROCESS_SPEED = 200;

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
		return this.hasCustomInventoryName() ? this.localizedName : "container.guiDryDistiller.name";
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
		this.currentItemBurnTime = this.getItemBurnTime(this.getStackInSlot(1));
		this.currentItemProcessTime = 0;
		IDryDistillerRecipe recipe = getCurrentRecipe();
		if(recipe != null)
			this.currentItemProcessTime = recipe.getProcessTime();

		String customName = nbt.getString("customName");
		if (!customName.isEmpty()) {
			this.localizedName = customName;
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
					if (this.getStackInSlot(1) != null) {
						this.getStackInSlot(1).stackSize--;
						if (this.getStackInSlot(1).stackSize == 0) {
							this.setInventorySlotContents(1, this.getStackInSlot(1).getItem().getContainerItem(this.getStackInSlot(1)));
						}
						invChanged = true;
					}
				}
			}

			if (isBurning() && canProcess()) {
				if(this.currentItemProcessTime == 0) {
					this.currentItemProcessTime = getCurrentRecipe().getProcessTime();
				}
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
				BlockDryDistiller.updateState(isBurning(), this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}

			if (invChanged) {
				this.markDirty();
			}
		}
	}

	private void process() {
		if (this.canProcess()) {
			IDryDistillerRecipe recipe = getCurrentRecipe();
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
		IDryDistillerRecipe recipe = getCurrentRecipe();
		if (recipe == null)
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
		return getItemBurnTime(itemStack) > 0;
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
	
	private IDryDistillerRecipe getCurrentRecipe() {
		ItemStack slot = getStackInSlot(SLOT_INPUT);
		return FractionatorRecipeManager.findMatchingRecipe(slot);
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

	public int getBurnTimeScaled(int i) {
		if(this.currentItemBurnTime != 0)
			return (this.burnTime * i) / this.currentItemBurnTime;
		return 0;
	}

	public int getProgressScaled(int i) {
		if(this.currentItemProcessTime != 0)
			return (this.processTime * i) / this.currentItemProcessTime;
		return 0;
	}

}
