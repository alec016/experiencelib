package es.degrassi.experiencelib.data.localization;

import es.degrassi.experiencelib.common.definition.ExperienceLibText;
import net.minecraft.data.DataGenerator;

final class RuRuLangProvider extends LocalizationProvider {
  RuRuLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void generate() {
    addEnum(ExperienceLibText.class, v -> switch (v) {
      case experience -> "Опыт";
      default -> v.getEnglishText();
    });
  }

  @Override
  public Lang getLang() {
    return Lang.ru_ru;
  }
}
