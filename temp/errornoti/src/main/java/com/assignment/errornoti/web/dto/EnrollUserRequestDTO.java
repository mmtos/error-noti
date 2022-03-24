package com.assignment.errornoti.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnrollUserRequestDTO {
    private final String notiGroupName;
    private final String notiUserName;
}
