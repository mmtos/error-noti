package com.assignment.errornoti.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotiMessage {
    private String token; // api를 보내기 위한 토큰
    private String channel; // 채널
    private String text;  // 알림 텍스트
    public String toJsonString(){
        return "{\"channel\":\""+channel+"\",\"text\":\""+text+"\"}";
    }
}
