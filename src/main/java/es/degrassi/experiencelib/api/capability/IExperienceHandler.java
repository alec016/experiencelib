package es.degrassi.experiencelib.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IExperienceHandler extends INBTSerializable<CompoundTag> {
  int getTanks();

  boolean canAcceptExperience(int tank, long amount);
  boolean canProvideExperience(int tank, long amount);

  long getMaxExtract(int tank);
  long getMaxReceive(int tank);

  default boolean canExtract(int tank) {
    return this.canProvideExperience(tank, 1);
  }

  default boolean canReceive(int tank) {
    return this.canAcceptExperience(tank, 1);
  }

  void setExperience(int tank, long amount);
  void setCapacity(int tank, long amount);

  long receiveExperience(int tank, long amount, boolean simulate);
  long extractExperience(int tank, long amount, boolean simulate);

  long receiveExperienceRecipe(int tank, long amount, boolean simulate);
  long extractExperienceRecipe(int tank, long amount, boolean simulate);

  long getExperience();
  long getExperienceCapacity();

  default long clamp(long num, long min, long max) {
    if (num < min)
      return min;
    return Math.min(num, max);
  }
}
