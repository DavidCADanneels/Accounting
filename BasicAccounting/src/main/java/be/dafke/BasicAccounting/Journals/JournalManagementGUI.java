package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessActions.JournalActions;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

public class JournalManagementGUI extends RefreshableFrame implements ListSelectionListener{
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
    private static final HashMap<Journals, JournalManagementGUI> journalManagementGuis = new HashMap<>();

    private JournalManagementGUI(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes) {
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

    public static JournalManagementGUI showJournalManager(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes) {
        JournalManagementGUI gui = journalManagementGuis.get(journals);
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes, accounts, accountTypes);
            journalManagementGuis.put(journals, gui);
            Main.addJFrame(gui);
        }
        return gui;
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
        newType = new JButton(getBundle("Accounting").getString("ADD_JOURNAL_TYPE"));
        add = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"));
        modifyName.addActionListener(e -> modifyName());
        modifyType.addActionListener(e -> modifyType());
        modifyAbbr.addActionListener(e -> modifyAbbr());
        delete.addActionListener(e -> deleteJournal());
        newType.addActionListener(e -> showJournalTypeManager(journalTypes,accountTypes));
        add.addActionListener(e -> new NewJournalGUI(journals, journalTypes, accounts, accountTypes).setVisible(true));
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
			boolean enabled = rows.length != 0;
            delete.setEnabled(enabled);
            modifyName.setEnabled(enabled);
            modifyAbbr.setEnabled(enabled);
            modifyType.setEnabled(enabled);
		}
	}

    public void modifyName() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            JournalActions.modifyNames(journalList, journals);
        }
    }

    public void modifyAbbr() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            JournalActions.modifyAbbr(journalList, journals);
        }
    }

    public void modifyType() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            JournalActions.modifyType(journalList, journalTypes);
        }
    }

    public void deleteJournal() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            JournalActions.deleteJournal(journalList, journals);
        }
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
