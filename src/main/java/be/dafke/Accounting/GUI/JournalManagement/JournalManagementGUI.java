package be.dafke.Accounting.GUI.JournalManagement;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.JournalType;
import be.dafke.Accounting.Objects.Accounting.JournalTypes;
import be.dafke.RefreshableTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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

	public JournalManagementGUI(Accounting accounting) {
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
		newType.addActionListener(this);
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
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPanel);
		pack();
//		setVisible(true);
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
        JournalTypes journaltypes = accounting.getJournalTypes();
        type = new JComboBox<JournalType>();
        for(JournalType journalType : journaltypes.values()){
            type.addItem(journalType);
        }
        super.refresh();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add || e.getSource() == name || e.getSource() == abbr) {
			addJournal();
		} else if (e.getSource() == modifyName) {
			modifyName();
		} else if (e.getSource() == modifyAbbr) {
			modifyAbbr();
		} else if (e.getSource() == modifyType) {
			modifyType();
		} else if (e.getSource() == delete) {
			deleteJournal();
		} else if (e.getSource() == newType) {
            String key = accounting.toString()+ComponentMap.JOURNAL_TYPE_MANAGEMENT;
            ComponentMap.getDisposableComponent(key).setVisible(true);
		}
        ComponentMap.refreshAllFrames();
	}

	private void deleteJournal() {
		int[] rows = tabel.getSelectedRows();
		int nrNotEmpty = 0;
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select a journal first");
		} else {
			for(int row : rows) {
				Journal journal = (Journal) model.getValueAt(row, 0);
				if (!journal.getTransactions().isEmpty()) {
					nrNotEmpty++;
				} else {
					accounting.getJournals().remove(journal.toString());
				}
			}
			if (nrNotEmpty > 0) {
				if (nrNotEmpty == 1) {
					if (rows.length == 1) {
						JOptionPane.showMessageDialog(this, "The journal already contains transactions,"
								+ "so it could not be deleted.");
					} else {
						JOptionPane.showMessageDialog(this, "The saldo of 1 journal already contains transactions,"
								+ "so it could not be deleted.");
					}
				} else {
					JOptionPane.showMessageDialog(this, "The saldo of " + nrNotEmpty
							+ " journal already contain transactions, so they could not be deleted");
				}
			}
		}
	}

	private void modifyName() {
		int[] rows = tabel.getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select a journal first");
		} else {
			for(int row : rows) {
				Journal journal = (Journal) model.getValueAt(row, 0);
				String oldName = journal.getName();
				String newName = JOptionPane.showInputDialog("New name", oldName);
				if (newName == null) {
					newName = oldName;
				} else {
					newName = newName.trim();
				}
				while (!oldName.equals(newName) && accounting.getJournals().containsKey(newName)) {
					JOptionPane.showMessageDialog(this, "Journal name already exists");
					newName = JOptionPane.showInputDialog("New name", oldName);
					if (newName == null) {
						newName = oldName;
					} else {
						newName = newName.trim();
					}

				}
				accounting.getJournals().rename(oldName, newName);
			}
		}
	}

	private void modifyAbbr() {
		int[] rows = tabel.getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select a journal first");
		} else {
			for(int row : rows) {
				Journal journal = (Journal) model.getValueAt(row, 0);
				String oldName = journal.getAbbreviation();
				String newName = JOptionPane.showInputDialog("New abbreviation", oldName);
				if (newName == null) {
					newName = oldName;
				} else {
					newName = newName.trim();
				}
				while (!oldName.equals(newName) && accounting.getJournals().containsAbbreviation(newName)) {
					JOptionPane.showMessageDialog(this, "This abbreviation already exists");
					newName = JOptionPane.showInputDialog("New abbreviation", oldName);
					if (newName == null) {
						newName = oldName;
					} else {
						newName = newName.trim();
					}
				}
				accounting.getJournals().reAbbrev(oldName, newName);
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
            JOptionPane.showMessageDialog(this, "There is already an journal with the name \""+newName+"\" and/or abbreviation \""+abbreviation+"\" .\r\n"+
                    "Please provide a new name.");
        } catch (EmptyNameException e) {
            JOptionPane.showMessageDialog(this, "Journal name and abbreviation cannot be empty\r\nPlease provide a new name and/or abbreviation.");
        }
        name.setText("");
        abbr.setText("");
	}

	private void modifyType() {
		int[] rows = tabel.getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select a journal first");
		} else {
			boolean singleMove;
			if (rows.length == 1) {
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
				if (nr != -1) {
					for(int row : rows) {
						Journal journal = (Journal) model.getValueAt(row, 0);
						journal.setType((JournalType) types[nr]);
					}
				}
			} else {
				for(int row : rows) {
					Journal journal = (Journal) model.getValueAt(row, 0);
					Object[] types = accounting.getJournalTypes().getAllTypes().toArray();
					int nr = JOptionPane.showOptionDialog(this, "Choose new type for " + journal.toString(),
							"Change type", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
							journal.getType());
					if (nr != -1) {
						journal.setType((JournalType) types[nr]);
					}
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
