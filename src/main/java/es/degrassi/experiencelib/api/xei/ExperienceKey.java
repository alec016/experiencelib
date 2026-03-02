package es.degrassi.experiencelib.api.xei;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.resources.ResourceLocation;

public class ExperienceKey {
  public final ResourceLocation getId() {
    return ExperienceLib.rl("experience");
  }
}
