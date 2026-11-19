package es.degrassi.experiencelib.api.network;

import es.degrassi.experiencelib.api.ExperienceStack;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class SyncableList {
  private final List<Syncable<?>> list;

  public <T> void sync(Syncable<T> syncable) {
    list.add(syncable);
  }

  public <T> void sync(Class<T> clazz, Supplier<T> getter, Consumer<T> setter) {
    sync(DataType.createSyncable(clazz, getter, setter));
  }

  @SuppressWarnings("unchecked")
  public <T extends Enum<T>> void syncEnum(Supplier<T> getter, Consumer<T> setter) {
    sync((Class<T>) getter.get().getClass(), getter, setter);
  }

  public void syncIdentifier(Supplier<Identifier> getter, Consumer<Identifier> setter) {
    sync(Identifier.class, getter, setter);
  }

  public void syncId(Supplier<Identifier> getter, Consumer<Identifier> setter) {
    syncIdentifier(getter, setter);
  }

  public void syncBool(Supplier<Boolean> getter, Consumer<Boolean> setter) {
    this.sync(Boolean.class, getter, setter);
  }

  public void syncFloat(Supplier<Float> getter, Consumer<Float> setter) {
    this.sync(Float.class, getter, setter);
  }

  public void syncDouble(Supplier<Double> getter, Consumer<Double> setter) {
    this.sync(Double.class, getter, setter);
  }

  public void syncInt(Supplier<Integer> getter, Consumer<Integer> setter) {
    this.sync(Integer.class, getter, setter);
  }

  public void syncString(Supplier<String> getter, Consumer<String> setter) {
    this.sync(String.class, getter, setter);
  }

  public void syncLong(Supplier<Long> getter, Consumer<Long> setter) {
    this.sync(Long.class, getter, setter);
  }

  public void syncNBT(Supplier<CompoundTag> getter, Consumer<CompoundTag> setter) {
    this.sync(CompoundTag.class, getter, setter);
  }

  public void syncComponent(Supplier<Component> getter, Consumer<Component> setter) {
    this.sync(Component.class, getter, setter);
  }

  public void syncItemStack(Supplier<ItemStack> getter, Consumer<ItemStack> setter) {
    this.sync(ItemStack.class, getter, setter);
  }

  public void syncFluidStack(Supplier<FluidStack> getter, Consumer<FluidStack> setter) {
    this.sync(FluidStack.class, getter, setter);
  }

  public void syncExperienceStack(Supplier<ExperienceStack> getter, Consumer<ExperienceStack> setter) {
    this.sync(ExperienceStack.class, getter, setter);
  }

  public void syncItemResource(Supplier<ItemResource> getter, Consumer<ItemResource> setter) {
    sync(ItemResource.class, getter, setter);
  }

  public void syncFluidResource(Supplier<FluidResource> getter, Consumer<FluidResource> setter) {
    sync(FluidResource.class, getter, setter);
  }
}
