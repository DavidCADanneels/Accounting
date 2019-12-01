package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.Mortgages
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class MortgagePanel extends JPanel implements ActionListener {
    final JList<Mortgage> mortgagesList
    final JButton create
    final JTextField nrPayed
    Mortgages mortgages
    Accounts accounts
    boolean init = true
    final JComboBox<Account> comboIntrest, comboCapital
    Mortgage selectedMortgage = null
    final MortgageDataModel model
    DefaultListModel<Mortgage> listModel
    DefaultComboBoxModel<Account> intrestModel, capitalModel

    final JTable table
    final JButton save, delete

    MortgagePanel(Mortgages mortgages, Accounts accounts) {
        this.mortgages = mortgages
        this.accounts = accounts
        mortgagesList = new JList<>()
        mortgagesList.setModel(new DefaultListModel<>())
        mortgagesList.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting() && mortgagesList.selectedIndex != -1) {
                selectedMortgage = mortgagesList.getSelectedValue()
            } else {
                selectedMortgage = null
            }
            select()
        })
        create = new JButton("Create new Mortgage table")
        create.addActionListener({ e ->
            MortgageCalculatorGUI mortgageCalculatorGUI = MortgageCalculatorGUI.showCalculator(mortgages)
            mortgageCalculatorGUI.setLocation(getLocationOnScreen())
            mortgageCalculatorGUI.visible = true
        })

        JPanel left = new JPanel(new BorderLayout())
        left.add(mortgagesList, BorderLayout.CENTER)
        left.add(create, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        add(left, BorderLayout.WEST)

        model = new MortgageDataModel(selectedMortgage)
        table = new JTable(model)
        table.setPreferredScrollableViewportSize(new Dimension(600, 200))
        JScrollPane scroll = new JScrollPane(table)

        comboIntrest = new JComboBox<>()
        comboIntrest.addActionListener(this)
        comboCapital = new JComboBox<>()
        comboCapital.addActionListener(this)
        nrPayed = new JTextField(4)
        nrPayed.addActionListener(this)
        save = new JButton("Save")
        save.addActionListener(this)
        delete = new JButton("Delete")
        delete.addActionListener(this)

        activateButtons(false)
        save.enabled = false

        JPanel accountsPanel = new JPanel()
        accountsPanel.setLayout(new GridLayout(2,0))
        accountsPanel.add(new JLabel("Intrest Account:"))
        accountsPanel.add(comboIntrest)
        accountsPanel.add(new JLabel("Capital Account:"))
        accountsPanel.add(comboCapital)
        //
        JPanel block3a = new JPanel()
        block3a.add(new JLabel("Already payed:"))
        block3a.add(nrPayed)
        //
        JPanel block3b = new JPanel()
        block3b.add(save)
        block3b.add(delete)
        //
        JPanel block3 = new JPanel()
        block3.setLayout(new BoxLayout(block3, BoxLayout.Y_AXIS))
        block3.add(block3a)
        block3.add(block3b)
        //
        JPanel north = new JPanel()
        north.add(accountsPanel)
        north.add(block3)

        JPanel right = new JPanel(new BorderLayout())
        right.add(scroll, BorderLayout.CENTER)
        right.add(north, BorderLayout.NORTH)

        add(right, BorderLayout.CENTER)



    }

    void activateButtons(boolean active) {
        comboCapital.enabled = active
        comboIntrest.enabled = active
        nrPayed.enabled = active
        delete.enabled = active
        if (active) {
            save.setText("Save")
        } else {
            save.setText("Edit")
        }
    }

    void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            if (save.getText().equals("Edit")) {
                activateButtons(true)
            } else {
                if (selectedMortgage != null) {
                    int nr = Utils.parseInt(nrPayed.getText())
                    selectedMortgage.setPayed(nr)
                    Main.fireMortgageEditedPayButton(selectedMortgage)
                }
                activateButtons(false)
            }
        } else if (e.getSource() == delete) {
            if (selectedMortgage != null) {
                try {
                    mortgages.removeBusinessObject(selectedMortgage)
                    Main.fireMortgageAddedOrRemoved(mortgages)
                } catch (NotEmptyException e1) {
                    System.err.println("This mortgage is in use !")
                    e1.printStackTrace()
                }
            }
        } else if (!init) {
            if (e.getSource() == comboIntrest) {
                Account intrestAccount = (Account) comboIntrest.selectedItem
                if (selectedMortgage != null && intrestAccount != null) {
                    selectedMortgage.setIntrestAccount(intrestAccount)
                }
            } else if (e.getSource() == comboCapital) {
                Account capitalAccount = (Account) comboCapital.selectedItem
                if (selectedMortgage != null && capitalAccount != null) {
                    selectedMortgage.setCapitalAccount(capitalAccount)
                }
            } else if (e.getSource() == nrPayed) {
                if (selectedMortgage != null) {
                    int nr = Utils.parseInt(nrPayed.getText())
                    selectedMortgage.setPayed(nr)
                }
            }
            Main.fireMortgageEditedPayButton(selectedMortgage)
        }
    }

    void refresh() {
        listModel = new DefaultListModel<>()
        for(Mortgage mortgage :mortgages.businessObjects) {
            if (!listModel.contains(mortgage)) {
                listModel.addElement(mortgage)
            }
        }
        mortgagesList.setModel(listModel)
        mortgagesList.revalidate()

        intrestModel = new DefaultComboBoxModel<>()
        capitalModel = new DefaultComboBoxModel<>()
        for(Account account : accounts.businessObjects){
            intrestModel.addElement(account)
            capitalModel.addElement(account)
        }
        comboCapital.setModel(capitalModel)
        comboIntrest.setModel(intrestModel)
        comboCapital.revalidate()
        comboIntrest.revalidate()
    }

    void reselect(Mortgage mortgage) {
        if (mortgage == selectedMortgage) {
            select()
        }
    }
    void select() {
        init = true
        save.enabled = selectedMortgage != null
        nrPayed.setText(selectedMortgage==null?"":selectedMortgage.getNrPayed() + "")
        if (selectedMortgage == null) {
            activateButtons(false)
            comboIntrest.setSelectedIndex(-1)
            comboCapital.setSelectedIndex(-1)
        } else {
            comboIntrest.setSelectedItem(selectedMortgage.getIntrestAccount())
            comboCapital.setSelectedItem(selectedMortgage.getCapitalAccount())
        }
        model.revalidate(selectedMortgage)
        table.revalidate()
        init = false
    }
}
