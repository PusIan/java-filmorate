package ru.yandex.practicum.filmorate.web.dto.request;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventTypeFeed;
import ru.yandex.practicum.filmorate.model.OperationFeed;

@Data
public class EventRequestDto {
    private Integer eventId;
    private Long timestamp;
    private Integer userId;
    private EventTypeFeed eventType;
    private OperationFeed operation;
    private Integer entityId;
}
