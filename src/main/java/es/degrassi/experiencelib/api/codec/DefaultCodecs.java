package es.degrassi.experiencelib.api.codec;

import com.mojang.serialization.DataResult;
import net.minecraft.IdentifierException;
import net.minecraft.resources.Identifier;

public class DefaultCodecs {
  private DefaultCodecs() {}

  public static final NamedCodec<Identifier> RESOURCE_LOCATION = NamedCodec.STRING.comapFlatMap(DefaultCodecs::decodeResourceLocation, Identifier::toString, "Resource location");

  private static DataResult<Identifier> decodeResourceLocation(String encoded) {
    try {
      return DataResult.success(Identifier.parse(encoded));
    } catch (IdentifierException e) {
      return DataResult.error(e::getMessage);
    }
  }
}
