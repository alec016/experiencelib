package es.degrassi.experiencelib.api.codec;

import com.mojang.serialization.DataResult;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

public class DefaultCodecs {
  private DefaultCodecs() {}

  public static final NamedCodec<ResourceLocation> RESOURCE_LOCATION = NamedCodec.STRING.comapFlatMap(DefaultCodecs::decodeResourceLocation, ResourceLocation::toString, "Resource location");


  private static DataResult<ResourceLocation> decodeResourceLocation(String encoded) {
    try {
      return DataResult.success(ResourceLocation.parse(encoded));
    } catch (ResourceLocationException e) {
      return DataResult.error(e::getMessage);
    }
  }
}
