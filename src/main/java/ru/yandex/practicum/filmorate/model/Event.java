package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"eventId"})
public class Event extends Entity {
    private Integer eventId;
    private Long timestamp;
    private Integer userId;
    private EventTypeFeed eventType;
    private OperationFeed operation;
    private Integer entityId;
}
