# Mathématiques Complexes

Année académique : 2025-2026

Dirigé par Jean-Marc Wagner

Fait par Thibault Theunissen & Justin Fransolet

---

Ce module regroupe deux classes Java (`Complexe.java` et `MatriceComplexe.java`) indispensables pour la manipulation des signaux et le passage dans le domaine fréquentiel (Transformée de Fourier 2D). Le traitement d'image recourt aux nombres complexes pour dissocier et stocker l'**amplitude** (énergie des textures) et la **phase** (orientation et contours) de chaque fréquence spatiale.

---

## 1. Modélisation d'un Nombre Complexe (`Complexe.java`)

La classe `Complexe` représente un nombre mathématique de type $z = a + ib$, où $a$ est la partie réelle et $b$ la partie imaginaire.

### Propriétés et Attributs
* `partieReelle` (`double`) : Représente la composante sur l'axe réel.
* `partieImaginaire` (`double`) : Représente la composante sur l'axe imaginaire ($i^2 = -1$).

### Opérations Mathématiques et Indicateurs

* **Calcul de l'Amplitude (`getModule`) :**
    * **Formule :** $|z| = \sqrt{\text{partieReelle}^2 + \text{partieImaginaire}^2}$
    * **Application :** Permet d'extraire le spectre de puissance/magnitude d'une image pour visualiser l'importance de chaque composante fréquentielle.
* **Calcul de l'Orientation (`getPhase`) :**
    * **Formule :** $\theta = \operatorname{atan2}(\text{partieImaginaire}, \text{partieReelle})$
    * **Application :** Détermine les déphasages spatiaux nécessaires à la reconstruction exacte des formes géométriques.
* **Addition Cumulée (`additionne`) :**
    * Modifie le complexe courant en lui ajoutant un autre complexe :
      $z_{\text{final}} = (a_1 + a_2) + i(b_1 + b_2)$
* **Multiplication Cumulée (`multiplie`) :**
    * Réalise le produit algébrique complexe :
      $z_{\text{final}} = (a_1 \cdot a_2 - b_1 \cdot b_2) + i(a_1 \cdot b_2 + b_1 \cdot a_2)$
* **Conjugué (`conjugue`) :**
    * Retourne un nouvel objet complexe avec une partie imaginaire inversée : $\bar{z} = a - ib$. Très utilisé pour les algorithmes de transformées inverses.

---

## 2. Conteneur Bidimensionnel (`MatriceComplexe.java`)

La classe `MatriceComplexe` encapsule un tableau à deux dimensions d'objets `Complexe[][]`. Elle fait office de structure d'accueil pour le spectre complet d'une image après l'application de la méthode `Fourier2D`.

### Fonctionnalités Clés

* **Initialisation Sécurisée :** Le constructeur instancie automatiquement chaque case de la matrice avec un objet `Complexe(0.0, 0.0)` pour s'assurer qu'aucune référence `null` ne provoque de crash (`NullPointerException`) lors des itérations de calcul.
* **Getters / Setters Adaptés :**
    * `set(ligne, colonne, Complexe)` : Assigne directement un objet complexe.
    * `set(ligne, colonne, reel, imaginaire)` : Surcharge utilitaire permettant de mettre à jour une case sans avoir à instancier manuellement l'objet à l'extérieur.
* **Projections de Données Spatialisées :**
    * **`getPartieReelle()` :** Extrait et renvoie une matrice brute `double[][]` contenant uniquement les valeurs réelles.
    * **`getPartieImaginaire()` :** Extrait et renvoie une matrice brute `double[][]` contenant uniquement les valeurs imaginaires.
    * **`getModule()` :** Calcule le module de chaque cellule et renvoie un tableau `double[][]`. C'est cette matrice qui est généralement convertie en niveaux de gris $[0, 255]$ pour afficher visuellement le spectre de Fourier.

---