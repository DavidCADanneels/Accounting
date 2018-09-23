package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class MortgageTable extends JFrame {
	private final JButton save;
	private final MortgageDataModel model;
	private final JTable tabel;
	private final BigDecimal startCapital;

    private static int counter = 1;
    protected final int nr;
    private Mortgage mortgage;
    private Mortgages mortgages;


    public MortgageTable(Mortgage mortgage, BigDecimal startCapital, Mortgages mortgages) {
		super("Aflossingstabel");
        nr = counter++;
        this.mortgage = mortgage;
        this.mortgages = mortgages;
		this.startCapital = startCapital;
		model = new MortgageDataModel(mortgage);
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		save = new JButton("Save table");
		save.addActionListener(e -> save());
		panel.add(save, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
		setVisible(true);
	}

	public void save() {
		String name = JOptionPane.showInputDialog(this, "Enter a name for the table.");
        mortgage.setName(name);
        mortgage.setStartCapital(startCapital);
        try {
            mortgages.addBusinessObject(mortgage);
            dispose();
        } catch (DuplicateNameException e) {
			ActionUtils.showErrorMessage(this, ActionUtils.MORTGAGE_DUPLICATE_NAME);
        } catch (EmptyNameException e) {
			ActionUtils.showErrorMessage(this, ActionUtils.MORTGAGE_NAME_EMPTY);
        }
        Main.fireMortgageAddedOrRemoved(mortgages);
	}
}
