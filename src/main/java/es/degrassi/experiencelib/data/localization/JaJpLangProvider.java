package es.degrassi.experiencelib.data.localization;

import es.degrassi.experiencelib.common.definition.ExperienceLibText;
import net.minecraft.data.DataGenerator;

final class JaJpLangProvider extends LocalizationProvider {
  JaJpLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void generate() {
    addEnum(ExperienceLibText.class, v -> switch (v) {
      case experience -> "経験値";
      default -> v.getEnglishText();
    });
  }

  @Override
  public Lang getLang() {
    return Lang.ja_jp;
  }
}
