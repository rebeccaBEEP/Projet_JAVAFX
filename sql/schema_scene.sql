-- Table des scènes (relation 1-N avec histoire)
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

-- Table de jointure pour la relation N-N entre scène et personnage
CREATE TABLE IF NOT EXISTS scene_personnage (
    scene_id INT NOT NULL,
    personnage_id INT NOT NULL,
    PRIMARY KEY (scene_id, personnage_id),
    CONSTRAINT fk_sp_scene FOREIGN KEY (scene_id)
        REFERENCES scene(id) ON DELETE CASCADE,
    CONSTRAINT fk_sp_personnage FOREIGN KEY (personnage_id)
        REFERENCES personnage(id) ON DELETE CASCADE
);