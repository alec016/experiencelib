package es.degrassi.experiencelib.api.network;

@FunctionalInterface
public interface ISyncableStuff {
  void getStuffToSync(SyncableList list);
}
