CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- foreigner keys
    CONSTRAINT fk_posts_author 
        FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_posts_subject 
        FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- foreigner keys
    CONSTRAINT fk_comments_author 
        FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_post 
        FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_subscription (user_id, subject_id),
    
    -- foreigner keys
    CONSTRAINT fk_subscriptions_user 
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_subscriptions_subject 
        FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);


-- Subjects
INSERT INTO subjects (name, description) VALUES
('JavaScript', 'JavaScript est un langage de programmation utilisé principalement pour le développement web.
Il permet de rendre les pages dynamiques et interactives dans les navigateurs.
C’est également le langage central des technologies front-end modernes comme React, Angular et Vue.
Avec Node.js, il est aussi utilisé côté serveur, ce qui permet un développement full-stack complet.
Sa communauté active et ses milliers de bibliothèques en font un outil incontournable.'),
('Java', 'Java est un langage de programmation orienté objet robuste et portable.
Il est largement utilisé dans les applications d’entreprise, le développement Android et les systèmes embarqués.
Grâce à la JVM, il peut s’exécuter sur presque toutes les plateformes.
Il bénéficie d’un écosystème riche, comprenant des frameworks comme Spring et Hibernate.
Sa syntaxe stricte et sa stabilité en font un choix privilégié pour les grandes entreprises.'),
('Angular', 'Angular est un framework open-source développé par Google basé sur TypeScript.
Il est conçu pour construire des applications web modernes et performantes en mode single-page (SPA).
Angular repose sur un système de composants modulaires et réutilisables.
Il propose aussi des outils puissants pour la gestion des formulaires, des routes et des services REST.
Sa structure claire et son intégration avec TypeScript favorisent la maintenabilité des projets.'),
('Spring Boot', 'Spring Boot est un framework Java qui simplifie la création d’applications autonomes.
Il repose sur l’écosystème Spring mais réduit la configuration manuelle grâce à des conventions intelligentes.
Il permet de créer rapidement des API REST robustes et sécurisées.
Spring Boot inclut un serveur embarqué (comme Tomcat), ce qui accélère le déploiement.
Il est aujourd’hui un standard pour le développement backend dans le monde Java.');

-- User with bcrypt password (Test!123)
INSERT INTO users (username, email, password) VALUES
('User Test', 'user@test.com', '$2a$10$FVxZAlfybFg6/FaDosMxIeOk.N6OHIfDq81WKIi1PAeO80ZeTCP7q');

-- Add one article for each subject written by this user
INSERT INTO posts (title, content, author_id, subject_id, created_at) VALUES
('Introduction à JavaScript',
'JavaScript est aujourd’hui l’un des langages les plus utilisés dans le monde.
Il permet de rendre les pages web interactives et dynamiques.
Sa syntaxe est simple à apprendre pour un débutant mais propose des concepts avancés pour les experts.
Il est supporté par tous les navigateurs modernes, ce qui garantit une compatibilité universelle.
JavaScript est aussi utilisé côté serveur grâce à Node.js.
Cela permet aux développeurs de travailler en full-stack avec un seul langage.
De nombreux frameworks comme React, Angular ou Vue reposent sur JavaScript.
Il existe une infinité de bibliothèques et outils disponibles pour accélérer le développement.
La communauté est immense et très active, avec de nouvelles mises à jour constantes.
Apprendre JavaScript, c’est donc investir dans une compétence incontournable pour l’avenir.',
1, 1, '2025-01-10 10:15:00'),

('Pourquoi apprendre Java en 2025 ?',
'Java est un langage de programmation mature et extrêmement répandu.
Il est au cœur de millions d’applications professionnelles à travers le monde.
Son principal atout est sa portabilité grâce à la machine virtuelle Java (JVM).
Cela permet à un même programme de tourner sur différents systèmes d’exploitation sans modification.
Java est le choix privilégié pour les grandes entreprises grâce à sa robustesse et sa stabilité.
Il possède un écosystème de frameworks et bibliothèques très riche.
Spring, Hibernate et Jakarta EE en sont quelques exemples puissants.
En 2025, Java reste encore demandé dans les secteurs bancaire, médical et industriel.
Sa communauté active continue de l’améliorer et de le moderniser.
Pour un développeur, Java reste un investissement solide et durable.',
1, 2, '2025-01-15 14:30:00'),

('Découvrir Angular pour vos projets web',
'Angular est un framework moderne développé par Google et basé sur TypeScript.
Il est conçu pour faciliter la création d’applications web complexes et évolutives.
Sa structure en composants encourage la modularité et la réutilisation du code.
Il propose un système puissant de gestion des formulaires et des validations.
La navigation entre pages est simplifiée grâce à un routeur intégré.
Angular offre aussi des outils pour consommer facilement des API REST.
Sa compilation et son optimisation intégrées améliorent les performances.
La documentation officielle est riche et la communauté très active.
Apprendre Angular permet de se préparer à des projets web d’envergure.
C’est un choix idéal pour les développeurs qui cherchent une solution complète.',
1, 3, '2025-01-20 09:00:00'),

('Spring Boot : simplifier le développement backend',
'Spring Boot est un framework Java qui révolutionne le développement backend.
Il réduit considérablement le temps de configuration grâce à son approche convention over configuration.
Avec lui, on peut créer rapidement des API REST robustes et sécurisées.
Il inclut un serveur embarqué comme Tomcat, ce qui facilite les tests et le déploiement.
Spring Boot s’intègre parfaitement avec les autres projets Spring.
Il propose de nombreux starters pour accélérer la mise en place de projets.
La gestion de la sécurité est simplifiée grâce à Spring Security.
Il est largement adopté dans le monde professionnel, en particulier dans les grandes entreprises.
Sa documentation claire et ses mises à jour régulières assurent une grande pérennité.
Pour tout développeur Java, maîtriser Spring Boot est devenu indispensable.',
1, 4, '2025-01-25 18:45:00');