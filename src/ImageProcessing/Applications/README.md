# Applications

Année académique : 2025-2026

Dirigé par Jean-Marc Wagner

Fait par Thibault Theunissen & Justin Fransolet

---

## Exercice 1

Réduire au maximum le bruit présent dans les images `imageBruitee1.png` et `imageBruitee2.png`. *Suggestions : filtrage linéaire, filtrage non-linéaire.*

### Méthodes

1. **Filtrage Linéaire Local (Flou Moyenneur) :**
  * **Méthode :** `FiltrageLineaireLocal.filtreMoyenneur(image, tailleMasque)`
  * **Principe :** Application d'un masque de convolution carré et impair (ex: $3 \times 3$ ou $5 \times 5$) où chaque coefficient vaut $1 / \text{tailleMasque}^2$.
  * **Justification :** Idéal pour atténuer le bruit blanc gaussien (variations douces et distribuées) en réalisant un lissage local (somme pondérée du voisinage).
2. **Filtrage Non-Linéaire (Opérateurs Morphologiques) :**
  * **Méthode :** `MorphoElementaire.ouverture(image, 3)` ou `MorphoElementaire.fermeture(image, 3)`
  * **Principe :** L'ouverture (érosion puis dilatation) élimine les petits artéfacts brillants isolés. La fermeture (dilatation puis érosion) comble les points noirs isolés.
  * **Justification :** Parfait pour nettoyer les bruits de type "Impulsionnel" ou "Poivre et Sel" sans détruire complètement la géométrie ou flouter les contours nets des objets principaux.
---

## Exercice 2

Rehausser l’image `lenaAEgaliser.jpg` en réalisant une égalisation d’histogramme.
Tester les deux possibilités : (a) Réaliser l’égalisation de l’histogramme des 3 imagesRGB séparément. 
(b) Égaliser l’histogramme de l’image « luminance » de l’image et appliquer cette même égalisation aux 3 composantes RGB de l’image (même courbe tonale). 
Quelle méthode donne le meilleur résultat ?

### Méthodes

* **Algorithme d'égalisation :** `Histogramme.creeCourbeTonaleEgalisation(matrice)` qui s'appuie sur l'histogramme cumulé pour redistribuer de manière uniforme les dynamiques de contrastes sur une plage $[0, 255]$.
* **Variante (a) - Canaux séparés :** Extraction des trois matrices (Rouge, Vert, Bleu) via `CImageRGB.getMatricesRGB()`. Calcul et application d'une courbe tonale d'égalisation propre à chaque canal, puis reconstruction de l'image.
    * *Résultat :* Cette méthode provoque des dérives chromatiques (modification des teintes originales), car le rapport d'intensité entre le rouge, le vert et le bleu n'est pas conservé.
* **Variante (b) - Courbe de luminance unique (Meilleur résultat) :** Calcul de la luminance moyenne de l'image (par exemple à l'aide de la formule standard $Y = 0.299R + 0.587V + 0.114B$ ou de la moyenne des trois plans). Génération d'une **unique** courbe tonale d'égalisation sur cette base, puis application de cette même transformation mathématique aux trois plans RGB.
    * *Résultat :* C'est la méthode (b) qui offre le meilleur rendu visuel, car elle rehausse efficacement le contraste et la luminosité globale sans altérer ni dénaturer les couleurs initiales de l'image.

---

## Exercice 3

À partir de l’image `petitsPois.png`, créer deux images binaires, l’une comportant les poids bleus, l’autre les pois rouges. 
*Suggestions : érosion, dilatation, ouverture, fermeture, seuillage, opérations géodésiques.*

### Méthodes

1. **Isolation des Pois Rouges :**
    * **Pipeline :** Soustraction matricielle brute entre le canal Rouge et le canal Bleu ($R - B$) pour éliminer le fond neutre et les pois bleus.
    * **Binarisation :** Application d'un seuillage automatique d'Otsu (`Applications.applicationOtsu`) sur la matrice résultante pour isoler proprement les formes.
    * **Post-traitement :** Une fermeture morphologique (`MorphoElementaire.fermeture`) supprime le bruit de fond résiduel et comble les éventuels trous au centre des pois.
2. **Isolation des Pois Bleus :**
    * **Pipeline :** Soustraction inverse ($B - R$) pour annuler la signature des pois rouges.
    * **Binarisation & Nettoyage :** Même logique d'Otsu suivie d'une ouverture/fermeture morphologique pour obtenir un masque binaire parfait des cercles bleus.

---

## Exercice 4

À partir de l’image `balanes.png`, créer deux images en niveaux de gris, l’une comportant les balanes de grande taille, l’autre les balanes de petite taille.
*Suggestions : érosion, dilatation, ouverture, fermeture, seuillage, opérations géodésiques.*

### Méthodes

1. **Séparation des Grandes Balanes (Par Ouverture Taille-Dépendante) :**
    * **Étape 1 :** Application d'une ouverture morphologique (`MorphoElementaire.ouverture`) avec un élément structurant de taille intermédiaire (ex: $7 \times 7$ ou $9 \times 9$).
    * **Principe :** L'érosion fait disparaître complètement les petites balanes (car l'élément structurant ne tient pas à l'intérieur), tandis que les grandes balanes survivent, bien que rognées. La dilatation restaure ensuite la taille initiale des grandes structures rescapées.
