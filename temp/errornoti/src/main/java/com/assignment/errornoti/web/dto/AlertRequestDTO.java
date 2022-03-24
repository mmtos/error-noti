package com.assignment.errornoti.web.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AlertRequestDTO {
    @Getter(AccessLevel.NONE)
    private final List<String> target;
    private final String severity;
    private final String message;

    public List<String> getTarget() {
        return Collections.unmodifiableList(target);
    }
}
