package es.degrassi.experiencelib.api.capability;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

public class ExperienceLibCapabilities {
  private ExperienceLibCapabilities() {}

  public static final CapSet<IExperienceHandler> EXPERIENCE = new CapSet<>(
      ExperienceLib.rl("experience_handler"),
      IExperienceHandler.class
  );

  public record CapSet<T>(BlockCapability<T, @Nullable Direction> block, ItemCapability<T, Void> item) {
    public CapSet(ResourceLocation name, Class<T> handlerClass) {
      this(BlockCapability.createSided(name, handlerClass), ItemCapability.createVoid(name, handlerClass));
    }
  }
}
