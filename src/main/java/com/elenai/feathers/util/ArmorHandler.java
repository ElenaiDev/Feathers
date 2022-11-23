package com.elenai.feathers.util;

import java.util.HashMap;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.config.FeathersCommonConfig;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ArmorHandler {
	private static HashMap<String, Integer> map = new HashMap<>();

	public static HashMap<String, Integer> getWeights() {
		if (map.isEmpty()) {
			populateWeights();
		}
		return map;
	}
	
	public static void populateWeights() {
		map.clear();
		FeathersCommonConfig.ARMOR_WEIGHTS.get().forEach(value -> {
			String[] split = value.split(":");
			try {
				map.putIfAbsent(split[0], Integer.parseInt(split[1]));
			} catch (Exception e) {
				Feathers.logger.warn(e + " error! Armor value not set as an integer.");
			}
		});
	}
	
	public static int getArmorWeight(ArmorItem armor) {
		return getWeights().getOrDefault(armor.getDescriptionId(), armor.getDefense());
	}
	
	/**
	 * Returns the cumulative total of an equipped enchantment type.
	 * 
	 * @author Diesieben07
	 * @param enchantment
	 * @param entity
	 * @return
	 */
	public static int getTotalEnchantmentLevel(Enchantment enchantment, LivingEntity entity) {
		Iterable<ItemStack> iterable = enchantment.getSlotItems(entity).values();
		if (iterable == null) {
			return 0;
		} else {
			int i = 0;
			for (ItemStack itemstack : iterable) {
				int j = itemstack.getEnchantmentLevel(enchantment);
				i += j;
			}
			return i;
		}
	}
	
	/**
	 * Returns the total of an item enchantment type.
	 * 
	 * @author Elenai
	 * @param enchantment
	 * @param entity
	 * @return
	 */
	public static int getItemEnchantmentLevel(Enchantment enchantment, ItemStack itemstack) {
			return itemstack.getEnchantmentLevel(enchantment);
	}
}
