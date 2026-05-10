# 🤖 Project Agents - IsilImage Processing

Ce document définit l'architecture modulaire de la librairie de traitement d'images, les responsabilités de chaque agent pour le développement des modules Java, ainsi que les critères de validation.

## 1. Architecture Overview
Le système est conçu comme une bibliothèque modulaire (`ImageProcessing`) divisée en packages spécifiques, où chaque agent est responsable d'un domaine mathématique et algorithmique du traitement d'image.

## 2. Agent Definitions

### **Agent A**: Filtrage Linéaire (Global & Local)
**Rôle** : Implémenter les traitements fréquentiels et les masques de convolution.

**Responsabilités** :
* Développer le filtrage global via la Transformée de Fourier (Passe-bas/haut Idéal et Butterworth).
* Concevoir le filtrage local par masque de convolution ($n \times n$ impair) et filtre moyenneur.
* Gérer le sous-package `ImageProcessing.Lineaire`.

### **Agent B**: Traitement Non-Linéaire (Morphologie)
**Rôle** : Gérer les opérateurs morphologiques élémentaires et complexes.

**Responsabilités** :
* Implémenter l'érosion, la dilatation, l'ouverture et la fermeture pour les images binaires et niveaux de gris.
* Développer la dilatation/reconstruction géodésique et le filtrage médian.
* Gérer le sous-package `ImageProcessing.NonLineaire`.

### **Agent C**: Gestionnaire d'Histogramme
**Rôle** : Analyser les paramètres de l'image et appliquer les rehaussements de contraste.

**Responsabilités** :
* Calculer les métriques statistiques : min, max, luminance et contrastes (formules 1.28 à 1.30).
* Créer les courbes tonales (Linéaire, Saturation, Gamma, Négatif et Égalisation d'histogramme).
* Gérer le sous-package `ImageProcessing.Histogramme`.

### **Agent D**: Expert Contours & Segmentation
**Rôle** : Détecter les structures d'objets et isoler les zones d'intérêt.

**Responsabilités** :
* Implémenter les gradients linéaires (Prewitt, Sobel, Laplaciens) et non-linéaires (Beucher, etc.).
* Mettre en place les méthodes de seuillage (Simple, Double et Automatique).
* Gérer les sous-packages `ImageProcessing.Contours` et `ImageProcessing.Seuillage`.

### **Agent E**: Intégrateur UI & Applications
**Rôle** : Développer l'interface Swing et résoudre les cas pratiques de validation.

**Responsabilités** :
* Concevoir l'interface graphique interactive avec menus pour chaque fonctionnalité.
* Assurer la visualisation des résultats "Avant/Après" et l'affichage des histogrammes.
* Résoudre les 7 applications pratiques (réduction de bruit, extraction de pois, segmentation d'outils, etc.).

## 3. Implementation Logic
La bibliothèque repose sur des principes de programmation robustes pour faciliter l'intégration :

* **Méthodes Statiques** : Toutes les classes de traitement utilisent des méthodes statiques pour un appel direct depuis l'UI.
* **Structure de Données** : Les images sont manipulées sous forme de matrices d'entiers (`int[][]`).

## 4. Evaluation & Quality Control
Le succès du projet dépend de la maîtrise technique et théorique :
* **Explication du Code** : Capacité à justifier chaque algorithme et paramètre utilisé.
* **Interprétation des Résultats** : Analyser l'effet physique des traitements sur les images.
* **Cohésion d'Équipe** : Chaque membre doit être capable d'expliquer l'intégralité du projet.