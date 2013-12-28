package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.GUI.BasicAccountingMain;
import be.dafke.Coda.Objects.CodaExtension;

/**
 * User: david
 * Date: 28-12-13
 * Time: 14:41
 */
public class CodaExtensionMain extends BasicAccountingMain {

    public static void main(String[] args) {
//        doIt();
        createAccountings();
        createComponents();
        extensions();
        getAccountings();
        getFrame();
        composePanel();
        completeFrame();
        launch();
    }

    public static void extensions(){
        accountings.addExtension(new CodaExtension(actionListener, menuBar));
    }


}
