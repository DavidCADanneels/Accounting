package be.dafke.Balances;

import be.dafke.BasicAccounting.BasicAccountingMain;
import be.dafke.BasicAccounting.Objects.Accounting;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:05
 */
public class BalancesExtensionMain extends BasicAccountingMain {
    public static void main(String[] args) {
        startReadingXmlFile();
        createBasicComponents();

        applyExtensions();

        continueReadingXmlFile();
        composeContentPanel();
        composeFrames();
        launch();
    }

    protected static void applyExtensions(){
        for(Accounting accounting: accountings.getBusinessObjects()){
            accounting.addExtension(new BalancesExtension(actionListener, menuBar));
        }
    }

}
