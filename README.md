<h1 align="center">Scrabble en Web Service</h1>

<p align="center">
  <img src="https://www.dictionary.com/e/wp-content/uploads/2019/02/1000x700-scrabble-3-790x310.jpg">
</p>

<h3> Code pour lancer une partie à deux joueurs : </h3>

<h4>Sans Docker</h4>

<ul>
  <li>mvn clean install</li>
  <li>mvn exec:java</li>
</ul>

<h4>Avec Docker</h4>

<ul>
  <li>mvn clean install</li>
  <li>docker-compose build</li>
  <li>docker-compose up</li>
</ul>


<i>Membres : Armand PREVOT, Steven BOUCHE, Pierre GRISERI, Lina BELKARFA, Margaux PARENT, Corentin GARNIER.</i>

Dans le cadre d'un projet universitaire, nous devons réaliser le jeu de plateau Scrabble en Java Spring.

Notre jeu devra être construit selon une architecture Web Service. Chaque service sera decoupé en module maven ; aussi, chaque service sera dans un conteneur Docker.
Nous devons aussi utiliser l'outil Travis.yml.

Le Scrabble (marque déposée) est un jeu de société et un jeu de lettres où l'objectif est de cumuler des points, sur la base de tirages aléatoires de lettres, en créant des mots sur une grille carrée, dont certaines cases sont primées.
Le jeu est commercialisé dans 121 pays et en 36 langues. L'entreprise Hasbro est détentrice des droits aux États-Unis d'Amérique et au Canada, et Mattel dans le reste du monde1.
Le Scrabble est un jeu de société très populaire en famille et entre amis, mais aussi sur Internet. Il se pratique aussi en clubs et en compétition.



