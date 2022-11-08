package com.elenai.feathers.util;

import java.util.HashMap;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.config.FeathersCommonConfig;

import net.minecraft.world.item.ArmorItem;

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
}
