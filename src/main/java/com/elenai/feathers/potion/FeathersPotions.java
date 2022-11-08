package com.elenai.feathers.potion;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.effect.FeathersEffects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FeathersPotions {

	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Feathers.MODID);
	
	public static final RegistryObject<Potion> ENDURANCE_POTION = POTIONS.register("endurance_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.ENDURANCE.get(), 2600, 0)));
	public static final RegistryObject<Potion> STRONG_ENDURANCE_POTION = POTIONS.register("strong_endurance_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.ENDURANCE.get(), 2000, 1)));
	public static final RegistryObject<Potion> LONG_ENDURANCE_POTION = POTIONS.register("long_endurance_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.ENDURANCE.get(), 4200, 0)));
	
	public static final RegistryObject<Potion> COLD_POTION = POTIONS.register("cold_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.COLD.get(), 5000, 0)));
	
	public static final RegistryObject<Potion> ENERGIZED_POTION = POTIONS.register("energized_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.ENERGIZED.get(), 1600, 0)));
	public static final RegistryObject<Potion> STRONG_ENERGIZED_POTION = POTIONS.register("strong_energized_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.ENERGIZED.get(), 1000, 1)));
	public static final RegistryObject<Potion> LONG_ENERGIZED_POTION = POTIONS.register("long_energized_potion", () -> new Potion(new MobEffectInstance(FeathersEffects.ENERGIZED.get(), 2600, 0)));

	public static void register(IEventBus eventBus) {
		POTIONS.register(eventBus);
	}
	
}
