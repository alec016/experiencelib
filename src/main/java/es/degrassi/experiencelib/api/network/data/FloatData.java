package es.degrassi.experiencelib.api.network.data;

import es.degrassi.experiencelib.api.network.Data;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class FloatData extends Data<Float> {
  public FloatData(short id, Float value) {
    super(DataRegistration.FLOAT_DATA.get(), id, value);
  }

  public FloatData(short id, RegistryFriendlyByteBuf buffer) {
    this(id, buffer.readFloat());
  }

  public void writeData(RegistryFriendlyByteBuf buffer) {
    super.writeData(buffer);
    buffer.writeFloat(getValue());
  }
}
