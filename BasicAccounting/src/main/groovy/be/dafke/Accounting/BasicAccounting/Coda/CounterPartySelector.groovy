package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils

import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class CounterPartySelector extends RefreshableDialog implements ActionListener {
    private final JButton ok, create, apply
    private final JComboBox<BusinessObject> oldCounterPartyCombo, newCounterPartyCombo
    private final CounterParties counterParties
    private CounterParty oldCounterParty, newCounterParty
    private final JTable movementTable
    private final GenericStatementDataModel movementDataModel
    private final Statement statement
    private final JRadioButton single, multiple
    private final JCheckBox searchOnTransactionCode, searchOnCommunication, searchOnCounterParty
    private final JTextField transactionCode, communication
    private final ButtonGroup singleMultiple
    private final SearchOptions searchOptions

    CounterPartySelector(Statement statement, Statements statements, CounterParties counterParties) {
        super("Select Counterparty")
        this.statement = statement
        this.counterParties = counterParties
        oldCounterParty = null
        newCounterParty = null

        // COMPONENTS
        oldCounterPartyCombo = new JComboBox<BusinessObject>()
        oldCounterPartyCombo.addItem(null)
        for(BusinessObject counterParty : counterParties.getBusinessObjects()){
            oldCounterPartyCombo.addItem(counterParty)
        }
        oldCounterPartyCombo.setSelectedItem(null)
        oldCounterPartyCombo.addActionListener(this)
        oldCounterPartyCombo.setEnabled(false)
        newCounterPartyCombo = new JComboBox<BusinessObject>()
        newCounterPartyCombo.addItem(null)
        for(BusinessObject counterParty : counterParties.getBusinessObjects()){
            newCounterPartyCombo.addItem(counterParty)
        }
        newCounterPartyCombo.setSelectedItem(null)
        newCounterPartyCombo.addActionListener(this)

        ok = new JButton("Ok (Close popup)")
        ok.addActionListener(this)
        create = new JButton("Create Counterparty")
        create.addActionListener(this)
        //
        searchOptions = new SearchOptions()
        searchOptions.searchForCounterParty(null)
        searchOptions.searchForTransactionCode(statement.getTransactionCode())
        movementDataModel = new GenericStatementDataModel(searchOptions,
                statements)
        movementDataModel.setSingleStatement(statement)
        movementTable = new JTable(movementDataModel)
        movementTable.setDefaultRenderer(CounterParty.class, new ColorRenderer())
        movementTable.setDefaultRenderer(TmpCounterParty.class, new ColorRenderer())
        JScrollPane scrollPane = new JScrollPane(movementTable)
        //
        single = new JRadioButton("Single statement")
        multiple = new JRadioButton("Multiple movements")
        searchOnTransactionCode = new JCheckBox("TransactionCode == ")
        searchOnTransactionCode.setEnabled(false)
        searchOnTransactionCode.setSelected(true)
        searchOnTransactionCode.addActionListener(this)
        searchOnCommunication = new JCheckBox("Communication == ")
        searchOnCommunication.setEnabled(false)
        searchOnCommunication.addActionListener(this)
        transactionCode = new JTextField(10)
        transactionCode.setEnabled(false)
        transactionCode.setText(statement.getTransactionCode())
        transactionCode.getDocument().addDocumentListener(new DocumentListener() {
            void changedUpdate(DocumentEvent e) {
                setTransactionCode()
            }
            void insertUpdate(DocumentEvent e) {
                setTransactionCode()
            }
            void removeUpdate(DocumentEvent e) {
                setTransactionCode()
            }
        })
        communication = new JTextField(10)
        communication.setEnabled(false)
        communication.setText(statement.getCommunication())
        communication.getDocument().addDocumentListener(new DocumentListener() {
            void changedUpdate(DocumentEvent e) {
                setCommunication()
            }
            void insertUpdate(DocumentEvent e) {
                setCommunication()
            }
            void removeUpdate(DocumentEvent e) {
                setCommunication()
            }
        })
        searchOnCounterParty = new JCheckBox("Counterparty == ")
        searchOnCounterParty.setEnabled(false)
        searchOnCounterParty.setSelected(true)
        searchOnCounterParty.addActionListener(this)
        singleMultiple = new ButtonGroup()
        singleMultiple.add(single)
        singleMultiple.add(multiple)
        single.setSelected(true)
        single.addActionListener(this)
        multiple.addActionListener(this)
        apply = new JButton("Apply Changes")
        apply.addActionListener(this)
        apply.setEnabled(false)

        // Layout
        JPanel searchOptionsPanel = new JPanel(new GridLayout(0,2))
        searchOptionsPanel.add(single)
        searchOptionsPanel.add(multiple)
        searchOptionsPanel.add(searchOnTransactionCode)
        searchOptionsPanel.add(transactionCode)
        searchOptionsPanel.add(searchOnCommunication)
        searchOptionsPanel.add(communication)
        searchOptionsPanel.add(searchOnCounterParty)
        searchOptionsPanel.add(oldCounterPartyCombo)
        searchOptionsPanel.add(new JLabel("New Counterparty"))
        searchOptionsPanel.add(newCounterPartyCombo)
        searchOptionsPanel.add(apply)
        searchOptionsPanel.add(create)
        //
        JPanel contentPanel = new JPanel(new BorderLayout())
        contentPanel.add(searchOptionsPanel, BorderLayout.NORTH)
        contentPanel.add(scrollPane, BorderLayout.CENTER)
        contentPanel.add(ok, BorderLayout.SOUTH)
        setContentPane(contentPanel)
        pack()
    }

    private void setCommunication() {
        searchOptions.setCommunication(communication.getText())
        fillInCounterParty()
    }

    private void setTransactionCode(){
        searchOptions.setTransactionCode(transactionCode.getText())
        fillInCounterParty()
    }

    CounterParty getSelection() {
        newCounterParty
    }

    void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            dispose()
        } else if (e.getSource() == create) {
            String s = JOptionPane.showInputDialog(this, "Enter a name for the new counterparty")
            if (s != null && !s.equals("")) {
                try {
                    CounterParty counterParty = new CounterParty()
                    counterParty.setMergeable(false)
                    counterParty.setName(s)
                    newCounterParty = (CounterParty)counterParties.addBusinessObject(counterParty)
                    oldCounterPartyCombo.addItem(newCounterParty)
                    newCounterPartyCombo.addItem(newCounterParty)
                    newCounterPartyCombo.setSelectedItem(newCounterParty)
                } catch (EmptyNameException e1) {
                    ActionUtils.showErrorMessage(this, ActionUtils.COUNTERPARTY_NAME_EMPTY)
                } catch (DuplicateNameException e1) {
                    ActionUtils.showErrorMessage(this, ActionUtils.COUNTERPARTY_DUPLICATE_NAME)
                }
            }
        } else if (e.getSource() == oldCounterPartyCombo) {
            oldCounterParty = (CounterParty) oldCounterPartyCombo.getSelectedItem()
            if(searchOnCounterParty.isSelected()){
                searchOptions.searchForCounterParty(oldCounterParty)
            } else {
                searchOptions.setSearchOnCounterParty(false)
            }
//            apply.setEnabled(counterParty!=null)
//			fillInCounterParty()
            movementDataModel.fireTableDataChanged()
        } else if (e.getSource() == newCounterPartyCombo) {
            newCounterParty = (CounterParty) newCounterPartyCombo.getSelectedItem()
            apply.setEnabled(newCounterParty != null)
            fillInCounterParty()
        } else if (e.getSource() == single) {
            movementDataModel.setSingleStatement(statement)
            enableSearchOptions(false)
            fillInCounterParty()
        } else if (e.getSource() == multiple) {
            movementDataModel.setSingleStatement(null)
            enableSearchOptions(true)
            fillInCounterParty()
        } else if (e.getSource() == apply) {
            setCounterParty()
        } else if (e.getSource() == searchOnCommunication){
            if(searchOnCommunication.isSelected()){
                searchOptions.searchForCommunication(communication.getText())
            } else {
                searchOptions.setSearchOnCommunication(false)
            }
            fillInCounterParty()
        } else if (e.getSource() == searchOnTransactionCode){
            if(searchOnTransactionCode.isSelected()){
                searchOptions.searchForTransactionCode(transactionCode.getText())
            } else {
                searchOptions.setSearchOnTransactionCode(false)
            }
            fillInCounterParty()
        } else if (e.getSource() == searchOnCounterParty){
            if(searchOnCounterParty.isSelected()){
                searchOptions.searchForCounterParty(oldCounterParty)
            } else {
                searchOptions.setSearchOnCounterParty(false)
            }
            fillInCounterParty()
        }
    }

    private void enableSearchOptions(boolean enabled){
        searchOnTransactionCode.setEnabled(enabled)
        searchOnCommunication.setEnabled(enabled)
        searchOnCounterParty.setEnabled(enabled)
        transactionCode.setEnabled(enabled)
        communication.setEnabled(enabled)
        oldCounterPartyCombo.setEnabled(enabled)
    }

    private void setCounterParty() {
        for(Statement m : movementDataModel.getAllStatements()) {
            TmpCounterParty tmpCounterParty = m.getTmpCounterParty()
            // TODO: check if tmpCounterParty is not null (now null-safe through disabled buttons in GUI)
            m.setCounterParty(tmpCounterParty.getCounterParty())
        }
        newCounterPartyCombo.setEnabled(false)
        single.setEnabled(false)
        multiple.setEnabled(false)
        create.setEnabled(false)
        apply.setEnabled(false)
        enableSearchOptions(false)
        movementDataModel.fireTableDataChanged()
    }

    private void fillInCounterParty() {
        for(Statement m : movementDataModel.getAllStatements()) {
            String name
            if (newCounterParty == null) {
                name = null
            } else {
                name = newCounterParty.getName()
            }
            TmpCounterParty tmp = new TmpCounterParty()
            tmp.setName(name)
            tmp.setCounterParty(newCounterParty)
            m.setTmpCounterParty(tmp)
        }
        movementDataModel.fireTableDataChanged()
    }

    void refresh() {
        // nothing to do here
    }
}
