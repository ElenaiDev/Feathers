package com.elenai.feathers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elenai.feathers.config.FeathersClientConfig;
import com.elenai.feathers.config.FeathersCommonConfig;
import com.elenai.feathers.effect.FeathersEffects;
import com.elenai.feathers.enchantment.FeathersEnchantments;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.potion.FeathersPotions;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Feathers.MODID)
public class Feathers {
	public static final String MODID = "feathers";
	public static final Logger logger = LogManager.getLogger(MODID);

	public Feathers() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::commonSetup);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FeathersClientConfig.SPEC, "Feathers-Client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FeathersCommonConfig.SPEC, "Feathers-Common.toml");
		
		FeathersEffects.register(modEventBus);
		FeathersPotions.register(modEventBus);
		FeathersEnchantments.register(modEventBus);
		MinecraftForge.EVENT_BUS.register(this);

		

	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			FeathersMessages.register();
		});
	}
}
