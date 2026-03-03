package es.degrassi.experiencelib;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ExperienceLibHooks {
  public record IntegrationInfo(String modid, boolean isLoaded) {
    private IntegrationInfo(String modid, Predicate<String> loadedCheck) {
      this(modid, loadedCheck.test(modid));
    }

    private void sendImc(String method, Supplier<?> toSend) {
      InterModComms.sendTo(modid, method, toSend);
    }

    public ResourceLocation rl(String path) {
      return ResourceLocation.fromNamespaceAndPath(modid, path);
    }

    public void assertLoaded() {
      if (!isLoaded) {
        throw new IllegalStateException(modid + " is not loaded");
      }
    }
  }

  public final IntegrationInfo emi;
  public final IntegrationInfo jei;

  public ExperienceLibHooks() {
    ModList modList = ModList.get();
    Predicate<String> loadedCheck = modList == null ? modid -> false : modList::isLoaded;
    jei = new IntegrationInfo("jei", loadedCheck);
    emi = new IntegrationInfo("emi", loadedCheck);
  }
}
