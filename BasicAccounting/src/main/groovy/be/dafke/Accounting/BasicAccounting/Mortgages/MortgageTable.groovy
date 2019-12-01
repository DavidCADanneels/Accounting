package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.Mortgages
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import javax.swing.*
import java.awt.*

class MortgageTable extends JFrame {
    final JButton save
    final MortgageDataModel model
    final JTable tabel
    final BigDecimal startCapital

    static int counter = 1
    protected final int nr
    Mortgage mortgage
    Mortgages mortgages


    MortgageTable(Mortgage mortgage, BigDecimal startCapital, Mortgages mortgages) {
        super("Aflossingstabel")
        nr = counter++
        this.mortgage = mortgage
        this.mortgages = mortgages
        this.startCapital = startCapital
        model = new MortgageDataModel(mortgage)
        tabel = new JTable(model)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))
        JScrollPane scrollPane = new JScrollPane(tabel)
        JPanel panel = new JPanel(new BorderLayout())
        panel.add(scrollPane, BorderLayout.CENTER)
        save = new JButton("Save table")
        save.addActionListener({ e -> save() })
        panel.add(save, BorderLayout.SOUTH)
        setContentPane(panel)
        pack()
        setVisible(true)
    }

    void save() {
        String name = JOptionPane.showInputDialog(this, "Enter a name for the table.")
        mortgage.setName(name)
        mortgage.setStartCapital(startCapital)
        try {
            mortgages.addBusinessObject(mortgage)
            dispose()
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.MORTGAGE_DUPLICATE_NAME)
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.MORTGAGE_NAME_EMPTY)
        }
        Main.fireMortgageAddedOrRemoved(mortgages)
    }
}
