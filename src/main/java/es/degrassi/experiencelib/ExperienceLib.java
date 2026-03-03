package es.degrassi.experiencelib;

import es.degrassi.experiencelib.api.capability.ExperienceLibCapabilities;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.impl.capability.BasicExperienceHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExperienceLib.MODID)
public class ExperienceLib {
  public static final String MODID = "experiencelib";
  public static final Logger LOGGER = LogManager.getLogger("ExperienceLib");
  public static final ExperienceLibHooks hooks = new ExperienceLibHooks();

  public ExperienceLib(final ModContainer CONTAINER, final IEventBus bus) {
    bus.addListener(this::addCapabilities);
  }

  private void addCapabilities(final RegisterCapabilitiesEvent event) {
    event.registerItem(ExperienceLibCapabilities.EXPERIENCE.item(), (x, y) -> {
      if (x.getItem() instanceof ExperienceBottleItem) {
        IExperienceHandler handler = new BasicExperienceHandler(1, 7, null);
        handler.setExperience(0, 7);
        return handler;
      } else if (x.getItem() instanceof BottleItem) {
        return new BasicExperienceHandler(1, 7, null);
      }
      return null;
    }, Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE);
  }

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(MODID, path);
  }
}
