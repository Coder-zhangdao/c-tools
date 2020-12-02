create table t_main(
    id int auto_increment,
    user_name varchar(32),
    gender int,
    edu int
);

create table education(
    id int auto_increment,
    edu_name varchar(32)
);

create table gender(
    id int auto_increment,
    sex varchar(32)
);

insert into education (edu_name) values ( '小学' ),('中学'),('大学');
insert into gender(sex)values ( '男' ),('女'),('变态');

insert into t_main(user_name, gender, edu) VALUES ( '张三',1,1 ),('李四', 2,2),('王五', 3,3);
