package es.degrassi.experiencelib.api.network;

import es.degrassi.experiencelib.common.network.server.SSyncDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ISyncableHandler {
  List<Syncable<?>> getStuffToSync();
  BlockEntity getEntity();

  default boolean needFullSync() {
    return needFullSync(100);
  }

  default boolean needFullSync(int ticks) {
    return getEntity().getLevel() != null && getEntity().getLevel().getGameTime() % ticks == 0;
  }

  @ApiStatus.NonExtendable
  @SuppressWarnings({"unchecked", "rawtypes"})
  default void handleData(Data<?> data) {
    try {
      short id = data.id();
      List<Syncable<?>> stuff = getStuffToSync();
      Objects.checkIndex(id, stuff.size());
      Syncable syncable = stuff.get(id);
      if (syncable != null)
        syncable.set(data.value());
    } catch(IndexOutOfBoundsException ignored) {}
  }

  default void broadcastChanges() {
    if (getEntity().getLevel() instanceof ServerLevel level) {
      if (needFullSync()) {
        List<Data<?>> toSync = new ArrayList<>();
        for (short id = 0; id < getStuffToSync().size(); id++)
          toSync.add(getStuffToSync().get(id).makeData(id));
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(getEntity().getBlockPos()), new SSyncDataPacket(getEntity().getBlockPos(), toSync));
        return;
      }
      List<Data<?>> toSync = new ArrayList<>();
      for (short id = 0; id < getStuffToSync().size(); id++)
        if (getStuffToSync().get(id).needSync())
          toSync.add(getStuffToSync().get(id).makeData(id));
      if (toSync.isEmpty()) return;
      PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(getEntity().getBlockPos()), new SSyncDataPacket(getEntity().getBlockPos(), toSync));
    }
  }
}
