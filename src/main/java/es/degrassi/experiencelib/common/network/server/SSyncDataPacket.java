package es.degrassi.experiencelib.common.network.server;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.network.IData;
import es.degrassi.experiencelib.api.network.ISyncableHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SSyncDataPacket(BlockPos entityPos, List<IData<?>> data) implements CustomPacketPayload {
  public static final Type<SSyncDataPacket> TYPE = new Type<>(ExperienceLib.rl("sync_data"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SSyncDataPacket> CODEC = StreamCodec.ofMember(SSyncDataPacket::write, SSyncDataPacket::read);

  @Override
  public Type<SSyncDataPacket> type() {
    return TYPE;
  }

  public void write(RegistryFriendlyByteBuf buf) {
    buf.writeBlockPos(this.entityPos);
    buf.writeShort(this.data.size());
    this.data.forEach(data -> data.writeData(buf));
  }

  public static SSyncDataPacket read(RegistryFriendlyByteBuf buf) {
    BlockPos pos = buf.readBlockPos();
    List<IData<?>> dataList = new ArrayList<>();
    short size = buf.readShort();
    for (short id = 0; id < size; id++) {
      IData<?> data = IData.readData(buf);
      if (data != null)
        dataList.add(data);
    }
    return new SSyncDataPacket(pos, dataList);
  }

  public static void handle(SSyncDataPacket packet, IPayloadContext context) {
    if (context.flow().isClientbound()) {
      context.enqueueWork(() -> {
        if (context.player().level().getBlockEntity(packet.entityPos()) instanceof ISyncableHandler handler) {
          packet.data().forEach(handler::handleData);
        }
      });
    }
  }
}
