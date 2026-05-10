package ImageProcessing.Contours;

import ImageProcessing.NonLineaire.MorphoElementaire;

public class ContoursNonLineaire {

    /**
     * Gradient d'érosion.
     *
     * Principe :
     * - on calcule d'abord l'érosion de l'image ;
     * - l'érosion remplace chaque pixel par le minimum de son voisinage 3x3 ;
     * - on soustrait ensuite l'image érodée à l'image originale.
     *
     * Formule :
     * gradientErosion = image - erosion(image)
     *
     * Effet visuel :
     * - fait ressortir certains contours de l'image ;
     * - les contours correspondent aux zones où l'érosion modifie fortement les niveaux de gris.
     */
    public static int[][] gradientErosion(int[][] image) {

        System.out.println("Fonction gradientErosion");

        int hauteur = image.length;
        int largeur = image[0].length;

        int[][] erosion = MorphoElementaire.erosion(image, 3);
        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {

                int valeur = image[i][j] - erosion[i][j];

                resultat[i][j] = clamp(valeur);
            }
        }

        return resultat;
    }

    /**
     * Gradient de dilatation.
     *
     * Principe :
     * - on calcule d'abord la dilatation de l'image ;
     * - la dilatation remplace chaque pixel par le maximum de son voisinage ;
     * - on soustrait ensuite l'image originale à l'image dilatée.
     *
     * Formule :
     * gradientDilatation = dilatation(image) - image
     *
     * Effet visuel :
     * - fait ressortir certains contours de l'image ;
     * - les contours correspondent aux zones où la dilatation modifie fortement les niveaux de gris.
     */
    public static int[][] gradientDilatation(int[][] image) {

        System.out.println("Fonction gradientDilatation");

        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] dilatation = MorphoElementaire.dilatation(image, 3);
        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {

                int valeur = dilatation[x][y] - image[x][y];

                resultat[x][y] = clamp(valeur);
            }
        }

        return resultat;
    }

    /**
     * Gradient de Beucher.
     *
     * Principe :
     * - on calcule d'abord la dilatation de l'image ;
     * - on calcule ensuite l'érosion de l'image ;
     * - on soustrait l'image érodée à l'image dilatée.
     *
     * Formule :
     * gradientBeucher = dilatation(image) - erosion(image)
     *
     * Effet visuel :
     * - fait ressortir les contours de l'image ;
     * - donne souvent des contours plus complets car il tient compte à la fois de l'érosion et de la dilatation.
     */
    public static int[][] gradientBeucher(int[][] image) {

        System.out.println("Fonction gradientBeucher");

        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] dilatation = MorphoElementaire.dilatation(image, 3);
        int[][] erosion = MorphoElementaire.erosion(image, 3);

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {

                int valeur = dilatation[x][y] - erosion[x][y];

                resultat[x][y] = clamp(valeur);
            }
        }

        return resultat;
    }

    /**
     * Laplacien non-linéaire.
     *
     * Principe :
     * - on calcule le gradient de dilatation de l'image ;
     * - on calcule le gradient d'érosion de l'image ;
     * - on soustrait ensuite le gradient d'érosion au gradient de dilatation.
     *
     * Formule :
     * laplacienNonLineaire = GD(image) - GE(image)
     *
     * avec :
     * GE(image) = image - erosion(image)
     * GD(image) = dilatation(image) - image
     *
     * Effet visuel :
     * - permet de faire ressortir les contours de manière non-linéaire ;
     * - le résultat dépend de la différence entre les transitions obtenues par dilatation et par érosion.
     */
    public static int[][] laplacienNonLineaire(int[][] image) {

        System.out.println("Fonction laplacienNonLineaire");

        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] gradientDilatation = gradientDilatation(image);
        int[][] gradientErosion = gradientErosion(image);

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {

                int valeur = gradientDilatation[x][y] - gradientErosion[x][y];

                resultat[x][y] = clamp(Math.abs(valeur));
            }
        }

        return resultat;
    }

    /** Ramène une valeur dans l'intervalle valide des niveaux de gris : [0,255]. */
    private static int clamp(int valeur) {

        if (valeur < 0) {
            return 0;
        }

        if (valeur > 255) {
            return 255;
        }

        return valeur;
    }
}
