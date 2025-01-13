package ui.components.button;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImportImageButton extends ImportButtion {
    ImageIcon importedImage;
    public ImportImageButton(Component parent,JLabel labelView){
        super(parent,labelView);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        importFile();
        File selectedFile = getImportedFile();
        setImportedImageFromFile(selectedFile);
        showImage(getImportedImage());
    }
    public void showImage(ImageIcon imageIcon){
        if (this.getShowerLabel() == null) {return;}
        getShowerLabel().setIcon(imageIcon);
    }
    
    public void setImportedImageFromFile(File file){
        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
        setImportedImage(imageIcon);
    }
    public ImageIcon getImportedImage() {
        if (importedImage == null && getImportedFile() != null) {
            setImportedImageFromFile(getImportedFile());
        }
        return importedImage;
    }

    public void setImportedImage(ImageIcon importedImage) {
        this.importedImage = importedImage;
    }
}
