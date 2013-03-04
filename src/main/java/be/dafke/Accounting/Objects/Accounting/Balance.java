package be.dafke.Accounting.Objects.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance {

    private final String name;
    private final String leftName;
    private final String rightName;
    private final String leftTotalName;
    private final String rightTotalName;
    private final String leftResultName;
    private final String rightResultName;
    private final ArrayList<Account.AccountType> leftTypes;
    private final ArrayList<Account.AccountType> rightTypes;
    private final Accounting accounting;
    private File xmlFile;
    private File xsl2XmlFile, xsl2HtmlFile;
    private File htmlFile;
    private File dtdFile;

    public Balance(String name,
                   String leftName, String rightName,
                   String leftTotalName, String rightTotalName,
                   String leftResultName, String rightResultName,
                   ArrayList<Account.AccountType> leftTypes, ArrayList<Account.AccountType> rightTypes,
                   Accounting accounting){
        this.name = name;
        this.leftName = leftName;
        this.rightName = rightName;
        this.leftTotalName = leftTotalName;
        this.rightTotalName = rightTotalName;
        this.leftResultName = leftResultName;
        this.rightResultName = rightResultName;
        this.leftTypes = leftTypes;
        this.rightTypes = rightTypes;
        this.accounting = accounting;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public String getLeftName() {
        return leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public ArrayList<Account.AccountType> getLeftTypes() {
        return leftTypes;
    }

    public ArrayList<Account.AccountType> getRightTypes() {
        return rightTypes;
    }

    public ArrayList<Account> getLeftAccounts() {
        return accounting.getAccounts().getAccountsNotEmpty(leftTypes);
    }

    public ArrayList<Account> getRightAccounts() {
        return accounting.getAccounts().getAccountsNotEmpty(rightTypes);
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public String getLeftTotalName() {
        return leftTotalName;
    }

    public String getRightTotalName() {
        return rightTotalName;
    }

    public String getLeftResultName() {
        return leftResultName;
    }

    public String getRightResultName() {
        return rightResultName;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getXsl2XmlFile() {
        return xsl2XmlFile;
    }

    public void setXsl2XmlFile(File xsl2XmlFile) {
        this.xsl2XmlFile = xsl2XmlFile;
    }

    public File getXsl2HtmlFile() {
        return xsl2HtmlFile;
    }

    public void setXsl2HtmlFile(File xsl2HtmlFile) {
        this.xsl2HtmlFile = xsl2HtmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    protected void setDefaultFiles(File subFolder, File xslFolder, File dtdFolder) {
        xmlFile = FileSystemView.getFileSystemView().getChild(subFolder, name + ".xml");
        dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "Balance.dtd");
        xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balance2xml.xsl");
        xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balance2html.xsl");
    }

}
