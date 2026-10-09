package es.degrassi.experiencelib.api.network.data;

import es.degrassi.experiencelib.api.network.Data;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class DoubleData extends Data<Double> {

    public DoubleData(short id, double value) {
        super(DataRegistration.DOUBLE_DATA.get(), id, value);
    }

    public DoubleData(short id, RegistryFriendlyByteBuf buffer) {
        this(id, buffer.readDouble());
    }

    @Override
    public void writeData(RegistryFriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeDouble(getValue());
    }
}
