package es.degrassi.experiencelib.api.xei;

import es.degrassi.experiencelib.api.codec.NamedCodec;
import es.degrassi.experiencelib.util.Utils;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExperienceStack {
  @Getter
  protected double amount;
  public static final ExperienceStack EMPTY = new ExperienceStack(0);
  private static final ExperienceKey KEY = new ExperienceKey();

  public static final NamedCodec<ExperienceStack> CODEC = NamedCodec.record(instance -> instance.group(
      NamedCodec.DOUBLE.fieldOf("amount").forGetter(ExperienceStack::getAmount)
  ).apply(instance, ExperienceStack::new), "ExperienceStack");

  public ExperienceStack(double amount) {
    this.amount = amount;
  }

  public final ExperienceKey getKey() {
    return KEY;
  }

  public final ResourceLocation getId() {
    return KEY.getId();
  }

  public boolean isEmpty() {
    return this == EMPTY || amount <= 0;
  }

  public Component getName() {
    // TODO: Add translation for this
    return Component.literal("Experience");
  }

  /**
   * @return List of components containing just the name
   */
  public List<Component> getTooltip() {
    if (isEmpty()) {
      return Collections.emptyList();
    }
    List<Component> tooltips = new ArrayList<>();
    tooltips.add(getName());
    return tooltips;
  }

  /**
   * @return List of components containing the name and the amount
   */
  public List<Component> getTooltips() {
    var tooltips = getTooltip();
    if (!isEmpty())
      tooltips.add(Component.translatable("%s XP", Utils.format(amount)));
    return tooltips;
  }

  public ExperienceStack copy() {
    return new ExperienceStack(amount);
  }

  public ExperienceStack copyWithAmount(double newAmount) {
    return new ExperienceStack(newAmount);
  }
}
