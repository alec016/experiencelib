package es.degrassi.experiencelib.api.xei.emi;

import dev.emi.emi.api.render.EmiRender;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import es.degrassi.experiencelib.api.xei.ExperienceKey;
import es.degrassi.experiencelib.api.xei.ExperienceStack;
import es.degrassi.experiencelib.client.Blitter;
import es.degrassi.experiencelib.util.TextComponentUtil;
import es.degrassi.experiencelib.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static es.degrassi.experiencelib.util.ClientExperienceUtils.EXPERIENCE;

public class ExperienceEmiStack extends EmiStack {
  private final double amount;

  public ExperienceEmiStack(double amount) {
    this.amount = amount;
  }

  @Override
  public EmiStack copy() {
    ExperienceEmiStack e = new ExperienceEmiStack(this.amount);
    e.setChance(this.chance);
    e.setRemainder(getRemainder().copy());
    e.comparison = this.comparison;
    return e;
  }

  @Override
  public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
    if ((flags & RENDER_ICON) != 0) {
      graphics.pose().pushPose();
      Blitter.sprite(EXPERIENCE.sprite())
              .blending(false)
              .dest(x, y, 16, 16)
              .blit(graphics);
      graphics.pose().popPose();
    }
    if ((flags & RENDER_REMAINDER) != 0) {
      EmiRender.renderRemainderIcon(this, graphics, x, y);
    }
  }

  public ExperienceStack getStack() {
    if (isEmpty()) {
      return ExperienceStack.EMPTY;
    }
    return new ExperienceStack(amount);
  }

  @Override
  public boolean isEmpty() {
    return amount <= 0;
  }

  @Override
  public DataComponentPatch getComponentChanges() {
    return DataComponentPatch.EMPTY;
  }

  @Override
  public ExperienceKey getKey() {
    return getStack().getKey();
  }

  @Override
  public ResourceLocation getId() {
    return getStack().getId();
  }

  @Override
  public List<Component> getTooltipText() {
    if (isEmpty()) {
      return Collections.emptyList();
    }
    List<Component> tooltips = new ArrayList<>();
    tooltips.add(getName());
    return tooltips;
  }

  @Override
  public List<ClientTooltipComponent> getTooltip() {
    List<ClientTooltipComponent> tooltips = getTooltipText().stream()
        .map(EmiTooltipComponents::of)
        .collect(Collectors.toList());
    if (amount > 1) {
      tooltips.add(EmiTooltipComponents.of(Component.literal(Utils.format(amount)).withStyle(ChatFormatting.GRAY)));
    }

    EmiTooltipComponents.appendModName(tooltips, getId().getNamespace());
    tooltips.addAll(super.getTooltip());
    return tooltips;
  }

  @Override
  public Component getName() {
    return TextComponentUtil.build("Experience");
  }
}
