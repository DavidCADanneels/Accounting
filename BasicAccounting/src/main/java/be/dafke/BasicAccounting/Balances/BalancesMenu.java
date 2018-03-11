package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.BalancesIO;

import javax.swing.*;

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
    private JournalEditPanel journalEditPanel;

    public BalancesMenu(JournalEditPanel journalEditPanel){
        super(getBundle("BusinessModel").getString("BALANCES"));
        this.journalEditPanel = journalEditPanel;
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"));
        manage.addActionListener(e -> BalancesManagementGUI.showBalancesManager(balances, accounts, accountTypes));
        pdfGeneration = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"));
        pdfGeneration.addActionListener(e -> BalancesIO.writeBalancePdfFiles(accounting));
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
                item.addActionListener(e -> BalanceGUI.getBalance(journals, balances.getBusinessObject(name)));
                add(item);
            });
            add(manage);
            add(pdfGeneration);
        }
    }
}
