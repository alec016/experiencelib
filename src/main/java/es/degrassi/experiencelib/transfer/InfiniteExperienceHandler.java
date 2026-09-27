package es.degrassi.experiencelib.transfer;

import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.util.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class InfiniteExperienceHandler implements IExperienceHandler {
  @Override
  public long getMaxExtract(int tank) {
    return Long.MAX_VALUE;
  }

  @Override
  public long getMaxInsert(int tank) {
    return 0;
  }

  @Override
  public void setExperience(int tank, long amount) {

  }

  @Override
  public void setCapacity(int tank, long amount) {

  }

  @Override
  public long insert(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return 0;
  }

  @Override
  public long extract(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return amount;
  }

  @Override
  public long insertRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return 0;
  }

  @Override
  public long extractRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    return amount;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public long getAmountAsLong(int index) {
    return Long.MAX_VALUE;
  }

  @Override
  public long getCapacityAsLong(int index, ExperienceResource resource) {
    return Long.MAX_VALUE;
  }
}
