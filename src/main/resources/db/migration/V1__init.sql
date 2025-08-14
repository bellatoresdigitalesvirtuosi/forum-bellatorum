-- Migración inicial del foro Bellatores Digitales Virtuosi

-- 🔹 Tabla users
CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- 🔹 Tabla roles
CREATE TABLE roles (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- 🔹 Tabla permissions
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- 🔹 Relación role_permissions
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- 🔹 Relación user_roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- 🔹 Tabla categories
CREATE TABLE categories (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    approved BOOLEAN DEFAULT FALSE
);

-- 🔹 Relación category_moderators (opcional)
CREATE TABLE category_moderators (
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, user_id)
);

-- 🔹 Tabla topics
CREATE TABLE topics (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES users(id),
    category_id BIGINT NOT NULL REFERENCES categories(id)
);

-- 🔹 Tabla messages
CREATE TABLE messages (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES users(id),
    topic_id BIGINT NOT NULL REFERENCES topics(id)
);

-- 🔹 Datos iniciales: Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_MODERATOR'), ('ROLE_USER');

-- 🔹 Datos iniciales: Permisos
INSERT INTO permissions (name) VALUES
('CATEGORY_CREATE'),
('CATEGORY_APPROVE'),
('TOPIC_CREATE'),
('TOPIC_DELETE'),
('MESSAGE_CREATE'),
('MESSAGE_DELETE');

-- 🔹 Asociar permisos a roles
-- ADMIN tiene todos los permisos
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p WHERE r.name='ROLE_ADMIN';

-- MODERATOR puede aprobar categorías y moderar mensajes
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name='ROLE_MODERATOR' AND p.name IN ('CATEGORY_APPROVE','MESSAGE_DELETE');

-- USER puede crear categorías, tópicos y mensajes
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name='ROLE_USER' AND p.name IN ('CATEGORY_CREATE','TOPIC_CREATE','MESSAGE_CREATE');
