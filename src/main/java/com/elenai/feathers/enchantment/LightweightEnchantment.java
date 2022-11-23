package com.elenai.feathers.enchantment;

import com.elenai.feathers.config.FeathersCommonConfig;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LightweightEnchantment extends Enchantment {

	protected LightweightEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slot) {
		super(rarity, category, slot);
	}
	
	@Override
	public int getMaxLevel() {
		return 3;
	}
	
	@Override
	public boolean isTreasureOnly() {
		return FeathersCommonConfig.ENABLE_LIGHTWEIGHT_ENCHANTMENT.get();
	}
}
