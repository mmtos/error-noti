package com.mock.slack.noti;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotiRequestDTO {
    private final String channel;
    private final String text;
}
