######## CMMS10 개발 규칙 및 가이드

### 인프라 구성

JDK 21
Spring Boot, gradle, groovy
Maria DB (현재 개발환경:10.6.21-MariaDB)
springframework.boot' version '3.4.7'
Gradle 8.14
운영환경 : Ubuntu, 개발환경 : Windows
Thymeleaf, tailwind css 적용

### 기본 원칙

* 각 코드는 시작 부분에 이름, 기능 설명, 생성자, 생성일, 수정자, 수정일
* 각 단계별 주요 기능에 대해 간단한 설명 포함, 파리미터 주석 
* 연계되는 프로그램에 대해 파라미터의 type과 name을 확인할 것
* 코딩은 최대한 간단하게 명확한 방향으로 제시할 것. 유지보수성을 고려하여 디버깅이 어렵거나 코드가 난해하지 않도록 할 것. Lombok 이노테이션의 NoArgsConstructor,AllArgsConstructor,Data를 활용하여 코드를 간소화. 그러나 autowired 대신 생성자는 직접 주입 방식 채택함함
* 동시에 진행하지 말고, 하나씩 단계적으로 진행할 것 

### 파일 Naming Rule

* Controller: 폴더명Controller.java → 예: inspectionController.java
              메소드는 form,editform,list,delete,save,detail..
              mapping은 모듈이름+기능 → 예: @Getmapping("WorkorderForm")
* Entity: 폴더명.java → 예: inspection.java
* Service: 폴더명Service.java → 예: inspectionService.java
           메소드는 최대한 자세히. 기능명+모듈명 → 예: saveWorkorder, getAllWorkorders
* Repository: 폴더명Repository.java → 예: inspectionRepository.java
              메소드는 파라미터를 알 수 있도록 → 예: findByCompanyIdAndOrderId
* DTO: 폴더명DTO.java → 예: inspectionDTO.java
* View: Form, List, Detail로 구분하고 Form은 입력화면, List는 조회, Detail은 각 항목별 출력폼
  예시: 폴더명+기능.html → 예: inspectionForm.html

## 화면 Layout 구조

* 로그인 Login.html
* Header, body(왼쪽 site menu), footer
* Layout.html 구성, 각 페이지는 Layout.html을 상속속
* 왼쪽 site menu의 toggle 구성
* 로그인 후, 화면 상단 오른쪽에 로그인 유저 정보(logout가능, 사용자이름, siteAccess 리스트 복수인 경우 변경선택택)

## 모듈별 기능 구현

* Domain : 기준 정보 CRUD 
* commonCode : 코드 값 관리 CRUD+detail(출력용)
  (참고) codeType의 설정값: JOBTP-job type, ASSET-asset type, DEPRE-deperciation type, PERMT-permitType 
* PlantMaster : 설비 기준정보 관리. Tag/PSM 등 관리대상 여부 체크. plamtMasterList에서 복수의 plantId를 선택 후 QR 코드 출력 기능 제공하여 스티커 프린터에서 출력 후 설비에 부착할 수 있도록 함(Inspection,workorder에 활용)
* InventoryMaster : 재고 기준정보 관리. plantMaster대비 이동이 더 빈번하고 출력빈도가 높을 것으로 예상되어 바코드 형태로 출력 기능 제공 
* Inspection : 예방점검. 등록 후 status 필드를 통해 상태값관리(저장-->승인). 일상점검에 해당되므로 자료를 DB화 하는데 의미가 있음. 모바일에서 QR 스캔 후 입력이 가능하도록 기능 제공 
* Workorder : 작업 지시서. 법인에서 지시서 발행 후 외주사 결과값 입력 후 승인 요청. 승인 후 확정. 모바일에서 QR 스캔 후 입력이 가능하도록 기능 제공 
* Workpermit : 작업안전 허가서. PlantMaster에서 안전관리 대상 설비(WorkpermitYN="Y")는 반드시 승인된 Workpermit 을 Workorder 생성시 입력해야 한다. 
* 추후 개발 예정: 결재 기능, 권한 기능
  (검토중) 