2. **Reconstruction Géodésique (Optionnel mais recommandé pour la fidélité) :**
    * Utilisation de la `MorphoComplexe.dilatationGeodesique` en prenant l'image ouverte comme marqueur et l'image d'origine comme masque pour reconstruire parfaitement les contours exacts des grandes balanes sans garder les petites.
3. **Séparation des Petites Balanes (Par Soustraction) :**
    * **Formule :** $\text{Image}_{\text{Petites}} = \text{Image}_{\text{Origine}} - \text{Image}_{\text{Grandes}}$
    * Par simple soustraction pixel à pixel des niveaux de gris, les grandes balanes s'annulent mutuellement, laissant isolées les balanes de petite taille sur l'image finale.

---

## Exercice 5

À partir de l’image `tools.png`, réaliser une segmentation binaire afin d’en extraire les outils. 
Le résultat doit donc être une image binaire valant 1 à l’endroit des outils et 0 à l’extérieur. 
*Suggestions : segmentation, érosion, dilatation, ouverture, fermeture.*

### Méthodes

1. **Seuillage Global :** Initialisation de la segmentation par l'algorithme d'Otsu (`Applications.applicationOtsu`) pour séparer les outils (clairs) du fond (sombre).
2. **Filtrage Morphologique de Nettoyage :**
    * **Fermeture (`MorphoElementaire.fermeture`) :** Permet de boucher les trous intérieurs (comme les reflets métalliques sombres ou les textures sur le corps des outils) afin de rendre les silhouettes des objets parfaitement pleines.
    * **Ouverture (`MorphoElementaire.ouverture`) :** Permet de sectionner les fins ponts numériques (faux contacts entre deux objets proches) et d'éliminer les petites impuretés ou poussières isolées sur le tapis de fond.

---

## Exercice 6

Extraire le petit vaisseau spatial de l’image `vaisseaux.jpg` et le coller dans l’image `planete.jpg` (au même endroit). Sauver le résultat sous le nom `synthese.png`.
Créer l’image `synthese2.png` ajoutant un contour rouge (épaisseur d’un pixel) au petit vaisseau.
*Suggestions : érosion, dilatation, ouverture, fermeture, seuillage, opérateurs géodésiques, détection de contours.*

### Méthodes

1. **Création du Masque du Vaisseau :**
    * Seuillage directionnel ou par composante de couleur sur l'image `vaisseaux.jpg` pour isoler la silhouette binaire du petit vaisseau.
    * Nettoyage par ouverture/fermeture pour obtenir un masque parfait (valeur $255$ sur le vaisseau, $0$ ailleurs).
2. **Incrustation Spatiale (`synthese.png`) :**
    * Parcours pixel par pixel des images. Si le masque binaire vaut $255$, on copie le pixel RGB de `vaisseaux.jpg` vers la matrice cible de `planete.jpg`. Sinon, on conserve le pixel d'origine de `planete.jpg`.
3. **Génération du Contour Rouge (`synthese2.png`) :**
    * **Extraction du contour d'épaisseur 1 :** Application d'un gradient morphologique externe sur le masque binaire du vaisseau : $\text{Contour} = \text{Dilatation}(\text{Masque}, 3) - \text{Masque}$.
    * **Application de la couleur :** Similaire à `Applications.marquerContoursEnVert`, on parcourt la matrice de contour obtenue. Partout où le contour est actif ($> 0$), on force les composantes du pixel à une valeur rouge vif : $R = 255, V = 0, B = 0$ directement sur l'image `synthese.png`.

---

## Exercice 7 - A

Détecter les contours des tartines de l’image `Tartines.jpg`. Une fois détectés, tracer ces contours en vert sur l’image de départ.

### Méthodes

1. **Calcul des Gradients Directionnels :**
    * Extraction de la composante horizontale : `int[][] sobelH = ContoursLineaire.gradientSobel(image, 1);`
    * Extraction de la composante verticale : `int[][] sobelV = ContoursLineaire.gradientSobel(image, 2);`
2. **Calcul de la Magnitude du Gradient :**
    * Pour chaque pixel, calcul de la force globale du contour par approximation de la norme : $G[i][j] = |sobelH[i][j]| + |sobelV[i][j]|$. Les valeurs sont stabilisées via la fonction `clamp` à $[0, 255]$.
3. **Binarisation des Contours :**
    * Application d'un seuillage binaire strict (`Seuillage.seuillageSimple`) avec un seuil manuel ou calculé par Otsu pour éliminer le bruit de fond texturé de la forêt et ne conserver que les lignes de contours structurelles blanches sur fond noir.

---

## Exercice 7 - B

Détecter les contours des tartines de l’image `Tartines.jpg`. Une fois détectés, tracer ces contours en vert sur l’image de départ.

### Méthodes

1. **Calcul des Gradients Morphologiques :**
    * Appel à `ContoursNonLineaire.laplacienNonLineaire(image)`.
    * **Algorithme interne :** La méthode génère la dilatation et l'érosion $3 \times 3$ de l'image. Elle extrait le gradient de dilatation ($GD = \text{Dilatation} - \text{Image}$) et le gradient d'érosion ($GE = \text{Image} - \text{Érosion}$), puis calcule leur différence absolue : $\text{Laplacien} = |GD - GE|$.
    * **Avantage :** Cet opérateur non-linéaire fournit des lignes de contours extrêmement fines, stables et parfaitement centrées sur la frontière géométrique réelle, sans l'effet d'élargissement propre aux filtres de convolution.
2. **Binarisation :**
    * Passage de la matrice du Laplacien dans `Seuillage.seuillageSimple` pour obtenir le rendu binaire final des contours nets.
