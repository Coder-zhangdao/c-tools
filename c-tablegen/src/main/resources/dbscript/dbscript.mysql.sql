create table t_metatable
(
    tid numeric(10) primary key,
    tname varchar(64) unique,
    isnode boolean default false,
    isstate boolean default false,
    isversion boolean default false,
    isuuid boolean default false,
    ismodifydate boolean default false,
    extrainterfaces varchar(1000),
    extrasuperclasses varchar(1000),
    description varchar(1000)
);


create table t_metacolumn
(
    cid numeric(10) primary key,
    tid int,
    cname varchar(64) not null,
    type int,
    columns int,
    decimalDigits int default 0,
    isNullable boolean default false,
    isAuto_increment boolean default false,
    description varchar(1000),
    unique (tid, cname)
);
