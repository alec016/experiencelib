package es.degrassi.experiencelib.util;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;

public class ClientExperienceUtils {
  public static final Material.Baked EXPERIENCE = new Material.Baked(
      Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.GUI).getSprite(ExperienceLib.rl("experience")),
      true
  );
  public static final Identifier EXPERIENCE_GUI = ExperienceLib.rl("textures/gui/experience.png");
}
