package com.blakebr0.cucumber.guide;

import java.util.List;

import com.blakebr0.cucumber.Cucumber;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.item.ItemBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGuide extends ItemBase implements IModelHelper {

	private Guide guide;
	
	public ItemGuide(String name, CreativeTabs tab, Guide guide) {
		super("guide." + name);
		this.setCreativeTab(tab);
		this.guide = guide;
		
		Guide.addGuideItem(this);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			ItemStack stack = this.guide.makeGuideBookStack(this);
			items.add(stack);
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (player != null && world.isRemote) {
			openGuide(player.getHeldItem(hand));
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag.hasKey("Author")) {
				tooltip.add("Author: " + tag.getString("Author"));
			}
		}
	}
	
	@Override
	public void initModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, ResourceHelper.getModelResource(Cucumber.MOD_ID, "guide", "inventory"));
	}
	
	public Guide getGuide() {
		return this.guide;
	}

	@SideOnly(Side.CLIENT)
	private void openGuide(ItemStack book) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiGuide(book, this.guide));
	}
}