package ImageProcessing.NonLineaire;

public class MorphoElementaire {

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

    public static int[][] ouverture(int [][] image,int tailleMasque) {

        System.out.println("Fonction ouverture");
        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        return dilatation(erosion(image, tailleMasque), tailleMasque);
    }

    public static int[][] fermeture(int [][] image,int tailleMasque) {

        System.out.println("Fonction fermeture");
        verifierImage(image);
        verifierTailleMasque(tailleMasque);

        return erosion(dilatation(image, tailleMasque), tailleMasque);
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

    private static void verifierTailleMasque(int tailleMasque) {
        if (tailleMasque <= 0) {
            throw new IllegalArgumentException("La taille du masque doit être > 0.");
        }

        if (tailleMasque % 2 == 0) {
            throw new IllegalArgumentException("La taille du masque doit être impaire.");
        }
    }
}
