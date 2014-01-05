package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
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

public class JournalGUI extends AccountingPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalDataModel journalDataModel;
	private final JTextField debet, credit, dag, bewijs, ident;
	private final JButton ok, clear;
    private final JPopupMenu popup;
    private final JTable table;
    private final JMenuItem delete;
//    private final JMenuItem edit;
    protected Calendar date;
	private BigDecimal debettotaal, credittotaal;
    private Journal journal;
    private int selectedRow;

    public JournalGUI() {
		debettotaal = new BigDecimal(0);
		credittotaal = new BigDecimal(0);
		date = Calendar.getInstance();
		setLayout(new BorderLayout());
		journalDataModel = new JournalDataModel();
		table = new JTable(journalDataModel);
		table.setPreferredScrollableViewportSize(new Dimension(800, 200));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

        popup = new JPopupMenu();
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
//        edit = new JMenuItem(getBundle("Accounting").getString("EDIT"));
        delete.addActionListener(this);
//        edit.addActionListener(this);
        popup.add(delete);
//        popup.add(edit);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Point cell = me.getPoint();//
                Point location = me.getLocationOnScreen();
                selectedRow = table.rowAtPoint(cell);
                if(selectedRow !=-1){
                    popup.show(null, location.x, location.y);
                }
            }
        });

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               popup.setVisible(false);
            }
        });

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

		ok = new JButton(getBundle("Accounting").getString("OK"));
		ok.addActionListener(this);
		ok.setEnabled(false);
		clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"));
		clear.addActionListener(this);
        clear.setEnabled(false);

		JPanel paneel1 = new JPanel();
		paneel1.add(new JLabel(
				getBundle("Accounting").getString("TRANSACTION")));
		paneel1.add(ident);
		paneel1.add(new JLabel(getBundle("Accounting").getString("DATE")));
		paneel1.add(dag);
        paneel1.add(new JLabel("(d/m/yyyy)"));

		JPanel paneel2 = new JPanel();
		paneel2.add(new JLabel(getBundle("Accounting").getString(
				"MESSAGE")));
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
	}

    public void setAccounting(Accounting accounting){
        if(accounting==null || accounting.getJournals()==null){
            journal = null;
        } else {
            journal = accounting.getJournals().getCurrentObject();
            if(journal==null){
                journalDataModel.setTransaction(null);
            } else {
                Transaction transaction = journal.getCurrentObject();
                journalDataModel.setTransaction(transaction);
            }
            journalDataModel.fireTableDataChanged();
        }
    }

	public void refresh() {
        if(journal!=null){
            Transaction transaction = journal.getCurrentObject();
            journalDataModel.setTransaction(transaction);
            journalDataModel.fireTableDataChanged();
            if(transaction!=null){
                debettotaal = transaction.getDebetTotaal();
                credittotaal = transaction.getCreditTotaal();
            } else {
                debettotaal = BigDecimal.ZERO;
                credittotaal = BigDecimal.ZERO;
            }
            debet.setText(debettotaal.toString());
            credit.setText(credittotaal.toString());
            ident.setText(journal.getAbbreviation() + " "
                    + journal.getId());
            boolean valid = transaction!=null && !transaction.getBookings().isEmpty() && debettotaal.compareTo(credittotaal)==0 && debettotaal.compareTo(BigDecimal.ZERO)!=0;
            clear.setEnabled(transaction!=null && !transaction.getBookings().isEmpty());
            ok.setEnabled(valid);
        } else {
            ident.setText("");
            ok.setEnabled(false);
            clear.setEnabled(false);
	    }
	}

    private void menuAction(JMenuItem source) {
        popup.setVisible(false);
        Booking booking = journalDataModel.getValueAt(selectedRow);
        Transaction transaction = booking.getTransaction();
        if (source == delete) {
            transaction.removeBooking(booking);
//        } else if (source == edit) {
//
        }
        AccountingComponentMap.refreshAllFrames();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            menuAction((JMenuItem) e.getSource());
        } else if (e.getSource() == ok) {
            if(date == null){
                JOptionPane.showMessageDialog(null, "Fill in date");
            } else {
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                Transaction transaction = journal.getCurrentObject();
                if(transaction!=null){
                    transaction.setDescription(bewijs.getText().trim());
                    transaction.setDate(date);
                    journal.addBusinessObject(transaction);
                    clear();
                    bewijs.setText("");
                    transaction = new Transaction();
                    transaction.setDate(date);
                    journal.setCurrentObject(transaction);
                }
                AccountingComponentMap.refreshAllFrames();
            }
		} else if (e.getSource() == clear) {
			clear();
		}
	}

	public void clear() {
        Transaction transaction = new Transaction();
        transaction.setDate(date);
        journal.setCurrentObject(transaction);
        ok.setEnabled(false);
        refresh();
	}
}