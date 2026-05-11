package ImageProcessing.Applications;

import CImage.CImageNG;
import CImage.CImageRGB;
import CImage.Exceptions.CImageNGException;
import CImage.Exceptions.CImageRGBException;
import ImageProcessing.Histogramme.Histogramme;
import ImageProcessing.NonLineaire.MorphoComplexe;
import ImageProcessing.NonLineaire.MorphoElementaire;
import java.io.File;
import java.io.IOException;


public class Applications {
    public static class Resultat {
        public final String titre;
        public final CImageNG imageNG;
        public final CImageRGB imageRGB;
        public final boolean resultatPrincipal;

        public Resultat(String titre, CImageNG image) {
            this(titre, image, true);
        }

        public Resultat(String titre, CImageNG image, boolean resultatPrincipal) {
            this.titre = titre;
            this.imageNG = image;
            this.imageRGB = null;
            this.resultatPrincipal = resultatPrincipal;
        }

        public Resultat(String titre, CImageRGB image) {
            this(titre, image, true);
        }

        public Resultat(String titre, CImageRGB image, boolean resultatPrincipal) {
            this.titre = titre;
            this.imageNG = null;
            this.imageRGB = image;
            this.resultatPrincipal = resultatPrincipal;
        }

        public boolean estRGB() {
            return imageRGB != null;
        }
    }

    public static Resultat[] exercice1(File dossierDatasets) throws IOException, CImageNGException {
        int[][] image1 = chargerNG(dossierDatasets, "imageBruitee1.png");
        int[][] image2 = chargerNG(dossierDatasets, "imageBruitee2.png");

        int[][] resultat1 = MorphoComplexe.filtreMedian(image1, 5);
        resultat1 = MorphoComplexe.filtreMedian(resultat1, 5);

        int[][] resultat2 = MorphoElementaire.fermeture(image2, 3);
        resultat2 = MorphoElementaire.ouverture(resultat2, 3);

        return new Resultat[] {
                new Resultat("1 - origine imageBruitee1", new CImageNG(image1), false),
                new Resultat("1 - imageBruitee1 reduite", new CImageNG(resultat1)),
                new Resultat("1 - origine imageBruitee2", new CImageNG(image2), false),
                new Resultat("1 - imageBruitee2 reduite", new CImageNG(resultat2))
        };
    }

    public static Resultat[] exercice2(File dossierDatasets) throws IOException, CImageRGBException {
        CImageRGB image = chargerRGB(dossierDatasets, "lenaAEgaliser.jpg");
        int largeur = image.getLargeur();
        int hauteur = image.getHauteur();
        int[][] rouge = new int[largeur][hauteur];
        int[][] vert = new int[largeur][hauteur];
        int[][] bleu = new int[largeur][hauteur];
        image.getMatricesRGB(rouge, vert, bleu);

        int[][] rougeSepare = Histogramme.rehaussement(rouge, Histogramme.creeCourbeTonaleEgalisation(rouge));
        int[][] vertSepare = Histogramme.rehaussement(vert, Histogramme.creeCourbeTonaleEgalisation(vert));
        int[][] bleuSepare = Histogramme.rehaussement(bleu, Histogramme.creeCourbeTonaleEgalisation(bleu));

        int[][] luminance = luminance(rouge, vert, bleu);
        int[] courbeLuminance = Histogramme.creeCourbeTonaleEgalisation(luminance);
        int[][] rougeLum = Histogramme.rehaussement(rouge, courbeLuminance);
        int[][] vertLum = Histogramme.rehaussement(vert, courbeLuminance);
        int[][] bleuLum = Histogramme.rehaussement(bleu, courbeLuminance);

        return new Resultat[] {
                new Resultat("2 - origine lenaAEgaliser", image, false),
                new Resultat("2a - egalisation RGB separee", new CImageRGB(rougeSepare, vertSepare, bleuSepare)),
                new Resultat("2b - egalisation par luminance", new CImageRGB(rougeLum, vertLum, bleuLum))
        };
    }

