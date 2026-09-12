package es.degrassi.experiencelib.impl.capability;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import es.degrassi.experiencelib.api.capability.IContentsListener;
import es.degrassi.experiencelib.api.capability.IExperienceHandler;
import es.degrassi.experiencelib.api.capability.IExperienceTank;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BasicExperienceHandler implements IExperienceHandler {
  private static final String TANKS_NBT = "tanks";
  private static final String TANKS_SIZE_NBT = "tank_size";
  private static final String DEFAULT_CAPACITY_NBT = "default_capacity";
  private final List<IExperienceTank> tanks;
  private long defaultCapacity;

  public BasicExperienceHandler(int tanksNumber, long capacity, @Nullable IContentsListener listener) {
    this.tanks = createTanks(tanksNumber, capacity, listener);
    this.defaultCapacity = capacity;
  }

  private static List<IExperienceTank> createTanks(int tanksNumber, long capacity, @Nullable IContentsListener listener) {
    List<IExperienceTank> tanks = new ArrayList<>();
    if (tanksNumber < 1)
      throw new IllegalArgumentException("Experience Tanks can not be less than 1");
    for (int i = 0; i < tanksNumber; i++) {
      var tank = new BasicExperienceTank(capacity, listener);
      tanks.add(tank);
    }
    return tanks;
  }

  private void checkTankIndex(int tank) {
    if (tank < 0 || tank >= getTanks())
      throw new IllegalArgumentException("Tank index can not be less than 0 or greater than " + getTanks());
  }

  @Override
  public int getTanks() {
    return tanks.size();
  }

  @Override
  public boolean canAcceptExperience(int tank, long amount) {
    checkTankIndex(tank);
    return tanks.get(tank).canAcceptExperience(amount);
  }

  @Override
  public boolean canProvideExperience(int tank, long amount) {
    checkTankIndex(tank);
    return tanks.get(tank).canProvideExperience(amount);
  }

  @Override
  public long getMaxExtract(int tank) {
    checkTankIndex(tank);
    return tanks.get(tank).getMaxExtract();
  }

  @Override
  public long getMaxReceive(int tank) {
    checkTankIndex(tank);
    return tanks.get(tank).getMaxReceive();
  }

  @Override
  public void setExperience(int tank, long amount) {
    checkTankIndex(tank);
    tanks.get(tank).setExperience(amount);
  }

  @Override
  public void setCapacity(int tank, long amount) {
    checkTankIndex(tank);
    tanks.get(tank).setCapacity(amount);
  }

  @Override
  public long receiveExperience(int tank, long amount, boolean simulate) {
    checkTankIndex(tank);
    return tanks.get(tank).receiveExperience(amount, simulate);
  }

  @Override
  public long extractExperience(int tank, long amount, boolean simulate) {
    checkTankIndex(tank);
    return tanks.get(tank).extractExperience(amount, simulate);
  }

  @Override
  public long receiveExperienceRecipe(int tank, long amount, boolean simulate) {
    checkTankIndex(tank);
    return tanks.get(tank).receiveExperienceRecipe(amount, simulate);
  }

  @Override
  public long extractExperienceRecipe(int tank, long amount, boolean simulate) {
    checkTankIndex(tank);
    return tanks.get(tank).extractExperienceRecipe(amount, simulate);
  }

  @Override
  public long getExperience() {
    return tanks.stream().mapToLong(IExperienceTank::getExperience).sum();
  }

  @Override
  public long getExperienceCapacity() {
    return tanks.stream().mapToLong(IExperienceTank::getExperienceCapacity).sum();
  }

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    CompoundTag nbt = new CompoundTag();
    ListTag tanksTag = new ListTag();
    tanks.forEach(tank -> {
      tanksTag.add(tank.serializeNBT(provider));
    });
    nbt.putInt(TANKS_SIZE_NBT, getTanks());
    nbt.put(TANKS_NBT, tanksTag);
    nbt.putLong(DEFAULT_CAPACITY_NBT, defaultCapacity);
    return nbt;
  }


  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    Optional<Integer> tankSize = nbt.getInt(TANKS_SIZE_NBT);
    Optional<ListTag> tanksTag = nbt.getList(TANKS_NBT);
    tanksTag.ifPresent(tanks -> {
      for (int i = 0; i < tankSize.orElse(0); i++) {
        var tankNbt = tanks.getCompound(i);
        int finalI = i;
        tankNbt.ifPresent(n -> this.tanks.get(finalI).deserializeNBT(provider, n));
      }
      defaultCapacity = nbt.getLong(DEFAULT_CAPACITY_NBT).orElse(0L);
    });
  }
}
