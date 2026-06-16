# Morphologie non linéaire

Année académique : 2025-2026

Dirigé par Jean-Marc Wagner

Fait par Thibault Theunissen & Justin Fransolet

---

Ce module regroupe deux classes Java (`MorphoElementaire.java` et `MorphoComplexe.java`) dédiées au traitement d'images en niveaux de gris à l'aide d'opérateurs de la morphologie mathématique. Les images y sont manipulées sous forme de matrices d'entiers bidimensionnelles (`int[][]`).

---

## 1. Morphologie Élémentaire (`MorphoElementaire.java`)

La classe `MorphoElementaire` implémente les opérateurs morphologiques de base utilisant un élément structurant carré de taille impaire ($3 \times 3$, $5 \times 5$, etc.).

### Opérateurs Implémentés

* **Érosion :**
    * **Principe :** Remplace l'intensité du pixel courant par la valeur **minimale** trouvée dans son voisinage.
    * **Effet visuel :** Réduit la taille des objets clairs (zones de forte intensité) et élargit les zones sombres. Supprime le bruit blanc de petite taille.
* **Dilatation :**
    * **Principe :** Remplace l'intensité du pixel courant par la valeur **maximale** trouvée dans son voisinage.
    * **Effet visuel :** Élargit les objets clairs et réduit les zones sombres. Comble les petits trous sombres au sein des formes claires.
* **Ouverture :**
    * **Formule :** $\text{Ouverture}(I) = \text{Dilatation}(\text{Érosion}(I))$
    * **Effet visuel :** Lisse les contours externes, supprime les petites îles claires et les détails fins isolés tout en préservant globalement la taille des structures principales.
* **Fermeture :**
    * **Formule :** $\text{Fermeture}(I) = \text{Érosion}(\text{Dilatation}(I))$
    * **Effet visuel :** Lisse les contours internes, comble les petites cavités sombres, connecte les objets clairs proches et bouche les fissures.

### Signatures des Méthodes
```java
public static int[][] erosion(int[][] image, int tailleMasque);
public static int[][] dilatation(int[][] image, int tailleMasque);
public static int[][] ouverture(int[][] image, int tailleMasque);
public static int[][] fermeture(int[][] image, int tailleMasque);
```

---

## 2. Morphologie Complexe & Géodésique (`MorphoComplexe.java`)
La classe `MorphoComplexe` propose des algorithmes plus avancés, notamment basés sur la **morphologie géodésique**. 
Contrairement aux filtres élémentaires qui s'étendent indéfiniment, les transformations géodésiques font évoluer une image de départ (le marqueur) sous la contrainte stricte d'une seconde image (le masque géodésique).

### Opérateurs Implémentés

* **Dilatation Géodésique** (`dilatationGeodesique`)
  * Principe : Dilate l'image avec un élément structurant de taille $3 \times 3$, puis applique un opérateur conditionnel de minimum pixel par pixel avec l'image masque.
  * Formule (pour 1 itération) : $R = \min(\text{Dilatation}(I, 3), \text{Masque})$
* **Reconstruction Géodésique** (`reconstructionGeodesique`) :
  * Principe : Répète la dilatation géodésique de manière itérative jusqu'à la stabilité complète de l'image (lorsque l'itération $t+1$ est identique à l'itération $t$).
  * Application : Permet de reconstruire intégralement des objets connectés complexes qui ont été partiellement marqués, sans modifier leurs contours d'origine.

### Signatures des Méthodes

```java
static int[][] dilatationGeodesique(int[][] image, int[][] masqueGeodesique, int nbIter);
public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique);
```

## 3. Gestion des Exceptions et Robustesse
Les deux classes intègrent des mécanismes stricts de validation des données d'entrée avant tout traitement :
1. **Validation de l'image** (`verifierImage`) : Lève une `IllegalArgumentException` si la matrice est **null**, vide ou si l'image n'est pas parfaitement rectangulaire (lignes de longueurs inégales).
2. **Validation du masque** (`verifierTailleMasque`) : Lève une `IllegalArgumentException` si la taille du masque est inférieure ou égale à $0$, ou si elle est paire (un nombre impair est obligatoire pour garantir la présence d'un pixel central unique).
3. **Cohérence Géodésique** (`verifierMemeTaille`) : Lève une `IllegalArgumentException` si le marqueur et le masque géodésique n'ont pas exactement les mêmes dimensions de matrice.