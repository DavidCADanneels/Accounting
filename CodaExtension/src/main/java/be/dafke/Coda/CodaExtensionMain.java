package be.dafke.Coda;

import be.dafke.BasicAccounting.BasicAccountingMain;

/**
 * User: david
 * Date: 28-12-13
 * Time: 14:41
 */
public class CodaExtensionMain extends BasicAccountingMain {

    public static void main(String[] args) {
        readXmlData();
        createBasicComponents();
        composeContentPanel();
        composeFrames();
        launch();
    }
}
