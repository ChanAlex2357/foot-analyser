package app;

import javax.swing.JFrame;

import pannel.ImportFilePannel;

public class Launcher {
    // Méthode principale pour tester l'interface
    public static void main(String[] args) {
        JFrame frame = new JFrame("Jeu d'Importation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Ajouter le panneau personnalisé
        ImportFilePannel panel = new ImportFilePannel();
        frame.add(panel);
        frame.setVisible(true);
    }
}
