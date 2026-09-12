package es.degrassi.experiencelib.api.xei.jei;

import es.degrassi.experiencelib.api.xei.ExperienceStack;
import es.degrassi.experiencelib.client.Blitter;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static es.degrassi.experiencelib.util.ClientExperienceUtils.EXPERIENCE;

public class ExperienceStackRenderer implements IIngredientRenderer<ExperienceStack> {
  @Override
  public void render(GuiGraphicsExtractor guiGraphics, ExperienceStack ingredient) {
    guiGraphics.pose().pushMatrix();
    Blitter.sprite(EXPERIENCE.sprite())
        .blending(false)
        .dest(0, 0, 16, 16)
        .blit(guiGraphics);
    guiGraphics.pose().popMatrix();
  }

  @Override
  @SuppressWarnings("removal")
  public List<Component> getTooltip(ExperienceStack ingredient, TooltipFlag tooltipFlag) {
    return ingredient.getTooltip();
  }

  @Override
  public List<Component> getTooltip(ExperienceStack ingredient, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
    return ingredient.getTooltip();
  }
}
