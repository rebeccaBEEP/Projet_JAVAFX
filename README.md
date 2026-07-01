# 📖 Story Forge - Gestionnaire de Structure Narrative

**Story Forge** est une application desktop développée en **Java / JavaFX**. Contrairement à un simple éditeur de texte, cette application permet aux auteurs de concevoir, structurer et organiser les éléments narratifs de leurs histoires sous forme de données exploitables (histoires, personnages, scènes).

## 🚀 Fonctionnalités principales

* **Gestion des histoires :** Création, modification, suppression et consultation.
* **Gestion des personnages :** Ajout de personnages spécifiques à chaque histoire avec définition de leurs rôles et descriptions.
* **Gestion des scènes :** * Création de scènes structurées (Titre, Lieu, Moment, Statut, Position).
* Association de multiples personnages présents dans une scène.
* Recherche textuelle rapide (par titre ou contenu).
* Filtrage avancé par statut (Brouillon, En cours, Publiée, etc.) ou par personnage.


* **Statistiques :** Suivi de l'avancement de l'histoire et analyse de la répartition des scènes.

## 🛠️ Prérequis techniques

Pour exécuter ce projet sur votre machine, vous devez disposer de :

* **Java JDK 17** (ou supérieur)
* **Maven** (pour la gestion des dépendances et le build)
* **MySQL Server** (actif sur le port 3306 par défaut)

## 🗄️ Configuration de la Base de données (Étape cruciale)

Pour que l'application fonctionne correctement (notamment l'ouverture de la fenêtre des scènes), **la base de données doit être initialisée**.

1. Ouvrez votre SGBD MySQL (via Workbench, phpMyAdmin, ou CLI).
2. Exécutez le script SQL fourni à la racine du projet :
```bash
# Fichier à exécuter :
sql/script_creation_bdd.sql

```


*Ce script va créer la base `storyforge`, générer les tables nécessaires (`histoire`, `personnage`, `scene`, `scene_personnage`) et insérer un jeu de données de test (3 histoires complètes).*
3. **Vérification des identifiants :** Par défaut, l'application tente de se connecter à la base avec les identifiants locaux standards. Si vos identifiants MySQL sont différents, veuillez les modifier dans le fichier `src/main/java/com/example/storyforge_project/database/ConnexionBD.java` :
```java
private static final String UTILISATEUR = "root"; // Modifiez si nécessaire
private static final String MOT_DE_PASSE = "root"; // Modifiez si nécessaire (ex: "" ou "votre_mdp")

```



## ▶️ Comment compiler et exécuter l'application

Le projet utilise le plugin `javafx-maven-plugin`. Vous pouvez lancer l'application directement depuis votre terminal à la racine du projet :

1. **Nettoyer et compiler le projet :**
```bash
mvn clean install

```


2. **Lancer l'application :**
```bash
mvn javafx:run

```



## 🧪 Tests

Le projet intègre des tests unitaires et des tests d'intégration avec JUnit 5 pour valider la logique métier (`HistoireService`) et les interactions avec la base de données.

Pour exécuter les tests :

```bash
mvn test

```

*(Note : Les tests d'intégration nécessitent que la base de données locale soit active).*

## 🏗️ Architecture du code

Le code respecte une architecture en couches pour séparer clairement les responsabilités (approche MVC modifiée) :

* **`model` :** Contient les classes métiers (`Histoire`, `Personnage`, `Scene`, `StatutScene`, etc.).
* **`DAO` (Data Access Object) :** Gère la persistance des données et les requêtes SQL (interfaces et implémentations pour chaque entité).
* **`service` :** Contient la logique métier, les validations de règles et fait le pont entre les contrôleurs et les DAO.
* **`database` :** Gère la connexion JDBC.
* **Contrôleurs (racine) :** `HelloController` et `SceneController` gèrent l'interaction avec les vues JavaFX (`.fxml`).

## 👥 Auteurs

* **Rebecca KOUADIO**
* **Fatou KA**
