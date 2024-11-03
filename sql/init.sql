-- Step 1: 이름 저장 테이블 생성
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    number_of_remaining_table INTEGER
);

CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT,
    restaurant_id BIGINT,
    reserved_at TIMESTAMP
);

-- Step 2: 예시 이름 데이터 삽입
INSERT INTO restaurants (id, name, number_of_remaining_table)
VALUES
    (7777, 'TGI Friday', 100)
