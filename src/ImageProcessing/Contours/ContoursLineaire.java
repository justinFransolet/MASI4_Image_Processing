package ImageProcessing.Contours;

public class ContoursLineaire {

    /**
     * Gradient de Prewitt.
     *
     * Principe :
     * - on applique un masque de convolution 3x3 sur l'image ;
     * - le masque mesure les variations de niveaux de gris dans une direction ;
     * - les fortes variations correspondent généralement à des contours.
     *
     * Paramètre dir :
     * - dir = 1 : gradient horizontal, variations gauche-droite ;
     * - dir = 2 : gradient vertical, variations haut-bas.
     *
     * Effet visuel :
     * - fait ressortir les contours de l'image ;
     * - le résultat est généralement moins marqué qu'avec Sobel.
     */
    public static int[][] gradientPrewitt(int[][] image,int dir) {

        System.out.println("Fonction gradientPrewitt");

        int[][] masque;

        if (dir == 1) {
            // Gradient horizontal : variations gauche-droite
            masque = new int[][] {
                    { 1, 0, -1},
                    { 1, 0, -1},
                    { 1, 0, -1}
            };
        }
        else if (dir == 2) {
            // Gradient vertical : variations haut-bas
            masque = new int[][] {
                    { 1,  1,  1},
                    { 0,  0,  0},
                    {-1, -1, -1}
            };
        }
        else {
            System.out.println("Erreur : dir doit valoir 1 horizontal ou 2 vertical");
            return image;
        }

        return appliquerMasque3x3(image, masque);
    }

    /**
     * Gradient de Sobel.
     *
     * Principe :
     * - on applique un masque de convolution 3x3 sur l'image ;
     * - comme Prewitt, Sobel mesure les variations de niveaux de gris ;
     * - les coefficients centraux valent 2 ou -2 afin de donner plus de poids
     *   aux pixels proches du centre.
     *
     * Paramètre dir :
     * - dir = 1 : gradient horizontal, variations gauche-droite ;
     * - dir = 2 : gradient vertical, variations haut-bas.
     *
     * Effet visuel :
     * - fait ressortir les contours de l'image ;
     * - les contours sont souvent plus marqués qu'avec Prewitt.
     */
    public static int[][] gradientSobel(int[][] image,int dir) {

        System.out.println("Fonction gradientSobel");

        int[][] masque;

        if (dir == 1) {
            // Gradient horizontal : variations gauche-droite
            masque = new int[][] {
                    { 1, 0, -1},
                    { 2, 0, -2},
                    { 1, 0, -1}
            };
        }
        else if (dir == 2) {
            // Gradient vertical : variations haut-bas
            masque = new int[][] {
                    { 1,  2,  1},
                    { 0,  0,  0},
                    {-1, -2, -1}
            };
        }
        else {
            System.out.println("Erreur : dir doit valoir 1 horizontal ou 2 vertical");
            return image;
        }

        return appliquerMasque3x3(image, masque);
    }

    /**
     * Laplacien 4-voisins.
     *
     * Principe :
     * - on applique un masque de convolution 3x3 ;
     * - le pixel central est comparé à ses 4 voisins directs : haut, bas, gauche et droite ;
     * - les diagonales ne sont pas prises en compte.
     *
     * Effet visuel :
     * - détecte les contours dans plusieurs directions ;
     * - contrairement à Prewitt et Sobel, il n'y a pas de paramètre de direction.
     */
    public static int[][] laplacien4(int[][] image) {

        System.out.println("Fonction laplacien4");

        int[][] masque = {
                {0,  1, 0},
                {1, -4, 1},
                {0,  1, 0}
        };

        return appliquerMasque3x3(image, masque);
    }

    /**
     * Laplacien 8-voisins.
     *
     * Principe :
     * - on applique un masque de convolution 3x3 ;
     * - le pixel central est comparé à ses 8 voisins ;
     * - contrairement au laplacien 4, les diagonales sont aussi prises en compte.
     *
     * Effet visuel :
     * - détecte les contours dans plusieurs directions ;
     * - il est souvent plus sensible que le laplacien 4 car il utilise aussi les diagonales.
     */
    public static int[][] laplacien8(int[][] image) {

        System.out.println("Fonction laplacien8");

        int[][] masque = {
                {1,  1, 1},
                {1, -8, 1},
                {1,  1, 1}
        };

        return appliquerMasque3x3(image, masque);
    }

    /**
     * Méthode commune aux filtres linéaires de contours.
     *
     * Elle réalise toutes les étapes :
     * 1. vérification de l'image ;
     * 2. parcours de l'image en évitant les bords ;
     * 3. application du masque 3x3 autour de chaque pixel ;
     * 4. calcul de la valeur absolue du résultat ;
     * 5. limitation de la valeur finale dans l'intervalle [0,255].
     *
     * Remarque :
     * - les bords de l'image ne sont pas traités car le masque 3x3
     *   nécessite des voisins autour du pixel central.
     */
    private static int[][] appliquerMasque3x3(int[][] image, int[][] masque) {

        int hauteur = image.length;
        int largeur = image[0].length;

        int[][] resultat = new int[hauteur][largeur];

        // On évite les bords car le masque est de taille 3x3
        for (int i = 1; i < hauteur - 1; i++) {
            for (int j = 1; j < largeur - 1; j++) {

                int somme = 0;

                for (int mi = -1; mi <= 1; mi++) {
                    for (int mj = -1; mj <= 1; mj++) {

                        int pixel = image[i + mi][j + mj];
                        int coefficient = masque[mi + 1][mj + 1];

                        somme += pixel * coefficient;
                    }
                }

                int valeur = Math.abs(somme);

                if (valeur > 255) {
                    valeur = 255;
                }

                resultat[i][j] = valeur;
            }
        }

        return resultat;
    }
}