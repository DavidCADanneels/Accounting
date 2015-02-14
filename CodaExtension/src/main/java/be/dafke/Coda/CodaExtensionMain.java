package be.dafke.Coda;

import be.dafke.BasicAccounting.BasicAccountingMain;
import be.dafke.BasicAccounting.Objects.Accounting;

/**
 * User: david
 * Date: 28-12-13
 * Time: 14:41
 */
public class CodaExtensionMain extends BasicAccountingMain {

    public static void main(String[] args) {
        startReadingXmlFile();
        createBasicComponents();

        applyExtensions();

        continueReadingXmlFile();
        composeContentPanel();
        composeFrames();
        launch();
    }

    public static void applyExtensions(){
        for(Accounting accounting: accountings.getBusinessObjects()){
            CodaExtension codaExtension = new CodaExtension(accountings, menuBar);
            accounting.addExtension(codaExtension);
        }
    }
}
