package com.sinesection.logisticraft.item;

import java.util.List;

import com.sinesection.logisticraft.Main;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemWheel extends LogisticraftItem {

	private static final String[] WHEEL_TEIRS = new String[] {
			"Wood", "Iron"
	};

	private static final String[] UNLOCALIZED_WHEEL_TEIRS = new String[] {
			"wheelTier_wood", "wheelTier_iron"
	};

	private static final int[] WHEEL_DURABILITES = new int[] {
			500, 1500
	};

	private static final EnumRarity[] WHEEL_RARITIES = new EnumRarity[] {
			EnumRarity.common, EnumRarity.uncommon
	};

	public IIcon[] icons = new IIcon[WHEEL_TEIRS.length];

	public ItemWheel() {
		super("wheel");
		this.setHasSubtypes(true);
		this.setMaxDamage(WHEEL_TEIRS.length);
		this.setMaxStackSize(16);
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < WHEEL_TEIRS.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (!stack.hasTagCompound())
			return this.getItemStackLimit();

		NBTTagCompound nbt = stack.getTagCompound();

		if (nbt.hasKey("wheelDamage")) {
			return nbt.getShort("wheelDamage") > 0 ? 1 : this.getItemStackLimit();
		}
		return this.getItemStackLimit();
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;
		
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey("wheelDamage")) {
			return WHEEL_DURABILITES[stack.getItemDamage()] - nbt.getShort("wheelDamage");
		}
		return 0;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return WHEEL_DURABILITES[stack.getItemDamage()];
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDisplayDamage(stack) > 0;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean bool) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		NBTTagCompound nbt = stack.getTagCompound();
		if (!nbt.hasKey("wheelDamage"))
			nbt.setShort("wheelDamage", (short) WHEEL_DURABILITES[stack.getItemDamage()]);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!stack.hasTagCompound())
			return stack;

		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey("wheelDamage")) {
			int dmg = nbt.getShort("wheelDamage");
			ItemStack retStack = stack.copy();
			if (stack.stackSize > 1) {
				ItemStack tempStack = new ItemStack(stack.getItem(), stack.stackSize - 1, stack.getItemDamage());
				retStack.stackSize = 1;
				tempStack.setTagCompound((NBTTagCompound) nbt.copy());
				if (!player.inventory.addItemStackToInventory(tempStack)) {
					EntityItem entityItem = player.entityDropItem(tempStack, 1.5f);
					world.spawnEntityInWorld(entityItem);
				}

			}

			dmg -= 10;
			nbt.setShort("wheelDamage", (short) dmg);
			retStack.setTagCompound(nbt);
			stack = retStack;
			return retStack;
		}
		return stack;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List infoList, boolean b) {
		if (!stack.hasTagCompound())
			return;

		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey("wheelDamage")) {
			infoList.add(I18n.format(UNLOCALIZED_WHEEL_TEIRS[stack.getItemDamage()]));
			infoList.add(I18n.format("tooltip.durability") + ": " + nbt.getShort("wheelDamage") + " / " + WHEEL_DURABILITES[stack.getItemDamage()]);
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack itemStack) {
		return WHEEL_RARITIES[itemStack.getItemDamage()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iIconRegister) {
		for (int i = 0; i < WHEEL_TEIRS.length; i++) {
			this.icons[i] = iIconRegister.registerIcon(Main.MODID + ":" + this.getUnlocalizedName().substring(5) + WHEEL_TEIRS[i]);
		}
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		if (damage > WHEEL_TEIRS.length)
			damage = 0;
		return this.icons[damage];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + WHEEL_TEIRS[stack.getItemDamage()];
	}

}
