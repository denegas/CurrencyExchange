package com.github.denegas.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    List<T> getAll() throws SQLException;

    boolean insert(T t) throws SQLException;


}
