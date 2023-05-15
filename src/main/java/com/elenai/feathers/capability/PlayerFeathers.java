package com.elenai.feathers.capability;

import com.elenai.feathers.config.FeathersCommonConfig;

import net.minecraft.nbt.CompoundTag;

public class PlayerFeathers {

	private int feathers = 20;
	private int maxFeathers = 20;
	private int cooldownReduction = 1;
	private final int MIN_FEATHERS = 0;
	
	private int enduranceFeathers = 0;
	
	private int cooldown = 0;
	private int maxCooldown = FeathersCommonConfig.COOLDOWN.get();
	private final int MIN_COOLDOWN = 0;
	
	private boolean cold = false;

	public int getFeathers() {
		return feathers;
	}

	public void setFeathers(int feathers) {
		this.feathers = feathers;
	}

	public int getMaxFeathers() {
		return maxFeathers;
	}

	public void setMaxFeathers(int feathers) {
		this.maxFeathers = feathers;
		if (getFeathers() > feathers) {
			setFeathers(feathers);
		}
	}

	public int getRegen() {
		return this.cooldownReduction;
	}

	public void setRegen(int ticks) {
		this.cooldownReduction = ticks;
	}

	public void addFeathers(int feathers) {
		this.feathers = Math.min(this.feathers + feathers, maxFeathers);
	}

	public void subFeathers(int feathers) {
		this.feathers = Math.max(this.feathers - feathers, MIN_FEATHERS);
	}

	public void copyFrom(PlayerFeathers source) {
		this.feathers = source.feathers;
		this.cooldown = source.cooldown;
		this.enduranceFeathers = source.enduranceFeathers;
		this.cold = source.cold;
	}

	public void saveNBTData(CompoundTag nbt) {
		nbt.putInt("feathers", this.feathers);
		nbt.putInt("max_feathers", this.maxFeathers);
		nbt.putInt("cooldown", this.cooldown);
		nbt.putInt("cooldown_reduction", this.cooldownReduction);
		nbt.putInt("endurance_feathers", this.enduranceFeathers);
		nbt.putBoolean("cold", this.cold);
	}

	public void loadNBTData(CompoundTag nbt) {
		this.feathers = nbt.getInt("feathers");
		this.maxFeathers = nbt.getInt("max_feathers");
		this.cooldown = nbt.getInt("cooldown");
		this.cooldownReduction = nbt.getInt("cooldown_reduction");
		this.enduranceFeathers = nbt.getInt("endurance_feathers");
		this.cold = nbt.getBoolean("cold");
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public void addCooldown(int ticks) {
		this.cooldown = Math.min(this.cooldown + ticks, maxCooldown);
	}

	public void subCooldown(int ticks) {
		this.cooldown = Math.max(this.cooldown - ticks, MIN_COOLDOWN);
	}

	public boolean isCold() {
		return cold;
	}

	public void setCold(boolean cold) {
		this.cold = cold;
	}

	public int getEnduranceFeathers() {
		return enduranceFeathers;
	}

	public void setEnduranceFeathers(int enduranceFeathers) {
		this.enduranceFeathers = enduranceFeathers;
	}
	public void addEndurance(int feathers) {
		this.enduranceFeathers = this.enduranceFeathers + feathers;
	}

	public void subEndurance(int feathers) { //TODO: Put this in the api to access it, we need to ensure if we overlap into regular feathers 
		this.enduranceFeathers = Math.max(this.enduranceFeathers - feathers, 0);
	}
}
