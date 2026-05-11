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

---

# Étape 5 — Applications

Voir le dossier `test` dans le dataset.

---

## 1. Réduction du bruit

Objectif : réduire au maximum le bruit présent dans les images `imageBruitee1.png` et `imageBruitee2.png`.

### Image bruitée 1

Après plusieurs tests, la meilleure solution retenue est le **filtrage médian**.

Tests effectués :

- **2 × filtre médian 3x3**  
  Le bruit est encore légèrement visible, mais la fleur reste assez nette.

- **2 × filtre médian 5x5**  
  Le bruit est davantage réduit, mais la fleur devient moins nette.

Conclusion pour cette image :  
Le filtre médian est le plus adapté, car le bruit est principalement de type “sel et poivre”. Plus la taille du filtre augmente, plus le bruit diminue, mais plus l’image devient floue. Le meilleur compromis semble donc être le filtre médian 3x3 appliqué plusieurs fois, ou un filtre médian 5x5 si l’on accepte une perte de netteté.

### Image bruitée 2

Après plusieurs tests, quatre méthodes donnent des résultats visuellement proches :

- fermeture 3x3 + ouverture 3x3 ;
- 2 × filtre médian 3x3 ;
- filtre médian 3x3 + fermeture 3x3 + ouverture 3x3 ;
- filtre médian 5x5.

À l’œil nu, les quatre solutions se valent.

Conclusion pour cette image :  
Le bruit est plus structuré que dans la première image, avec des traits clairs et sombres. Le filtre médian permet de supprimer une partie de ces parasites, tandis que l’ouverture et la fermeture peuvent aider à réduire certaines rayures. Cependant, plus on applique de traitements, plus l’image devient floue. Il faut donc trouver un compromis entre réduction du bruit et conservation des détails du visage.

### Conclusion générale

Plusieurs solutions permettent de réduire le bruit d’une image. L’objectif n’est pas seulement de supprimer tout le bruit, mais aussi de conserver une image suffisamment nette. Une suppression trop forte du bruit peut entraîner une perte importante de détails. Le choix du filtre dépend donc du type de bruit et du résultat visuel recherché.

---

## 2. Rehaussement de l’image lenaAEgaliser.jpg

Objectif : rehausser l’image `lenaAEgaliser.jpg` en réalisant une égalisation d’histogramme.

Deux méthodes ont été testées :

- égalisation séparée des trois composantes RGB ;
- égalisation à partir de la luminance de l’image.

### Méthode A : égalisation séparée des composantes RGB

Dans cette méthode, l’image couleur est séparée en trois composantes :

- rouge ;
- verte ;
- bleue.

Une courbe d’égalisation est ensuite calculée séparément pour chaque composante.  
La composante rouge est donc rehaussée avec sa propre courbe tonale, la composante verte avec une autre courbe, et la composante bleue avec une troisième courbe.

Cette méthode permet d’augmenter fortement le contraste de l’image. Cependant, comme les trois composantes ne sont pas transformées de la même manière, l’équilibre des couleurs peut être modifié. Le résultat obtenu peut donc paraître moins naturel, avec des couleurs légèrement déformées.


### Méthode B : égalisation à partir de la luminance

Dans cette méthode, une image de luminance est d’abord calculée à partir des composantes RGB.

La formule utilisée est :

```text
Y = 0.299R + 0.587G + 0.114B
```
Cette image de luminance représente l’intensité lumineuse globale de l’image couleur. Une seule courbe d’égalisation est ensuite calculée à partir de cette luminance. Cette même courbe tonale est appliquée aux trois composantes rouge, verte et bleue. L’avantage est que les trois composantes RGB sont transformées de la même manière. 
Les relations entre les couleurs sont donc mieux conservées.

### Comparaison des résultats
Les deux méthodes permettent d’améliorer le contraste de l’image.

La méthode A, avec égalisation séparée des composantes RGB, produit une image plus contrastée, mais elle peut modifier les couleurs originales. Cela s’explique par le fait que chaque composante couleur possède sa propre transformation.

La méthode B, basée sur la luminance, donne un résultat plus naturel. Le contraste est amélioré, mais les couleurs restent plus proches de celles de l’image de départ, car une seule et même courbe tonale est appliquée aux trois composantes.

### Conclusion
Visuellement, je choisirai la méthode A.

---

## 3. Extraction des pois rouges et bleus

Objectif : à partir de l’image `petitsPois.png`, créer deux images binaires :
- une image contenant uniquement les pois rouges ;
- une image contenant uniquement les pois bleus.

