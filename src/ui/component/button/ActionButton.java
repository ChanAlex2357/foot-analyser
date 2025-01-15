package ui.component.button;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public abstract class ActionButton extends JButton implements ActionListener{
    public ActionButton(String title){
        super(title);
        this.addActionListener(this);
    }
}
