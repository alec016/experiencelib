package es.degrassi.experiencelib.api.xei.jei;

import es.degrassi.experiencelib.api.xei.ExperienceStack;
import es.degrassi.experiencelib.client.Blitter;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static es.degrassi.experiencelib.util.ClientExperienceUtils.EXPERIENCE;

public class ExperienceStackRenderer implements IIngredientRenderer<ExperienceStack> {
  @Override
  public void render(GuiGraphics guiGraphics, ExperienceStack ingredient) {
    guiGraphics.pose().pushPose();
    Blitter.sprite(EXPERIENCE.sprite())
        .blending(false)
        .dest(0, 0, 16, 16)
        .blit(guiGraphics);
    guiGraphics.pose().popPose();
  }

  @Override
  public List<Component> getTooltip(ExperienceStack ingredient, TooltipFlag tooltipFlag) {
    return ingredient.getTooltip();
  }
}
