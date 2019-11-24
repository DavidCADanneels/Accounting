package be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class AccountsTablePopupMenu extends JPopupMenu {
    private final JMenuItem manage, add, edit, debit, credit, details

    AccountsTablePopupMenu(AccountsTablePanel accountsGUI) {

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"))
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"))
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_ACCOUNT"))
        debit = new JMenuItem(getBundle("Accounting").getString("DEBIT"))
        credit = new JMenuItem(getBundle("Accounting").getString("CREDIT"))
        details = new JMenuItem(getBundle("Accounting").getString("VIEW_ACCOUNT"))

        add(debit)
        add(credit)
        add(details)
        add(edit)
        addSeparator()
        add(add)
        add(manage)

        debit.addActionListener({ e -> accountsGUI.book(true) })
        credit.addActionListener({ e -> accountsGUI.book(false) })
        manage.addActionListener({ e -> accountsGUI.manageAccounts() })
        add.addActionListener({ e -> accountsGUI.addAccount() })
        details.addActionListener({ e -> accountsGUI.showDetails() })
        edit.addActionListener({ e -> accountsGUI.editAccount() })
    }
}