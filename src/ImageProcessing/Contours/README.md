# Module : Extraction de Contours (Traitement d'Image)

Ce module regroupe deux classes Java (`ContoursLineaire.java` et `ContoursNonLineaire.java`) dédiées à la détection et au rehaussement des contours sur des images en niveaux de gris (`int[][]`). Il met en œuvre deux philosophies fondamentales : l'approche linéaire par masques de convolution spatiaux (Prewitt et Sobel) et l'approche non-linéaire basée sur la morphologie mathématique (gradients morphologiques et Laplacien non-linéaire).

---

## 1. Opérateurs de Contours Linéaires (`ContoursLineaire.java`)

Le filtrage linéaire local utilise des masques de convolution $3 \times 3$ spécifiques pour mesurer les variations ou gradients d'intensité directionnels au sein de l'image.

### Filtres Spatiaux Implémentés

* **Gradient de Prewitt (`gradientPrewitt`) :**
    * **Principe :** Utilise un lissage uniforme combiné à une dérivation. Il calcule le saut d'intensité des niveaux de gris selon la direction choisie (`dir = 1` pour l'horizontal, `dir = 2` pour le vertical).
    * **Masques de convolution :**
      $$\text{Horizontal (dir 1)} = \begin{pmatrix} 1 & 0 & -1 \\ 1 & 0 & -1 \\ 1 & 0 & -1 \end{pmatrix}, \quad \text{Vertical (dir 2)} = \begin{pmatrix} 1 & 1 & 1 \\ 0 & 0 & 0 \\ -1 & -1 & -1 \end{pmatrix}$$
* **Gradient de Sobel (`gradientSobel`) :**
    * **Principe :** Similaire à Prewitt, mais attribue un poids supérieur ($2$) au pixel central du voisinage pour renforcer l'effet du gradient et mieux lisser le bruit.
    * **Masques de convolution :**
      $$\text{Horizontal (dir 1)} = \begin{pmatrix} 1 & 0 & -1 \\ 2 & 0 & -2 \\ 1 & 0 & -1 \end{pmatrix}, \quad \text{Vertical (dir 2)} = \begin{pmatrix} 1 & 2 & 1 \\ 0 & 0 & 0 \\ -1 & -1 & -1 \end{pmatrix}$$

### Signatures des Méthodes

```java
public static int[][] gradientPrewitt(int[][] image, int dir);
public static int[][] gradientSobel(int[][] image, int dir);

```

*Note : Les bords extérieurs de l'image de 1 pixel d'épaisseur ne sont pas traités car le masque $3 \times 3$ nécessite un voisinage complet.*

---

## 2. Opérateurs de Contours Non-Linéaires (`ContoursNonLineaire.java`)

Cette approche s'appuie sur la comparaison pixel par pixel entre l'image d'origine et ses transformations par les opérateurs morphologiques élémentaires d'érosion et de dilatation (voisinage $3 \times 3$).

### Algorithmes Implémentés

* **Gradient d'Érosion (`gradientErosion`) :**
* **Formule :** $\text{Gradient}_{\text{Érosion}} = \text{Image} - \text{Érosion}(\text{Image})$
* **Effet visuel :** Extrait les contours internes des structures claires.


* **Gradient de Dilatation (`gradientDilatation`) :**
* **Formule :** $\text{Gradient}_{\text{Dilatation}} = \text{Dilatation}(\text{Image}) - \text{Image}$
* **Effet visuel :** Extrait les contours externes (la bordure extérieure) des structures claires.


* **Laplacien Non-Linéaire (`laplacienNonLineaire`) :**
* **Formule :** $\text{Laplacien}_{\text{Non-Linéaire}} = \text{Gradient}_{\text{Dilatation}} - \text{Gradient}_{\text{Érosion}}$
* **Effet visuel :** Combine les sauts d'intensités internes et externes. Offre des lignes de contours très précises et bien centrées sur la frontière géométrique réelle des objets.



### Signatures des Méthodes

```java
public static int[][] gradientErosion(int[][] image);
public static int[][] gradientDilatation(int[][] image);
public static int[][] laplacienNonLineaire(int[][] image);

```

---

## 3. Sécurité du Code et Fonctions Internes

Afin de garantir la stabilité de l'exécution, les deux classes intègrent des mécanismes de validation et de correction des valeurs :

1. **`verifierImage` :** Lève une exception `IllegalArgumentException` si la matrice passée en paramètre est `null`, vide ou asymétrique (lignes de longueurs inégales).
2. **`clamp` :** Méthode utilitaire interne forçant l'écrêtage de toute valeur calculée (notamment suite aux soustractions ou convolutions) dans la plage physique des niveaux de gris $[0, 255]$.

---