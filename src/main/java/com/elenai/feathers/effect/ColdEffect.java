package com.elenai.feathers.effect;

import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class ColdEffect extends MobEffect{

    public ColdEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    
    @Override
    public void addAttributeModifiers(LivingEntity target, AttributeMap map, int strength) {
      	if(target instanceof ServerPlayer player) {
    		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
    			if (!f.isCold()) {
					f.setCold(true);
					FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(f.isCold()),
							player);
				}
    		});
    	}
    	super.addAttributeModifiers(target, map, strength);
    }
	
    @Override
    public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int strength) {
    	if(target instanceof ServerPlayer player) {
    		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
    			if (f.isCold()) {
					f.setCold(false);
					FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(f.isCold()), player);
				}
    		});
    	}
    	super.removeAttributeModifiers(target, map, strength);
    }
}
