package es.degrassi.experiencelib.api.datagen;

public interface Variant<
    T extends Enum<T> & VariantLocalizationEnum<V, T>,
    V extends Enum<V> & Variant<T, V>
> {
  String text(T variantLocalization);
}
