package be.dafke.BasicAccounting.VAT;

import javax.swing.*;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATGUI extends JFrame {
    private static VATGUI instance = null;

    public static VATGUI getInstance() {
        if(instance==null){
            instance = new VATGUI();
        }
        return instance;
    }
}
