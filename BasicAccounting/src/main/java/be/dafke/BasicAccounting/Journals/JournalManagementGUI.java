package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessActions.JournalActions;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class JournalManagementGUI extends RefreshableFrame implements ActionListener, ListSelectionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//
	private JButton add, delete, modifyName, modifyType, modifyAbbr, newType;
	private final DefaultListSelectionModel selection;
    private RefreshableTable<Journal> tabel;
    private JournalManagementTableModel dataModel;
    private Journals journals;
    private JournalTypes journalTypes;
    private Accounts accounts;
    private AccountTypes accountTypes;

    public JournalManagementGUI(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE"));
        this.journals = journals;
        this.journalTypes = journalTypes;
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        dataModel = new JournalManagementTableModel(journals);

        tabel = new RefreshableTable<>(dataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        //tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);


		JPanel south = createContentPanel();
		contentPanel.add(south, BorderLayout.SOUTH);
		setContentPane(contentPanel);
		pack();
        refresh();
	}

    public void refresh() {
//		tabel.refresh();
        dataModel.fireTableDataChanged();
    }

	private JPanel createContentPanel(){
        JPanel south = new JPanel();
        modifyName = new JButton(getBundle("Accounting").getString("MODIFY_NAME"));
        modifyAbbr = new JButton(getBundle("Accounting").getString("MODIFY_ABBR"));
        modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
        delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"));
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        add = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"));
        modifyName.addActionListener(this);
        modifyType.addActionListener(this);
        modifyAbbr.addActionListener(this);
        delete.addActionListener(this);
        newType.addActionListener(this);
        add.addActionListener(this);
        modifyName.setEnabled(false);
        modifyType.setEnabled(false);
        modifyAbbr.setEnabled(false);
        delete.setEnabled(false);
        south.add(modifyName);
        south.add(modifyType);
        south.add(modifyAbbr);
        south.add(delete);
        south.add(newType);
        south.add(add);
        return south;
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

//    @Override
//    public void selectObject(Journal account) {
//
//    }
//
//    @Override
//    public Journal getSelectedObject() {
//        return null;
//    }



	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add) {
            new NewJournalGUI(journals, journalTypes, accounts, accountTypes).setVisible(true);
		}else if (e.getSource() == newType) {
            GUIActions.showJournalTypeManager(accountTypes);
        } else {
            ArrayList<Journal> journalList = getSelectedJournals();
            if(!journalList.isEmpty()){
                if (e.getSource() == modifyName) {
                    JournalActions.modifyNames(journalList, journals);
                } else if (e.getSource() == modifyAbbr) {
                    JournalActions.modifyAbbr(journalList, journals);
                } else if (e.getSource() == modifyType) {
                    JournalActions.modifyType(journalList, journalTypes);
                } else if (e.getSource() == delete) {
                    JournalActions.deleteJournal(journalList, journals);
                }
            }
            delete.setEnabled(false);
            modifyName.setEnabled(false);
            modifyAbbr.setEnabled(false);
            modifyType.setEnabled(false);
        }
        //ComponentMap.refreshAllFrames();
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
