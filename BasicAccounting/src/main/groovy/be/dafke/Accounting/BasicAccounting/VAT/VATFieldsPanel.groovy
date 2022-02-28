package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BasicAccounting.Contacts.ContactsPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Accounting.BusinessModelDao.VATWriter

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import javax.swing.filechooser.FileNameExtensionFilter
import java.awt.*
import java.util.List

import static be.dafke.Accounting.BusinessModelDao.VATWriter.Period.QUARTER
import static javax.swing.BoxLayout.Y_AXIS

class VATFieldsPanel extends JPanel {
    static final String CREATE_FILE = "Create file"
    static final String SALES_AT_0 = "Sales at 0%"
    static final String SALES_AT_6 = "Sales at 6%"
    static final String SALES_AT_12 = "Sales at 12%"
    static final String SALES_AT_21 = "Sales at 21%"
    static final String TAX_ON_SALES_0_3 = "Tax on Sales (0-3)"
    static final String CN_ON_SALES = "CN on Sales"
    static final String TAX_ON_CN = "Tax on CN"
    static final String TAX_ON_ICL = "Tax on ICL"
    static final String SALES_ICL_TURNOVER = "Turnover ICL"
    static final String PURCHASE_OF_SUPPLIES = "Purchase of supplies"
    static final String PURCHASE_OF_SERVICES = "Purchase of services"
    static final String PURCHASE_OF_INVESTMENTS = "Purchase of investments"
    static final String PURCHASE_ICL = "Purchase ICL"
    static final String TAX_ON_PURCHASES_81_83 = "Tax on Purchases (81-83)"
    static final String CN_ON_PURCHASES = "CN on Purchases"
    HashMap<String, JTextField> textFields = new HashMap<>()
    VATFields vatFields

    VATFieldsPanel(VATFields vatFields) {
        this.vatFields = vatFields
        JPanel left = createSalesPanel()
        JPanel right = createPurchasePanel()
        JPanel totals = createTotalsPanel()
        JPanel buttonPanel = createButtonPanel()
        setLayout(new BoxLayout(this,Y_AXIS))
        add(left)
        add(right)
        add(totals)
        add(buttonPanel)
    }

    JPanel createFieldPanel(String nr, String description){
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        JPanel line1 = new JPanel()

        line1.add(new JLabel(nr))
        JTextField textField = new JTextField(vatFields.getBusinessObject(nr).amount.toString())
        textField.setEditable(false)
        textField.setHorizontalAlignment(JTextField.RIGHT)
        textFields.put(nr,textField)
        line1.add(textField)
        textField.setToolTipText(description)

        JPanel line2 = new JPanel()
        line2.add(new JLabel(description))

        panel.add(line2)
        panel.add(line1)
        panel
    }

    JPanel createSalesPanel() {
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Sales"))
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS))

        JPanel left = createSalesMainPanel()
        JPanel right = createSalesTaxAndCNPanel()

        panel.add(left)
        panel.add(right)

        panel
    }

    JPanel createSalesMainPanel() {
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        panel.add(createFieldPanel("0", SALES_AT_0))
        panel.add(createFieldPanel("1", SALES_AT_6))
        panel.add(createFieldPanel("2", SALES_AT_12))
        panel.add(createFieldPanel("3", SALES_AT_21))
        panel
    }

    JPanel createSalesTaxAndCNPanel() {
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        panel.add(createFieldPanel("54", TAX_ON_SALES_0_3))
        panel.add(createFieldPanel("49", CN_ON_SALES))
        panel.add(createFieldPanel("64", TAX_ON_CN))
        panel.add(createFieldPanel("46", SALES_ICL_TURNOVER))
        panel
    }

    JPanel createPurchasePanel() {
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Purchases"))
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS))

        JPanel left = createPurchaseMainPanel()
        JPanel right = createPurchaseTaxAndCNPanel()

        panel.add(left)
        panel.add(right)

        panel
    }

    JPanel createPurchaseMainPanel() {
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS))
        panel.add(createFieldPanel("81", PURCHASE_OF_SUPPLIES))
        panel.add(createFieldPanel("82", PURCHASE_OF_SERVICES))
        panel.add(createFieldPanel("83", PURCHASE_OF_INVESTMENTS))
        panel.add(createFieldPanel("86", PURCHASE_ICL))
        panel
    }

    JPanel createPurchaseTaxAndCNPanel() {
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS))
        panel.add(createFieldPanel("59", TAX_ON_PURCHASES_81_83))
        panel.add(createFieldPanel("85", CN_ON_PURCHASES))
        panel.add(createFieldPanel("63", TAX_ON_CN))
        panel.add(createFieldPanel("55", TAX_ON_ICL))
        panel
    }

    JPanel createTotalsPanel() {
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Totals"))
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS))

        JPanel left = createTotalsLeftPanel()
        JPanel right = createTotalsRightPanel()

        panel.add(left)
        panel.add(right)

        panel
    }

    JPanel createTotalsLeftPanel(){
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS))
        panel.add(createFieldPanel("XX", "54+55+63"))
        panel.add(createFieldPanel("YY", "59+64"))
        panel
    }

    JPanel createTotalsRightPanel(){
        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS))
        panel.add(createFieldPanel("71", "XX-YY"))
        panel.add(createFieldPanel("72", "YY-XX"))
        panel
    }

    JPanel createButtonPanel() {
        JPanel panel = new JPanel()
        JTextField year = new JTextField(6)
        JTextField nr = new JTextField(4)
        panel.add(new JLabel("Year:"))
        panel.add(year)
        panel.add(new JLabel("Month/Quarter:"))
        panel.add(nr)
        JButton button = new JButton(CREATE_FILE)
        button.addActionListener({ e ->
            Contact companyContact = Session.activeAccounting.getCompanyContact()
            if (companyContact == null) {
                ContactsPanel.setCompanyContact(Session.activeAccounting)
            }
            JFileChooser fileChooser = new JFileChooser()
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML_EXTENSION files", "xml"))
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile()
                VATWriter.writeVATFields(vatFields, selectedFile, year.getText(), nr.getText(), companyContact, QUARTER)
//                VATTransactions vatTransactions = Session.activeAccounting.vatTransactions
//                vatTransactions.registerVATTransactions(selectedVatTransactions)
                Main.fireVATFieldsUpdated()
            }
        })
        panel.add(button)
        panel
    }
}
