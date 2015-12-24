DROP TABLE BaseEntity IF EXISTS;

CREATE TABLE BaseEntity (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  updtimestamp timestamp default SYSDATE,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);
