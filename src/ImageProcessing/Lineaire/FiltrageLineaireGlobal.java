package ImageProcessing.Lineaire;

import ImageProcessing.Complexe.Complexe;
import ImageProcessing.Complexe.MatriceComplexe;
import ImageProcessing.Fourier.Fourier;

public class FiltrageLineaireGlobal {

    public static int [][] filtrePasseBasIdeal(int [][] image, int frequenceCoupure) {

        System.out.println("Fonction filtrePasseBasIdeal");
        verifierImage(image);
        verifierFrequenceCoupure(frequenceCoupure);

        return appliquerFiltrageFrequentiel(image, (distance, fc, ordre) -> {
            return (distance <= fc) ? 1.0 : 0.0;
        }, frequenceCoupure, 1);
    }

    public static int [][] filtrePasseHautIdeal(int [][] image, int frequenceCoupure) {

        System.out.println("Fonction filtrePasseHautIdeal");
        verifierImage(image);
        verifierFrequenceCoupure(frequenceCoupure);

        return appliquerFiltrageFrequentiel(image, (distance, fc, ordre) -> {
            return (distance <= fc) ? 0.0 : 1.0;
        }, frequenceCoupure, 1);
    }

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

    private static void verifierFrequenceCoupure(int frequenceCoupure) {
        if (frequenceCoupure < 0) {
            throw new IllegalArgumentException("La fréquence de coupure doit être >= 0.");
        }
    }

    private static void verifierOrdre(int ordre) {
        if (ordre <= 0) {
            throw new IllegalArgumentException("L'ordre doit être > 0.");
        }
    }

    // =========================================================
    // Outils
    // =========================================================

    private static double distanceEuclidienne(int x, int y, int cx, int cy) {
        int dx = x - cx;
        int dy = y - cy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static int clamp(int valeur) {
        if (valeur < 0) return 0;
        if (valeur > 255) return 255;
        return valeur;
    }

    // =========================================================
    // Interface fonctionnelle interne
    // =========================================================

    @FunctionalInterface
    private interface FonctionTransfert {
        double calculer(double distance, int frequenceCoupure, int ordre);
    }
}
