package es.degrassi.experiencelib.api.capability;

import com.google.common.primitives.Ints;
import es.degrassi.experiencelib.api.ExperienceResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.ApiStatus;

public interface IExperienceHandler extends ResourceHandler<ExperienceResource> {
  long getMaxExtract(int tank);
  long getMaxInsert(int tank);

  void setExperience(int tank, long amount);
  void setCapacity(int tank, long amount);

  long insert(int tank, long amount, TransactionContext context);
  long extract(int tank, long amount, TransactionContext context);

  long insertRecipe(int tank, long amount, TransactionContext context);
  long extractRecipe(int tank, long amount, TransactionContext context);

  default void onExperienceChange(long prevAmount) {}

  @ApiStatus.NonExtendable
  default long clamp(long num, long min, long max) {
    if (num < min)
      return min;
    return Math.min(num, max);
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
