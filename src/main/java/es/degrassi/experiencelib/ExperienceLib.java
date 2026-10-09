package es.degrassi.experiencelib;

import com.mojang.logging.LogUtils;
import es.degrassi.experiencelib.api.capability.ExperienceLibCapabilities;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.api.network.DataType;
import es.degrassi.experiencelib.api.network.IData;
import es.degrassi.experiencelib.common.ExperienceLibRegistration;
import es.degrassi.experiencelib.common.config.ExperienceLibConfig;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import es.degrassi.experiencelib.transfer.ItemAccessExperienceHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.slf4j.Logger;

import java.util.Optional;

@Mod(ExperienceLib.MODID)
public class ExperienceLib {
  public static final String MODID = "experiencelib";
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final ExperienceLibHooks hooks = new ExperienceLibHooks();

  public ExperienceLib(IEventBus bus, ModContainer modContainer) {
    modContainer.registerConfig(ModConfig.Type.STARTUP, ExperienceLibConfig.getSpec());
    ExperienceLibRegistration.register(bus);
    bus.addListener(this::addCapabilities);
    bus.addListener(this::addExternal);
  }

  private void addExternal(final BuildCreativeModeTabContentsEvent contents) {
    if (CreativeModeTabs.TOOLS_AND_UTILITIES.equals(contents.getTabKey()))
      contents.accept(ExperienceLibRegistration.EXPERIENCE_BUCKET.stack());
  }

  private void addCapabilities(final RegisterCapabilitiesEvent event) {
    event.registerItem(ExperienceLibCapabilities.EXPERIENCE.item(), (x, y) -> {
      IExperienceHandler handler = new ItemAccessExperienceHandler(Optional.ofNullable(y).orElse(ItemAccess.forStack(x)), ExperienceLibRegistration.EXPERIENCE_COMPONENT.get(), ExperienceLibConfig.get().XP_AMOUNT.get());
      if (x.getItem() instanceof ExperienceBottleItem) {
        try (Transaction tx = Transaction.open(null)) {
          var inserted = handler.insert(0, ExperienceLibConfig.get().XP_AMOUNT.get(), tx);
          if (inserted > 0) tx.commit();
        }
      }
      return handler;
    }, Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE);
  }

  public static Registry<DataType<? extends IData<?>, ?>> dataRegistrar() {
    return DataRegistration.DATA_REGISTRY;
  }

  public static Identifier rl(String path) {
    return Identifier.fromNamespaceAndPath(MODID, path);
  }
}
