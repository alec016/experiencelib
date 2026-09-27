package es.degrassi.experiencelib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.neoforged.neoforge.transfer.resource.Resource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransferPreconditions {
  /**
   * Ensures the resource is non-empty, throws otherwise.
   *
   * @throws IllegalArgumentException when resource is empty.
   */
  public static void checkNonEmpty(Resource resource) {
    if (resource.isEmpty()) {
      throw new IllegalArgumentException("Expected resource to be non-empty: " + resource);
    }
  }

  /**
   * Ensures the value is non-negative, throws otherwise.
   *
   * @throws IllegalArgumentException when value is negative.
   */
  public static <T extends Number> void checkNonNegative(T value) {
    if (value.doubleValue() < 0) throw new IllegalArgumentException("Expected value to be non-negative: " + value);
  }

  /**
   * Ensures the values are non-negative, throws otherwise.
   *
   * @throws IllegalArgumentException when value is negative.
   */
  @SafeVarargs
  public static <T extends Number> void checkNonNegative(T... values) {
    for (T value : values) checkNonNegative(value);
  }

  /**
   * Ensures the resource is non-empty and the value is non-negative, throws otherwise.
   *
   * @throws IllegalArgumentException when resource is empty or value is negative.
   */
  public static <T extends Number> void checkNonEmptyNonNegative(Resource resource, T value) {
    checkNonEmpty(resource);
    checkNonNegative(value);
  }
}
