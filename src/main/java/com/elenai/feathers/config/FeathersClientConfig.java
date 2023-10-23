package com.elenai.feathers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FeathersClientConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.ConfigValue<Boolean> REGEN_EFFECT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FROST_SOUND;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DISPLAY_WEIGHTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> VISUAL_WEIGHTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> AFFECTED_BY_RIGHT_HEIGHT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FADE_WHEN_FULL;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> X_OFFSET;
	public static final ForgeConfigSpec.ConfigValue<Integer> Y_OFFSET;

	public static final ForgeConfigSpec.ConfigValue<Integer> FADE_IN_COOLDOWN;
	public static final ForgeConfigSpec.ConfigValue<Integer> FADE_OUT_COOLDOWN;
	public static final ForgeConfigSpec.ConfigValue<Integer> FADE_COOLDOWN;

	static {
		BUILDER.push("Feathers' Config");

		FADE_WHEN_FULL = BUILDER.comment("Fade the HUD overlay if the feathers are full.")
				.define("Fade When Full", false);

		FADE_COOLDOWN = BUILDER.comment("Duration (in ticks) before the HUD overlay fades.")
				.define("Fade Cooldown", 60);

		FADE_IN_COOLDOWN = BUILDER.comment("Duration (in ticks) of the HUD Fade-in animation.")
				.define("Fade-in Duration", 40);

		FADE_OUT_COOLDOWN = BUILDER.comment("Duration (in ticks) of the HUD Fade-out animation.")
				.define("Fade-out Duration", 40);

		REGEN_EFFECT = BUILDER.comment("Whether the feathers flash white when regenerating.")
				.define("Regeneration Effect", true);
		
		FROST_SOUND = BUILDER.comment("Whether a sound is played when feathers freeze in cold biomes")
				.define("Cold Sound Effect", true);
		
		DISPLAY_WEIGHTS = BUILDER.comment("Whether armor weights are displayed when hovering over an item")
				.define("Display Weights in Inventory", true);
		
		VISUAL_WEIGHTS = BUILDER.comment("Whether armor weights are displayed as icons (true) or text (false) when hovering over an item")
				.define("Display Weight As Icons", false);
		
		AFFECTED_BY_RIGHT_HEIGHT = BUILDER.comment("Whether feather icons are affected by icons on the right side of the HUD.")
				.define("Feathers Affected by Right Side Icons", true);
		
		X_OFFSET = BUILDER.comment("How far left or right you want the feathers to be")
				.define("HUD X Offset", 0);
		
		Y_OFFSET = BUILDER.comment("How far up or down you want the feathers to be. TIP: use this for compatibility with mods that add other bars such as thirst")
				.define("HUD Y Offset", 0);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
