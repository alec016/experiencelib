package es.degrassi.experiencelib.api.xei.jei;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.xei.ExperienceStack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public class ExperienceLibJeiPlugin implements IModPlugin {
  public static final ResourceLocation PLUGIN_ID = ExperienceLib.rl("jei_plugin");

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(IngredientTypes.EXPERIENCE, List.of(new ExperienceStack(1)),
        new ExperienceIngredientHelper(), new ExperienceStackRenderer(), ExperienceStack.CODEC.codec());
  }

  @Override
  public ResourceLocation getPluginUid() {
    return PLUGIN_ID;
  }
}
