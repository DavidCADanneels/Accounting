package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class MortgageTable extends RefreshableFrame implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JButton save;
	private final MortgageDataModel model;
	private final RefreshableTable<Mortgage> tabel;
	private final BigDecimal startCapital;
	private final Accounting accounting;

    private static int counter = 1;
    protected final int nr;
    private Mortgage mortgage;
    private Mortgages mortgages;


    public MortgageTable(Mortgage mortgage, BigDecimal startCapital, Accounting accounting, Mortgages mortgages) {
		super("Aflossingstabel");
        nr = counter++;
        this.mortgages = mortgages;
		this.accounting = accounting;
		this.startCapital = startCapital;
		model = new MortgageDataModel(mortgage);
		tabel = new RefreshableTable<Mortgage>(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		save = new JButton("Save table");
		save.addActionListener(this);
		panel.add(save, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
		setVisible(true);
	}

	public void refresh() {
		model.fireTableDataChanged();
	}

	public void actionPerformed(ActionEvent arg0) {
		String name = JOptionPane.showInputDialog(this, "Enter a name for the table.");
        mortgage.setName(name);
        mortgage.setStartCapital(startCapital);
        try {
//            accounting.getAccounts().addBusinessObject(mortgage);    // this is implicitely done in the next step
            mortgages.addBusinessObject(mortgage);
            ComponentMap.refreshAllFrames();
            dispose();
        } catch (DuplicateNameException e) {
			ActionUtils.showErrorMessage(ActionUtils.MORTGAGE_DUPLICATE_NAME);
        } catch (EmptyNameException e) {
			ActionUtils.showErrorMessage(ActionUtils.MORTGAGE_NAME_EMPTY);
        }
	}
}
