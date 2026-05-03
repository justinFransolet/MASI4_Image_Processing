package ImageProcessing.Lineaire;

import ImageProcessing.Complexe.Complexe;
import ImageProcessing.Complexe.MatriceComplexe;
import ImageProcessing.Fourier.Fourier;

public class FiltrageLineaireGlobal {

    /**
     * Filtre passe-bas idéal.
     * Principe :
     * - on passe l'image dans le domaine fréquentiel avec Fourier ;
     * - on garde uniquement les basses fréquences proches du centre du spectre ;
     * - on supprime les fréquences dont la distance au centre est supérieure
     *   à la fréquence de coupure.
     * Effet visuel :
     * - l'image devient plus floue ;
     * - les détails fins et une partie du bruit sont atténués.
     */
    public static int [][] filtrePasseBasIdeal(int [][] image, int frequenceCoupure) {

        System.out.println("Fonction filtrePasseBasIdeal");
        verifierImage(image);
        verifierFrequenceCoupure(frequenceCoupure);

        return appliquerFiltrageFrequentiel(image, (distance, fc, ordre) -> {
            return (distance <= fc) ? 1.0 : 0.0;
        }, frequenceCoupure, 1);
    }

    /**
     * Filtre passe-haut idéal.
     *
     * Principe :
     * - on supprime les basses fréquences proches du centre ;
     * - on garde les hautes fréquences éloignées du centre.
     *
     * Effet visuel :
     * - les contours et détails ressortent davantage ;
     * - l'image peut devenir sombre car la composante moyenne est supprimée.
     */
    public static int [][] filtrePasseHautIdeal(int [][] image, int frequenceCoupure) {

        System.out.println("Fonction filtrePasseHautIdeal");
        verifierImage(image);
        verifierFrequenceCoupure(frequenceCoupure);

        return appliquerFiltrageFrequentiel(image, (distance, fc, ordre) -> {
            return (distance <= fc) ? 0.0 : 1.0;
        }, frequenceCoupure, 1);
    }

    /**
     * Filtre passe-bas de Butterworth.
     *
     * Principe :
     * - comme le passe-bas idéal, il garde surtout les basses fréquences ;
     * - mais la coupure est progressive au lieu d'être brutale.
     *
     * Formule :
     * H(D) = 1 / (1 + (D / D0)^(2n))
     *
     * D  = distance au centre du spectre
     * D0 = fréquence de coupure
     * n  = ordre du filtre
     *
     * Effet visuel :
     * - flou plus progressif et souvent moins brutal que le filtre idéal.
     */
    public static int [][] filtrePasseBasButterworth(int [][] image, int frequenceCoupure, int ordre) {

        System.out.println("Fonction filtrePasseBasButterworth");
        verifierImage(image);
        verifierFrequenceCoupure(frequenceCoupure);
        verifierOrdre(ordre);

        return appliquerFiltrageFrequentiel(image, (distance, fc, n) -> {
            if (fc == 0) {
                return (distance == 0.0) ? 1.0 : 0.0;
            }
            return 1.0 / (1.0 + Math.pow(distance / fc, 2.0 * n));
        }, frequenceCoupure, ordre);
    }

    /**
     * Filtre passe-haut de Butterworth.
     *
     * Principe :
     * - il atténue progressivement les basses fréquences ;
     * - il conserve progressivement les hautes fréquences.
     *
     * Formule :
     * H(D) = 1 / (1 + (D0 / D)^(2n))
     *
     * Effet visuel :
     * - fait ressortir les détails et contours,
     *   mais de manière moins brutale qu'un passe-haut idéal.
     */
    public static int [][] filtrePasseHautButterworth(int [][] image, int frequenceCoupure, int ordre) {

        System.out.println("Fonction filtrePasseHautButterworth");
        verifierImage(image);
        verifierFrequenceCoupure(frequenceCoupure);
        verifierOrdre(ordre);

        return appliquerFiltrageFrequentiel(image, (distance, fc, n) -> {
            if (distance == 0.0) {
                return 0.0; // on supprime la composante continue
            }
            if (fc == 0) {
                return 1.0;
            }
            return 1.0 / (1.0 + Math.pow((double) fc / distance, 2.0 * n));
        }, frequenceCoupure, ordre);
    }

    // =========================================================
    // Méthode générale de filtrage fréquentiel
    // =========================================================

