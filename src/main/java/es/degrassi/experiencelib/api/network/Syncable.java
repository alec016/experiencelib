package es.degrassi.experiencelib.api.network;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class Syncable<T> {
  private final DataType<T> type;
  private final Supplier<T> getter;
  private final Consumer<T> setter;
  @Nullable
  public T lastKnownValue = null;

  public T get() {
    return getter.get();
  }

  public void set(T value) {
    setter.accept(value);
  }

  public Data<T> makeData(short id) {
    return new Data<>(type, id, this.get());
  }

  /**
   * Ensure that the data is synced only if the hold value changed.
   * @return true if the data need to be synced.
   */
  public boolean needSync() {
    T value = get();
    boolean needSync = true;
    if (lastKnownValue != null) needSync = !type.areEquals(lastKnownValue, value);
    lastKnownValue = type.copyValue(value);
    return needSync;
  }
}
