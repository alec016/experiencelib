package es.degrassi.experiencelib.impl.capability;

import es.degrassi.experiencelib.api.capability.IContentsListener;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BasicExperienceTank implements IExperienceHandler, INBTSerializable<CompoundTag> {
  private long experience;
  private long capacity;
  private final @Nullable IContentsListener listener;

  public BasicExperienceTank(long capacity, @Nullable IContentsListener listener) {
    this.capacity = capacity;
    this.experience = 0L;
    this.listener = listener;
  }

  @Override
  public boolean canAcceptExperience(long experience) {
    return this.receiveExperience(experience, true) > 0;
  }

  @Override
  public boolean canProvideExperience(long experience) {
    return this.extractExperience(experience, true) > 0;
  }

  @Override
  public boolean canReceive() {
    return getMaxReceive() > 0;
  }

  @Override
  public boolean canExtract() {
    return getMaxExtract() > 0;
  }

  @Override
  public long getMaxExtract() {
    return capacity;
  }

  @Override
  public long getMaxReceive() {
    return capacity;
  }

  @Override
  public long getExperience() {
    return experience;
  }

  @Override
  public long getExperienceCapacity() {
    return capacity;
  }

  @Override
  public void setExperience(long experience) {
    this.experience = experience;
    if (listener != null) listener.onContentsChanged();
  }

  @Override
  public void setCapacity(long capacity) {
    this.capacity = capacity;
    if (listener != null) listener.onContentsChanged();
  }

  @Override
  public long receiveExperience(long toReceive, boolean simulate) {
    if (this.canReceive() && toReceive > 0) {
      long experienceReceived = Mth.clamp(this.capacity - this.experience, 0, Math.min(this.getMaxReceive(), toReceive));
      if (!simulate) {
        this.experience += experienceReceived;
        if (listener != null) listener.onContentsChanged();
      }

      return experienceReceived;
    } else {
      return 0;
    }
  }

  @Override
  public long extractExperience(long toExtract, boolean simulate) {
    if (this.canExtract() && toExtract > 0) {
      long experienceExtracted = Math.min(this.experience, Math.min(this.getMaxExtract(), toExtract));
      if (!simulate) {
        this.experience -= experienceExtracted;
        if (listener != null) listener.onContentsChanged();
      }

      return experienceExtracted;
    } else {
      return 0;
    }
  }

  @Override
  public long extractExperienceRecipe(long maxExtract, boolean simulate) {
    long extractable = this.experience - maxExtract < 0 ? this.experience : maxExtract;
    if (!simulate) {
      this.experience = clamp(this.experience - extractable, 0, getExperienceCapacity());
      if (listener != null) listener.onContentsChanged();
    }
    return extractable;
  }

  @Override
  public long receiveExperienceRecipe(long maxReceive, boolean simulate) {
    long insertable = this.experience + maxReceive > this.getExperienceCapacity() ?
        this.getExperienceCapacity() - this.experience :
        maxReceive;
    if (!simulate) {
      this.experience = clamp(this.experience + insertable, 0, this.getExperienceCapacity());
      if (listener != null) listener.onContentsChanged();
    }
    return insertable;
  }

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    CompoundTag tag = new CompoundTag();
    tag.putLong("experience", getExperience());
    tag.putLong("capacity", getExperienceCapacity());
    return tag;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    this.experience = nbt.getLong("experience");
    this.capacity = nbt.getLong("capacity");
  }
}
