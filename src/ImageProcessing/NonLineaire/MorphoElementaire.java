package ImageProcessing.NonLineaire;

public class MorphoElementaire {

    /**
     * Réalise une érosion morphologique.
     *
     * Principe :
     * - on parcourt le voisinage carré autour de chaque pixel ;
     * - on remplace le pixel courant par la valeur minimale du voisinage.
     *
     * Effet :
     * - sur une image binaire : les zones blanches rétrécissent ;
     * - sur une image en niveaux de gris : les zones claires sont atténuées.
     */
    public static int[][] erosion(int [][] image,int tailleMasque) {

        System.out.println("Fonction erosion");
        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        int largeur = image.length;
        int hauteur = image[0].length;
        int rayon = tailleMasque / 2;

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                int min = 255;

                for (int i = -rayon; i <= rayon; i++) {
                    for (int j = -rayon; j <= rayon; j++) {
                        int xx = x + i;
                        int yy = y + j;

                        if (xx >= 0 && xx < largeur && yy >= 0 && yy < hauteur) {
                            if (image[xx][yy] < min) {
                                min = image[xx][yy];
                            }
                        }
                    }
                }

                resultat[x][y] = min;
            }
        }

        return resultat;
    }

    /**
     * Réalise une dilatation morphologique.
     *
     * Principe :
     * - on parcourt le voisinage carré autour de chaque pixel ;
     * - on remplace le pixel courant par la valeur maximale du voisinage.
     *
     * Effet :
     * - sur une image binaire : les zones blanches s'agrandissent ;
     * - sur une image en niveaux de gris : les zones claires se propagent.
     */
    public static int[][] dilatation(int [][] image,int tailleMasque) {

        System.out.println("Fonction dilatation");
        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        int largeur = image.length;
        int hauteur = image[0].length;
        int rayon = tailleMasque / 2;

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                int max = 0;

                for (int i = -rayon; i <= rayon; i++) {
                    for (int j = -rayon; j <= rayon; j++) {
                        int xx = x + i;
                        int yy = y + j;

                        if (xx >= 0 && xx < largeur && yy >= 0 && yy < hauteur) {
                            if (image[xx][yy] > max) {
                                max = image[xx][yy];
                            }
                        }
                    }
                }

                resultat[x][y] = max;
            }
        }

        return resultat;
    }

    /**
     * Réalise une ouverture morphologique.
     *
     * Principe :
     * - ouverture = érosion suivie d'une dilatation.
     *
     * Effet :
     * - supprime les petits objets clairs ;
     * - lisse les contours des objets clairs ;
     * - utile pour enlever de petits bruits blancs.
     */
    public static int[][] ouverture(int [][] image,int tailleMasque) {

        System.out.println("Fonction ouverture");
        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        return dilatation(erosion(image, tailleMasque), tailleMasque);
    }

    /**
     * Réalise une fermeture morphologique.
     *
     * Principe :
     * - fermeture = dilatation suivie d'une érosion.
     *
     * Effet :
     * - bouche les petits trous sombres ;
     * - relie des objets clairs proches ;
     * - utile pour compléter des formes.
     */
    public static int[][] fermeture(int [][] image,int tailleMasque) {

        System.out.println("Fonction fermeture");
        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        return erosion(dilatation(image, tailleMasque), tailleMasque);
    }

    /**
     * Vérifie que l'image est utilisable :
     * - elle ne doit pas être null ;
     * - elle doit avoir une largeur et une hauteur ;
     * - toutes ses colonnes doivent avoir la même hauteur.
     */
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

    /**
     * Vérifie que la taille de l'élément structurant est correcte.
     *
     * Dans ce projet, l'élément structurant est carré nxn
     * et n doit être impair : 3, 5, 7, ...
     */
    private static void verifierTailleMasque(int tailleMasque) {
        if (tailleMasque <= 0) {
            throw new IllegalArgumentException("La taille du masque doit être > 0.");
        }

        if (tailleMasque % 2 == 0) {
            throw new IllegalArgumentException("La taille du masque doit être impaire.");
        }
    }
}
