# Review Queue
- 학습한 내용을 주기적으로 복습하도록 도와주는 웹 서비스
- 서비스 웹 링크

### 1. 기술 스택
- **Language** : Java17
- **Framework** : Spring Boot 3.3.5, Spring Data JPA
- **DB** : MySQL, Redis
- **Infra** : Docker, Jenkins

### 2. 서버 아키텍처

### 3. ERD

### 4. 서비스 페이지 스크린 샷

### 10. 주요 시나리오
- **카카오톡 소셜 로그인을 활용해 회원 가입을 한다.**
- **원하는 학습을 생성한다.**
  - 강의, 도서, 코테의 세 가지 분류
- **원하는 학습을 하고 일일 학습 내용을 등록한다.**
  - 학습 주제, 학습 범위, 핵심 키워드, 핵심 키워드 내용을 저장
- **일일 학습에 대해 복습 횟수, 복습 주기를 설정한다.**
  - 일일 학습별로 복습 조건을 추가
  - 복습 횟수는 0 ~ 5회, 복습 주기는 1일 ~ 14일
- **복습 조건에 맞게 복습 큐에 추가**
- **복습일에 해당하면 알림 전송**
  - 사용자는 알림을 통해 복습할 주제, 키워드를 확인
  - 키워드를 클릭하면 해당 키워드에 대해 저장된 상세 설명이 노출
  - 사용자가 온라인인 경우엔 실시간 전송
  - 사용자가 오프라인인 경우엔 DB에 알림 내역을 저장하여 로그인시 미확인 알림 표시
- **복습 알림을 통해 내용 확인 후, 실제 복습 내용을 학습**
  - 복습 알림엔 알림 아이디, 복습 아이디, 알림 일자, 복습 주제에 대한 내용이 표시됨
  - 해당 알림을 클릭하면 알림이 '읽음'처리 되고, 조건에 해당하는 복습을 가져옴
  - 알림을 확인하면 '알림 확인 여부'를 변경하고, 
  - 사용자가 해당 '복습'을 완료 체크 시, '복습'을 복습 큐에서 제거 
- **알림 확인 후, 복습 큐를 확인해 큐가 비어 있으면 추가 복습 여부 선택**
  - 알림에서 복습일에 따른 복습 데이터를 확인 
  - 해당 데이터가 일일 학습에 대한 마지막 복습인 경우 추가 복습 여부를 확인

### 11. 도메인 기능 분석
- **Member**
  - [x] 카카오톡을 활용한 소셜 로그인
  - [ ] 로그아웃 시 access token 블락 처리
- **Study**
  - [x] 학습 등록, 수정, 삭제, 조회
  - [ ] 학습은 강의, 도서, 코테로 분류하여 필터링 조회가 가능하도록 구현
- **DailyStudy**
  - [x] 일일 학습은 등록된 학습에 대한 일일 학습 내용을 의미
  - [x] 일일 학습 등록, 수정, 삭제, 조회
  - [x] 학습 주제, 학습 범위
- **StudyKeyword**
  - [x] 일일 학습의 핵심 키워드, 핵심 키워드 내용을 저장
  - [x] 학습 내용 등록, 수정, 삭제, 조회
- **Review**
  - [x] 복습은 DailyStudy 등록 후 조건에 따라 생성
  - [x] 복습 큐가 비워지면 사용자에게 추가 복습 여부를 확인한다.
- **ReviewReminder**
  - [x] 복습 알람은 복습 큐에 저장된 정보를 확인하여 사용자에게 알람을 전송(매일 5:00)

