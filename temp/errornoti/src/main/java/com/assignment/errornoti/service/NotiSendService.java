package com.assignment.errornoti.service;
import com.assignment.errornoti.vo.NotiMessage;

import java.io.IOException;

/**
 * 알림을 알림대상에게 보내기 위한 외부 API 호출을 담당
 * 문제 발생시 내부 MQ로 fallback하는 구현체 사용
 */
public interface NotiSendService {
    void send(NotiMessage messsge) throws IOException;
}
