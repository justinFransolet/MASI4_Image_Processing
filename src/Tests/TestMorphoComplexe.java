package Tests;

import ImageProcessing.NonLineaire.MorphoComplexe;

public class TestMorphoComplexe {

    public static void main(String[] args) {
        testFiltreMedianPixelNoirIsole();
        testFiltreMedianPixelBlancIsole();
        testFiltreMedianNiveauxDeGris();

        testDilatationGeodesiqueUneIteration();
        testDilatationGeodesiqueDeuxIterations();

        testReconstructionGeodesiqueMasquePlein();
        testReconstructionGeodesiqueMasqueCroix();

        System.out.println("\nTous les tests MorphoComplexe sont OK.");
    }

    private static void testFiltreMedianPixelNoirIsole() {
        int[][] image = {
                {255, 255, 255},
                {255, 0, 255},
                {255, 255, 255}
        };

        int[][] res = MorphoComplexe.filtreMedian(image, 3);

        System.out.println("\n===== TEST FILTRE MEDIAN PIXEL NOIR ISOLE =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le pixel noir isolé devient blanc.");

        assertEquals(255, res[1][1], "Filtre médian pixel noir isolé");
    }

    private static void testFiltreMedianPixelBlancIsole() {
        int[][] image = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] res = MorphoComplexe.filtreMedian(image, 3);

        System.out.println("\n===== TEST FILTRE MEDIAN PIXEL BLANC ISOLE =====");
        afficherMatrice("Image de départ", image);
        afficherBinaire("Image visuelle", image);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le pixel blanc isolé devient noir.");

        assertEquals(0, res[1][1], "Filtre médian pixel blanc isolé");
    }

    private static void testFiltreMedianNiveauxDeGris() {
        int[][] image = {
                {10, 20, 30},
                {40, 200, 60},
                {70, 80, 90}
        };

        int[][] res = MorphoComplexe.filtreMedian(image, 3);

        System.out.println("\n===== TEST FILTRE MEDIAN NIVEAUX DE GRIS =====");
        afficherMatrice("Image de départ", image);
        afficherMatrice("Résultat obtenu", res);
        System.out.println("Valeurs triées : 10,20,30,40,60,70,80,90,200. Médiane = 60.");

        assertEquals(60, res[1][1], "Filtre médian niveaux de gris");
    }

    private static void testDilatationGeodesiqueUneIteration() {
        int[][] marqueur = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] masque = {
                {0, 255, 0},
                {255, 255, 255},
                {0, 255, 0}
        };

        int[][] res = MorphoComplexe.dilatationGeodesique(marqueur, masque, 1);

        System.out.println("\n===== TEST DILATATION GEODESIQUE 1 ITERATION =====");
        afficherMatrice("Marqueur", marqueur);
        afficherBinaire("Marqueur visuel", marqueur);
        afficherMatrice("Masque", masque);
        afficherBinaire("Masque visuel", masque);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : la dilatation est limitée par le masque en croix.");

        assertEquals(0, res[0][0], "Dilatation géodésique 1 it coin");
        assertEquals(255, res[0][1], "Dilatation géodésique 1 it gauche");
        assertEquals(255, res[1][0], "Dilatation géodésique 1 it haut");
        assertEquals(255, res[1][1], "Dilatation géodésique 1 it centre");
        assertEquals(0, res[2][2], "Dilatation géodésique 1 it coin bas droit");
    }

    private static void testDilatationGeodesiqueDeuxIterations() {
        int[][] marqueur = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 255, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        int[][] masque = {
                {0, 0, 255, 0, 0},
                {0, 0, 255, 0, 0},
                {255, 255, 255, 255, 255},
                {0, 0, 255, 0, 0},
                {0, 0, 255, 0, 0}
        };

        int[][] res = MorphoComplexe.dilatationGeodesique(marqueur, masque, 2);

        System.out.println("\n===== TEST DILATATION GEODESIQUE 2 ITERATIONS =====");
        afficherMatrice("Marqueur", marqueur);
        afficherBinaire("Marqueur visuel", marqueur);
        afficherMatrice("Masque", masque);
        afficherBinaire("Masque visuel", masque);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : la région grandit deux fois, mais reste dans le masque.");

        assertEquals(255, res[2][0], "Dilatation géodésique 2 it haut");
        assertEquals(255, res[2][4], "Dilatation géodésique 2 it bas");
        assertEquals(255, res[0][2], "Dilatation géodésique 2 it gauche");
        assertEquals(255, res[4][2], "Dilatation géodésique 2 it droite");
        assertEquals(0, res[0][0], "Dilatation géodésique 2 it coin");
    }

    private static void testReconstructionGeodesiqueMasquePlein() {
        int[][] marqueur = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] masque = {
                {255, 255, 255},
                {255, 255, 255},
                {255, 255, 255}
        };

        int[][] res = MorphoComplexe.reconstructionGeodesique(marqueur, masque);

        System.out.println("\n===== TEST RECONSTRUCTION GEODESIQUE MASQUE PLEIN =====");
        afficherMatrice("Marqueur", marqueur);
        afficherBinaire("Marqueur visuel", marqueur);
        afficherMatrice("Masque", masque);
        afficherBinaire("Masque visuel", masque);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le marqueur remplit tout le masque.");

        assertAllEquals(res, 255, "Reconstruction masque plein");
    }

    private static void testReconstructionGeodesiqueMasqueCroix() {
        int[][] marqueur = {
                {0, 0, 0},
                {0, 255, 0},
                {0, 0, 0}
        };

        int[][] masque = {
                {0, 255, 0},
                {255, 255, 255},
                {0, 255, 0}
        };

        int[][] res = MorphoComplexe.reconstructionGeodesique(marqueur, masque);

        System.out.println("\n===== TEST RECONSTRUCTION GEODESIQUE MASQUE CROIX =====");
        afficherMatrice("Marqueur", marqueur);
        afficherBinaire("Marqueur visuel", marqueur);
        afficherMatrice("Masque", masque);
        afficherBinaire("Masque visuel", masque);
        afficherMatrice("Résultat obtenu", res);
        afficherBinaire("Résultat visuel", res);
        System.out.println("Attendu : le résultat final doit être exactement la croix du masque.");

        assertMatriceEquals(masque, res, "Reconstruction masque croix");
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

    private static void assertMatriceEquals(int[][] attendu, int[][] obtenu, String test) {
        for (int x = 0; x < attendu.length; x++) {
            for (int y = 0; y < attendu[0].length; y++) {
                if (attendu[x][y] != obtenu[x][y]) {
                    throw new RuntimeException(
                            test + " échoué en [" + x + "][" + y + "] : attendu="
                                    + attendu[x][y] + ", obtenu=" + obtenu[x][y]
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