L’image utilisée est l’image RGB, car l’image en niveaux de gris ne permet pas de distinguer correctement les objets rouges des objets bleus. En niveaux de gris, les deux couleurs deviennent simplement des intensités proches.

### Méthode utilisée

L’image RGB est séparée en trois composantes : rouge, verte et bleue.

Pour extraire les pois rouges, on conserve les pixels dont la composante rouge est dominante par rapport aux composantes verte et bleue.

Pour extraire les pois bleus, on conserve les pixels dont la composante bleue est dominante par rapport aux composantes rouge et verte.

Après cette première segmentation, l’image contient encore de petits points parasites. Une ouverture morphologique est donc appliquée afin de supprimer les petits objets. Une fermeture est ensuite utilisée pour lisser légèrement les formes obtenues.

### Résultat

Les deux images obtenues sont des images binaires :
- les pois sélectionnés valent 255 ;
- le fond vaut 0.

La méthode permet de séparer correctement les gros pois rouges et les gros pois bleus. Les petits points colorés sont supprimés grâce à l’ouverture morphologique, car ils sont beaucoup plus petits que les pois à conserver.

---

## 4. Séparation des grandes et petites balanes

Objectif : à partir de l’image `balanes.png`, créer deux images en niveaux de gris :

- une image contenant les balanes de grande taille ;
- une image contenant les balanes de petite taille.

L’image utilisée est une image en niveaux de gris. Les balanes sont globalement plus claires que le fond, ce qui permet de commencer par une segmentation par seuillage.

### Méthode utilisée

La première étape consiste à isoler toutes les balanes présentes dans l’image. Pour cela, un seuillage automatique est appliqué sur l’image originale.

Le résultat est une image binaire dans laquelle :

- les balanes valent 255 ;
- le fond vaut 0.

Après le seuillage, un nettoyage morphologique est réalisé. Une fermeture est d’abord appliquée afin de combler certaines petites zones noires dans les balanes et de consolider les formes. Ensuite, une ouverture est utilisée pour supprimer une partie du bruit et des petits éléments indésirables.

Les traitements utilisés sont donc :

- seuillage automatique ;
- fermeture 7x7 ;
- ouverture 11x11.

### Extraction des grandes balanes

Pour extraire les grandes balanes, une ouverture morphologique plus importante est appliquée sur le masque contenant toutes les balanes.

L’ouverture avec un élément structurant de taille 23x23 permet de supprimer les objets trop petits. Les petites balanes disparaissent donc en grande partie, tandis que les grandes balanes restent présentes.

Le résultat obtenu sert de masque pour les grandes balanes.

### Extraction des petites balanes

Les petites balanes sont obtenues par soustraction binaire :

```text
petites balanes = toutes les balanes - grandes balanes
```

Autrement dit, on conserve les pixels appartenant au masque de toutes les balanes, mais qui ne sont pas présents dans le masque des grandes balanes.

Après cette soustraction, une ouverture 7x7 est appliquée afin de supprimer une partie des petits parasites restants.

### Création des images finales en niveaux de gris

Les masques binaires obtenus ne sont pas utilisés comme résultats finaux directement. Ils sont appliqués sur l’image originale afin de conserver les niveaux de gris réels des balanes.

Ainsi :

le masque des grandes balanes est appliqué sur l’image originale pour créer l’image des grandes balanes ;
le masque des petites balanes est appliqué sur l’image originale pour créer l’image des petites balanes.

Les pixels appartenant au masque conservent leur intensité d’origine, tandis que les autres pixels sont mis à 0.

### Résultat

L’image des grandes balanes est satisfaisante : les balanes principales sont correctement conservées et le fond est supprimé.

L’image des petites balanes est plus difficile à obtenir proprement. Certaines petites balanes sont bien extraites, mais il reste encore des fragments ou du bruit provenant de la texture du fond et de certaines parties des grandes balanes. Cela s’explique par le fait que les petites balanes ont parfois une intensité proche du bruit ou des détails présents sur les grandes balanes.

### Conclusion

La séparation des balanes par taille fonctionne correctement pour les grandes balanes.
Pour les petites balanes, le résultat est moins précis, car elles sont plus proches du bruit et plus difficiles à distinguer uniquement avec des opérations morphologiques simples.

La méthode retenue repose donc sur :
* un seuillage automatique pour isoler les objets clairs ;
* des opérations morphologiques pour nettoyer l’image ;
* une ouverture de grande taille pour isoler les grandes balanes ;
* une soustraction binaire pour récupérer les petites balanes ;
* l’application des masques sur l’image originale afin d’obtenir des images finales en niveaux de gris.


