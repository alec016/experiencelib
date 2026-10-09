package es.degrassi.experiencelib.api.definition;

import lombok.Getter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BlockDefinition<T extends Block> implements ItemLike {
  @Getter
  private final String englishName;
  private final ItemDefinition<BlockItem> item;
  private final DeferredBlock<T> block;

  public BlockDefinition(String englishName, DeferredBlock<T> block, ItemDefinition<BlockItem> item) {
    this.englishName = englishName;
    this.block = block;
    this.item = item;
  }

  public Identifier id() {
    return block.getId();
  }

  public final T block() {
    return block.get();
  }

  public ItemStack stack() {
    return item.stack();
  }

  public ItemStack stack(int stackSize) {
    return item.stack(stackSize);
  }

  public boolean is(ItemStack comparableStack) {
    return item.is(comparableStack);
  }

  public ItemDefinition<BlockItem> item() {
    return item;
  }

  @Override
  public Item asItem() {
    return item.asItem();
  }
}
