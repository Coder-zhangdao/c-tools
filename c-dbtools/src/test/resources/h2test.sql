CREATE TABLE user1 (id int primary key,  name varchar(20) NOT NULL);
CREATE TABLE test (id int(11) NOT NULL AUTO_INCREMENT COMMENT '这里是id!',
                        name varchar(100) DEFAULT NULL COMMENT '这里是名称！',
                        PRIMARY KEY (id)
) ;
CREATE TABLE t_log (
                         lid int(11) NOT NULL DEFAULT 0,
                         uid int(11) DEFAULT NULL,
                         uname varchar(32) DEFAULT NULL,
                         content text DEFAULT NULL,
                         ipaddress varchar(32) DEFAULT NULL,
                         url varchar(256) DEFAULT NULL,
                         op_time timestamp NOT NULL DEFAULT current_timestamp(),
                         PRIMARY KEY (lid)
);
insert into t_log (lid, uid, content) values ( 1,1, 'OK');

CREATE TABLE t_sequence (
                              KEYNAME varchar(64) NOT NULL,
                              KEYVALUE bigint(18) DEFAULT 100,
                              PRIMARY KEY (KEYNAME)
);

CREATE TABLE t_config (
                            c_key varchar(64) NOT NULL,
                            c_name varchar(64) NOT NULL DEFAULT '',
                            c_value varchar(1024) DEFAULT NULL,
                            PRIMARY KEY (c_key,c_name)
);
