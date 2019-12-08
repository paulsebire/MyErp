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



