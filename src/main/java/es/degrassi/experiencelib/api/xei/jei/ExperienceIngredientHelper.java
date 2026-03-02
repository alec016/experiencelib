package es.degrassi.experiencelib.api.xei.jei;

import es.degrassi.experiencelib.api.xei.ExperienceStack;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ExperienceIngredientHelper implements IIngredientHelper<ExperienceStack> {

  @Override
  public IIngredientType<ExperienceStack> getIngredientType() {
    return IngredientTypes.EXPERIENCE;
  }

  @Override
  public String getDisplayName(ExperienceStack ing) {
    return ing.getName().getString();
  }

  //Safe to remove
  @SuppressWarnings("removal")
  @Override
  public String getUniqueId(ExperienceStack ing, UidContext context) {
    return ing.getId().toString();
  }

  @Override
  public Object getUid(ExperienceStack ing, UidContext context) {
    return ing.getId().toString();
  }

  @Override
  public ExperienceStack copyIngredient(ExperienceStack ing) {
    return ing.copy();
  }

  @Override
  public String getErrorInfo(@Nullable ExperienceStack ing) {
    return "";
  }

  @Override
  public ResourceLocation getResourceLocation(ExperienceStack ingredient) {
    return ingredient.getId();
  }
}
