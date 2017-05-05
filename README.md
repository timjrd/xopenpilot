# Projet technologique XOpenPilot
*Timothée Jourde, Laëtitia Leoni*

Ce fichier explique comment compiler et lancer une session de démonstration sur votre machine, à savoir :
- le bus
- le moniteur
- le client de test
- le capteur simulé (gyroscope)
- le capteur mobile (accéléromètre Android)

Ce projet utilise Maven et Android Studio. Pour commencer, il vous faut donc installer ces deux outils.

# Bus
Pour compiler et lancer le bus :
```
cd xopenpilot-libbus/
mvn compile
mvn exec:java@bus
```

Attendez quelques secondes, vous pouvez maintenant connecter les clients.

# Moniteur
Pour compiler et lancer le moniteur :
```
cd xopenpilot-libbus/
mvn compile
mvn exec:java@monitor
```

Une boite de dialogue va s'afficher, saisissez "localhost" puis validez.

# Client de test
Pour compiler et lancer le client de test :
```
cd xopenpilot-libbus/
mvn compile
mvn exec:java@client -Dexec.args="localhost"
```

Vous pouvez maintenant envoyer des messages sur le bus et les consulter sur le moniteur.

# Capteur simulé
Pour compiler et lancer le capteur simulé :
```
cd xopenpilot-libbus/
mvn compile
mvn exec:java@gyroscope -Dexec.args="localhost"
```

# Capteur mobile
Il faut tout d'abord compiler et installer la bibliothèque client :
```
cd xopenpilot-libbus/
mvn compile
mvn install
```

Lancez ensuite Android Studio et ouvrez le répertoire `xopenpilot-android-sensors/`.
Activez le débogage USB sur votre smartphone Android et branchez le en USB sur votre machine.
À partir d'Android Studio, compilez et déployez l'application.

Sur l'application, saisissez l'adresse IP de votre machine et appuyez sur `CONNECT`. Le smartphone doit être dans le même réseau que votre machine. Si vous avez un par-feux, désactivez le.
