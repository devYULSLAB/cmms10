# CMMS10 DB 테이블 정의서 (Full Version)

### 공통기준정보 관리(단수 표현),UTF8mb4_general_ci  
## company

| 필드명      | 데이터 타입   | 설명          |
|:------------|:-------------|:--------------|
| companyId   | Char(5)      | 회사 ID (PK)  |
| companyName | Varchar(100) | 회사 이름     |
| note        | Text         | 설명         |
| fileGroupId | CHAR(10)     | 첨부파일 그룹 |
| createBy    | CHAR(5)      | 생성자        |
| createDate  | DATETIME     | 생성일        |
| updateBy    | CHAR(5)      | 수정자        |
| updateDate  | DATETIME     | 수정일        |
| deleteMark  | CHAR(1)      | 삭제 마크     |

## site

| 필드명      | 데이터 타입   | 설명          |
|:------------|:-------------|:--------------|
| companyId   | Char(5)      | 회사 ID (PK)  |
| siteId      | CHAR(5)      | 사이트 ID (PK)  |
| siteName    | VARCHAR(100) | 사이트 이름   |
| fileGroupId | CHAR(10)     | 첨부파일 그룹 |
| createBy    | CHAR(5)      | 생성자        |
| createDate  | DATETIME     | 생성일        |
| updateBy    | CHAR(5)      | 수정자        |
| updateDate  | DATETIME     | 수정일        |
| deleteMark  | CHAR(1)      | 삭제 마크     |

## dept : 한 회사 내에 dept는 1개 임. 사이트별로 다를 수 없음(다수의 사이트에 같은 기능의 부수가 존재시 코드값 분리해야 함 )

| 필드명       | 데이터 타입   | 설명                                     |
|:-------------|:-------------|:-----------------------------------------|
| companyId    | Char(5)      | 회사 ID (PK)                             |
| deptId       | Char(5)      | 부서 ID (PK)                             |
| deptName     | Varchar(100) | 부서 이름                                |
| parentDeptId | CHAR(5)      | 상위 부서 ID (NULL이면 최상위)           |
| deptLevel    | INT          | 계층 단계 (예: 1=HQ, 2=사업부, 3=파트) |
| sortOrder    | INT          | 정렬 순서                                |         
| note         | Text         | 설명                                     |
| fileGroupId  | CHAR(10)     | 첨부파일 그룹                            |
| createBy     | CHAR(5)      | 생성자                                   |
| createDate   | DATETIME     | 생성일                                   |
| updateBy     | CHAR(5)      | 수정자                                   |
| updateDate   | DATETIME     | 수정일                                   |
| deleteMark   | CHAR(1)      | 삭제마크                                |

## user : Spring boot에 맞게 userId 대신 username 활용함

| 필드명        | 데이터 타입   | 설명                 |
|:--------------|:-------------|:---------------------|
| companyId     | Char(5)      | 회사 ID (PK)         |
| username      | Char(5)      | 사용자 ID (PK)       |
| userFullName  | Varchar(100) | 사용자 이름          |
| password      | Varchar(100) | 패스워드             |
| email         | Varchar(100) | 이메일               |
| phone         | Varchar(100) | 전화번호             |
| deptId        | Char(5)      | 부서 ID              |
| roleId        | Char(5)      | 권한 ID           |
| note          | Text         | 설명                 | 
| fileGroupId   | CHAR(10)     | 첨부파일 그룹        |
| createBy      | CHAR(5)      | 생성자               |
| createDate    | DATETIME     | 생성일               |
| updateBy      | CHAR(5)      | 수정자               |
| updateDate    | DATETIME     | 수정일               |
| deleteMark    | CHAR(1)      | 삭제 마크            |

### 권한 관리 : company, site, 페이지, 권한 그룹별 관리가 현재 구현하기 복잡함. company 가 다른 경우 별도의 ID로 로그인 하도록 하며, (같은 company) site가 다른 경우 권한 구분 없이 페이지별 권한만 관리하도록 함. 현재 기능 구현이 안 되어 있음 

