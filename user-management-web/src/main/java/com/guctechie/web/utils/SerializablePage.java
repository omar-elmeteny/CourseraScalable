package com.guctechie.web.utils;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SerializablePage<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int size;
    private final int totalElements;
    private final int totalPages;

    public SerializablePage(ArrayList<T> data, int pageNumber, int pageSize, int totalElements) {
        this.content = data;
        this.pageNumber = pageNumber;
        this.size = pageSize;
        this.totalElements = totalElements;

        if (pageSize != 0) {
            this.totalPages = totalElements / pageSize;
        } else {
            this.totalPages = 0;
        }
    }

    public static <T, U> SerializablePage<T> of(List<U> data, int pageNumber, int pageSize, int totalElements, Mapper<T, U> mapper) {
        ArrayList<T> mappedData = new ArrayList<>();
        for (U u : data) {
            mappedData.add(mapper.map(u));
        }
        return new SerializablePage<>(mappedData, pageNumber, pageSize, totalElements);
    }

    public interface Mapper<T, U> {
        T map(U u);
    }
}
