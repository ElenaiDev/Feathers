package com.elenai.feathers.event;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.api.FeathersHelper;
import com.elenai.feathers.capability.PlayerFeathers;
import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.config.FeathersCommonConfig;
import com.elenai.feathers.effect.FeathersEffects;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;
import com.elenai.feathers.networking.packet.EnergizedSyncSTCPacket;
import com.elenai.feathers.networking.packet.FeatherSyncSTCPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Feathers.MODID)

public class CommonEvents {

	@SubscribeEvent
	public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (!level.isClientSide && (event.getEntity() instanceof ServerPlayer player)) {
			player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
				FeathersMessages.sendToPlayer(
						new FeatherSyncSTCPacket(f.getFeathers(), FeathersHelper.getPlayerWeight(player), f.getEnduranceFeathers()), player);
				FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(f.isCold()), player);
				FeathersMessages.sendToPlayer(new EnergizedSyncSTCPacket(player.hasEffect(FeathersEffects.ENERGIZED.get())), player);
			});
		}
	}

	/**
	 * Handle the beta cold mechanic here
	 */
	private static void handleFrostEffect(PlayerTickEvent event) {
		if (FeathersCommonConfig.ENABLE_FROST_EFFECTS.get()) {
		Level level = event.player.level;
			if ((event.player.isInPowderSnow || event.player.wasInPowderSnow
					|| level.getBiome(event.player.blockPosition()).get()
							.coldEnoughToSnow(event.player.blockPosition()))) { //TODO: Make fire stop ice effect AND make this a potion effect instead
				if(!event.player.hasEffect(FeathersEffects.COLD.get()) || event.player.getActiveEffectsMap().get(FeathersEffects.COLD.get()).getDuration() < 1000) {
					event.player.addEffect(new MobEffectInstance(FeathersEffects.COLD.get(), 999999, 0, false, true));
				}
			} else if (event.player.hasEffect(FeathersEffects.COLD.get()) && event.player.getActiveEffectsMap().get(FeathersEffects.COLD.get()).getDuration() > 201){
				event.player.removeEffect(FeathersEffects.COLD.get());
				event.player.addEffect(new MobEffectInstance(FeathersEffects.COLD.get(), 200, 0, false, true));
			}
		}
	}
	
	/**
	 * Handle the Endurance mechanic here, where the potion leaves if the player has no endurance feathers left
	 */
	private static void handleEnduranceEffect(PlayerTickEvent event) {
		if(event.player.hasEffect(FeathersEffects.ENDURANCE.get()) && FeathersHelper.getEndurance((ServerPlayer) event.player) == 0) {
			event.player.removeEffect(FeathersEffects.ENDURANCE.get());
		}
	}
	
	/**
	 * Regenerate the player's feathers, taking the energized potion into account
	 * @param event
	 */
	private static void regenerateFeathers(PlayerTickEvent event) {
		event.player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			
			if (f.getFeathers() < 20 && (!f.isCold())) {
				
				if(!event.player.hasEffect(FeathersEffects.ENERGIZED.get())) {
					f.addCooldown(1);
				} else {
					f.addCooldown(Math.min(event.player.getActiveEffectsMap().get(FeathersEffects.ENERGIZED.get()).getAmplifier()+5, FeathersCommonConfig.REGEN_SPEED.get()));
				}
				
			}
			if (f.getCooldown() >= FeathersCommonConfig.REGEN_SPEED.get()) {
				FeathersHelper.addFeathers((ServerPlayer) event.player, 1);
			}
		});
	}
	
	@SubscribeEvent
	public static void playerTickEvent(PlayerTickEvent event) {
		if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
			regenerateFeathers(event);
			handleFrostEffect(event);
			handleEnduranceEffect(event);
		}
	}

	@SubscribeEvent
	public static void onPlayerChangeArmor(LivingEquipmentChangeEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getSlot().getType() == EquipmentSlot.Type.ARMOR) {
			player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
				FeathersMessages.sendToPlayer(
						new FeatherSyncSTCPacket(f.getFeathers(), FeathersHelper.getPlayerWeight(player), f.getEnduranceFeathers()), player);
			});
		}
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			if (!event.getObject().getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).isPresent()) {
				event.addCapability(new ResourceLocation(Feathers.MODID, "properties"), new PlayerFeathersProvider());
			}
		}
	}

	//TODO: Repair this
	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		if (!event.isWasDeath()) {
			event.getOriginal().getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(oldStore -> {
				event.getOriginal().getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(newStore -> {
					newStore.copyFrom(oldStore);
				});
			});
		}
	}

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.register(PlayerFeathers.class);
	}

}