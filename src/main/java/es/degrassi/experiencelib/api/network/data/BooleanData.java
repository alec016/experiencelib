package es.degrassi.experiencelib.api.network.data;

import es.degrassi.experiencelib.api.network.Data;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class BooleanData extends Data<Boolean> {

    public BooleanData(short id, boolean value) {
        super(DataRegistration.BOOLEAN_DATA.get(), id, value);
    }

    public BooleanData(short id, RegistryFriendlyByteBuf buffer) {
        this(id, buffer.readBoolean());
    }

    @Override
    public void writeData(RegistryFriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeBoolean(getValue());
    }
}
