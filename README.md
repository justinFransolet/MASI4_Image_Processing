# MASI4 Image Processing

Année académique : 2025-2026

Dirigé par Jean-Marc Wagner

Ce projet consiste à concevoir une mini-librairie de traitement d'images en Java. Il a été réalisé dans le cadre du cours de **Traitement du Signal 1D et 2D** dirigé par Jean-Marc Wagner.

L'objectif principal est de comprendre et maîtriser les concepts fondamentaux du traitement d'image abordé dans le syllabus de théorie. Le but ultime est la compréhension profonde du sujet et des algorithmes plutôt que le produit fini lui-même.

---

## Structure du Projet

La librairie est organisée en plusieurs sous-packages spécialisés au sein de `ImageProcessing` :

* `Lineaire` : Filtrage fréquentiel (global) et spatial (local).
* `NonLineaire` : Opérateurs morphologiques élémentaires et complexes.
* `Histogramme` : Calcul de statistiques et méthodes de rehaussement.
* `Contours` : Opérateurs de détection de contours linéaires et non-linéaires.
* `Seuillage` : Algorithmes de segmentation binaire et multiple.

---

## Fonctionnalités Implémentées

### 1. Filtrage Linéaire
* **Global (Domaine fréquentiel)** : Filtres Passe-Bas et Passe-Haut de type Idéal et Butterworth.
* **Local (Domaine spatial)** : Filtrage par masque de convolution générique (taille $n \times n$ impaire) et filtrage moyenneur.

### 2. Traitement Non-Linéaire (Morphologie Mathématique)
* **Opérateurs Élémentaires** : Érosion, dilatation, ouverture et fermeture pour images binaires et niveaux de gris.
* **Opérateurs Complexes** : Dilatation et reconstruction géodésique, ainsi que le filtrage médian.

### 3. Gestion de l'Histogramme
* **Analyse** : Calcul du minimum, maximum, luminance et contraste de l'image.
* **Rehaussement** : Transformation linéaire (avec ou sans saturation), correction Gamma, négatif d'image et égalisation d'histogramme.

### 4. Détection de Contours et Segmentation
* **Contours** : Gradients de Prewitt et Sobel (horizontal/vertical), Laplacien (masques 4 et 8-voisinage) et gradients morphologiques (Erosion, Dilatation, Beucher).
* **Segmentation** : Seuillage simple, double et automatique.

---

## Applications Pratiques

Le menu "Applications" de l'interface permet de lancer la résolution de 7 problèmes types :
1.  **Réduction de bruit** sur des images bruitées.
2.  **Égalisation d'histogramme** sur les composantes RGB séparées ou sur la luminance.
3.  **Extraction de couleurs** (pois rouges et bleus) via opérations morphologiques et seuillage.
4.  **Tri par taille** (balanes) via opérations géodésiques.
5.  **Segmentation d'objets** complexes (outils).
6.  **Synthèse d'image** (détourage de vaisseau spatial) avec ajout de contours.
7.  **Analyse de contours** (détection et tracé en vert sur l'image d'origine).

---

## Consignes et Évaluation

* **Travail en équipe** : Projet réalisé par un groupe de 3 étudiants.
* **Interface Utilisateur** : Une interface Java permet d'interagir avec les différents paramètres (fréquences de coupure, masques, seuils).
* **Évaluation** : Présentation orale portant sur l'explication du code, des algorithmes et l'interprétation des résultats obtenus.