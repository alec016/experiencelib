package es.degrassi.experiencelib.api;

import es.degrassi.experiencelib.api.codec.NamedCodec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.transfer.resource.Resource;

import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ExperienceResource implements Resource {
  public static final ExperienceResource EMPTY = new ExperienceResource(ExperienceStack.EMPTY);

  public static final NamedCodec<ExperienceResource> CODEC = ExperienceStack.fixedAmountCodec(1)
      .xmap(ExperienceResource::of, resource -> resource.toStack(1), "ExperienceResource");

  public static final NamedCodec<ExperienceResource> OPTIONAL_CODEC = NamedCodec.of(ExtraCodecs.optionalEmptyMap(CODEC.codec())
    .xmap(
        op -> op.orElse(ExperienceResource.EMPTY),
        res -> res.isEmpty() ? Optional.empty() : Optional.of(res)
    )
  );

  public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceResource> STREAM_CODEC = OPTIONAL_CODEC.streamCodec();

  public static ExperienceResource of(ExperienceStack stack) {
    if (stack.isEmpty()) return EMPTY;
    return new ExperienceResource(stack);
  }

  public static ExperienceResource of(double amount) {
    if (amount < 1) return EMPTY;
    return new ExperienceResource(new ExperienceStack(amount));
  }

  private final ExperienceStack innerStack;

  public double value() {
    return innerStack.getAmount();
  }

  public double getExperience() {
    return value();
  }

  @Override
  public boolean isEmpty() {
    return innerStack.isEmpty();
  }

  public ExperienceStack toStack(double amount) {
    if (amount < 1) return ExperienceStack.EMPTY;
    return innerStack.copyWithAmount(amount);
  }
}
