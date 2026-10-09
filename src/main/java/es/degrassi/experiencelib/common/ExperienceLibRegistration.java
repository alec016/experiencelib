package es.degrassi.experiencelib.common;

import com.google.common.base.Preconditions;
import es.degrassi.experiencelib.ExperienceLib;
import es.degrassi.experiencelib.api.codec.NamedCodec;
import es.degrassi.experiencelib.api.definition.BlockDefinition;
import es.degrassi.experiencelib.api.definition.ItemDefinition;
import es.degrassi.experiencelib.common.fluid.ExperienceFluid;
import es.degrassi.experiencelib.common.registry.DataRegistration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExperienceLibRegistration {
  private static final DeferredRegister.DataComponents DATA_COMPONENTS_REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExperienceLib.MODID);
  private static final DeferredRegister<Fluid> FLUID_REGISTER = DeferredRegister.create(Registries.FLUID, ExperienceLib.MODID);
  private static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(ExperienceLib.MODID);
  private static final DeferredRegister.Blocks BLOCKS_REGISTER = DeferredRegister.createBlocks(ExperienceLib.MODID);
  private static final DeferredRegister<FluidType> FLUID_TYPE_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, ExperienceLib.MODID);

  private static final List<BlockDefinition<?>> BLOCKS = new ArrayList<>();
  private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

  public static List<ItemDefinition<?>> getItems() {
    return Collections.unmodifiableList(ITEMS);
  }

  public static List<BlockDefinition<?>> getBlocks() {
    return Collections.unmodifiableList(BLOCKS);
  }

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> EXPERIENCE_COMPONENT =
      DATA_COMPONENTS_REGISTER.registerComponentType(
        "experience",
        builder -> builder.networkSynchronized(NamedCodec.LONG.streamCodec()).persistent(NamedCodec.LONG.codec())
      );

  public static final DeferredHolder<FluidType, FluidType> EXPERIENCE_FLUID_TYPE = FLUID_TYPE_REGISTER.register(
      "experience",
      () -> new FluidType(FluidType.Properties.create()
          .descriptionId("block.experiencelib.experience")
          .canExtinguish(true)
          .canConvertToSource(false)
          .lightLevel(15)
          .canHydrate(false)
          .canDrown(false)
          .canSwim(false)
          .supportsBoating(false)
          .canPushEntity(true)
          .isWaterLike(true)
          .density(2000)
          .viscosity(4000)
          .temperature(600)
          .rarity(Rarity.UNCOMMON)
          .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
          .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
          .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
      )
  );

  public static final DeferredHolder<Fluid, ExperienceFluid.Source> EXPERIENCE_FLUID = FLUID_REGISTER.register(
      "experience",
      ExperienceFluid.Source::new
  );

  public static final DeferredHolder<Fluid, ExperienceFluid.Flowing> EXPERIENCE_FLUID_FLOWING = FLUID_REGISTER.register(
      "flowing_experience",
      ExperienceFluid.Flowing::new
  );

  public static final ItemDefinition<BucketItem> EXPERIENCE_BUCKET = item(
      "Experience Bucket",
      ExperienceLib.rl("experience_bucket"),
      p -> new BucketItem(EXPERIENCE_FLUID.get(), p.craftRemainder(Items.BUCKET).stacksTo(1))
  );

  public static final BlockDefinition<LiquidBlock> EXPERIENCE_BLOCK = block(
      "Fluid Experience",
      ExperienceLib.rl("experience"),
      props -> new LiquidBlock(EXPERIENCE_FLUID.get(),
          props
              .mapColor(MapColor.WATER)
              .replaceable()
              .noCollision()
              .strength(100f)
              .pushReaction(PushReaction.DESTROY)
              .lightLevel(state -> Mth.clamp(state.getValueOrElse(LiquidBlock.LEVEL, 0), 7, 15))
              .noLootTable()
              .liquid()
              .sound(SoundType.EMPTY)
      )
  );

  private static <T extends Block> BlockDefinition<T> block(String englishName, Identifier id,
                                                            Function<BlockBehaviour.Properties, T> blockSupplier) {
    return block(englishName, id, blockSupplier, null);
  }

  private static <T extends Block> BlockDefinition<T> block(String englishName, Identifier id,
                                                            Function<BlockBehaviour.Properties, T> blockSupplier,
                                                            @Nullable BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
    Preconditions.checkArgument(id.getNamespace().equals(ExperienceLib.MODID));

    // Create block and matching item
    var deferredBlock = BLOCKS_REGISTER.registerBlock(id.getPath(), blockSupplier);
    var deferredItem = ITEM_REGISTER.register(id.getPath(), () -> {
      var block = deferredBlock.get();
      var itemProperties = new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))
          .useBlockDescriptionPrefix();
      if (itemFactory != null) {
        var item = itemFactory.apply(block, itemProperties);
        if (item  == null) throw new IllegalArgumentException("BlockItem factory for %s returned null".formatted(id.toShortString()));
        return item;
      } else {
        return new BlockItem(block, itemProperties);
      }
    });

    var itemDef = new ItemDefinition<>(englishName, deferredItem);
    BlockDefinition<T> definition = new BlockDefinition<>(englishName, deferredBlock, itemDef);
    BLOCKS.add(definition);
    return definition;
  }

  private static <T extends Item> ItemDefinition<T> item(String name, Identifier id,
                                                         Function<Item.Properties, T> factory) {
    Preconditions.checkArgument(id.getNamespace().equals(ExperienceLib.MODID));
    var definition = new ItemDefinition<>(name, ITEM_REGISTER.registerItem(id.getPath(), factory));
    ITEMS.add(definition);
    return definition;
  }

  private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> factory, Item.Properties props) {
    return ITEM_REGISTER.register(name, id -> factory.apply(props.setId(ResourceKey.create(Registries.ITEM, id))));
  }

  public static void register(IEventBus bus) {
    DATA_COMPONENTS_REGISTER.register(bus);
    DataRegistration.register(bus);
    BLOCKS_REGISTER.register(bus);
    ITEM_REGISTER.register(bus);
    FLUID_TYPE_REGISTER.register(bus);
    FLUID_REGISTER.register(bus);
  }
}
