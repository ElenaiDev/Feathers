package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.client.ClientFeathersData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class FeatherSyncSTCPacket {
	private final int feathers;
	private final int maxFeathers;
	private final int regenRate;
	private final int weight;
	private final int endurance;
	
	public FeatherSyncSTCPacket(int feathers, int maxFeathers, int regenRate, int weight, int endurance) {
	this.feathers = feathers;
	this.maxFeathers = maxFeathers;
	this.regenRate = regenRate;
	this.weight = weight;
	this.endurance = endurance;
	}

	public FeatherSyncSTCPacket(FriendlyByteBuf buf) {
		this.feathers = buf.readInt();
		this.maxFeathers = buf.readInt();
		this.regenRate = buf.readInt();
		this.weight = buf.readInt();
		this.endurance = buf.readInt();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(feathers);
		buf.writeInt(maxFeathers);
		buf.writeInt(regenRate);
		buf.writeInt(weight);
		buf.writeInt(endurance);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ClientFeathersData.setFeathers(feathers);
			ClientFeathersData.setMaxFeathers(maxFeathers);
			ClientFeathersData.setRegenRate(regenRate);
			ClientFeathersData.setWeight(weight);
			ClientFeathersData.setEnduranceFeathers(endurance);
		});
		return true;
	}
}
