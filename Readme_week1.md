# 1주차 과제

## 1. Use-Case Diagram

- ![Use-Case Diagram](https://github.com/user-attachments/assets/18981082-8938-4506-99fd-235d0bf48e9a)

## 2. 프로젝트 초기 세팅

### 2.1 Response 템플릿 구현

- `ResponseEntity<ApiResponse<T>>` 형식으로 응답 템플릿을 구현.

### 2.2 PostgreSQL Setting

### 2.3 Exception Handle

- 글로벌 예외 처리 메커니즘 설정.
- `@ControllerAdvice`와 `@ExceptionHandler` 사용.

### 2.4 BaseEntity Setting (Persistable 미적용)

- `BaseEntity` 추상 클래스 설정.
- `Persistable` 아직 미적용.

## 3. JPA Soft-delete 전략으로 테이블 및 Entity 설계

- ![Entity 설계 이미지](https://github.com/user-attachments/assets/a690f6e1-d308-40df-9b5a-d47ad2358b21)
- Soft-delete를 통해 데이터를 물리적으로 삭제하지 않고 비활성화.(isActive)

## 4. Spring Security 기초 세팅

- 게시물 패스워드 암호화 `BCryptPasswordEncoder` bean 등록.
- board api 접근 권한 해제

## 5. 과제 진행

### 5.1 게시글 등록 API

### 5.2 게시글 전체조회 API (비활성화 제외)

- 비활성화된 게시글은 제외하고 조회.

### 6. 단위테스트, 통합테스트 코드 구현

- 연속된 assertThat -> assertChaining 방식으로 변경 (가독성↑ 유지보수↑ 에러핸들링↑)
---


