package es.degrassi.experiencelib.data.localization;

import es.degrassi.experiencelib.common.ExperienceLibRegistration;
import es.degrassi.experiencelib.common.definition.ExperienceLibText;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.LiquidBlock;

final class EnUsLangProvider extends LocalizationProvider {
  EnUsLangProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  public Lang getLang() {
    return Lang.en_us;
  }

  @Override
  protected void generate() {
    for (var block : ExperienceLibRegistration.getBlocks()) {
      add("block.%s.%s".formatted(block.id().getNamespace(), block.id().getPath()), block.getEnglishName());
      if (block.block() instanceof LiquidBlock) continue;
      add("item.%s.%s".formatted(block.id().getNamespace(), block.id().getPath()), block.getEnglishName());
    }
    for (var item : ExperienceLibRegistration.getItems()) {
      add("item.%s.%s".formatted(item.id().getNamespace(), item.id().getPath()), item.getEnglishName());
    }
    addEnum(ExperienceLibText.class);
  }
}
