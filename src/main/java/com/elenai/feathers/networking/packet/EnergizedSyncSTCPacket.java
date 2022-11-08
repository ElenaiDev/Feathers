package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.client.ClientFeathersData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class EnergizedSyncSTCPacket {
	private final boolean energized;
	
	public EnergizedSyncSTCPacket(boolean cold) {
	this.energized = cold;
	}

	public EnergizedSyncSTCPacket(FriendlyByteBuf buf) {
		this.energized = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBoolean(energized);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			ClientFeathersData.setEnergized(energized);
		});
		return true;
	}
}
