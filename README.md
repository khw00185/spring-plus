# SPRING PLUS
12. AWS 활용
12-1. EC2

- EC2 인스턴스에서 어플리케이션을 실행하세요.
- <img width="969" alt="스크린샷 2025-03-21 오전 10 21 16" src="https://github.com/user-attachments/assets/d5c42172-25ff-45c3-ad8b-6d9f35f19a3e" />

- 탄력적 IP를 설정해서 외부에서도 접속할 수 있도록 해주세요.
- <img width="321" alt="스크린샷 2025-03-21 오전 10 25 24" src="https://github.com/user-attachments/assets/84c6dde1-a648-457a-ac87-7a53671fa84d" />

- 서버 접속 및 Live 상태를 확인할 수 있는 health check API만들기
 
  http://43.200.186.128:8080/health
  
  <img width="449" alt="image" src="https://github.com/user-attachments/assets/6ffaa310-a30c-44e8-988e-274a2f50cbec" />


12-2. RDS

- RDS에 데이터베이스를 구축하고, EC2에서 실행되는 어플리케이션에 연결하세요.
 <img width="851" alt="image" src="https://github.com/user-attachments/assets/2400a4f3-1e15-4ae9-afd4-b0300b497f76" />

<img width="838" alt="image" src="https://github.com/user-attachments/assets/13d33f82-b40f-40e6-907b-6ee67266843a" />

12-3. S3

- S3 버킷을 생성하여 유저의 프로필 이미지 업로드 및 관리 API를 구현하세요.
  <img width="838" alt="image" src="https://github.com/user-attachments/assets/094bea17-7917-4ff8-9cbb-fe40be79a91d" />

  <img width="1014" alt="image" src="https://github.com/user-attachments/assets/64988942-a667-4d83-b544-9aabba7e27a3" />

13. 대용량 데이터 처리

- 대용량 데이터 처리 실습을 위해, *테스트 코드*로 유저 데이터를 100만 건 생성해주세요.
    - 데이터 생성 시 닉네임은 랜덤으로 지정해주세요.
    - 가급적 동일한 닉네임이 들어가지 않도록 방법을 생각해보세요.
      <img width="635" alt="image" src="https://github.com/user-attachments/assets/08d2da7b-b3e9-4016-b820-4d40f4ddaf51" />
      
- 닉네임을 조건으로 유저 목록을 검색하는 API를 만들어주세요.
    - 닉네임은 정확히 일치해야 검색이 가능해요.
- 여러가지 아이디어로 유저 검색 속도를 줄여주세요.
    - 조회 속도를 개선할 수 있는 여러 방법을 고민하고, 각각의 방법들을 실행해보세요.
    - `README.md` 에 각 방법별 실행 결과를 비교할 수 있도록 최초 조회 속도와 개선 과정 별 조회 속도를 확인할 수 있는 표 혹은 이미지를 첨부해주세요.
개선 전
![image](https://github.com/user-attachments/assets/718ac954-e6a7-4d29-b87f-2c92211c2a81)

개선 후 (Covering Index + Dto + index 크기 경량화)
![image](https://github.com/user-attachments/assets/e065f851-0c4b-40a2-9849-442394710145)


https://ksng0185.tistory.com/18

 
