package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.Calendar;

public class JournalGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalDataModel journalDataModel;
	private final JTextField debet, credit, dag, bewijs, ident;
	private final JButton ok, clear;
//	protected String tekst;
	protected Calendar date;
	private BigDecimal debettotaal, credittotaal;
	private final Accountings accountings;

	public JournalGUI(final Accountings accountings) {
		this.accountings = accountings;
		debettotaal = new BigDecimal(0);
		credittotaal = new BigDecimal(0);
		date = Calendar.getInstance();
		setLayout(new BorderLayout());
		journalDataModel = new JournalDataModel(accountings);
		JTable table = new JTable(journalDataModel);
		table.setPreferredScrollableViewportSize(new Dimension(800, 200));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		ident = new JTextField(4);
		ident.setEditable(false);
		dag = new JTextField(8);
		dag.setText(Utils.toString(date));
		dag.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
            }

            @Override
            public void focusLost(FocusEvent fe) {
                date = Utils.toCalendar(dag.getText());
                if (date == null){
                    JOptionPane.showMessageDialog(null,"invalid date");
                    dag.setText("");
                }else{
                    dag.setText(Utils.toString(date));
                }
            }

        });
		bewijs = new JTextField(30);

		ok = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("OK"));
		ok.addActionListener(this);
		ok.setEnabled(false);
		clear = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("WIS_PANEEL"));
		clear.addActionListener(this);

		JPanel paneel1 = new JPanel();
		paneel1.add(new JLabel(
				java.util.ResourceBundle.getBundle("Accounting").getString("VERRICHTING")));
		paneel1.add(ident);
		paneel1.add(new JLabel(java.util.ResourceBundle.getBundle("Accounting").getString("DATUM")));
		paneel1.add(dag);

		JPanel paneel2 = new JPanel();
		paneel2.add(new JLabel(java.util.ResourceBundle.getBundle("Accounting").getString(
				"BEWIJS-STUK(KEN)")));
		paneel2.add(bewijs);

		JPanel paneel3 = new JPanel();
		paneel3.add(ok);
		paneel3.add(clear);
		debet = new JTextField(8);
		credit = new JTextField(8);
		debet.setText(debettotaal.toString());
		credit.setText(credittotaal.toString());
		debet.setEditable(false);
		credit.setEditable(false);
		paneel3.add(new JLabel(
				java.util.ResourceBundle.getBundle("Accounting").getString("DEBETTOTAAL")));
		paneel3.add(debet);
		paneel3.add(new JLabel(java.util.ResourceBundle.getBundle("Accounting").getString(
				"CREDITTOTAAL")));
		paneel3.add(credit);

		JPanel onder = new JPanel(new GridLayout(0, 1));
		onder.add(paneel1);
		onder.add(paneel2);
		onder.add(paneel3);

		add(onder, BorderLayout.SOUTH);
	}

	public void setOK() {
		ok.setEnabled(true);
	}

	public void refresh() {
		debettotaal = accountings.getCurrentAccounting().getCurrentTransaction().getDebetTotaal();
		credittotaal = accountings.getCurrentAccounting().getCurrentTransaction().getCreditTotaal();
		debet.setText(debettotaal.toString());
		credit.setText(credittotaal.toString());
		journalDataModel.fireTableDataChanged();
        if(debettotaal.compareTo(credittotaal)==0){
            ok.setEnabled(true);
        }
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            if(date == null){
                JOptionPane.showMessageDialog(null, "Fill in date");
            } else {
                Accounting accounting = accountings.getCurrentAccounting();
                Transaction transaction = accounting.getCurrentTransaction();
                // TODO Encode text for XML / HTML
                transaction.setDescription(bewijs.getText());
                transaction.setDate(date);
                transaction.book(accounting.getCurrentJournal());
                init();
                clear();
                transaction = new Transaction();
                transaction.setDate(date);
                accounting.setCurrentTransaction(transaction);
                AccountingMenuBar.refreshAllFrames();
            }
		}
		if (e.getSource() == clear) {
			clear();
		}
	}

	public void clear() {
		ok.setEnabled(false);
        Transaction transaction = new Transaction();
        transaction.setDate(date);
        accountings.getCurrentAccounting().setCurrentTransaction(transaction);
		refresh();
	}

	public void init() {
        // TODO check this init method (null pointers ???)
		Accounting accounting = accountings.getCurrentAccounting();
		if (accounting != null) {
			if (accounting.getJournals().isEmpty()) {
				AccountingMenuBar.getFrame(AccountingMenuBar.NEW_JOURNAL).setVisible(true);
			} else {
				ident.setText(accounting.getCurrentJournal().getAbbreviation() + " "
						+ accounting.getCurrentJournal().getId());
			}
		} else {
			ident.setText("");
		}
	}
}