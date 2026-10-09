package es.degrassi.experiencelib.common.registry;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.network.DataType;
import es.degrassi.experiencelib.api.network.EnumDataType;
import es.degrassi.experiencelib.api.network.IData;
import es.degrassi.experiencelib.api.network.data.BooleanData;
import es.degrassi.experiencelib.api.network.data.ComponentData;
import es.degrassi.experiencelib.api.network.data.DoubleData;
import es.degrassi.experiencelib.api.network.data.EnumData;
import es.degrassi.experiencelib.api.network.data.FloatData;
import es.degrassi.experiencelib.api.network.data.IdentifierData;
import es.degrassi.experiencelib.api.network.data.IntegerData;
import es.degrassi.experiencelib.api.network.data.LongData;
import es.degrassi.experiencelib.api.network.data.NbtData;
import es.degrassi.experiencelib.api.network.data.StringData;
import es.degrassi.experiencelib.api.network.syncable.BooleanSyncable;
import es.degrassi.experiencelib.api.network.syncable.ComponentSyncable;
import es.degrassi.experiencelib.api.network.syncable.DoubleSyncable;
import es.degrassi.experiencelib.api.network.syncable.EnumSyncable;
import es.degrassi.experiencelib.api.network.syncable.FloatSyncable;
import es.degrassi.experiencelib.api.network.syncable.IdentifierSyncable;
import es.degrassi.experiencelib.api.network.syncable.IntegerSyncable;
import es.degrassi.experiencelib.api.network.syncable.LongSyncable;
import es.degrassi.experiencelib.api.network.syncable.NbtSyncable;
import es.degrassi.experiencelib.api.network.syncable.StringSyncable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataRegistration {
  public static final DeferredRegister<DataType<? extends IData<?>, ?>> DATAS =
      DeferredRegister.create(DataType.REGISTRY_KEY, ExperienceLib.MODID);
  public static final Registry<DataType<? extends IData<?>, ?>> DATA_REGISTRY = DATAS.makeRegistry(builder -> {});

  public static final Supplier<DataType<BooleanData, Boolean>> BOOLEAN_DATA = create("boolean",
      DataType.create(Boolean.class, BooleanSyncable::create, BooleanData::new));
  public static final Supplier<DataType<IntegerData, Integer>> INTEGER_DATA = create("integer",
      DataType.create(Integer.class, IntegerSyncable::create, IntegerData::new));
  public static final Supplier<DataType<DoubleData, Double>> DOUBLE_DATA = create("double",
      DataType.create(Double.class, DoubleSyncable::create, DoubleData::new));
  public static final Supplier<DataType<FloatData, Float>> FLOAT_DATA = create("float",
      DataType.create(Float.class, FloatSyncable::create, FloatData::new));
  public static final Supplier<DataType<StringData, String>> STRING_DATA = create("string",
      DataType.create(String.class, StringSyncable::create, StringData::new));
  public static final Supplier<DataType<LongData, Long>> LONG_DATA = create("long",
      DataType.create(Long.class, LongSyncable::create, LongData::new));
  public static final Supplier<DataType<NbtData, CompoundTag>> NBT_DATA = create("nbt",
      DataType.create(CompoundTag.class, NbtSyncable::create, NbtData::new));
  public static final Supplier<DataType<IdentifierData, Identifier>> IDENTIFIER_DATA = create("identifier",
      DataType.create(Identifier.class, IdentifierSyncable::create, IdentifierData::new));

  public static final Supplier<DataType<ComponentData, Component>> COMPONENT_DATA = create("component",
      DataType.create(Component.class, ComponentSyncable::create, ComponentData::new));
  public static final Supplier<EnumDataType<?>> ENUM_DATA = create("enum",
      EnumDataType.createEnum(EnumSyncable::create, EnumData::readData));

  private static <D extends IData<T>, T> Supplier<DataType<D, T>> create(String path, DataType<D, T> type) {
    return DATAS.register(path.toLowerCase(Locale.ENGLISH), () -> type);
  }

  private static Supplier<EnumDataType<?>> create(String path, EnumDataType<?> type) {
    return DATAS.register(path.toLowerCase(Locale.ENGLISH), () -> type);
  }

  public static void register(final IEventBus bus) {
    DATAS.register(bus);
  }
}
