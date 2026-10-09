package es.degrassi.experiencelib.api.network.data;

import es.degrassi.experiencelib.api.network.Data;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

public class IdentifierData extends Data<Identifier> {
  public IdentifierData(short id, Identifier value) {
    super(DataRegistration.IDENTIFIER_DATA.get(), id, value);
  }

  public IdentifierData(short id, RegistryFriendlyByteBuf buffer) {
    this(id, buffer.readIdentifier());
  }

  @Override
  public void writeData(RegistryFriendlyByteBuf buffer) {
    super.writeData(buffer);
    buffer.writeIdentifier(getValue());
  }
}
