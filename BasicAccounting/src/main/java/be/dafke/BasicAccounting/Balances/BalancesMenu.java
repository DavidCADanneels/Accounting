package be.dafke.BasicAccounting.Balances;

import be.dafke.Accounting.BusinessModel.*;
import be.dafke.BusinessModelDao.BalancesIO;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class BalancesMenu extends JMenu {
    private JMenuItem manage;
    private JMenuItem pdfGeneration;
    private Journals journals;
    private Accounts accounts;
    private Balances balances;
    private Accounting accounting;
    private AccountTypes accountTypes;

    public BalancesMenu(){
        super(getBundle("BusinessModel").getString("BALANCES"));
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"));
        manage.addActionListener(e -> {
            BalancesManagementGUI balancesManagementGUI = BalancesManagementGUI.getInstance(balances, accounts, accountTypes);
            balancesManagementGUI.setLocation(getLocationOnScreen());
            balancesManagementGUI.setVisible(true);
        });
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
            balances.getBusinessObjects().forEach(balance -> {
                String name = balance.getName();
                JMenuItem item = new JMenuItem(name);
                item.addActionListener(e -> {
                    BalanceGUI balanceGUI = BalanceGUI.getBalance(accounting, balances.getBusinessObject(name));
                    balanceGUI.setLocation(getLocationOnScreen());
                    balanceGUI.setVisible(true);
                });
                add(item);
            });
            add(manage);
            add(pdfGeneration);
        }
    }
}
