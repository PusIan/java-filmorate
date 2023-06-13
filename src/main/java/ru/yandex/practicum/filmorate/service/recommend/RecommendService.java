package ru.yandex.practicum.filmorate.service.recommend;

import java.util.HashMap;
import java.util.List;

public interface RecommendService<T, Y> {
    List<Y> recommend(T entity, HashMap<T, HashMap<Y, Integer>> data);
}