## roleAuth : 권한 이름 관리 : Company별로 권한 구분 안 됨 
*roleId
RADMN : Role_admin : Admin user
RMAST : Role_master : Master data user 
RMGNR : Role_manager : Manager user (approval)
RUSER : Role_user : general user 
* authGranted
create, read, update, delete, approve, master


| 필드명       | 타입         | PK   | FK          | 설명                   |
|:------------|:------------|:-----|:------------|:-----------------------|
| roleId      | char(5)     | PK   |             | 권한 ID                 |
| roleName    | varchar(50) |      |             | 권한 Name               |
| authGranted | varchar(50) | PK   |             | 권한 ID2                |

### 마스터 정보관리 : 기준정보는 삭제하지 않고 deleteMark 마크처리 (삭제된 데이터는 복구할 수 있도록. 기본 null, 삭제 "Y")
## plantMaster

| 필드명               | 데이터 타입   | 설명                                              |
|:---------------------|:-------------|:--------------------------------------------------|
| companyId            | CHAR(5)      | 회사 ID (PK)                                      |
| siteId               | CHAR(5)      | 사이트 ID (PK)                                    |
| plantId              | CHAR(10)     | 설비 ID (PK)                                      |
| plantName            | VARCHAR(100) | 설비명                                           |
| plantLoc             | VARCHAR(100) | 설비위치                                          |
| funcId               | CHAR(4)      | 기능 위치 코드(설비에 대한 기능분류)               |
| respDept             | CHAR(5)      | 관리 부서 ID                                      |
| installDate          | Date         | 설치일                                            |
| assetType            | Char(5)      | 자산 타입                                         |
| depreMethod          | Char(5)      | 상각방법                                          |
| acquitionValue       | Decimal(15,2)| 취득가                                            |
| residualValue        | Decimal(15,2)| 잔존가                                            |
| manufacturer         | Varchar(100) | 제조사                                            |
| manufacturerModel    | Varchar(100) | 모델넘버                                          |
| manufacturerSN       | Varchar(100) | 시리얼번호                                        |
| manufacturerSpec     | Varchar(100) | 규격                                              |
| inspectionYN         | Char(1)      | 계획정비대상                                      |
| plannedMaintenanceYN | Char(1)      | 예방점검대상                                      |
| psmYN                | Char(1)      | PSM대상                                           |
| workPermitYN         | Char(1)      | 작업허가대상 (신설)                                 |
| tagYN                | Char(1)      | TAG대상                                           |
| note                 | text         | 비고(notes 아닌 note. Naming rule에 복수형 안 씀) |
| fileGroupId          | CHAR(10)     | 첨부파일 그룹                                     |
| createBy             | CHAR(5)      | 생성자                                            |
| createDate           | DATETIME     | 생성일                                            |
| updateBy             | CHAR(5)      | 수정자                                            |
| updateDate           | DATETIME     | 수정일                                            |
| deleteMark           | CHAR(1)      | 삭제 마크                                         |

## funcMaster - PlantMaster의 funcId 관리 

| 필드명      | 데이터 타입   | 설명                                            |
|:------------|:-------------|:----------------------------------------------|
| companyId   | Char(5)      | 회사 ID (PK)                                   |
| siteId      | CHAR(5)      | 사이트 ID (PK)                                 |
| funcId      | Char(5)      | 코드 ID (PK)                                   |
| funcType    | Char(5)      |                                               |
| funcName    | Varchar(100) | 코드 이름                                      |

## inventoryMaster

| 필드명            | 데이터 타입   | 설명                                              |
|:------------------|:-------------|:--------------------------------------------------|
| companyId         | CHAR(5)      | 회사 ID (PK)                                      |
| inventoryId       | CHAR(10)     | 재고 ID (PK)                                      |
| inventoryName     | VARCHAR(100) | 재고명                                            |
| respDept          | CHAR(5)      | 관리 부서 ID                                      |
| assetType         | Char(5)      | 자산 타입                                         |
| manufacturer      | Varchar(100) | 제조사                                            |
| manufacturerModel | Varchar(100) | 모델넘버                                          |
| manufacturerSN    | Varchar(100) | 시리얼번호                                        |
| manufacturerSpec  | Varchar(100) | 규격                                             |
| note              | text         | 비고(notes 아닌 note. Naming rule에 복수형 안 씀)  |
| fileGroupId       | CHAR(10)     | 첨부파일 그룹                                     |
| createBy          | CHAR(5)      | 생성자                                            |
| createDate        | DATETIME     | 생성일                                            |
| updateBy          | CHAR(5)      | 수정자                                            |
| updateDate        | DATETIME     | 수정일                                            |
| deleteMark        | CHAR(1)      | 삭제 마크                                         |

