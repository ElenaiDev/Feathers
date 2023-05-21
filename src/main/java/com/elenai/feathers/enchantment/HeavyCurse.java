package com.elenai.feathers.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class HeavyCurse extends Enchantment {

	protected HeavyCurse(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slot) {
		super(rarity, category, slot);
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}
}
