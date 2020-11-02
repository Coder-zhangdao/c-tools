DROP TABLE IF EXISTS t_sequence; 

CREATE TABLE
  t_sequence( KEYNAME varchar(128) NOT NULL ,
  --COMMENT 'Sequence名称',
   KEYVALUE
  numeric(20) DEFAULT 10000,
  -- COMMENT 'Sequence最大值',
   PRIMARY KEY (KEYNAME) ) ;