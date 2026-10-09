package es.degrassi.experiencelib.api.network;

import es.degrassi.experiencelib.common.network.server.SSyncDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public interface ISyncableHandler {
  List<ISyncable<?, ?>> getStuffToSync();
  BlockEntity getEntity();

  default boolean needFullSync() {
    return needFullSync(100);
  }

  default boolean needFullSync(int ticks) {
    return getEntity().getLevel() != null && getEntity().getLevel().getGameTime() % ticks == 0;
  }

  @ApiStatus.NonExtendable
  @SuppressWarnings({"unchecked", "rawtypes"})
  default void handleData(IData<?> data) {
    short id = data.getID();
    ISyncable syncable = getStuffToSync().get(id);
    if (syncable != null)
      syncable.set(data.getValue());
  }

  default void broadcastChanges() {
    if (getEntity().getLevel() instanceof ServerLevel level) {
      if (needFullSync()) {
        List<IData<?>> toSync = new ArrayList<>();
        for (short id = 0; id < getStuffToSync().size(); id++)
          toSync.add(getStuffToSync().get(id).getData(id));
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(getEntity().getBlockPos()), new SSyncDataPacket(getEntity().getBlockPos(), toSync));
        return;
      }
      List<IData<?>> toSync = new ArrayList<>();
      for (short id = 0; id < getStuffToSync().size(); id++)
        if (getStuffToSync().get(id).needSync())
          toSync.add(getStuffToSync().get(id).getData(id));
      if (toSync.isEmpty()) return;
      PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(getEntity().getBlockPos()), new SSyncDataPacket(getEntity().getBlockPos(), toSync));
    }
  }
}
