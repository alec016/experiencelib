package es.degrassi.experiencelib.common.network.server;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.network.Data;
import es.degrassi.experiencelib.api.network.ISyncableHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SSyncDataPacket(BlockPos entityPos, List<Data<?>> data) implements CustomPacketPayload {
  public static final Type<SSyncDataPacket> TYPE = new Type<>(ExperienceLib.rl("sync_data"));

  public static final StreamCodec<RegistryFriendlyByteBuf, SSyncDataPacket> CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC,
      SSyncDataPacket::entityPos,
      Data.STREAM_CODEC.apply(ByteBufCodecs.list()),
      SSyncDataPacket::data,
      SSyncDataPacket::new
  );

  @Override
  public Type<SSyncDataPacket> type() {
    return TYPE;
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
