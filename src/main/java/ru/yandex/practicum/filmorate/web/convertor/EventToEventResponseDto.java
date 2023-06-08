package ru.yandex.practicum.filmorate.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.web.dto.response.EventResponseDto;

@Component
public class EventToEventResponseDto implements Converter<Event, EventResponseDto> {

    @Override
    public EventResponseDto convert(Event event) {
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setEventId(event.getEventId());
        eventResponseDto.setTimestamp(event.getTimestamp());
        eventResponseDto.setUserId(event.getUserId());
        eventResponseDto.setEventType(event.getEventType());
        eventResponseDto.setOperation(event.getOperation());
        eventResponseDto.setEntityId(event.getEntityId());
        return eventResponseDto;
    }
}
