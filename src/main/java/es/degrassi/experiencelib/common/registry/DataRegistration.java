package es.degrassi.experiencelib.common.registry;

import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.codec.NamedCodec;
import es.degrassi.experiencelib.api.network.DataType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Locale;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("rawtypes, unchecked")
public final class DataRegistration {
  public static final DeferredRegister<DataType<?>> DATAS = DeferredRegister.create(DataType.REGISTRY_KEY, ExperienceLib.MODID);
  public static final Registry<DataType<?>> DATA_REGISTRY = DATAS.makeRegistry(builder -> builder.sync(true));

  public static final Supplier<DataType<Boolean>> BOOLEAN_DATA = create("boolean",
      DataType.create(Boolean.class, ByteBufCodecs.BOOL.cast()));
  public static final Supplier<DataType<Integer>> INTEGER_DATA = create("integer",
      DataType.create(Integer.class, ByteBufCodecs.INT.cast()));
  public static final Supplier<DataType<Double>> DOUBLE_DATA = create("double",
      DataType.create(Double.class, ByteBufCodecs.DOUBLE.cast()));
  public static final Supplier<DataType<Float>> FLOAT_DATA = create("float",
      DataType.create(Float.class, ByteBufCodecs.FLOAT.cast()));
  public static final Supplier<DataType<String>> STRING_DATA = create("string",
      DataType.create(String.class, ByteBufCodecs.STRING_UTF8.cast()));
  public static final Supplier<DataType<Long>> LONG_DATA = create("long",
      DataType.create(Long.class, ByteBufCodecs.LONG.cast()));
  public static final Supplier<DataType<CompoundTag>> NBT_DATA = create("nbt",
      DataType.create(CompoundTag.class, ByteBufCodecs.fromCodec(CompoundTag.CODEC).cast(), CompoundTag::copy));
  public static final Supplier<DataType<Identifier>> IDENTIFIER_DATA = create("identifier",
      DataType.create(Identifier.class, Identifier.STREAM_CODEC.cast()));

  public static final Supplier<DataType<Component>> COMPONENT_DATA = create("component",
      DataType.create(Component.class, ComponentSerialization.STREAM_CODEC.cast(), Component::copy));
  public static final Supplier<DataType<Enum>> ENUM_DATA = create("enum", DataType.createEnum(Enum.class));

  public static final Supplier<DataType<ItemStack>> ITEMSTACK_DATA = create("itemstack",
      DataType.create(ItemStack.class, ItemStack.OPTIONAL_STREAM_CODEC.cast(), ItemStack::copy));
  public static final Supplier<DataType<FluidStack>> FLUIDSTACK_DATA = create("fluidstack",
      DataType.create(FluidStack.class, FluidStack.OPTIONAL_STREAM_CODEC.cast(), FluidStack::copy));
  // Resources
  public static final Supplier<DataType<FluidResource>> FLUID_RESOURCE_DATA = create("fluid_resource",
      DataType.create(FluidResource.class, FluidResource.STREAM_CODEC.cast(), r -> FluidResource.of(
          r.typeHolder(),
          r.getComponentsPatch()
      )));
  public static final Supplier<DataType<ItemResource>> ITEM_RESOURCE_DATA = create("item_resource",
      DataType.create(ItemResource.class, ItemResource.STREAM_CODEC.cast(), r -> ItemResource.of(
          r.typeHolder(),
          r.getComponentsPatch()
      )));

  private static <T> Supplier<DataType<T>> create(String path, DataType<T> type) {
    return DATAS.register(path.toLowerCase(Locale.ENGLISH), () -> type);
  }

  public static void register(final IEventBus bus) {
    DATAS.register(bus);
  }
}
