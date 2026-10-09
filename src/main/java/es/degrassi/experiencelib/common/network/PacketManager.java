package es.degrassi.experiencelib.common.network;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.common.network.server.SSyncDataPacket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ExperienceLib.MODID)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PacketManager {
  @SubscribeEvent
  public static void register(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar(ExperienceLib.MODID);
    // TO CLIENT
    registrar.playToClient(SSyncDataPacket.TYPE, SSyncDataPacket.CODEC, SSyncDataPacket::handle);

    // TO SERVER
  }
}
