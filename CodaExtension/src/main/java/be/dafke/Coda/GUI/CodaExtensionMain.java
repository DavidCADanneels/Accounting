package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.GUI.BasicAccountingMain;
import be.dafke.BasicAccounting.Objects.Accounting;
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
        readBasicXmlFile();
        extensions();
        readXmlFile();
        extensions2();
        getFrame();
        composePanel();
        completeFrame();
        launch();
    }

    public static void extensions(){
        for(Accounting accounting: accountings.getBusinessObjects()){
            CodaExtension codaExtension = new CodaExtension(actionListener, menuBar);
            accounting.addExtension(codaExtension);
        }
    }

    protected static void extensions2(){

    }
}
