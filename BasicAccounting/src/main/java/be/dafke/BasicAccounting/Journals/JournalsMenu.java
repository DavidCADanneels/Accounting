package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.JournalsIO;
import be.dafke.BusinessModelDao.PDFCreator;
import org.apache.fop.apps.FOPException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.transform.TransformerException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import static be.dafke.BasicAccounting.Journals.JournalManagementGUI.showJournalManager;
import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class JournalsMenu extends JMenu {
    private Accounting accounting;
    private JMenuItem add, manage, types, generatePdf;

    private Journals journals;
    private JournalTypes journalTypes;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private JournalInputGUI journalInputGUI;
    private Journal transactions;

    public JournalsMenu(JournalInputGUI journalInputGUI) {
        super(getBundle("Accounting").getString("JOURNALS"));
        this.journalInputGUI = journalInputGUI;
//        setMnemonic(KeyEvent.VK_P);
        add = new JMenuItem(getBundle("Accounting").getString("ADD_JOURNAL"));
        add.addActionListener(e -> NewJournalGUI.getInstance(accounts, journals, journalTypes, accountTypes).setVisible(true));
        add.setEnabled(false);

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNALS"));
        manage.addActionListener(e -> showJournalManager(accounts, journals, journalTypes, accountTypes).setVisible(true));
        manage.setEnabled(false);

        types = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        types.addActionListener(e -> showJournalTypeManager(accounts, journalTypes,accountTypes));
        types.setEnabled(false);

        generatePdf = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"));
        generatePdf.addActionListener(e -> {
            File xmlFolder = Main.getXmlFolder();
            File accountingsFolder = new File(xmlFolder, "Accountings");
            File accountingFolder = new File(accountingsFolder, accounting.getName());
            JournalsIO.writeJournals(accounting.getJournals(), accountingFolder);
            String journalsFolderPath = "data/accounting/xml/Accountings/" + accounting.getName() + "/Journals/";
            String xslPath = "data/accounting/xsl/JournalPdf.xsl";
            String resultPdfPolderPath = "data/accounting/xml/Accountings/" + accounting.getName() + "/Balances/";
            journals.getBusinessObjects().forEach(journal -> {
                try {
                    PDFCreator.convertToPDF(journalsFolderPath + journal.getName() + ".xml", xslPath, resultPdfPolderPath + journal.getName() + ".pdf");
                } catch (IOException | FOPException | TransformerException e1) {
                    e1.printStackTrace();
                }
            });
        });
        generatePdf.setEnabled(false);

        add(add);
        add(manage);
        add(types);
        add(generatePdf);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setJournals(accounting==null?null:accounting.getJournals());
        transactions = accounting==null?null:accounting.getTransactions();
        journalTypes = accounting==null?null:accounting.getJournalTypes();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
        accounts = accounting==null?null:accounting.getAccounts();
        add.setEnabled(journals!=null);
        manage.setEnabled(journals!=null);
        types.setEnabled(journals!=null);
        generatePdf.setEnabled(journals!=null);
        fireJournalDataChanged();
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
        fireJournalDataChanged();
    }

    public void fireJournalDataChanged(){
        removeAll();
        add(add);
        add(manage);
        add(types);
        add(generatePdf);
        if(journals!=null){
            addSeparator();
            journals.getBusinessObjects().stream()
                    .forEach(journal -> {
                        JMenuItem details = new JMenuItem(journal.getName());
                        details.addActionListener(e -> JournalDetails.getJournalDetails(journal,journals,journalInputGUI));
                        add(details);
                    });
            addSeparator();
            JMenuItem master = new JMenuItem("Master");
            master.addActionListener(e -> JournalDetails.getJournalDetails(transactions,journals,journalInputGUI));
            add(master);
        }
    }
}
