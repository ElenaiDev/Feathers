package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.client.ClientFeathersData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class FeatherSyncSTCPacket {
	private final int feathers;
	private final int weight;
	private final int endurance;
	
	public FeatherSyncSTCPacket(int feathers, int weight, int endurance) {
	this.feathers = feathers;
	this.weight = weight;
	this.endurance = endurance;
	}

	public FeatherSyncSTCPacket(FriendlyByteBuf buf) {
		this.feathers = buf.readInt();
		this.weight = buf.readInt();
		this.endurance = buf.readInt();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(feathers);
		buf.writeInt(weight);
		buf.writeInt(endurance);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ClientFeathersData.setFeathers(feathers);
			ClientFeathersData.setWeight(weight);
			ClientFeathersData.setEnduranceFeathers(endurance);
		});
		return true;
	}
}
