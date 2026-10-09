package es.degrassi.experiencelib.data;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.common.ExperienceLibRegistration;
import es.degrassi.experiencelib.data.localization.LocalizationProvider;
import es.degrassi.experiencelib.data.localization.Lang;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = ExperienceLib.MODID)
public class ExperienceLibDataGenerators {

  @SubscribeEvent
  public static void onGatherData(GatherDataEvent.Client event) {
    var generator = event.getGenerator();
    var pack = generator.getVanillaPack(true);

    var localizations = LocalizationProvider.create(generator);

    var translationEvent = new TranslationLangEvent();
    ModLoader.postEventWrapContainerInModOrder(translationEvent);
    // Must run last
    for (var local : localizations) {
      pack.addProvider(_ -> local.withTranslations(translationEvent.get(local.getLang())));
    }
  }

  @SubscribeEvent
  public static void addTranslations(final TranslationLangEvent event) {
    var es_es = Lang.es_es;
    var xpBlockKey = "block.%s.%s".formatted(ExperienceLib.MODID, ExperienceLibRegistration.EXPERIENCE_BLOCK.id().getPath());
    var xpBucketKey = "item.%s.%s".formatted(ExperienceLib.MODID, ExperienceLibRegistration.EXPERIENCE_BUCKET.id().getPath());
    event.add(es_es, xpBlockKey, "Experiencia Líquida");
    event.add(es_es, xpBucketKey, "Cubo de Experiencia");
  }
}
