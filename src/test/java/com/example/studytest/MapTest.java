package com.example.studytest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.*;

public class MapTest {

    @DisplayName("")
    @Test
    void get() {
        // given
        Map<Long, String> map = new ConcurrentHashMap<>();
        map.put(1L, "one");
        map.put(2L, "two");

        // when
        List<Long> key = List.of(1L, 2L, 3L);
        List<String> values = key.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();

        // then
        assertThat(values).hasSize(2)
                .contains("one", "two");
    }
}
