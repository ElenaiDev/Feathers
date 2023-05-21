package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.api.FeathersHelper;
import com.elenai.feathers.networking.FeathersMessages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;

public class RequestWeightCTSPacket {
	
	private final int itemId;
	private final int lightweightLevel;
	private final int heavyLevel;

		public RequestWeightCTSPacket(int itemId, int lightweightLevel, int heavyLevel) {
			this.itemId = itemId;
			this.lightweightLevel = lightweightLevel;
			this.heavyLevel = heavyLevel;
		}

		public RequestWeightCTSPacket(FriendlyByteBuf buf) {
			this.itemId = buf.readInt();
			this.lightweightLevel = buf.readInt();
			this.heavyLevel = buf.readInt();
		}

		public void toBytes(FriendlyByteBuf buf) {
			buf.writeInt(itemId);
			buf.writeInt(lightweightLevel);
			buf.writeInt(heavyLevel);
		}

		public boolean handle(Supplier<NetworkEvent.Context> supplier) {
			NetworkEvent.Context context = supplier.get();
			context.enqueueWork(() -> {
				FeathersMessages.sendToPlayer(new ReplyWithWeightSTCPacket(FeathersHelper.getArmorWeight(Item.byId(itemId), lightweightLevel, heavyLevel)), context.getSender());
			});
			return true;
		}
}
