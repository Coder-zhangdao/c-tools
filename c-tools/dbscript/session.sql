drop table t_session cascade constraints;
CREATE TABLE t_session ( s_id varchar2(64) NOT
	  NULL, user_id number(10) NOT NULL, s_start date default sysdate NOT NULL ,
	  s_expire number(11) NOT NULL , s_fp varchar2(64) NOT NULL, PRIMARY KEY
	  (s_id), unique (user_id) ) ;