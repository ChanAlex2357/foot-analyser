package pann;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import pann.Joueur; // Add this import statement
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Frame extends JFrame {

    private JLabel imageLabel;
    private Mat currentImage;

    public Frame() {
        setTitle("Football Offside Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton loadButton = new JButton("Importer une Image");
        JButton scanButton = new JButton("Scan Hors Jeu");
        JButton detectTerrainButton = new JButton("Detect Terrain");
        JButton equipePossessionButton = new JButton("Equipe Possession");
        JButton equipeDefenseButton = new JButton("Equipe Defense");
        JButton dernierDefenseurButton = new JButton("Dernier Defenseur");
        JButton playerBallonButton = new JButton("Player Ballon");
        JButton showBallonButton = new JButton("Show Ballon");

        buttonPanel.add(loadButton);
        buttonPanel.add(scanButton);
        buttonPanel.add(detectTerrainButton);
        buttonPanel.add(equipePossessionButton);
        buttonPanel.add(equipeDefenseButton);
        buttonPanel.add(dernierDefenseurButton);
        buttonPanel.add(playerBallonButton);
        buttonPanel.add(showBallonButton);

        add(buttonPanel, BorderLayout.WEST);

        loadButton.addActionListener(e -> loadAndDisplayImage());
        scanButton.addActionListener(e -> scanHorsJeu());
        detectTerrainButton.addActionListener(e -> detectTerrain());
        equipePossessionButton.addActionListener(e -> equipePossession());
        equipeDefenseButton.addActionListener(e -> equipeDefense());
        dernierDefenseurButton.addActionListener(e -> dernierDefenseur());
        playerBallonButton.addActionListener(e -> playerBallon());
        showBallonButton.addActionListener(e -> showBallon());
    }

    private void loadAndDisplayImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentImage = Imgcodecs.imread(file.getAbsolutePath());
            if (currentImage.empty()) {
                JOptionPane.showMessageDialog(this, "Impossible de charger l'image", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                displayImage(currentImage);
            }
        }
    }

    private void displayImage(Mat image) {
        BufferedImage bufferedImage = matToBufferedImage(image);
        imageLabel.setIcon(new ImageIcon(bufferedImage));
        imageLabel.repaint();
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int type = (mat.channels() > 1) ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), buffer);
        return image;
    }

    private Terrain detectTerrain() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Mat processedImage = currentImage.clone();
        Mat gray = new Mat();
        Imgproc.cvtColor(processedImage, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, gray, 200, 255, Imgproc.THRESH_BINARY);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Terrain terrain = new Terrain();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            terrain.addContour(rect);
            Imgproc.drawContours(processedImage, List.of(contour), -1, new Scalar(0, 0, 0), 2);
        }

        displayImage(processedImage);
        return terrain;
    }

    private List<Joueur> equipePossession() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Terrain terrain = detectTerrain();
        if (terrain == null) {
            return null;
        }

        Mat processedImage = currentImage.clone();
        Mat hsv = new Mat();
        Imgproc.cvtColor(processedImage, hsv, Imgproc.COLOR_BGR2HSV);

        // Detect team 1 (red)
        Mat maskTeam1 = new Mat();
        Core.inRange(hsv, new Scalar(0, 70, 50), new Scalar(10, 255, 255), maskTeam1);
        Mat maskTeam1_2 = new Mat();
        Core.inRange(hsv, new Scalar(170, 70, 50), new Scalar(180, 255, 255), maskTeam1_2);
        Core.add(maskTeam1, maskTeam1_2, maskTeam1);

        // Detect team 2 (blue)
        Mat maskTeam2 = new Mat();
        Core.inRange(hsv, new Scalar(100, 150, 0), new Scalar(140, 255, 255), maskTeam2);

        // Detect ball (black)
        Mat maskBall = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(180, 255, 30), maskBall);

        // Find contours for the ball
        List<MatOfPoint> contoursBall = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(maskBall, contoursBall, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contoursBall.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ballon non détecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Assume the first contour is the ball
        Rect ballRect = Imgproc.boundingRect(contoursBall.get(0));
        Point ballCenter = new Point(ballRect.x + ballRect.width / 2.0, ballRect.y + ballRect.height / 2.0);

        // Find contours for team 1
        List<MatOfPoint> contoursTeam1 = new ArrayList<>();
        Imgproc.findContours(maskTeam1, contoursTeam1, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find contours for team 2
        List<MatOfPoint> contoursTeam2 = new ArrayList<>();
        Imgproc.findContours(maskTeam2, contoursTeam2, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double minDistance = Double.MAX_VALUE;
        String teamInPossession = "None";

        // Check team 1
        for (MatOfPoint contour : contoursTeam1) {
            Rect rect = Imgproc.boundingRect(contour);
            Point center = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
            double distance = Math.sqrt(Math.pow(ballCenter.x - center.x, 2) + Math.pow(ballCenter.y - center.y, 2));

            if (distance < minDistance) {
                minDistance = distance;
                teamInPossession = "Team 1";
            }
        }

        // Check team 2
        for (MatOfPoint contour : contoursTeam2) {
            Rect rect = Imgproc.boundingRect(contour);
            Point center = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
            double distance = Math.sqrt(Math.pow(ballCenter.x - center.x, 2) + Math.pow(ballCenter.y - center.y, 2));

            if (distance < minDistance) {
                minDistance = distance;
                teamInPossession = "Team 2";
            }
        }

        List<MatOfPoint> teamContours = teamInPossession.equals("Team 1") ? contoursTeam1 : contoursTeam2;
        Scalar color = new Scalar(0, 0, 0);
        List<Joueur> teamInPossessionList = new ArrayList<>();

        for (MatOfPoint contour : teamContours) {
            Rect rect = Imgproc.boundingRect(contour);
            boolean isInTerrain = false;
            for (Rect terrainRect : terrain.getContours()) {
                if (rect.tl().inside(terrainRect) && rect.br().inside(terrainRect)) {
                    isInTerrain = true;
                    break;
                }
            }
            if (isInTerrain) {
                teamInPossessionList.add(new Joueur(rect));
                Imgproc.rectangle(processedImage, rect.tl(), rect.br(), color, 2);
            }
        }

        displayImage(processedImage);
        return teamInPossessionList;
    }

    private List<Joueur> equipeDefense() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Terrain terrain = detectTerrain();
        if (terrain == null) {
            return null;
        }

        Mat processedImage = currentImage.clone();
        Mat hsv = new Mat();
        Imgproc.cvtColor(processedImage, hsv, Imgproc.COLOR_BGR2HSV);

        // Detect team 1 (red)
        Mat maskTeam1 = new Mat();
        Core.inRange(hsv, new Scalar(0, 70, 50), new Scalar(10, 255, 255), maskTeam1);
        Mat maskTeam1_2 = new Mat();
        Core.inRange(hsv, new Scalar(170, 70, 50), new Scalar(180, 255, 255), maskTeam1_2);
        Core.add(maskTeam1, maskTeam1_2, maskTeam1);

        // Detect team 2 (blue)
        Mat maskTeam2 = new Mat();
        Core.inRange(hsv, new Scalar(100, 150, 0), new Scalar(140, 255, 255), maskTeam2);

        // Detect ball (black)
        Mat maskBall = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(180, 255, 30), maskBall);

        // Find contours for the ball
        List<MatOfPoint> contoursBall = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(maskBall, contoursBall, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contoursBall.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ballon non détecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Assume the first contour is the ball
        Rect ballRect = Imgproc.boundingRect(contoursBall.get(0));
        Point ballCenter = new Point(ballRect.x + ballRect.width / 2.0, ballRect.y + ballRect.height / 2.0);

        // Find contours for team 1
        List<MatOfPoint> contoursTeam1 = new ArrayList<>();
        Imgproc.findContours(maskTeam1, contoursTeam1, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find contours for team 2
        List<MatOfPoint> contoursTeam2 = new ArrayList<>();
        Imgproc.findContours(maskTeam2, contoursTeam2, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double minDistance = Double.MAX_VALUE;
        String teamInPossession = "None";

        // Check team 1
        for (MatOfPoint contour : contoursTeam1) {
            Rect rect = Imgproc.boundingRect(contour);
            Point center = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
            double distance = Math.sqrt(Math.pow(ballCenter.x - center.x, 2) + Math.pow(ballCenter.y - center.y, 2));

            if (distance < minDistance) {
                minDistance = distance;
                teamInPossession = "Team 1";
            }
        }

        // Check team 2
        for (MatOfPoint contour : contoursTeam2) {
            Rect rect = Imgproc.boundingRect(contour);
            Point center = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
            double distance = Math.sqrt(Math.pow(ballCenter.x - center.x, 2) + Math.pow(ballCenter.y - center.y, 2));

            if (distance < minDistance) {
                minDistance = distance;
                teamInPossession = "Team 2";
            }
        }

        List<MatOfPoint> teamContours = teamInPossession.equals("Team 1") ? contoursTeam2 : contoursTeam1;
        Scalar color = new Scalar(0, 0, 0);
        List<Joueur> teamInDefenseList = new ArrayList<>();

        for (MatOfPoint contour : teamContours) {
            Rect rect = Imgproc.boundingRect(contour);
            boolean isInTerrain = false;
            for (Rect terrainRect : terrain.getContours()) {
                if (rect.tl().inside(terrainRect) && rect.br().inside(terrainRect)) {
                    isInTerrain = true;
                    break;
                }
            }
            if (isInTerrain) {
                teamInDefenseList.add(new Joueur(rect));
                Imgproc.rectangle(processedImage, rect.tl(), rect.br(), color, 2);
            }
        }

        displayImage(processedImage);
        return teamInDefenseList;
    }

    private Joueur dernierDefenseur() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        List<Joueur> defendingTeam = equipeDefense();
        if (defendingTeam == null || defendingTeam.size() < 2) {
            JOptionPane.showMessageDialog(this, "Impossible de détecter l'équipe en défense.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Sort players by their distance from the goal line
        defendingTeam.sort((joueur1, joueur2) -> {
            Rect rect1 = joueur1.getPosition();
            Rect rect2 = joueur2.getPosition();
            return Double.compare(rect1.y + rect1.height, rect2.y + rect2.height);
        });

        // Get the last defender (second closest to the goal line)
        Joueur lastDefender = defendingTeam.get(1);

        Mat processedImage = currentImage.clone();
        Scalar color = new Scalar(0, 255, 0);
        Rect lastDefenderRect = lastDefender.getPosition();
        Imgproc.rectangle(processedImage, lastDefenderRect.tl(), lastDefenderRect.br(), color, 2);

        displayImage(processedImage);
        return lastDefender;
    }

    private Joueur playerBallon() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        List<Joueur> attackingTeam = equipePossession();
        if (attackingTeam == null) {
            JOptionPane.showMessageDialog(this, "Impossible de détecter l'équipe en possession.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Detect ball (black)
        Mat hsv = new Mat();
        Imgproc.cvtColor(currentImage, hsv, Imgproc.COLOR_BGR2HSV);
        Mat maskBall = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(180, 255, 30), maskBall);

        // Find contours for the ball
        List<MatOfPoint> contoursBall = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(maskBall, contoursBall, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contoursBall.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ballon non détecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Assume the first contour is the ball
        Rect ballRect = Imgproc.boundingRect(contoursBall.get(0));
        Point ballCenter = new Point(ballRect.x + ballRect.width / 2.0, ballRect.y + ballRect.height / 2.0);

        // Find the player closest to the ball
        Joueur closestPlayer = null;
        double minDistanceToBall = Double.MAX_VALUE;
        for (Joueur joueur : attackingTeam) {
            Rect rect = joueur.getPosition();
            Point playerCenter = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
            double distanceToBall = Math.sqrt(Math.pow(ballCenter.x - playerCenter.x, 2) + Math.pow(ballCenter.y - playerCenter.y, 2));
            if (distanceToBall < minDistanceToBall) {
                minDistanceToBall = distanceToBall;
                closestPlayer = joueur;
            }
        }

        if (closestPlayer == null) {
            JOptionPane.showMessageDialog(this, "Impossible de détecter le joueur le plus proche du ballon.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Highlight the player in possession of the ball
        Mat processedImage = currentImage.clone();
        Scalar color = new Scalar(0, 255, 0);
        Rect closestPlayerRect = closestPlayer.getPosition();
        Imgproc.rectangle(processedImage, closestPlayerRect.tl(), closestPlayerRect.br(), color, 2);
        displayImage(processedImage);

        return closestPlayer;
    }

    private Ballon detectBallon() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Detect ball (black)
        Mat hsv = new Mat();
        Imgproc.cvtColor(currentImage, hsv, Imgproc.COLOR_BGR2HSV);
        Mat maskBall = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(180, 255, 30), maskBall);

        // Find contours for the ball
        List<MatOfPoint> contoursBall = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(maskBall, contoursBall, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contoursBall.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ballon non détecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Assume the first contour is the ball
        Rect ballRect = Imgproc.boundingRect(contoursBall.get(0));

        return new Ballon(ballRect);
    }

    private void showBallon() {
        Ballon ballon = detectBallon();
        if (ballon == null) {
            return;
        }

        Mat processedImage = currentImage.clone();
        Scalar color = new Scalar(0, 0, 255); // Red for the ball
        Rect ballRect = ballon.getPosition();
        Imgproc.rectangle(processedImage, ballRect.tl(), ballRect.br(), color, 2);
        Imgproc.putText(processedImage, "Ballon", new Point(ballRect.x, ballRect.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 2);
        displayImage(processedImage);
    }

    private void scanHorsJeu() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Joueur> attackingTeam = equipePossession();
        List<Joueur> defendingTeam = equipeDefense();
        Joueur lastDefender = dernierDefenseur();
        Ballon ballon = detectBallon();

        if (attackingTeam == null || defendingTeam == null || lastDefender == null || ballon == null) {
            JOptionPane.showMessageDialog(this, "Impossible de détecter les équipes, le dernier défenseur ou le ballon.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Mat processedImage = currentImage.clone();
        Scalar colorHJ = new Scalar(0, 0, 255); // Red for offside
        Scalar colorN = new Scalar(0, 255, 0); // Green for not offside
        Rect lastDefenderRect = lastDefender.getPosition();
        boolean offsideDetected = false;

        Rect ballRect = ballon.getPosition();
        Point ballCenter = new Point(ballRect.x + ballRect.width / 2.0, ballRect.y + ballRect.height / 2.0);

        for (Joueur joueur : attackingTeam) {
            Rect rect = joueur.getPosition();
            if (rect.y + rect.height < lastDefenderRect.y + lastDefenderRect.height) {
                Imgproc.putText(processedImage, "HJ", new Point(rect.x, rect.y + rect.height + 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, colorHJ, 2);
                offsideDetected = true;
            } else {
                Imgproc.putText(processedImage, "N", new Point(rect.x, rect.y + rect.height + 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, colorN, 2);
                // Draw arrow from ball to player
                Point playerCenter = new Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0);
                Imgproc.arrowedLine(processedImage, ballCenter, playerCenter, colorN, 2);
            }
        }

        if (!offsideDetected) {
            JOptionPane.showMessageDialog(this, "Aucun joueur hors jeu.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            displayImage(processedImage);
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame();
            frame.setVisible(true);
        });
    }
}