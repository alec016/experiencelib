package es.degrassi.experiencelib.api.xei.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

@EmiEntrypoint
public class ExperienceLibEmiPlugin implements EmiPlugin {
  private static final ExperienceEmiIngredientSerializer EXPERIENCE_SERIALIZER = new ExperienceEmiIngredientSerializer();
  @Override
  public void initialize(EmiInitRegistry registry) {
    registry.addIngredientSerializer(ExperienceEmiStack.class, EXPERIENCE_SERIALIZER);
  }

  @Override
  public void register(EmiRegistry registry) {
    EXPERIENCE_SERIALIZER.addEmiStacks(registry);
  }
}
