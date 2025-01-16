import org.opencv.core.Core;

public class Main {
    public static void main(String[] args) {
        // Charger la bibliothèque OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        System.out.println("OpenCV chargé avec succès !");
    }
}
