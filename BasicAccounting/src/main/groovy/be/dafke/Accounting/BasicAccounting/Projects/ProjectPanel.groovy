package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.Balances.BalanceLeftDataModel
import be.dafke.Accounting.BasicAccounting.Balances.BalanceRightDataModel
import be.dafke.Accounting.BasicAccounting.Journals.JournalDetailsDataModel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.SelectableTable
import be.dafke.ComponentModel.SelectableTableModel

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

import static java.util.ResourceBundle.getBundle

class ProjectPanel extends JPanel{
    final JButton manage
    final JComboBox<Project> combo
    Project project
    JournalDetailsDataModel journalDetailsDataModel
    BalanceLeftDataModel resultBalanceLeftDataModel, relationsBalanceLeftDataModel
    BalanceRightDataModel resultBalanceRightDataModel, relationsBalanceRightDataModel
    Projects projects

    ProjectPanel(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        this.projects = projects
        setLayout(new BorderLayout())

        JPanel noord = new JPanel()
        //
        combo = new JComboBox<>()
        combo.addActionListener({ e ->
            project = (Project) combo.selectedItem
            refresh()
        })
        //
        manage = new JButton(getBundle("Projects").getString("PROJECTMANAGER"))
        manage.addActionListener({ e ->
            ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(accounts, accountTypes, projects)
            projectManagementGUI.setLocation(getLocationOnScreen())
            projectManagementGUI.visible = true
        })
        //
        noord.add(combo)
        noord.add(manage)
        add(noord, BorderLayout.NORTH)

//------------------------------------------------------------------------------------------
        resultBalanceLeftDataModel = new BalanceLeftDataModel(getBundle("BusinessModel").getString("COSTS"), true)
        resultBalanceRightDataModel = new BalanceRightDataModel(getBundle("BusinessModel").getString("REVENUES"), true)
        JScrollPane resultLeftBalance = createBalancePanel(resultBalanceLeftDataModel)
        JScrollPane resultRightBalance = createBalancePanel(resultBalanceRightDataModel)

        JSplitPane resultPane = Main.createSplitPane(resultLeftBalance, resultRightBalance, JSplitPane.HORIZONTAL_SPLIT)

        resultPane.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("RESULTBALANCE")))
//------------------------------------------------------------------------------------------
        relationsBalanceLeftDataModel = new BalanceLeftDataModel(getBundle("BusinessModel").getString("FUNDS_FROM_CUSTOMERS"),true)
        relationsBalanceRightDataModel = new BalanceRightDataModel(getBundle("BusinessModel").getString("DEBTS_TO_SUPPLIERS"), true)
        JScrollPane partnerLeftBalance = createBalancePanel(relationsBalanceLeftDataModel)
        JScrollPane partnerRightBalance = createBalancePanel(relationsBalanceRightDataModel)

        JSplitPane partnerPane = Main.createSplitPane(partnerLeftBalance, partnerRightBalance, JSplitPane.HORIZONTAL_SPLIT)

        partnerPane.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("RELATIONSBALANCE")))
//------------------------------------------------------------------------------------------
        JScrollPane transactionsPanel = createTransactionsPanel()
        transactionsPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("TRANSACTIONS")))
//------------------------------------------------------------------------------------------
        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        JSplitPane balances = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        balances.add(resultPane,JSplitPane.TOP)
        balances.add(partnerPane, JSplitPane.BOTTOM)
        center.add(balances, JSplitPane.TOP)
        center.add(transactionsPanel, JSplitPane.BOTTOM)
//------------------------------------------------------------------------------------------
        add(center,BorderLayout.CENTER)
    }

    JScrollPane createBalancePanel(SelectableTableModel balanceDataModel){
        SelectableTable<Account> table = new SelectableTable<>(balanceDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))
        new JScrollPane(table)
    }

    JScrollPane createTransactionsPanel() {
        journalDetailsDataModel = new JournalDetailsDataModel()
        SelectableTable<Booking> table = new SelectableTable<>(journalDetailsDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(800, 200))
        new JScrollPane(table)
    }

    void refresh() {
        combo.removeAllItems()
        for(Project project : projects.businessObjects) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project)
        }
        if (!projects.businessObjects.empty) {
            if(project==null) project = projects.businessObjects.get(0)
            combo.setSelectedItem(project)
        }
        if(project!=null) {
            journalDetailsDataModel.setJournal(project.journal)
            resultBalanceLeftDataModel.setBalance(project.getResultBalance())
            resultBalanceRightDataModel.setBalance(project.getResultBalance())
            relationsBalanceLeftDataModel.setBalance(project.getRelationsBalance())
            relationsBalanceRightDataModel.setBalance(project.getRelationsBalance())
        }
        journalDetailsDataModel.fireTableDataChanged()
        resultBalanceLeftDataModel.fireTableDataChanged()
        resultBalanceRightDataModel.fireTableDataChanged()
        relationsBalanceLeftDataModel.fireTableDataChanged()
        relationsBalanceRightDataModel.fireTableDataChanged()
    }
}
