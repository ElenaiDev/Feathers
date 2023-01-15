package com.elenai.feathers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FeathersClientConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.ConfigValue<Boolean> REGEN_EFFECT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FROST_SOUND;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_WEIGHTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> VISUAL_WEIGHTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> EFFECTED_BY_RIGHT_HEIGHT;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> X_OFFSET;
	public static final ForgeConfigSpec.ConfigValue<Integer> Y_OFFSET;
	
	static {
		BUILDER.push("Feathers' Config");

		REGEN_EFFECT = BUILDER.comment("Whether the feathers flash white when regenerating.")
				.define("Regeneration Effect", true);
		
		FROST_SOUND = BUILDER.comment("Whether a sound is played when feathers freeze in cold biomes")
				.define("Cold Sound Effect", true);
		
		DISPLAY_WEIGHTS = BUILDER.comment("Whether armor weights are displayed when hovering over an item")
				.define("Display Weights in Inventory", true);
		
		VISUAL_WEIGHTS = BUILDER.comment("Whether armor weights are displayed as icons (true) or text (false) when hovering over an item")
				.define("Display Weight As Icons", false);
		
		EFFECTED_BY_RIGHT_HEIGHT = BUILDER.comment("Whether armor weights are effected by icons on the right side of the HUD.")
				.define("Weight Effected by Right Side Icons", true);
		
		X_OFFSET = BUILDER.comment("How far left or right you want the feathers to be")
				.define("HUD X Offset", 0);
		
		Y_OFFSET = BUILDER.comment("How far up or down you want the feathers to be. TIP: use this for compatibility with mods that add other bars such as thirst")
				.define("HUD Y Offset", 0);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
