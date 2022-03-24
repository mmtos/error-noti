package com.assignment.errornoti.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetGroupResponseDTO {
    private String notiGroupId;
    private String notiGroupName;
}