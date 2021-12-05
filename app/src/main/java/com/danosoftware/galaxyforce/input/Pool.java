package com.danosoftware.galaxyforce.input;

import java.util.ArrayList;
import java.util.List;

class Pool<T> {

  private final List<T> freeObjects;
  private final PoolObjectFactory<T> factory;
  private final int maxSize;

  public Pool(PoolObjectFactory<T> factory, int maxSize) {
    this.factory = factory;
    this.maxSize = maxSize;
    this.freeObjects = new ArrayList<>(maxSize);
  }

  public T newObject() {
    T object;

    if (freeObjects.isEmpty()) {
      object = factory.createObject();
    } else {
      object = freeObjects.remove(freeObjects.size() - 1);
    }
    return object;
  }

  public void free(T object) {
    if (freeObjects.size() < maxSize) {
      freeObjects.add(object);
    }
  }

  public interface PoolObjectFactory<T> {

    T createObject();
  }
}
