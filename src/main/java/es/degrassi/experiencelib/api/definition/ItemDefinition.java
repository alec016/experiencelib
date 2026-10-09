package es.degrassi.experiencelib.api.definition;

import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemDefinition<T extends Item> implements ItemLike, Supplier<T> {
  @Getter
  private final String englishName;
  private final DeferredItem<T> item;

  public ItemDefinition(String englishName, DeferredItem<T> item) {
    this.englishName = englishName;
    this.item = item;
  }

  public Identifier id() {
    return item.getId();
  }

  public ItemStack stack() {
    return stack(1);
  }

  public ItemStack stack(int stackSize) {
    return new ItemStack((ItemLike) item, stackSize);
  }

  public ItemStackTemplate template() {
    return template(1);
  }

  public ItemStackTemplate template(int stackSize) {
    return new ItemStackTemplate(item, stackSize);
  }

  public ItemStackTemplate template(Consumer<DataComponentPatch.Builder> customizer) {
    return template(1, customizer);
  }

  public ItemStackTemplate template(int stackSize, Consumer<DataComponentPatch.Builder> customizer) {
    var patch = DataComponentPatch.builder();
    customizer.accept(patch);
    return new ItemStackTemplate(item, stackSize, patch.build());
  }

  public Holder<Item> holder() {
    return item;
  }

  public Component getName() {
    return item.get().getName(item.get().getDefaultInstance());
  }
  /**
   * Compare {@link ItemStack} with this
   *
   * @param comparableStack compared item
   * @return true if the item stack is a matching item.
   */
  public final boolean is(ItemStack comparableStack) {
    var stack = stack();
    return !comparableStack.isEmpty() && !stack.isEmpty() && comparableStack.getItem() == stack.getItem();
  }

  @Override
  public T get() {
    return item.get();
  }

  @Override
  public T asItem() {
    return item.get();
  }
}
