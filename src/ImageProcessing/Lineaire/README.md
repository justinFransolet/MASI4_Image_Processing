# Filtrage Linéaire

Ce module regroupe deux classes Java (`FiltrageLineaireLocal.java` et `FiltrageLineaireGlobal.java`) dédiées à l'atténuation du bruit, au lissage ou au rehaussement de détails sur des images en niveaux de gris (`int[][]`). Il met en œuvre deux approches fondamentales : le filtrage par convolution (domaine spatial) et le filtrage fréquentiel (domaine de Fourier).

---

## 1. Filtrage Linéaire Local (`FiltrageLineaireLocal.java`)

Le filtrage local opère directement dans le **domaine spatial**. La nouvelle valeur d'un pixel est calculée en combinant les valeurs de ses pixels voisins à l'aide d'une matrice de coefficients appelée **masque de convolution**.

### Fonctionnalités Implémentées

* **Convolution Générique (`filtreMasqueConvolution`) :**
    * **Principe :** Parcourt l'image et applique un masque carré de taille impaire ($3 \times 3$, $5 \times 5$, etc.). Pour chaque position, il effectue la somme pondérée des pixels du voisinage par les coefficients du masque.
    * **Gestion des bords :** Les pixels trop proches des bords (ne disposant pas d'un voisinage complet) sont ignorés et conservent leur valeur d'origine.
* **Filtre Moyenneur (`filtreMoyenneur`) :**
    * **Principe :** Génère dynamiquement un masque de convolution uniformément rempli par la valeur $\frac{1}{\text{tailleMasque}^2}$.
    * **Effet visuel :** Floute l'image, lisse les transitions abruptes et atténue le bruit électronique fin (bruit gaussien).

### Signatures des Méthodes

```java
public static int[][] filtreMasqueConvolution(int[][] image, double[][] masque);
public static int[][] filtreMoyenneur(int[][] image, int tailleMasque);
```

---

## 2. Filtrage Linéaire Global (`FiltrageLineaireGlobal.java`)

Le filtrage global opère dans le **domaine fréquentiel**. L'image subit une transformée de Fourier, est multipliée par une fonction de transfert mathématique (filtre), puis est reconstruite dans le domaine spatial via une transformée inverse.

### Filtres Fréquentiels Implémentés

* **Filtre Passe-Bas Idéal (`filtrePasseBasIdeal`) :**
* **Principe :** Conserve intactes toutes les basses fréquences situées à une distance inférieure ou égale à la fréquence de coupure ($f_c$) par rapport au centre du spectre, et coupe brutalement le reste.
* **Effet visuel :** Floute l'image, supprime les détails très fins. Peut générer des artefacts de rebond (phénomène de Gibbs).


* **Filtre Passe-Haut Idéal (`filtrePasseHautIdeal`) :**
* **Principe :** Supprime complètement les basses fréquences au centre du spectre (inférieures à $f_c$) et préserve les hautes fréquences.
* **Effet visuel :** Extrait les contours et les transitions abruptes, l'image finale apparaît sombre avec les transitions illuminées.


* **Filtre de Butterworth Passe-Bas (`filtreButterworthPasseBas`) :**
* **Principe :** Atténue les hautes fréquences de manière continue et fluide selon une fonction mathématique dépendante d'un *ordre*.
* **Avantage :** Élimine le bruit et adoucit l'image sans introduire les artefacts de rebond visuels du filtre idéal.



### Signatures des Méthodes

```java
public static int[][] filtrePasseBasIdeal(int[][] image, int frequenceCoupure);
public static int[][] filtrePasseHautIdeal(int[][] image, int frequenceCoupure);
public static int[][] filtreButterworthPasseBas(int[][] image, int frequenceCoupure, int ordre);
```

---

## 3. Architecture et Robustesse du Code

### Gestion des Règles et Contraintes de Formes

Afin d'éviter les crashs d'exécution (comme les débordements d'index de matrice), les deux classes appliquent des filtres de sécurité stricts (`IllegalArgumentException`) :

1. **Validité des Matrices (`verifierImage`) :** L'image ne doit pas être `null` ou vide, et sa structure doit obligatoirement former un rectangle parfait.
2. **Symétrie des Masques :** Le masque spatial doit être rigoureusement carré et de dimension **impaire** pour posséder un pixel central unique évident.
3. **Cohérence Fréquentielle :** La fréquence de coupure doit être positive ($\geq 0$) et l'ordre du filtre de Butterworth doit être strictement supérieur à $0$.

### Outils Internes Intégrés

* **`distanceEuclidienne` :** Détermine avec précision la distance séparant une coordonnée $(x, y)$ fréquentielle du centre mathématique du spectre de Fourier.
* **`clamp` :** Sécurise les valeurs calculées après traitement pour s'assurer qu'elles se situent toujours dans la plage standard des niveaux de gris $[0, 255]$.

---