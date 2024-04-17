# `📋 HashBoard v0.0.2 - Guide d'utilisation`

## Description de la librairie
Cette librairie est faîte pour les éléments suivants :
- Création d'équipes *(Avec préfix / suffix)*
- Création de Scoreboard *(SideBar)*
- Création de Tablist *(Header + Footer)*

---

## `HashBoard`

### Prototype
```java
HashBoard();
```

### Description
Classe principale permettant d'utiliser HashSideBar et HashTeam avec toutes leurs fonctionnalités.

---

## `HashSideBar`

### Prototype
```java
HashSideBar(HashBoard board);
```

### Description
Classe permettant de créer une sidebar. *(Le p'tit rectangle avec un fond noir sur le côté droite de l'écran du joueur)*

### Paramètres
`HashBoard board`: Une instance de la classe HashBoard.
- `⚠️`  Il est fortement recommandé de n'avoir qu'une instance de HashBoard

### Exemple

Pour ajouter un scoreboard *(appelé sidebar)*:
```java
HashBoard board = new HashBoard();

board.setToPlayers(player1, player2, player3, ...);
// ou
board.setToPlayers(listOfPlayers);

HashSideBar sidebar = new HashSideBar(board);

sidebar.setTitle("My title");

sidebar
    .setLine(0, "Line 0")
    .setLine(1, "Line 1")
    .setLines("Lines 2, 3 and 4", 2, 3, 4)
    .fillLines("Lines from 5 to 8", 5, 8)
    .addLine("Line 9")
    .addLine("Line 10")
    .addLine("Line 11");
```

Ce qui donne :
![image](https://github.com/hashtek-mc/hashboard/assets/83085376/61f3a353-b63f-4915-89b7-035e8248045b)

Pour retirer des lignes du scoreboard *(appelé sidebar)*:
```java
sidebar
    .removeLine(0)
    .removeLine(1)
    .removeLines(2, 3, 4)
    .clearLines(5, 11);
```

---

## `HashTeam`

### Prototype
```java
HashTeam(int tablistPriority, String prefix, String suffix, int teamSize, HashBoard board);
HashTeam(int tablistPriority, String prefix, String suffix, int teamSize);
```

### Description
Classe permettant de créer/gérer une équipe.

### Paramètres
`int tablistPriority`: La priorité du joueur dans le scoreboard. *(Plus le nombre est élevé, plus la priorité est faible)*

`String prefix`: Le préfixe de l'équipe. *(Ce qui sera affiché à gauche du pseudo du joueur.)*
- `⚠️` Un préfixe ne peut contenir plus de 16 caractères.

`String suffix`: Le suffixe de l'équipe. *(Ce qui sera affiché à droite du pseudo du joueur.)*
- `⚠️` Un suffixe ne peut contenir plus de 16 caractères.

`int teamSize`: La taille de l'équipe.

`HashBoard board`: Une instance de la classe HashBoard.
- `⚠️` Il est fortement recommandé de n'avoir qu'une instance de HashBoard

### Exemple

Pour créer des équipes :
```java
HashBoard board = new HashBoard(); // Important pour afficher
                         // les équipes au dessus de
                         // la tête des joueurs et
                         // dans le tablist

board.setToPlayers(player1, player2);
// ou
board.setToPlayers(listOfPlayers);

HashTeam blue = new HashTeam(0, "§9Bleu ", " §8Equipe bleu", 2, board);
HashTeam red = new HashTeam(1, "§cRouge ", " §8Equipe rouge", 2);

red.setBoard(board); // Si vous n'avez pas définit le board dans le constructeur
                     // Il est préférable de le définir dans le constructeur directement

red.add(player1);
blue.add(player2);
```

En partant du principe que `player1` est le joueur nommé `Epitoch` et que `player2` est le joueur nommé `Zeynix`, le résultat sera :
![image](https://github.com/hashtek-mc/hashboard/assets/83085376/b3351b1e-e242-44bd-b44d-f4e76e8efbfc)
![image](https://github.com/hashtek-mc/hashboard/assets/83085376/02e53092-38b5-43ba-b27f-6ad4bfcf4625)
![image](https://github.com/hashtek-mc/hashboard/assets/83085376/848e487c-68c7-412b-94f9-56616c531b4f)

`⚠️` Ajouter un joueur dans une équipe ne le retire pas de toutes les autres équipes. Il faut donc vérifier et retirer manuellement le joueur de son équipe actuelle pour qu'il ne soit pas présent dans plusieurs équipes à la fois.

Pour retirer un joueur d'une équipe :
```java
red.remove(player);
```

Pour vérifier qu'un joueur appartient à une équipe :
```java
boolean has = red.has(player); // true si le joueur est dans l'équipe rouge, autrement false
```

---

## `HashTabList`

### Prototype
```java
HashTabList();
```

### Description
Classe permettant de gérer le header et le footer du tablist.

### Exemple

Pour définir un header et un footer :
```java
HashTabList tablist = new HashTabList();

tablist.setHeader("my header");
tablist.setFooter("my footer");

// Mettre à jour le header et le footer du tablist de joueurs
tablist.update(listOfPlayers);
// ou
tablist.update(player1, player2, player3);
```
Ce qui donne :
![image](https://github.com/hashtek-mc/hashboard/assets/83085376/6fd7988a-18e4-458f-914e-ca8e82bc18af)

---

# `⚠️ RAPPEL DES WARNINGS`
- Un préfix/suffix ne peut contenir que 16 caractères (codes couleurs inclut)
- Avoir une seule instance de HashBoard permet de regrouper les sidebars et les teams en une seule classe. C'est pourquoi il est fortement recommandé de n'avoir qu'une seule instance de HashBoard.
- Ajouter un joueur dans une équipe ne le retire pas automatiquement des autres équipes. Il faut donc le retirer manuellement des autres équipes pour qu'il ne soit présent que dans une seule équipe.
