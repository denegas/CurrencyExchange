package com.github.denegas.model.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();

    T insert(T t);


}
