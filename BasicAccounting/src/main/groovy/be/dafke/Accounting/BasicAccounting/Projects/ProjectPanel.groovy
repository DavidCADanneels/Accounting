package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.Balances.BalancePanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalDetailsDataModel
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Project
import be.dafke.Accounting.BusinessModel.Projects
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import static java.util.ResourceBundle.getBundle

class ProjectPanel extends JPanel implements ActionListener{
    final JButton manage
    final JComboBox<Project> combo
    Project project
    JournalDetailsDataModel journalDetailsDataModel
    BalancePanel resultBalance, relationsBalance
    Projects projects
    Accounting accounting

    ProjectPanel(Accounting accounting) {
        this.accounting = accounting
        projects = accounting.projects
        setLayout(new BorderLayout())

        JPanel noord = new JPanel()
        //
        combo = new JComboBox<>()
        combo.addActionListener(this)
        //
        manage = new JButton(getBundle("Projects").getString("PROJECTMANAGER"))
        manage.addActionListener({ e ->
            ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(accounting)
            projectManagementGUI.setLocation(getLocationOnScreen())
            projectManagementGUI.visible = true
        })
        //
        noord.add(combo)
        noord.add(manage)
        add(noord, BorderLayout.NORTH)

//        Projects projects1 = accounting.projects
        ArrayList<Project> projects = projects.getBusinessObjects()
        if(projects.isEmpty()){
            project = new Project('all', accounting.accounts, accounting.accountTypes)
        } else {
            project = projects[0]
        }
//------------------------------------------------------------------------------------------
        resultBalance = new BalancePanel(accounting, project.resultBalance, true, false)
        resultBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("RESULTBALANCE")))
//------------------------------------------------------------------------------------------
        relationsBalance = new BalancePanel(accounting, project.relationsBalance, true, false)
        relationsBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("BusinessModel").getString("RELATIONSBALANCE")))
//------------------------------------------------------------------------------------------
        JScrollPane transactionsPanel = createTransactionsPanel()
        transactionsPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("TRANSACTIONS")))
//------------------------------------------------------------------------------------------
        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        JSplitPane balances = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        balances.add(resultBalance,JSplitPane.TOP)
        balances.add(relationsBalance, JSplitPane.BOTTOM)
        center.add(balances, JSplitPane.TOP)
        center.add(transactionsPanel, JSplitPane.BOTTOM)
//------------------------------------------------------------------------------------------
        add(center,BorderLayout.CENTER)
    }

//    static JScrollPane createBalancePanel(SelectableTableModel balanceDataModel){
//        SelectableTable<Account> table = new SelectableTable<>(balanceDataModel)
//        table.setPreferredScrollableViewportSize(new Dimension(500, 200))
//        new JScrollPane(table)
//    }

    JScrollPane createTransactionsPanel() {
        journalDetailsDataModel = new JournalDetailsDataModel()
        SelectableTable<Booking> table = new SelectableTable<>(journalDetailsDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(800, 200))
        new JScrollPane(table)
    }

    void refresh() {
        combo.removeActionListener(this)
        combo.removeAllItems()
        for(Project project : projects.businessObjects) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project)
        }
//        combo.addActionListener(this)
        if (!projects.businessObjects.isEmpty()) {
            if(project==null) project = projects.businessObjects.get(0)
            combo.setSelectedItem(project)
        }
        combo.addActionListener(this)
        if(project) {
            journalDetailsDataModel.setJournal(project.journal)
            resultBalance.setBalance(project.resultBalance)
            relationsBalance.setBalance(project.relationsBalance)
//            resultBalanceLeftDataModel.setBalance(project.getResultBalance())
//            resultBalanceRightDataModel.setBalance(project.getResultBalance())
//            relationsBalanceLeftDataModel.setBalance(project.getRelationsBalance())
//            relationsBalanceRightDataModel.setBalance(project.getRelationsBalance())
        }
        journalDetailsDataModel.fireTableDataChanged()
        resultBalance.fireTableDataChanged()
        relationsBalance.fireTableDataChanged()
//        resultBalanceLeftDataModel.fireTableDataChanged()
//        resultBalanceRightDataModel.fireTableDataChanged()
//        relationsBalanceLeftDataModel.fireTableDataChanged()
//        relationsBalanceRightDataModel.fireTableDataChanged()
    }

    @Override
    void actionPerformed(ActionEvent e) {
        project = (Project) combo.selectedItem
        refresh()
    }
}
