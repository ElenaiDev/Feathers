package com.elenai.feathers.effect;

import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.EnergizedSyncSTCPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EnergizedEffect extends MobEffect{

    public EnergizedEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    
    @Override
    public void addAttributeModifiers(LivingEntity target, AttributeMap map, int strength) {
    	if(target instanceof ServerPlayer player) {
    		FeathersMessages.sendToPlayer(new EnergizedSyncSTCPacket(true), player);
    	}
    	super.addAttributeModifiers(target, map, strength);
    }
	
    @Override
    public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int strength) {
    	if(target instanceof ServerPlayer player) {
    		FeathersMessages.sendToPlayer(new EnergizedSyncSTCPacket(false), player);
    	}
    	super.removeAttributeModifiers(target, map, strength);
    }
    
    
}
