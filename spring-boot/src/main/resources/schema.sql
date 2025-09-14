DROP TABLE IF EXISTS CHECKOUT_RECORD;
DROP TABLE IF EXISTS BOOK;

CREATE TABLE BOOK
(
    isbn                  VARCHAR(50) PRIMARY KEY,
    checkout_time_in_days INT NOT NULL
);

CREATE TABLE CHECKOUT_RECORD
(
    id            uuid PRIMARY KEY,
    checkout_time TIMESTAMP WITH TIME ZONE NOT NULL,
    book_isbn     VARCHAR(50)              NOT NULL,
    user_id       VARCHAR(50)              NOT NULL,
    FOREIGN KEY (book_isbn) REFERENCES BOOK (isbn)
);
