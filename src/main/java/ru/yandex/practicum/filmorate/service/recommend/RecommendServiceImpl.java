package ru.yandex.practicum.filmorate.service.recommend;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/*
 *    Collaborative Filtering
 */
@Service
public class RecommendServiceImpl<T, Y> implements RecommendService<T, Y> {

    private final HashMap<Y, HashMap<Y, Double>> diff;
    private final HashMap<Y, HashMap<Y, Integer>> freq;

    public RecommendServiceImpl() {
        diff = new HashMap<>();
        freq = new HashMap<>();
    }

    @Override
    public List<Y> recommend(T entity, HashMap<T, HashMap<Y, Integer>> data) {
        if (!data.containsKey(entity)) {
            return Collections.emptyList();
        } else {
            prepareDiffFreqMatrices(data);
            return predict(entity, data);
        }
    }

    private void prepareDiffFreqMatrices(HashMap<T, HashMap<Y, Integer>> data) {
        HashMap<Y, Double> diffKey;
        HashMap<Y, Integer> freqKey;
        int oldCount;
        double oldDiff;
        double observedDiff;
        Y key2;
        Y key;
        /*
         * Собираем diff & freq.
         */
        for (HashMap<Y, Integer> user : data.values()) {
            for (Map.Entry<Y, Integer> e : user.entrySet()) {
                key = e.getKey();
                if (!diff.containsKey(key)) {
                    diff.put(key, new HashMap<>());
                    freq.put(key, new HashMap<>());
                }
                diffKey = diff.get(key);
                freqKey = freq.get(key);
                for (Map.Entry<Y, Integer> e2 : user.entrySet()) {
                    key2 = e2.getKey();
                    oldCount = freqKey.getOrDefault(key2, 0);
                    oldDiff = diffKey.getOrDefault(key2, 0.0);
                    observedDiff = e.getValue() - e2.getValue();
                    freqKey.put(key2, oldCount + 1);
                    diffKey.put(key2, oldDiff + observedDiff);
                }
            }
        }
        /*
        Проапдейтили diff на средние значения.
         */
        int count;
        for (Y j : diff.keySet()) {
            diffKey = diff.get(j);
            for (Y i : diffKey.keySet()) {
                oldDiff = diffKey.get(i);
                count = freq.get(j).get(i);
                diffKey.put(i, oldDiff / count);
            }
        }
    }

    private List<Y> predict(T entity, HashMap<T, HashMap<Y, Integer>> data) {
        HashMap<Y, Integer> uFreq = new HashMap<>();
        int freqVal;
        HashMap<Y, Integer> userData = data.get(entity);
        if (userData == null) {
            return Collections.emptyList();
        }
        // 2 вычисляем только uFreq
        // 3 uPred.keySet() == diff.keySet()
        // uPred не нужен
        for (Y j : userData.keySet()) {
            for (Y k : diff.keySet()) {
                freqVal = freq.get(k).getOrDefault(j, 0);
                uFreq.put(k, uFreq.getOrDefault(k, 0) + freqVal);
            }
        }
        // 1 от uPred нужен только кисет
        Set<Y> predicted = diff
                .keySet()
                .stream()
                .filter(j -> uFreq.get(j) > 0)
                .collect(Collectors.toSet());
        predicted.removeAll(userData.keySet());
        return new ArrayList<>(predicted);
    }
}
