package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Contacts.ContactsSettingsPanel
import be.dafke.Accounting.BasicAccounting.Meals.MealsSettingsPanel
import be.dafke.Accounting.BasicAccounting.Trade.TradeSettingsPanel
import be.dafke.Accounting.BasicAccounting.VAT.VATSettingsPanel
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class AccountingSettingsPanel extends JTabbedPane {
    static final String TRADE = getBundle("Accounting").getString("TRADE")
    static final String VAT = getBundle("VAT").getString("VAT")
    static final String CONTACTS = getBundle("Contacts").getString("CONTACTS")
    static final String PROJECTS = getBundle("Projects").getString("PROJECTS")
    static final String MEALS = getBundle("Accounting").getString("MEALS")
    static final String MORTGAGES = getBundle("Mortgage").getString("MORTGAGES")
    static final int CONTACTS_INDEX = 1
    static final int VAT_INDEX = 2
    static final int TRADE_INDEX = 3
    static final int MEALS_INDEX = 3 // or 4 if Trade is active
    private JCheckBox vatAccounting
    private JCheckBox tradeAccounting
    private JCheckBox contacts
    private JCheckBox projects
    private JCheckBox meals
    private JCheckBox mortgages
    private Accounting accounting
    private ContactsSettingsPanel contactsTab
    private VATSettingsPanel vatTab
    private TradeSettingsPanel tradeTab
    private MealsSettingsPanel mealsTab
    private AccountingCopyPanel copyPanel

    AccountingSettingsPanel(Accounting accounting, AccountingCopyPanel copyPanel) {
        this.accounting = accounting
        this.copyPanel = copyPanel

        setTabPlacement(JTabbedPane.TOP)
        JComponent mainPanel = createCenterPanel()
        addTab("Modules", mainPanel)

        contactsTab = new ContactsSettingsPanel(accounting)
        vatTab = new VATSettingsPanel(accounting)
        tradeTab = new TradeSettingsPanel(accounting)
        mealsTab = new MealsSettingsPanel(accounting)

        updateProjectSetting()
        updateMortgageSetting()
        updateContactSetting()
        updateVatSetting()
        updateTradeSetting()
        updateMealsSetting()
    }

    void setContactsSelected(boolean selected){
        contacts.setSelected(selected)
        updateContactSetting()
    }

    void setVatSelected(boolean selected) {
        vatAccounting.setSelected(selected)
        updateVatSetting()
    }

    void setMealsSelected(boolean selected) {
        meals.setSelected(selected)
        updateMealsSetting()
    }

    void setTradeSelected(boolean selected) {
        tradeAccounting.setSelected(selected)
        updateTradeSetting()
    }

    private void updateContactSetting(){
        boolean selected = contacts.isSelected()
        contactsTab.setEnabled(selected)
        if(!selected){
            accounting.setCompanyContact(null)
            setVatSelected(false)
//            vatAccounting.setSelected(false)
//            updateVatSetting()
            int indexOfComponent = indexOfComponent(contactsTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        } else {
            insertTab("Contacts", null, contactsTab, "", CONTACTS_INDEX)
        }
        accounting.setContactsAccounting(selected)
        if(copyPanel!=null){
            copyPanel.enableCopyContacts()
        }
        Main.fireAccountingTypeChanged(accounting)
    }

    private void updateVatSetting(){
        boolean selected = vatAccounting.isSelected()
        vatTab.setEnabled(selected)
        if(selected) {
            setContactsSelected(true)
//            contacts.setSelected(true)
//            updateContactSetting()
            insertTab("VAT", null, vatTab, "", VAT_INDEX)
        } else {
            setTradeSelected(false)
//            tradeAccounting.setSelected(false)
//            updateTradeSetting()
            setMealsSelected(false)
//            meals.setSelected(false)
//            updateMealsSetting()
            int indexOfComponent = indexOfComponent(vatTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        }
        accounting.setVatAccounting(selected)
        if(copyPanel!=null){
            copyPanel.enableCopyVat()
        }
        Main.fireAccountingTypeChanged(accounting)
    }

    private void updateTradeSetting(){
        boolean selected = tradeAccounting.isSelected()
        tradeTab.setEnabled(selected)
        if(selected){
            setVatSelected(true)
//            vatAccounting.setSelected(true)
//            updateVatSetting()
            insertTab("Trade", null, tradeTab, "", TRADE_INDEX)
        } else {
            int indexOfComponent = indexOfComponent(tradeTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        }
        accounting.setTradeAccounting(selected)
        if(copyPanel!=null){
            copyPanel.enableCopyTrade()
        }
        Main.fireAccountingTypeChanged(accounting)
    }

    private void updateMealsSetting(){
        boolean selected = meals.isSelected()
        mealsTab.setEnabled(selected)
        if(selected){
            vatAccounting.setSelected(true)
            updateVatSetting()
            int index = MEALS_INDEX
            if(tradeAccounting.isSelected()){
                index++
            }
            // TODO: if tab for Mortgages is available, raise index as well
//            if(mortgages.isSelected()){
//                index++
//            }
            insertTab("Meals", null, mealsTab, "", index)
        } else {
            int indexOfComponent = indexOfComponent(mealsTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        }
        accounting.setMealsAccounting(selected)
        if(copyPanel!=null){
            copyPanel.enableCopyMeals()
        }
        Main.fireAccountingTypeChanged(accounting)
    }

    private void updateMortgageSetting(){
        accounting.setMortgagesAccounting(mortgages.isSelected())
        Main.fireAccountingTypeChanged(accounting)
    }

    private void updateProjectSetting(){
        accounting.setProjectsAccounting(projects.isSelected())
        Main.fireAccountingTypeChanged(accounting)
    }

    private JComponent createCenterPanel(){
        JPanel panel = new JPanel()

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS))
        projects = new JCheckBox(PROJECTS)
        mortgages = new JCheckBox(MORTGAGES)
        contacts = new JCheckBox(CONTACTS)
        vatAccounting = new JCheckBox(VAT)
        tradeAccounting = new JCheckBox(TRADE)
        meals = new JCheckBox(MEALS)

        projects.setSelected(accounting==null||accounting.isProjectsAccounting())
        mortgages.setSelected(accounting==null||accounting.isMortgagesAccounting())
        contacts.setSelected(accounting==null||accounting.isContactsAccounting())
        vatAccounting.setSelected(accounting==null||accounting.isVatAccounting())
        tradeAccounting.setSelected(accounting==null||accounting.isTradeAccounting())
        meals.setSelected(accounting==null||accounting.isMealsAccounting())

        projects.addActionListener({ e -> updateProjectSetting() })
        mortgages.addActionListener({ e -> updateMortgageSetting() })
        contacts.addActionListener({ e -> updateContactSetting() })
        vatAccounting.addActionListener({ e -> updateVatSetting() })
        tradeAccounting.addActionListener({ e -> updateTradeSetting() })
        meals.addActionListener({ e -> updateMealsSetting() })

        panel.add(projects)
        panel.add(mortgages)
        panel.add(contacts)
        panel.add(vatAccounting)
        panel.add(tradeAccounting)
        panel.add(meals)

        if(copyPanel==null) {
            panel
        } else {
            Main.createSplitPane(panel, copyPanel, JSplitPane.HORIZONTAL_SPLIT)
        }
    }

    void copyContacts(Accounting copyFrom) {
        contactsTab.copyContacts(copyFrom)
    }

    void copyVatSettings(Accounting copyFrom) {
        vatTab.copyVatSettings(copyFrom)
    }

    void copyTradeSettings(Accounting copyFrom) {
        tradeTab.copyTradeSettings(copyFrom)
    }

    void copyMealSettings(Accounting copyFrom) {
        mealsTab.copyMealOrderSettings(copyFrom)
    }
}
