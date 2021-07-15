## Iteration 4

Itération ciblée sur le peaufinage de l'existant, notament au travers de : 
  - Intégration d'une majeure partie des tests unitaires et d'intégration.
  - Ecriture de JavaDoc
  - Suppression du code inutile
  - Refactorisation de certaines méthodes
  
### Ce que l'on a réussit à produire : 

  Un jeu de scrabble à 4 joueurs non-humains en ligne ; ces joueurs sont gérés par des IA. Notre architecture est découpée en micro-service dans une structure Docker, dont voici les différents services :
    - Service de vérification de mots
    - Service de Joueurs
    - Service de gestion de parties
 
  Nous avons également produit une librairie "Share" qui contient l'ensemble de nos modèle d'objets transitants sur le réseau.
  
  Il est intéressant de noter que nous atteint le seuil de x% au terme de cette itération.
  
### Ce que l'on aurait aimé améliorer

  - Diversifier les comportements d'IA pour les joueurs
  - Implémenter un service de statistique portant sur le jeu
  - Extraire des statistiques pour les stocker en base de données
