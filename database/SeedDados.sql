/*
 * =======================================================================================
 * SEED COMPLEMENTAR - DADOS DE DEMONSTRAÇÃO
 * Sistema: Dell'Asse — Gestão de Festas
 * Banco: PostgreSQL
 *
 * Uso: execute APÓS o CriacaoDB.sql e com o back-end já rodado ao menos uma vez
 *      (para criar roles via DataInitializer), OU ajuste os role_id conforme seu banco.
 *
 * Credenciais de demonstração:
 *   Admin  → username: admin   | senha: 123456
 *   Cliente → username: teste  | senha: 123456  (criado pelo DataInitializer)
 * =======================================================================================
 */

-- ---------------------------------------------------------------------------
-- 1. Empresa de demonstração (id fixo para referência nos inserts)
-- ---------------------------------------------------------------------------
INSERT INTO enterprise (id, name, document, address, phone_number, email, url_image, date_expiration)
VALUES (
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    'Dell''Asse Eventos',
    '12.345.678/0001-90',
    'Av. das Festas, 100 - Centro',
    '(49) 99999-0000',
    'contato@dellasse.com.br',
    'https://placehold.co/200x200?text=DellAsse',
    NOW() + INTERVAL '365 days'
)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------------------------
-- 2. Usuário administrador
-- Senha: 123456 (hash BCrypt)
-- ---------------------------------------------------------------------------
INSERT INTO users (uuid, active, birthday, email, name, password, phone, username, enterprise_id)
VALUES (
    'b2c3d4e5-f6a7-8901-bcde-f12345678901',
    TRUE,
    '1990-05-15',
    'admin@dellasse.com.br',
    'Administrador Dell''Asse',
    '$2y$12$/zUqWwdkk25NOu1vsBNiCedtuRVetS/JwatQYd9QM1U1QXYesYIGG',
    '49999000001',
    'admin',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
)
ON CONFLICT (uuid) DO NOTHING;

-- Vincula role ADMIN (conforme nome criado pelo DataInitializer)
INSERT INTO user_roles (user_id, role_id)
SELECT 'b2c3d4e5-f6a7-8901-bcde-f12345678901', r.role_id
FROM "role" r
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = 'b2c3d4e5-f6a7-8901-bcde-f12345678901'
      AND ur.role_id = r.role_id
  );

-- ---------------------------------------------------------------------------
-- 3. Produtos de demonstração (3 itens)
-- ---------------------------------------------------------------------------
INSERT INTO product (id, name, description, price, stock_quantity, category, image_url, date_create, date_update, enterprise_id, user_id)
VALUES
    (1, 'Balões Personalizados', 'Kit com 50 balões nas cores do tema', 89.90, 200, 'decoracao',
     'https://placehold.co/300x200?text=Baloes', NOW(), NOW(),
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'b2c3d4e5-f6a7-8901-bcde-f12345678901'),
    (2, 'Mesa Temática Premium', 'Mesa decorada com flores e velas', 450.00, 15, 'decoracao',
     'https://placehold.co/300x200?text=Mesa', NOW(), NOW(),
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'b2c3d4e5-f6a7-8901-bcde-f12345678901'),
    (3, 'Bolo Artístico 3 Andares', 'Bolo personalizado para até 80 convidados', 680.00, 8, 'alimentacao',
     'https://placehold.co/300x200?text=Bolo', NOW(), NOW(),
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'b2c3d4e5-f6a7-8901-bcde-f12345678901')
ON CONFLICT (id) DO NOTHING;

-- Ajusta a sequência de IDs após inserts manuais
SELECT setval(pg_get_serial_sequence('product', 'id'), (SELECT COALESCE(MAX(id), 1) FROM product));

-- ---------------------------------------------------------------------------
-- 4. Festas de demonstração (2 registros)
-- ---------------------------------------------------------------------------
INSERT INTO party (id, title, description, generate_budget, img_example, last_atualization, observations, status, enterprise_id, user_id)
VALUES
    (1, 'Aniversário Infantil — Tema Espaço',
     'Festa com decoração espacial, mesa de doces e animação',
     1219.90, 'https://placehold.co/400x250?text=Festa+Espaco',
     NOW(), 'Cliente prefere tons azul e prata', 'PENDENTE',
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'b2c3d4e5-f6a7-8901-bcde-f12345678901'),
    (2, 'Casamento ao Ar Livre',
     'Cerimônia e recepção com decoração rústica',
     539.90, 'https://placehold.co/400x250?text=Casamento',
     NOW(), 'Previsão para 120 convidados', 'APROVADO',
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'b2c3d4e5-f6a7-8901-bcde-f12345678901')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('party', 'id'), (SELECT COALESCE(MAX(id), 1) FROM party));

-- ---------------------------------------------------------------------------
-- 5. Produtos vinculados às festas
-- ---------------------------------------------------------------------------
INSERT INTO party_products (party_id, product_id)
SELECT v.party_id, v.product_id
FROM (VALUES (1, 1), (1, 2), (1, 3), (2, 1), (2, 2)) AS v(party_id, product_id)
WHERE NOT EXISTS (
    SELECT 1 FROM party_products pp
    WHERE pp.party_id = v.party_id AND pp.product_id = v.product_id
);

-- ---------------------------------------------------------------------------
-- Verificação rápida
-- ---------------------------------------------------------------------------
-- SELECT u.username, r.name AS role FROM users u
-- JOIN user_roles ur ON ur.user_id = u.uuid
-- JOIN "role" r ON r.role_id = ur.role_id;
