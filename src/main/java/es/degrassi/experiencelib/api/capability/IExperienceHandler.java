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

  long receiveExperience(long experience, boolean simulate);

  long extractExperience(long experience, boolean simulate);
}
