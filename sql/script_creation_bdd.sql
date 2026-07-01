DROP DATABASE IF EXISTS storyforge;
CREATE DATABASE IF NOT EXISTS storyforge;
USE storyforge;

CREATE TABLE IF NOT EXISTS histoire (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    resume TEXT
    );

CREATE TABLE IF NOT EXISTS personnage (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          nom VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    description TEXT,
    histoire_id INT NOT NULL,
    CONSTRAINT fk_personnage_histoire FOREIGN KEY (histoire_id)
    REFERENCES histoire(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS scene (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     titre VARCHAR(255) NOT NULL,
    lieu VARCHAR(255),
    moment VARCHAR(255),
    contenu TEXT NOT NULL,
    position INT NOT NULL,
    statut VARCHAR(50) NOT NULL,
    histoire_id INT NOT NULL,
    CONSTRAINT fk_scene_histoire FOREIGN KEY (histoire_id)
    REFERENCES histoire(id) ON DELETE CASCADE,
    CONSTRAINT uq_scene_position UNIQUE (histoire_id, position)
    );

CREATE TABLE IF NOT EXISTS scene_personnage (
                                                scene_id INT NOT NULL,
                                                personnage_id INT NOT NULL,
                                                PRIMARY KEY (scene_id, personnage_id),
    CONSTRAINT fk_sp_scene FOREIGN KEY (scene_id)
    REFERENCES scene(id) ON DELETE CASCADE,
    CONSTRAINT fk_sp_personnage FOREIGN KEY (personnage_id)
    REFERENCES personnage(id) ON DELETE CASCADE
    );

-- =============================================
-- HISTOIRE 1 : Le Secret de la Bibliothèque
-- =============================================
INSERT INTO histoire (titre, auteur, resume) VALUES (
                                                        'Le Secret de la Bibliothèque',
                                                        'Rebecca Kouadio',
                                                        'Une ancienne bibliothèque renferme un secret oublié depuis plusieurs décennies. Plusieurs personnages vont progressivement découvrir des indices menant à une révélation inattendue.'
                                                    );

INSERT INTO personnage (nom, role, description, histoire_id) VALUES
                                                                 ('Emma', 'Étudiante', 'Passionnée d''histoire et de littérature.', 1),
                                                                 ('Lucas', 'Journaliste', 'Curieux et persévérant.', 1),
                                                                 ('Madame Moreau', 'Bibliothécaire', 'Gardienne des archives de la bibliothèque.', 1),
                                                                 ('Victor', 'Historien', 'Spécialiste des documents anciens.', 1);

INSERT INTO scene (titre, lieu, moment, contenu, position, statut, histoire_id) VALUES
                                                                                    ('La découverte du livre', 'Bibliothèque', 'Matin',
                                                                                     'Emma découvre un livre ancien contenant des annotations mystérieuses.', 1, 'BROUILLON', 1),
                                                                                    ('Recherche dans les archives', 'Salle des archives', 'Après-midi',
                                                                                     'Les deux personnages recherchent l''origine des annotations trouvées dans le livre.', 2, 'EN_COURS', 1),
                                                                                    ('Le témoignage de Victor', 'Université', 'Fin de journée',
                                                                                     'Victor apporte de nouvelles informations concernant l''histoire de la bibliothèque.', 3, 'EN_COURS', 1),
                                                                                    ('La révélation du secret', 'Sous-sol de la bibliothèque', 'Soir',
                                                                                     'Les personnages découvrent enfin le secret caché derrière les archives.', 4, 'PRET_A_PUBLIER', 1);

INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (1, 1), (1, 3);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (2, 1), (2, 2);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (3, 2), (3, 4);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (4, 1), (4, 2), (4, 3), (4, 4);

-- =============================================
-- HISTOIRE 2 : L'Héritière Oubliée
-- =============================================
INSERT INTO histoire (titre, auteur, resume) VALUES (
                                                        'L''Héritière Oubliée',
                                                        'Fatou KA',
                                                        'Une jeune femme découvre qu''elle est l''héritière d''une fortune colossale après la mort mystérieuse de son oncle. Pour toucher l''héritage, elle doit résoudre une énigme que son oncle lui a laissée.'
                                                    );

INSERT INTO personnage (nom, role, description, histoire_id) VALUES
                                                                 ('Léa', 'Héritière', 'Jeune femme déterminée et intelligente, ignorant tout de sa famille.', 2),
                                                                 ('Maître Duval', 'Notaire', 'Notaire de la famille, gardien de nombreux secrets.', 2),
                                                                 ('Antoine', 'Cousin', 'Jaloux de l''héritage, il cherche à écarter Léa.', 2),
                                                                 ('Rose', 'Gouvernante', 'Fidèle domestique de l''oncle, elle détient des informations cruciales.', 2);

INSERT INTO scene (titre, lieu, moment, contenu, position, statut, histoire_id) VALUES
                                                                                    ('La lecture du testament', 'Cabinet du notaire', 'Matin',
                                                                                     'Maître Duval révèle à Léa qu''elle est la seule héritière de son oncle Henri, sous condition de résoudre une énigme cachée dans le manoir familial.', 1, 'PUBLIEE', 2),
                                                                                    ('L''arrivée au manoir', 'Manoir familial', 'Après-midi',
                                                                                     'Léa arrive au manoir pour la première fois. Rose l''accueille froidement tandis qu''Antoine l''observe depuis le couloir avec méfiance.', 2, 'PUBLIEE', 2),
                                                                                    ('La découverte du coffre', 'Bibliothèque du manoir', 'Soir',
                                                                                     'En fouillant la bibliothèque, Léa découvre un coffre dissimulé derrière un tableau. Il porte une inscription : Celui qui comprend le passé ouvre le futur.', 3, 'EN_COURS', 2),
                                                                                    ('La trahison', 'Couloir du manoir', 'Nuit',
                                                                                     'Léa surprend Antoine en train de fouiller les affaires de l''oncle. Une confrontation éclate et Rose révèle enfin le secret de la famille.', 4, 'BROUILLON', 2);

INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (5, 5), (5, 6);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (6, 5), (6, 7), (6, 8);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (7, 5);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (8, 5), (8, 7), (8, 8);

-- =============================================
-- HISTOIRE 3 : Le Dernier Vol
-- =============================================
INSERT INTO histoire (titre, auteur, resume) VALUES (
                                                        'Le Dernier Vol',
                                                        'Rebecca Kouadio',
                                                        'Un pilote à la retraite accepte une dernière mission pour sauver son fils retenu en otage. Mais au fil du voyage, il réalise que rien dans cette mission n''est ce qu''il paraît.'
                                                    );

INSERT INTO personnage (nom, role, description, histoire_id) VALUES
                                                                 ('Commandant Samir', 'Pilote', 'Vétéran de l''aviation militaire, rongé par le remords d''une mission passée.', 3),
                                                                 ('Yasmine', 'Co-pilote', 'Jeune pilote ambitieuse qui cache ses véritables intentions.', 3),
                                                                 ('Karim', 'Otage', 'Fils du commandant Samir, utilisé comme levier de pression.', 3),
                                                                 ('L''Agent', 'Espion', 'Mystérieux personnage qui manipule les événements depuis l''ombre.', 3);

INSERT INTO scene (titre, lieu, moment, contenu, position, statut, histoire_id) VALUES
                                                                                    ('Le chantage', 'Appartement de Samir', 'Nuit',
                                                                                     'Samir reçoit un appel anonyme : son fils Karim a été kidnappé. Pour le récupérer sain et sauf, il doit piloter un avion cargo vers une destination inconnue sans poser de questions.', 1, 'PUBLIEE', 3),
                                                                                    ('La rencontre avec Yasmine', 'Aéroport', 'Aube',
                                                                                     'Samir arrive à l''aéroport où Yasmine l''attend déjà dans le cockpit. Elle lui remet une enveloppe contenant les coordonnées de destination et lui demande de ne pas contacter la tour de contrôle.', 2, 'PUBLIEE', 3),
                                                                                    ('Les doutes en vol', 'Cockpit de l''avion', 'Matin',
                                                                                     'Au-dessus de l''Atlantique, Samir remarque des incohérences dans les documents de vol. Il commence à soupçonner que Yasmine sait plus qu''elle ne le laisse paraître.', 3, 'EN_COURS', 3),
                                                                                    ('La révélation finale', 'Piste d''atterrissage secrète', 'Soir',
                                                                                     'A l''atterrissage, Samir découvre que Karim n''a jamais été en danger. C''était un piège pour l''attirer dans une opération d''infiltration. L''Agent se révèle être un ancien coéquipier de Samir.', 4, 'BROUILLON', 3);

INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (9, 9), (9, 11);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (10, 9), (10, 10);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (11, 9), (11, 10);
INSERT INTO scene_personnage (scene_id, personnage_id) VALUES (12, 9), (12, 10), (12, 11), (12, 12);