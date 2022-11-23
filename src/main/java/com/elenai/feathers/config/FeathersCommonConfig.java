package com.elenai.feathers.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class FeathersCommonConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.ConfigValue<Integer> REGEN_SPEED;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMOR_WEIGHTS;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ARMOR_WEIGHTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_FROST_EFFECTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_LIGHTWEIGHT_ENCHANTMENT;
	
	public static List<String> armorWeightBuilder = new ArrayList<>();

	static {
		BUILDER.push("Feathers' Config");

		REGEN_SPEED = BUILDER.comment("How many ticks it takes to regenerate half a feather.")
				.define("Regeneration Speed", 40);

		/**
		 * Add all current armor types on config creation
		 */
		ForgeRegistries.ITEMS.forEach(i -> {
			if(i.asItem() instanceof ArmorItem armor) {
				int def = armor.getDefense();
				FeathersCommonConfig.armorWeightBuilder.add(i.getDescriptionId() + ":" + def);
			}
		});
		ARMOR_WEIGHTS = BUILDER.comment("How many half feathers each item weighs.").defineList("Armor Weights Override", Lists.newArrayList(armorWeightBuilder), o -> o instanceof String);
		
		ENABLE_ARMOR_WEIGHTS = BUILDER.comment("If enabled, armor types have weight, this reduces the amount of feathers you can use based on how heavy your armor is").define("Enable Armor Weights", true);
		ENABLE_FROST_EFFECTS = BUILDER.comment("Whether feathers freeze in cold biomes. If they do, they don't regenerate until in a different biome")
				.define("Enable Frost In Cold Biomes", false);
		ENABLE_LIGHTWEIGHT_ENCHANTMENT = BUILDER.comment("Whether the Lightweight enchantment can be enhanted in an enchantment table, or if it is treasure only.")
				.define("Enable Lightweight Enchantment in Table", true);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
