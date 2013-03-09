package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Accounting.Objects.Accounting.Statement;
import be.dafke.Accounting.Objects.Accounting.TmpCounterParty;
import be.dafke.RefreshableDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CounterPartySelector extends RefreshableDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton ok, create, apply;
	private final JComboBox<CounterParty> oldCounterPartyCombo, newCounterPartyCombo;
	private CounterParty oldCounterParty, newCounterParty;
    private final JTable movementTable;
	private final GenericStatementDataModel movementDataModel;
	private final Statement statement;
	private final JRadioButton single, multiple;
    private final JCheckBox searchOnTransactionCode, searchOnCommunication, searchOnCounterParty;
    private final JTextField transactionCode, communication;
	private final ButtonGroup singleMultiple;
	private final Accounting accounting;
    private final SearchOptions searchOptions;

    public CounterPartySelector(Statement statement, Accounting accounting) {
		super("Select Counterparty");
		this.statement = statement;
		this.accounting = accounting;
		oldCounterParty = null;
        newCounterParty = null;

        // COMPONENTS
		oldCounterPartyCombo = new JComboBox<CounterParty>();
		oldCounterPartyCombo.addItem(null);
        for(CounterParty counterParty : accounting.getCounterParties().getBusinessObjects()){
            oldCounterPartyCombo.addItem(counterParty);
        }
        oldCounterPartyCombo.setSelectedItem(null);
		oldCounterPartyCombo.addActionListener(this);
        oldCounterPartyCombo.setEnabled(false);
        newCounterPartyCombo = new JComboBox<CounterParty>();
        newCounterPartyCombo.addItem(null);
        for(CounterParty counterParty : accounting.getCounterParties().getBusinessObjects()){
            newCounterPartyCombo.addItem(counterParty);
        }
        newCounterPartyCombo.setSelectedItem(null);
        newCounterPartyCombo.addActionListener(this);

        ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		create = new JButton("Create Counterparty");
		create.addActionListener(this);
        //
        searchOptions = new SearchOptions();
        searchOptions.searchForCounterParty(null);
        searchOptions.searchForTransactionCode(statement.getTransactionCode());
        movementDataModel = new GenericStatementDataModel(searchOptions,
                accounting);
        movementDataModel.setSingleStatement(statement);
        movementTable = new JTable(movementDataModel);
        movementTable.setDefaultRenderer(CounterParty.class, new ColorRenderer());
        movementTable.setDefaultRenderer(TmpCounterParty.class, new ColorRenderer());
        JScrollPane scrollPane = new JScrollPane(movementTable);
        //
        single = new JRadioButton("Single statement");
        multiple = new JRadioButton("Multiple movements");
        searchOnTransactionCode = new JCheckBox("TransactionCode == ");
        searchOnTransactionCode.setEnabled(false);
        searchOnTransactionCode.setSelected(true);
        searchOnTransactionCode.addActionListener(this);
        searchOnCommunication = new JCheckBox("Communication == ");
        searchOnCommunication.setEnabled(false);
        searchOnCommunication.addActionListener(this);
        transactionCode = new JTextField(10);
        transactionCode.setEnabled(false);
        transactionCode.setText(statement.getTransactionCode());
        transactionCode.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setTransactionCode();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setTransactionCode();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setTransactionCode();
            }
        });
        communication = new JTextField(10);
        communication.setEnabled(false);
        communication.setText(statement.getCommunication());
        communication.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setCommunication();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setCommunication();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setCommunication();
            }
        });
        searchOnCounterParty = new JCheckBox("Counterparty == ");
        searchOnCounterParty.setEnabled(false);
        searchOnCounterParty.setSelected(true);
        searchOnCounterParty.addActionListener(this);
        singleMultiple = new ButtonGroup();
        singleMultiple.add(single);
        singleMultiple.add(multiple);
        single.setSelected(true);
        single.addActionListener(this);
        multiple.addActionListener(this);
        apply = new JButton("Apply Changes");
        apply.addActionListener(this);
        apply.setEnabled(false);

        // Layout
        JPanel searchOptionsPanel = new JPanel(new GridLayout(0,2));
        searchOptionsPanel.add(single);
        searchOptionsPanel.add(multiple);
        searchOptionsPanel.add(searchOnTransactionCode);
        searchOptionsPanel.add(transactionCode);
        searchOptionsPanel.add(searchOnCommunication);
        searchOptionsPanel.add(communication);
        searchOptionsPanel.add(searchOnCounterParty);
        searchOptionsPanel.add(oldCounterPartyCombo);
        searchOptionsPanel.add(new JLabel("New Counterparty"));
        searchOptionsPanel.add(newCounterPartyCombo);
        searchOptionsPanel.add(apply);
        searchOptionsPanel.add(create);
        //
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(searchOptionsPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(ok, BorderLayout.SOUTH);
        setContentPane(contentPanel);
        pack();
    }

    private void setCommunication() {
        searchOptions.setCommunication(communication.getText());
        fillInCounterParty();
    }

    private void setTransactionCode(){
        searchOptions.setTransactionCode(transactionCode.getText());
        fillInCounterParty();
    }

    public CounterParty getSelection() {
		return newCounterParty;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			dispose();
		} else if (e.getSource() == create) {
			String s = JOptionPane.showInputDialog(this, "Enter a name for the new counterparty");
			if (s != null && !s.equals("")) {
				CounterParties counterParties = accounting.getCounterParties();
                try {
                    CounterParty counterParty = new CounterParty();
                    counterParty.setMergeable(false);
                    counterParty.setName(s);
                    newCounterParty = counterParties.addBusinessObject(counterParty);
                    oldCounterPartyCombo.addItem(newCounterParty);
                    newCounterPartyCombo.addItem(newCounterParty);
                    newCounterPartyCombo.setSelectedItem(newCounterParty);
                } catch (EmptyNameException e1) {
                    JOptionPane.showMessageDialog(this, "The Name of the CounterParty cannot be empty.");
                } catch (DuplicateNameException e1) {
                    JOptionPane.showMessageDialog(this, "The Name of the CounterParty already exists.");
                }
			}
		} else if (e.getSource() == oldCounterPartyCombo) {
			oldCounterParty = (CounterParty) oldCounterPartyCombo.getSelectedItem();
            if(searchOnCounterParty.isSelected()){
                searchOptions.searchForCounterParty(oldCounterParty);
            } else {
                searchOptions.setSearchOnCounterParty(false);
            }
//            apply.setEnabled(counterParty!=null);
//			fillInCounterParty();
            movementDataModel.fireTableDataChanged();
        } else if (e.getSource() == newCounterPartyCombo) {
            newCounterParty = (CounterParty) newCounterPartyCombo.getSelectedItem();
            apply.setEnabled(newCounterParty != null);
            fillInCounterParty();
        } else if (e.getSource() == single) {
			movementDataModel.setSingleStatement(statement);
            enableSearchOptions(false);
			fillInCounterParty();
		} else if (e.getSource() == multiple) {
			movementDataModel.setSingleStatement(null);
            enableSearchOptions(true);
			fillInCounterParty();
		} else if (e.getSource() == apply) {
			setCounterParty();
		} else if (e.getSource() == searchOnCommunication){
            if(searchOnCommunication.isSelected()){
                searchOptions.searchForCommunication(communication.getText());
            } else {
                searchOptions.setSearchOnCommunication(false);
            }
            fillInCounterParty();
        } else if (e.getSource() == searchOnTransactionCode){
            if(searchOnTransactionCode.isSelected()){
                searchOptions.searchForTransactionCode(transactionCode.getText());
            } else {
                searchOptions.setSearchOnTransactionCode(false);
            }
            fillInCounterParty();
        } else if (e.getSource() == searchOnCounterParty){
            if(searchOnCounterParty.isSelected()){
                searchOptions.searchForCounterParty(oldCounterParty);
            } else {
                searchOptions.setSearchOnCounterParty(false);
            }
            fillInCounterParty();
        }
	}

    private void enableSearchOptions(boolean enabled){
        searchOnTransactionCode.setEnabled(enabled);
        searchOnCommunication.setEnabled(enabled);
        searchOnCounterParty.setEnabled(enabled);
        transactionCode.setEnabled(enabled);
        communication.setEnabled(enabled);
        oldCounterPartyCombo.setEnabled(enabled);
    }

	private void setCounterParty() {
		for(Statement m : movementDataModel.getAllStatements()) {
			TmpCounterParty tmpCounterParty = m.getTmpCounterParty();
            // TODO: check if tmpCounterParty is not null (now null-safe through disabled buttons in GUI)
			m.setCounterParty(tmpCounterParty.getCounterParty());
		}
        newCounterPartyCombo.setEnabled(false);
		single.setEnabled(false);
		multiple.setEnabled(false);
		create.setEnabled(false);
		apply.setEnabled(false);
        enableSearchOptions(false);
        movementDataModel.fireTableDataChanged();
	}

	private void fillInCounterParty() {
		for(Statement m : movementDataModel.getAllStatements()) {
			String name;
			if (newCounterParty == null) {
				name = null;
			} else {
				name = newCounterParty.getName();
			}
            TmpCounterParty tmp = new TmpCounterParty();
            tmp.setName(name);
            tmp.setCounterParty(newCounterParty);
			m.setTmpCounterParty(tmp);
		}
		movementDataModel.fireTableDataChanged();
	}

    @Override
    public void refresh() {
        // nothing to do here
    }
}
