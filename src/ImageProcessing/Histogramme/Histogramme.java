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
}
