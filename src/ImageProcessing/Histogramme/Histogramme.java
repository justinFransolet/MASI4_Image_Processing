package ImageProcessing.Histogramme;

public class Histogramme 
{
    public static int[] Histogramme256(int[][] mat)
    {
        int M = mat.length;
        int N = mat[0].length;
        int[] histo = new int[256];
        
        for(int i=0 ; i<256 ; i++) histo[i] = 0;

        for (int[] ints : mat)
            for (int j = 0; j < N; j++)
                if ((ints[j] >= 0) && (ints[j] <= 255)) histo[ints[j]]++;
        
        return histo;
    }

    public static int minimum(int[][] image) {

        System.out.println("Fonction minimum");

        //TODO (étape 3.1)

        return 0;
    }

    public static int maximum(int[][] image) {

        System.out.println("Fonction maximum");

        //TODO (étape 3.2)

        return 0;
    }

    public static int luminance(int[][] image) {

        System.out.println("Fonction luminance");

        //TODO (étape 3.3)

        return 0;
    }

    public static double contraste1(int[][] image) {

        System.out.println("Fonction contraste1");

        //TODO (étape 3.4)

        return 0;
    }

    public static double contraste2(int[][] image) {

        System.out.println("Fonction contraste2");

        //TODO (étape 3.5)

        return 0;
    }

    public static int[][] rehaussement(int[][] image, int[] courbeTonale) {

        System.out.println("Fonction rehaussement");

        //TODO (étape 3.6)

        return null;
    }

    public static int[] creeCourbeTonaleLineaireSaturation(int smin, int smax) {

        System.out.println("Fonction creeCourbeTonaleLineaireSaturation");

        //TODO (étape 3.7)

        return null;
    }

    public static int[] creeCourbeTonaleGamma(double gamma) {

        System.out.println("Fonction creeCourbeTonaleGamma");

        //TODO (étape 3.8)

        return null;
    }

    public static int[] creeCourbeTonaleNegatif() {

        System.out.println("Fonction creeCourbeTonaleNegatif");

        //TODO (étape 3.9)

        return null;
    }

    public static int[] creeCourbeTonaleEgalisation(int[][] image) {

        System.out.println("Fonction creeCourbeTonaleEgalisation");

        //TODO (étape 3.10)

        return null;
    }
}
