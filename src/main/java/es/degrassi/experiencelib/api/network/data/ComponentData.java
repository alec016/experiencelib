package es.degrassi.experiencelib.api.network.data;

import es.degrassi.experiencelib.api.network.Data;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public class ComponentData extends Data<Component> {
  public ComponentData(short id, Component value) {
    super(DataRegistration.COMPONENT_DATA.get(), id, value);
  }
  public ComponentData(short id, RegistryFriendlyByteBuf buffer) {
    this(id, ComponentSerialization.STREAM_CODEC.decode(buffer));
  }

  @Override
  public void writeData(RegistryFriendlyByteBuf buffer) {
    super.writeData(buffer);
    ComponentSerialization.STREAM_CODEC.encode(buffer, getValue());
  }
}
