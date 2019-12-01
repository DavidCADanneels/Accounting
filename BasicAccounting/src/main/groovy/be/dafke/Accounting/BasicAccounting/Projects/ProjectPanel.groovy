package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.Balances.BalanceDataModel
import be.dafke.Accounting.BasicAccounting.Journals.JournalDetailsDataModel
import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

import static java.util.ResourceBundle.getBundle

class ProjectPanel extends JPanel{
    private final JButton manage
    private final JComboBox<Project> combo
    private Project project
    private JournalDetailsDataModel journalDetailsDataModel
    private BalanceDataModel resultBalanceDataModel, relationsBalanceDataModel
    private Projects projects

    ProjectPanel(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        this.projects = projects
        setLayout(new BorderLayout())

        JPanel noord = new JPanel()
        //
        combo = new JComboBox<>()
        combo.addActionListener({ e ->
            project = (Project) combo.getSelectedItem()
            refresh()
        })
        //
        manage = new JButton(getBundle("Projects").getString("PROJECTMANAGER"))
        manage.addActionListener({ e ->
            ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(accounts, accountTypes, projects)
            projectManagementGUI.setLocation(getLocationOnScreen())
            projectManagementGUI.setVisible(true)
        })
        //
        noord.add(combo)
        noord.add(manage)
        add(noord, BorderLayout.NORTH)

//------------------------------------------------------------------------------------------
        resultBalanceDataModel = new BalanceDataModel(
                getBundle("BusinessModel").getString("COSTS"),
                getBundle("BusinessModel").getString("REVENUES"), true)
        JScrollPane resultBalance = createBalancePanel(resultBalanceDataModel)
        resultBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "BusinessModel").getString("RESULTBALANCE")))
//------------------------------------------------------------------------------------------
        relationsBalanceDataModel = new BalanceDataModel(
                getBundle("BusinessModel").getString("FUNDS_FROM_CUSTOMERS"),
                getBundle("BusinessModel").getString("DEBTS_TO_SUPPLIERS"), true)
        JScrollPane partnerBalance = createBalancePanel(relationsBalanceDataModel)
        partnerBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "BusinessModel").getString("RELATIONSBALANCE")))
//------------------------------------------------------------------------------------------
        JScrollPane transactionsPanel = createTransactionsPanel()
        transactionsPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("TRANSACTIONS")))
//------------------------------------------------------------------------------------------
        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        JSplitPane balances = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        balances.add(resultBalance,JSplitPane.TOP)
        balances.add(partnerBalance, JSplitPane.BOTTOM)
        center.add(balances, JSplitPane.TOP)
        center.add(transactionsPanel, JSplitPane.BOTTOM)
//------------------------------------------------------------------------------------------
        add(center,BorderLayout.CENTER)
    }

    private JScrollPane createBalancePanel(BalanceDataModel balanceDataModel){
        SelectableTable<Account> table = new SelectableTable<>(balanceDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))
        new JScrollPane(table)
    }

    private JScrollPane createTransactionsPanel() {
        journalDetailsDataModel = new JournalDetailsDataModel()
        SelectableTable<Booking> table = new SelectableTable<>(journalDetailsDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(800, 200))
        new JScrollPane(table)
    }

    void refresh() {
        combo.removeAllItems()
        for(Project project : projects.getBusinessObjects()) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project)
        }
        if (!projects.getBusinessObjects().isEmpty()) {
            if(project==null) project = projects.getBusinessObjects().get(0)
            combo.setSelectedItem(project)
        }
        if(project!=null) {
            journalDetailsDataModel.setJournal(project.getJournal())
            resultBalanceDataModel.setBalance(project.getResultBalance())
            relationsBalanceDataModel.setBalance(project.getRelationsBalance())
        }
        journalDetailsDataModel.fireTableDataChanged()
        resultBalanceDataModel.fireTableDataChanged()
        relationsBalanceDataModel.fireTableDataChanged()
    }
}
