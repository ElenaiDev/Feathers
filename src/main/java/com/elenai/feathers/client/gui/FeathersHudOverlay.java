package com.elenai.feathers.client.gui;

import com.elenai.feathers.Feathers;
import com.elenai.feathers.client.ClientFeathersData;
import com.elenai.feathers.config.FeathersClientConfig;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FeathersHudOverlay {

	public final static ResourceLocation ICONS = new ResourceLocation(Feathers.MODID, "textures/gui/icons.png");
	public final static int NONE = 16;
	public final static int FULL = 34;
	public final static int HALF = 25;
	public final static int ARMORED = 52;
	public final static int HALF_ARMORED = 43;

	public static int k = 0;
	public static float alpha = 1.0f;

	/**
	 * Renders the Feathers to the hotbar
	 */
	public static final IGuiOverlay FEATHERS = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {

		int fadeCooldown = FeathersClientConfig.FADE_COOLDOWN.get();
		int fadeIn = FeathersClientConfig.FADE_IN_COOLDOWN.get();
		int fadeOut = FeathersClientConfig.FADE_OUT_COOLDOWN.get();
		int xOffset = FeathersClientConfig.X_OFFSET.get();
		int yOffset = FeathersClientConfig.Y_OFFSET.get();
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;

		int x = screenWidth / 2;
		int y = screenHeight;

		int rightOffset = FeathersClientConfig.AFFECTED_BY_RIGHT_HEIGHT.get() ? gui.rightHeight : 0;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
		RenderSystem.setShaderTexture(0, ICONS);

		if (!player.isSpectator() && !player.isCreative()) {

			if (FeathersClientConfig.FADE_WHEN_FULL.get()) {
				if (ClientFeathersData.getFeathers() == ClientFeathersData.getMaxFeathers()) {
					if (alpha != 0 & ClientFeathersData.getFadeCooldown() == fadeCooldown) {
						alpha = Math.max((alpha - 1.0f / fadeOut), 0.0f);
					}
				} else {
					if (alpha < 1.0f) {
						alpha = Math.min((alpha + 1.0f / fadeIn), 1.0f);
					}
				}
			}

			if (alpha == 0) return;

			/*
			 * Always render the background up to the maximum feather amount
			 */
			for (int i = 0; i < 10; i++) {
				if ((i + 1 <= Math.ceil((double) ClientFeathersData.getMaxFeathers() / 2.0d))) {
					int cold = ((ClientFeathersData.isCold()) ? 18 : 0);
					int height = (k > i * 10 && k < (i + 1) * 10) ? 2 : 0;
					GuiComponent.blit(poseStack, x + 81 - (i * 8) + xOffset, y - rightOffset - height + yOffset, NONE,
							cold, 9, 9, 256, 256);
				}
			}

			/*
			 * Only render the currently active feathers
			 */
			for (int i = 0; i < 10; i++) {
				if ((i + 1 <= Math.ceil((double) ClientFeathersData.getFeathers() / 2.0d))
						&& ClientFeathersData.getFeathers() > 0) {
					// Check if feather is half or full
					int type = ((i + 1 == Math.ceil((double) ClientFeathersData.getFeathers() / 2.0d)
							&& (ClientFeathersData.getFeathers() % 2 != 0)) ? HALF : FULL);

					int height = (k > i * 10 && k < (i + 1) * 10) ? 2 : 0;

					int cold = ((ClientFeathersData.isCold()) ? 18 : 0);
					GuiComponent.blit(poseStack, x + 81 - (i * 8) + xOffset, y- rightOffset - height + yOffset,
							type, cold, 9, 9, 256, 256);
				} else {
					break;
				}
			}

			/*
			 * Only render the currently worn armor
			 */
			for (int i = 0; i < 10; i++) {
				if ((i + 1 <= Math.ceil((double) ClientFeathersData.getWeight() / 2.0d))
						&& ClientFeathersData.getWeight() > 0
						&& (i + 1 <= Math.ceil((double) ClientFeathersData.getFeathers() / 2.0d))) {
					int height = (k > i * 10 && k < (i + 1) * 10) ? 2 : 0;

					// Check if feather is half or full
					int type = ((i + 1 == Math.ceil((double) ClientFeathersData.getWeight() / 2.0d)
							&& (ClientFeathersData.getWeight() % 2 != 0)) ? HALF_ARMORED : ARMORED);

					int lowerFeathers = (i >= Math.floor((double) ClientFeathersData.getFeathers() / 2.0d)) ? 9 : 0;

					GuiComponent.blit(poseStack, x + 81 - (i * 8) + xOffset, y - rightOffset - height + yOffset,
							type, lowerFeathers, 9, 9, 256, 256);
				} else {
					break;
				}
			}

			/*
			 * Render the Regeneration effect
			 */
			for (int i = 0; i < 10; i++) {
				if (ClientFeathersData.getAnimationCooldown() >= 18
						|| ClientFeathersData.getAnimationCooldown() == 10) {
					if ((i + 1 <= Math.ceil((double) ClientFeathersData.getMaxFeathers() / 2.0d))) {
						int height = (k > i * 10 && k < (i + 1) * 10) ? 2 : 0;
						GuiComponent.blit(poseStack, x + 81 - (i * 8) + xOffset, y - rightOffset - height + yOffset,
								NONE, 9, 9, 9, 256, 256);
					}
				}
			}

			if (ClientFeathersData.isEnergized()) {
				if (k == 100) {
					k = -40;
				} else {
					k += 2;
				}
			} else if (k != 0) {
				k = 0;
			}
		}

		if(FeathersClientConfig.AFFECTED_BY_RIGHT_HEIGHT.get()) {
			rightOffset += 10;
		}

		int lines = 0;

		/*
		 * Only render the currently active endurance feathers by line
		 */
		for (int i = 0; i < Math.ceil((double) ClientFeathersData.getEnduranceFeathers() / 20.0d); i++) { //TODO: fix half feathers
			lines += 10;
			for (int j = 0; j < 10; j++) {
				if ((((i) * 10.0d) + (j + 1) <= Math
						.ceil((double) ClientFeathersData.getEnduranceFeathers() / 2.0d))
						&& ClientFeathersData.getEnduranceFeathers() > 0) {

					// Check if feather is half or full
					int type = (((j + 1) + (10 * i) == Math.ceil((double) ClientFeathersData.getEnduranceFeathers() / 2.0d)
							&& (ClientFeathersData.getEnduranceFeathers() % 2 != 0)) ? HALF : FULL);

					GuiComponent.blit(poseStack, x + 81 - (j * 8) + xOffset,
							y /*- 58*/ - rightOffset + yOffset - ((i) * 10), type, 9, 9, 9, 256, 256);
				} else {
					break;
				}
			}
		}
		if(FeathersClientConfig.AFFECTED_BY_RIGHT_HEIGHT.get()) {
			gui.rightHeight += 10 + lines;
		}
	});

}