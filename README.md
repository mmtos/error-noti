# 시스템 아키텍처
- errornoti (사용자의 알림요청을 받아 외부 알림서비스에 위임하는 메인 서비스)
- slack (errornoti로 부터 받은 알림을 실제로 전달하는 서비스, Mocking) 

![시스템 아키텍처](https://user-images.githubusercontent.com/36130931/159920348-8610011f-4554-44fb-8152-c4f5e6d98f4e.PNG)

# 사용자 정보 및 알림 그룹 정보 테이블 스키마
![테이블스키마](https://user-images.githubusercontent.com/36130931/159920476-d6fd5f7d-f23a-4bd2-b8b1-66195f383a50.PNG)

# API 명세
## errornoti
- POST /v1/alert : 알림 전송
  - 요청 (JSON) 
    - target(list) : 알림 대상
    - severity(string) : 장애 심각도
    - message(string) : 알림 메시지
- POST /v1/group : 알림 그룹 생성
  - 요청 (JSON)
    - notiGroupName : 생성할 그룹의 이름
- GET /v1/group/{notiGroupId} : 알림 그룹 조회
  - 요청 (JSON)
    - notiGroupId : 조회할 그룹의 ID 
- PUT /v1/enroll : 그룹에 유저 추가
  - 요청 (JSON)
    - notiGroupName : 유저를 등록할 그룹의 이름
    - notiUserName : 유저명
- DELETE /v1/withdraw/user/{notiUserName}/group/{notiGroupName} : 그룹에서 유저 삭제
  - 요청 (JSON)
    - notiGroupName : 유저를 삭제할 그룹의 이름
    - notiUserName : 유저명

## slack
- POST /api/chat.postMessage : 알림 정상 처리
    - 요청 (JSON)
      - channel : 알림을 게시할 채널
      - text : 알림 메시지
- POST /api/chat.postMessage.fail : 알림 실패 처리
    - 요청 (JSON)
      - channel : 알림을 게시할 채널
      - text : 알림 메시지
- POST /api/chat.postMessage.busy : 알림 지연 처리
    - 요청 (JSON)
      - channel : 알림을 게시할 채널
      - text : 알림 메시지
    
# 주요 외부 라이브러리 및 오픈 소스
라이브러리를 용도별로 분류 후, 라이브러리명, 버전, 사용목적을 명시

## 캐시 
- org.ehcache.ehcache (3.8.1) : DB 접근 횟수 줄이기, 사용자 및 그룹 매핑 정보 캐시
- javax.cache.cache-api (1.1.0) : ehcache 3버전 부터 JSR 107 표준을 따름
- org.springframework.boot.spring-boot-starter-cache : cache manager를 spring bean으로 구성

## API 호출
- org.apache.httpcomponents(4.5.13) : slack 앱에 알림전송 요청을 하기위해 사용

## Persistence
- org.mybatis.spring.boot.mybatis-spring-boot-starter(2.2.2) : 객체-DB 매핑을 위해 사용
- com.h2database.h2 : 과제 요구사항에 따름

## 장애 전파 방지 및 회복 
- io.github.resilience4j.resilience4j-spring-boot2(1.7.0) : 서킷브레이커를 통한 장애 전파 방지 및 fallback 메서드 사용을 위함

# 주요 설정 및 실행 방법
## 환경 변수 설정
- MOCK_SLACK_IP : 외부 알림 API IP
- MOCK_SLACK_PORT : 외부 알림 API PORT 
- MOCK_SLACK_TYPE : 외부 알림 API 상태 타입
  - normal : 정상 동작 중
  - busy : 지연 동작 중
  - fail : 항상 실패

## 실행 방법
1. docker 및 docker-compose가 설치된 리눅스에서 첨부파일 가져오기
2. submit 폴더로 이동하여 docker-compose up -d 명령어 실행
3. API 동작환경을 바꾸고 싶은 경우 docker-compose.yml 파일에 있는 MOCK_SLACK_TYPE을 normal, busy, fail 중 하나로 변경 후 2번 과정 수행
4. 컨테이너가 정상적으로 띄워지면 새로운 터미널을 2개 켜서 다음 명령어 각각 실행
   - sudo docker logs -f submit-errornoti-1
   - sudo docker logs -f submit-slack-1
   
5. 새로운 터미널을 열고 다음 curl 명령어로 시스템 동작 확인
```
curl --request POST 'http://localhost:8080/v1/alert' \
--header 'Content-Type: application/json' \
--data-raw '{
    "target": [
        "@all"
    ],
    "severity": "normal",
    "message": "node node"
}'
```
## 시스템 동작 확인
### busy
![busy_errornoti](https://user-images.githubusercontent.com/36130931/159920585-c86501a8-5992-4665-9954-56b08c941384.PNG)
![busy_slack](https://user-images.githubusercontent.com/36130931/159920597-62eba12e-6ba6-4573-89ff-08af751405b8.PNG)

### normal
![normal_errornoti](https://user-images.githubusercontent.com/36130931/159920646-6e4f18e5-dace-42cb-ae16-da096009b9d9.PNG)

### fail
![fail_errornoti](https://user-images.githubusercontent.com/36130931/159920641-dbb8f7a2-2cf9-4138-a3b0-2d214bbf6700.PNG)

# 추가 질문 답변
1. 장애로 인해 외부 오픈 API 응답이 지연되거나 일시적으로 사용할 수 없는 경우, 장애 전파를 막기 위한 기능을 구현하거나 해결 방안에 대해 상세히 기술해주세요.
```
Hystorix나 Resilence4J와 같이 서킷브레이커를 제공하는 프레임워크를 적용할 수 있습니다. 
느린 응답이나 실패 응답이 최근 시도한 요청에서 일정 비율을 차지하면 
외부 접속을 막고 미리 정의한 fallback동작을 하도록 구현 할 수 있습니다.
여기서는 Resilence4J를 사용하여 일시적 장애시 큐에 담도록 구현했습니다.
```
2. 외부 오픈 API가 초 당 처리할 수 있는 양이 제한되어있는 경우, 초과하는 외부 오픈 API 요청을 막기 위한 기능을 구현하거나 해결 방안에 대해 상세히 기술해주세요.
```
1번 질문의 경우 errornoti 입장에서 봤을때 Outbound에 대한 조치라면, 
서비스들이 떠있는 내부 시스템에서 부하를 분산할 수 있는 방법이 필요합니다. 
Istio와 같은 서비스 메시 오픈소스를 사용하면 inbound에서의 부하 분산이 가능한 것으로 알고 있습니다. 
서비스 내 Pod마다 SideCar를 배치하여 서비스간 통신을 제어한다면 
장애지점이 발생하더라도 어느 정도 대응할 수 있는 시간적 여유를 확보 할수 있다고 생각합니다. 
```
