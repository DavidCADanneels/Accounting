package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.JournalGUIPopupMenu;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;



/**
 * Created by ddanneels on 29/04/2016.
 */
public class JournalInputGUI extends AccountingPanel implements FocusListener, ActionListener{
    private static final long serialVersionUID = 1L;
    private final JTextField debet, credit, dag, maand, jaar, bewijs, ident;
    private final JButton singleBook, save, clear;
    private final RefreshableTable<Booking> inputTable;
    private final JournalGUIPopupMenu inputPopup;
    private final JournalDataModel journalDataModel;
    private BigDecimal debettotaal, credittotaal;

    private Journal journal;
    private Accounts accounts;

    public JournalInputGUI(Accounting accounting) {
        if(accounting!=null) {
            journal = accounting.getJournals().getCurrentObject();
        }
        setLayout(new BorderLayout());
        debettotaal = new BigDecimal(0);
        credittotaal = new BigDecimal(0);

        journalDataModel = new JournalDataModel();

        inputTable = new RefreshableTable<Booking>(journalDataModel);
        inputTable.setPreferredScrollableViewportSize(new Dimension(800, 200));

        inputPopup = new JournalGUIPopupMenu(inputTable, accounting);
        inputTable.addMouseListener(new PopupForTableActivator(inputPopup, inputTable));

        JPanel center = new JPanel();
        JScrollPane scrollPane2 = new JScrollPane(inputTable);
        center.add(scrollPane2);
        add(center, BorderLayout.CENTER);

        scrollPane2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                inputPopup.setVisible(false);
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

        singleBook = new JButton(getBundle("Accounting").getString("OK"));
        singleBook.addActionListener(this);
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
        paneel3.add(singleBook);
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
        setAccounting(accounting);
        refresh();

    }

    public void setAccounting(Accounting accounting){
        inputPopup.setAccounting(accounting);
        if (accounting!=null) {
            accounts = accounting.getAccounts();
            if (accounting.getJournals() != null) {
                journal = accounting.getJournals().getCurrentObject();
            } else {
                journal = null;
            }
        }else {
            accounts = null;
        }

        if(journal==null){
            journalDataModel.setTransaction(null);
        } else {
            Transaction transaction = journal.getCurrentObject();
            journalDataModel.setTransaction(transaction);
        }
        journalDataModel.fireTableDataChanged();
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

    public void focusGained(FocusEvent fe) {
        JTextField field = (JTextField)fe.getComponent();
        field.selectAll();
    }

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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            clear();
        } else if (e.getSource() == save) {
            saveTransaction();
        } else if (e.getSource() == singleBook){
            Transaction transaction = saveTransaction();
            if(journal!=null && transaction!=null && transaction.isBookable()){
                journal.addBusinessObject(transaction);
                clear();
                ComponentMap.refreshAllFrames();
            }
        }
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
            okEnabled = transaction!=null && transaction.isBookable();
            clearEnabled = transaction!=null && !transaction.getBusinessObjects().isEmpty();
        }
        ident.setText(identification);
        clear.setEnabled(clearEnabled);
        singleBook.setEnabled(okEnabled);
        save.setEnabled(clearEnabled);
        setDate(date);
        bewijs.setText(description);
        debet.setText(debettotaal.toString());
        credit.setText(credittotaal.toString());
    }


    private Calendar getDate(){
        return Utils.toCalendar(dag.getText(),maand.getText(),jaar.getText());
    }

    private String getDescription(){
        return bewijs.getText().trim();
    }

    public Transaction saveTransaction(){
        Transaction transaction = journal.getCurrentObject();
        if(transaction!=null){
            Calendar date = getDate();
            if(date == null){
                ActionUtils.showErrorMessage(ActionUtils.FILL_IN_DATE);
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
        Transaction transaction = new Transaction(accounts, getDate(), "");
        journal.setCurrentObject(transaction);
        refresh();
    }
}
