package com.elenai.feathers.effect;

import com.elenai.feathers.Feathers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FeathersEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Feathers.MODID);
	
	public static final RegistryObject<MobEffect> ENDURANCE = EFFECTS.register("endurance", () -> new EnduranceEffect(MobEffectCategory.BENEFICIAL, 16776960));
	public static final RegistryObject<MobEffect> COLD = EFFECTS.register("cold", () -> new ColdEffect(MobEffectCategory.HARMFUL, 11993087));
	public static final RegistryObject<MobEffect> ENERGIZED = EFFECTS.register("energized", () -> new EnergizedEffect(MobEffectCategory.BENEFICIAL, 7458303));

	
	public static void register(IEventBus eventBus) {
		EFFECTS.register(eventBus);
	}
}
