package ImageProcessing.Lineaire;

public class FiltrageLineaireLocal {

    public static int [][] filtreMasqueConvolution(int [][] image, double [][] masque) {

        System.out.println("Fonction filtreMasqueConvolution");

        // Vérifications de base
        if (image == null || image.length == 0 || image[0].length == 0)
        {
            throw new IllegalArgumentException("Image invalide.");
        }

        if (masque == null || masque.length == 0 || masque[0].length == 0)
        {
            throw new IllegalArgumentException("Masque invalide.");
        }

        int tailleMasque = masque.length;

        if (tailleMasque != masque[0].length)
        {
            throw new IllegalArgumentException("Le masque doit être carré.");
        }

        if (tailleMasque % 2 == 0)
        {
            throw new IllegalArgumentException("La taille du masque doit être impaire.");
        }

        int largeur = image.length;
        int hauteur = image[0].length;

        // Vérifie que l'image est bien rectangulaire
        for (int x = 0; x < largeur; x++)
        {
            if (image[x] == null || image[x].length != hauteur)
            {
                throw new IllegalArgumentException("L'image n'est pas rectangulaire.");
            }
        }

        // Vérifie que le masque est bien rectangulaire
        for (int i = 0; i < tailleMasque; i++)
        {
            if (masque[i] == null || masque[i].length != tailleMasque)
            {
                throw new IllegalArgumentException("Le masque n'est pas carré.");
            }
        }

        int[][] resultat = new int[largeur][hauteur];

        int rayon = tailleMasque / 2;

        // Stratégie simple pour les bords :
        // on recopie d'abord l'image originale
        for (int x = 0; x < largeur; x++)
        {
            System.arraycopy(image[x], 0, resultat[x], 0, hauteur);
        }

        // Convolution uniquement sur les pixels intérieurs
        for (int x = rayon; x < largeur - rayon; x++)
        {
            for (int y = rayon; y < hauteur - rayon; y++)
            {
                double somme = 0.0;

                for (int i = -rayon; i <= rayon; i++)
                {
                    for (int j = -rayon; j <= rayon; j++)
                    {
                        int pixel = image[x + i][y + j];
                        double coeff = masque[i + rayon][j + rayon];
                        somme += pixel * coeff;
                    }
                }

                int valeur = (int) Math.round(somme);
                resultat[x][y] = clamp(valeur);
            }
        }

        return resultat;
    }

    public static int [][] filtreMoyenneur(int [][] image, int tailleMasque) {

        System.out.println("Fonction filtreMoyenneur");

        if (tailleMasque <= 0)
        {
            throw new IllegalArgumentException("La taille du masque doit être > 0.");
        }

        if (tailleMasque % 2 == 0)
        {
            throw new IllegalArgumentException("La taille du masque doit être impaire.");
        }

        double[][] masque = new double[tailleMasque][tailleMasque];
        double valeur = 1.0 / (tailleMasque * tailleMasque);

        for (int i = 0; i < tailleMasque; i++)
        {
            for (int j = 0; j < tailleMasque; j++)
            {
                masque[i][j] = valeur;
            }
        }

        return filtreMasqueConvolution(image, masque);
    }


    private static int clamp(int valeur)
    {
        if (valeur < 0) return 0;
        if (valeur > 255) return 255;
        return valeur;
    }
}
