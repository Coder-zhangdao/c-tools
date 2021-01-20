CREATE TABLE user (id int primary key,  name varchar(20) NOT NULL);
CREATE TABLE test (id int(11) NOT NULL AUTO_INCREMENT COMMENT '这里是id!',
                        name varchar(100) DEFAULT NULL COMMENT '这里是名称！',
                        PRIMARY KEY (id)
) ;
