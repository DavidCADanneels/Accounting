package be.dafke.BasicAccounting.Coda;

import be.dafke.Accounting.BusinessModel.SearchOptions;
import be.dafke.Accounting.BusinessModel.Statements;

import javax.swing.*;
import java.awt.*;

public class GenericStatementTableFrame extends JFrame {
    private JTable tabel;
    private GenericStatementDataModel dataModel;

	public GenericStatementTableFrame(SearchOptions searchOptions,
                                      Statements statements) {
		super("Statements where"+
                (searchOptions.isSearchOnCounterParty()?" [counterParty = "+searchOptions.getCounterParty()+"]":"")+
                (searchOptions.isSearchOnTransactionCode()? " [transactioncode = "+searchOptions.getTransactionCode()+"]":"")+
                (searchOptions.isSearchOnCommunication()? " [communication = "+searchOptions.getCommunication()+"]":""));
        dataModel = new GenericStatementDataModel(searchOptions,statements);

        tabel = new JTable(dataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setContentPane(contentPanel);
        pack();
	}

    public void refresh() {
//		tabel.refresh();
        dataModel.fireTableDataChanged();
    }
}
