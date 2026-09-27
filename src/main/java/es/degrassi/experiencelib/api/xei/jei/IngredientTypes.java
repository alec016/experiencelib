package es.degrassi.experiencelib.api.xei.jei;

import es.degrassi.experiencelib.api.ExperienceStack;
import mezz.jei.api.ingredients.IIngredientType;

public class IngredientTypes {
  public static final IIngredientType<ExperienceStack> EXPERIENCE = () -> ExperienceStack.class;
}
