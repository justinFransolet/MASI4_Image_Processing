package ImageProcessing.NonLineaire;

import java.util.Arrays;

public class MorphoComplexe {

    /**
     * Réalise une dilatation géodésique.
     *
     * Principe :
     * - on dilate l'image de départ ;
     * - puis on limite le résultat par le masque géodésique ;
     * - cette limitation se fait pixel par pixel avec un minimum :
     *   resultat = min(dilatation(image), masqueGeodesique)
     *
     * nbIter indique combien de fois on répète cette opération.
     *
     * Utilisation typique :
     * - surtout sur des images binaires ;
     * - permet de faire grandir une région sans dépasser un masque imposé.
     */
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

    /**
     * Réalise une reconstruction géodésique.
     *
     * Principe :
     * - on applique des dilatations géodésiques successives ;
     * - on s'arrête quand l'image ne change plus.
     *
     * Autrement dit :
     * - on part d'un marqueur ;
     * - on le fait grandir dans les limites du masque géodésique ;
     * - la croissance s'arrête naturellement quand la stabilité est atteinte.
     */
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

    /**
     * Réalise un filtre médian.
     *
     * Principe :
     * - pour chaque pixel, on récupère les valeurs du voisinage ;
     * - on trie ces valeurs ;
     * - on remplace le pixel par la valeur médiane.
     *
     * Effet :
     * - très utile pour supprimer le bruit impulsionnel,
     *   par exemple le bruit "sel et poivre" ;
     * - conserve souvent mieux les contours qu'un filtre moyenneur.
     */
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

    /**
     * Calcule le minimum pixel par pixel entre deux images.
     *
     * Utilisé pour la dilatation géodésique :
     * le masque géodésique impose une limite à ne pas dépasser.
     */
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

    /**
     * Crée une copie complète d'une image.
     *
     * Important :
     * - cela évite de modifier directement la matrice reçue en paramètre.
     */
    private static int[][] copie(int[][] image) {
        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] copie = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            System.arraycopy(image[x], 0, copie[x], 0, hauteur);
        }

        return copie;
    }

    /**
     * Compare deux images pixel par pixel.
     *
     * Utilisé pour savoir quand la reconstruction géodésique est stabilisée.
     */
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
     * Vérifie que deux images ont exactement les mêmes dimensions.
     *
     * Nécessaire pour les opérations géodésiques,
     * car on combine l'image et le masque pixel par pixel.
     */
    private static void verifierMemeTaille(int[][] image1, int[][] image2) {
        if (image1.length != image2.length || image1[0].length != image2[0].length) {
            throw new IllegalArgumentException("L'image et le masque géodésique doivent avoir la même taille.");
        }
    }

    /**
     * Vérifie que la taille du masque est valide.
     *
     * Le masque doit être :
     * - de taille strictement positive ;
     * - impair, pour avoir un pixel central.
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
