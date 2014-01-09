package be.dafke.BasicAccounting.GUI.JournalManagement;

import static java.util.ResourceBundle.getBundle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.JournalType;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

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
		super(getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE") + " " + accounting.toString(), new JournalManagementTableModel(accounting));
		this.accounting = accounting;
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel(getBundle("Accounting").getString("NAME_LABEL")));
		name = new JTextField(20);
		abbr = new JTextField(6);
		line1.add(name);
		line1.add(new JLabel(getBundle("Accounting").getString("ABBR_LABEL")));
		line1.add(abbr);
		JPanel line2 = new JPanel();
		line2.add(new JLabel(getBundle("Accounting").getString("TYPE_LABEL")));
		type = new JComboBox<JournalType>();
		line2.add(type);
		add = new JButton(getBundle("Accounting").getString("CREATE_NEW_JOURNAL"));
		add.addActionListener(this);
		name.addActionListener(this);
		abbr.addActionListener(this);
		name.addFocusListener(this);
		line2.add(add);
		newType = new JButton(getBundle("Accounting").getString("MANAGE_TYPES"));
        newType.setActionCommand(AccountingActionListener.JOURNAL_TYPE_MANAGEMENT);
		newType.addActionListener(actionListener);
		line2.add(newType);
		north.add(line1);
		north.add(line2);
		contentPanel.add(north, BorderLayout.NORTH);
		JPanel south = new JPanel();
		modifyName = new JButton(getBundle("Accounting").getString("MODIFY_NAME"));
		modifyAbbr = new JButton(getBundle("Accounting").getString("MODIFY_ABBR"));
		modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
		delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"));
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
        for(JournalType journalType : accounting.getJournalTypes().getBusinessObjects()){
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
        AccountingComponentMap.refreshAllFrames();
    }

    private ArrayList<Journal> getSelectedJournals(){
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("SELECT_JOURNAL_FIRST"));
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
                accounting.getJournals().removeBusinessObject(journal);
            }catch (NotEmptyException e){
                failed.add(journal.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(this, failed.get(0) + " "+ getBundle("Accounting").getString("JOURNAL_NOT_EMPTY"));
            } else {
                StringBuilder builder = new StringBuilder(getBundle("Accounting").getString("MULTIPLE_JOURNALS_NOT_EMPTY")+"\r\n");
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
                String newName = JOptionPane.showInputDialog(getBundle("Accounting").getString("NEW_NAME"), oldName.trim());
                try {
                    if(newName!=null && !oldName.trim().equals(newName.trim())){
                        accounting.getJournals().modifyJournalName(oldName, newName);
                        AccountingComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("JOURNAL_DUPLICATE_NAME") +" \"" +newName.trim()+"\".\r\n"+
                            getBundle("Accounting").getString("PROVIDE_NEW_NAME"));
                } catch (EmptyNameException e) {
                    JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("JOURNAL_NAME_EMPTY")+"\r\n"+
                            getBundle("Accounting").getString("PROVIDE_NEW_NAME"));
                }
            }
        }
	}

	private void modifyAbbr(ArrayList<Journal> journalList) {
        for(Journal journal : journalList){
            String oldAbbreviation = journal.getAbbreviation();
            boolean retry = true;
            while(retry){
                String newAbbreviation = JOptionPane.showInputDialog(getBundle("Accounting").getString("NEW_ABBR"), oldAbbreviation.trim());
                try {
                    if(newAbbreviation!=null && !oldAbbreviation.trim().equals(newAbbreviation.trim())){
                        accounting.getJournals().modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
                        AccountingComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("JOURNAL_DUPLICATE_ABBR")+" \""+newAbbreviation.trim()+"\".\r\n"+
                            getBundle("Accounting").getString("PROVIDE_NEW_ABBR"));
                } catch (EmptyNameException e) {
                    JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("JOURNAL_ABBR_EMPTY")+"\r\n"+
                            getBundle("Accounting").getString("PROVIDE_NEW_ABBR"));
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
            Journal journal = accounting.getJournals().createNewChild(newName);
            journal.setAbbreviation(abbreviation);
            journal.setType(journalType);
            accounting.getJournals().addBusinessObject(journal);
            accounting.getJournals().setCurrentObject(journal);
            AccountingComponentMap.refreshAllFrames();
        } catch (DuplicateNameException e) {
            JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("JOURNAL_DUPLICATE_NAME")
                    +" \""+newName.trim()+"\" "+getBundle("Accounting").getString("AND_OR_ABBR")
                    +" \""+abbreviation.trim()+"\" .\r\n"+
                    getBundle("Accounting").getString("PROVIDE_NEW_NAME_ABBR"));
        } catch (EmptyNameException e) {
            JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("JOURNAL_NAME_ABBR_EMPTY")+
                    "\r\n"+getBundle("Accounting").getString("PROVIDE_NEW_NAME_ABBR"));
        }
        name.setText("");
        abbr.setText("");
	}

	private void modifyType(ArrayList<Journal> journalList) {

        boolean singleMove;
        if (journalList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(this, getBundle("Accounting").getString("APPLY_SAME_TYPE_FOR_ALL_JOURNALS"),
                    getBundle("Accounting").getString("ALL"),
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            Object[] types = accounting.getJournalTypes().getBusinessObjects().toArray();
            int nr = JOptionPane.showOptionDialog(this, getBundle("Accounting").getString("CHOOSE_NEW_TYPE"),
                    getBundle("Accounting").getString("CHANGE_TYPE"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                for(Journal journal : journalList) {
                    journal.setType((JournalType) types[nr]);
                }
            }
        } else {
            for(Journal journal : journalList) {
                Object[] types = accounting.getJournalTypes().getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(this, getBundle("Accounting").getString("CHOOSE_NEW_TYPE_FOR")+" " + journal.getName(),
                        getBundle("Accounting").getString("CHANGE_TYPE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                        journal.getType());
                if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                    journal.setType((JournalType) types[nr]);
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
