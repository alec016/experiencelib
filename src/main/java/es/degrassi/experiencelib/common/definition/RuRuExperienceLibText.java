package es.degrassi.experiencelib.common.definition;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.datagen.LocalizationEnum;
import es.degrassi.experiencelib.data.localization.AutoCollectLang;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@AutoCollectLang(value = ExperienceLib.MODID, lang = "ru_ru")
public enum RuRuExperienceLibText implements LocalizationEnum {
  experience("Опыт"),
  tooltip("%s XP");

  private final String root;
  @Nullable
  private final String englishText;

  private final Component text;

  RuRuExperienceLibText(@Nullable String englishText) {
    this.root = "%s.resource".formatted(ExperienceLib.MODID);
    this.englishText = englishText;
    this.text = Component.translatable(getTranslationKey());
  }

  RuRuExperienceLibText(@Nullable String englishText, String root) {
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
