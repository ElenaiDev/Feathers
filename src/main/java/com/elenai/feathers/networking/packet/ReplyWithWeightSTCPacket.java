package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.event.ClientEvents;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ReplyWithWeightSTCPacket {
private final int weight;
	
	public ReplyWithWeightSTCPacket(int weight) {
	this.weight = weight;
	}

	public ReplyWithWeightSTCPacket(FriendlyByteBuf buf) {
		this.weight = buf.readInt();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(weight);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ClientEvents.currentWeight = weight;
		});
		return true;
	}
}
