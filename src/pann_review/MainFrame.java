package pann_review;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    static {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME); // Charger la bibliothèque OpenCV
    }

    private JLabel labelImage;
    private JScrollPane scrollPane;

    public MainFrame() {
        // Configuration de la fenêtre principale
        setTitle("Swing OpenCV Image Viewer avec Scroll");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panneau latéral (Barre de navigation)
        JPanel navBar = new JPanel();
        navBar.setLayout(new GridLayout(5, 1, 5, 5)); // Une colonne avec espacement
        navBar.setBackground(new Color(230, 230, 250)); // Couleur de fond
        navBar.setPreferredSize(new Dimension(150, 0)); // Largeur fixe

        // Boutons dans la barre de navigation
        JButton btnImporter = new JButton("Importer une image");
        JButton btnDetecterTerrain = new JButton("Détecter Terrain");
        JButton btnDetecterEquipe = new JButton("Détecter équipe");
        JButton btnOption3 = new JButton("Option 3");
        JButton btnQuitter = new JButton("Quitter");

        // Ajouter des actions aux boutons
        btnImporter.addActionListener(e -> importerFichierAvecOpenCV());
        btnDetecterTerrain.addActionListener(e -> detecterTerrain());
        btnDetecterEquipe.addActionListener(e -> detecterEquipe());
        btnQuitter.addActionListener(e -> System.exit(0));

        // Ajouter les boutons à la barre de navigation
        navBar.add(btnImporter);
        navBar.add(btnDetecterTerrain);
        navBar.add(btnDetecterEquipe);
        navBar.add(btnOption3);
        navBar.add(btnQuitter);

        // Panneau pour afficher l'image avec défilement
        labelImage = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
        labelImage.setBackground(Color.LIGHT_GRAY);
        labelImage.setOpaque(true);

        // Ajouter le JLabel dans un JScrollPane
        scrollPane = new JScrollPane(labelImage);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Ajouter les panneaux à la fenêtre
        add(navBar, BorderLayout.WEST); // Barre de navigation à gauche
        add(scrollPane, BorderLayout.CENTER); // Zone d'affichage avec scroll au centre

        setVisible(true);
    }

    private void importerFichierAvecOpenCV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionnez une image");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (JPEG, PNG, BMP)", "jpg", "jpeg", "png",
                "bmp");
        fileChooser.setFileFilter(filter);

        int resultat = fileChooser.showOpenDialog(this);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            File fichierSelectionne = fileChooser.getSelectedFile();
            Mat imageMat = Imgcodecs.imread(fichierSelectionne.getAbsolutePath());
            Mat imageRBG = new Mat();
            Imgproc.cvtColor(imageMat, imageRBG, Imgproc.COLOR_BGR2RGB);

            if (!imageMat.empty()) {
                BufferedImage imageBuffered = convertirMatEnBufferedImage(imageRBG);
                labelImage.setIcon(new ImageIcon(imageBuffered));
                labelImage.setText(null);
                labelImage.setPreferredSize(new Dimension(imageBuffered.getWidth(), imageBuffered.getHeight()));
                scrollPane.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Impossible de charger l'image.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Terrain detecterTerrain() {
        Icon icon = labelImage.getIcon();
        if (icon == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord importer une image.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        BufferedImage bufferedImage = (BufferedImage) ((ImageIcon) icon).getImage();
        Mat mat = convertirBufferedImageEnMat(bufferedImage);

        Mat edges = new Mat();
        Imgproc.cvtColor(mat, edges, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(edges, edges, new Size(5, 5), 0);
        Imgproc.Canny(edges, edges, 50, 150);

        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 0;
        MatOfPoint terrainContour = null;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                terrainContour = contour;
            }
        }

        if (terrainContour != null) {
            Mat result = mat.clone();
            Imgproc.drawContours(result, List.of(terrainContour), -1, new Scalar(0, 255, 0), 2);

            BufferedImage resultImage = convertirMatEnBufferedImage(result);
            labelImage.setIcon(new ImageIcon(resultImage));

            Rect boundingRect = Imgproc.boundingRect(terrainContour);
            return new Terrain(boundingRect.x, boundingRect.y, boundingRect.width, boundingRect.height);
        } else {
            JOptionPane.showMessageDialog(this, "Aucun terrain détecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void detecterEquipe() {
        Terrain terrain = detecterTerrain();
        if (terrain == null)
            return;

        Icon icon = labelImage.getIcon();
        BufferedImage bufferedImage = (BufferedImage) ((ImageIcon) icon).getImage();
        Mat mat = convertirBufferedImageEnMat(bufferedImage);

        Mat edges = new Mat();
        Imgproc.cvtColor(mat, edges, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(edges, edges, new Size(5, 5), 0);
        Imgproc.Canny(edges, edges, 50, 150);

        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Joueur> joueurs = new ArrayList<>();
        Rect ballon = null;

        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            double area = Imgproc.contourArea(contour);

            // Si la surface est trop petite, ignorer
            if (boundingRect.width * boundingRect.height > 100 && terrain.contains(boundingRect)) {
                // Si c'est potentiellement un ballon (couleur sombre/noir avec petite taille)
                if (area < 1000) {
                    ballon = boundingRect;
                } else {
                    joueurs.add(new Joueur(boundingRect.x, boundingRect.y, boundingRect.width, boundingRect.height));
                }
            }
        }

        // Diviser les joueurs en deux équipes
        int mid = joueurs.size() / 2;
        List<Joueur> equipe1 = joueurs.subList(0, mid);
        List<Joueur> equipe2 = joueurs.subList(mid, joueurs.size());

        // Dessiner les contours sur l'image
        Mat result = mat.clone();

        // Dessiner l'équipe 1 (contour noir)
        for (Joueur joueur : equipe1) {
            Rect rect = new Rect(joueur.x, joueur.y, joueur.width, joueur.height);
            Imgproc.rectangle(result, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 0), 3); // Noir
        }

        // Dessiner l'équipe 2 (contour blanc)
        for (Joueur joueur : equipe2) {
            Rect rect = new Rect(joueur.x, joueur.y, joueur.width, joueur.height);
            Imgproc.rectangle(result, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255, 255, 255), 3); // Blanc
        }

        // Dessiner le ballon (contour jaune)
        if (ballon != null) {
            Imgproc.rectangle(result, new Point(ballon.x, ballon.y),
                    new Point(ballon.x + ballon.width, ballon.y + ballon.height),
                    new Scalar(0, 255, 255), 3); // Jaune
        }

        // Mettre à jour l'image affichée
        BufferedImage resultImage = convertirMatEnBufferedImage(result);
        labelImage.setIcon(new ImageIcon(resultImage));
        System.out.println("Équipe 1 : " + equipe1);
        System.out.println("Équipe 2 : " + equipe2);
        if (ballon != null) {
            System.out.println("Ballon détecté à : " + ballon);
        }
    }

    private BufferedImage convertirMatEnBufferedImage(Mat mat) {
        int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
        int largeur = mat.cols();
        int hauteur = mat.rows();
        byte[] data = new byte[largeur * hauteur * mat.channels()];
        mat.get(0, 0, data);

        BufferedImage image = new BufferedImage(largeur, hauteur, type);
        image.getRaster().setDataElements(0, 0, largeur, hauteur, data);
        return image;
    }

    private Mat convertirBufferedImageEnMat(BufferedImage image) {
        int type = image.getType() == BufferedImage.TYPE_BYTE_GRAY ? CvType.CV_8UC1 : CvType.CV_8UC3;
        Mat mat = new Mat(image.getHeight(), image.getWidth(), type);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }

    // Classe Terrain
    public static class Terrain {
        private int x, y, width, height;

        public Terrain(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(Rect rect) {
            return rect.x >= x && rect.x + rect.width <= x + width &&
                    rect.y >= y && rect.y + rect.height <= y + height;
        }

        @Override
        public String toString() {
            return "Terrain{x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "}";
        }
    }

    // Classe Joueur
    public static class Joueur {
        private int x, y, width, height;

        public Joueur(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "Joueur{x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "}";
        }
    }
}
