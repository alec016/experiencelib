package es.degrassi.experiencelib.data.localization;

import net.minecraft.data.DataGenerator;

final class RuRuLangProvider extends LocalizationProvider {
  RuRuLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void generate() {
  }

  @Override
  public Lang getLang() {
    return Lang.ru_ru;
  }
}
