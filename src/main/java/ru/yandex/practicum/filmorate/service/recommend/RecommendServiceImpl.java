package ru.yandex.practicum.filmorate.service.recommend;

import org.springframework.stereotype.Service;

import java.util.*;

/*
 *    Collaborative Filtering
 */
@Service
public class RecommendServiceImpl<T, Y> implements RecommendService<T, Y> {
    private final HashMap<Y, HashMap<Y, Integer>> freq;

    public RecommendServiceImpl() {
        freq = new HashMap<>();
    }

    @Override
    public List<Y> recommend(T entity, HashMap<T, HashMap<Y, Integer>> data) {
        prepareDiffFreqMatrices(data);
        return predict(entity, data);
    }

    private void prepareDiffFreqMatrices(HashMap<T, HashMap<Y, Integer>> data) {
        HashMap<Y, Integer> freqKey;
        int oldCount;
        Y key2;
        Y key;
        for (HashMap<Y, Integer> user : data.values()) {
            for (Map.Entry<Y, Integer> e : user.entrySet()) {
                key = e.getKey();
                if (!freq.containsKey(key)) {
                    freq.put(key, new HashMap<>());
                }
                freqKey = freq.get(key);
                for (Map.Entry<Y, Integer> e2 : user.entrySet()) {
                    key2 = e2.getKey();
                    oldCount = freqKey.getOrDefault(key2, 0);
                    freqKey.put(key2, oldCount + 1);
                }
            }
        }
    }
    /**
     * Сужение цикла, если k уже добавлен в predicted.
     */
    private List<Y> predict(T entity, HashMap<T, HashMap<Y, Integer>> data) {
        Set<Y> userFilmsId = data.getOrDefault(entity, new HashMap<>()).keySet();
        Set<Y> predicted = new HashSet<>();
        Set<Y> copy = freq.keySet();
        for (Y j : userFilmsId) {
            if (copy.isEmpty()) {
                break;
            }
            for (Y k: new HashSet<>(copy)) {
                if (freq.get(k).getOrDefault(j, 0) > 0) {
                    predicted.add(k);
                    copy.remove(k);
                }
            }
        }
        predicted.removeAll(userFilmsId);
        return new ArrayList<>(predicted);
    }
}
