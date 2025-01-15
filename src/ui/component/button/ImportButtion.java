package ui.component.button;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

public abstract class ImportButtion extends ActionButton{
    Component parentComponent;
    JFileChooser fileChooser;
    File importedFile;
    JLabel showerLabel;
    public ImportButtion(Component parent,JLabel label) {
        super("Parcourir...");
        setParentComponent(parent);
        setFileChooser(new JFileChooser());
        setShowerLabel(label);
    }
    public Component getParentComponent() {
        return parentComponent;
    }
    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public JFileChooser getFileChooser() {
        return fileChooser;
    }
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
    public JLabel getShowerLabel() {
        return showerLabel;
    }
    public void setShowerLabel(JLabel showerLabel) {
        this.showerLabel = showerLabel;
    }
    public File getImportedFile() {
        return importedFile;
    }
    public void setImportedFile(File file) {
        this.importedFile = file;
    }

    public File importFile(){
        int result = getFileChooser().showOpenDialog(getParentComponent());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            setImportedFile(selectedFile);
            return importedFile;
        }
        return null;
    }
}
