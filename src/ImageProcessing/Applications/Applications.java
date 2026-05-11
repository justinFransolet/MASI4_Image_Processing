package ImageProcessing.Applications;

import CImage.CImageNG;
import CImage.CImageRGB;
import CImage.Exceptions.CImageNGException;
import CImage.Exceptions.CImageRGBException;
import ImageProcessing.Contours.ContoursNonLineaire;
import ImageProcessing.Histogramme.Histogramme;
import ImageProcessing.NonLineaire.MorphoComplexe;
import ImageProcessing.NonLineaire.MorphoElementaire;
import ImageProcessing.Seuillage.Seuillage;

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

        int[][] image = chargerNG(dossierDatasets, "balanes.png");

        // 1) Binarisation : toutes les balanes en blanc, fond en noir
        int[][] balanes = Seuillage.seuillageAutomatique(image);

        // 2) Nettoyage morphologique
        balanes = MorphoElementaire.fermeture(balanes, 7);
        balanes = MorphoElementaire.ouverture(balanes, 11);

        // 3) Extraction des grandes balanes
        int[][] marqueurGrandes = MorphoElementaire.ouverture(balanes, 23);

        /*
         * 5) Les petites balanes sont ce qui reste quand on enlève les grandes.
         */
        int[][] petitesBalanesMasque = soustractionBinaire(balanes, marqueurGrandes);
        petitesBalanesMasque = MorphoElementaire.ouverture(petitesBalanesMasque, 7);

        /*
         * 7) Application des masques sur l'image originale pour avoir des images NG.
         */
        int[][] imageGrandes = appliquerMasque(image, marqueurGrandes);
        int[][] imagePetites = appliquerMasque(image, petitesBalanesMasque);

        return new Resultat[] {
                new Resultat("4 - origine balanes", new CImageNG(image), false),
                new Resultat("4 - masque de toutes les balanes", new CImageNG(balanes), false),
                new Resultat("4 - marqueur grandes balanes", new CImageNG(marqueurGrandes), false),
                new Resultat("4 - marqueur petites balanes", new CImageNG(petitesBalanesMasque), false),
                new Resultat("4 - grandes balanes", new CImageNG(imageGrandes)),
                new Resultat("4 - petites balanes", new CImageNG(imagePetites))
        };
    }

    public static Resultat[] exercice5(File dossierDatasets) throws IOException, CImageNGException {

        int[][] image = chargerNG(dossierDatasets, "tools.png");

        // 1) Détection des nuages sans les objets
        int[][] nuages = MorphoElementaire.ouverture(image,29);

        // 2) Soustraction des nuages de l'image de base
        int[][] imageSansBruit = soustraction(image, nuages);

        // 3) Seuillage pour avoir l'image en binaire
        int[][] imageSeuillie = Seuillage.seuillageSimple(imageSansBruit, 45);

        return new Resultat[] {
                new Resultat("5 - Image de base", new CImageNG(image), false),
                new Resultat("5 - Masque des nuages(bruit)", new CImageNG(nuages), false),
                new Resultat("5 - Image sans bruit", new CImageNG(imageSansBruit), false),
                new Resultat("5 - Image finale", new CImageNG(imageSeuillie))
        };
    }

    public static Resultat[] exercice6(File dossierDatasets, File dossierSortie) throws IOException, CImageRGBException, CImageNGException {

        int[][] image = chargerNG(dossierDatasets, "vaisseaux.jpg");

        int [][] seuillage = Seuillage.seuillageAutomatique(image);

        int [][] fermeture = MorphoElementaire.fermeture(seuillage, 7);

        int [][] ouverture = MorphoElementaire.ouverture(fermeture, 47);

        int [][] grosVaisseau = MorphoComplexe.reconstructionGeodesique(ouverture, image);

        int [][] soustractionGrosVaisseau = soustraction(image, grosVaisseau);

        int [][] ouverturePetitVaisseau = MorphoElementaire.ouverture(soustractionGrosVaisseau, 9);

        int [][] seuillagePetitVaiseau = Seuillage.seuillageSimple(ouverturePetitVaisseau, 1);

        int [][] masquePetitVaisseau = MorphoElementaire.fermeture(seuillagePetitVaiseau, 3);

        CImageRGB imageVaisseauxRGB = chargerRGB(dossierDatasets, "vaisseaux.jpg");
        CImageRGB imagePlaneteRGB = chargerRGB(dossierDatasets, "planete.jpg");

        int largeur = imageVaisseauxRGB.getLargeur();
        int hauteur = imageVaisseauxRGB.getHauteur();

        int[][] rougeV = new int[largeur][hauteur];
        int[][] vertV = new int[largeur][hauteur];
        int[][] bleuV = new int[largeur][hauteur];
        imageVaisseauxRGB.getMatricesRGB(rougeV, vertV, bleuV);

        int[][] rougeP = new int[largeur][hauteur];
        int[][] vertP = new int[largeur][hauteur];
        int[][] bleuP = new int[largeur][hauteur];
        imagePlaneteRGB.getMatricesRGB(rougeP, vertP, bleuP);

        if (imageVaisseauxRGB.getLargeur() != imagePlaneteRGB.getLargeur()
                || imageVaisseauxRGB.getHauteur() != imagePlaneteRGB.getHauteur()) {
            throw new IllegalArgumentException("Les deux images doivent avoir la même taille.");
        }

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                if (masquePetitVaisseau[x][y] > 0) {
                    rougeP[x][y] = rougeV[x][y];
                    vertP[x][y] = vertV[x][y];
                    bleuP[x][y] = bleuV[x][y];
                }
            }
        }

        CImageRGB synthese = new CImageRGB(rougeP, vertP, bleuP);

        File fichierSynthese = new File(dossierSortie, "synthese.png");
        synthese.enregistreFormatPNG(fichierSynthese);

        // Création du contour du petit vaisseau avec les fonctions de contours déjà implémentées
        int[][] contourPetitVaisseau = ContoursNonLineaire.gradientDilatation(masquePetitVaisseau);

        // Par sécurité, on rebinarise le contour (mais pas vraiment nécessaire)
        contourPetitVaisseau = Seuillage.seuillageSimple(contourPetitVaisseau, 1);

        // Copie de l'image synthese pour créer synthese2
        int[][] rougeS2 = new int[largeur][hauteur];
        int[][] vertS2 = new int[largeur][hauteur];
        int[][] bleuS2 = new int[largeur][hauteur];

        synthese.getMatricesRGB(rougeS2, vertS2, bleuS2);

        // Coloration du contour en rouge
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                if (contourPetitVaisseau[x][y] > 0) {
                    rougeS2[x][y] = 255;
                    vertS2[x][y] = 0;
                    bleuS2[x][y] = 0;
                }
            }
        }

        CImageRGB synthese2 = new CImageRGB(rougeS2, vertS2, bleuS2);

        File fichierSynthese2 = new File(dossierSortie, "synthese2.png");
        synthese2.enregistreFormatPNG(fichierSynthese2);

        return new Resultat[] {
                new Resultat("6 - Image de base vaisseaux", imageVaisseauxRGB, false),
                new Resultat("6 - Image de base planete", imagePlaneteRGB, false),
                new Resultat("6 - Synthese", synthese, false),
                new Resultat("6 - Synthese2 contour rouge", synthese2)
        };
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

    private static int[][] soustractionBinaire(int[][] image1, int[][] image2) {
        int largeur = image1.length;
        int hauteur = image1[0].length;
        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                if (image1[x][y] > 0 && image2[x][y] == 0) {
                    resultat[x][y] = 255;
                } else {
                    resultat[x][y] = 0;
                }
            }
        }

        return resultat;
    }
    private static int[][] soustraction(int[][] image1, int[][] image2) {
        int largeur = image1.length;
        int hauteur = image1[0].length;
        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                int diff = image1[x][y] - image2[x][y];
                resultat[x][y] = Math.max(0, diff);
            }
        }

        return resultat;
    }


    private static int[][] appliquerMasque(int[][] image, int[][] masque) {
        int largeur = image.length;
        int hauteur = image[0].length;
        int[][] resultat = new int[largeur][hauteur];

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                if (masque[x][y] > 0) {
                    resultat[x][y] = image[x][y];
                } else {
                    resultat[x][y] = 0;
                }
            }
        }

        return resultat;
    }

    private static int clamp(int valeur) {
        if (valeur < 0) return 0;
        if (valeur > 255) return 255;
        return valeur;
    }
}
