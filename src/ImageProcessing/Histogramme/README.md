# Histogrammes

Année académique : 2025-2026

Dirigé par Jean-Marc Wagner

Fait par Thibault Theunissen & Justin Fransolet

---

Ce module regroupe deux classes Java (`Histogramme.java` et `ImageTools.java`) dédiées au calcul statistique, à la correction de contraste et à l'application de courbes tonales sur des images en niveaux de gris. Les images y sont manipulées sous forme de matrices d'entiers bidimensionnelles (`int[][]`).

---

## 1. Outils Utilitaires de Base (`ImageTools.java`)

La classe `ImageTools` regroupe des fonctions utilitaires indispensables destinées à sécuriser les calculs pixel par pixel et à extraire des indicateurs statistiques fondamentaux.

### Fonctionnalités Clés

* **Écrêtage des Niveaux de Gris (`limiterNiveauGris`) :**
    * **Principe :** Restreint rigoureusement une valeur entière calculée dans l'intervalle standard $[0, 255]$ d'un pixel en niveaux de gris.
    * **Formule :** $\max(0, \min(255, \text{valeur}))$
* **Recherche des Extrémums (`extremum`) :**
    * **Principe :** Parcourt l'ensemble de la matrice pour identifier la valeur d'intensité minimale ou maximale selon un indicateur booléen (`chercherMin`).
* **Calcul de la Moyenne Globale (`averagePixelValue`) :**
    * **Principe :** Calcule la moyenne arithmétique précise (retournée en `double`) de l'intensité lumineuse de la totalité de l'image.

---

## 2. Analyseur et Transformateur d'Histogrammes (`Histogramme.java`)

La classe `Histogramme` offre les outils nécessaires pour quantifier la distribution des niveaux de gris d'une image et générer des **courbes tonales** de transformation (tables de correspondance à 256 entrées).

### Méthodes d'Analyse Statistique

* **Calcul de l'Histogramme (`Histogramme256`) :**
    * **Principe :** Dénombre la fréquence d'apparition de chaque niveau de gris de $0$ à $255$ au sein de l'image. Retourne un tableau `int[256]`.
* **Recherche des Bornes Visuelles (`minimum` / `maximum`) :**
    * **Principe :** Détermine les valeurs minimale et maximale réelles présentes dans l'image (en ignorant les valeurs aberrantes hors de la plage $[0, 255]$).

### Génération de Courbes Tonales (Lookup Tables - LUT)

Le module permet de générer des tableaux de transfert `int[256]` où chaque indice `i` représente le niveau de gris d'entrée et la valeur correspondante représente le nouveau niveau de gris de sortie.

* **Correction Gamma (`creeCourbeTonaleGamma`) :**
    * **Formule :** $f(i) = \text{limiterNiveauGris}\left(255 \times \left(\frac{i}{255}\right)^{\frac{1}{\gamma}}\right)$
    * **Effet visuel :** Ajuste la luminosité globale de manière non-linéaire (éclaircit si $\gamma > 1$, assombrit si $\gamma < 1$) tout en préservant le noir pur ($0$) et le blanc pur ($255$).
* **Inversion Négative (`creeCourbeTonaleNegatif`) :**
    * **Formule :** $f(i) = 255 - i$
    * **Effet visuel :** Inverse les intensités (le noir devient blanc, le blanc devient noir), similaire à un négatif photo.
* **Égalisation d'Histogramme (`creeCourbeTonaleEgalisation`) :**
    * **Principe :** Calcule l'histogramme cumulé pour étaler de façon uniforme les dynamiques de l'image.
    * **Effet visuel :** Augmente de façon optimale le contraste global des images trop sombres, trop claires ou trop ternes.

---

## 3. Robustesse et Tolérance aux Erreurs

Les deux fichiers partagent des règles de programmation défensive pour assurer la stabilité des traitements :
1.  **Tolérance aux matrices irrégulières :** Les boucles de parcours vérifient systématiquement si les lignes de l'image ou si les matrices de pixels sont `null` ou vides avant d'accéder aux données.
2.  **Filtrage des valeurs aberrantes :** Lors de la construction de l'histogramme ou de la recherche d'extrémums, les valeurs situées en dehors de la plage physique $[0, 255]$ sont naturellement ignorées pour éviter les corruptions de données.

---