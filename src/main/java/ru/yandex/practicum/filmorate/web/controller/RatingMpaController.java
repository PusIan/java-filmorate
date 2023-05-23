package ru.yandex.practicum.filmorate.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.RatingMpaService;
import ru.yandex.practicum.filmorate.web.dto.response.RatingMpaResponseDto;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaController {
    private final ConversionService conversionService;
    private final RatingMpaService ratingMpaService;

    @GetMapping
    public Collection<RatingMpaResponseDto> getAll() {
        return ratingMpaService.getAll().stream()
                .map(ratingMpa -> conversionService.convert(ratingMpa, RatingMpaResponseDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{ratingMpaId}")
    public RatingMpaResponseDto getById(@PathVariable int ratingMpaId) {
        return conversionService.convert(ratingMpaService.getById(ratingMpaId), RatingMpaResponseDto.class);
    }
}