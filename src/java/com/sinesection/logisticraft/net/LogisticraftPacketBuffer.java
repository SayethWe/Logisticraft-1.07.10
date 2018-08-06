package com.sinesection.logisticraft.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class LogisticraftPacketBuffer extends PacketBuffer {
	public LogisticraftPacketBuffer(ByteBuf wrapped) {
		super(wrapped);
	}

	public String readString() {
		try {
			return super.readStringFromBuffer(1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeItemStacks(List<ItemStack> itemStacks) {
		writeVarIntToBuffer(itemStacks.size());
		for (ItemStack stack : itemStacks) {
			try {
				writeItemStackToBuffer(stack);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<ItemStack> readItemStacks() throws IOException {
		int stackCount = readVarIntFromBuffer();
		List<ItemStack> itemStacks = new ArrayList<>();
		for (int i = 0; i < stackCount; i++) {
			itemStacks.add(readItemStackFromBuffer());
		}
		return itemStacks;
	}

	public void writeInventory(IInventory inventory) {
		int size = inventory.getSizeInventory();
		writeVarIntToBuffer(size);

		for (int i = 0; i < size; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			try {
				writeItemStackToBuffer(stack);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void readInventory(IInventory inventory) throws IOException {
		int size = readVarIntFromBuffer();

		for (int i = 0; i < size; i++) {
			ItemStack stack = readItemStackFromBuffer();
			inventory.setInventorySlotContents(i, stack);
		}
	}

	public void writeFluidStack(@Nullable FluidStack fluidStack) {
		if (fluidStack == null) {
			writeVarIntToBuffer(-1);
		} else {
			writeVarIntToBuffer(fluidStack.amount);
			try {
				writeStringToBuffer(fluidStack.getFluid().getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Nullable
	public FluidStack readFluidStack() {
		int amount = readVarIntFromBuffer();
		if (amount > 0) {
			String fluidName = readString();
			Fluid fluid = FluidRegistry.getFluid(fluidName);
			if (fluid == null) {
				return null;
			}

			return new FluidStack(fluid, amount);
		}
		return null;
	}

	public void writeEntityById(Entity entity) {
		writeVarIntToBuffer(entity.getEntityId());
	}

	@Nullable
	public Entity readEntityById(World world) {
		int entityId = readVarIntFromBuffer();
		return world.getEntityByID(entityId);
	}

	public <T extends Enum<T>> void writeEnum(T enumValue, T[] enumValues) {
		if (enumValues.length <= 256) {
			writeByte(enumValue.ordinal());
		} else {
			writeVarIntToBuffer(enumValue.ordinal());
		}
	}

	public <T extends Enum<T>> T readEnum(T[] enumValues) {
		int ordinal;
		if (enumValues.length <= 256) {
			ordinal = readByte();
		} else {
			ordinal = readVarIntFromBuffer();
		}
		return enumValues[ordinal];
	}
	
	public void writeStreamable(@Nullable Object object) {
		if (object != null && object instanceof IStreamable) {
			IStreamable streamable = (IStreamable) object;
			writeBoolean(true);
			streamable.writeData(this);
		} else {
			writeBoolean(false);
		}
	}

	public void writeStreamable(@Nullable IStreamable streamable) {
		if (streamable != null) {
			writeBoolean(true);
			streamable.writeData(this);
		} else {
			writeBoolean(false);
		}
	}

	@Nullable
	public <T extends IStreamable> T readStreamable(IStreamableFactory<T> factory) throws IOException {
		if (readBoolean()) {
			return factory.create(this);
		}
		return null;
	}

	public <T extends IStreamable> void writeStreamables(@Nullable List<T> streamables) {
		if (streamables == null) {
			writeVarIntToBuffer(0);
		} else {
			writeVarIntToBuffer(streamables.size());
			for (IStreamable streamable : streamables) {
				writeStreamable(streamable);
			}
		}
	}

	public <T extends IStreamable> void readStreamables(List<T> outputList, IStreamableFactory<T> factory) throws IOException {
		outputList.clear();
		int length = readVarIntFromBuffer();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				T streamable = readStreamable(factory);
				outputList.add(streamable);
			}
		}
	}
	
	public interface IStreamableFactory<T extends IStreamable> {
		T create(LogisticraftPacketBuffer data) throws IOException;
	}

}