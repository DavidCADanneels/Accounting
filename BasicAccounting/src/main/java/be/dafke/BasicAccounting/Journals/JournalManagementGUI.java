package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

public class JournalManagementGUI extends JFrame implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JButton add, delete, modifyName, modifyType, modifyAbbr, newType;
	private final DefaultListSelectionModel selection;
    private RefreshableTable<Journal> tabel;
    private JournalManagementTableModel journalManagementTableModel;
    private VATTransactions vatTransactions;
    private Journals journals;
    private JournalTypes journalTypes;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private static final HashMap<Journals, JournalManagementGUI> journalManagementGuis = new HashMap<>();

    private JournalManagementGUI(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes, VATTransactions vatTransactions) {
		super(getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE"));
        this.journals = journals;
        this.journalTypes = journalTypes;
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        this.vatTransactions = vatTransactions;
        journalManagementTableModel = new JournalManagementTableModel(journals);

        tabel = new RefreshableTable<>(journalManagementTableModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));

        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);


		JPanel south = createContentPanel();
		contentPanel.add(south, BorderLayout.SOUTH);
		setContentPane(contentPanel);
		pack();
	}

    public static JournalManagementGUI showJournalManager(Journals journals, JournalTypes journalTypes, Accounts accounts, AccountTypes accountTypes, VATTransactions vatTransactions) {
        JournalManagementGUI gui = journalManagementGuis.get(journals);
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes, accounts, accountTypes, vatTransactions);
            journalManagementGuis.put(journals, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireJournalDataChangedForAll() {
        for(JournalManagementGUI journalManagementGUI:journalManagementGuis.values()){
            journalManagementGUI.fireJournalDataChanged();
        }
    }

    public void fireJournalDataChanged() {
        journalManagementTableModel.fireTableDataChanged();
    }

	private JPanel createContentPanel(){
        JPanel south = new JPanel();
        modifyName = new JButton(getBundle("Accounting").getString("MODIFY_NAME"));
        modifyAbbr = new JButton(getBundle("Accounting").getString("MODIFY_ABBR"));
        modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
        delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"));
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        add = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"));
        modifyName.addActionListener(e -> modifyName());
        modifyType.addActionListener(e -> modifyType());
        modifyAbbr.addActionListener(e -> modifyAbbr());
        delete.addActionListener(e -> deleteJournal());
        newType.addActionListener(e -> showJournalTypeManager(journalTypes,accountTypes));
        add.addActionListener(e -> new NewJournalGUI(journals, journalTypes, accountTypes, vatTransactions).setVisible(true));
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
            modifyNames(journalList, journals);
            fireJournalDataChanged();
        }
    }

    public void modifyAbbr() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            modifyAbbr(journalList, journals);
            fireJournalDataChanged();
        }
    }

    public void modifyType() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            modifyType(journalList, journalTypes);
            fireJournalDataChanged();
        }
    }

    public void deleteJournal() {
        ArrayList<Journal> journalList = getSelectedJournals();
        if (!journalList.isEmpty()) {
            deleteJournal(journalList, journals);
            fireJournalDataChanged();
        }
    }

    private ArrayList<Journal> getSelectedJournals(){
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            ActionUtils.showErrorMessage(ActionUtils.SELECT_JOURNAL_FIRST);
        }
        ArrayList<Journal> journalList = new ArrayList<>();
        for(int row : rows) {
            Journal journal = (Journal) tabel.getModel().getValueAt(row, 0);
            journalList.add(journal);
        }
        return journalList;

    }

    private void modifyNames(ArrayList<Journal> journalList, Journals journals) {
        for(Journal journal : journalList){
            String oldName = journal.getName();
            boolean retry = true;
            while(retry){
                String newName = JOptionPane.showInputDialog(getBundle("BusinessActions").getString("NEW_NAME"), oldName.trim());
                try {
                    if(newName!=null && !oldName.trim().equals(newName.trim())){
                        journals.modifyJournalName(oldName, newName);
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_NAME, newName.trim());
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_NAME_EMPTY);
                }
            }
        }
    }

    private void modifyAbbr(ArrayList<Journal> journalList, Journals journals) {
        for(Journal journal : journalList){
            String oldAbbreviation = journal.getAbbreviation();
            boolean retry = true;
            while(retry){
                String newAbbreviation = JOptionPane.showInputDialog(getBundle("BusinessActions").getString("NEW_ABBR"), oldAbbreviation.trim());
                try {
                    if(newAbbreviation!=null && !oldAbbreviation.trim().equals(newAbbreviation.trim())){
                        journals.modifyJournalAbbreviation(oldAbbreviation, newAbbreviation);
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_DUPLICATE_ABBR,newAbbreviation.trim());
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(ActionUtils.JOURNAL_ABBR_EMPTY);
                }
            }
        }
    }

    private void modifyType(ArrayList<Journal> journalList, JournalTypes journalTypes) {

        boolean singleMove;
        if (journalList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(null, getBundle("BusinessActions").getString("APPLY_SAME_TYPE_FOR_ALL_JOURNALS"),
                    getBundle("BusinessActions").getString("ALL"),
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            Object[] types = journalTypes.getBusinessObjects().toArray();
            int nr = JOptionPane.showOptionDialog(null, getBundle("BusinessActions").getString("CHOOSE_NEW_TYPE"),
                    getBundle("BusinessActions").getString("CHANGE_TYPE"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            if(nr != JOptionPane.CLOSED_OPTION){
                for(Journal journal : journalList) {
                    journal.setType((JournalType) types[nr]);
                }
            }
        } else {
            for(Journal journal : journalList) {
                Object[] types = journalTypes.getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(null, getBundle("BusinessActions").getString("CHOOSE_NEW_TYPE_FOR")+" " + journal.getName(),
                        getBundle("BusinessActions").getString("CHANGE_TYPE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                        journal.getType());
                if(nr != JOptionPane.CLOSED_OPTION){
                    journal.setType((JournalType) types[nr]);
                }
            }
        }
    }

    private void deleteJournal(ArrayList<Journal> journalList, Journals journals) {
        ArrayList<String> failed = new ArrayList<>();
        for(Journal journal : journalList) {
            try{
                journals.removeBusinessObject(journal);
            }catch (NotEmptyException e){
                failed.add(journal.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(null, failed.get(0) + " "+ getBundle("BusinessActions").getString("JOURNAL_NOT_EMPTY"));
            } else {
                StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_JOURNALS_NOT_EMPTY")+"\n");
                for(String s : failed){
                    builder.append("- ").append(s).append("\n");
                }
                JOptionPane.showMessageDialog(null, builder.toString());
            }
        }
    }
}
