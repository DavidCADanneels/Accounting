package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.BalancesIO;
import be.dafke.BusinessModelDao.PDFCreator;
import org.apache.fop.apps.FOPException;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesMenu extends JMenu {
    private JMenuItem manage;
    private JMenuItem pdfGeneration;
    private Journals journals;
    private Accounts accounts;
    private Balances balances;
    private Accounting accounting;
    private AccountTypes accountTypes;
    private JournalInputGUI journalInputGUI;

    public BalancesMenu(JournalInputGUI journalInputGUI){
        super(getBundle("BusinessModel").getString("BALANCES"));
        this.journalInputGUI = journalInputGUI;
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"));
        manage.addActionListener(e -> BalancesManagementGUI.showBalancesManager(balances, accounts, accountTypes));
        pdfGeneration = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"));
        pdfGeneration.addActionListener(e -> {
            File xmlFolder = Main.getXmlFolder();
            File accountingsFolder = new File(xmlFolder, "Accountings");
            File accountingFolder = new File(accountingsFolder, accounting.getName());
            BalancesIO.writeBalances(balances,accountingFolder);
            try {
                String resultXmlPath = "data/accounting/xml/Accountings/"+accounting.getName()+"/Balances/ResultBalance.xml";
                String yearXmlPath = "data/accounting/xml/Accountings/"+accounting.getName()+"/Balances/YearBalance.xml";
                String relationsXmlPath = "data/accounting/xml/Accountings/"+accounting.getName()+"/Balances/RelationsBalance.xml";
                String xslPath = "data/accounting/xsl/BalancePdf.xsl";
                String resultPdfPath = "data/accounting/xml/Accountings/"+accounting.getName()+"/Balances/ResultBalance.pdf";
                String yearPdfPath = "data/accounting/xml/Accountings/"+accounting.getName()+"/Balances/YearBalance.pdf";
                String relationsPdfPath = "data/accounting/xml/Accountings/"+accounting.getName()+"/Balances/RelationsBalance.pdf";
                PDFCreator.convertToPDF(resultXmlPath, xslPath, resultPdfPath);
                PDFCreator.convertToPDF(yearXmlPath, xslPath, yearPdfPath);
                PDFCreator.convertToPDF(relationsXmlPath, xslPath, relationsPdfPath);
            } catch (IOException | FOPException | TransformerException ex) {
                ex.printStackTrace();
            }
        });
        add(manage);
        add(pdfGeneration);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        journals = accounting==null?null:accounting.getJournals();
        accounts = accounting==null?null:accounting.getAccounts();
        balances = accounting==null?null:accounting.getBalances();
        accountTypes = accounting==null?null:accounting.getAccountTypes();

        fireBalancesChanged();
    }

    public void fireBalancesChanged(){
        removeAll();
        if(balances!=null) {
            balances.getBusinessObjects().stream().forEach(balance -> {
                String name = balance.getName();
                JMenuItem item = new JMenuItem(name);
                item.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(name), journalInputGUI));
                add(item);
            });
            add(manage);
            add(pdfGeneration);
        }
    }
}
