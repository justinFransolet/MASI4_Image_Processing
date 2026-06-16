# Transformée de Fourier 2D

Ce module regroupe la classe Java (`Fourier.java`) nécessaire au passage d'une image du **domaine spatial** (matrice de pixels) au **domaine fréquentiel** (spectre des fréquences spatiales). Il permet de réaliser de l'analyse spectrale et prépare les données pour le filtrage linéaire global (passe-bas, passe-haut, Butterworth).

---

## 1. Analyse Fréquentielle (`Fourier.java`)

La classe `Fourier` implémente l'algorithme de passage et de réorganisation du spectre fréquentiel.

### Fonctions Principales

* **Transformée de Fourier 2D (`Fourier2D`) :**
    * **Signature :** 
    ```java
    public static MatriceComplexe Fourier2D(double[][] f);
    ```
    * **Principe :** Applique la formule de Fourier discrète à deux dimensions sur une matrice de signaux réels (l'image spatiale). Elle décompose l'image en une somme de fonctions sinus et cosinus représentant les variations spatiales (les textures et les contours).
* **Décroisement Spectral (`decroise`) :**
    * **Signature :** 
    ```java 
    public static MatriceComplexe decroise(MatriceComplexe F);
    ```
    * **Principe :** Par défaut, le calcul informatique place les composantes de basses fréquences (continues) aux quatre coins de la matrice. La méthode `decroise` effectue une permutation croisée des quadrants.
    * **Effet visuel :** Déplace le centre fréquentiel (fréquence zéro) au **centre de la matrice**. C'est cette représentation standard qui permet d'afficher un spectre de Fourier interprétable (le centre brillant représente la luminosité moyenne globale et la périphérie les hautes fréquences).

---

## 2. Architecture et Algorithme interne

L'implémentation de la transformée de Fourier 2D s'appuie sur une séparation des dimensions :
1. **Transformation en lignes ($n \rightarrow v$) :** Le signal subit d'abord une projection horizontale calculée avec les fonctions trigonométriques `Math.cos` et `Math.sin` appliquées aux indices.
2. **Transformation en colonnes ($m \rightarrow u$) :** Une seconde passe verticale est effectuée sur le résultat intermédiaire pour finaliser la matrice complexe à deux dimensions.

*Note : L'algorithme utilise un modèle itératif de mise à jour des fonctions trigonométriques (`newCosTheta` / `newSinTheta`) optimisant l'évolution des phases $\theta$ à l'intérieur des boucles imbriquées.*

---