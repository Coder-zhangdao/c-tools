grant all  on test.* to test@'%' identified by 'test123';
grant all  on test.* to test@'localhost' identified by 'test123';

flush privileges;

use test;

create table t_log (
    lid int auto_increment primary key ,
    uid int default 0,
    content varchar(1024)
);
