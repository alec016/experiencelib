package es.degrassi.experiencelib.transfer;

import com.google.common.primitives.Ints;
import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.ExperienceStack;
import es.degrassi.experiencelib.api.capability.IContentsListener;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.util.TransferPreconditions;
import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LimitedExperienceStacksResourceHandler extends StacksResourceHandler<ExperienceStack, ExperienceResource> implements IExperienceHandler {
  private long defaultCapacity;
  private final long maxInsert, maxExtract;
  private final IContentsListener listener;

  public LimitedExperienceStacksResourceHandler(int tanksNumber, long capacity, @Nullable IContentsListener listener) {
    this(tanksNumber, capacity, capacity, listener);
  }

  public LimitedExperienceStacksResourceHandler(NonNullList<ExperienceStack> stacks, long capacity, @Nullable IContentsListener listener) {
    this(stacks, capacity, capacity, listener);
  }

  public LimitedExperienceStacksResourceHandler(int tanksNumber, long capacity, long maxTransfer, @Nullable IContentsListener listener) {
    this(tanksNumber, capacity, maxTransfer, maxTransfer, listener);
  }

  public LimitedExperienceStacksResourceHandler(NonNullList<ExperienceStack> stacks, long capacity, long maxTransfer, @Nullable IContentsListener listener) {
    this(stacks, capacity, maxTransfer, maxTransfer, listener);
  }

  public LimitedExperienceStacksResourceHandler(int tanksNumber, long capacity, long maxInsert, long maxExtract, @Nullable IContentsListener listener) {
    TransferPreconditions.checkNonNegative(tanksNumber, capacity, maxInsert, maxExtract);
    super(tanksNumber, ExperienceStack.EMPTY, ExperienceStack.CODEC.codec());
    this.defaultCapacity = capacity;
    this.maxInsert = maxInsert;
    this.maxExtract = maxExtract;
    this.listener = listener;
  }

  public LimitedExperienceStacksResourceHandler(NonNullList<ExperienceStack> stacks, long capacity, long maxInsert, long maxExtract, @Nullable IContentsListener listener) {
    TransferPreconditions.checkNonNegative(capacity, maxInsert, maxExtract);
    super(stacks, ExperienceStack.EMPTY, ExperienceStack.CODEC.codec());
    this.defaultCapacity = capacity;
    this.maxInsert = maxInsert;
    this.maxExtract = maxExtract;
    this.listener = listener;
  }

  @Override
  public void onContentsChanged(int index, ExperienceStack previousContents) {
    if (listener != null) listener.onContentsChanged();
  }

  @Override
  public ExperienceResource getResourceFrom(ExperienceStack stack) {
    return ExperienceResource.of(stack);
  }

  @Override
  public int getAmountFrom(ExperienceStack stack) {
    return Ints.saturatedCast((long) stack.getAmount());
  }

  @Override
  public ExperienceStack getStackFrom(ExperienceResource resource, int amount) {
    return resource.toStack(amount);
  }

  @Override
  public ExperienceStack copyOf(ExperienceStack stack) {
    return stack.copy();
  }

  @Override
  public int getCapacity(int index, ExperienceResource resource) {
    return Ints.saturatedCast(defaultCapacity);
  }

  @Override
  public long getCapacityAsLong(int index, ExperienceResource resource) {
    return defaultCapacity;
  }

  @Override
  public long getMaxExtract(int tank) {
    Objects.checkIndex(tank, size());
    return maxExtract;
  }

  @Override
  public long getMaxInsert(int tank) {
    Objects.checkIndex(tank, size());
    return maxInsert;
  }

  @Override
  public void setExperience(int tank, long amount) {
    Objects.checkIndex(tank, size());
  }

  @Override
  public void setCapacity(int tank, long amount) {
    validate(tank, amount);
    this.defaultCapacity = amount;
  }

  private <T extends Number> void validate(int tank, T amount) {
    Objects.checkIndex(tank, size());
    TransferPreconditions.checkNonNegative(amount);
  }

  private long internalInsert(int tank, long amount, TransactionContext context) {
    validate(tank, amount);
    long inserted = 0;
    long lastInserted = 0;
    while (lastInserted != amount && amount > 0) {
      lastInserted = insert(tank, ExperienceResource.of(amount), Ints.saturatedCast(amount), context);
      inserted += lastInserted;
      amount -= lastInserted;
    }
    return inserted;
  }

  private long internalExtract(int tank, long amount, TransactionContext context) {
    validate(tank, amount);
    long extracted = 0;
    long lastExtracted = 0;
    while (lastExtracted != amount && amount > 0) {
      lastExtracted = extract(tank, ExperienceResource.of(amount), Ints.saturatedCast(amount), context);
      extracted += lastExtracted;
      amount -= lastExtracted;
    }
    return extracted;
  }

  @Override
  public long insert(int tank, long amount, TransactionContext context) {
    return internalInsert(tank, Math.min(amount, maxInsert), context);
  }

  @Override
  public long extract(int tank, long amount, TransactionContext context) {
    return internalExtract(tank, Math.min(amount, maxExtract), context);
  }

  @Override
  public long insertRecipe(int tank, long amount, TransactionContext context) {
    validate(tank, amount);
    return internalInsert(tank, amount, context);
  }

  @Override
  public long extractRecipe(int tank, long amount, TransactionContext context) {
    validate(tank, amount);
    return internalExtract(tank, amount, context);
  }
}
