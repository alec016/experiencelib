package es.degrassi.experiencelib.api.capability;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jetbrains.annotations.Nullable;

public class ExperienceLibCapabilities {
  private ExperienceLibCapabilities() {}

  public static final CapSet<IExperienceHandler> EXPERIENCE = new CapSet<>(
      ExperienceLib.rl("experience_handler"),
      IExperienceHandler.class
  );

  public record CapSet<T>(
      BlockCapability<T, @Nullable Direction> block,
      ItemCapability<T, ItemAccess> item,
      EntityCapability<T, Void> entity,
      EntityCapability<T, @Nullable Direction> entity_automation
  ) {
    public CapSet(Identifier name, Class<T> handlerClass) {
      this(
          BlockCapability.createSided(name, handlerClass),
          ItemCapability.create(name, handlerClass, ItemAccess.class),
          EntityCapability.createVoid(name, handlerClass),
          EntityCapability.createSided(name.withSuffix("_automation"), handlerClass)
      );
    }
  }
}
