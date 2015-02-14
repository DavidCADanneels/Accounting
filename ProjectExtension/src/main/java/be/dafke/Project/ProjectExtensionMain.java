package be.dafke.Project;

import be.dafke.BasicAccounting.BasicAccountingMain;
import be.dafke.BasicAccounting.Objects.Accounting;

/**
 * User: david
 * Date: 30-12-13
 * Time: 10:15
 */
public class ProjectExtensionMain extends BasicAccountingMain {
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
            accounting.addExtension(new ProjectExtension(menuBar));
        }
    }
}
