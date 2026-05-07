import ImageProcessing.Lineaire.FiltrageLineaireGlobal;

public class TestFiltrageLineaireGlobal {

    public static void main(String[] args) {
        testPasseBasIdealUniforme();
        testPasseBasIdealGradient();
        testPasseHautIdealUniforme();
        testPasseHautIdealGradient();

        testPasseBasButterworthUniformeOrdre1();
        testPasseBasButterworthUniformeOrdre3();
        testPasseHautButterworthUniformeOrdre1();
        testPasseHautButterworthUniformeOrdre3();

        System.out.println("\nTous les tests FiltrageLineaireGlobal sont OK.");
    }

    private static void testPasseBasIdealUniforme() {
        int[][] image = imageUniforme(8, 8, 100);
        int[][] res = FiltrageLineaireGlobal.filtrePasseBasIdeal(image, 2);

        System.out.println("\n===== TEST PASSE-BAS IDEAL IMAGE UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : image uniforme conservée à environ 100.");

        assertImagePresqueUniforme(res, 100, 2, "Passe-bas idéal uniforme");
        assertValeursDansIntervalle(res, "Passe-bas idéal uniforme");
    }

    private static void testPasseBasIdealGradient() {
        int[][] image = imageGradient8x8();
        int[][] res = FiltrageLineaireGlobal.filtrePasseBasIdeal(image, 2);

        System.out.println("\n===== TEST PASSE-BAS IDEAL GRADIENT =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : valeurs toujours valides entre 0 et 255, image globalement lissée.");

        assertValeursDansIntervalle(res, "Passe-bas idéal gradient");
    }

    private static void testPasseHautIdealUniforme() {
        int[][] image = imageUniforme(8, 8, 100);
        int[][] res = FiltrageLineaireGlobal.filtrePasseHautIdeal(image, 2);

        System.out.println("\n===== TEST PASSE-HAUT IDEAL IMAGE UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : image environ noire, car une image uniforme ne contient que des basses fréquences.");

        assertImagePresqueUniforme(res, 0, 2, "Passe-haut idéal uniforme");
        assertValeursDansIntervalle(res, "Passe-haut idéal uniforme");
    }

    private static void testPasseHautIdealGradient() {
        int[][] image = imageGradient8x8();
        int[][] res = FiltrageLineaireGlobal.filtrePasseHautIdeal(image, 2);

        System.out.println("\n===== TEST PASSE-HAUT IDEAL GRADIENT =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : les variations ressortent, valeurs toujours entre 0 et 255.");

        assertValeursDansIntervalle(res, "Passe-haut idéal gradient");
    }

    private static void testPasseBasButterworthUniformeOrdre1() {
        int[][] image = imageUniforme(8, 8, 120);
        int[][] res = FiltrageLineaireGlobal.filtrePasseBasButterworth(image, 2, 1);

        System.out.println("\n===== TEST PASSE-BAS BUTTERWORTH UNIFORME ORDRE 1 =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : image uniforme conservée à environ 120.");

        assertImagePresqueUniforme(res, 120, 2, "Passe-bas Butterworth uniforme ordre 1");
        assertValeursDansIntervalle(res, "Passe-bas Butterworth uniforme ordre 1");
    }

    private static void testPasseBasButterworthUniformeOrdre3() {
        int[][] image = imageUniforme(8, 8, 120);
        int[][] res = FiltrageLineaireGlobal.filtrePasseBasButterworth(image, 2, 3);

        System.out.println("\n===== TEST PASSE-BAS BUTTERWORTH UNIFORME ORDRE 3 =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : image uniforme conservée à environ 120.");

        assertImagePresqueUniforme(res, 120, 2, "Passe-bas Butterworth uniforme ordre 3");
        assertValeursDansIntervalle(res, "Passe-bas Butterworth uniforme ordre 3");
    }

    private static void testPasseHautButterworthUniformeOrdre1() {
        int[][] image = imageUniforme(8, 8, 120);
        int[][] res = FiltrageLineaireGlobal.filtrePasseHautButterworth(image, 2, 1);

        System.out.println("\n===== TEST PASSE-HAUT BUTTERWORTH UNIFORME ORDRE 1 =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : image environ noire.");

        assertImagePresqueUniforme(res, 0, 2, "Passe-haut Butterworth uniforme ordre 1");
        assertValeursDansIntervalle(res, "Passe-haut Butterworth uniforme ordre 1");
    }

    private static void testPasseHautButterworthUniformeOrdre3() {
        int[][] image = imageUniforme(8, 8, 120);
        int[][] res = FiltrageLineaireGlobal.filtrePasseHautButterworth(image, 2, 3);

        System.out.println("\n===== TEST PASSE-HAUT BUTTERWORTH UNIFORME ORDRE 3 =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : image environ noire.");

        assertImagePresqueUniforme(res, 0, 2, "Passe-haut Butterworth uniforme ordre 3");
        assertValeursDansIntervalle(res, "Passe-haut Butterworth uniforme ordre 3");
    }

    private static int[][] imageUniforme(int largeur, int hauteur, int valeur) {
        int[][] image = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                image[x][y] = valeur;
            }
        }

        return image;
    }

    private static int[][] imageGradient8x8() {
        int[][] image = new int[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                image[x][y] = x * 30;
            }
        }

        return image;
    }

    private static void assertImagePresqueUniforme(int[][] image, int attendu, int tolerance, String test) {
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                if (Math.abs(image[x][y] - attendu) > tolerance) {
                    throw new RuntimeException(
                            test + " échoué en [" + x + "][" + y + "] : attendu≈"
                                    + attendu + ", obtenu=" + image[x][y]
                    );
                }
            }
        }
    }

    private static void assertValeursDansIntervalle(int[][] image, String test) {
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                if (image[x][y] < 0 || image[x][y] > 255) {
                    throw new RuntimeException(
                            test + " échoué : valeur hors [0,255] en [" + x + "][" + y + "] = " + image[x][y]
                    );
                }
            }
        }
    }

    private static void afficherMatrice(String titre, int[][] matrice) {
        System.out.println("\n" + titre + " :");
        for (int y = 0; y < matrice[0].length; y++) {
            for (int x = 0; x < matrice.length; x++) {
                System.out.printf("%5d", matrice[x][y]);
            }
            System.out.println();
        }
    }
}