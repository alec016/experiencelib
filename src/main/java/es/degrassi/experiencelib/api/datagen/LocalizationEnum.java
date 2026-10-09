package es.degrassi.experiencelib.api.datagen;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface LocalizationEnum {
  String getEnglishText();
  String getTranslationKey();

  default MutableComponent text() {
    return Component.translatable(getTranslationKey());
  }

  default MutableComponent text(Object...args) {
    if (args == null || args.length == 0) return text();
    return Component.translatable(getTranslationKey(), args);
  }

  default MutableComponent withSuffix(String text, Object...args) {
    return text(args).copy().append(text);
  }

  default MutableComponent withSuffix(Component text, Object...args) {
    return text(args).copy().append(text);
  }
}
