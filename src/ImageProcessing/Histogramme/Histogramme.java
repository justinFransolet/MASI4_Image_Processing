package ImageProcessing.Histogramme;

import ImageProcessing.Histogramme.utils.ImageTools;

/**
 * Histogram-processing utilities for grayscale images.
 *
 * <p>The methods in this class are static and operate on images represented as
 * integer matrices {@code int[][]}.</p>
 */
public class Histogramme
{
    /**
     * Computes the 256-level grayscale histogram of an image.
     *
     * @param mat grayscale image; values outside {@code [0..255]} are ignored
     * @return an array of 256 entries containing the occurrence count of each
     *         grayscale level; returns an empty histogram if the image is null
     */
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

    /**
     * Determines the smallest grayscale value present in the image.
     *
     * @param image grayscale image
     * @return the minimum value found, or {@code 0} if the image is null, empty,
     *         or contains no usable pixel
     */
    public static int minimum(int[][] image) {
        return ImageTools.extremum(image, true);
    }

    /**
     * Determines the largest grayscale value present in the image.
     *
     * @param image grayscale image
     * @return the maximum value found, or {@code 0} if the image is null, empty,
     *         or contains no usable pixel
     */
    public static int maximum(int[][] image) {
        return ImageTools.extremum(image, false);
    }

    /**
     * Computes the average luminance of the image.
     *
     * @param image grayscale image
     * @return the arithmetic mean of the pixels, rounded to the nearest integer;
     *         returns {@code 0} if the image is null, empty, or contains no
     *         usable pixel
     */
    public static int luminance(int[][] image) {
        return (int) Math.round(ImageTools.averagePixelValue(image));
    }

    /**
     * Computes a simple contrast defined as {@code maximum(image) - minimum(image)}.
     *
     * @param image grayscale image
     * @return difference between the maximum and minimum values; returns
     *         {@code 0} if the image is null or empty
     */
    public static double contraste1(int[][] image) {
        if (image == null || image.length == 0) return 0;
        return maximum(image) - minimum(image);
    }

    /**
     * Computes a standard-deviation-based contrast on grayscale values.
     *
     * @param image grayscale image
     * @return pixel standard deviation; returns {@code 0} if the image is null,
     *         empty, or contains no usable pixel
     */
    public static double contraste2(int[][] image) {
        double moyenne = ImageTools.averagePixelValue(image);
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

        long nombrePixels = ImageTools.countPixel(image);
        if (nombrePixels == 0) return 0;

        variance /= nombrePixels;
        return Math.sqrt(variance);
    }

    /**
     * Applies a tone curve to the image to perform enhancement.
     *
     * @param image source grayscale image
     * @param courbeTonale lookup table of 256 values, indexed by input grayscale
     *                     level
     * @return transformed image; returns {@code null} if the image is null,
     *         empty, or if the tone curve is invalid
     */
    public static int[][] rehaussement(int[][] image, int[] courbeTonale) {
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
                resultat[x][y] = ImageTools.limiterNiveauGris(courbeTonale[valeur]);
            }
        }

        return resultat;
    }

    /**
     * Builds a linear tone curve with saturation.
     *
     * <p>Values less than or equal to {@code smin} are mapped to 0, values
     * greater than or equal to {@code smax} are mapped to 255, and intermediate
     * values are linearly interpolated.</p>
     *
     * @param smin lower saturation threshold
     * @param smax upper saturation threshold
     * @return tone curve of 256 values
     */
    public static int[] creeCourbeTonaleLineaireSaturation(int smin, int smax) {
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
            else courbe[i] = ImageTools.limiterNiveauGris((int) Math.round(255.0 * (i - smin) / (smax - smin)));
        }

        return courbe;
    }

    /**
     * Builds a gamma correction tone curve.
     *
     * @param gamma gamma exponent; must be strictly positive
     * @return tone curve of 256 values, or {@code null} if {@code gamma <= 0}
     */
    public static int[] creeCourbeTonaleGamma(double gamma) {
        if (gamma <= 0) return null;

        int[] courbe = new int[256];

        for (int i = 0; i < 256; i++)
        {
            courbe[i] = ImageTools.limiterNiveauGris((int) Math.round(255.0 * Math.pow(i / 255.0, gamma)));
        }

        return courbe;
    }

    /**
     * Builds the negative tone curve.
     *
     * @return tone curve of 256 values where {@code i} is mapped to
     *         {@code 255 - i}
     */
    public static int[] creeCourbeTonaleNegatif() {
        int[] courbe = new int[256];

        for (int i = 0; i < 256; i++) courbe[i] = 255 - i;

        return courbe;
    }

    /**
     * Builds a tone curve for histogram equalization.
     *
     * @param image source grayscale image
     * @return tone curve of 256 values, or {@code null} if the image is null,
     *         empty, or contains no usable pixel
     */
    public static int[] creeCourbeTonaleEgalisation(int[][] image) {
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
            courbe[i] = ImageTools.limiterNiveauGris((int) Math.round(255.0 * cumul / totalPixels));
        }

        return courbe;
    }
}
