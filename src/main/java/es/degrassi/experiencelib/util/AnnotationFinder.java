package es.degrassi.experiencelib.util;

import lombok.NoArgsConstructor;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor
public final class AnnotationFinder {
  /**
   * Returns all classes (TYPE elements) annotated with {@code annotation}
   * whose {@code value} attribute (a String) matches {@code expected}.
   * <p>
   * Uses the annotation scan data that FML already collected at startup,
   * so it works both in dev (exploded classes) and inside mod jars.
   * Classes are loaded WITHOUT initialization (no static initializers run),
   * thanks to {@link Class#forName(String, boolean, ClassLoader)}.
   *
   * <p><b>Note:</b> ASM only records attributes that were explicitly written
   * in the source, so annotation defaults (e.g. {@code String value() default "x"})
   * are not present in the scan data map.
   *
   * @param annotation  the annotation type to look for
   * @param extraFilter an additional extra filter for the annotation
   * @param expected    the expected {@code value} attribute
   * @return the matching classes, in scan order
   */
  public static List<Class<?>> byValue(Class<? extends Annotation> annotation,
                                       Predicate<ModFileScanData.AnnotationData> extraFilter, String expected) {
    List<Class<?>> result = new ArrayList<>();
    ClassLoader loader = AnnotationFinder.class.getClassLoader();

    for (var scan : ModList.get().getAllScanData()) {
      scan.getAnnotatedBy(annotation, ElementType.TYPE)
          .filter(data -> data.annotationData().get("value") instanceof String s && s.equals(expected))
          .filter(extraFilter)
          .forEach(data -> {
            try {
              result.add(Class.forName(data.clazz().getClassName(), false, loader));
            } catch (ClassNotFoundException e) {
              throw new IllegalStateException("Could not load class " + data.clazz().getClassName(), e);
            }
          });
    }
    return result;
  }

  public static <T> List<Class<T>> byValueFiltered(
      Class<? extends Annotation> annotation,
      Predicate<ModFileScanData.AnnotationData> extraFilter,
      String expected,
      Predicate<Class<?>> checker,
      Function<Class<?>, Class<T>> parser
  ) {
    return byValue(annotation, extraFilter, expected)
        .parallelStream()
        .filter(checker)
        .map(parser)
        .toList();
  }

  public static <T> List<Class<T>> byValueFiltered(
      Class<? extends Annotation> annotation,
      String expected,
      Predicate<Class<?>> checker,
      Function<Class<?>, Class<T>> parser
  ) {
    return byValue(annotation, _ -> true, expected)
        .parallelStream()
        .filter(checker)
        .map(parser)
        .toList();
  }
}
