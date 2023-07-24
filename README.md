### 실행명령
$ docker compose up -d

* pem파일이 제거되어 있기 때문에 각 서비스의 resources/jwt 경로에 공개키/개인키 추가 필요
* 'SECURED' 문자를 실제 패스워드로 변경 필요

### 네트워크 구성

       +--------+        +------------+     +-----------------+
       | Client +------->+ Krakend    +---->+ Sample Server   |
       |        |        | Gateway    |     | + POSTGRES      |
       +--------+        +-----+------+     +-----------------+
                               |
                               v
                         +-----+------+
                         | JWT Auth   |
                         | Server     |
                         | + REDIS    |
                         +-----+------+
                               |
                               v
                         +-----+------+    
                         | User Server|
                         | + POSTGRES |
                         +------------+

* Krakend Gateway: 네트워크 트래픽 분배 및 JWT 토큰 검증
* JWT Auth Server: JWT 토큰 발행 및 검증, Refresh
* User Server: 사용자 정보 관 (IDP역할)
* Sample Server: 토큰 활용한 사용자 인증 및 데이터 제공
