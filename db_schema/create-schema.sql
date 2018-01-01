-- CREATE DATABASE `oauth` /*!40100 DEFAULT CHARACTER SET utf8 */;


CREATE TABLE Client(
    client_id VARCHAR(32) primary key,
	grant_type VARCHAR(32),
    client_secret VARCHAR(32),
    access_token_endpoint VARCHAR(32),
	scope_list VARCHAR(32)
);

CREATE TABLE Token(
	token_type VARCHAR(32),
    expires_in bigint,
    access_token VARCHAR(32),
	primary key(token_type, expires_in, access_token)
);

CREATE TABLE Request(
    url VARCHAR(100),
    parametersmap VARCHAR(200),
    header VARCHAR(200),
    request_type CHAR(7),
    body VARCHAR(300),
	primary key(url,parametersmap,header,request_type,body)
);

CREATE TABLE ins_Client(
    client_id VARCHAR(32) primary key,
	grant_type VARCHAR(32),
    client_secret VARCHAR(32),
    access_token_endpoint VARCHAR(32),
	scope_list VARCHAR(32)
);

CREATE TABLE ins_Token(
	token_type VARCHAR(32),
    expires_in bigint,
    access_token VARCHAR(32),
	primary key(token_type, expires_in, access_token)

);

CREATE TABLE ins_Request(
    url VARCHAR(100),
    parametersmap VARCHAR(200),
    header VARCHAR(200),
    request_type CHAR(7),
    body VARCHAR(300),
	primary key(url,parametersmap,header,request_type,body)

);

CREATE TABLE del_Client(
    client_id VARCHAR(32) primary key,
	grant_type VARCHAR(32),
    client_secret VARCHAR(32),
    access_token_endpoint VARCHAR(32),
	scope_list VARCHAR(32)
);

CREATE TABLE del_Token(
	token_type VARCHAR(32),
    expires_in bigint,
    access_token VARCHAR(32),
	primary key(token_type, expires_in, access_token)
);

CREATE TABLE del_Request(
    url VARCHAR(100),
    parametersmap VARCHAR(200),
    header VARCHAR(200),
    request_type CHAR(7),
    body VARCHAR(300),
	primary key(url,parametersmap,header,request_type,body)
);