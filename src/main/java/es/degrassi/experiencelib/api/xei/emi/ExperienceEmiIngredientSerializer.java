package es.degrassi.experiencelib.api.xei.emi;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.serializer.EmiStackSerializer;
import es.degrassi.experiencelib.api.xei.ExperienceStack;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;

public class ExperienceEmiIngredientSerializer implements EmiStackSerializer<ExperienceEmiStack> {
  @Override
  public EmiStack create(ResourceLocation id, DataComponentPatch ignored, long amount) {
    return id.equals(ExperienceStack.EMPTY.getId()) ? new ExperienceEmiStack(amount) : EmiStack.EMPTY;
  }

  @Override
  public String getType() {
    return "experiencelib_experiecne";
  }

  void addEmiStacks(EmiRegistry emiRegistry) {
    emiRegistry.addEmiStack(new ExperienceEmiStack(1));
  }
}
