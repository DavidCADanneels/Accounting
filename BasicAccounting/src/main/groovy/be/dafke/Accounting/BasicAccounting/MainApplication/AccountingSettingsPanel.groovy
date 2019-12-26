package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Contacts.ContactsSettingsPanel
import be.dafke.Accounting.BasicAccounting.Meals.MealsSettingsPanel
import be.dafke.Accounting.BasicAccounting.Trade.TradeSettingsPanel
import be.dafke.Accounting.BasicAccounting.VAT.VATSettingsPanel
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class AccountingSettingsPanel extends JTabbedPane {
    static final String TRADE = getBundle("Accounting").getString("TRADE")
    static final String VAT = getBundle("VAT").getString("VAT")
    static final String CONTACTS = getBundle("Contacts").getString("CONTACTS")
    static final String PROJECTS = getBundle("Projects").getString("PROJECTS")
    static final String MEALS = getBundle("Accounting").getString("MEALS")
    static final String ACCOUNT_NUMBERS = getBundle("Accounting").getString("ACCOUNT_NUMBERS")
    static final String MORTGAGES = getBundle("Mortgage").getString("MORTGAGES")
    static final int CONTACTS_INDEX = 1
    static final int VAT_INDEX = 2
    static final int TRADE_INDEX = 3
    static final int MEALS_INDEX = 3 // or 4 if Trade is active
    JCheckBox vatAccounting
    JCheckBox tradeAccounting
    JCheckBox contacts
    JCheckBox projects
    JCheckBox meals
    JCheckBox mortgages
    JCheckBox accountNumbers
    Accounting accounting
    ContactsSettingsPanel contactsTab
    VATSettingsPanel vatTab
    TradeSettingsPanel tradeTab
    MealsSettingsPanel mealsTab
    AccountingCopyPanel copyPanel

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

    void updateContactSetting(){
        boolean selected = contacts.selected
        contactsTab.enabled = selected
        if(!selected){
            accounting.companyContact = null
            setVatSelected(false)
//            vatAccounting.selected = false
//            updateVatSetting()
            int indexOfComponent = indexOfComponent(contactsTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        } else {
            insertTab("Contacts", null, contactsTab, "", CONTACTS_INDEX)
        }
        accounting.contactsAccounting = selected
        if(copyPanel!=null){
            copyPanel.enableCopyContacts()
        }
        Main.fireAccountingTypeChanged accounting
    }

    void updateVatSetting(){
        boolean selected = vatAccounting.selected
        vatTab.enabled = selected
        if(selected) {
            setContactsSelected(true)
//            contacts.selected = true
//            updateContactSetting()
            insertTab("VAT", null, vatTab, "", VAT_INDEX)
        } else {
            setTradeSelected(false)
//            tradeAccounting.selected = false
//            updateTradeSetting()
            setMealsSelected(false)
//            meals.selected = false
//            updateMealsSetting()
            int indexOfComponent = indexOfComponent(vatTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        }
        accounting.vatAccounting = selected
        if(copyPanel!=null){
            copyPanel.enableCopyVat()
        }
        Main.fireAccountingTypeChanged accounting
    }

    void updateTradeSetting(){
        boolean selected = tradeAccounting.selected
        tradeTab.enabled = selected
        if(selected){
            setVatSelected(true)
//            vatAccounting.selected = true
//            updateVatSetting()
            insertTab("Trade", null, tradeTab, "", TRADE_INDEX)
        } else {
            int indexOfComponent = indexOfComponent(tradeTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        }
        accounting.tradeAccounting = selected
        if(copyPanel!=null){
            copyPanel.enableCopyTrade()
        }
        Main.fireAccountingTypeChanged accounting
    }

    void updateMealsSetting(){
        boolean selected = meals.selected
        mealsTab.enabled = selected
        if(selected){
            vatAccounting.selected = true
            updateVatSetting()
            int index = MEALS_INDEX
            if(tradeAccounting.selected){
                index++
            }
            // TODO: if tab for Mortgages is available, raise index as well
//            if(mortgages.selected){
//                index++
//            }
            insertTab("Meals", null, mealsTab, "", index)
        } else {
            int indexOfComponent = indexOfComponent(mealsTab)
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent)
            }
        }
        accounting.mealsAccounting = selected
        if(copyPanel!=null){
            copyPanel.enableCopyMeals()
        }
        Main.fireAccountingTypeChanged accounting
    }

    void updateMortgageSetting(){
        accounting.mortgagesAccounting = mortgages.selected
        Main.fireAccountingTypeChanged accounting
    }

    void updateProjectSetting(){
        accounting.projectsAccounting = projects.selected
        Main.fireAccountingTypeChanged accounting
    }

    JComponent createCenterPanel(){
        JPanel panel = new JPanel()

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS))
        projects = new JCheckBox(PROJECTS)
        mortgages = new JCheckBox(MORTGAGES)
        contacts = new JCheckBox(CONTACTS)
        vatAccounting = new JCheckBox(VAT)
        tradeAccounting = new JCheckBox(TRADE)
        meals = new JCheckBox(MEALS)
        accountNumbers = new JCheckBox(ACCOUNT_NUMBERS)

        projects.selected = accounting?accounting.projectsAccounting:true
        mortgages.selected = accounting?accounting.mortgagesAccounting:true
        contacts.selected = accounting?accounting.contactsAccounting:true
        vatAccounting.selected = accounting?accounting.vatAccounting:true
        tradeAccounting.selected = accounting?accounting.tradeAccounting:true
        meals.selected = accounting?accounting.mealsAccounting:true
        if(accounting) {
            AccountingSession accountingSession = Session.getAccountingSession(accounting)
            accountNumbers.selected = accountingSession?accountingSession.showNumbers:false
        } else accountNumbers.selected = false

        projects.addActionListener({ e -> updateProjectSetting() })
        mortgages.addActionListener({ e -> updateMortgageSetting() })
        contacts.addActionListener({ e -> updateContactSetting() })
        vatAccounting.addActionListener({ e -> updateVatSetting() })
        tradeAccounting.addActionListener({ e -> updateTradeSetting() })
        meals.addActionListener({ e -> updateMealsSetting() })
        accountNumbers.addActionListener( { e ->
            AccountingSession accountingSession = Session.getAccountingSession(accounting)
            accountingSession.showNumbers = accountNumbers.selected
        })

        panel.add(projects)
        panel.add(mortgages)
        panel.add(contacts)
        panel.add(vatAccounting)
        panel.add(tradeAccounting)
        panel.add(meals)
        panel.add(accountNumbers)

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
