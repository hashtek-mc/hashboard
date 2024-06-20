## 📋 HashBoard v0.0.3 - Guide d'utilisation

### Description de la librairie
Cette librairie est faîte pour les éléments suivants :
- Création d'équipes *(Avec préfix / suffix / taille)*
- Création de Scoreboard *(Sidebar)*
- Création de Tablist *(Header + Footer)*

## 🏁 Getting Started

> [!information]
> Les sidebars ainsi que les tablists utilisent les packets.

### Création d'une board
Pour commencer, il faudra créer une board *(avec HashBoard)*.
```java
import fr.hashtek.spigot.hashboard.HashBoard;

HashBoard board = new HashBoard();
```

> [!information]
> Cette instance d'HashBoard de gérer une équipe, une sidebar *(souvent appelé scoreboard par les joueurs)*, et une tablist.

### Création d'une équipe
Pour créer une équipe, il vous faudra [créer une instance d'HashBoard](#-pour-commencer).

Pour créer une équipe, il faudra renseigner:
1. La priorité de l'équipe.
2. Le préfix de l'équipe.
3. Le suffix de l'équipe.
4. Une instance d'HashBoard.

> [!important]
> Il est fortement recommandé de préciser l'instance d'HashBoard à la création de la classe.

> [!warning]
> La taille d'un préfix/suffix ne peut pas être supérieur 16 caractères.
>
> Si la board n'est pas indiquée, alors l'affichage ne sera pas effectué,
> et certaines fonctionnalités tel que le friendlyFire ne seront pas disponibles.

> [!info]
> Plus la priorité est élevé, plus les joueurs de l'équipe seront affichés en bas de la tablist.

```java
import fr.hashtek.spigot.hashboard.HashBoard;
import fr.hashtek.spigot.hashboard.HashTeam;

HashBoard board; // Instance d'HashBoard

HashTeam team = new HashTeam(0, "[TeamPrefix] ", " [TeamSuffix]", 10, board);

// Principales méthodes
team.add(player);
team.remove(player);
team.has(player);
team.setFriendlyFire(true); // Les joueurs de l'équipe pourront ainsi se frapper.
```

### Création d'une sidebar
Pour créer une équipe, il vous faudra [créer une instance d'HashBoard](#-pour-commencer).

```java
import fr.hashtek.spigot.hashboard.HashBoard;
import fr.hashtek.spigot.hashboard.HashSideBar;
import fr.hashtek.spigot.hashboard.HashTeam;

HashBoard board;
HashSideBar sidebar = new HashSideBar(board);

// Principales méthodes
sidebar.setTitle("titre"); // Définit le titre de la sidebar.
sidebar.addLine("Nouvelle ligne."); // Ajoute une nouvelle ligne.
sidebar.setLine(4, "Ma 4ème ligne."); // Définit la 4ème ligne par "Ma 4ème ligne."
sidebar.setLines("Mes lignes 1, 2, 3.", 1, 2, 3); // Définit les lignes 1, 2, 3 par la string "Mes lignes 1, 2, 3".
sidebar.fillLines("Mes lignes de 4 à 8.", 4, 8); // Définit les lignes 4 à 8 par la string "Mes lignes de 4 à 8".
sidebar.removeLine(6); // Retire la ligne 6.
sidebar.removeLines(4, 5, 7); // Retire les lignes 4, 5, 7.
sidebar.clearLines(1, 10); // Retire les lignes 1 à 10
sidebar.flush(); // Retire toutes les lignes de la sidebar.
```

### Création d'une tablist
> [!warning]
> N'oubliez pas de mettre à jour votre tablist après avoir définit votre
> header et/ou votre footer pour que les modifications soient prises en compte.

```java
import fr.hashtek.spigot.hashboard.HashTabList;

HashTabList tablist = new HashTabList();

// Principales méthodes
tablist.addDefaultPlayer(player); // Voir la documentation

tablist.setHeader("Mon header"); // Définit le header par "Mon header"
tablist.setFooter("Mon footer"); // Définit le footer par "Mon footer"

tablist.update(); // Met à jour la tablist de tous les defaultPlayers
tablist.update(players); // Met à jour la tablist de tous les defaultPlayers ainsi que les joueurs passés en paramètre.
```

> [!info]
> Pour plus d'information sur les classes, [cliquez ici](readme/MORE_DETAILS.md).
