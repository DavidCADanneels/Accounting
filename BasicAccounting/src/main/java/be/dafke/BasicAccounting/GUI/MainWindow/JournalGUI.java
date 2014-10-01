package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accounts;
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

public class JournalGUI extends AccountingPanel implements ActionListener, FocusListener {
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
    private final JMenuItem edit;
    private final JMenuItem change;
	private BigDecimal debettotaal, credittotaal;
    private Journal journal;
    private int selectedRow;
    private Accounts accounts;
    private Accounting accounting;

    public JournalGUI() {
		debettotaal = new BigDecimal(0);
		credittotaal = new BigDecimal(0);
		setLayout(new BorderLayout());
		journalDataModel = new JournalDataModel();
		table = new JTable(journalDataModel);
		table.setPreferredScrollableViewportSize(new Dimension(800, 200));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

        popup = new JPopupMenu();
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_AMOUNT"));
        change = new JMenuItem(getBundle("Accounting").getString("CHANGE_ACCOUNT"));
        delete.addActionListener(this);
        edit.addActionListener(this);
        change.addActionListener(this);
        popup.add(delete);
        popup.add(edit);
        popup.add(change);
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
		dag.addFocusListener(this);
		bewijs = new JTextField(30);
        bewijs.addFocusListener(this);

		ok = new JButton(getBundle("Accounting").getString("OK"));
		ok.addActionListener(this);
		clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"));
		clear.addActionListener(this);

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

    @Override
    public void focusLost(FocusEvent fe) {
        Transaction transaction = journal.getCurrentObject();
        if(transaction!=null){
            Object source = fe.getSource();
            if(source == dag){
                Calendar date = Utils.toCalendar(dag.getText());
                transaction.setDate(date);
                if (date == null){
                    dag.setText("");
                    JOptionPane.showMessageDialog(null,"invalid date");
                }else{
                    dag.setText(Utils.toString(date));
                }
            } else if (source == bewijs){
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(bewijs.getText().trim());
            }
        }
    }

    public void setAccounting(Accounting accounting){
        this.accounting = accounting;
        if(accounting==null || accounting.getJournals()==null){
            journal = null;
            accounts = null;
        } else {
            journal = accounting.getJournals().getCurrentObject();
            accounts = accounting.getAccounts();
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
            }
            identification = journal.getAbbreviation() + " " + journal.getId();
            okEnabled = transaction!=null && !transaction.getBusinessObjects().isEmpty() && debettotaal.compareTo(credittotaal)==0 && debettotaal.compareTo(BigDecimal.ZERO)!=0;
            clearEnabled = transaction!=null && !transaction.getBusinessObjects().isEmpty();
        }
        ident.setText(identification);
        clear.setEnabled(clearEnabled);
        ok.setEnabled(okEnabled);
        dag.setText(Utils.toString(date));
        bewijs.setText(description);
        debet.setText(debettotaal.toString());
        credit.setText(credittotaal.toString());
    }

    private void menuAction(JMenuItem source) {
        popup.setVisible(false);
        Booking booking = journalDataModel.getValueAt(selectedRow);
        Transaction transaction = booking.getTransaction();
        if (source == delete) {
            transaction.removeBusinessObject(booking);
        } else if (source == edit) {

        } else if (source == change) {
            AccountSelector sel = new AccountSelector(accounts, this);
            sel.setVisible(true);
            Account account = sel.getSelection();
            if(account!=null){
                booking.setAccount(account);
            }
        }
        AccountingComponentMap.refreshAllFrames();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: implement ActionListener in AccountSelector or separate Action
        if(AccountingActionListener.NEW_ACCOUNT.equals(e.getActionCommand())){
            new NewAccountGUI(accounting).setVisible(true);
        }

        // TODO: use actionCommand i.s.o. Object (getSource()) --> or later: Actions
        if (e.getSource() instanceof JMenuItem) {
            menuAction((JMenuItem) e.getSource());
        } else if (e.getSource() == ok) {
            Transaction transaction = journal.getCurrentObject();
            if(transaction!=null){
                Calendar date = transaction.getDate();
                if(date == null){
                    JOptionPane.showMessageDialog(null, "Fill in date");
                } else {
                    journal.addBusinessObject(transaction);
                    clear();
                    AccountingComponentMap.refreshAllFrames();
                }
            }
		} else if (e.getSource() == clear) {
			clear();
		}
	}

	public void clear() {
        Transaction transaction = new Transaction();
        transaction.setDate(Utils.toCalendar(dag.getText().trim()));
        journal.setCurrentObject(transaction);
        refresh();
	}
}