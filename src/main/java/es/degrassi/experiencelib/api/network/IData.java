package es.degrassi.experiencelib.api.network;

import es.degrassi.experiencelib.ExperienceLib;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

/**
 * Used to sync any kind of {@link Object} from server to client.
 * @param <T> The {@link Object} to sync.
 */
public interface IData<T> {

  /**
   * @return A registered {@link DataType} corresponding to this {@link IData}.
   */
  DataType<?, T> getType();

  /**
   * @return The ID of this {@link IData}, used by the container syncing packet to read/write the data in the proper order.
   */
  short getID();

  /**
   * @return The value of the object hold by this {@link IData}.
   */
  T getValue();

  /**
   * Override this method to pass write the Object hold by this IData to the PacketBuffer.
   * Overriding methods MUST call super.writeData() BEFORE writing their stuff into the PacketBuffer.
   * @param buffer The PacketBuffer that will be sent to the client.
   */
  default void writeData(RegistryFriendlyByteBuf buffer) {
    if(getType().getId() == null)
      throw new IllegalStateException("Attempting to write invalid data to Modular Machine container syncing packet : " + getType().toString() + " is not registered !");
    buffer.writeIdentifier(getType().getId());
    buffer.writeShort(getID());
  }

  /**
   * Utility method used by the container syncing packet to construct the IData on client side, from the PacketBuffer send by the server.
   * Don't touch this.
   */
  @SuppressWarnings("unchecked")
  static <T extends Enum<T>> IData<?> readData(RegistryFriendlyByteBuf buffer) {
    Identifier typeId = buffer.readIdentifier();
    var type = ExperienceLib.dataRegistrar().get(typeId).map(Holder::value).orElseThrow(() -> new IllegalStateException("Attempting to read invalid IData : " + typeId + " is not a valid registered DataType !"));
    short id = buffer.readShort();
    if (type instanceof EnumDataType<?>) {
      EnumDataType<T> enumType = (EnumDataType<T>) type;
      var className = buffer.readUtf();
      try {
        var c = Class.forName(className);
        if (!c.isEnum()) {
          return null;
        }
        return enumType.readData(id, (Class<T>) c, buffer);
      } catch(ClassNotFoundException | ClassCastException e) {
        return null;
      }
    }
    return type.readData(id, buffer);
  }
}
