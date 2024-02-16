-- Category 더미 데이터
INSERT INTO category (id, category_name, created_at, updated_at)
VALUES (1, 'Java', NOW(), NOW());
INSERT INTO category (id, category_name, created_at, updated_at)
VALUES (2, 'Spring', NOW(), NOW());
INSERT INTO category (id, category_name, created_at, updated_at)
VALUES (3, 'Backend', NOW(), NOW());

-- Question 더미 데이터
INSERT INTO question (id, content, hearts_count, created_at, updated_at)
VALUES (1, 'JVM의 역할에 대해 설명해주세요.', 1, NOW(), NOW());
INSERT INTO question (id, content, hearts_count, created_at, updated_at)
VALUES (2, '의존성 주입에 대해 설명해주세요.', 2, NOW(), NOW());
INSERT INTO question (id, content, hearts_count, created_at, updated_at)
VALUES (3, '테스트 코드에 대해서 어떻게 생각하고, 작성하나요?', 0, NOW(), NOW());

-- QuestionCategory 더미 데이터
INSERT INTO question_category (id, question_id, category_id)
VALUES (1, 1, 1);
INSERT INTO question_category (id, question_id, category_id)
VALUES (2, 1, 3);
INSERT INTO question_category (id, question_id, category_id)
VALUES (3, 2, 2);
INSERT INTO question_category (id, question_id, category_id)
VALUES (4, 2, 3);
INSERT INTO question_category (id, question_id, category_id)
VALUES (5, 3, 3);

-- QuestionHeart 더미 데이터
INSERT INTO question_heart (question_id, member_id)
VALUES (1, 1);
INSERT INTO question_heart (question_id, member_id)
VALUES (2, 1);
INSERT INTO question_heart (question_id, member_id)
VALUES (2, 2);

-- Member 더미 데이터
INSERT INTO member (email, nickname, provider, role)
VALUES('test@gmail.com', 'test', 'KAKAO', 'ROLE_USER');
