package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

public class AccountingCopyPanel extends JPanel {

    private final JCheckBox copyAccounts, copyJournals, copyContacts, copyVat, copyTrade, copyDeliveroo;
    private AccountingSettingsPanel accountingSettingsPanel;
    private Accounting copyFrom;
    private Accounting newAccounting;

    public AccountingCopyPanel(){
        newAccounting = createAccounting();
        copyAccounts = new JCheckBox("copy Accounts");
        copyJournals = new JCheckBox("copy Journals");
        copyContacts = new JCheckBox("copy Contacts");
        copyVat = new JCheckBox("copy VAT Settings");
        copyTrade = new JCheckBox("copy Trade Settings");
        copyDeliveroo = new JCheckBox("copy Deliveroo Settings");

        copyAccounts.addActionListener(e -> updateCopyAccountsSelected());
        copyJournals.addActionListener(e -> updateCopyJournalsSelected());
        copyContacts.addActionListener(e -> updateCopyContactsSelected());
        copyVat.addActionListener(e -> updateCopyVATSelected());
        copyTrade.addActionListener(e -> updateCopyTradeSelected());
        copyDeliveroo.addActionListener(e -> updateCopyDeliverooSelected());

        copyAccounts.setEnabled(false);
        copyJournals.setEnabled(false);
        copyContacts.setEnabled(false);
        copyVat.setEnabled(false);
        copyTrade.setEnabled(false);
        copyDeliveroo.setEnabled(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(copyAccounts);
        panel.add(copyJournals);
        panel.add(copyContacts);
        panel.add(copyVat);
        panel.add(copyTrade);
        panel.add(copyDeliveroo);

        add(panel);
    }

    // SET

    public Accounting getAccounting(){
        return newAccounting;
    }

    public void setSettingsPanel(AccountingSettingsPanel accountingSettingsPanel) {
        this.accountingSettingsPanel = accountingSettingsPanel;
    }

    public void setCopyFrom(Accounting copyFrom) {
        this.copyFrom = copyFrom;
        selectCopyAccounts(copyAccounts.isSelected());
        selectCopyJournals(copyJournals.isSelected());
        selectCopyContacts(copyContacts.isSelected());
        selectCopyVat(copyVat.isSelected());
        selectCopyTrade(copyTrade.isSelected());
        selectCopyDeliveroo(copyDeliveroo.isSelected());
    }

    // CREATE

    private Accounting createAccounting(){
        Accounting accounting = new Accounting("New Accounting");
        accounting.getAccountTypes().addDefaultTypes();
        accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes());
        accounting.getBalances().addDefaultBalances();
        return accounting;
    }

    // COPY

    public void copyAccounts(){
        newAccounting.copyAccounts(copyFrom.getAccounts());
    }

    public void copyJournals(){
        newAccounting.copyJournals(copyFrom.getJournals());
        newAccounting.copyJournalTypes(copyFrom.getJournalTypes());
    }

    public void copyContacts(){
        newAccounting.copyContacts(copyFrom.getContacts());
    }

    public void copyVatSettings(){
        newAccounting.copyVatSettings(copyFrom.getVatTransactions());
    }

    private void copyArticles() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setName("PO0");

