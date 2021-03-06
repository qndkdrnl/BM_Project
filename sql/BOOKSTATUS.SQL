CREATE TABLE BOOKSTATUS(
BOOK_ID VARCHAR2(30) NOT NULL,
BOOK_STATUS NUMBER(2) NOT NULL,
INSERTED_DATE DATE NOT NULL,
MODIFIED_DATE DATE NULL,
CONSTRAINT PK_BOOKSTATUS PRIMARY KEY(BOOK_ID),
CONSTRAINT FK_BOOKSTATUS FOREIGN KEY(BOOK_ID)
REFERENCES BOOKINFO(BOOK_ID)
);
