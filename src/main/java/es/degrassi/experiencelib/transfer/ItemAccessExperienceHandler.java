package es.degrassi.experiencelib.transfer;

import es.degrassi.experiencelib.api.ExperienceResource;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.util.TransferPreconditions;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class ItemAccessExperienceHandler implements IExperienceHandler {
  protected final ItemAccess itemAccess;
  protected final Item validItem;
  protected final DataComponentType<Long> experienceComponent;
  protected final long capacity, maxInsert, maxExtract;

  public ItemAccessExperienceHandler(ItemAccess itemAccess, DataComponentType<Long> experienceComponent, long capacity) {
    this(itemAccess, experienceComponent, capacity, capacity);
  }

  public ItemAccessExperienceHandler(ItemAccess itemAccess, DataComponentType<Long> experienceComponent,
                                     long capacity, long maxTransfer) {
    this(itemAccess, experienceComponent, capacity, maxTransfer, maxTransfer);
  }

  public ItemAccessExperienceHandler(ItemAccess itemAccess, DataComponentType<Long> experienceComponent,
                                     long capacity, long maxInsert, long maxExtract) {
    TransferPreconditions.checkNonNegative(capacity);
    TransferPreconditions.checkNonNegative(maxExtract);
    TransferPreconditions.checkNonNegative(maxInsert);
    this.itemAccess = itemAccess;
    // Store the current item, such that if the item changes later we don't return any stored content from it.
    this.validItem = itemAccess.getResource().getItem();
    this.experienceComponent = experienceComponent;
    this.capacity = capacity;
    this.maxInsert = maxInsert;
    this.maxExtract = maxExtract;
  }

  protected long getAmountFrom(ItemResource accessResource) {
    if (!accessResource.is(validItem)) {
      return 0;
    }
    return accessResource.getOrDefault(experienceComponent, 0L);
  }

  protected ItemResource update(ItemResource accessResource, long newAmount) {
    return accessResource.with(experienceComponent, newAmount);
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
  public void setExperience(int tank, long amount) {
    TransferPreconditions.checkNonNegative(amount);
    update(itemAccess.getResource(), amount);
  }

  @Override
  public void setCapacity(int tank, long amount) {
    TransferPreconditions.checkNonNegative(amount);
  }

  @Override
  public long insert(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);

    int accessAmount = itemAccess.getAmount();
    if (accessAmount == 0) return 0;

    long amountPerItem = Math.min(maxInsert, amount / accessAmount);
    if (amountPerItem == 0) return 0;

    ItemResource accessResource = itemAccess.getResource();
    if (!accessResource.is(validItem)) return 0;

    long currentAmountPerItem = getAmountFrom(accessResource);
    long insertedPerItem = Math.min(amountPerItem, capacity - currentAmountPerItem);
    if (insertedPerItem > 0) {
      ItemResource filledResource = update(accessResource, currentAmountPerItem + insertedPerItem);
      if (!filledResource.isEmpty()) {
        return insertedPerItem * itemAccess.exchange(filledResource, accessAmount, context);
      }
    }
    return 0;
  }

  @Override
  public long extract(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    int accessAmount = itemAccess.getAmount();
    if (accessAmount == 0) return 0;

    long amountPerItem = Math.min(maxExtract, amount / accessAmount);
    if (amountPerItem == 0) return 0;

    ItemResource accessResource = itemAccess.getResource();
    // If the resource is not validItem this will return 0 and avoid extraction
    long currentAmountPerItem = getAmountFrom(accessResource);

    long extractedPerItem = Math.min(amountPerItem, currentAmountPerItem);
    if (extractedPerItem > 0) {
      ItemResource emptiedResource = update(accessResource, currentAmountPerItem - extractedPerItem);

      if (!emptiedResource.isEmpty()) {
        return extractedPerItem * itemAccess.exchange(emptiedResource, accessAmount, context);
      }
    }

    return 0;
  }

  @Override
  public long insertRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);

    int accessAmount = itemAccess.getAmount();
    if (accessAmount == 0) return 0;

    long amountPerItem = amount / accessAmount;
    if (amountPerItem == 0) return 0;

    ItemResource accessResource = itemAccess.getResource();
    if (!accessResource.is(validItem)) return 0;

    long currentAmountPerItem = getAmountFrom(accessResource);
    long insertedPerItem = Math.min(amountPerItem, capacity - currentAmountPerItem);
    if (insertedPerItem > 0) {
      ItemResource filledResource = update(accessResource, currentAmountPerItem + insertedPerItem);
      if (!filledResource.isEmpty()) {
        return insertedPerItem * itemAccess.exchange(filledResource, accessAmount, context);
      }
    }
    return 0;
  }

  @Override
  public long extractRecipe(int tank, long amount, TransactionContext context) {
    TransferPreconditions.checkNonNegative(amount);
    int accessAmount = itemAccess.getAmount();
    if (accessAmount == 0) return 0;

    long amountPerItem = amount / accessAmount;
    if (amountPerItem == 0) return 0;

    ItemResource accessResource = itemAccess.getResource();
    // If the resource is not validItem this will return 0 and avoid extraction
    long currentAmountPerItem = getAmountFrom(accessResource);

    long extractedPerItem = Math.min(amountPerItem, currentAmountPerItem);
    if (extractedPerItem > 0) {
      ItemResource emptiedResource = update(accessResource, currentAmountPerItem - extractedPerItem);

      if (!emptiedResource.isEmpty()) {
        return extractedPerItem * itemAccess.exchange(emptiedResource, accessAmount, context);
      }
    }

    return 0;
  }

  @Override
  public int size() {
    return itemAccess.getAmount();
  }

  @Override
  public long getAmountAsLong(int index) {
    return (long) itemAccess.getAmount() * getAmountFrom(itemAccess.getResource());
  }

  @Override
  public long getCapacityAsLong(int index, ExperienceResource resource) {
    var accessResource = itemAccess.getResource();
    if (!accessResource.is(validItem)) {
      return 0;
    }

    return (long) itemAccess.getAmount() * this.capacity;
  }
}
