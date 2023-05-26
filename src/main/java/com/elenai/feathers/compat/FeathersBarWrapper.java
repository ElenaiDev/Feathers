package com.elenai.feathers.compat;

import com.elenai.feathers.api.FeathersHelper;
import com.elenai.feathers.client.ClientFeathersData;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class FeathersBarWrapper {

    public FeathersClassicBar getBar() {
        return new FeathersClassicBar();
    }

    private class FeathersClassicBar extends tfar.classicbar.impl.BarOverlayImpl {

        private static final ResourceLocation DODGE_ICONS = new ResourceLocation("feathers", "textures/gui/icons.png");

        public FeathersClassicBar() {
            super("feathers");
        }

        @Override
        public boolean shouldRender(Player player) {
            return true;
        }

        @Override
        public void renderBar(ForgeGui gui, PoseStack stack, Player player, int screenWidth, int screenHeight, int vOffset) {
            double feathers = FeathersHelper.getFeathers();
            int maxFeathers = FeathersHelper.getMaxFeathers();

            int xStart = screenWidth / 2 + 10;
            int yStart = screenHeight - vOffset;
            GlStateManager._enableBlend();

            tfar.classicbar.util.Color.reset();
            //Bar background
            tfar.classicbar.util.ModUtils.drawTexturedModalRect(stack, xStart, yStart, 0, 0, 81, 9);
            //draw portion of bar based on feathers amount
            double f = xStart + 78 - tfar.classicbar.util.ModUtils.getWidth(feathers, maxFeathers);
            tfar.classicbar.util.ColorUtils.hex2Color("#22a5f0").color2Gl();
            tfar.classicbar.util.ModUtils.drawTexturedModalRect(stack, f, yStart + 1, 1, 10, tfar.classicbar.util.ModUtils.getWidth(feathers, maxFeathers) + 1, 7);

            // Then do the same for Armored Feathers
            double j = xStart + 78 - tfar.classicbar.util.ModUtils.getWidth(ClientFeathersData.getWeight(), 20);
            tfar.classicbar.util.ColorUtils.hex2Color("#b8b9c4").color2Gl();
            tfar.classicbar.util.ModUtils.drawTexturedModalRect(stack, j, yStart + 1, 1, 10, tfar.classicbar.util.ModUtils.getWidth(ClientFeathersData.getWeight(), 20) + 1, 7);

        }

        @Override
        public boolean shouldRenderText() {
            return tfar.classicbar.config.ClassicBarsConfig.showHungerNumbers.get();
        }

        @Override
        public void renderText(PoseStack stack, Player player, int width, int height, int vOffset) {
            //draw feathers amount
            double feathers = FeathersHelper.getFeathers();
            int c = Integer.decode("#22a5f0");
            int xStart = width / 2 + getIconOffset();
            int yStart = height - vOffset;
            textHelper(stack, xStart, yStart, feathers, c);
        }

        @Override
        public void renderIcon(PoseStack stack, Player player, int width, int height, int vOffset) {
            int xStart = width / 2 + 10;
            int yStart = height - vOffset;
            //Draw feathers icon
            tfar.classicbar.util.ModUtils.drawTexturedModalRect(stack, xStart + 82, yStart, 34, 0, 9, 9);
        }

        @Override
        public double getBarWidth(Player player) {
            throw new RuntimeException("not implemented");
        }

        @Override
        public ResourceLocation getIconRL() {
            return DODGE_ICONS;
        }
    }
}