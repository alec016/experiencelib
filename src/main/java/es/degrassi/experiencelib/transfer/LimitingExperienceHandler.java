package es.degrassi.experiencelib.transfer;

import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.util.TransferPreconditions;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.Supplier;

public class LimitingExperienceHandler extends DelegatingExperienceHandler {
  protected long maxInsert, maxExtract;

  public LimitingExperienceHandler(IExperienceHandler delegate, long maxInsert, long maxExtract) {
    super(delegate);
    TransferPreconditions.checkNonNegative(maxExtract);
    TransferPreconditions.checkNonNegative(maxInsert);

    this.maxExtract = maxExtract;
    this.maxInsert = maxInsert;
  }

  public LimitingExperienceHandler(Supplier<IExperienceHandler> delegate, long maxInsert, long maxExtract) {
    super(delegate);
    TransferPreconditions.checkNonNegative(maxExtract);
    TransferPreconditions.checkNonNegative(maxInsert);

    this.maxExtract = maxExtract;
    this.maxInsert = maxInsert;
  }

  @Override
  public long getMaxExtract(int tank) {
    return maxExtract;
  }

  @Override
  public long getMaxInsert(int tank) {
    return maxInsert;
  }

  @Override
  public long insert(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    long toInsert = Math.min(amount, maxInsert);
    return toInsert <= 0 ? 0 : super.insert(tank, toInsert, context);
  }

  @Override
  public long extract(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    long toExtract = Math.min(amount, maxExtract);
    return toExtract <= 0 ? 0 : super.extract(tank, toExtract, context);
  }
}
