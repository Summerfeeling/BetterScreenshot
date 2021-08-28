package dev.summerfeeling.betterscreenshot.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.util.IReorderingProcessor;

import java.util.List;

public class DescribableHeaderElement extends HeaderElement {

    private static final int TEXT_HEIGHT = LabyMod.getInstance().getDrawUtils().getFontRenderer().FONT_HEIGHT;

    private long startDescription = -1L;

    public DescribableHeaderElement(String displayName) {
        super(displayName);
    }

    public HeaderElement editDescriptionText(String descriptionText) {
        super.setDescriptionText(descriptionText);
        return this;
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(matrixStack, x, y, maxX, maxY, mouseX, mouseY);

        int textWidth = LabyMod.getInstance().getDrawUtils().getStringWidth(super.getDisplayName()) / 2;
        int boxMiddle = (maxX + x) / 2;

        boolean hovered = (mouseY >= y + 7 && mouseY <= y + 7 + TEXT_HEIGHT) && (mouseX >= (boxMiddle - textWidth) && mouseX <= (boxMiddle + textWidth));

        if (hovered && startDescription == -1) {
            this.startDescription = System.currentTimeMillis() + 500L;
        } else if (hovered) {
            this.drawDescriptionText(matrixStack, mouseX, mouseY, LabyMod.getInstance().getDrawUtils().getWidth());
        } else if (startDescription != -1) {
            this.startDescription = -1;
        }
    }

    public void drawDescriptionText(MatrixStack matrixStack, int x, int y, int screenWidth) {
        String description = this.getDescriptionText();

        if (description != null && System.currentTimeMillis() >= startDescription) {
            DrawUtils draw = LabyMod.getInstance().getDrawUtils();

            List<IReorderingProcessor> list = draw.listFormattedStringToWidth(description, screenWidth / 3);
            TooltipHelper.getHelper().pointTooltip(x, y, 500L, list.toArray(new IReorderingProcessor[0]));
        }
    }
}
