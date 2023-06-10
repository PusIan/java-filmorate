package ru.yandex.practicum.filmorate.service.recommend;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface RecommendService<T, Y> {
    List<Y> recommend(T entity, HashMap<T, HashMap<Y, Integer>> data);
}
