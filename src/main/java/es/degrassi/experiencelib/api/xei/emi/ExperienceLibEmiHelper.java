package es.degrassi.experiencelib.api.xei.emi;

import dev.emi.emi.api.stack.EmiStack;
import es.degrassi.experiencelib.api.xei.ExperienceKey;
import es.degrassi.experiencelib.api.xei.ExperienceStack;

import java.util.Optional;

public class ExperienceLibEmiHelper implements IExperienceLibEmiHelper {
  public static final ExperienceLibEmiHelper INSTANCE = new ExperienceLibEmiHelper();

  private ExperienceLibEmiHelper() {}

  @Override
  public EmiStack createEmiStack(ExperienceKey experience, double size) {
    if (size < 1) return EmiStack.EMPTY;
    return new ExperienceEmiStack(size);
  }

  @Override
  public Optional<ExperienceStack> asExperienceStack(EmiStack stack) {
    if (stack instanceof ExperienceEmiStack s) {
      return Optional.of(s.getStack());
    }
    return Optional.empty();
  }
}
