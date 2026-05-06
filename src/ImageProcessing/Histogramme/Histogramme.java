package ImageProcessing.Histogramme;

public class Histogramme 
{
    public static int[] Histogramme256(int[][] mat)
    {
        int[] histo = new int[256];

        if (mat == null) return histo;

        for (int[] row : mat)
        {
            if (row == null) continue;
            for (int value : row)
                if ((value >= 0) && (value <= 255)) histo[value]++;
        }

        return histo;
    }

    public static int minimum(int[][] image) {

        System.out.println("Fonction minimum");

        return extremum(image, true);
    }

    public static int maximum(int[][] image) {

        System.out.println("Fonction maximum");

        return extremum(image, false);
    }

    public static int luminance(int[][] image) {

        System.out.println("Fonction luminance");

        return (int) Math.round(averagePixelValue(image));
    }

    public static double contraste1(int[][] image) {

        System.out.println("Fonction contraste1");

        if (image == null || image.length == 0) return 0;
        return maximum(image) - minimum(image);
    }

    public static double contraste2(int[][] image) {

        System.out.println("Fonction contraste2");

        double moyenne = averagePixelValue(image);
        double variance = 0;

        for (int[] row : image)
        {
            if (row == null) continue;
            for (int value : row)
            {
                double ecart = value - moyenne;
                variance += ecart * ecart;
            }
        }

        variance /= countPixel(image);
        return Math.sqrt(variance);
    }

    public static int[][] rehaussement(int[][] image, int[] courbeTonale) {

        System.out.println("Fonction rehaussement");

        if (image == null || courbeTonale == null || courbeTonale.length != 256 || image.length == 0)
            return null;

        int largeur = image.length;
        int hauteur = image[0].length;
        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++)
        {
            if (image[x] == null) continue;
            for (int y = 0; y < hauteur; y++)
            {
                int valeur = image[x][y];
                if (valeur < 0) valeur = 0;
                else if (valeur > 255) valeur = 255;
                resultat[x][y] = limiterNiveauGris(courbeTonale[valeur]);
            }
        }

        return resultat;
    }

    public static int[] creeCourbeTonaleLineaireSaturation(int smin, int smax) {

        System.out.println("Fonction creeCourbeTonaleLineaireSaturation");

        int[] courbe = new int[256];

        if (smin > smax)
        {
            int tmp = smin;
            smin = smax;
            smax = tmp;
        }

        smin = Math.max(0, Math.min(255, smin));
        smax = Math.max(0, Math.min(255, smax));

        if (smin == smax)
        {
            for (int i = 0; i < 256; i++) courbe[i] = (i <= smin) ? 0 : 255;
            return courbe;
        }

        for (int i = 0; i < 256; i++)
        {
            if (i <= smin) courbe[i] = 0;
            else if (i >= smax) courbe[i] = 255;
            else courbe[i] = limiterNiveauGris((int) Math.round(255.0 * (i - smin) / (smax - smin)));
        }

        return courbe;
    }

    public static int[] creeCourbeTonaleGamma(double gamma) {

        System.out.println("Fonction creeCourbeTonaleGamma");

        if (gamma <= 0) return null;

        int[] courbe = new int[256];

        for (int i = 0; i < 256; i++)
        {
            courbe[i] = limiterNiveauGris((int) Math.round(255.0 * Math.pow(i / 255.0, gamma)));
        }

        return courbe;
    }

    public static int[] creeCourbeTonaleNegatif() {

        System.out.println("Fonction creeCourbeTonaleNegatif");

        int[] courbe = new int[256];

        for (int i = 0; i < 256; i++) courbe[i] = 255 - i;

        return courbe;
    }

    public static int[] creeCourbeTonaleEgalisation(int[][] image) {

        System.out.println("Fonction creeCourbeTonaleEgalisation");

        if (image == null || image.length == 0) return null;

        int[] histogramme = Histogramme256(image);
        int totalPixels = 0;
        for (int valeur : histogramme) totalPixels += valeur;
        if (totalPixels == 0) return null;

        int[] courbe = new int[256];
        long cumul = 0;

        for (int i = 0; i < 256; i++)
        {
            cumul += histogramme[i];
            courbe[i] = limiterNiveauGris((int) Math.round(255.0 * cumul / totalPixels));
        }

        return courbe;
    }

    private static int limiterNiveauGris(int valeur)
    {
        return Math.max(0, Math.min(255, valeur));
    }

    private static int extremum(int[][] image, boolean chercherMin)
    {
        if (image == null || image.length == 0) return 0;

        boolean trouve = false;
        int extremum = 0;

        for (int[] row : image)
        {
            if (row == null) continue;
            for (int value : row)
            {
                if (!trouve)
                {
                    extremum = value;
                    trouve = true;
                }
                else if ((chercherMin && (value < extremum)) || (!chercherMin && (value > extremum)))
                {
                    extremum = value;
                }
            }
        }

        return trouve ? extremum : 0;
    }

    private static double averagePixelValue(int[][] image)
    {
        if (image == null || image.length == 0) return 0;

        long somme = 0;
        long nombrePixels = 0;

        for (int[] row : image)
        {
            if (row == null) continue;
            for (int value : row)
            {
                somme += value;
                nombrePixels++;
            }
        }

        if (nombrePixels == 0) return 0;

        return (double) somme / nombrePixels;
    }

    private static long countPixel(int[][] image)
    {
        if (image == null || image.length == 0) return 0;

        long nombrePixels = 0;

        for (int[] row : image)
        {
            if (row == null) continue;
            nombrePixels += row.length;
        }

        return nombrePixels;
    }
}
