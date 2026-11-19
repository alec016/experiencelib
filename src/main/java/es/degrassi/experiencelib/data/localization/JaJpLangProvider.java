package es.degrassi.experiencelib.data.localization;

import net.minecraft.data.DataGenerator;

final class JaJpLangProvider extends LocalizationProvider {
  JaJpLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void generate() {
  }

  @Override
  public Lang getLang() {
    return Lang.ja_jp;
  }
}
