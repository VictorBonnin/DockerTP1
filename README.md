# 🚗 DockerTP1 — Gestion de Voitures

Application Spring Boot avec une API REST de gestion de voitures, une interface web, des tests unitaires JUnit, une couverture de code JaCoCo/Codecov, une analyse de qualité SonarCloud et un pipeline CI/CD GitHub Actions.

**Auteur** : Victor BONNIN — TP DevOps Efrei

---

## Fonctionnalités

- **Interface web** : Ajouter, lister et supprimer des voitures depuis le navigateur
- **API REST** : Endpoints `/cars` (GET, POST, DELETE)
- **Tests unitaires** : JUnit 5 + MockMvc
- **Couverture de code** : JaCoCo + Codecov
- **Qualité du code** : SonarCloud (bugs, vulnérabilités, code smells)
- **CI/CD** : GitHub Actions (tests automatiques + couverture + analyse qualité + build Docker)

---

## Lancer le projet

### Prérequis

- **Java 25** (OpenJDK)
- Vérifiez votre version : `java -version`

### Démarrer l'application

```bash
# Depuis le dossier dockertp1/
./gradlew bootRun
```

> **Windows** : Si vous obtenez l'erreur `Gradle requires JVM 17 or later`, lancez :
> ```powershell
> $env:JAVA_HOME = "C:\Users\bonni\.jdks\openjdk-25"; .\gradlew.bat bootRun
> ```

L'application démarre sur **http://localhost:8080**

### Pages disponibles

