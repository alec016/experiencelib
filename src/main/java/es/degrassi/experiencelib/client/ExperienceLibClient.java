package es.degrassi.experiencelib.client;


import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.common.ExperienceLibRegistration;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = ExperienceLib.MODID)
public class ExperienceLibClient {
  @SubscribeEvent
  static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
    ExperienceLibRegistration.EXPERIENCE_FLUID_TYPE.asOptional().ifPresent(_ -> {
      Fluid stillFluid = ExperienceLibRegistration.EXPERIENCE_FLUID.value();
      Fluid flowingFluid = ExperienceLibRegistration.EXPERIENCE_FLUID_FLOWING.value();
      event.register(new FluidModel.Unbaked(
          new Material(ExperienceLib.rl("block/experience")),
          new Material(ExperienceLib.rl("block/experience")),
          new Material(ExperienceLib.rl("block/experience")),
          null), stillFluid, flowingFluid);
    });
  }
}