## inventoryStock

| 필드명             | 데이터 타입   | 설명                                              |
|:------------------|:-------------|:-------------------------------------------------|
| companyId         | CHAR(5)      | 회사 ID (PK)                                      |
| siteId            | CHAR(5)      | 사이트 ID (PK)                                    |
| locId             | CHAR(5)      | 창고 ID (PK)                                      |
| inventoryId       | CHAR(10)     | 재고 ID (PK)                                      |
| qty               | Decimal(15,2)| 수량                                              |
| unitPrice         | Decimal(15,2)| 재고 ID                                      |
| totalValue        | Decimal(15,2)| 재고 ID                                       |

## inventoryHistory

| 필드명          | 데이터 타입         | 설명                        |
|:---------------|:------------------|:---------------------------|
| companyId      | CHAR(5)           | 회사 ID (PK)                |
| historyId      | CHAR(10)          | 입출고 이력 ID (PK)          |
| siteId         | CHAR(5)           | 사이트 ID                   |
| locId          | CHAR(5)           | 창고 ID                     |
| inventoryId    | CHAR(10)          | 재고 ID                     |
| ioType         | CHAR(1)           | 입출고 구분 ('I': 입고, 'O': 출고) |
| ioDate         | DATETIME          | 입출고 일시                       |
| qty            | Decimal(15,2)     | 입출고 수량 (출고는 음수 가능)     |
| unitPrice      | Decimal(15,2)     | 단가 (평균단가 기준)             |
| totalValue     | Decimal(15,2)     | 총 금액 (수량 × 단가)           |
| note           | TEXT              | 비고                          |
| createBy       | CHAR(5)           | 생성자                         |
| createDate     | DATETIME          | 생성일                         |

## storMaster - InventoryMaster의 locId 관리 

| 필드명      | 데이터 타입   | 설명                                           |
|:------------|:-------------|:-----------------------------------------------|
| companyId   | Char(5)      | 회사 ID (PK)                                   |
| siteId      | Char(5)      | 사이트  ID (PK)                                |
| locId       | Char(5)      | 창고 ID (PK)                                   |
| parentLocId | Char(5)      | 계층형구조 지원용 ID                             |
| locType     | Char(5)      | 'WAREHOUSE','ZONE','SHELF'                    |
| locName     | Varchar(100) | 창고 이름                                      |

### 점검 관리 : 향후 status 필드 추가하여 결재 등 상태관리 추가
## inspection

| 필드명         | 데이터 타입   | 설명             |
|:---------------|:-------------|:-----------------|
| companyId      | CHAR(5)      | 회사 ID (PK)     |
| siteId         | CHAR(5)      | 사이트 ID (PK)      |
| inspectionId   | CHAR(10)     | 점검계획 ID (PK) |
| inspectionName | Varchar(100) | 예방점검 이름    |
| plantId        | CHAR(10)      | 설비 ID          |
| JOBTP          | CHAR(5)      | 작업 유형        |
| performDept    | CHAR(5)      | 수행부서         |
| note           | text         | 비고             |
| fileGroupId    | CHAR(10)     | 첨부파일 그룹    |
| createBy       | CHAR(5)      | 생성자           |
| createDate     | DATETIME     | 생성일           |
| updateBy       | CHAR(5)      | 수정자           |
| updateDate     | DATETIME     | 수정일           |

## inspectionSchedule ==> 삭제 

