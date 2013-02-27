package be.dafke.Accounting.Objects.Accounting;

import java.io.File;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances {
    private File xmlFolder;
    private File htmlFolder;

    public void setHtmlFolder(File htmlFolder) {
        this.htmlFolder = htmlFolder;
    }

    public void setXmlFolder(File xmlFolder) {
        this.xmlFolder = xmlFolder;
    }

    public File getHtmlFolder() {
        return htmlFolder;
    }

    public File getXmlFolder() {
        return xmlFolder;
    }
}
