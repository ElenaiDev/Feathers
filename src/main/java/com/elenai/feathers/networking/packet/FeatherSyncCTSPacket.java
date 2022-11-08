package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.capability.PlayerFeathersProvider;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class FeatherSyncCTSPacket {

		private final int feathers;
		private final int endurance;
		private final int cooldown;
		
		public FeatherSyncCTSPacket(int feathers, int endurance, int cooldown) {
		this.feathers = feathers;
		this.endurance = endurance;
		this.cooldown = cooldown;
		}

		public FeatherSyncCTSPacket(FriendlyByteBuf buf) {
			this.feathers = buf.readInt();
			this.endurance = buf.readInt();
			this.cooldown = buf.readInt();
		}

		public void toBytes(FriendlyByteBuf buf) {
			buf.writeInt(feathers);
			buf.writeInt(endurance);
			buf.writeInt(cooldown);
		}

		public boolean handle(Supplier<NetworkEvent.Context> supplier) {
			NetworkEvent.Context context = supplier.get();
			context.enqueueWork(() -> {
				context.getSender().getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
							f.setFeathers(feathers);
							f.setCooldown(cooldown);
							f.setEnduranceFeathers(endurance);
				});

			});
			return true;
		}
}
