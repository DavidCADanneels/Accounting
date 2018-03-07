package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.util.ResourceBundle.getBundle;

public class JournalTypeManagementPanel extends JPanel implements ActionListener {
    private JComboBox<JournalType> combo;
    private JournalTypes journalTypes;
    private JournalType journalType;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private JComboBox<VATTransaction.VATType> taxType;
    //	private JPanel left, right;
    private AccountsListConfigPanel accountsListConfigPanelLeft, accountsListConfigPanelRight;
//	private AccountsListConfigPanel left, right;


    public JournalTypeManagementPanel(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        this.journalTypes = journalTypes;

        setLayout(new BorderLayout());
        add(createCenterPanel(), CENTER);
        add(createSavePanel(), NORTH);
        setJournalTypes(journalTypes);
//		comboAction();
    }

    private JPanel createSavePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,0));
        JButton newType = new JButton(getBundle("Accounting").getString("NEW_JOURNAL_TYPE"));
        newType.addActionListener(e -> createNewJournalType());
        combo = new JComboBox<>();
        combo.addActionListener(e -> comboAction());
        taxType = new JComboBox<>();
        taxType.addItem(null);
        taxType.addItem(VATTransaction.VATType.PURCHASE);
        taxType.addItem(VATTransaction.VATType.SALE);
        taxType.setSelectedItem(null);
        taxType.addActionListener(this);
        JButton switchButton = new JButton("Switch VAT Types");
        switchButton.addActionListener(e -> {
            journalType.switchVatTypes();
            accountsListConfigPanelLeft.setVatType(journalType.getLeftVatType());
            accountsListConfigPanelRight.setVatType(journalType.getRightVatType());
        });

        panel.add(new JLabel("Selected JournalType:"));
        panel.add(combo);
        panel.add(newType);

        panel.add(new JLabel("Journal VATType:"));
        panel.add(taxType);
        panel.add(switchButton);

        return panel;
    }

    public void setJournalTypes(JournalTypes journalTypes){
        this.journalTypes = journalTypes;
        combo.removeAllItems();
        for(JournalType type : journalTypes.getBusinessObjects()){
            ((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(type);
        }
    }

    private void switchVatType(){
        VATTransaction.VATType vatType = (VATTransaction.VATType) taxType.getSelectedItem();
        journalType.setVatType(vatType);
        VATTransaction.VATType vatTypeLeft = JournalType.calculateLeftVatType(vatType);
        VATTransaction.VATType vatTypeRight = JournalType.calculateRightVatType(vatType);
        journalType.getLeft().setVatType(vatTypeLeft);
        journalType.getRight().setVatType(vatTypeRight);
        accountsListConfigPanelLeft.setVatType(vatTypeLeft);
        accountsListConfigPanelRight.setVatType(vatTypeRight);
    }

    private void comboAction() {
        journalType = (JournalType) combo.getSelectedItem();
        taxType.removeActionListener(this);
        taxType.setSelectedItem(journalType.getVatType());
        taxType.addActionListener(this);
        AccountsList leftList = journalType.getLeft();
        accountsListConfigPanelLeft.setAccountsList(leftList);
        accountsListConfigPanelLeft.setJournalType(journalType);
        accountsListConfigPanelLeft.setVatType(leftList.getVatType());
        AccountsList rightList = journalType.getRight();
        accountsListConfigPanelRight.setAccountsList(rightList);
        accountsListConfigPanelRight.setJournalType(journalType);
        accountsListConfigPanelRight.setVatType(rightList.getVatType());
        accountsListConfigPanelLeft.refresh();
        accountsListConfigPanelRight.refresh();
    }

    public void createNewJournalType(){
        String name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
        while (name != null) {
            name = name.trim();
            JournalType journalType = new JournalType(name);
            journalType.addAllAccountTypes(accountTypes);
            try {
                journalTypes.addBusinessObject(journalType);
                JournalManagementGUI.fireJournalTypeDataChangedForAll(journalTypes);
                ((DefaultComboBoxModel<JournalType>) combo.getModel()).addElement(journalType);
                (combo.getModel()).setSelectedItem(journalType);
                name = null;
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.JOURNAL_TYPE_DUPLICATE_NAME, name);
                name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.JOURNAL_TYPE_NAME_EMPTY);
                name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
            }
        }
    }

    public JPanel createCenterPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));

//		panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("ACCOUNTTYPES")));;
        accountsListConfigPanelLeft = new AccountsListConfigPanel(accounts, accountTypes, true);
        JPanel left = new JPanel();
        left.add(accountsListConfigPanelLeft);
        left.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("LEFT")));
        panel.add(left);
        accountsListConfigPanelRight = new AccountsListConfigPanel(accounts, accountTypes, false);
        JPanel right = new JPanel();
        right.add(accountsListConfigPanelRight);
        right.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("RIGHT")));

        panel.add(right);


        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(combo == e.getSource()){

        } else if (taxType == e.getSource()){
            switchVatType();
        }
    }
}
