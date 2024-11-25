# Étape 1 : Utiliser une image de base compatible Java 21
FROM openjdk:21-jdk-slim

# Étape 2 : Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le fichier JAR dans le conteneur
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Étape 4 : Exposer le port 8080 (ou autre port défini dans votre projet)
EXPOSE 8080

# Étape 5 : Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
