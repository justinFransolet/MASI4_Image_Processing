# Seuillage

Année académique : 2025-2026

Dirigé par Jean-Marc Wagner

Fait par Thibault Theunissen & Justin Fransolet

---

La classe Seuillage regroupe un ensemble de méthodes statiques dédiées au traitement **d'images en niveaux de gris** (représentées sous forme de matrices d'entiers `int[][]`). 
Son objectif principal est de segmenter une image ou de l'isoler en plages d'intensités spécifiques en appliquant des seuils numériques.

---

## 1. Vue d'ensemble des Fonctionnalités

Cette classe fournit trois principaux types de traitements algorithmiques :

* **Seuillage Simple** : Sépare l'image en deux classes de pixels (noir et blanc) selon qu'ils dépassent ou non une valeur pivot.
* **Seuillage Double** : Isole une plage de niveaux de gris spécifique entre un seuil bas et un seuil haut.
* **Seuillage Automatique** : Calcule dynamiquement le seuil optimal basé sur la moyenne de l'image de manière itérative avant d'appliquer un seuillage binarisé.

---

## 2. Spécification des Méthodes Publiques

`seuillageSimple`

Transforme une image en niveaux de gris en une image binaire (noir et blanc).

* **Signature** : 
    ```java
    static int[][] seuillageSimple(int[][] image, int seuil);
    ```
* **Principe Mathématique** :
    $$resultat(x,y) = \begin{cases} 255 & \text{si } image(x,y) \geq seuil \\ 0 & \text{sinon} \end{cases}$$
* **Effet visuel** : Les zones claires deviennent uniformément blanches et les zones sombres deviennent uniformément noires.
* **Exceptions levées** : IllegalArgumentException si l'image est invalide (vide, non rectangulaire) ou si le seuil n'est pas compris entre $0$ et $255$.

`seuillageDouble`

Isole les pixels dont l'intensité se situe à l'intérieur d'une plage fermée définie par deux valeurs limites.

* **Signature** : 
    ```java
    static int[][] seuillageDouble(int[][] image, int seuil1, int seuil2);
    ```
* **Principe Mathématique** :
    $$resultat(x,y) = \begin{cases} 255 & \text{si } seuil1 \leq image(x,y) \leq seuil2 \\ 0 & \text{s_*inon} \end{cases}$$
* **Effet visuel** : Permet de faire ressortir une bande de gris précise (par exemple, uniquement les gris intermédiaires) en masquant en noir tout ce qui est trop sombre ou trop clair.
* **Exceptions levées** : IllegalArgumentException si les seuils sont hors limites ou si seuil1 >= seuil2.

`seuillageAutomatique`

Calcule automatiquement un seuil optimal de manière itérative, puis binarise l'image.

* **Signature** : 
    ```java
    static int[][] seuillageAutomatique(int[][] image);
    ```
* **Algorithme interne** :
  1. Calcul du seuil initial $S_0$ correspondant à la moyenne globale des niveaux de gris de l'image. 
  2. À chaque étape $t$, division de l'image en deux groupes :
     * Groupe 1 : Pixels d'intensité $< S_t$
     * Groupe 2 : Pixels d'intensité $\geq S_t$
  3. Calcul des moyennes d'intensité de chaque groupe : $\mu_1$ et $\mu_2$. 
  4. Mise à jour du seuil : $S_{t+1} = \frac{\mu_1 + \mu_2}{2}$. 
  5. Répétition jusqu'à convergence (lorsque $S_{t+1} == S_t$). 
  6. Application de seuillageSimple avec le seuil final obtenu.
  
* **Effet visuel** : Offre une binarisation adaptative robuste, sans nécessiter l'intervention humaine pour régler le paramètre du seuil.

---

## 3. Méthodes Privées (Utilitaires Internes)
Ces méthodes renforcent la robustesse du code en encapsulant les validations de données et les calculs statistiques intermédiaires :

`verifierImage`

* **Signature** : private static void verifierImage(int[][] image)
* **Rôle** : S'assure que l'objet image n'est pas null, possède des dimensions strictement supérieures à zéro et présente une structure de matrice parfaitement rectangulaire (chaque ligne possède la même longueur).

`verifierSeuil`

* **Signature** : private static void verifierSeuil(int seuil)
* **Rôle** : Valide que la valeur du seuil respecte la plage standard d'un pixel en niveaux de gris, c'est-à-dire l'intervalle $[0, 255]$.

`verifierOrdreSeuils`

* **Signature** : private static void verifierOrdreSeuils(int seuil1, int seuil2)
* **Rôle** : Garantit la cohérence géométrique du seuillage double en validant que seuil1 est strictement inférieur à seuil2.

`moyenneImage`

* **Signature** : private static int moyenneImage(int[][] image)
* **Rôle** : Parcourt l'intégralité de l'image pour calculer et retourner la moyenne arithmétique globale (arrondie à l'entier inférieur) de l'intensité des pixels.

---