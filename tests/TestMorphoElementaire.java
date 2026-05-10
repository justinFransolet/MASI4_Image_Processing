import ImageProcessing.NonLineaire.MorphoElementaire;

public class TestMorphoElementaire {

    public static void main(String[] args) {
        testErosionBinairePixelNoir();
        testErosionNiveauxDeGris();
        testErosionImageUniforme();

        testDilatationBinairePixelBlanc();
        testDilatationNiveauxDeGris();
        testDilatationImageUniforme();

        testOuverturePixelBlancIsole();
        testOuvertureObjetPlusGrand();

        testFermetureTrouNoirIsole();
        testFermetureObjetPlusGrand();

        System.out.println("\nTous les tests MorphoElementaire sont OK.");
    }

    private static void testErosionBinairePixelNoir() {
        int[][] image = {
                {255, 255, 255},
                {255, 0, 255},
                {255, 255, 255}
        };

        int[][] res = MorphoElementaire.erosion(image, 3);

        System.out.println("\n===== TEST EROSION BINAIRE PIXEL NOIR =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le noir se propage partout.");

        assertAllEquals(res, 0, "Erosion binaire pixel noir");
    }

    private static void testErosionNiveauxDeGris() {
        int[][] image = {
                {50, 60, 70},
                {80, 10, 90},
                {100, 110, 120}
        };

        int[][] res = MorphoElementaire.erosion(image, 3);

        System.out.println("\n===== TEST EROSION NIVEAUX DE GRIS =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : le centre devient le minimum du voisinage, donc 10.");

        assertEquals(10, res[1][1], "Erosion niveaux de gris centre");
    }

    private static void testErosionImageUniforme() {
        int[][] image = imageUniforme(5, 5, 80);

        int[][] res = MorphoElementaire.erosion(image, 3);

        System.out.println("\n===== TEST EROSION IMAGE UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : une image uniforme reste uniforme.");

        assertAllEquals(res, 80, "Erosion image uniforme");
    }

    private static void testDilatationBinairePixelBlanc() {
        int[][] image = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] res = MorphoElementaire.dilatation(image, 3);

        System.out.println("\n===== TEST DILATATION BINAIRE PIXEL BLANC =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le blanc se propage partout.");

        assertAllEquals(res, 255, "Dilatation binaire pixel blanc");
    }

    private static void testDilatationNiveauxDeGris() {
        int[][] image = {
                {50, 60, 70},
                {80, 200, 90},
                {100, 110, 120}
        };

        int[][] res = MorphoElementaire.dilatation(image, 3);

        System.out.println("\n===== TEST DILATATION NIVEAUX DE GRIS =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : le centre devient le maximum du voisinage, donc 200.");

        assertEquals(200, res[1][1], "Dilatation niveaux de gris centre");
    }

    private static void testDilatationImageUniforme() {
        int[][] image = imageUniforme(5, 5, 90);

        int[][] res = MorphoElementaire.dilatation(image, 3);

        System.out.println("\n===== TEST DILATATION IMAGE UNIFORME =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Attendu : une image uniforme reste uniforme.");

        assertAllEquals(res, 90, "Dilatation image uniforme");
    }

    private static void testOuverturePixelBlancIsole() {
        int[][] image = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] res = MorphoElementaire.ouverture(image, 3);

        System.out.println("\n===== TEST OUVERTURE PIXEL BLANC ISOLE =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le pixel blanc isolé disparaît.");

        assertAllEquals(res, 0, "Ouverture pixel blanc isolé");
    }

    private static void testOuvertureObjetPlusGrand() {
        int[][] image = {
                {0, 0, 0, 0, 0},
                {0, 255, 255, 255, 0},
                {0, 255, 255, 255, 0},
                {0, 255, 255, 255, 0},
                {0, 0, 0, 0, 0}
        };

        int[][] res = MorphoElementaire.ouverture(image, 3);

        System.out.println("\n===== TEST OUVERTURE OBJET 3x3 =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : un objet 3x3 ne disparaît pas complètement.");

        assertEquals(255, res[2][2], "Ouverture objet 3x3 centre");
    }

    private static void testFermetureTrouNoirIsole() {
        int[][] image = {
                {255, 255, 255},
                {255, 0, 255},
                {255, 255, 255}
        };

        int[][] res = MorphoElementaire.fermeture(image, 3);

        System.out.println("\n===== TEST FERMETURE TROU NOIR ISOLE =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le trou noir isolé est bouché.");

        assertAllEquals(res, 255, "Fermeture trou noir isolé");
    }

    private static void testFermetureObjetPlusGrand() {
        int[][] image = {
                {255, 255, 255, 255, 255},
                {255, 0, 0, 0, 255},
                {255, 0, 0, 0, 255},
                {255, 0, 0, 0, 255},
                {255, 255, 255, 255, 255}
        };

        int[][] res = MorphoElementaire.fermeture(image, 3);

        System.out.println("\n===== TEST FERMETURE TROU 3x3 =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : un grand trou 3x3 ne sera pas forcément totalement bouché par un masque 3x3.");

        assertEquals(0, res[2][2], "Fermeture grand trou centre");
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

    private static void assertAllEquals(int[][] image, int attendu, String test) {
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                if (image[x][y] != attendu) {
                    throw new RuntimeException(
                            test + " échoué en [" + x + "][" + y + "] : attendu="
                                    + attendu + ", obtenu=" + image[x][y]
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