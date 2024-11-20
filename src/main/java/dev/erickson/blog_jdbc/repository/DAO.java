package dev.erickson.blog_jdbc.repository;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Integer count();

    List<T> findAll();

    Optional<T> findById(Long id);

    int save(T t);

    int update(T t);

    int deleteById(Long id);
}
