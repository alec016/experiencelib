package es.degrassi.experiencelib.api.xei.emi;

import dev.emi.emi.api.stack.EmiStack;
import es.degrassi.experiencelib.api.xei.ExperienceKey;
import es.degrassi.experiencelib.api.xei.ExperienceStack;

import java.util.Optional;

public interface IExperienceLibEmiHelper {
  default EmiStack createEmiStack(ExperienceStack stack) {
    return createEmiStack(stack.getKey(), stack.getAmount());
  }

  EmiStack createEmiStack(ExperienceKey experience, double size);

  Optional<ExperienceStack> asExperienceStack(EmiStack stack);
}
