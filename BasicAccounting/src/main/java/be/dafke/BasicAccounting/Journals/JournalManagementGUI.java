package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessActions.JournalActions;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import static java.util.ResourceBundle.getBundle;

public class JournalManagementGUI extends RefreshableTableFrame<Journal> implements ActionListener, ListSelectionListener{//}, FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//
	private final JButton add, delete, modifyName, modifyType, modifyAbbr;
	private final DefaultListSelectionModel selection;
    private Accounting accounting;

    public JournalManagementGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE"), new JournalManagementTableModel(accounting.getJournals()));
        this.accounting = accounting;
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
        tabel.setRowSorter(null);
		JPanel south = new JPanel();
		modifyName = new JButton(getBundle("Accounting").getString("MODIFY_NAME"));
		modifyAbbr = new JButton(getBundle("Accounting").getString("MODIFY_ABBR"));
		modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
		delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"));
		add = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"));
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		modifyAbbr.addActionListener(this);
		delete.addActionListener(this);
		add.addActionListener(this);
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		modifyAbbr.setEnabled(false);
		delete.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
		south.add(modifyAbbr);
		south.add(delete);
        south.add(add);
		contentPanel.add(south, BorderLayout.SOUTH);
		setContentPane(contentPanel);
		pack();
        refresh();
	}

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
    public void selectObject(Journal account) {

    }

    @Override
    public Journal getSelectedObject() {
        return null;
    }



	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add) {
            new NewJournalGUI(accounting).setVisible(true);
//		}else if (e.getSource() == newType) {
//            GUIActions.showJournalTypeManager(accounting.getAccountTypes());
        } else {
            ArrayList<Journal> journalList = getSelectedJournals();
            if(!journalList.isEmpty()){
                if (e.getSource() == modifyName) {
                    JournalActions.modifyNames(journalList, accounting.getJournals());
                } else if (e.getSource() == modifyAbbr) {
                    JournalActions.modifyAbbr(journalList, accounting.getJournals());
                } else if (e.getSource() == modifyType) {
                    JournalActions.modifyType(journalList, accounting.getJournalTypes());
                } else if (e.getSource() == delete) {
                    JournalActions.deleteJournal(journalList, accounting.getJournals());
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
            ActionUtils.showErrorMessage(ActionUtils.SELECT_JOURNAL_FIRST);
        }
        ArrayList<Journal> journalList = new ArrayList<Journal>();
        for(int row : rows) {
            Journal journal = (Journal) tabel.getModel().getValueAt(row, 0);
            journalList.add(journal);
        }
        return journalList;

    }
}