| URL | Description |
|-----|-------------|
| [http://localhost:8080](http://localhost:8080) | Interface web (ajout/liste/suppression de voitures) |
| [http://localhost:8080/cars](http://localhost:8080/cars) | API REST — Liste des voitures (JSON) |
| [http://localhost:8080/hello](http://localhost:8080/hello) | Message de bienvenue |

---

## Tests unitaires avec JUnit

### Qu'est-ce qu'un test unitaire ?

Un test unitaire est un programme qui vérifie qu'une partie du code fonctionne correctement. C'est comme une mini-application de test qui :

1. Crée des données de test
2. Exécute du code
3. Vérifie que le résultat est correct

**Pourquoi c'est important ?** Les tests permettent de trouver les bugs rapidement et de vérifier que chaque modification n'a pas cassé la fonctionnalité existante.

### Structure des tests

```
src/test/java/dockertp1/
├── controllers/
│   └── RentServiceRestTest.java    (Tests de l'API REST avec MockMvc)
├── entities/
│   └── CarTest.java                (Tests du modèle Car)
├── services/
│   └── CarServiceTest.java         (Tests du service métier)
├── Dockertp1ApplicationTests.java  (Test de chargement du contexte)
└── HelloControllerTest.java        (Test du endpoint /hello)
```

### Exécuter les tests

```bash
# Depuis le dossier dockertp1/
./gradlew test
```

> **Windows** : Si vous obtenez l'erreur `Gradle requires JVM 17 or later`, lancez :
> ```powershell
> $env:JAVA_HOME = "C:\Users\bonni\.jdks\openjdk-25"; .\gradlew.bat test
> ```

Les résultats s'affichent dans le terminal :
- ✅ Tests réussis
- ❌ Tests échoués

### Voir les résultats détaillés

Les rapports HTML sont générés dans :

```
build/reports/tests/test/index.html
```

Ouvrez ce fichier dans votre navigateur pour voir le détail de chaque test.

---

## Exemples de tests

### Test unitaire simple (CarTest)

```java
@Test
public void testCarConstructor() {
    Car car = new Car("ABC123", "Toyota", 15000.0);
    assertEquals("ABC123", car.getPlateNumber());
    assertEquals("Toyota", car.getBrand());
    assertEquals(15000.0, car.getPrice());
}
```

Ce test crée une voiture et vérifie que ses propriétés sont correctes.

### Test de l'API REST avec MockMvc (RentServiceRestTest)

```java
@Test
public void testAddCar() throws Exception {
    String carJson = """
            {"plateNumber":"ABC123","brand":"Toyota","price":15000.0}
            """;

    mockMvc.perform(post("/cars")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(carJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.plateNumber").value("ABC123"))
            .andExpect(jsonPath("$.brand").value("Toyota"));
}
```

Ce test simule une requête HTTP POST sans démarrer de serveur réel.

### Test du service métier (CarServiceTest)

```java
@Test
public void testGetCarByPlateNumberFound() {
    carService.addCar(new Car("ABC123", "Toyota", 15000.0));
    Optional<Car> result = carService.getCarByPlateNumber("ABC123");
    assertTrue(result.isPresent());
    assertEquals("Toyota", result.get().getBrand());
}
```

---

## 📊 Couverture de code avec JaCoCo et Codecov

### Qu'est-ce que la couverture de code ?

La couverture de code mesure le pourcentage de votre code source qui est testé. C'est une métrique importante pour :

- **Identifier** les parties du code non testées
- **Déterminer** les zones à risque
- **Améliorer** la qualité générale du projet

### Comment fonctionne JaCoCo ?

JaCoCo (Java Code Coverage) est un outil qui :

1. Analyse l'exécution des tests
2. Enregistre quelles lignes de code sont exécutées
3. Génère un rapport de couverture en HTML et XML

### Générer un rapport de couverture localement

```bash
# Depuis le dossier dockertp1/
./gradlew test jacocoTestReport
```

> **Windows** :
> ```powershell
> $env:JAVA_HOME = "C:\Users\bonni\.jdks\openjdk-25"; .\gradlew.bat test jacocoTestReport
> ```

Le rapport complet est accessible à :

```
build/reports/jacoco/test/html/index.html
```

Ouvrez ce fichier dans votre navigateur pour voir :

- **Couverture globale** : Pourcentage total de code couvert
- **Par classe** : Détails pour chaque classe
- **Par méthode** : Quelles lignes sont couvertes/non couvertes

### Codecov et GitHub Actions

Codecov est une plateforme qui agrège et visualise les rapports de couverture. À chaque push ou pull request, le workflow GitHub Actions automatiquement :

1. Exécute les tests avec JaCoCo
2. Télécharge le rapport vers Codecov
3. Ajoute un commentaire à votre PR avec :
   - Le pourcentage de couverture global
   - Les modifications de couverture par rapport à `main`
   - Des badges indiquant la santé du code

### Consulter les résultats sur Codecov

1. Allez sur [https://app.codecov.io](https://app.codecov.io)
2. Connectez-vous avec votre compte GitHub
3. Sélectionnez le repository
4. Vous verrez :
   - L'historique de la couverture
   - Les fichiers avec la meilleure/pire couverture
   - Les comparaisons entre branches

### Interpréter le rapport

| Couverture | Niveau | Description |
|------------|--------|-------------|
| 🟢 90-100% | Très bon | Presque tout est testé |
| 🟡 70-89% | Acceptable | Quelques zones non testées |
| 🔴 < 70% | À risque | Besoin d'ajouter des tests |

### Configuration JaCoCo du projet

Ce projet est configuré pour :

- ✅ Générer des rapports **XML** et **HTML** avec JaCoCo
- ✅ Uploader automatiquement vers **Codecov** en CI/CD
- ✅ Exclure les **entities** (getters/setters) et la **classe Application** de la couverture

> **Important** : Pour que Codecov fonctionne, ajoutez le secret `CODECOV_TOKEN` dans votre repository GitHub :
> 1. Allez sur [https://app.codecov.io](https://app.codecov.io) et récupérez votre token
> 2. Dans GitHub → Settings → Secrets and variables → Actions
> 3. Cliquez **New repository secret** → Name: `CODECOV_TOKEN`, Value: votre token

---

## 🔍 Qualité du code avec SonarCloud

### Qu'est-ce que SonarCloud ?

SonarCloud est une plateforme cloud qui analyse la qualité du code en continu. Elle détecte :

- 🐛 **Bugs** : Erreurs potentielles et code défectueux
- 🔒 **Vulnérabilités de sécurité** : Failles de sécurité connues
- 🧹 **Code smells** : Mauvaises pratiques et code difficile à maintenir
- 🔄 **Duplications** : Code dupliqué inutile
- ✅ **Couverture de tests** : Intégration avec JaCoCo

### Comment fonctionne SonarCloud ?

À chaque push ou pull request, le workflow GitHub Actions :

1. Exécute les tests avec JaCoCo (comme vu précédemment)
2. Lance l'analyse SonarQube
3. Envoie les résultats à SonarCloud
4. Ajoute un rapport de qualité à votre PR

### Consulter les résultats sur SonarCloud

1. Allez sur [https://sonarcloud.io](https://sonarcloud.io)
2. Connectez-vous avec votre compte GitHub
3. Sélectionnez l'organisation **benoit-charroux-1** et le projet **rent**
4. Vous verrez un dashboard complet avec :

**Métriques principales :**

| Métrique | Description |
|----------|-------------|
| 📊 Reliability (Fiabilité) | Nombre de bugs détectés |
| 🔒 Security (Sécurité) | Vulnérabilités et hotspots de sécurité |
| 🧹 Maintainability (Maintenabilité) | Code smells et dettes techniques |
| ✅ Coverage (Couverture) | Pourcentage de code testé (issu de JaCoCo) |
| 🔄 Duplications | Pourcentage de code dupliqué |

### Interpréter un rapport SonarCloud

| Note | Niveau | Description |
|------|--------|-------------|
| 🟢 A | Excellent | Très peu de problèmes |
| 🟢 B | Bon | Quelques problèmes à corriger |
| 🟡 C | Acceptable | Besoin d'améliorations |
| 🟠 D | Faible | Plusieurs problèmes |
| 🔴 E | Mauvais | Nombreux problèmes |

### Activer SonarCloud pour les Pull Requests

Le projet est configuré pour afficher des statuts de qualité directement dans les PRs :

1. Ouvrez une Pull Request sur GitHub
2. Descendez jusqu'à **Checks**
3. Vous verrez **SonarCloud Code Analysis**
4. Le statut peut être :
   - ✅ **Passed** : La qualité du code est acceptable
   - ❌ **Failed** : Des problèmes de sécurité ou de qualité critiques ont été détectés

### Initialiser le SONAR_TOKEN

> **Important** : Le `SONAR_TOKEN` est un secret nécessaire pour que GitHub Actions puisse envoyer les résultats à SonarCloud.

**1. Générer le token dans SonarCloud :**

1. Allez sur [https://sonarcloud.io](https://sonarcloud.io)
2. Cliquez sur votre profil (coin supérieur droit) → **Security**
3. Sous **Generate Tokens**, cliquez **Generate**
4. Donnez-lui un nom (ex: `github-rent`)
5. Copiez le token (vous ne pourrez pas le revoir après)

**2. Ajouter le secret à GitHub :**

1. Allez sur votre repository GitHub
2. **Settings** → **Secrets and variables** → **Actions**
3. Cliquez **New repository secret**
4. Name: `SONAR_TOKEN`, Value: collez le token SonarCloud
5. Cliquez **Add secret**

### Configuration SonarCloud du projet

| Paramètre | Valeur |
|-----------|--------|
| Clé du projet | `benoit-charroux_rent` |
| Organisation SonarCloud | `benoit-charroux-1` |
| URL hôte | `https://sonarcloud.io` |

Les fichiers de configuration sont :

- **`build.gradle`** : Plugin SonarQube avec les propriétés du projet
- **`.github/workflows/ci.yml`** : Workflow qui exécute `./gradlew sonar`

---

## Bonnes pratiques

### Couverture de code

✅ **À faire :**
- Viser une couverture ≥ 80% en production
- Tester les cas de succès ET les cas d'erreur
- Exclure les codes générés (getter/setter, configurations, etc.)
- Réviser les parties non couvertes lors des PRs

❌ **À éviter :**
- Augmenter la couverture artificiellement avec des tests inutiles
- Viser 100% de couverture (rarement réaliste)
- Tester uniquement les "happy paths"

### Qualité du code

✅ **À faire :**
- Viser une note A ou B minimum
- Corriger les vulnérabilités de sécurité avant le merge
- Revoir les bugs critiques et majeurs
- Maintenir la couverture de tests ≥ 80%

❌ **À éviter :**
- Merger une PR avec des issues critiques de sécurité
- Ignorer les rapports SonarCloud
- Augmenter la dette technique sans limite

---

## 🔄 CI/CD avec GitHub Actions

Chaque **push** ou **pull request** déclenche automatiquement le pipeline CI :

1. ✅ Checkout du code (avec historique complet pour SonarCloud)
2. ✅ Installation de Java 25
3. ✅ Exécution des tests + rapport JaCoCo (`./gradlew test jacocoTestReport`)
4. ✅ Upload de la couverture vers Codecov
5. ✅ Analyse SonarCloud (`./gradlew sonar`)
6. ✅ Build de l'application (`./gradlew build`)
7. ✅ Build de l'image Docker

Les résultats sont visibles dans l'onglet **Actions** du repository GitHub.

> **Important** : Si les tests échouent, le code ne peut pas être fusionné ! Corrigez les tests avant de faire un merge.

### Secrets GitHub requis

| Secret | Description | Où le trouver |
|--------|-------------|---------------|
| `SONAR_TOKEN` | Token d'authentification SonarCloud | [sonarcloud.io](https://sonarcloud.io) → Profil → Security |
| `CODECOV_TOKEN` | Token d'authentification Codecov | [app.codecov.io](https://app.codecov.io) → Settings |

---

## Docker

### Builder l'image

```bash
docker build -t dockertp1 .
```

### Lancer le conteneur

```bash
docker run -p 8080:8080 dockertp1
```

L'application sera accessible sur **http://localhost:8080**

---

## 📁 Structure du projet

```
dockertp1/
├── src/
│   ├── main/
│   │   ├── java/dockertp1/
│   │   │   ├── controllers/
│   │   │   │   └── RentServiceRest.java     (API REST /cars)
│   │   │   ├── entities/
│   │   │   │   └── Car.java                 (Modèle de données)
│   │   │   ├── services/
│   │   │   │   └── CarService.java          (Logique métier)
│   │   │   ├── Dockertp1Application.java    (Point d'entrée)
│   │   │   └── HelloController.java         (Endpoint /hello)
│   │   └── resources/
│   │       └── static/
│   │           └── index.html               (Interface web)
│   └── test/
│       └── java/dockertp1/
│           ├── controllers/
│           │   └── RentServiceRestTest.java
│           ├── entities/
│           │   └── CarTest.java
│           ├── services/
│           │   └── CarServiceTest.java
│           ├── Dockertp1ApplicationTests.java
│           └── HelloControllerTest.java
├── .github/workflows/ci.yml                 (Pipeline CI/CD)
├── Dockerfile
├── build.gradle
└── README.md
```

---

## Technologies utilisées

| Technologie | Usage |
|-------------|-------|
| Spring Boot 4.0 | Framework web |
| Java 25 | Langage |
| JUnit 5 + MockMvc | Tests unitaires |
| JaCoCo | Couverture de code |
| Codecov | Visualisation de la couverture |
| SonarCloud | Analyse de qualité du code |
| GitHub Actions | CI/CD |
| Docker | Conteneurisation |
