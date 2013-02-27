package be.dafke.Accounting.Objects.Accounting;

import java.io.File;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances {
    private File balanceLocationXML;
    private File balanceLocationHTML;

    public void setBalanceLocationHtml(File location) {
        balanceLocationHTML = location;
        balanceLocationHTML.mkdir();
    }

    public void setBalanceLocationXml(File location) {
        balanceLocationXML = location;
        balanceLocationXML.mkdir();
    }

    public File getBalanceLocationHtml() {
        return balanceLocationHTML;
    }

    public File getBalanceLocationXml() {
        return balanceLocationXML;
    }
}
