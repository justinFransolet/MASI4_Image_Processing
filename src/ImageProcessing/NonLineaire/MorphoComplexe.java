package ImageProcessing.NonLineaire;

import java.util.Arrays;

public class MorphoComplexe {

    public static int[][] dilatationGeodesique(int[][] image,int[][] masqueGeodesique, int nbIter) {

        System.out.println("Fonction dilatationGeodesique");

        verifierImage(image);
        verifierImage(masqueGeodesique);
        verifierMemeTaille(image, masqueGeodesique);

        if (nbIter < 1) {
            throw new IllegalArgumentException("Le nombre d'itérations doit être >= 1.");
        }

        int[][] resultat = copie(image);

        for (int k = 0; k < nbIter; k++) {
            int[][] dilatee = MorphoElementaire.dilatation(resultat, 3);
            resultat = minimumPixelParPixel(dilatee, masqueGeodesique);
        }

        return resultat;
    }

    public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique) {

        System.out.println("Fonction reconstructionGeodesique");

        verifierImage(image);
        verifierImage(masqueGeodesique);
        verifierMemeTaille(image, masqueGeodesique);

        int[][] ancienne;
        int[][] courante = copie(image);

        do {
            ancienne = copie(courante);
            courante = dilatationGeodesique(courante, masqueGeodesique, 1);
        }
        while (!matricesEgales(ancienne, courante));

        return courante;
    }

    public static int[][] filtreMedian(int[][] image, int tailleMasque) {

        System.out.println("Fonction filtreMedian");

        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        int largeur = image.length;
        int hauteur = image[0].length;
        int rayon = tailleMasque / 2;

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {

                int[] valeurs = new int[tailleMasque * tailleMasque];
                int compteur = 0;

                for (int i = -rayon; i <= rayon; i++) {
                    for (int j = -rayon; j <= rayon; j++) {
                        int xx = x + i;
                        int yy = y + j;

                        if (xx >= 0 && xx < largeur && yy >= 0 && yy < hauteur) {
                            valeurs[compteur] = image[xx][yy];
                            compteur++;
                        }
                    }
                }

                int[] valeursUtiles = Arrays.copyOf(valeurs, compteur);
                Arrays.sort(valeursUtiles);

                resultat[x][y] = valeursUtiles[valeursUtiles.length / 2];
            }
        }

        return resultat;
    }

    private static int[][] minimumPixelParPixel(int[][] image1, int[][] image2) {
        int largeur = image1.length;
        int hauteur = image1[0].length;

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                resultat[x][y] = Math.min(image1[x][y], image2[x][y]);
            }
        }

        return resultat;
    }

    private static int[][] copie(int[][] image) {
        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] copie = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            System.arraycopy(image[x], 0, copie[x], 0, hauteur);
        }

        return copie;
    }

    private static boolean matricesEgales(int[][] image1, int[][] image2) {
        for (int x = 0; x < image1.length; x++) {
            for (int y = 0; y < image1[0].length; y++) {
                if (image1[x][y] != image2[x][y]) {
                    return false;
                }
            }
        }

        return true;
    }

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

    private static void verifierMemeTaille(int[][] image1, int[][] image2) {
        if (image1.length != image2.length || image1[0].length != image2[0].length) {
            throw new IllegalArgumentException("L'image et le masque géodésique doivent avoir la même taille.");
        }
    }

    private static void verifierTailleMasque(int tailleMasque) {
        if (tailleMasque <= 0) {
            throw new IllegalArgumentException("La taille du masque doit être > 0.");
        }

        if (tailleMasque % 2 == 0) {
            throw new IllegalArgumentException("La taille du masque doit être impaire.");
        }
    }
}
