# 🛡️ [Project] JUnit/Mockito로 검증된 견고한 Spring Boot 게시판 서비스

> **개발 기간:** 1주 (개인 프로젝트)

---

## 🚀 프로젝트 개요 (Overview)

본 프로젝트는 단순한 CRUD 기능을 넘어, **JUnit 5, Mockito, MockMvc**를 활용하여 애플리케이션의 **모든 비즈니스 로직과 통합 흐름을 검증**하는 데 초점을 맞춘 게시판 서비스입니다.

3계층 아키텍처(Controller-Service-Mapper)를 기반으로 설계되었으며, 동적 SQL, 서버 측 Validation, Docker 기반의 개발 환경 표준화 등 주니어 개발자로서 갖춰야 할 핵심 역량을 체계적으로 적용하고 입증했습니다.

---

## 🛠️ 핵심 기술 스택 (Tech Stack)

| 구분 | 기술 스택 | 적용 목적 및 주요 기능 |
| :---: | :---: | :--- |
| **Backend** | Java 21, Spring Boot 3.5.7, Spring Web | IoC, DI 기반의 계층형 아키텍처 구현 |
| **Persistence** | MyBatis, MySQL | 동적 SQL (`<where>`, `<choose>`) 활용 및 DB 레벨 페이징 |
| **Frontend** | Thymeleaf | 서버 사이드 렌더링(SSR) 및 폼 데이터 처리 |
| **Test/DevOps** | **JUnit 5, Mockito, MockMvc**, **Dev Container** (Docker) | **코드 품질 검증**, 개발 환경 표준화 및 격리 |

---

## 💎 주요 구현 기능 및 기술적 강점

### 1. 🏆 테스트 중심 개발 (TDD Approach & Verification)

프로젝트의 가장 강력한 강점입니다. 모든 비즈니스 로직과 통합 흐름을 검증하여 코드가 의도대로 정확히 작동함을 객관적으로 입증했습니다.

* **Service 단위 테스트 (Mockito):**
    * DB 의존성 없이 순수 비즈니스 로직(조회수 증가, DTO 변환)만 독립적으로 검증.
* `BDDMockito.given()`을 사용하여 가짜 데이터(Entity)를 주입하고, `then().should(times(1))`을 통해 **Mapper 호출 횟수 및 의존성 주입 여부**를 명확히 검증했습니다.* **Controller 통합 테스트 (MockMvc):**
    * HTTP 요청에 대한 응답(상태 코드, View, Model)이 정확한지 검증하여 프레임워크 통합 환경의 안정성 확인.
    * **Validation 실패 시나리오**를 테스트하여 **`model().attributeHasFieldErrors`**로 에러가 뷰에 올바르게 전달되는 코드의 **견고함**을 입증했습니다.

### 2. 🧩 견고한 아키텍처 및 설계 원칙

* **3계층 분리 및 DTO 활용:** Controller, Service, Mapper 구조를 명확히 분리하고, `BoardRequest`(입력) / `BoardResponse`(출력) DTO를 분리하여 **관심사 분리** 및 **보안성**을 확보했습니다.
* **MyBatis 동적 SQL 및 페이징:**
    * `<where>`, `<choose>`를 사용한 동적 쿼리로 복합 검색 조건에 단일 쿼리로 대응했습니다.
    * `LIMIT/OFFSET`을 적용하여 DB 부하를 최소화하는 **DB 레벨 페이징**을 구현했습니다.
* **서버 Validation:** `@Valid`와 `th:errors`를 활용하여 서버 측 데이터 무결성을 보장하고, Validation 실패 시 **302 Redirect 대신 200 OK 상태**와 함께 오류 View를 반환하여 사용자 친화적인 피드백을 제공했습니다.

### 3. 🧠 문제 해결 및 DevOps 경험

* **Dev Container 환경 표준화:** Docker 기반의 Dev Container를 구축하여 로컬 환경에 의존하지 않는 일관된 개발 환경을 확보했습니다.
    * **문제 해결:** 초기 DB 접속 오류 발생 시, **`docker ps` 명령**을 통해 컨테이너 상태를 확인하고 **애플리케이션 설정 파일의 DB 주소만 수정**하여 컨테이너 네트워크 환경에서의 연결 원리를 이해하고 문제를 해결했습니다.
* **AI 기반 디버깅:** 프레임워크 종속적인 복잡한 오류에 대해 AI 도구를 활용하여 근본 원인(Root Cause)을 빠르게 분석하고, **프레임워크 동작 원리를 역추적**하여 코드를 시스템에 안정적으로 통합하는 능력을 입증했습니다.

---

## 🔮 향후 계획 및 확장성

* **보안 강화:** Spring Security를 도입하여 사용자 인증/인가 및 권한 관리 기능 구현.
* **기능 확장:** 댓글 기능, 좋아요/싫어요 기능, 조회수 IP 고정 기능 추가.
* **배포:** Jenkins 또는 GitHub Actions를 활용한 CI/CD 파이프라인 구축 및 클라우드 배포 경험 확보.

---
