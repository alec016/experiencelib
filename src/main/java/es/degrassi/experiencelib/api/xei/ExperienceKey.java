package es.degrassi.experiencelib.api.xei;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.resources.Identifier;

public class ExperienceKey {
  public final Identifier getId() {
    return ExperienceLib.rl("experience");
  }
}
