# Clavardage2

I - Ce qui était prévu:

Au lancement du "Launcher":
- apparition de la fenêtre de login (cf image LoginFrame)
- renseignement du login/mdp et check avec la BDD distante d'authentification)
- si ok, lancement du serveur multicast, du serveur TCP, et de la fenêtre principale de l'application (cf image MainFrame)


II - Description des attendus de la fenêtre principale de l'application:

Panneau de Gauche
- menu Admin accessible si les droits admin sont accordés à la personne loggé:
  -- mise en place des différents paramètres renseignés dans la classe Java "Configuration.java" (adresses multicast, ports multicast et TCP, etc.)
- Avatar, Pseudo et Nom/Prénom de l'utilisateur loggé
- Liste des connectés avec Pseudo et Status en ligne
- Bouton de déconnexion pour retour à la fenêtre de login
- ComboBox pour le choix du status ("ONLINE", "BUSY", "AFK")
- Bouton pour le changement de Pseudo
- Bouton pour le forçage du refresh de la liste des connectés
- Bouton pour l'établissement d'une ChatRoom avec l'utilisateur sélectionné par création d'un onglet et association d'un thread de TCp Client

Panneau de Droite
- Onglet ChatRoom Nbr en liaison avec un iD unique de ChatRoom (concatenation du UniqueID des 2 utilisaterus de la discussion)
- Pseudo de l'interlocuteur
- TextArea avec l'historique de la conversation daté
- Boutton de fin de discussion : arrêt et fermeture de l'onglet de la chatroom, sauvegarde sur BDD distante de l'historique de discussion, et fermeture du thread TCP Client associé
- TextField pour rentrer les messages textes : envoi du message directement par appui sur la touche entrée
- Boutton Upload : permet de choisir un fichier et de l'envoyer en pièce jointe
- Boutton Download : permet de choisir l'emplacement où déposer le fichier reçu


III - Constitution du Programme

1) Launcher ---> Lance le programme principal

Package - MODEL

2) AuthorizedUserHandler ---> décrit les getter/setter du type de données liées à la BDD d'authentification

3) Configuration ---> décrit toutes les variables rêglables par l'administrateur, ou devant être cohérentes au sein du programme

4) PayloadDataHandler ---> décrit les méthodes de transformation de et vers le type PayloadHander

5) PayloadHandler ---> décrit les getter/setter du type de données liés aux paquets TCP

6) UserDataHandler ---> décrit les getter/setter du type de données liées aux utilisateurs pour le Multicast

7) UserListHandler ---> décrit la méthode de création de la liste des utilisateurs connectés de type UserDataHandler pour le Multicast

Package - NETWORK

8) Multicast ---> décrit les méthode pour la création et la gestion du serveur Singleton (single thread) d'envoi/réception du Multicast

9) TCPClient ---> décrit les méthode pour la création et la gestion de clients TCP instantiable (multi-threads)

10) TCPServer ---> décrit les méthode pour la création et la gestion du serveur TCP Singleton (single thread)

Package - VIEW

11) ChatRoomTabFrameController ---> décrit les variables et méthodes liées à la forme standard du contenu d'un onglet de ChatRoom

12) LoginFrameController ---> décrit les variables et méthodes liées à la fenêtre de Login

13) MainFrameController ---> décrit les variables et méthodes liées à la fenêtre principal de discussion


IV - Etat de fonctionnement au 31/01/2019

Sont Fonctionnels:
- la fenêtre de login (avec 2 identité codées en dure car pas de BDD) avec messages d'erreur de saisie, et le bouton X en haut à droite
- ouverture de la fenêtre principal et mise en place du Pseudo
- connection du serveur Multicast (complètement fonctionnel en envoi et réceptions de messages, et exploitation des données reçues)
- connection du serveur TCP (fonctionnel en écoute de connection)
- Bouton LogOut
- Bouton SetPseudo
- Bouton Refresh (pas de refresh automatique à la réception d'un packet multicast: problème d'appel de méthode non static dans une méthode static)

Ne Sont Pas Fonctionnels:
- l'ouverture d'un nouvel onglet au lancement d'une conversation TCP
- les BDD MongoDB ne sont pas connectées ni opérationnelles (encore moins accessibles via servlets)
- Toute la partie droite (ormis pour l'envoi de message du TextField : les commandes et methodes sont écrites mais n'ont pas été testées pour les autres fonctionnalités du Client TCP)


V - Mises à Jour Fonctionnelle post 31/01/2019

Mises à jour datées:
