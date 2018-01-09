-- CREATE DATABASE `oauth` /*!40100 DEFAULT CHARACTER SET utf8 */;


CREATE TABLE Client(
    client_id VARCHAR(255) primary key,
	  grant_type VARCHAR(32),
    client_secret VARCHAR(255),
    access_token_endpoint TEXT,
	  scope_list TEXT
);

CREATE TABLE Token(
	  token_type VARCHAR(32),
    expires_in bigint,
    access_token TEXT
);

CREATE TABLE Request(
    url TEXT,
    parametersmap TEXT,
    header TEXT,
    request_type CHAR(7),
    body TEXT
);

CREATE TABLE ins_Client(
    client_id VARCHAR(255) primary key,
	  grant_type VARCHAR(32),
    client_secret VARCHAR(255),
    access_token_endpoint TEXT,
	  scope_list TEXT
);

CREATE TABLE ins_Token(
	  token_type VARCHAR(32),
    expires_in bigint,
    access_token TEXT
);

CREATE TABLE ins_Request(
    url TEXT,
    parametersmap TEXT,
    header TEXT,
    request_type CHAR(7),
    body TEXT
);

CREATE TABLE del_Client(
    client_id VARCHAR(255) primary key,
	  grant_type VARCHAR(32),
    client_secret VARCHAR(255),
    access_token_endpoint TEXT,
	  scope_list TEXT
);

CREATE TABLE del_Token(
	  token_type VARCHAR(32),
    expires_in bigint,
    access_token TEXT
);

CREATE TABLE del_Request(
    url TEXT,
    parametersmap TEXT,
    header TEXT,
    request_type CHAR(7),
    body TEXT
);