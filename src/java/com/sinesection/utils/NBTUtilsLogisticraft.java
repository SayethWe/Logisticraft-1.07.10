package com.sinesection.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ForwardingList;
import com.sinesection.logisticraft.net.IStreamable;
import com.sinesection.logisticraft.net.PacketBufferLogisticraft;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.*;

public abstract class NBTUtilsLogisticraft {

	public enum EnumNBTType {

		END(NBTTagEnd.class),
		BYTE(NBTTagByte.class),
		SHORT(NBTTagShort.class),
		INT(NBTTagInt.class),
		LONG(NBTTagLong.class),
		FLOAT(NBTTagFloat.class),
		DOUBLE(NBTTagDouble.class),
		BYTE_ARRAY(NBTTagByteArray.class),
		STRING(NBTTagString.class),
		LIST(NBTTagList.class),
		COMPOUND(NBTTagCompound.class),
		INT_ARRAY(NBTTagIntArray.class);
		public static final EnumNBTType[] VALUES = values();
		public final Class<? extends NBTBase> classObject;

		EnumNBTType(Class<? extends NBTBase> c) {
			this.classObject = c;
		}
	}

	public static <T extends NBTBase> NBTList<T> getNBTList(NBTTagCompound nbt, String tag, EnumNBTType type) {
		NBTTagList nbtList = nbt.getTagList(tag, type.ordinal());
		return new NBTList<>(nbtList);
	}

	public static class NBTList<T extends NBTBase> extends ForwardingList<T> {

		private final ArrayList<T> backingList;
		private final NBTTagList nbtList;

		public NBTList(NBTTagList nbtList) {
			this.nbtList = nbtList;
			backingList = ObfuscationReflectionHelper.getPrivateValue(NBTTagList.class, nbtList, 1);
		}

		@Override
		protected List<T> delegate() {
			return backingList;
		}

	}

	public static NBTTagCompound writeStreamableToNbt(IStreamable streamable, NBTTagCompound nbt) {
		PacketBufferLogisticraft data = new PacketBufferLogisticraft(Unpooled.buffer());
		streamable.writeData(data);

		byte[] bytes = new byte[data.readableBytes()];
		data.getBytes(0, bytes);
		nbt.setByteArray("dataBytes", bytes);
		return nbt;
	}

	@SideOnly(Side.CLIENT)
	public static void readStreamableFromNbt(IStreamable streamable, NBTTagCompound nbt) {
		if (nbt.hasKey("dataBytes")) {
			byte[] bytes = nbt.getByteArray("dataBytes");
			PacketBufferLogisticraft data = new PacketBufferLogisticraft(Unpooled.wrappedBuffer(bytes));
			try {
				streamable.readData(data);
			} catch (IOException e) {
				Log.error("Failed to read streamable data", e);
			}
		}
	}

}