package es.degrassi.experiencelib.common;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.codec.NamedCodec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExperieneLibRegistration {
  private static final DeferredRegister.DataComponents DATA_COMPONENTS_REGISTER =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExperienceLib.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> EXPERIENCE_COMPONENT =
      DATA_COMPONENTS_REGISTER.registerComponentType(
        "experience",
        builder -> builder.networkSynchronized(NamedCodec.LONG.streamCodec()).persistent(NamedCodec.LONG.codec())
      );

  public static void register(IEventBus bus) {
    DATA_COMPONENTS_REGISTER.register(bus);
  }
}
