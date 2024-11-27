Installation et Déploiement du Projet Spring
Ce guide vous accompagne dans les étapes nécessaires pour configurer et déployer le projet Spring.

---

Prérequis
Assurez-vous que votre système dispose des outils suivants :
Docker
Maven (version 3.9.5)
Java (version 21)

---

Étapes d'installation
Installer Docker
Assurez-vous que Docker est correctement installé sur votre machine. Consultez la documentation officielle de Docker pour les instructions détaillées.

---

Installer Maven (version 3.9.5)
Téléchargez Maven 3.9.5 :

   wget https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz

Extrayez le contenu de l'archive :

   sudo tar -xvzf apache-maven-3.9.5-bin.tar.gz -C /opt/

Remplacez l'exécutable Maven existant par la nouvelle version :

   sudo rm /usr/bin/mvn
   sudo ln -s /opt/apache-maven-3.9.5/bin/mvn /usr/bin/mvn

Vérifiez la version installée :

mvn -v



---

Installer Java (version 21)
Mettez à jour la liste des paquets :

sudo apt update

Installez OpenJDK 21 :

sudo apt install openjdk-21-jdk


Configurez la version par défaut de Java :

sudo update-alternatives --config java


Vérifiez la version installée :

java --version



---

Construire le projet avec Maven
Nettoyez et construisez les packages :

mvn clean package


---

Construire l'image Docker
Lancez la construction des images Docker définies dans le fichier docker-compose.yml :

   docker compose build


---

Démarrer les conteneurs Docker
Lancez les conteneurs définis dans le fichier docker-compose.yml :

docker compose up



---

Accéder à l'application
Une fois les conteneurs en cours d'exécution, l'application sera accessible à l'adresse suivante :
[http://localhost:8080](http://localhost:8080)
