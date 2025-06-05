package es.degrassi.experiencelib.util;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class ClientExperienceUtils {
  public static final Material EXPERIENCE = new Material(InventoryMenu.BLOCK_ATLAS, ExperienceLib.rl("block/experience"));
  public static final ResourceLocation EXPERIENCE_GUI = ExperienceLib.rl("textures/gui/experience.png");
}
