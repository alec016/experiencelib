package es.degrassi.experiencelib.api.network.syncable;

import es.degrassi.experiencelib.api.network.AbstractSyncable;
import es.degrassi.experiencelib.api.network.data.EnumData;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class EnumSyncable<T extends Enum<T>> extends AbstractSyncable<EnumData<T>, T> {

  @Override
  public EnumData<T> getData(short id) {
    return new EnumData<>(id, get());
  }

  public static <T extends Enum<T>> EnumSyncable<T> create(Supplier<T> supplier, Consumer<T> consumer) {
    return new EnumSyncable<>() {
      @Override
      public T get() {
        return supplier.get();
      }

      @Override
      public void set(T value) {
        consumer.accept(value);
      }
    };
  }
}
