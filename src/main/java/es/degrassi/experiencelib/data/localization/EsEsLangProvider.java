package es.degrassi.experiencelib.data.localization;

import es.degrassi.experiencelib.common.definition.ExperienceLibText;
import net.minecraft.data.DataGenerator;

final class EsEsLangProvider extends LocalizationProvider {
  EsEsLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  public Lang getLang() {
    return Lang.es_es;
  }

  @Override
  protected void generate() {
    addEnum(ExperienceLibText.class, v -> switch (v) {
      case experience -> "Experiencia";
      default -> v.getEnglishText();
    });
  }
}
