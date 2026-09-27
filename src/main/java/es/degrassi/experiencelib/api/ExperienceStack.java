package es.degrassi.experiencelib.api;

import es.degrassi.experiencelib.api.codec.NamedCodec;
import es.degrassi.experiencelib.util.Utils;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

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

  public static NamedCodec<ExperienceStack> fixedAmountCodec(long amount) {
    return NamedCodec.unit(new ExperienceStack(amount));
  }

  public ExperienceStack(double amount) {
    this.amount = amount;
  }

  public final ExperienceKey getKey() {
    return KEY;
  }

  public final Identifier getId() {
    return KEY.getId();
  }

  public boolean isEmpty() {
    return equals(EMPTY) || amount <= 0;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof ExperienceStack that)) return false;
    return that.amount == this.amount;
  }
}
