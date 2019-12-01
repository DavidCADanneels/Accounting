package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.event.FocusEvent
import java.awt.event.FocusListener

import static java.util.ResourceBundle.getBundle 

class DateAndDescriptionPanel extends JPanel implements FocusListener {
    private JTextField dag, maand, jaar, description
    private Transaction transaction
    private Journal journal

    DateAndDescriptionPanel() {
        dag = new JTextField(2)
        maand = new JTextField(2)
        jaar = new JTextField(4)
        dag.addFocusListener(this)
        maand.addFocusListener(this)
        jaar.addFocusListener(this)
        description = new JTextField(20)
        description.addFocusListener(this)

        JPanel panel1 = new JPanel()
        panel1.add(new JLabel(getBundle("Accounting").getString("DATE")))
        panel1.add(dag)
        panel1.add(new JLabel("/"))
        panel1.add(maand)
        panel1.add(new JLabel("/"))
        panel1.add(jaar)
        panel1.add(new JLabel("(d/m/yyyy)"))

        JPanel panel2 = new JPanel()
        panel2.add(new JLabel(getBundle("Accounting").getString("MESSAGE")))
        panel2.add(description)

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS))
        add(panel1)
        add(panel2)
    }

    void addDescriptionFocusListener(FocusListener listener){
        description.addFocusListener(listener)
    }

    Calendar getDate(){
        Utils.toCalendar(dag.getText(),maand.getText(),jaar.getText())
    }

    String getDescription(){
        description.getText().trim()
    }

    void focusGained(FocusEvent fe) {
        JTextField field = (JTextField)fe.getComponent()
        field.selectAll()
    }

    void focusLost(FocusEvent fe) {
        if(transaction!=null){
            Object source = fe.getSource()
            if(source == dag || source == maand || source == jaar){
                Calendar date = getDate()
                if (date != null){
                    transaction.setDate(date)
                    dag.setText(Utils.toDay(date)+"")
                    maand.setText(Utils.toMonth(date)+"")
                    jaar.setText(Utils.toYear(date)+"")
                }
            } else if (source == description){
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.setDescription(description.getText().trim())
            }
        }
    }

    void fireTransactionDataChanged() {
        setDate(transaction==null?Calendar.getInstance():transaction.getDate())
        description.setEnabled((transaction!=null))
        dag.setEnabled((transaction!=null))
        maand.setEnabled((transaction!=null))
        jaar.setEnabled((transaction!=null))
        description.setText(transaction==null?"":transaction.getDescription())

        boolean okEnabled = journal!=null && transaction!=null && transaction.isBookable()
        boolean clearEnabled = journal!=null && transaction!=null && !transaction.getBusinessObjects().isEmpty()
    }

    void setDate(Calendar date){
        if (date == null){
            dag.setText("")
            maand.setText("")
            jaar.setText("")
        }else{
            dag.setText(Utils.toDay(date)+"")
            maand.setText(Utils.toMonth(date)+"")
            jaar.setText(Utils.toYear(date)+"")
        }
    }

    void setTransaction(Transaction transaction) {
        this.transaction = transaction
    }

    Transaction getTransaction() {
        transaction
    }

    void setJournal(Journal journal) {
        this.journal = journal
    }

    Journal getJournal() {
        journal
    }

    void setDescription(String description) {
        this.description.setText(description)
    }

    void enableDescription(boolean enabled){
        description.setEnabled(enabled)
    }

    void enableDate(boolean enabled){
        dag.setEnabled(enabled)
        maand.setEnabled(enabled)
        jaar.setEnabled(enabled)
    }
}
