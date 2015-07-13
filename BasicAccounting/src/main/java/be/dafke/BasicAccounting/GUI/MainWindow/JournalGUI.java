package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.BookTransactionActionListener;
import be.dafke.BasicAccounting.Actions.JournalGUIPopupMenu;
import be.dafke.BasicAccounting.Actions.PopupActivator;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

public class JournalGUI extends AccountingPanel implements ActionListener, FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalDataModel journalDataModel;
	private final JTextField debet, credit, dag, maand, jaar, bewijs, ident;
	private final JButton ok, save, clear;
    private final JPopupMenu popup;
    private final RefreshableTable<Booking> table;
	private BigDecimal debettotaal, credittotaal;
    private Journal journal;
    private Accountings accountings;

    public JournalGUI(Accountings accountings) {
        this.accountings = accountings;
        debettotaal = new BigDecimal(0);
		credittotaal = new BigDecimal(0);
		setLayout(new BorderLayout());
		journalDataModel = new JournalDataModel();
		table = new RefreshableTable<Booking>(journalDataModel);
		table.setPreferredScrollableViewportSize(new Dimension(800, 200));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

        popup = new JournalGUIPopupMenu(table, accountings);
        table.addMouseListener(new PopupActivator(popup,table));

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               popup.setVisible(false);
            }
        });

		ident = new JTextField(4);
		ident.setEditable(false);
		dag = new JTextField(2);
		maand = new JTextField(2);
		jaar = new JTextField(4);
        dag.addFocusListener(this);
        maand.addFocusListener(this);
        jaar.addFocusListener(this);
		bewijs = new JTextField(30);
        bewijs.addFocusListener(this);

		ok = new JButton(getBundle("Accounting").getString("OK"));
		ok.addActionListener(new BookTransactionActionListener(this.accountings,this));
        save = new JButton(getBundle("Accounting").getString("SAVE"));
        save.addActionListener(this);
        clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"));
		clear.addActionListener(this);

		JPanel paneel1 = new JPanel();
		paneel1.add(new JLabel(
				getBundle("Accounting").getString("TRANSACTION")));
		paneel1.add(ident);
		paneel1.add(new JLabel(getBundle("Accounting").getString("DATE")));
		paneel1.add(dag);
        paneel1.add(new JLabel("/"));
		paneel1.add(maand);
        paneel1.add(new JLabel("/"));
		paneel1.add(jaar);
        paneel1.add(new JLabel("(d/m/yyyy)"));

		JPanel paneel2 = new JPanel();
		paneel2.add(new JLabel(getBundle("Accounting").getString(
				"MESSAGE")));
		paneel2.add(bewijs);

		JPanel paneel3 = new JPanel();
		paneel3.add(ok);
		paneel3.add(clear);
        paneel3.add(save);
		debet = new JTextField(8);
		credit = new JTextField(8);
		debet.setEditable(false);
		credit.setEditable(false);
		paneel3.add(new JLabel(
				getBundle("Accounting").getString("TOTAL_DEBIT")));
		paneel3.add(debet);
		paneel3.add(new JLabel(getBundle("Accounting").getString(
				"TOTAL_CREDIT")));
		paneel3.add(credit);

		JPanel onder = new JPanel(new GridLayout(0, 1));
		onder.add(paneel1);
		onder.add(paneel2);
		onder.add(paneel3);

		add(onder, BorderLayout.SOUTH);

        refresh();
	}

    @Override
    public void focusGained(FocusEvent fe) {
    }

    private void setDate(Calendar date){
        if (date == null){
            dag.setText("");
            maand.setText("");
            jaar.setText("");
        }else{
            dag.setText(Utils.toDay(date)+"");
            maand.setText(Utils.toMonth(date)+"");
            jaar.setText(Utils.toYear(date)+"");
        }
    }

    private Calendar getDate(){
        return Utils.toCalendar(dag.getText(),maand.getText(),jaar.getText());
    }

    private String getDescription(){
        return bewijs.getText().trim();
    }

    @Override
    public void focusLost(FocusEvent fe) {
        Transaction transaction = journal.getCurrentObject();
        if(transaction!=null){
            Object source = fe.getSource();
            if(source == dag || source == maand || source == jaar){
                Calendar date = getDate();
                if (date != null){
                    transaction.setDate(date);
                    dag.setText(Utils.toDay(date)+"");
                    maand.setText(Utils.toMonth(date)+"");
                    jaar.setText(Utils.toYear(date)+"");
                }
            } else if (source == bewijs){
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(bewijs.getText().trim());
            }
        }
    }

    public void setAccounting(Accounting accounting){
        if(accounting==null || accounting.getJournals()==null){
            journal = null;
        } else {
            journal = accounting.getJournals().getCurrentObject();
        }
        if(journal==null){
            journalDataModel.setTransaction(null);
        } else {
            Transaction transaction = journal.getCurrentObject();
            journalDataModel.setTransaction(transaction);
        }
        journalDataModel.fireTableDataChanged();
    }

	public void refresh() {
        debettotaal = BigDecimal.ZERO;
        credittotaal = BigDecimal.ZERO;
        boolean okEnabled = false;
        boolean clearEnabled = false;
        String identification = "";
        String description = "";
        Calendar date = Calendar.getInstance();
        bewijs.setEnabled(false);
        dag.setEnabled(false);
        maand.setEnabled(false);
        jaar.setEnabled(false);
        if(journal!=null){
            Transaction transaction = journal.getCurrentObject();
            journalDataModel.setTransaction(transaction);
            journalDataModel.fireTableDataChanged();
            if(transaction!=null){
                debettotaal = transaction.getDebetTotaal();
                credittotaal = transaction.getCreditTotaal();
                date = transaction.getDate();
                description = transaction.getDescription();
                bewijs.setEnabled(true);
                dag.setEnabled(true);
                maand.setEnabled(true);
                jaar.setEnabled(true);
            }
            identification = journal.getAbbreviation() + " " + journal.getId();
            okEnabled = transaction!=null && !transaction.getBusinessObjects().isEmpty() && debettotaal.compareTo(credittotaal)==0 && debettotaal.compareTo(BigDecimal.ZERO)!=0;
            clearEnabled = transaction!=null && !transaction.getBusinessObjects().isEmpty();
        }
        ident.setText(identification);
        clear.setEnabled(clearEnabled);
        ok.setEnabled(okEnabled);
        save.setEnabled(clearEnabled);
        setDate(date);
        bewijs.setText(description);
        debet.setText(debettotaal.toString());
        credit.setText(credittotaal.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
			clear();
		} else if (e.getSource() == save) {
            saveTransaction();
        }
	}

    public Transaction saveTransaction(){
        Transaction transaction = journal.getCurrentObject();
        if(transaction!=null){
            Calendar date = getDate();
            if(date == null){
                JOptionPane.showMessageDialog(null, "Fill in date");
                return null;
            } else {
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(getDescription());
                transaction.setDate(getDate());
            }
        }
        return transaction;
    }

	public void clear() {
        Transaction transaction = new Transaction();
        transaction.setDate(getDate());
        journal.setCurrentObject(transaction);
        refresh();
	}
}