    public static Resultat[] exercice3(File dossierDatasets) throws IOException, CImageRGBException, CImageNGException {

        CImageRGB image = chargerRGB(dossierDatasets, "petitsPois.png");
        int largeur = image.getLargeur();
        int hauteur = image.getHauteur();
        int[][] rouge = new int[largeur][hauteur];
        int[][] vert = new int[largeur][hauteur];
        int[][] bleu = new int[largeur][hauteur];
        image.getMatricesRGB(rouge, vert, bleu);

        int[][] masqueBleus = new int[largeur][hauteur];
        int[][] masqueRouges = new int[largeur][hauteur];
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                masqueBleus[x][y] = (bleu[x][y] > 80 && bleu[x][y] > rouge[x][y] + 25 && bleu[x][y] > vert[x][y] + 10) ? 255 : 0;
                masqueRouges[x][y] = (rouge[x][y] > 80 && rouge[x][y] > vert[x][y] + 25 && rouge[x][y] > bleu[x][y] + 25) ? 255 : 0;
            }
        }

        int[][] poisBleus = nettoyerBinaire(masqueBleus, 5);
        int[][] poisRouges = nettoyerBinaire(masqueRouges, 5);

        return new Resultat[] {
                new Resultat("3 - origine petitsPois", image, false),
                new Resultat("3 - masque bleu avant nettoyage", new CImageNG(masqueBleus), false),
                new Resultat("3 - masque rouge avant nettoyage", new CImageNG(masqueRouges), false),
                new Resultat("3 - pois bleus", new CImageNG(poisBleus)),
                new Resultat("3 - pois rouges", new CImageNG(poisRouges))
        };
    }

    public static Resultat[] exercice4(File dossierDatasets) throws IOException, CImageNGException {

        return null;
    }

    public static Resultat[] exercice5(File dossierDatasets) throws IOException, CImageNGException {

        return null;
    }

    public static Resultat[] exercice6(File dossierDatasets, File dossierSortie) throws IOException, CImageRGBException {

        return null;
    }

    public static Resultat[] exercice7(File dossierDatasets) throws IOException, CImageRGBException {

        return null;
    }

    private static CImageNG chargerImageNG(File dossierDatasets, String nom) throws IOException {
        return new CImageNG(trouverFichier(dossierDatasets, nom));
    }

    private static int[][] chargerNG(File dossierDatasets, String nom) throws IOException, CImageNGException {
        return chargerImageNG(dossierDatasets, nom).getMatrice();
    }

    private static CImageRGB chargerRGB(File dossierDatasets, String nom) throws IOException {
        return new CImageRGB(trouverFichier(dossierDatasets, nom));
    }

    private static File trouverFichier(File dossier, String nom) throws IOException {
        File direct = new File(dossier, nom);
        if (direct.isFile()) return direct;

        File trouve = chercherFichier(dossier, nom);
        if (trouve != null) return trouve;

        throw new IOException("Fichier introuvable dans datasets : " + nom);
    }

    private static File chercherFichier(File dossier, String nom) {
        if (dossier == null || !dossier.isDirectory()) return null;

        File[] fichiers = dossier.listFiles();
        if (fichiers == null) return null;

        for (File fichier : fichiers) {
            if (fichier.isFile() && fichier.getName().equalsIgnoreCase(nom)) return fichier;
        }

        for (File fichier : fichiers) {
            if (fichier.isDirectory()) {
                File trouve = chercherFichier(fichier, nom);
                if (trouve != null) return trouve;
            }
        }

        return null;
    }

    private static int[][] luminance(int[][] rouge, int[][] vert, int[][] bleu) {
        int largeur = rouge.length;
        int hauteur = rouge[0].length;
        int[][] resultat = new int[largeur][hauteur];
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                resultat[x][y] = clamp((int) Math.round(0.299 * rouge[x][y] + 0.587 * vert[x][y] + 0.114 * bleu[x][y]));
            }
        }
        return resultat;
    }

    private static int[][] nettoyerBinaire(int[][] masque, int taille) {
        return MorphoElementaire.fermeture(MorphoElementaire.ouverture(masque, taille), taille);
    }

    private static int clamp(int valeur) {
        if (valeur < 0) return 0;
        if (valeur > 255) return 255;
        return valeur;
    }
}
