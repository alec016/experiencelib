package es.degrassi.experiencelib.api.capability;

import com.google.common.primitives.Ints;
import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.network.ISyncable;
import es.degrassi.experiencelib.api.network.ISyncableStuff;
import es.degrassi.experiencelib.api.network.syncable.LongSyncable;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public interface IExperienceHandler extends ResourceHandler<ExperienceResource>, ValueIOSerializable, ISyncableStuff {
  String AMOUNT_KEY = "amount";
  String MAX_IN_KEY = "max_insert";
  String MAX_OUT_KEY = "max_extract";
  String CAPACITY = "capacity";
  String TANKS = "tanks";
  String SLOT = "slot";

  long getMaxExtract(int tank);
  long getMaxInsert(int tank);

  default void setMaxExtract(int tank, long amount) {}
  default void setMaxInsert(int tank, long amount) {}

  void setExperience(int tank, long amount);
  void setCapacity(int tank, long amount);

  long insert(int tank, long amount, TransactionContext context);
  long extract(int tank, long amount, TransactionContext context);

  long insertRecipe(int tank, long amount, TransactionContext context);
  long extractRecipe(int tank, long amount, TransactionContext context);

  default void onExperienceChange(long prevAmount) {}

  @Override
  default void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
    for (AtomicInteger index = new AtomicInteger(0); index.get() < size(); index.getAndIncrement()) {
      container.accept(LongSyncable.create(() -> getAmountAsLong(index.get()), (value) -> setExperience(index.get(), value)));
      container.accept(LongSyncable.create(() -> getCapacityAsLong(index.get(), getResource(index.get())), (value) -> setCapacity(index.get(), value)));
      container.accept(LongSyncable.create(() -> getMaxInsert(index.get()), (value) -> setMaxInsert(index.get(), value)));
      container.accept(LongSyncable.create(() -> getMaxExtract(index.get()), (value) -> setMaxExtract(index.get(), value)));
    }
  }

  @ApiStatus.NonExtendable
  default long clamp(long num, long min, long max) {
    if (num < min)
      return min;
    return Math.min(num, max);
  }

  @Override
  default void serialize(ValueOutput output) {
    var tanks = output.childrenList(TANKS);
    for (int i = 0; i < size(); i++) {
      var tank = tanks.addChild();
      tank.putLong(AMOUNT_KEY, getAmountAsLong(i));
      tank.putLong(CAPACITY, getCapacityAsLong(i, ExperienceResource.EMPTY));
      tank.putLong(MAX_IN_KEY, getMaxInsert(i));
      tank.putLong(MAX_OUT_KEY, getMaxExtract(i));
      tank.putInt(SLOT, i);
    }
  }

  @Override
  default void deserialize(ValueInput input) {
    input.childrenList(TANKS).ifPresent(tanks -> {
      tanks.forEach(tank -> {
        int slot = tank.getInt(SLOT).orElseThrow();
        setCapacity(slot, tank.getLong(CAPACITY).orElseThrow());
        setExperience(slot, tank.getLong(AMOUNT_KEY).orElseThrow());
        setMaxExtract(slot, tank.getLong(MAX_OUT_KEY).orElseThrow());
        setMaxInsert(slot, tank.getLong(MAX_IN_KEY).orElseThrow());
      });
    });
  }

  @Override
  @ApiStatus.NonExtendable
  default ExperienceResource getResource(int index) {
    return ExperienceResource.of(getAmountAsLong(index));
  }

  @Override
  @ApiStatus.NonExtendable
  default boolean isValid(int index, ExperienceResource resource) {
    return true;
  }

  @Override
  @ApiStatus.NonExtendable
  default int insert(int index, ExperienceResource resource, int amount, TransactionContext transaction) {
    return Ints.saturatedCast(insert(index, amount, transaction));
  }

  @Override
  @ApiStatus.NonExtendable
  default int extract(int index, ExperienceResource resource, int amount, TransactionContext transaction) {
    return Ints.saturatedCast(extract(index, amount, transaction));
  }
}
