package be.dafke.Project;

import be.dafke.BasicAccounting.BasicAccountingMain;

/**
 * User: david
 * Date: 30-12-13
 * Time: 10:15
 */
public class ProjectExtensionMain extends BasicAccountingMain {
    public static void main(String[] args) {
        readXmlData();
        createBasicComponents();

//        applyExtensions();

        continueReadingXmlFile();
        composeContentPanel();
        composeFrames();
        launch();
    }

}