        Articles articlesFrom = copyFrom.getArticles();
        Articles articlesTo = newAccounting.getArticles();
        articlesTo.clear();
        articlesFrom.getBusinessObjects().forEach(article -> {
            Article newArticle = new Article(article, newAccounting.getContacts());
            Integer numberOfItems = article.getNrInStock();
            if(numberOfItems>0){
                OrderItem orderItem = new OrderItem(numberOfItems, newArticle, purchaseOrder);
                purchaseOrder.addBusinessObject(orderItem);
                // TODO: call setPurchaseTransaction (= beginBalans) 'later' iso setPoOrdered
                // setPurchaseTransaction calls setPoOrdered as well
                newArticle.setPoOrdered(numberOfItems);
            }
            try {
                articlesTo.addBusinessObject(newArticle);
            } catch (EmptyNameException e) {
                e.printStackTrace();
            } catch (DuplicateNameException e) {
                e.printStackTrace();
            }
        });
        if (!purchaseOrder.getBusinessObjects().isEmpty()) {
            // TODO: 'later' = here !
//            purchaseOrder.setPurchaseTransaction(beginBalans);
            try {
                PurchaseOrders purchaseOrders = newAccounting.getPurchaseOrders();
                purchaseOrders.addBusinessObject(purchaseOrder);
            } catch (EmptyNameException e) {
                e.printStackTrace();
            } catch (DuplicateNameException e) {
                e.printStackTrace();
            }
            StockTransactions stockTransactions = newAccounting.getStockTransactions();
            stockTransactions.addOrder(purchaseOrder);
        }
    }

    private void copyDeliverooMeals() {
        DeliverooMeals deliverooMealsFrom = copyFrom.getDeliverooMeals();
        DeliverooMeals deliverooMealsTo = newAccounting.getDeliverooMeals();
        deliverooMealsTo.clear();
        deliverooMealsFrom.getBusinessObjects().forEach(deliverooMeal -> {
            DeliverooMeal newMeal = new DeliverooMeal(deliverooMeal);
            try {
                deliverooMealsTo.addBusinessObject(newMeal);
            } catch (EmptyNameException e){
                e.printStackTrace();
            } catch (DuplicateNameException e) {
                e.printStackTrace();
            }
        });
    }

    // UPDATE

    private void updateCopyAccountsSelected() {
        boolean copyAccountsSelected = copyAccounts.isSelected();
        if(copyAccountsSelected){
            copyAccounts();
        } else {
            selectCopyContacts(false);
        }
    }

    private void updateCopyJournalsSelected() {
        boolean copyJournalsSelected = copyJournals.isSelected();
        if(copyJournalsSelected){
            copyJournals();
        } else {

        }
    }

    private void updateCopyContactsSelected() {
        boolean enabled = copyFrom.isContactsAccounting();
        boolean selected = enabled && copyContacts.isSelected();
        if(selected){
            selectCopyAccounts(true);
            accountingSettingsPanel.setContactsSelected(true);
            copyContacts();
            if(copyFrom!=null){
                accountingSettingsPanel.copyContacts(copyFrom);
            } else {
                accountingSettingsPanel.copyContacts(null);
            }
        } else {
//            accountingSettingsPanel.setVatSelected(false);
            selectCopyVat(false);
            accountingSettingsPanel.copyContacts(null);
        }
    }

    private void updateCopyVATSelected() {
        boolean selected = copyVat.isSelected();
        if(selected){
            selectCopyAccounts(true);
            selectCopyJournals(true);
            selectCopyContacts(true);
            accountingSettingsPanel.setVatSelected(true);
            copyVatSettings();
            if(copyFrom!=null){
                accountingSettingsPanel.copyVatSettings(copyFrom);
            } else {
                accountingSettingsPanel.copyVatSettings(null);
            }
        } else {
            selectCopyTrade(false);
            selectCopyDeliveroo(false);
            accountingSettingsPanel.copyVatSettings(null);
        }
    }

    private void updateCopyTradeSelected() {
        boolean selected = copyTrade.isSelected();
        if(selected){
            selectCopyVat(true);
            accountingSettingsPanel.setTradeSelected(true);
            if(copyFrom!=null){
                // TODO: need separate check box to copy Articles?
                accountingSettingsPanel.copyTradeSettings(copyFrom);
                copyArticles();
            } else {
                accountingSettingsPanel.copyTradeSettings(null);
            }
        } else {
            accountingSettingsPanel.copyTradeSettings(null);
        }

    }

    private void updateCopyDeliverooSelected() {
        boolean selected = copyDeliveroo.isSelected();
        if(selected){
            selectCopyVat(true);
            accountingSettingsPanel.setDeliveooSelected(true);
            if(copyFrom!=null){
                // TODO: need separate check box to copy Meals?
                copyDeliverooMeals();
                accountingSettingsPanel.copyDeliverooSettings(copyFrom);
            } else {
                accountingSettingsPanel.copyDeliverooSettings(null);
            }
        } else {
            accountingSettingsPanel.copyDeliverooSettings(null);
        }
    }

    // SELECT

    public void selectCopyAccounts(boolean selected){
        boolean enabled = copyFrom != null;
        copyAccounts.setEnabled(enabled);
        copyAccounts.setSelected(enabled && selected);
        updateCopyAccountsSelected();
    }

    public void selectCopyJournals(boolean selected){
        boolean enabled = copyFrom != null;
        copyJournals.setEnabled(enabled);
        copyJournals.setSelected(enabled && selected);
        updateCopyJournalsSelected();
    }

    public void selectCopyContacts(boolean selected){
        boolean enabled = enableCopyContacts();
        copyContacts.setSelected(enabled && selected);
        updateCopyContactsSelected();
    }

    public void selectCopyVat(boolean selected) {
        boolean enabled = enableCopyVat();
        copyVat.setSelected(enabled && selected);
        updateCopyVATSelected();
    }

    public void selectCopyTrade(boolean selected) {
        boolean enabled = enableCopyTrade();
        copyTrade.setSelected(enabled && selected);
        updateCopyTradeSelected();
    }

    public void selectCopyDeliveroo(boolean selected) {
        boolean enabled = enableCopyDeliveroo();
        copyDeliveroo.setSelected(enabled && selected);
        updateCopyDeliverooSelected();
    }

    // ENABLE

    public boolean enableCopyContacts() {
        boolean enabled = copyFrom!=null && copyFrom.isContactsAccounting() && newAccounting.isContactsAccounting();
        copyContacts.setEnabled(enabled);
        if(!enabled) copyContacts.setSelected(false);
        return enabled;
    }

    public boolean enableCopyVat() {
        boolean enabled = copyFrom!=null && copyFrom.isVatAccounting() && newAccounting.isVatAccounting();
        copyVat.setEnabled(enabled);
        if(!enabled) copyVat.setSelected(false);
        return enabled;
    }

    public boolean enableCopyTrade() {
        boolean enabled = copyFrom!=null && copyFrom.isTradeAccounting() && newAccounting.isTradeAccounting();
        copyTrade.setEnabled(enabled);
        if(!enabled) copyTrade.setSelected(false);
        return enabled;
    }

    public boolean enableCopyDeliveroo() {
        boolean enabled = copyFrom!=null && copyFrom.isDeliverooAccounting() && newAccounting.isDeliverooAccounting();
        copyDeliveroo.setEnabled(enabled);
        if(!enabled) copyDeliveroo.setSelected(false);
        return enabled;
    }

}
