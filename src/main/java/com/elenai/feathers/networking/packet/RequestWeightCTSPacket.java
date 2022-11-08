package com.elenai.feathers.networking.packet;

import java.util.function.Supplier;

import com.elenai.feathers.api.FeathersHelper;
import com.elenai.feathers.networking.FeathersMessages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class RequestWeightCTSPacket {
	
	private final int itemId;

		public RequestWeightCTSPacket(int itemId) {
			this.itemId = itemId;
		}

		public RequestWeightCTSPacket(FriendlyByteBuf buf) {
			this.itemId = buf.readInt();
		}

		public void toBytes(FriendlyByteBuf buf) {
			buf.writeInt(itemId);
		}

		public boolean handle(Supplier<NetworkEvent.Context> supplier) {
			NetworkEvent.Context context = supplier.get();
			context.enqueueWork(() -> {
				FeathersMessages.sendToPlayer(new ReplyWithWeightSTCPacket(FeathersHelper.getArmorWeight(new ItemStack(Item.byId(itemId)))), context.getSender());
			});
			return true;
		}
}
