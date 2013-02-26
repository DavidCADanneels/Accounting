package be.dafke.Accounting.GUI.JournalManagement;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.JournalType;
import be.dafke.RefreshableTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class JournalManagementGUI extends RefreshableTable implements ActionListener, ListSelectionListener, FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField name, abbr;
	private JComboBox<JournalType> type;
	private final JButton add, delete, modifyName, modifyType, newType, modifyAbbr;
	private final DefaultListSelectionModel selection;
	private final Accounting accounting;

	public JournalManagementGUI(Accounting accounting, ActionListener actionListener) {
		super("Create and modify journals for " + accounting.toString(), new NewJournalDataModel(accounting));
		this.accounting = accounting;
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel("Name:"));
		name = new JTextField(20);
		abbr = new JTextField(6);
		line1.add(name);
		line1.add(new JLabel("Abbreviation:"));
		line1.add(abbr);
		JPanel line2 = new JPanel();
		line2.add(new JLabel("Type:"));
		type = new JComboBox<JournalType>();
		line2.add(type);
		add = new JButton("Create new journal");
		add.addActionListener(this);
		name.addActionListener(this);
		abbr.addActionListener(this);
		name.addFocusListener(this);
		line2.add(add);
		newType = new JButton("Manage types ...");
        newType.setActionCommand(ComponentMap.JOURNAL_TYPE_MANAGEMENT);
		newType.addActionListener(actionListener);
		line2.add(newType);
		north.add(line1);
		north.add(line2);
		contentPanel.add(north, BorderLayout.NORTH);
		JPanel south = new JPanel();
		modifyName = new JButton("Modify name");
		modifyAbbr = new JButton("Modify abbreviation");
		modifyType = new JButton("Modify type");
		delete = new JButton("Delete journal");
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		modifyAbbr.addActionListener(this);
		delete.addActionListener(this);
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		modifyAbbr.setEnabled(false);
		delete.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
		south.add(modifyAbbr);
		south.add(delete);
		contentPanel.add(south, BorderLayout.SOUTH);
		setContentPane(contentPanel);
		pack();
        refresh();
	}

    @Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int[] rows = tabel.getSelectedRows();
			if (rows.length != 0) {
				delete.setEnabled(true);
				modifyName.setEnabled(true);
				modifyAbbr.setEnabled(true);
				modifyType.setEnabled(true);
			}
		}
	}

    @Override
    public void refresh(){
        type.removeAllItems();
        for(JournalType journalType : accounting.getJournalTypes().getAllTypes()){
            type.addItem(journalType);
        }
        super.refresh();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add || e.getSource() == name || e.getSource() == abbr) {
			addJournal();
		} else {
            ArrayList<Journal> journalList = getSelectedJournals();
            if(!journalList.isEmpty()){
                if (e.getSource() == modifyName) {
                    modifyNames(journalList);
                } else if (e.getSource() == modifyAbbr) {
                    modifyAbbr(journalList);
                } else if (e.getSource() == modifyType) {
                    modifyType(journalList);
                } else if (e.getSource() == delete) {
                    deleteJournal(journalList);
                }
            }
            delete.setEnabled(false);
            modifyName.setEnabled(false);
            modifyAbbr.setEnabled(false);
            modifyType.setEnabled(false);
        }
        ComponentMap.refreshAllFrames();
    }

    private ArrayList<Journal> getSelectedJournals(){
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select a journal first");
        }
        ArrayList<Journal> journalList = new ArrayList<Journal>();
        for(int row : rows) {
            Journal journal = (Journal) model.getValueAt(row, 0);
            journalList.add(journal);
        }
        return journalList;

    }

	private void deleteJournal(ArrayList<Journal> journals) {
        ArrayList<String> failed = new ArrayList<String>();
        for(Journal journal : journals) {
            try{
                accounting.getJournals().removeJournal(journal);
            }catch (NotEmptyException e){
                failed.add(journal.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(this, failed.get(0) + " already has transactions, so it can not be deleted.");
            } else {
                StringBuilder builder = new StringBuilder("The following accounts already have transactions, so they can not be deleted:\r\n");
                for(String s : failed){
                    builder.append("- ").append(s).append("\r\n");
                }
                JOptionPane.showMessageDialog(this, builder.toString());
            }
        }
	}

	private void modifyNames(ArrayList<Journal> journalList) {
        for(Journal journal : journalList){
            String oldName = journal.getName();
            boolean retry = true;
            while(retry){
                String newName = JOptionPane.showInputDialog("New name", oldName.trim());
                try {
                    if(newName!=null && !oldName.trim().equals(newName.trim())){
                        accounting.getJournals().modifyJournalName(oldName, newName);
                        ComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    JOptionPane.showMessageDialog(this, "There is already a journal with the name \""+newName.trim()+"\".\r\n"+
                            "Please provide a new name.");
                } catch (EmptyNameException e) {
                    JOptionPane.showMessageDialog(this, "Journal name cannot be empty\r\nPlease provide a new name.");
                }
            }
        }
	}

	private void modifyAbbr(ArrayList<Journal> journalList) {
        for(Journal journal : journalList){
            String oldAbbreviation = journal.getAbbreviation();
            boolean retry = true;
            while(retry){
                String newAbbreviation = JOptionPane.showInputDialog("New abbreviation", oldAbbreviation.trim());
                try {
                    if(newAbbreviation!=null && !oldAbbreviation.trim().equals(newAbbreviation.trim())){
                        accounting.getJournals().modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
                        ComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    JOptionPane.showMessageDialog(this, "There is already a journal with the abbreviation \""+newAbbreviation.trim()+"\".\r\n"+
                            "Please provide a new abbreviation.");
                } catch (EmptyNameException e) {
                    JOptionPane.showMessageDialog(this, "Journal abbreviation cannot be empty\r\nPlease provide a new abbreviation.");
                }
            }
        }
	}

	private void addJournal() {
        String newName = name.getText().trim();
        String abbreviation = abbr.getText().trim();
        if(!newName.isEmpty() && abbreviation.isEmpty() && newName.length() > 2) {
            abbreviation = newName.substring(0, 3).toUpperCase();
            abbr.setText(abbreviation);
        }
        JournalType journalType = (JournalType)type.getSelectedItem();
        try {
            Journal journal = accounting.getJournals().addJournal(newName, abbreviation, journalType);
            accounting.setCurrentJournal(journal);
            ComponentMap.refreshAllFrames();
        } catch (DuplicateNameException e) {
            JOptionPane.showMessageDialog(this, "There is already an journal with the name \""+newName.trim()+"\" and/or abbreviation \""+abbreviation.trim()+"\" .\r\n"+
                    "Please provide a new name and/or abbreviation.");
        } catch (EmptyNameException e) {
            JOptionPane.showMessageDialog(this, "Journal name and abbreviation cannot be empty\r\nPlease provide a new name and/or abbreviation.");
        }
        name.setText("");
        abbr.setText("");
	}

	private void modifyType(ArrayList<Journal> journalList) {

        boolean singleMove;
        if (journalList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(this, "Apply same type for all selected journals?", "All",
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            Object[] types = accounting.getJournalTypes().getAllTypes().toArray();
            int nr = JOptionPane.showOptionDialog(this, "Choose new type", "Change type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                for(Journal journal : journalList) {
                    journal.setType((JournalType)types[nr]);
                }
            }
        } else {
            for(Journal journal : journalList) {
                Object[] types = accounting.getJournalTypes().getAllTypes().toArray();
                int nr = JOptionPane.showOptionDialog(this, "Choose new type for " + journal.getName(),
                        "Change type", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                        journal.getType());
                if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                    journal.setType((JournalType)types[nr]);
                }
            }
        }
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == name) {
			String text = name.getText();
			if (text.length() > 2) {
				String part = text.substring(0, 3).toUpperCase();
				abbr.setText(part);
			}
		}
	}
}
