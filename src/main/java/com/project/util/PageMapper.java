package com.project.util;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageMapper {

    public static <T, R> PageResponse<R> map(Page<T> page, Function<T, R> converter) {

        List<R> dtoList = page.getContent()
                .stream()
                .map(converter)
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
