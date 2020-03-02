package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class OpenCustomerInvoicesPanel extends JPanel {

    final JButton pay, receive
    final SelectableTable<SalesOrder> tabel
    OpenCustomerInvoicesTableModel openCustomerInvoicesTableModel
    Accounting accounting

    OpenCustomerInvoicesPanel() {
        openCustomerInvoicesTableModel = new OpenCustomerInvoicesTableModel()
        tabel = new SelectableTable<>(openCustomerInvoicesTableModel)
//        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))

//        DefaultListSelectionModel selection = new DefaultListSelectionModel()
//        selection.addListSelectionListener(this)
//        tabel.setSelectionModel(selection)

        JScrollPane scrollPane = new JScrollPane(tabel)

        pay = new JButton("Pay (CN)")
        receive = new JButton("Receive")

        pay.addActionListener({ e ->
            Account account = selectedAccount
            if (account) {
                AccountActions.book account, true, null, accounting, this
            }
        })

        receive.addActionListener({ e ->
            Account account = selectedAccount
            if (account) {
                AccountActions.book account, false, null, accounting, this
            }
        })

        JPanel buttonPanel = new JPanel()
        buttonPanel.add receive
        buttonPanel.add pay

        setLayout(new BorderLayout())
        add scrollPane, BorderLayout.CENTER
        add buttonPanel, BorderLayout.SOUTH
    }

    Account getSelectedAccount(){
        SalesOrder salesOrder = tabel.getSelectedObject()
        salesOrder?salesOrder.customer.customerAccount:null
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        openCustomerInvoicesTableModel.setSalesOrders accounting?accounting.salesOrders:null
    }
}