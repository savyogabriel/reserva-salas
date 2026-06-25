-- ============================================================
--  Sistema de Reserva de Salas de Estudo
--  Script de criação do banco de dados PostgreSQL
--  Disciplina: Engenharia de Software II — IFPI
-- ============================================================

-- 1. Criar o banco de dados (execute separadamente se necessário)
-- CREATE DATABASE reserva_salas_db;

-- 2. Conecte ao banco criado antes de executar o restante:
-- \c reserva_salas_db

-- ── Tabela: usuarios ────────────────────────────────────────
-- Usuários padrão são criados pelo DataInitializer.java ao iniciar o sistema.
-- As senhas são codificadas com BCrypt; não insira senhas em texto puro aqui.
CREATE TABLE IF NOT EXISTS usuarios (
    id       BIGSERIAL    PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome     VARCHAR(100) NOT NULL,
    email    VARCHAR(150),
    role     VARCHAR(20)  NOT NULL DEFAULT 'USER',
    ativo    BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ── Tabela: salas ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS salas (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL UNIQUE,
    capacidade INTEGER      NOT NULL CHECK (capacidade >= 1),
    ativa      BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ── Tabela: reservas ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS reservas (
    id                 BIGSERIAL    PRIMARY KEY,
    sala_id            BIGINT       NOT NULL REFERENCES salas(id),
    nome_usuario       VARCHAR(100) NOT NULL,
    data               DATE         NOT NULL,
    hora_inicio        TIME         NOT NULL,
    hora_fim           TIME         NOT NULL,
    status             VARCHAR(20)  NOT NULL DEFAULT 'PENDENTE'
                                    CHECK (status IN ('PENDENTE','APROVADA','REJEITADA','CANCELADA')),
    data_criacao       TIMESTAMP    NOT NULL,
    data_cancelamento  TIMESTAMP
);

-- ── Índices para performance ─────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_reservas_sala_id ON reservas(sala_id);
CREATE INDEX IF NOT EXISTS idx_reservas_status  ON reservas(status);
CREATE INDEX IF NOT EXISTS idx_reservas_data    ON reservas(data);

-- ── Dados de exemplo ─────────────────────────────────────────
INSERT INTO salas (nome, capacidade, ativa) VALUES
    ('Sala 101 — Estudo Individual', 10, TRUE),
    ('Sala 102 — Laboratório de Informática', 25, TRUE),
    ('Sala 103 — Sala de Reuniões', 15, TRUE),
    ('Sala 104 — Auditório', 80, TRUE),
    ('Sala 105 — Sala Inativa', 5, FALSE)
ON CONFLICT (nome) DO NOTHING;

-- Reservas de exemplo
INSERT INTO reservas (sala_id, nome_usuario, data, hora_inicio, hora_fim, status, data_criacao)
VALUES
    (1, 'joao',  CURRENT_DATE + INTERVAL '1 day', '08:00', '10:00', 'PENDENTE',  NOW()),
    (2, 'maria', CURRENT_DATE + INTERVAL '1 day', '14:00', '16:00', 'APROVADA',  NOW()),
    (3, 'pedro', CURRENT_DATE + INTERVAL '2 days','09:00', '11:00', 'PENDENTE',  NOW()),
    (1, 'joao',  CURRENT_DATE - INTERVAL '1 day', '10:00', '12:00', 'CANCELADA', NOW() - INTERVAL '2 days')
ON CONFLICT DO NOTHING;
