package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"id"})
public class Film extends Entity {
    private Integer id;
    private String name;
    private String description;
    private Date releaseDate;
    private Integer duration;
    private List<Genre> genres;
    private RatingMpa mpa;
    private List<Directors> directors;

    public int getYear() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(releaseDate);
        return calendar.get(Calendar.YEAR);
    }
}
