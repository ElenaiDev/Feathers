package com.elenai.feathers.api;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.client.ClientFeathersData;
import com.elenai.feathers.config.FeathersCommonConfig;
import com.elenai.feathers.enchantment.FeathersEnchantments;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;
import com.elenai.feathers.networking.packet.FeatherSyncCTSPacket;
import com.elenai.feathers.networking.packet.FeatherSyncSTCPacket;
import com.elenai.feathers.util.ArmorHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FeathersHelper {

	/**
	 * Sets the inputed players feathers and syncs them to the client
	 * 
	 * @side server
	 * @param player
	 * @param feathers
	 */
	public static void setFeathers(ServerPlayer player, int feathers) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.setFeathers(feathers);
			f.setCooldown(0);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Returns the given player's feather count
	 * 
	 * @side server
	 * @param player
	 * @return the player's feathers
	 */
	public static int getFeathers(ServerPlayer player) {
		return player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).map(f -> {
			return f.getFeathers();
		}).orElse(0);
	}

	/**
	 * Returns the client player's feather count
	 * 
	 * @side client
	 * @return the player's feathers
	 */
	public static int getFeathers() {
		return ClientFeathersData.getFeathers();
	}
	
	/**
	 * Returns the given player's endurance count
	 * 
	 * @side server
	 * @param player
	 * @return the player's feathers
	 */
	public static int getEndurance(ServerPlayer player) {
		return player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).map(f -> {
			return f.getEnduranceFeathers();
		}).orElse(0);
	}

	/**
	 * Returns the client player's endurance count
	 * 
	 * @side client
	 * @return the player's feathers
	 */
	public static int getEndurance() {
		return ClientFeathersData.getEnduranceFeathers();
	}

	/**
	 * Adds the inputed players feathers to their total and syncs them to the client
	 * 
	 * @side server
	 * @param player
	 * @param feathers
	 */
	public static void addFeathers(ServerPlayer player, int feathers) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.addFeathers(feathers);
			f.setCooldown(0);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Decreases the inputed players feathers from their total and syncs them to the
	 * client
	 * 
	 * NOTE: This differs from spendFeathers as it does not take armor weight into
	 * account and is therefore not recommended, Only use this if you want to drain armor too
	 * 
	 * @side server
	 * @param player
	 * @param feathers
	 */
	public static void subFeathers(ServerPlayer player, int feathers) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.subFeathers(feathers);
			f.setCooldown(0);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Decreases the inputed players feathers + endurance from their total and syncs them to the
	 * client IF the final result is greater than the armor weight, returns whether
	 * it is possible to or not
	 * 
	 * TIP: Use this method at the end of if statements when you wish to spend feathers
	 * 
	 * 
	 * @side server
	 * @param player
	 * @param feathers
	 * @return If the effect was applied
	 */
	public static boolean spendFeathers(ServerPlayer player, int feathers) {

		if(player.isCreative() || player.isSpectator()) { return true; }
		
		if (Math.min(getPlayerWeight(player), 20) <= (getFeathers(player) + getEndurance(player) - feathers)) {
			player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
				
				int amount = f.getEnduranceFeathers()-feathers;
				if(f.getEnduranceFeathers() > 0) {
					f.setEnduranceFeathers(Math.max(0, amount));
				}
				if(amount < 0) {
					f.addFeathers(amount);
				}
				
				f.setCooldown(0);
				FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
			});
			return true;
		}
		return false;
	}
	
	/**
	 * Decreases the inputed players feathers + endurance from their total and syncs them to the
	 * server IF the final result is greater than the armor weight, returns whether
	 * it is possible to or not
	 * 
	 * TIP: Use this method at the end of if statements when you wish to spend feathers
	 * 
	 * 
	 * @side client
	 * @param player
	 * @param feathers
	 * @return If the effect was applied
	 */
	public static boolean spendFeathers(int feathers) {
		
		Minecraft instance = Minecraft.getInstance();
		if (instance.player.isCreative() || instance.player.isSpectator()) { return true; }
		
		if (Math.min(ClientFeathersData.getWeight(), 20) <= (getFeathers() + getEndurance() - feathers)) {
				
				int amount = ClientFeathersData.getEnduranceFeathers()-feathers;
				if(ClientFeathersData.getEnduranceFeathers() > 0) {
					ClientFeathersData.setEnduranceFeathers(Math.max(0, amount));
				}
				if(amount < 0) {
					ClientFeathersData.setFeathers(ClientFeathersData.getFeathers() + amount);
				}
				
				FeathersMessages.sendToServer(new FeatherSyncCTSPacket(ClientFeathersData.getFeathers(), ClientFeathersData.getEnduranceFeathers(), 0));
			return true;
		}
		return false;
	}

	/**
	 * Gets the weight of the given armor item stack, if the item has a weight in
	 * the config, returns that value, if not it returns the item's defence rating
	 * 
	 * @side server
	 * @param item The armor who's weight you wish to get
	 * @return the armor's weight
	 */
	public static int getArmorWeight(Item item, int lightweightLevel) {
		if (item instanceof ArmorItem armor) { //TODO: this
			return Math.max(ArmorHandler.getArmorWeight(armor) - lightweightLevel, 0);
		} else if (item == Items.AIR) {
			return 0;
		}
		Feathers.logger.warn("Attempted to calculate weight of non armor item: " + item.getDescriptionId());
		return 0;
	}

	/**
	 * Gets the total weight of the inputed player based on the armor they are wearing
	 * 
	 * @param player
	 * @return
	 */
	public static int getPlayerWeight(ServerPlayer player) {
		if(!FeathersCommonConfig.ENABLE_ARMOR_WEIGHTS.get()) {
			return 0;
		}
		
		int weight = 0;
		for (ItemStack i : player.getArmorSlots()) {
			weight += getArmorWeight(i.getItem(), 0); // Set as 0 as we work this out on the server as shown below.
		}
		return weight - ArmorHandler.getTotalEnchantmentLevel(FeathersEnchantments.LIGHTWEIGHT.get(), player);
	}
	
	/**
	 * Returns the given player's coldness
	 * 
	 * @side server
	 * @param player
	 * @return if the player is cold
	 */
	public static boolean getCold(ServerPlayer player) {
		return player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).map(f -> {
			return f.isCold();
		}).orElse(false);
	}
	
	/**
	 * Sets the inputed players cold value and syncs it to the client
	 * Remember to always undo this when the condition is no longer met
	 * 
	 * @side server
	 * @param player
	 * @param cold
	 */
	public static void setCold(ServerPlayer player, boolean cold) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.setCold(cold);
			FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(f.isCold()), player);
		});
	}
	
}
