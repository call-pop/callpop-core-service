-- Users
INSERT INTO users (login_id, password, name, created_date)
SELECT 'shsoong', '$2a$10$FXlhnnTNYIpWzaA578Ic1OFoxUw65bToFf8Io6JzRqYhZP9ogXV.C', '송승희', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE login_id = 'shsoong');

INSERT INTO users (login_id, password, name, created_date)
SELECT 'dykim', '$2a$10$ECqGe9IulopwKh7XQia6U.ayyZhuGsOoQPY3vSgcDEO5w7GeQT8RW', '김다연', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE login_id = 'dykim');

-- Chat room
INSERT INTO chat_room (name, created_by, created_date)
SELECT '테스트 방', 'system', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM chat_room WHERE name = '테스트 방');

-- Room members
INSERT INTO room_member (chat_room_id, user_id, last_read_date)
SELECT cr.id, u.id, CURRENT_TIMESTAMP
FROM chat_room cr, users u
WHERE cr.name = '테스트 방' AND u.login_id IN ('shsoong', 'dykim')
AND NOT EXISTS (
    SELECT 1 FROM room_member rm
    WHERE rm.chat_room_id = cr.id AND rm.user_id = u.id
);
