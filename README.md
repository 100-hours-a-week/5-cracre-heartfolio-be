# 1. 주식 포트폴리오 서비스 (2024.07 ~ 2024.10)

## 프로젝트명

- **기부로 마음을 공유할 수 있는 모의투자, HeartFolio**

## 프로젝트 소개

- 해외주식 모의투자 포트폴리오 서비스
- 모의투자를 통해 자산의 손실을 최소화하고, 주식에 대한 친밀도를 높이기 위함
- 4가지 영역 (교육 / 사회참여 / 문화 / 환경) 에 기부를 통해 캐시 구매 가능
- 경제 뉴스를 통해 최신 트렌드 확인 가능

### 본인의 역할

- 백엔드 개발
- 리더

### 프로젝트 링크 및 홍보 자료

https://xn--vr-kg0j662g.site/

https://disquiet.io/product/heartfolio-1727164046296

### 성과

- Disquiet 트렌딩 프로덕트 2위
- 실제 사용자 하루 10명

## 수행 인원

| [이름1](https://github.com/이름1) | [이름2](https://github.com/이름2) | [이름3](https://github.com/이름3) | [이름4](https://github.com/이름4) | [이름5](https://github.com/이름5) |
|:---:|:---:|:---:|:---:|:---:|
| BE | BE | FE | DevOps | 관리자 |


## 수행 기간

- 2024.07 ~ 2024.10 (4개월)

## 개발 환경

<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> , <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> 

## 사용 언어

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

## 데이터베이스

<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

https://github.com/100-hours-a-week/5-team-cracre-heartfolio-fe

https://github.com/100-hours-a-week/5-team-cracre-heartfolio-be

## 구현 내용

### 유저 관리

- 카카오 소셜 로그인을 이용한 JWT 기반 로그인 구현
- Access Token과 Refresh Token을 통한 토큰 관리
- 마이페이지 관리 및 닉네임 변경,중복검사 구현
- 페이지별 접근 권한 부여

### 거래 관리

- 매수/매도 기능 구현
- 매수/매도시 포트폴리오 업데이트 및 자산 정보 업데이트 구현
- 종목별 최신순 거래내역 제공, 전체 최신순 거래내역 제공
- 프론트엔드에서 현재가를 보내준다는 취약점으로 주식 1원 매수, 원하는 값에 매도하는 버그 발생
- Redis에 저장된 Web Socket의 실시간 값과, 프론트엔드의 요청값 비교 로직 구현을 통해 해결

### 결제 기능

- PortOne을 이용한 결제 기능 구현
- 프론트 측 요청 가격과 콜백되는 가격 일치시 기부 완료 로직 구현
- 1000원 , 2000원 옵션에 따라 다른 금액 기부 가능
- 사업자 번호가 없어, 테스트 결제인 관계로 밤 11시 환불

### 어려웠던 점 및 배운점

- 리더로서의 책임감, 일정관리의 소중함을 알게 된 계기
- API 명세서 업데이트, 소통은 개발자에게 필수적
- Spring Boot에 대해 많이 공부하며 배움
