package ImageProcessing.Seuillage;

public class Seuillage {

    /**
     * Seuillage simple d'une image en niveaux de gris.
     *
     * Principe :
     * - on parcourt tous les pixels de l'image ;
     * - si la valeur du pixel est supérieure ou égale au seuil, on met le pixel à 255 ;
     * - sinon, on met le pixel à 0.
     *
     * Formule :
     * resultat(x,y) = 255 si image(x,y) >= seuil
     * resultat(x,y) = 0   sinon
     *
     * Effet visuel :
     * - transforme une image en niveaux de gris en image binaire ;
     * - les pixels clairs deviennent blancs ;
     * - les pixels sombres deviennent noirs.
     */
    public static int[][] seuillageSimple(int[][] image, int seuil) {

        System.out.println("Fonction seuillageSimple");

        verifierImage(image);
        verifierSeuil(seuil);

        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {

                if (image[x][y] >= seuil) {
                    resultat[x][y] = 255;
                }
                else {
                    resultat[x][y] = 0;
                }
            }
        }

        return resultat;
    }

    /**
     * Seuillage double d'une image en niveaux de gris.
     *
     * Principe :
     * - on utilise deux seuils pour séparer les pixels en trois classes ;
     * - les pixels inférieurs au premier seuil deviennent noirs ;
     * - les pixels compris entre les deux seuils prennent une valeur intermédiaire ;
     * - les pixels supérieurs ou égaux au deuxième seuil deviennent blancs.
     *
     * Formule :
     * resultat(x,y) = 0   si image(x,y) < seuil1
     * resultat(x,y) = 128 si seuil1 <= image(x,y) < seuil2
     * resultat(x,y) = 255 si image(x,y) >= seuil2
     *
     * Effet visuel :
     * - transforme l'image en trois niveaux de gris distincts ;
     * - permet de séparer les zones sombres, moyennes et claires.
     */
    public static int[][] seuillageDouble(int[][] image,int seuil1, int seuil2) {

        System.out.println("Fonction seuillageDouble");

        verifierImage(image);
        verifierSeuil(seuil1);
        verifierSeuil(seuil2);
        verifierOrdreSeuils(seuil1, seuil2);

        int largeur = image.length;
        int hauteur = image[0].length;

        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {

                int pixel = image[x][y];

                if (pixel < seuil1) {
                    resultat[x][y] = 0;
                }
                else if (pixel < seuil2) {
                    resultat[x][y] = 128;
                }
                else {
                    resultat[x][y] = 255;
                }
            }
        }

        return resultat;
    }

    /**
     * Seuillage automatique d'une image en niveaux de gris.
     *
     * Principe :
     * - on choisit un seuil initial, ici la moyenne des niveaux de gris de l'image ;
     * - on sépare les pixels en deux groupes :
     *   G1 : pixels strictement supérieurs au seuil ;
     *   G2 : pixels inférieurs ou égaux au seuil ;
     * - on calcule la moyenne de chaque groupe ;
     * - on met à jour le seuil avec la moyenne des deux moyennes ;
     * - on répète jusqu'à ce que le seuil se stabilise.
     *
     * Formule :
     * T = (moyenne(G1) + moyenne(G2)) / 2
     *
     * Effet visuel :
     * - transforme automatiquement une image en niveaux de gris en image binaire ;
     * - évite de devoir choisir manuellement le seuil.
     */
    public static int[][] seuillageAutomatique(int[][] image) {

        System.out.println("Fonction seuillageAutomatique");

        verifierImage(image);

        int largeur = image.length;
        int hauteur = image[0].length;

        // 1. Seuil initial : moyenne globale de l'image
        int ancienSeuil = -1;
        int seuil = moyenneImage(image);

        // 2. On répète tant que le seuil change
        while (seuil != ancienSeuil) {

            ancienSeuil = seuil;

            int sommeG1 = 0;
            int nbG1 = 0;

            int sommeG2 = 0;
            int nbG2 = 0;

            // Séparation des pixels en deux groupes
            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < hauteur; y++) {

                    int pixel = image[x][y];

                    if (pixel > seuil) {
                        sommeG1 += pixel;
                        nbG1++;
                    }
                    else {
                        sommeG2 += pixel;
                        nbG2++;
                    }
                }
            }

            /*
             * Cas de sécurité :
             * si un des deux groupes est vide, on ne peut pas calculer sa moyenne.
             * Dans ce cas, on arrête l'algorithme avec le seuil actuel.
             */
            if (nbG1 == 0 || nbG2 == 0) {
                break;
            }

            int moyenneG1 = sommeG1 / nbG1;
            int moyenneG2 = sommeG2 / nbG2;

            // Mise à jour du seuil selon la formule des notes
            seuil = (moyenneG1 + moyenneG2) / 2;
        }

        System.out.println("Seuil automatique trouvé : " + seuil);
        // On réutilise le seuillage simple avec le seuil trouvé automatiquement
        return seuillageSimple(image, seuil);
    }

    // =========================================================
    // Vérifications
    // =========================================================

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

    /** Vérifie que le seuil est bien compris entre 0 et 255. */
    private static void verifierSeuil(int seuil) {

        if (seuil < 0 || seuil > 255) {
            throw new IllegalArgumentException("Le seuil doit être compris entre 0 et 255.");
        }
    }

    /** Vérifie que le premier seuil est strictement inférieur au deuxième. */
    private static void verifierOrdreSeuils(int seuil1, int seuil2) {

        if (seuil1 >= seuil2) {
            throw new IllegalArgumentException("Le seuil1 doit être strictement inférieur au seuil2.");
        }
    }

    /** Calcule la moyenne des niveaux de gris de toute l'image. */
    private static int moyenneImage(int[][] image) {

        int largeur = image.length;
        int hauteur = image[0].length;

        int somme = 0;
        int nbPixels = largeur * hauteur;

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                somme += image[x][y];
            }
        }

        return somme / nbPixels;
    }
}
