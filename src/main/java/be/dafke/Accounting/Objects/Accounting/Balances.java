package be.dafke.Accounting.Objects.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends HashMap<String, Balance>{
    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";
    private File xmlFile;
    private File xsl2XmlFile;
    private File xsl2HtmlFile;
    private File dtdFile;
    private File htmlFile;

    public void addDefaultBalances(Accounting accounting){
        ArrayList<Account.AccountType> costs = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> revenues = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> credit = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> debit = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> active = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> passive = new ArrayList<Account.AccountType>();
        costs.add(Account.AccountType.Cost);
        revenues.add(Account.AccountType.Revenue);
        credit.add(Account.AccountType.Credit);
        debit.add(Account.AccountType.Debit);
        active.add(Account.AccountType.Active);
        active.add(Account.AccountType.Credit);
        passive.add(Account.AccountType.Passive);
        passive.add(Account.AccountType.Debit);
        put(RESULT_BALANCE, new Balance(RESULT_BALANCE,
                getBundle("Accounting").getString("KOSTEN"), getBundle("Accounting").getString("OPBRENGSTEN"),
                getBundle("Accounting").getString("TOTAAL_KOSTEN"), getBundle("Accounting").getString("TOTAAL_OPBRENGSTEN"),
                getBundle("Accounting").getString("VERLIES"), getBundle("Accounting").getString("WINST"),
                costs, revenues,
                accounting));
        put(RELATIONS_BALANCE, new Balance(RELATIONS_BALANCE,
                getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"), getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS"),
                getBundle("Accounting").getString("TOTAAL_TEGOEDEN"), getBundle("Accounting").getString("TOTAAL_SCHULDEN"),
                getBundle("Accounting").getString("RESTEREND_TEGOED"), getBundle("Accounting").getString("RESTERENDE_SCHULD"),
                credit, debit,
                accounting));
        put(YEAR_BALANCE, new Balance(YEAR_BALANCE,
                getBundle("Accounting").getString("ACTIVA"), getBundle("Accounting").getString("PASSIVA"),
                getBundle("Accounting").getString("TOTAAL_ACTIVA_TEGOEDEN"), getBundle("Accounting").getString("TOTAAL_PASSIVA_SCHULDEN"),
                getBundle("Accounting").getString("WINST"), getBundle("Accounting").getString("VERLIES"),
                active, passive,
                accounting));
    }

    public Collection<Balance> getBalances() {
        return values();
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXsl2XmlFile(File xsl2XmlFile) {
        this.xsl2XmlFile = xsl2XmlFile;
    }

    public File getXsl2XmlFile() {
        return xsl2XmlFile;
    }

    public File getXsl2HtmlFile() {
        return xsl2HtmlFile;
    }

    public void setXsl2HtmlFile(File xsl2HtmlFile) {
        this.xsl2HtmlFile = xsl2HtmlFile;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public void setDefaultHtmlFolderAndFiles(Accounting accounting, String name, boolean overwrite){
        File htmlFolder = accounting.getHtmlFolder();
        if(overwrite || htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name + ".html");
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(htmlFolder, name);
        subFolder.mkdirs();
        for(Balance balance: getBalances()){
            balance.setHtmlFile(FileSystemView.getFileSystemView().getChild(subFolder, balance.getName() + ".html"));
        }
    }

    public void setDefaultXmlFolderAndFiles(Accounting accounting, String name, boolean overwrite) {
        File xmlFolder = accounting.getXmlFolder();
        File xslFolder = accounting.getXslFolder();
        File dtdFolder = accounting.getDtdFolder();
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, name + ".xml");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balances2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balances2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(dtdFolder, "Balances.dtd");
        }
        File subFolder = FileSystemView.getFileSystemView().getChild(xmlFolder, name);
        subFolder.mkdirs();
        for(Balance balance: getBalances()){
            balance.setDefaultFiles(subFolder,xslFolder, dtdFolder);
        }
    }
}
