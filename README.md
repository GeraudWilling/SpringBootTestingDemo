# Projet Démo des Tests unitaires/d'intégrations avec le framework Spring Boot

## Pré-requis

Maven >= 3.6.2
Java Open JDK >= 1.8

## Développement

Lancer l'application Spring Boot avec la commande

    mvn spring-boot:run
L'application se lance par défaut sur le port 8080.

## Configuration
Le port de lancement du serveur peuvent être configurés dans le fichier de propriétés
```properties
#application.properties
server.port=8080
logging.level.root=DEBUG
```

## Testing
* Pour lancer les tests unitaires (plugin maven surefire) :


    mvn test
    
* Pour lancer les tests d'intégration (plugin maven fail-safe) :


    mvn verify

* Pour tester l'api en ligne de commande:
```bash
curl -v http://localhost:8080/api/employees
```  

## License
[MIT](https://choosealicense.com/licenses/mit/)   
    
    