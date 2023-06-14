package ru.yandex.practicum.filmorate.web.mapper;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.web.dto.request.EventRequestDto;

public interface EventMapper {

    Event mapToEvent(EventRequestDto dto);
}
