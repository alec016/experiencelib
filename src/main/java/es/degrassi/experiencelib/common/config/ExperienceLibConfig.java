package es.degrassi.experiencelib.common.config;

import lombok.Getter;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public final class ExperienceLibConfig {
  private static final ExperienceLibConfig INSTANCE;
  @Getter
  private static final ModConfigSpec spec;

  static {
    Pair<ExperienceLibConfig, ModConfigSpec> pair = new Builder().configure(ExperienceLibConfig::new);
    INSTANCE = pair.getLeft();
    spec = pair.getRight();
  }

  public final ConfigValue<Long> XP_AMOUNT;

  private ExperienceLibConfig(Builder builder) {
    builder.push("General");
    this.XP_AMOUNT = builder
        .comment("Defines the amount of xp for the default capability of ExperienceBottle and GlassBottle items")
        .defineInRange("xp", 7, 1, Long.MAX_VALUE);
    builder.pop();
  }

  public static ExperienceLibConfig get() {
    return INSTANCE;
  }
}
