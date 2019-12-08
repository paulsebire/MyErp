## Projet P9
* TAUX DE COVERAGE du projet : 98,6%
* Répertoire - doc : Diagramme de classes, configuration jenkins, taux de coverage
* Répertoire - docker : conteneur docker de la base de données du projet
##### Requêtes SQL :
    Création de la base de données et du jeux de données : 
        - /docker/dev/init/dev/docker-entrypoint-initdb.b/*
    Les requêtes du projet :
        - /myerp-consumer/src/main/resources/com/dummy/myerp/consumer/sqlContext.xml
---
### Initialisation du projet

#### 1 - Docker :
Dans le fichier /docker/dev/docker-compose.yml modifier l'adresse IP en fonction de votre propre environnement

    ports:
        - "[votre adresse ip]:9032:5432"

Depuis votre environnement docker
 
    1) se placer dans le répertoire du conteneur : 
        -  cd /docker/dev
        
    2) Charger le conteneur
        - docker-compose up -d
        
    3) Démarrage du conteneur
        - docker run --name dev_myerp.db_1 -e POSTGRES_PASSWORD=myerp -d postgres

    Pour arréter le conteneur
        - docker-compose stop

#### 2 - Properties Database
Modifier l'adresse ip que vous avez paramétré dans docker

    - /myerp-consumer/src/main/resources/database.properties :
        myerp.datasource.driver=org.postgresql.Driver
        myerp.datasource.url=jdbc:postgresql://[votre adresse ip]:9032/db_myerp
        myerp.datasource.username=usr_myerp
        myerp.datasource.password=myerp
        
#### 3 - Tests unitaires
Afin de vérifier le coverage on s'appuie sur jacoco et sonar ( optionnel )
les profiles à activer sont :
    
    - coverage et sonar
    
Lancement avec sonar la commande maven :

    - mvn clean verify sonar:sonar

Lancement sans sonar commande maven :

    - mvn clean verify

Lancement avec jenkins (voir paramétrage dans /doc/jenkinsConfig)

Pour le taux de coverage :

    - soit avec sonar
    - soit par votre navigateur en ouvrant les fichiers :
        - myerp-business/target/site/jacoco/index.html
        - myerp-consumer/target/site/jacoco/index.html
        - myerp-model/target/site/jacoco/index.html


## Correctifs principaux
*   Absent de configuration dataSource
*   Dans l'entité `EcritureComptable`, correction des méthodes `getTotalCredit()` && `getTotalDebit()` sur le format de retour 2 chiffres après la virgule?
*   Dans l'entité `EcritureComptable`, correction de la méthode `getTotalCredit()` qui accédait à la méthode `getDebit()` au lieu de `getCredit()`
*   Dans l'entité `EcritureComptable`, correction de l'expression régulière qui était erronée
*   Dans le fichier `sqlContext.xml`, corriger la propriété `SQLinsertListLigneEcritureComptable`. Il manquait une virgule dans le INSERT entre les colonnes `debit` et `credit`
*   Dans la classe `ComptabiliteManagerImpl`, correction de la méthode `updateEcritureComptable()`. Ajouter la ligne `this.checkEcritureComptable(pEcritureComptable);` en haut afin de vérifier que la référence de l'écriture comptable respecte les règles de comptabilité 5 et 6
*   Dans la classe `SpringRegistry` de la couche business, modification de la variable `CONTEXT_APPLI_LOCATION` afin d'adapter le chemin d'accès au fichier `bootstrapContext.xml` qui est un conteneur Spring IoC, dans lequel on importe le `businessContext.xml`, `consumerContext.xml` et le `datasourceContext.xml` qui va redéfinir le bean `dataSourceMYERP` pour les tests
*   Correction de divers code smells indiqués par le plugin SonarLint de IntelliJ et par SonarQube
