-- Company 테이블 데이터 추가
INSERT INTO company (companyId, companyname, createby, createdate) 
VALUES ('C0001', 'comp1', 'ADMIN', NOW());

-- Site 테이블 데이터 추가
INSERT INTO site (companyid, siteid, sitename, createby, createdate)
VALUES 
('C0001', 'S0001', 'site1', 'ADMIN', NOW()),
('C0001', 'S0002', 'site2', 'ADMIN', NOW())
;

-- Department 테이블 데이터 추가
INSERT INTO dept (companyid, deptid, deptname, parentdeptId, deptlevel, createby, createdate)
VALUES 
('C0001','D0001', 'dept1','',1,'ADMIN', NOW()),
('C0001','DD001', 'dep11','D0001',2, 'ADMIN', NOW()),
('C0001','DDD01', 'de111','DD01',3, 'ADMIN', NOW())
;

-- ADMIN 사용자 입력
INSERT INTO `user` (
    companyId, 
    username, 
    userFullName, 
    password, 
    email, 
    phone, 
    deptId, 
    roleId, 
    note,
    fileGroupId, 
    createBy, 
    createDate, 
    updateBy, 
    updateDate, 
    deleteMark
) VALUES (
    'C0001', 
    'ADMIN', 
    'ADMINFULLNAME', 
    '1234', 
    'admin@example.com', 
    '010-1234-5678', 
    'D0001', 
    'RADMN', 
    '', 
    'F0001', 
    'ADMIN', 
    NOW(), 
    'ADMIN', 
    NOW(), 
    NULL
);

-- USER1 사용자 입력
INSERT INTO `user` (
    companyId, 
    username, 
    userFullName, 
    password, 
    email, 
    phone, 
    deptId, 
    roleId, 
    note, 
    fileGroupId, 
    createBy, 
    createDate, 
    updateBy, 
    updateDate, 
    deleteMark
) VALUES (
    'C0001', 
    'USER1', 
    'USERFULLNAME1', 
    '1234', 
    'user1@example.com', 
    '010-8765-4321', 
    'D0002', 
    'RUSER', 
    '', 
    'F0002', 
    'ADMIN', 
    NOW(), 
    'ADMIN', 
    NOW(), 
    NULL
);

-- Role 입력
INSERT INTO `roleAuth` (
	roleId,
	roleName,
	authGranted
) VALUES (
	'RADMN',
	'role_admin',
	'save,read,update,delete,approve,master'
);
INSERT INTO `roleAuth` (
	roleId,
	roleName,
	authGranted
) VALUES (
	'RMAST',
	'role_master',
	'master'
);
INSERT INTO `roleAuth` (
	roleId,
	roleName,
	authGranted
) VALUES (
	'RMGNR',
	'role_manager',
	'save,read,update,delete,approve'
);
INSERT INTO `roleAuth` (
	roleId,
	roleName,
	authGranted
) VALUES (
	'RUSER',
	'role_user',
	'save,read,update,delete'
);

-- commonCode 입력
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'JB01',
	'JOBTP',
	'Job type1'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'JB02',
	'JOBTP',
	'Job type2'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'AS01',
	'ASSET',
	'Asset type1'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'AS02',
	'ASSET',
	'Asset type2'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'DP01',
	'DEPRE',
	'depreciation type1'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'PM01',
	'PERMT',
	'PERMIT type1'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'PM02',
	'PERMT',
	'PERMIT type2'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'FC01',
	'FUNTP',
	'발전기'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'FC02',
	'FUNTP',
	'BOP'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'LC01',
	'LOCTP',
	'재고 창고'
);
INSERT INTO `commonCode` (
	companyId,
	codeId,
	codeType,
	codeName
) values (
	'C0001',
	'LC02',
	'LOCTP',
	'소모품 창고'
);





	