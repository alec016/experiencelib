package es.degrassi.experiencelib.api.xei.jei;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.ExperienceStack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.resources.Identifier;

import java.util.List;

@JeiPlugin
public class ExperienceLibJeiPlugin implements IModPlugin {
  public static final Identifier PLUGIN_ID = ExperienceLib.rl("jei_plugin");

  @Override
  public Identifier getPluginUid() {
    return PLUGIN_ID;
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(IngredientTypes.EXPERIENCE, List.of(new ExperienceStack(1)),
        new ExperienceIngredientHelper(), new ExperienceStackRenderer(), ExperienceStack.CODEC.codec());
  }
}
