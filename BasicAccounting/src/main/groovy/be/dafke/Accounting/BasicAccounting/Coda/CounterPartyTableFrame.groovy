package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BasicAccounting.Accounts.Selector.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.ComponentModel.SelectableTableModel

import javax.swing.*
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.regex.Pattern

class CounterPartyTableFrame extends JFrame implements MouseListener {
    final Statements statements
    Accounts accounts
    AccountTypes accountTypes
    JTable tabel
    CounterPartyDataModel dataModel
    static final HashMap<CounterParties, CounterPartyTableFrame> counterpartiesGuis = new HashMap<>()

    CounterPartyTableFrame(CounterParties counterParties, Statements statements) {
        super("Counterparties")
        this.statements = statements

        dataModel = new CounterPartyDataModel(counterParties)

        tabel = new JTable(dataModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))
        tabel.setRowSorter(null)
        JScrollPane scrollPane = new JScrollPane(tabel)
        JPanel contentPanel = new JPanel(new BorderLayout())
        contentPanel.add(scrollPane, BorderLayout.CENTER)
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)

        setContentPane(contentPanel)
        pack()

        tabel.addMouseListener(this)
    }

    static CounterPartyTableFrame showStatements(Statements statements, CounterParties counterParties) {
        CounterPartyTableFrame gui = counterpartiesGuis.get(counterParties)
        if(gui == null){
            gui = new CounterPartyTableFrame(counterParties, statements)
            counterpartiesGuis.put(counterParties,gui)
            Main.addFrame(gui)
        }
        gui
    }

    void setAccounting(Accounting accounting){
        accounts = accounting.accounts
        accountTypes = accounting.accountTypes
    }

    void refresh() {
//		tabel.refresh()
        dataModel.fireTableDataChanged()
    }

    void mouseClicked(MouseEvent me) {
        Point cell = me.getPoint()
//		Point location = me.getLocationOnScreen()
        if (me.getButton() == 3) {
            int col = tabel.columnAtPoint(cell)
            int row = tabel.rowAtPoint(cell)
            if (col == 0) {
                CounterParty counterParty = (CounterParty) tabel.getValueAt(row, col)
                System.out.println(counterParty.name)
                for(BankAccount account : counterParty.getBankAccounts().values()) {
                    System.out.println(account.getAccountNumber())
                    System.out.println(account.getBic())
                    System.out.println(account.getCurrency())
                }
                SearchOptions searchOptions = new SearchOptions()
                searchOptions.setCounterParty(counterParty)
                searchOptions.setSearchOnCounterParty(true)
                new GenericStatementTableFrame(searchOptions, statements).visible = true
                // parent.addChildFrame(refreshTable)
            } else if (col == 1){
                String alias = (String) tabel.getValueAt(row, col)
                if(alias != null && !alias.equals("")){
                    String[] aliases = alias.split(Pattern.quote(" | "))
                    int result = JOptionPane.showOptionDialog(this,"Select new name", "Select new name",
                            JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE, null,aliases,aliases[0])
                    if(result != JOptionPane.CLOSED_OPTION){
                        CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0)
                        String name = counterParty.name
                        counterParty.setName(aliases[result])
                        counterParty.removeAlias(aliases[result])
                        // TODO: ask user if old name should be saved as alias
                        counterParty.addAlias(name)
                        ((SelectableTableModel<BusinessObject>)tabel.getModel()).fireTableDataChanged()
                    }
                }
            } else if (col == 5) {
                AccountSelectorDialog sel = AccountSelectorDialog.getAccountSelector(accounts, accountTypes.businessObjects)
                sel.visible = true
                Account account = sel.getSelection()

                if (account != null) {
                    CounterParty counterParty = (CounterParty) tabel.getValueAt(row, 0)
                    counterParty.setAccount(account)
                    refresh()
                }
            }

        }
    }

    void mouseEntered(MouseEvent e) {
    }

    void mouseExited(MouseEvent e) {
    }

    void mousePressed(MouseEvent e) {
    }

    void mouseReleased(MouseEvent e) {
    }

//    @Override
//    void selectObject(CounterParty counterParty) {
//
//    }
//
//    @Override
//    CounterParty getSelectedObject() {
//        null
//    }
}
