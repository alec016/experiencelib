package es.degrassi.experiencelib.api.network.syncable;

import es.degrassi.experiencelib.api.network.AbstractSyncable;
import es.degrassi.experiencelib.api.network.data.IdentifierData;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class IdentifierSyncable extends AbstractSyncable<IdentifierData, Identifier> {
  @Override
  public IdentifierData getData(short id) {
    return new IdentifierData(id, get());
  }

  public static IdentifierSyncable create(Supplier<Identifier> supplier, Consumer<Identifier> consumer) {
    return new IdentifierSyncable() {
      @Override
      public Identifier get() {
        return supplier.get();
      }

      @Override
      public void set(Identifier value) {
        consumer.accept(value);
      }
    };
  }
}
