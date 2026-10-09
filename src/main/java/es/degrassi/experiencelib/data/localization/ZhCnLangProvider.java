package es.degrassi.experiencelib.data.localization;

import es.degrassi.experiencelib.common.definition.ExperienceLibText;
import net.minecraft.data.DataGenerator;

final class ZhCnLangProvider extends LocalizationProvider {
  ZhCnLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void generate() {
    addEnum(ExperienceLibText.class, v -> switch (v) {
      case experience -> "经验";
      default -> v.getEnglishText();
    });
  }

  @Override
  public Lang getLang() {
    return Lang.zh_cn;
  }
}
