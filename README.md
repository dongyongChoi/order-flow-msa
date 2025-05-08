# Order Flow MSA

`OrderFlowMSA`는 주문 관리 시스템을 MSA(Microservices Architecture) 구조로 구현한 학습용 프로젝트입니다.

유저 등록, 주문 생성, 주문 상태 흐름 등을 중심으로 마이크로서비스 간 통신, 분리된 DB, REST 연동을 단계적으로 구축하며, Kafka 기반 이벤트 처리, 인증 서비스, API Gateway 등을 차차 확장할 수 있도록 설계되었습니다.

## 사용 기술
- FeignClient
- Spring Cloud Gateway
- Eureka