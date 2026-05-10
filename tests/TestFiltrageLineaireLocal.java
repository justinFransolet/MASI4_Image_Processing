import ImageProcessing.Lineaire.FiltrageLineaireLocal;

public class TestFiltrageLineaireLocal {

    public static void main(String[] args) {
        testMasqueIdentite();
        testMasqueMultiplicationCentre();
        testMasqueRenforcementCentre();
        testMoyenneur3x3PixelIsole();
        testMoyenneur3x3ImageUniforme();
        testMoyenneur5x5ImageUniforme();
        testMoyenneur3x3NonUniforme();

        System.out.println("\nTous les tests FiltrageLineaireLocal sont OK.");
    }

    private static void testMasqueIdentite() {
        int[][] image = {
                {10, 20, 30},
                {40, 50, 60},
                {70, 80, 90}
        };

        double[][] masque = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        int[][] res = FiltrageLineaireLocal.filtreMasqueConvolution(image, masque);

        System.out.println("\n===== TEST MASQUE IDENTITE =====");
        afficherMatrice("Image de départ", image);
        afficherMasque("Masque", masque);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : le centre reste 50.");

        assertEquals(50, res[1][1], "Masque identité");
    }

    private static void testMasqueMultiplicationCentre() {
        int[][] image = {
                {10, 20, 30},
                {40, 50, 60},
                {70, 80, 90}
        };

        double[][] masque = {
                {0, 0, 0},
                {0, 3, 0},
                {0, 0, 0}
        };

        int[][] res = FiltrageLineaireLocal.filtreMasqueConvolution(image, masque);

        System.out.println("\n===== TEST MASQUE CENTRE x3 =====");
        afficherMatrice("Image de départ", image);
        afficherMasque("Masque", masque);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : le centre devient 50 * 3 = 150.");

        assertEquals(150, res[1][1], "Masque centre x3");
    }

    private static void testMasqueRenforcementCentre() {
        int[][] image = {
                {10, 10, 10},
                {10, 50, 10},
                {10, 10, 10}
        };

        double[][] masque = {
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}
        };

        int[][] res = FiltrageLineaireLocal.filtreMasqueConvolution(image, masque);

        System.out.println("\n===== TEST MASQUE RENFORCEMENT =====");
        afficherMatrice("Image de départ", image);
        afficherMasque("Masque", masque);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Calcul attendu au centre : 5*50 - 4*10 = 210.");

        assertEquals(210, res[1][1], "Masque renforcement");
    }

    private static void testMoyenneur3x3PixelIsole() {
        int[][] image = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] res = FiltrageLineaireLocal.filtreMoyenneur(image, 3);

        System.out.println("\n===== TEST MOYENNEUR 3x3 PIXEL ISOLE =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : centre = 255 / 9 = 28.");

        assertEquals(28, res[1][1], "Moyenneur 3x3 pixel isolé");
    }

    private static void testMoyenneur3x3ImageUniforme() {
        int[][] image = imageUniforme(5, 5, 100);

        int[][] res = FiltrageLineaireLocal.filtreMoyenneur(image, 3);

        System.out.println("\n===== TEST MOYENNEUR 3x3 IMAGE UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : les pixels intérieurs restent à 100.");

        assertEquals(100, res[2][2], "Moyenneur 3x3 image uniforme");
    }

    private static void testMoyenneur5x5ImageUniforme() {
        int[][] image = imageUniforme(7, 7, 80);

        int[][] res = FiltrageLineaireLocal.filtreMoyenneur(image, 5);

        System.out.println("\n===== TEST MOYENNEUR 5x5 IMAGE UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : le centre reste à 80.");

        assertEquals(80, res[3][3], "Moyenneur 5x5 image uniforme");
    }

    private static void testMoyenneur3x3NonUniforme() {
        int[][] image = {
                {10, 20, 30},
                {40, 50, 60},
                {70, 80, 90}
        };

        int[][] res = FiltrageLineaireLocal.filtreMoyenneur(image, 3);

        System.out.println("\n===== TEST MOYENNEUR 3x3 NON UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu au centre : (10+20+30+40+50+60+70+80+90)/9 = 50.");

        assertEquals(50, res[1][1], "Moyenneur 3x3 non uniforme");
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

    private static void assertEquals(int attendu, int obtenu, String test) {
        if (attendu != obtenu) {
            throw new RuntimeException(test + " échoué : attendu=" + attendu + ", obtenu=" + obtenu);
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

    private static void afficherMasque(String titre, double[][] masque) {
        System.out.println("\n" + titre + " :");
        for (int y = 0; y < masque[0].length; y++) {
            for (int x = 0; x < masque.length; x++) {
                System.out.printf("%8.2f", masque[x][y]);
            }
            System.out.println();
        }
    }

    private static void afficherBinaire(String titre, int[][] matrice) {
        System.out.println("\n" + titre + " :");
        for (int y = 0; y < matrice[0].length; y++) {
            for (int x = 0; x < matrice.length; x++) {
                System.out.print(matrice[x][y] == 255 ? "█ " : ". ");
            }
            System.out.println();
        }
    }
}