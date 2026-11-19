package es.degrassi.experiencelib.api.datagen;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public interface VariantLocalizationEnum<
    V extends Enum<V> & Variant<T, V>,
    T extends Enum<T> & VariantLocalizationEnum<V, T>
> {
  @SuppressWarnings("unchecked")
  default T self() {
    return (T) this;
  }

  default String getEnglishText(V variant) {
    if (Arrays.stream(getVariants()).noneMatch(v -> v.equals(variant)))
      throw new IllegalStateException("Invalid variant provided %s, only %s are accepted".formatted(variant.name(), Arrays.stream(getVariants()).map(Enum::name).toList()));
    return variant.text(self());
  }

  String getTranslationKey(V variant);

  V[] getVariants();

  default MutableComponent text(V variant) {
    return Component.translatable(getTranslationKey(variant));
  }

  default MutableComponent text(V variant, Object...args) {
    if (args == null || args.length == 0) return text(variant);
    return Component.translatable(getTranslationKey(variant), args);
  }

  default MutableComponent withSuffix(V variant, String text, Object...args) {
    return text(variant, args).append(text);
  }

  default MutableComponent withSuffix(V variant, Component text, Object...args) {
    return text(variant, args).append(text);
  }
}
