package es.degrassi.experiencelib.common.fluid;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import es.degrassi.experiencelib.common.ExperienceLibRegistration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExperienceFluid {
  public static class Flowing extends BaseFlowingFluid.Flowing {
    public Flowing() {
      super(
          new Properties(
              ExperienceLibRegistration.EXPERIENCE_FLUID_TYPE,
              ExperienceLibRegistration.EXPERIENCE_FLUID,
              ExperienceLibRegistration.EXPERIENCE_FLUID_FLOWING
          )
          .bucket(ExperienceLibRegistration.EXPERIENCE_BUCKET::get)
          .block(ExperienceLibRegistration.EXPERIENCE_BLOCK::block)
          .explosionResistance(100f)
      );
    }
  }

  public static class Source extends BaseFlowingFluid.Source {
    public Source() {
      super(
          new Properties(
              ExperienceLibRegistration.EXPERIENCE_FLUID_TYPE,
              ExperienceLibRegistration.EXPERIENCE_FLUID,
              ExperienceLibRegistration.EXPERIENCE_FLUID_FLOWING
          )
          .bucket(ExperienceLibRegistration.EXPERIENCE_BUCKET::get)
          .block(ExperienceLibRegistration.EXPERIENCE_BLOCK::block)
          .explosionResistance(100f)
      );
    }
  }
}
