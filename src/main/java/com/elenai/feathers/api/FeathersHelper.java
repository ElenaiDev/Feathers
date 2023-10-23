package com.elenai.feathers.api;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.attributes.FeathersAttributes;
import com.elenai.feathers.capability.PlayerFeathers;
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
	 * Sets the inputted players feathers and syncs them to the client
	 * 
	 * @side server
	 * @param player Player to set feathers for
	 * @param feathers Amount of feathers to set
	 */
	public static void setFeathers(ServerPlayer player, int feathers) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.setFeathers(feathers);
			f.setCooldown(0);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Sets the inputted player's max feathers and syncs them to the client
	 *
	 * @side server
	 * @param player Player to set max feathers for
	 * @param feathers Amount of feathers to set
	 */
	public static void setMaxFeathers(ServerPlayer player, int feathers) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			if (player.getAttributeValue(FeathersAttributes.MAX_FEATHERS.get()) != feathers)
				player.getAttribute(FeathersAttributes.MAX_FEATHERS.get()).setBaseValue(feathers);
			f.setMaxFeathers(feathers);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Sets the inputted player's feather regeneration rate and syncs them to the client
	 *
	 * @side server
	 * @param player Player to set max feathers for
	 * @param ticks Amount of feathers to set
	 */
	public static void setFeatherRegen(ServerPlayer player, int ticks) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.setRegen(ticks);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Returns the given player's feather count
	 * 
	 * @side server
	 * @param player Player from which the feather value is being acquired
	 * @return the player's feathers
	 */
	public static int getFeathers(ServerPlayer player) {
		return player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).map(PlayerFeathers::getFeathers).orElse(0);
	}

	/**
	 * Returns the given player's feather count
	 *
	 * @side server
	 * @param player Player from which the feather value is being acquired
	 * @return the player's feathers
	 */
	public static int getMaxFeathers(ServerPlayer player) {
		return player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).map(PlayerFeathers::getMaxFeathers).orElse(0);
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
	 * Returns the client player's max feather count
	 *
	 * @side client
	 * @return the player's feathers
	 */
	public static int getMaxFeathers() {
		return ClientFeathersData.getMaxFeathers();
	}

	/**
	 * Returns the given player's endurance count
	 * 
	 * @side server
	 * @param player Player whose endurance is being acquired
	 * @return the player's feathers
	 */
	public static int getEndurance(ServerPlayer player) {
		return player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).map(PlayerFeathers::getEnduranceFeathers).orElse(0);
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
	 * Adds the inputted players feathers to their total and syncs them to the client
	 * 
	 * @side server
	 * @param player
	 * @param feathers
	 */
	public static void addFeathers(ServerPlayer player, int feathers) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(f -> {
			f.addFeathers(feathers);
			f.setCooldown(0);
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Decreases the inputted players feathers from their total and syncs them to the
	 * client
	 * <p>
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
			FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
		});
	}

	/**
	 * Decreases the inputted players feathers + endurance from their total and syncs them to the
	 * client IF the final result is greater than the armor weight, returns whether
	 * it is possible to or not
	 * <p>
	 * TIP: Use this method at the end of if statements when you wish to spend feathers
	 * <p>
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
				FeathersMessages.sendToPlayer(new FeatherSyncSTCPacket(f.getFeathers(), f.getMaxFeathers(), f.getRegen(), getPlayerWeight(player), f.getEnduranceFeathers()), player);
			});
			return true;
		}
		return false;
	}
	
	/**
	 * Decreases the inputted players feathers + endurance from their total and syncs them to the
	 * server IF the final result is greater than the armor weight, returns whether
	 * it is possible to or not
	 * <p>
	 * TIP: Use this method at the end of if statements when you wish to spend feathers
	 * <p>
	 * 
	 * @side client
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
					ClientFeathersData.setFadeCooldown(0);
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
	 * Gets the weight of the given armor item, minus the input lightweight level, if the item has a weight in
	 * the config, returns that value, if not it returns the item's defence rating
	 * <p>
	 * This method is for use when sending items as packets to the server
	 * </p>
	 *
	 * @side server
	 * @param item The armor who's weight you wish to get
	 * @return the armor's weight
	 */
	public static int getArmorWeight(Item item, int lightweightLevel, int heavyLevel) {
		if (item instanceof ArmorItem armor) {
			return Math.max(ArmorHandler.getArmorWeight(armor) - lightweightLevel + (heavyLevel * ArmorHandler.getArmorWeight(armor)), 0);
		} else if (item == Items.AIR) {
			return 0;
		}
		return 0;
	}
	
	/**
	 * Gets the weight of the given armor item stack, if the item has a weight in
	 * the config, returns that value, if not it returns the item's defence rating
	 *
	 * @side server
	 * @param itemStack The armor who's weight you wish to get
	 * @return the armor's weight
	 */
	public static int getArmorWeightByStack(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ArmorItem armor) {
			return Math.max(ArmorHandler.getArmorWeight(armor) -
					ArmorHandler.getItemEnchantmentLevel(FeathersEnchantments.LIGHTWEIGHT.get(), itemStack) +
					(ArmorHandler.getItemEnchantmentLevel(FeathersEnchantments.HEAVY.get(), itemStack) * ArmorHandler.getArmorWeight(armor)), 0);
		} else if (itemStack.getItem() == Items.AIR) {
			return 0;
		}
		Feathers.logger.warn("Attempted to calculate weight of non armor item: " + itemStack.getDescriptionId());
		return 0;
	}

	/**
	 * Gets the total weight of the inputted player based on the armor they are wearing
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
			weight += getArmorWeightByStack(i);
		}
		return weight;
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
	 * Sets the inputted players cold value and syncs it to the client
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
	
	/**
	 * Checks whether the player has any feathers remaining
	 * 
	 * @side client
	 * @return Whether the player has feathers to spend
	 */
	public static boolean checkFeathersRemaining() {
		return getFeathers() + getEndurance() > ClientFeathersData.getWeight();
	}
	
}
