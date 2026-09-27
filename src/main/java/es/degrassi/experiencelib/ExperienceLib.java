package es.degrassi.experiencelib;

import com.mojang.logging.LogUtils;
import es.degrassi.experiencelib.api.capability.ExperienceLibCapabilities;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.common.ExperieneLibRegistration;
import es.degrassi.experiencelib.transfer.ItemAccessExperienceHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.slf4j.Logger;

import java.util.Optional;

@Mod(ExperienceLib.MODID)
public class ExperienceLib {
  public static final String MODID = "experiencelib";
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final ExperienceLibHooks hooks = new ExperienceLibHooks();

  public ExperienceLib(IEventBus bus, ModContainer modContainer) {
    ExperieneLibRegistration.register(bus);
    bus.addListener(this::addCapabilities);
  }

  private void addCapabilities(final RegisterCapabilitiesEvent event) {
    event.registerItem(ExperienceLibCapabilities.EXPERIENCE.item(), (x, y) -> {
      IExperienceHandler handler = new ItemAccessExperienceHandler(Optional.ofNullable(y).orElse(ItemAccess.forStack(x)), ExperieneLibRegistration.EXPERIENCE_COMPONENT.get(), 7);
      if (x.getItem() instanceof ExperienceBottleItem) {
        handler.setExperience(0, 7);
      }
      return handler;
    }, Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE);
  }

  public static Identifier rl(String path) {
    return Identifier.fromNamespaceAndPath(MODID, path);
  }
}
