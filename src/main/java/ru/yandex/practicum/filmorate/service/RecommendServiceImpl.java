package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.*;

/*
 *    Collaborative Filtering
 *    The Slope One algorithm is an item-based collaborative filtering system.
 *    It means that it is completely based on the user-item ranking. When we compute the
 *    similarity between objects, we only know the history of rankings, not the content itself.
 *    This similarity is then used to predict potential user rankings for user-item pairs not present in the dataset.
 *
 *    T - Entity which rates items
 *    Y - Type of Item, for which we have rating
 *
 *    Description: https://www.baeldung.com/java-collaborative-filtering-recommendations
 */
@Service
public class RecommendServiceImpl<T, Y> implements RecommendService<T, Y> {

    private final HashMap<Y, HashMap<Y, Double>> diff = new HashMap<>();
    private final HashMap<Y, HashMap<Y, Integer>> freq = new HashMap<>();

    @Override
    public List<Y> recommend(T entity, HashMap<T, HashMap<Y, Integer>> data) {
        if (!data.keySet().isEmpty() && data.containsKey(entity)) {
            prepareDiffFreqMatrices(data);
            return predict(entity, data);
        }
        return Collections.emptyList();
    }

    /*
     * Calculation of differences and frequencies matrices
     * */
    private void prepareDiffFreqMatrices(HashMap<T, HashMap<Y, Integer>> data) {
        /*
         * Based on the available data, we'll calculate the relationships between the items,
         * as well as the number of items' occurrences. For each user, we check his/her rating of the items
         * */
        for (HashMap<Y, Integer> user : data.values()) {
            for (Map.Entry<Y, Integer> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Y, Double>());
                    freq.put(e.getKey(), new HashMap<Y, Integer>());
                }

                for (Map.Entry<Y, Integer> e2 : user.entrySet()) {
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

    /*
     * The main part of the Slope One, we are going to predict all missing ratings based on the existing data.
     * In order to do that, we need to compare the user-item ratings
     * with differences matrix calculated in the prepareDiffFreqMatrices
     * */
    private List<Y> predict(T entity, HashMap<T, HashMap<Y, Integer>> data) {
        HashMap<Y, Double> uPred = new HashMap<>();
        HashMap<Y, Integer> uFreq = new HashMap<>();
        HashMap<Y, Integer> userData = data.get(entity);
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

            /* We should receive the predictions for items that user didn't rate,
            but also the repeated ratings for the items that he rated.
            Those repeated rates are not needed in our case, but can be used for algorithm validation.
            * */
            ret.removeAll(userData.keySet());

            return new ArrayList<>(ret);
        }
        return Collections.emptyList();
    }
}
