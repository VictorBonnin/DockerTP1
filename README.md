# 🚗 DockerTP1 — Gestion de Voitures

Application Spring Boot avec une API REST de gestion de voitures, une interface web, des tests unitaires JUnit et un pipeline CI/CD GitHub Actions.

**Auteur** : Victor BONNIN — TP DevOps Efrei

---

## Fonctionnalités

- **Interface web** : Ajouter, lister et supprimer des voitures depuis le navigateur
- **API REST** : Endpoints `/cars` (GET, POST, DELETE)
- **Tests unitaires** : JUnit 5 + MockMvc
- **CI/CD** : GitHub Actions (tests automatiques + build Docker)

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

## 🔄 CI/CD avec GitHub Actions

Chaque **push** ou **pull request** déclenche automatiquement le pipeline CI :

1. ✅ Checkout du code
2. ✅ Installation de Java 25
3. ✅ Exécution des tests (`./gradlew test`)
4. ✅ Build de l'application (`./gradlew build`)
5. ✅ Build de l'image Docker

Les résultats sont visibles dans l'onglet **Actions** du repository GitHub.

> **Important** : Si les tests échouent, le code ne peut pas être fusionné ! Corrigez les tests avant de faire un merge.

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
