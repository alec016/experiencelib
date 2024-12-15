package es.degrassi.experiencelib.api.capability;

public interface IExperienceHandler {
  boolean canAcceptExperience(long experience);

  boolean canProvideExperience(long experience);

  long getMaxExtract();

  long getMaxReceive();

  default boolean canExtract() {
    return this.canProvideExperience(1);
  }

  default boolean canReceive() {
    return this.canAcceptExperience(1);
  }

  long getExperience();

  long getExperienceCapacity();

  void setExperience(long experience);
  void setCapacity(long experience);

  long receiveExperience(long experience, boolean simulate);

  long extractExperience(long experience, boolean simulate);

  long extractExperienceRecipe(long maxExtract, boolean simulate);
  long receiveExperienceRecipe(long maxReceive, boolean simulate);

  default long clamp(long num, long min, long max) {
    if (num < min) {
      return min;
    } else {
      return Math.min(num, max);
    }
  }
}
