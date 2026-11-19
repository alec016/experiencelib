package es.degrassi.experiencelib.data.localization;

import net.minecraft.data.DataGenerator;

final class ZhCnLangProvider extends LocalizationProvider {
  ZhCnLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void generate() {
  }

  @Override
  public Lang getLang() {
    return Lang.zh_cn;
  }
}