    /**
     * Méthode commune aux 4 filtres fréquentiels.
     *
     * Elle réalise toutes les étapes :
     * 1. conversion de l'image int[][] en double[][];
     * 2. transformée de Fourier 2D ;
     * 3. centrage du spectre ;
     * 4. application du filtre fréquentiel ;
     * 5. décentrage du spectre ;
     * 6. transformée de Fourier inverse ;
     * 7. conversion du résultat en image int[][] entre 0 et 255.
     */
    private static int[][] appliquerFiltrageFrequentiel(
            int[][] image,
            FonctionTransfert fonctionTransfert,
            int frequenceCoupure,
            int ordre
    ) {
        int largeur = image.length;
        int hauteur = image[0].length;

        // Conversion en double[][]
        double[][] imageDouble = new double[largeur][hauteur];
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                imageDouble[x][y] = image[x][y];
            }
        }

        // Transformée de Fourier
        MatriceComplexe fourier = Fourier.Fourier2D(imageDouble);

        // Centrage du spectre
        MatriceComplexe fourierCentre = Fourier.decroise(fourier);

        int centreX = largeur / 2;
        int centreY = hauteur / 2;

        // Application du masque fréquentiel
        for (int u = 0; u < largeur; u++) {
            for (int v = 0; v < hauteur; v++) {
                double distance = distanceEuclidienne(u, v, centreX, centreY);
                double h = fonctionTransfert.calculer(distance, frequenceCoupure, ordre);

                Complexe c = fourierCentre.get(u, v);

                double pr = c.getPartieReelle() * h;
                double pi = c.getPartieImaginaire() * h;

                fourierCentre.set(u, v, new Complexe(pr, pi));
            }
        }

        // Décentrage pour revenir au format attendu par l'iDFT
        MatriceComplexe fourierFiltre = Fourier.decroise(fourierCentre);

        // Transformée de Fourier inverse
        MatriceComplexe imageComplexe = Fourier.InverseFourier2D(fourierFiltre);

        // Récupération de la partie réelle
        double[][] partieReelle = imageComplexe.getPartieReelle();

        int[][] resultat = new int[largeur][hauteur];
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                int val = (int) Math.round(partieReelle[x][y]);
                resultat[x][y] = clamp(val);
            }
        }

        return resultat;
    }

    // =========================================================
    // Vérifications
    // =========================================================

    /** Vérifie que l'image existe et que toutes ses colonnes ont la même hauteur. */
    private static void verifierImage(int[][] image) {
        if (image == null || image.length == 0 || image[0].length == 0) {
            throw new IllegalArgumentException("Image invalide.");
        }

        int hauteur = image[0].length;
        for (int x = 0; x < image.length; x++) {
            if (image[x] == null || image[x].length != hauteur) {
                throw new IllegalArgumentException("L'image n'est pas rectangulaire.");
            }
        }
    }

    /** Vérifie que la fréquence de coupure est positive ou nulle. */
    private static void verifierFrequenceCoupure(int frequenceCoupure) {
        if (frequenceCoupure < 0) {
            throw new IllegalArgumentException("La fréquence de coupure doit être >= 0.");
        }
    }

    /** Vérifie que l'ordre du filtre de Butterworth est strictement positif. */
    private static void verifierOrdre(int ordre) {
        if (ordre <= 0) {
            throw new IllegalArgumentException("L'ordre doit être > 0.");
        }
    }

    // =========================================================
    // Outils
    // =========================================================

    /** Calcule la distance entre un point du spectre et le centre du spectre. */
    private static double distanceEuclidienne(int x, int y, int cx, int cy) {
        int dx = x - cx;
        int dy = y - cy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /** Ramène une valeur dans l'intervalle valide des niveaux de gris : [0,255]. */
    private static int clamp(int valeur) {
        if (valeur < 0) return 0;
        if (valeur > 255) return 255;
        return valeur;
    }

    // =========================================================
    // Interface fonctionnelle interne
    // =========================================================

    /**
     * Interface utilisée pour passer dynamiquement la fonction de transfert
     * du filtre fréquentiel : passe-bas idéal, passe-haut idéal, Butterworth, etc.
     */
    @FunctionalInterface
    private interface FonctionTransfert {
        double calculer(double distance, int frequenceCoupure, int ordre);
    }
}
