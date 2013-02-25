package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
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
	protected Calendar date;
	private BigDecimal debettotaal, credittotaal;
	private Accounting accounting;

	public JournalGUI(final Accounting accounting) {
		this.accounting = accounting;
		debettotaal = new BigDecimal(0);
		credittotaal = new BigDecimal(0);
		date = Calendar.getInstance();
		setLayout(new BorderLayout());
		journalDataModel = new JournalDataModel(accounting);
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
        clear.setEnabled(false);

		JPanel paneel1 = new JPanel();
		paneel1.add(new JLabel(
				java.util.ResourceBundle.getBundle("Accounting").getString("VERRICHTING")));
		paneel1.add(ident);
		paneel1.add(new JLabel(java.util.ResourceBundle.getBundle("Accounting").getString("DATUM")));
		paneel1.add(dag);
        paneel1.add(new JLabel("(d/m/yyyy)"));

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

    public void setAccounting(Accounting accounting){
        this.accounting = accounting;
        refresh();
    }

	private void refresh() {
        if(accounting!=null){
            journalDataModel.fireTableDataChanged();
            debettotaal = accounting.getCurrentTransaction().getDebetTotaal();
            credittotaal = accounting.getCurrentTransaction().getCreditTotaal();
            debet.setText(debettotaal.toString());
            credit.setText(credittotaal.toString());
            journalDataModel.fireTableDataChanged();
            ok.setEnabled(debettotaal.compareTo(credittotaal)==0 && debettotaal.compareTo(BigDecimal.ZERO)!=0);
            if(accounting.getCurrentJournal()!=null){
                ident.setText(accounting.getCurrentJournal().getAbbreviation() + " "
                        + accounting.getCurrentJournal().getId());
            } else {
                ident.setText("");
            }
            clear.setEnabled(true);
        } else{
            ok.setEnabled(false);
            ident.setText("");
            clear.setEnabled(false);
        }
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            if(date == null){
                JOptionPane.showMessageDialog(null, "Fill in date");
            } else {
                Transaction transaction = accounting.getCurrentTransaction();
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(bewijs.getText());
                transaction.setDate(date);
                transaction.book(accounting.getCurrentJournal());
                clear();
                bewijs.setText("");
                transaction = new Transaction();
                transaction.setDate(date);
                accounting.setCurrentTransaction(transaction);
                ComponentMap.refreshAllFrames();
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
        accounting.setCurrentTransaction(transaction);
		refresh();
	}
}