package ImageProcessing.Histogramme.utils;

public class ImageTools {
    /**
     * Clamps a value to the {@code [0..255]} interval.
     *
     * @param valeur value to clamp
     * @return value forced into the grayscale range
     */
    public static int limiterNiveauGris(int valeur)
    {
        return Math.max(0, Math.min(255, valeur));
    }

    /**
     * Search the extremum value into an image.
     *
     * @param image source grayscale image
     * @param chercherMin True for searching minus and False for maximum
     *
     * @return the extremum value found into the pixel image
     */
    public static int extremum(int[][] image, boolean chercherMin)
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

    /**
     * Get the average value for pixel into an image.
     *
     * @param image source grayscale image
     * @return the average value for pixel into an image; returns {@code 0} if the image is null,
     */
    public static double averagePixelValue(int[][] image)
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
}