* AI 기능 : 매뉴얼 기반으로 FAISS 등으로 벡터 DB를 구성하고, LLM을 구축하고자 함 

## 다국어 구현

* 한국어와 영어를 기본 구현하고 resources/messages\_en.properties/messages\_ko.properties : messages.properties를 유지해야 작동함 

  ## Template 구조

  templates/cmms10/
  ├── src/
  │   ├── main/
  │   │   ├── java/           /\*\* Java 코드 */
  │   │   │   ├── com/
  │   │   │   │   └── cmms10/
  │   │   │   │       ├── config/         /*\* 설정 관련련 */
  │   │   │   │       ├── auth/           /*\* 인증 관련 */
  │   │   │   │       │   ├── controller/
  │   │   │   │       │   ├── service/
  │   │   │   │       │   └── dto/
  │   │   │   │       ├── domain/         /*\* 도메인 엔티티 */
  │   │   │   │       │   ├── company/
  │   │   │   │       │   ├── site/
  │   │   │   │       │   ├── dept/
  │   │   │   │       │   ├── user/
  │   │   │   │       │   ├── siteAccess/
  │   │   │   │       │   └── roleAuth/
  │   │   │   │       ├── plantMaster/
  │   │   │   │       ├── inventoryMaster/
  │   │   │   │       ├── inspection/
  │   │   │   │       ├── workPermit/
  │   │   │   │       ├── workorder/
  │   │   │   │       ├── memo/
  │   │   │   │       └── common/     /*\* 공통코드드 */
  │   │   │   └──   cmms4Application.java  
  │   │   │  
  │   │   └── resources/           /*\* 리소스 */
  │   │       ├── static/          /*\* 정적 리소스 */
  │   │       │   ├── css/         /*\* CSS 파일 */
  │   │       │   └── js/          /*\* JavaScript 파일 */
  │   │       ├── templates/       /*\* HTML 템플릿 */
  │   │       │   ├── layout
  |   |       |   |   └── defaultLayout.html
  │   │       │   ├── auth/
  │   │       │   │   └── Login.html
  │   │       │   ├── domain/
  │   │       │   │   ├── company/
  │   │       │   │   ├── site/
  │   │       │   │   ├── dept/
  │   │       │   │   ├── user/
  │   │       │   │   ├── siteAccess/
  │   │       │   │   └── roleAuth/
  │   │       │   ├── plantMaster/
  │   │       │   ├── inventoryMaster/
  │   │       │   ├── inspection/
  │   │       │   ├── workPermit/
  │   │       │   ├── workorder/
  │   │       │   ├── memo/
  |   |       |   └── commonCode/ /* 공통코드 관리, Group과 item으로 구성 */
  │   │       ├── messages/       /*\* 다국어 메시지 */
  │   │       │   ├── messages_ko.properties
  │   │       │   └── messages_en.properties
  │   │       └── application.yml
  │   └── test/            /*\* 테스트 코드 */
  └── build.gradle         /*\* 빌드 설정 파일 \*/
  **주의:** 각 도메인 엔티티는 데이터베이스 테이블과 1:1 매핑됩니다.

  ## ID 채번 규칙

* 수동: companyId, siteId, deptId, username, codeId
* 자동 숫자 일련번호 : scheduleId, itemId, memoId, commentId
* 자동이나 채번규칙 있음 : 
  plantId(1로 시작,10자리 숫자)  , inventoryId(2로 시작,10자리 숫자), inspectionId(3로 시작,10자리 숫자), workPermit(9로 시작,10자리),workorderId(5로 시작,10자리 숫자), fileGroupId(테이블 이름 + YYMM + 5자리번호)
  단, 자동채번은 companyId를 기준으로 Max 값을 선택한다. 즉, site 단위에서 키 값이 중복되지 않도록 한다. item 레벨에서 siteId를 PK로 관리하지 않으므로 중복될 수 있기 때문이다. 

  ## 보안 및 로깅

* CSRF, CORS, AOP 적용
* daily log: logging\_연도날짜일

## user 테이블에서 userId 대신 username 사용함

