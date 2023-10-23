package com.elenai.feathers.effect;

import com.elenai.feathers.api.FeathersHelper;
import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.FeatherSyncSTCPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EnduranceEffect extends MobEffect {

    public EnduranceEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity target, AttributeMap map, int strength) {
        if (target instanceof ServerPlayer player) {
            player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
                f.setEnduranceFeathers((strength + 1) * 8);
                FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(),
                        FeathersHelper.getPlayerWeight(player), f.getEnduranceFeathers()), player);
            });
        }
        super.addAttributeModifiers(target, map, strength);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int strength) {
        if (target instanceof ServerPlayer player) {
            player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
                f.setEnduranceFeathers(0);
                FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(),
                        FeathersHelper.getPlayerWeight(player), f.getEnduranceFeathers()), player);
            });
        }
        super.removeAttributeModifiers(target, map, strength);
    }

}
