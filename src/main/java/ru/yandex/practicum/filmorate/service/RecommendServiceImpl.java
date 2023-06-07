package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendServiceImpl<T, Y> implements RecommendService<T, Y> {

    private final HashMap<Y, HashMap<Y, Double>> diff = new HashMap<>();
    private final HashMap<Y, HashMap<Y, Integer>> freq = new HashMap<>();


    @Override
    public List<Y> recommend(T entity, HashMap<T, HashMap<Y, Double>> data) {
        if (!data.keySet().isEmpty() && data.containsKey(entity)) {
            prepareDiffFreqMatrices(data);
            return predict(entity, data);
        }
        return Collections.emptyList();
    }

    private void prepareDiffFreqMatrices(HashMap<T, HashMap<Y, Double>> data) {
        for (HashMap<Y, Double> user : data.values()) {
            for (Map.Entry<Y, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Y, Double>());
                    freq.put(e.getKey(), new HashMap<Y, Integer>());
                }

                for (Map.Entry<Y, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }

                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }

                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }

                for (Y j : diff.keySet()) {
                    for (Y i : diff.get(j).keySet()) {
                        double oldValue = diff.get(j).get(i).doubleValue();
                        int count = freq.get(j).get(i).intValue();
                        diff.get(j).put(i, oldValue / count);
                    }
                }
            }
        }
    }

    private List<Y> predict(T entity, HashMap<T, HashMap<Y, Double>> data) {
        HashMap<Y, Double> uPred = new HashMap<>();
        HashMap<Y, Integer> uFreq = new HashMap<>();
        HashMap<Y, Double> userData = data.get(entity);
        if (userData != null) {
            for (Y j : userData.keySet()) {
                for (Y k : diff.keySet()) {
                    double predictedValue =
                            diff.get(k).getOrDefault(j, 0.0).doubleValue() + userData.get(j).doubleValue();
                    double finalValue = predictedValue * freq.get(k).getOrDefault(j, 0).intValue();
                    uPred.put(k, uPred.getOrDefault(k, 0.0) + finalValue);
                    uFreq.put(k, uFreq.getOrDefault(k, 0) + freq.get(k).getOrDefault(j, 0).intValue());
                }
            }
            HashMap<Y, Double> clean = new HashMap<Y, Double>();
            for (Y j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }

            Set<Y> ret = clean.keySet();
            ret.removeAll(userData.keySet());
            return new ArrayList<>(ret);
        }
        return Collections.emptyList();
    }
}
