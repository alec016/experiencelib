package es.degrassi.experiencelib.transfer;

import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.util.TransferPreconditions;
import lombok.AllArgsConstructor;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;
import java.util.function.Supplier;

@AllArgsConstructor
public class DelegatingExperienceHandler implements IExperienceHandler {
  protected final Supplier<IExperienceHandler> delegate;

  public DelegatingExperienceHandler(IExperienceHandler delegate) {
    Objects.requireNonNull(delegate);
    this.delegate = () -> delegate;
  }

  public IExperienceHandler getDelegate() {
    return delegate.get();
  }

  @Override
  public long getMaxExtract(int tank) {
    return getDelegate().getMaxExtract(tank);
  }

  @Override
  public long getMaxInsert(int tank) {
    return getDelegate().getMaxInsert(tank);
  }

  @Override
  public void setExperience(int tank, long amount) {
    TransferPreconditions.checkNonNegative(amount);
    getDelegate().setExperience(tank, amount);
  }

  @Override
  public void setCapacity(int tank, long amount) {
    TransferPreconditions.checkNonNegative(amount);
    getDelegate().setCapacity(tank, amount);
  }

  @Override
  public long insert(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return getDelegate().insert(tank, amount, context);
  }

  @Override
  public long extract(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return getDelegate().extract(tank, amount, context);
  }

  @Override
  public long insertRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return getDelegate().insertRecipe(tank, amount, context);
  }

  @Override
  public long extractRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return getDelegate().extract(tank, amount, context);
  }

  @Override
  public int size() {
    return getDelegate().size();
  }

  @Override
  public long getAmountAsLong(int index) {
    return getDelegate().getAmountAsLong(index);
  }

  @Override
  public long getCapacityAsLong(int index, ExperienceResource resource) {
    return getDelegate().getCapacityAsLong(index, resource);
  }
}
