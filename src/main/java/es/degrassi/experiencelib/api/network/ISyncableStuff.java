package es.degrassi.experiencelib.api.network;

import java.util.function.Consumer;

public interface ISyncableStuff {
  void getStuffToSync(Consumer<ISyncable<?, ?>> container);
}
