package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.client.ClientFeathersData;
import com.elenai.feathers.config.FeathersClientConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

public class ColdSyncSTCPacket {
	private final boolean cold;
	
	public ColdSyncSTCPacket(boolean cold) {
	this.cold = cold;
	}

	public ColdSyncSTCPacket(FriendlyByteBuf buf) {
		this.cold = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBoolean(cold);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ClientFeathersData.setCold(cold);
			if(ClientFeathersData.isCold() && FeathersClientConfig.FROST_SOUND.get()) {
				Minecraft instance = Minecraft.getInstance();
				instance.level.playLocalSound(instance.player.blockPosition(), SoundEvent.createVariableRangeEvent(new ResourceLocation("entity.player.hurt_freeze")),
						SoundSource.PLAYERS, 1f, instance.level.random.nextFloat(), false);
			}
		});
		return true;
	}
}
