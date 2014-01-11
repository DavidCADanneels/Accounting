package be.dafke.ComponentModel;

import javax.swing.*;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 13:18
 */
public abstract class RefreshableDialog extends JDialog implements RefreshableComponent{
    public RefreshableDialog(String title){
        setTitle(title);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
