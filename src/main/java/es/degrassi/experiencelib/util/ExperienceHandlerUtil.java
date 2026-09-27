package es.degrassi.experiencelib.util;

import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.util.Mth;
import net.minecraft.world.level.redstone.Redstone;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExperienceHandlerUtil {
  public static boolean isFull(IExperienceHandler handler) {
    if (handler.size() == 0) return false;
    for (int i = 0; i < handler.size(); i++) {
      if (handler.getAmountAsLong(i) < handler.getCapacityAsLong(i, ExperienceResource.EMPTY)) return false;
    }
    return true;
  }

  public static int getRedstoneSignalFromExperienceHandler(IExperienceHandler handler) {
    long amount = 0, capacity = 0;
    if (handler.size() == 0) return Redstone.SIGNAL_NONE;

    for (int i = 0; i < handler.size(); i++) {
      amount += handler.getAmountAsLong(i);
      capacity += handler.getCapacityAsLong(i, ExperienceResource.EMPTY);
    }

    if (amount == 0 || capacity == 0) return Redstone.SIGNAL_NONE;

    return Mth.lerpDiscrete(
        Math.min(1f, (float) amount / capacity),
        Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX
    );
  }

  public static long move(
      @Nullable IExperienceHandler from, @Nullable IExperienceHandler to,
      long amount,
      @Nullable TransactionContext transaction
  ) {
    TransferPreconditions.checkNonNegative(amount);
    if (from == null || to == null || amount == 0) return 0;

    try(Transaction subTransaction = Transaction.open(transaction)) {
      long totalInserted = 0;
      for (int i = 0; i < from.size() && amount > 0; i++) {
        try (Transaction subSubTransaction = Transaction.open(subTransaction)) {
          long maxExtracted;
          long inserted = 0;
          try (Transaction simulatedExtract = Transaction.open(subSubTransaction)) {
            maxExtracted = from.extract(i, amount, simulatedExtract);
          }
          if (maxExtracted == 0) continue;

          for (int j = 0; j < to.size() && maxExtracted > 0; j++) {
            long toAdd = to.insert(j, maxExtracted, subSubTransaction);
            maxExtracted -= toAdd;
            inserted += toAdd;
          }

          if (inserted != from.extract(i, inserted, subSubTransaction)) continue;

          subSubTransaction.commit();
          totalInserted += inserted;
          amount -= inserted;
        }
      }

      subTransaction.commit();
      return totalInserted;
    } catch(Exception e) {
      CrashReport report = CrashReport.forThrowable(e, "Moving experience between handlers");

      report.addCategory("Move Details")
          .setDetail("Input", from::toString)
          .setDetail("Output", to::toString)
          .setDetail("Amount", amount)
          .setDetail("Transaction", transaction);
      throw new ReportedException(report);
    }
  }
}
