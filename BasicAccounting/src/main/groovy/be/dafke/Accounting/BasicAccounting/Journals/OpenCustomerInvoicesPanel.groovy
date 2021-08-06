package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class OpenCustomerInvoicesPanel extends JPanel {

    final JButton pay, receive
    final SelectableTable<SalesOrder> tabel
    OpenCustomerInvoicesTableModel openCustomerInvoicesTableModel

    OpenCustomerInvoicesPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Open Sales Orders"))
        openCustomerInvoicesTableModel = new OpenCustomerInvoicesTableModel()
        tabel = new SelectableTable<>(openCustomerInvoicesTableModel)

        JScrollPane scrollPane = new JScrollPane(tabel)

        pay = new JButton("Pay (CN)")
        receive = new JButton("Receive")

        pay.addActionListener({ e ->
            SalesOrder salesOrder = tabel.getSelectedObject()
            if(salesOrder) {
                AccountActions.book selectedAccount, true, null, this, salesOrder
//                refresh()
            }
        })

        receive.addActionListener({ e ->
            SalesOrder salesOrder = tabel.getSelectedObject()
            if(salesOrder) {
                AccountActions.book selectedAccount, false, null, this, salesOrder
//                refresh()
            }
        })

        JPanel buttonPanel = new JPanel()
        buttonPanel.add receive
        buttonPanel.add pay

        setLayout(new BorderLayout())
        add scrollPane, BorderLayout.CENTER
        add buttonPanel, BorderLayout.SOUTH
    }

    void refresh(){
        openCustomerInvoicesTableModel.fireTableDataChanged()
    }

    Account getSelectedAccount(){
        SalesOrder salesOrder = tabel.getSelectedObject()
        salesOrder?.customer?.customer?.customerAccount?:null
    }
}