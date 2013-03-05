package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.Accounting.Objects.Accounting.Transaction;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalsGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;
    private Journals journals;

	public JournalsGUI(ActionListener actionListener) {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("DAGBOEKEN")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<Journal>();
        combo.addActionListener(this);
		combo.setEnabled(false);
		add(combo);
		JPanel paneel = new JPanel();
		journalManagement = new JButton(getBundle("Accounting").getString("NIEUW_DAGBOEK"));
		journalManagement.addActionListener(actionListener);
        journalManagement.setActionCommand(ComponentMap.JOURNAL_MANAGEMENT);
		journalManagement.setEnabled(false);
		paneel.add(journalManagement);
		details = new JButton(getBundle("Accounting").getString("DETAILS_DAGBOEK"));
		details.addActionListener(actionListener);
        details.setActionCommand(ComponentMap.JOURNAL_DETAILS);
		details.setEnabled(false);
		paneel.add(details);
		add(paneel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == combo) {
            Journal oldJournal = journals.getCurrentJournal();
			Journal newJournal = (Journal) combo.getSelectedItem();
            if(newJournal!=null && oldJournal!=null){
                checkTransfer(oldJournal, newJournal);
            } else {
                journals.setCurrentJournal(newJournal);
            }
            ComponentMap.refreshAllFrames();
		}
	}

    private void checkTransfer(Journal oldJournal, Journal newJournal){
        Transaction oldTransaction = oldJournal.getCurrentTransaction();
        Transaction newTransaction = newJournal.getCurrentTransaction();
        if(oldTransaction!=null && !oldTransaction.isEmpty()){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(oldJournal).append(" to ").append(newJournal);
            if(newTransaction!=null && !newTransaction.isEmpty()){
                builder.append("\r\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer");
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString());
            if(answer == JOptionPane.YES_OPTION){
                newJournal.setCurrentTransaction(oldTransaction);
                oldJournal.setCurrentTransaction(new Transaction());
                journals.setCurrentJournal(newJournal);
            } else if(answer == JOptionPane.NO_OPTION){
                journals.setCurrentJournal(newJournal);
            } else {
                journals.setCurrentJournal(oldJournal);
            }
        } else {
            journals.setCurrentJournal(newJournal);
        }
    }

    public void setAccounting(Accounting accounting){
        if(accounting==null){
            setJournals(null);
        } else {
            setJournals(accounting.getJournals());
        }
    }

    public void setJournals(Journals journals){
        this.journals = journals;
    }

	public void refresh() {
        combo.removeActionListener(this);
        combo.removeAllItems();
		if (journals!=null) {
            for(Journal journal: journals.values()){
                combo.addItem(journal);
            }
			combo.setSelectedItem(journals.getCurrentJournal());
            details.setEnabled(journals!=null && journals.getCurrentJournal()!=null);
            combo.setEnabled(journals!=null);
            journalManagement.setEnabled(journals != null);
		} else {
			combo.setSelectedItem(null);
            details.setEnabled(false);
            combo.setEnabled(false);
            journalManagement.setEnabled(false);
		}
        combo.addActionListener(this);
	}
}