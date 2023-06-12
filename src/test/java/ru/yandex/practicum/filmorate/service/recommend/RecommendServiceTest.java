package ru.yandex.practicum.filmorate.service.recommend;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ru.yandex.practicum.filmorate.web.starter.FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RecommendServiceTest {
    private final RecommendFilmService recommendService;

    @Test
    public void testRecommendNotMatchingRatingEmptyCase() {
        Integer entity1 = 1;
        Integer entity2 = 2;
        HashMap<Integer, HashMap<Integer, Integer>> data = new HashMap<>();
        data.put(entity1, new HashMap<>(Map.of(1, 1, 2, 1)));
        data.put(entity2, new HashMap<>(Map.of(3, 1, 4, 1)));
        assertAll(() -> assertThat(recommendService.recommend(entity1, data)).isEqualTo(Collections.emptyList()),
                () -> assertThat(recommendService.recommend(entity2, data)).isEqualTo(Collections.emptyList()));
    }

    @Test
    public void testRecommendMatchingRatingSimpleCase() {
        Integer entity1 = 1;
        Integer entity2 = 2;
        Integer entity3 = 3;
        HashMap<Integer, HashMap<Integer, Integer>> data = new HashMap<>();
        data.put(entity1, new HashMap<>(Map.of(1, 1, 3, 1)));
        data.put(entity2, new HashMap<>(Map.of(1, 1)));
        data.put(entity3, new HashMap<>(Map.of(2, 1)));
        assertAll(() -> assertThat(recommendService.recommend(entity1, data)).isEqualTo(Collections.emptyList()),
                () -> assertThat(recommendService.recommend(entity2, data)).isEqualTo(List.of(3)),
                () -> assertThat(recommendService.recommend(entity3, data)).isEqualTo(Collections.emptyList()));
    }

    @Test
    public void testRecommendEmptyCasesEmptyResult() {
        Integer entity1 = 1;
        Integer entity2 = 2;
        HashMap<Integer, HashMap<Integer, Integer>> data = new HashMap<>();
        data.put(entity1, new HashMap<>());
        data.put(entity2, new HashMap<>());
        assertAll(() -> assertThat(recommendService.recommend(entity1, data)).isEqualTo(Collections.emptyList()),
                () -> assertThat(recommendService.recommend(entity2, data)).isEqualTo(Collections.emptyList()),
                () -> assertThat(recommendService.recommend(-1, data)).isEqualTo(Collections.emptyList()),
                () -> assertThat(recommendService.recommend(entity1, new HashMap<>())).isEqualTo(Collections.emptyList()),
                () -> assertThat(recommendService.recommend(-1, new HashMap<>())).isEqualTo(Collections.emptyList()));
    }
}
