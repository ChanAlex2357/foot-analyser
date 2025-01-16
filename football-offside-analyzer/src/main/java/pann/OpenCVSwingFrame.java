package pann;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenCVSwingFrame extends JFrame {

    private JLabel imageLabel;
    private Mat currentImage;

    public OpenCVSwingFrame() {
        setTitle("Football Offside Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        add(scrollPane, BorderLayout.CENTER);

        JButton loadButton = new JButton("Charger une Image");
        JButton analyzeButton = new JButton("Analyser Hors-Jeu");
        add(loadButton, BorderLayout.SOUTH);
        add(analyzeButton, BorderLayout.NORTH);

        loadButton.addActionListener(e -> loadAndDisplayImage());
        analyzeButton.addActionListener(e -> processImage());
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

    private void loadAndDisplayImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentImage = Imgcodecs.imread(file.getAbsolutePath());
            if (currentImage.empty()) {
                JOptionPane.showMessageDialog(this, "Impossible de charger l'image", "Erreur", JOptionPane.ERROR_MESSAGE);
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

    private void processImage() {
        if (currentImage == null) {
            JOptionPane.showMessageDialog(this, "Veuillez charger une image d'abord.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Mat processedImage = currentImage.clone();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat gray = new Mat();
        Imgproc.cvtColor(currentImage, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        List<Rect> playerRects = new ArrayList<>();
        Rect ballRect = null;

        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            if (rect.width > 10 && rect.height > 10) {
                if (rect.width < 20 && rect.height < 20 && Math.abs(rect.width - rect.height) < 5) {
                    ballRect = rect;
                } else {
                    playerRects.add(rect);
                }
            }
        }

        int sens = SensChecker(processedImage);
        List<Player> players = new ArrayList<>();
        for (Rect rect : playerRects) {
            players.add(new Player(rect, TeamChecker(processedImage, rect), sens));
        }
        Team teamPossession = ballRect != null ? TeamPossession(players, ballRect) : Team.NONE;
        Player lastDefender = LastDefender(players, teamPossession == Team.TEAM1 ? Team.TEAM2 : Team.TEAM1, sens);

        if (ballRect != null && lastDefender != null) {
            for (Player player : players) {
                if (Offside(player, lastDefender, ballRect, sens) && player.team != teamPossession) {
                    Imgproc.rectangle(processedImage, player.rect, new Scalar(0, 0, 255), 2);
                } else {
                    Imgproc.rectangle(processedImage, player.rect, new Scalar(0, 255, 0), 2);
                }
            }
        }

        displayImage(processedImage);
    }

    private enum Team {TEAM1, TEAM2, NONE}

    private static class Player {
        public Rect rect;
        public Team team;
        public int sens;

        public Player(Rect rect, Team team, int sens) {
            this.rect = rect;
            this.team = team;
            this.sens = sens;
        }
    }

    private Team TeamChecker(Mat image, Rect rect) {
        Mat roi = new Mat(image, rect);
        Scalar meanColor = Core.mean(roi);
        if (meanColor.val[0] > meanColor.val[2]) {
            return Team.TEAM1;
        } else {
            return Team.TEAM2;
        }
    }

    private Team TeamPossession(List<Player> players, Rect ballRect) {
        for (Player player : players) {
            if (player.rect.contains(new Point(ballRect.x + ballRect.width / 2, ballRect.y + ballRect.height / 2))) {
                return player.team;
            }
        }
        return Team.NONE;
    }

    private int SensChecker(Mat image) {
        return image.width() / 2;
    }

    private Player LastDefender(List<Player> players, Team defendingTeam, int sens) {
        Player lastDefender = null;
        int lastDefenderX = Integer.MAX_VALUE;
        for (Player player : players) {
            if (player.team == defendingTeam) {
                int playerX = player.rect.x + player.rect.width / 2;
                if ((player.sens > sens && playerX < lastDefenderX) || (player.sens < sens && playerX > lastDefenderX)) {
                    lastDefender = player;
                    lastDefenderX = playerX;
                }
            }
        }
        return lastDefender;
    }

    private boolean Offside(Player player, Player lastDefender, Rect ballRect, int sens) {
        if (player.sens > sens) {
            return player.rect.x > lastDefender.rect.x && player.rect.x > ballRect.x;
        } else {
            return player.rect.x < lastDefender.rect.x && player.rect.x < ballRect.x;
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        SwingUtilities.invokeLater(() -> {
            OpenCVSwingFrame frame = new OpenCVSwingFrame();
            frame.setVisible(true);
        });
    }
}