package es.degrassi.experiencelib.transfer;

import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.util.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class SimpleExperienceHandler implements IExperienceHandler {
  protected long amount = 0, capacity;
  protected long maxInsert, maxExtract;

  protected final ExperienceJournal journal = new ExperienceJournal();

  public SimpleExperienceHandler(long capacity) {
    this(capacity, capacity, capacity);
  }

  public SimpleExperienceHandler(long capacity, long maxExtract, long maxInsert) {
    this.capacity = capacity;
    this.maxInsert = maxInsert;
    this.maxExtract = maxExtract;
  }

  @Override
  public long getMaxExtract(int tank) {
    return maxExtract;
  }

  @Override
  public long getMaxInsert(int tank) {
    return maxInsert;
  }

  public void setMaxExtract(int tank, long maxExtract) {
    this.maxExtract = maxExtract;
  }

  public void setMaxInsert(int tank, long maxInsert) {
    this.maxInsert = maxInsert;
  }

  @Override
  public void setExperience(int tank, long amount) {
    this.amount = Math.min(amount, capacity);
  }

  @Override
  public void setCapacity(int tank, long amount) {
    this.capacity = amount;
    setExperience(tank, this.amount);
  }

  @Override
  public long insert(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    var toInsert = Math.min(amount, Math.min(this.capacity - this.amount, this.maxInsert));
    if (toInsert > 0) {
      this.journal.updateSnapshots(context);
      this.amount += toInsert;
      return toInsert;
    }
    return 0;
  }

  @Override
  public long extract(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    var toExtract = Math.min(this.amount, Math.min(amount, this.maxExtract));
    if (toExtract > 0) {
      this.journal.updateSnapshots(context);
      this.amount -= toExtract;
      return toExtract;
    }
    return 0;
  }

  @Override
  public long insertRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    var toInsert = Math.min(amount, this.capacity - amount);
    if (toInsert > 0) {
      this.journal.updateSnapshots(context);
      this.amount += toInsert;
      return toInsert;
    }
    return 0;
  }

  @Override
  public long extractRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    var toExtract = Math.min(this.amount, amount);
    if (toExtract > 0) {
      this.journal.updateSnapshots(context);
      this.amount -= toExtract;
      return toExtract;
    }
    return 0;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public long getAmountAsLong(int index) {
    return this.amount;
  }

  @Override
  public long getCapacityAsLong(int index, ExperienceResource resource) {
    return this.capacity;
  }

  protected class ExperienceJournal extends SnapshotJournal<Long> {

    @Override
    protected Long createSnapshot() {
      return amount;
    }

    @Override
    protected void revertToSnapshot(Long snapshot) {
      amount = snapshot;
    }

    @Override
    protected void onRootCommit(Long originalState) {
      long previousAmount = originalState;
      if (amount != previousAmount) {
        onExperienceChange(previousAmount);
      }
    }
  }
}