| 필드명       | 데이터 타입   | 설명                     |
|:-------------|:-------------|:-------------------------|
| companyId    | CHAR(5)      | 회사 ID (PK)             |
| inspectionId | CHAR(10)     | 점검계획 ID (PK)         |
| scheduleId   | CHAR(2)      | 예방점검 일정번호 (PK)   |
| frequency    | Char(5)      | 점검주기(default " day") |
| scheduleDate | Date         | 예방점검일               |
| executeDate  | Date         | 실제점검일               |

## inspectionItem

| 필드명       | 데이터 타입   | 설명                   |
|:-------------|:-------------|:-----------------------|
| companyId    | CHAR(5)      | 회사 ID (PK)           |
| inspectionId | CHAR(10)     | 점검계획 ID (PK)       |
| itemId       | CHAR(2)      | 예방점검항목번호 (PK)  |
| itemName     | Varchar(100) | 예방점검항목이름       |
| itemMethod   | Varchar(100) | 예방점검항목방법       |
| itemUnit     | Char(10)     | 단위                   |
| itemLower    | Decimal(15,2)| 하한값                 |
| itemUpper    | Decimal(15,2)| 상한값                 |
| itemStandard | Decimal(15,2)| 표준값                 |
| itemResult   | Decimal(15,2)| 결과값                 |
| note         | Text         | 비고                   |

### 작업허가 관리 : plantMaster에서 workpermitYN="Y"인 것은 permit 번호를 workorder에 추가 
## workPermit

| 필드명      | 데이터 타입   | 설명                                                         |
|:-----------|:-------------|:-------------------------------------------------------------|
| companyId  | CHAR(5)      | 회사 ID (PK)                                                 |
| siteId     | CHAR(5)      | 사이트 ID (PK)                                               |
| permitId   | CHAR(10)     | 작업허가서 ID (PK, 6으로 시작하는 10자리 채번)                |
| permitName    | Varchar(100) | 작업허가서서 이름        |
| plantId    | CHAR(10)     | 대상 설비                                                    |
| deptId     | CHAR(5)      | 부서 코드                                                 |
| permitType | CHAR(5)      | 작업 유형 코드 (예: HOT, ELEC 등, 공통코드)                  |
| status     | CHAR(1)      | 상태: D(초안), R(요청), A(승인), C(종료), R(반려)            |
| startDate  | DATE         | 작업 시작 일시                                                |
| endDate    | DATE         | 작업 종료 일시                                                |
| requestBy  | CHAR(5)      | 작성자 (username)                                            |
| approveBy  | CHAR(5)      | 승인자 (username)                                            |
| performBy  | CHAR(5)      | 실제 작업자 (username)                                       |
| hazardNote | TEXT         | 위험 요소                                                    |
| safetyMeasure | TEXT      | 안전조치                                                     |
| workDesc   | TEXT         | 작업 요약                                                    |
| fileGroupId| CHAR(10)     | 첨부파일 그룹                                                |
| createBy   | CHAR(5)      | 생성자                                                       |
| createDate | DATETIME     | 생성일                                                       |
| updateBy   | CHAR(5)      | 수정자                                                       |
| updateDate | DATETIME     | 수정일                                                       |

## workPermitItem

| 필드명      | 데이터 타입   | 설명                                         |
|:-----------|:-------------|:---------------------------------------------|
| companyId  | CHAR(5)      | 회사 ID (PK)                                 |
| permitId   | CHAR(10)     | 작업허가서 ID (PK)                           |
| itemId     | CHAR(2)      | 항목 번호 (PK, 자동 증가)                    |
| signerName | VARCHAR(100) | 서명자 이름 (직접 입력 또는 사용자 선택)     |
| signature  | TEXT         | 서명 데이터 (이미지 또는 base64)             |
| signedAt   | DATETIME     | 서명 시간                                    |
| role       | CHAR(5)      | 역할 (WRITER, APPROVER, WORKER 등)           |
| note       | TEXT         | 비고 (선택)                                  |

### 작업오더 관리 : 향후 status 필드 추가하여 결재 등 상태관리 추가
## workorder

