package es.degrassi.experiencelib.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class RegistrarCodec<V> implements NamedCodec<V> {
  public static final NamedCodec<ResourceLocation> EXL_LOC_CODEC = NamedCodec.STRING.comapFlatMap(
    s -> {
      try {
        if (s.contains(":"))
          return DataResult.success(ResourceLocation.tryParse(s));
        else
          return DataResult.success(ExperienceLib.rl(s));
      } catch (Exception e) {
        return DataResult.error(e::getMessage);
      }
    },
    ResourceLocation::toString,
    "ExpL Resource location"
  );

  public static <V> RegistrarCodec<V> of(Registry<V> registrar, boolean isExpL) {
    return new RegistrarCodec<>(registrar, isExpL);
  }

  private final Registry<V> registrar;
  private final boolean isExpL;

  private RegistrarCodec(Registry<V> registrar, boolean isExpL) {
    this.registrar = registrar;
    this.isExpL = isExpL;
  }

  @Override
  public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
    return (this.isExpL ? EXL_LOC_CODEC : DefaultCodecs.RESOURCE_LOCATION)
        .decode(ops, input)
        .flatMap(keyValuePair ->
          !this.registrar.containsKey(keyValuePair.getFirst())
            ? DataResult.error(() -> "Unknown registry key in " + this.registrar.key() + ": " + keyValuePair.getFirst())
            : DataResult.success(keyValuePair.mapFirst(this.registrar::get))
        );
  }

  @Override
  public <T> DataResult<T> encode(DynamicOps<T> ops, V input, T prefix) {
    return DefaultCodecs.RESOURCE_LOCATION.encode(ops, this.registrar.getKey(input), prefix);
  }

  @Override
  public String name() {
    return this.registrar.key().location().toString();
  }
}
