# projectMap
Voici le contenu du **README.md** :

---

# Application de Suivi de Localisation en Temps Réel

## Introduction  
Ce projet vise à développer une application mobile Android qui récupère et envoie la localisation en temps réel à un serveur web via un service PHP. L'application utilise également un fragment de carte (Google Maps) pour afficher les différentes positions enregistrées sur le serveur. Cette solution permet de suivre la localisation de plusieurs utilisateurs ou appareils en temps réel.

## Objectifs  
1. **Récupération de la localisation** : Capturer la position GPS de l’utilisateur à intervalles réguliers.  
2. **Envoi au serveur** : Transmettre les données de localisation (latitude, longitude, identifiant IMEI) à un service web.  
3. **Affichage des positions** : Afficher toutes les positions enregistrées sur une carte Google Maps à partir d'un autre service web.  
4. **Intégration serveur** : Utiliser un service PHP pour stocker et récupérer les positions à partir d'une base de données MySQL.  

## Description Fonctionnelle  
### 1. MainActivity – Récupération et Envoi des Localisations  
- **Localisation GPS** : L’application utilise le service GPS du téléphone pour récupérer la latitude et la longitude de l’utilisateur.  
- **Envoi au serveur** : Les données de localisation, accompagnées de l’IMEI du téléphone (un identifiant unique), sont envoyées à un service web via une requête HTTP.  
- **Notification** : L’application informe l’utilisateur du succès ou de l’échec de l’envoi via un toast.

### 2. MapFragment – Affichage des Positions sur Google Maps  
- **Connexion au serveur** : Le fragment de carte récupère les positions via un service PHP.  
- **Affichage des marqueurs** : Chaque position est affichée avec un marqueur indiquant les coordonnées.  
- **Zoom automatique** : La carte effectue un zoom vers la première position reçue.

## Architecture et Technologies Utilisées  
### Côté client (Android) :  
- **Java** : Langage principal pour le développement Android.  
- **Volley** : Librairie pour la gestion des requêtes HTTP.  
- **Google Maps API** : Intégration de cartes interactives.

### Côté serveur (PHP et MySQL) :  
- **PHP** : Crée deux services web (`savePosition.php` et `getAll.php`).  
- **MySQL** : Stocke les positions dans une base de données.

## Fonctionnement du Système  
1. **Récupération de la Position** : Utilise `LocationManager` pour récupérer les coordonnées GPS.  
2. **Envoi de la Position** : Envoie les données au serveur via Volley.  
3. **Affichage sur la Carte** : Le `MapFragment` affiche les positions sous forme de marqueurs.

## Cas d’Utilisation  
1. **Suivi en temps réel d’utilisateurs** : Pour la sécurité ou la logistique.  
2. **Visualisation de trajets** : Permet de suivre le parcours d’un utilisateur.  
3. **Gestion de flotte** : Suivi des véhicules ou équipements en déplacement.

## Suggestions d’Amélioration  
1. **Optimiser la consommation de batterie** en réduisant la fréquence des mises à jour.  
2. **Ajouter une notification en arrière-plan** pour le suivi continu.  
3. **Sécuriser les données avec HTTPS** pour éviter les interceptions.

## Démonstration  
Les captures d'écran suivantes illustrent les résultats finaux de l'application.

**Figure 1** : Capture d'écran 1  
**Figure 2** : Capture d'écran 2  
**Figure 3** : Capture d'écran 3

## Conclusion  
Cette application propose une solution complète de géolocalisation combinant Android, PHP, et MySQL. Elle permet d’envoyer la position GPS en temps réel et de visualiser les positions sur une carte interactive.

---

Vous pouvez copier-coller ce contenu directement dans un fichier nommé **README.md**. Si vous avez besoin d’un fichier déjà prêt, faites-le moi savoir !