| 필드명       | 데이터 타입   | 설명                 |
|:-------------|:-------------|:---------------------|
| companyId    | Char(5)      | 회사코드 (PK)        |
| siteId       | CHAR(5)      | 사이트 ID (PK)         |
| orderId      | CHAR(10)     | 작업오더 ID (PK)     |
| orderName    | Varchar(100) | 작업오더 이름        |
| plantId      | CHAR(10)     | 설비 마스터 ID       |
| memoId       | CHAR(10)     | 메모 ID 레퍼런스             |
| JOBTPpe      | Char(5)      | 작업유형             |
| dept         | Char(5)      | 수행부서             |
| permitId     | Char(10)     | 작업허가가             |
| scheduleDate | Date         | 계획일               |
| scheduleMM   | Decimal(15,2)| 예상 공수(Man Month) |
| scheduleCost | Decimal(15,2)| 예상 비용            |
| scheduleHSE  | Varchar(100) | 안전환경계획         |
| executeDate  | Date         | 실적일               |
| executeMM    | Decimal(15,2)| 실적 공수(Man Month) |
| executeCost  | Decimal(15,2)| 실적 비용            |
| executeHSE   | Varchar(100) | 안전환경실적         |
| note         | Text         | 비고                 |
| fileGroupId  | CHAR(10)     | 첨부파일 그룹        |
| createBy     | CHAR(5)      | 생성자               |
| createDate   | DATETIME     | 생성일               |
| updateBy     | CHAR(5)      | 수정자               |
| updateDate   | DATETIME     | 수정일               |

## workorderItem

| 필드명     | 데이터 타입   | 설명             |
|:-----------|:-------------|:-----------------|
| companyId  | Char(5)      | 회사코드 (PK)    |
| orderId    | CHAR(10)     | 작업오더 ID (PK) |
| itemId     | CHAR(2)      | 아이템 ID (PK)   |
| itemName   | Varchar(100) | 이름             |
| itemMethod | Varchar(100) | 방법             |
| itemResult | Varchar(100) | 결과값           |
| note       | Text         | 비고             |

### 메모 관리 : 작업 현황 메모 리스트에 등록하여 공유 목적 
## memo : siteId를 key도 두지 않음 

| 필드명      | 데이터 타입   | 설명                     |
|:------------|:-------------|:-------------------------|
| companyId   | CHAR(5)      | 회사 ID (PK)             |
| memoId      | CHAR(10)     | 메모 ID (PK)             |
| memoName    | VARCHAR(100) | 제목                     |
| note        | text         | 내용                     |
| isPinned    | CHAR(1)      | 상단 고정 여부 ('Y'/'N') |
| viewCount   | INT          | 조회수                   |
| fileGroupId | CHAR(10)     | 첨부파일 그룹            |
| createBy    | CHAR(5)      | 생성자                   |
| createDate  | DATETIME     | 생성일                   |
| updateBy    | CHAR(5)      | 수정자                   |
| updateDate  | DATETIME     | 수정일                   |

## memoComment

| 필드명    | 데이터 타입   | 설명                                     |
|:----------|:-------------|:-----------------------------------------|
| companyId | CHAR(5)      | 회사 ID (PK)                             |
| memoId    | CHAR(10)     | 메모 ID (PK)                             |
| commentId | CHAR(2)      | 댓글 ID (PK)                             |
| note      | text         | 댓글 내용                                |
| parentId  | INT          | FK. 부모 댓글 ID (null이면 최상위 댓글)  |
| depth     | INT          | 계층 수준 (0: 루트, 1: 대댓글 등)        |
| sortOrder | INT          | 정렬 순서 (UI에서 표시 순서를 위한 필드) |


### 공통코드 관리 : AssetType(플랜트,재고마스터터), JOBTPpe(작업유형)
## commonCode

| 필드명      | 데이터 타입   | 설명                                           |
|:------------|:-------------|:-----------------------------------------------|
| companyId   | Char(5)      | 회사 ID (PK)                                   |
| codeId      | Char(5)      | 코드 ID (PK)                                   |
| codeType    | Char(5)      | 코드 유형 / 자산유형, 작업유형, 분류유형(자산)       |
| codeName    | Varchar(100) | 코드 이름                                      |



