package ru.yandex.practicum.filmorate.service;

import java.util.HashMap;
import java.util.List;

public interface RecommendService<T, Y> {
    List<Y> recommend(T entity, HashMap<T, HashMap<Y, Integer>> data);
}
