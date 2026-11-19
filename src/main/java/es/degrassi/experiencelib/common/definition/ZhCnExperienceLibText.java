package es.degrassi.experiencelib.common.definition;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.datagen.LocalizationEnum;
import es.degrassi.experiencelib.data.localization.AutoCollectLang;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@AutoCollectLang(value = ExperienceLib.MODID, lang = "zh_cn")
public enum ZhCnExperienceLibText implements LocalizationEnum {
  experience("经验"),
  tooltip("%s XP");

  private final String root;
  @Nullable
  private final String englishText;

  private final Component text;

  ZhCnExperienceLibText(@Nullable String englishText) {
    this.root = "%s.resource".formatted(ExperienceLib.MODID);
    this.englishText = englishText;
    this.text = Component.translatable(getTranslationKey());
  }

  ZhCnExperienceLibText(@Nullable String englishText, String root) {
    this.root = root;
    this.englishText = englishText;
    this.text = Component.translatable(getTranslationKey());
  }

  @Override
  public String getEnglishText() {
    return englishText;
  }

  @Override
  public String getTranslationKey() {
    return "%s.%s".formatted(root, name());
  }

  public String getLocal() {
    return text.getString();
  }
}
