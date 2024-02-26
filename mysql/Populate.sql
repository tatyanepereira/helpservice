-- POPULATE

-- ADMIN
insert into usuarios (dtype, nome, email, perfil, senha, habilitado, endereco_id)
value ("Administrador", "Admin", "admin@email.com.br", "ADMIN", "$2a$10$r3r9V682sIhE/61jZjqkauT.08pxrBx.GE1T.yEogN5r8Ly2S8eTK", 1); # 123456

-- Tabela Serviços
INSERT INTO servicos (categoria, descricao, nome) 
VALUES 
	('Limpeza', 'Serviço de limpeza residencial', 'Limpeza Residencial'),
	('Saúde', 'Consulta médica de clínico geral', 'Consulta Clínico Geral'),
	('Treinamento', 'Treinamento corporativo em liderança', 'Treinamento em Liderança'),
    ('Consultoria', 'Consultoria empresarial em estratégia de mercado', 'Consultoria em Estratégia de Mercado'),
    ('Educação', 'Aulas particulares de matemática para alunos do ensino médio', 'Aulas de Matemática para Ensino Médio'),
    ('Design', 'Design de logotipo para empresas', 'Design de Logotipo'),
	('Desenvolvimento', 'Desenvolvimento de aplicativo móvel', 'Desenvolvimento de App'),
    ('Tecnologia', 'Instalação e configuração de redes de computadores', 'Instalação de Redes de Computadores'),
    ('Saúde', 'Sessão de fisioterapia para reabilitação', 'Fisioterapia de Reabilitação'),
    ('Eventos', 'Planejamento e organização de festas de casamento', 'Organização de Casamentos'),
    ('Saúde', 'Serviços de terapia ocupacional para crianças', 'Terapia Ocupacional Infantil'),
    ('Desenvolvimento', 'Desenvolvimento de jogos para dispositivos móveis', 'Desenvolvimento de Jogos Mobile');
    
    
-- Prestador Serviço
INSERT INTO prestadores_servicos (servico_id, prestador_id)
VALUES
    (11, 11),
    (13, 12),
    (15, 11),
    (16, 12),
    (17, 11),
    (18, 12),
    (20, 11),
    (21, 12),
    (22, 11),
    (11, 12),
    (13, 11),
    (15, 12),
    (16, 11),
    (17, 12),
    (18, 11),
    (20, 12),
    (21, 11),
    (22, 12),
    (11, 11),
    (13, 12),
    (15, 11),
    (16, 12),
    (17, 11),
    (18, 12),
    (20, 11),
    (21, 12),
    (22, 11),
    (11, 12),
    (13, 11),
    (15, 12);
