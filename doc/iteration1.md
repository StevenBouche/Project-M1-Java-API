<h2>Ce que l'on veut faire </h2>
<details>
  <summary>
    <b>
      En tant que joueur, je souhaite jouer une partie. Cela implique : 
    </b>
  </summary>
  <ul>
    <li> D'implémenter un service joueur :  jouer un tour, s'arrêter à la fin de la partie, stocker des lettres </li>
  </ul>
</details>
<br>
<details>
  <summary>
    <b>
      En tant que joueur, je souhaite me connecter à une partie. Cela implique : 
    </b>
  </summary>
    <ul>
      <li> D'implémenter un service de partie qui inclut : la création de partie, l'accueil de 2 joueurs, la gestion d'une partie </li>
    </ul>
</details>
<br>
<details>
  <summary>    
    <b>
      En tant que joueur je souhaite recevoir l'adresse d'une partie disponible. Cela implique : 
    </b>
  </summary>
  <ul>
  <li> D'implémenter un service launcher qui renverra seulement l'adresse de la partie aux joueurs </li>
  </ul>
</details>


<br>
<h2>Ce que l'on à fait </h2> 
<details>
  <summary>
    <b>
      En tant que joueur, je peux jouer une partie : 
    </b>
  </summary>
  <ul>
    <li> Nous avons implémenté un service joueur :  un joueur joue un tour en proposant un mot aléatoire </li>
    <li> Cependant nous n'avons pas encore stocker les lettres d'un joueur </li>
  </ul> 
</details>
<br>
<details>
  <summary>
    <b>
      En tant que joueur, je peux me connecter à une partie :
    </b>
  </summary>
    <ul>
      <li> Nous avons bien implémenté un service de partie qui inclut : la création d'une partie, l'accueil de 2 joueurs et la gestion d'une partie </li>
    </ul>
</details>
<br>

<h2>Ce que l'on doit améliorer </h2> 
<details>
  <summary>    
    <b>
      En tant que joueur je ne peux pas recevoir l'adresse d'une partie disponible. Pour l'instant nous avons :
    </b>
  </summary>
  <ul>
  <li> Une classe Launcher qui sert de Main pour executer les deux services. Il ne s'agit donc pour l'instant pas d'un service, ce sera pour la prochaine itération </li>
  </ul>
</details>
<br>
<details>
  <summary>    
    <b>
      Nous engloberons nos services dans des conteneurs Docker.
    </b>
  </summary>
  Nous changerons quelque peu l'architecture pour permettre l'execution de nos services dans des conteneurs
</details>

<h2>Comment lancer la partie :</h2>
<summary>    
  <ul>
    <li>mvn clean install</li>
    <li>cd launcher</li>
    <li>mvn exec;java</li>
  </ul>
</summary>
