package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.JournalType;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

public class JournalManagementGUI extends JFrame implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JButton add, delete, newType;
	private final DefaultListSelectionModel selection;
    private SelectableTable<Journal> tabel;
    private JournalManagementTableModel journalManagementTableModel;
    private Journals journals;
    private JournalTypes journalTypes;
    private AccountTypes accountTypes;
    private static final HashMap<Journals, JournalManagementGUI> journalManagementGuis = new HashMap<>();

    private JournalManagementGUI(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
		super(journals.getAccounting().getName() + " / " + getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE"));
        this.journals = journals;
        this.journalTypes = journalTypes;
        this.accountTypes = accountTypes;
        journalManagementTableModel = new JournalManagementTableModel(journals);

        tabel = new SelectableTable<>(journalManagementTableModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        tabel.setRowSorter(null);

        JComboBox<JournalType> comboBox=createComboBox();

        TableColumn column = tabel.getColumnModel().getColumn(JournalManagementTableModel.TYPE_COL);
        column.setCellEditor(new DefaultCellEditor(comboBox));

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

    private JComboBox<JournalType> createComboBox() {
        JComboBox<JournalType> comboBox = new JComboBox<>();
        journalTypes.getBusinessObjects().forEach(journalType -> comboBox.addItem(journalType));
        return comboBox;
    }

    public static JournalManagementGUI showJournalManager(Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        JournalManagementGUI gui = journalManagementGuis.get(journals);
        if(gui == null){
            gui = new JournalManagementGUI(journals, journalTypes, accountTypes);
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
        delete = new JButton(getBundle("Accounting").getString("DELETE_JOURNAL"));
        newType = new JButton(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        add = new JButton(getBundle("Accounting").getString("NEW_JOURNAL"));
        delete.addActionListener(e -> deleteJournal());
        newType.addActionListener(e -> showJournalTypeManager(journalTypes,accountTypes));
        add.addActionListener(e -> NewJournalGUI.getInstance(journals, journalTypes, accountTypes).setVisible(true));
        delete.setEnabled(false);
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
