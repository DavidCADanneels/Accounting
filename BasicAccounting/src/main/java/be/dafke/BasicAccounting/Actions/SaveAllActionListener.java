package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Dao.MortgagesSAXParser;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModelDao.XMLtoHTMLWriter;
import be.dafke.ObjectModelDao.XMLWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class SaveAllActionListener extends WindowAdapter implements ActionListener {
    private Accountings accountings;

    public SaveAllActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        ComponentMap.closeAllFrames();
        saveData();
    }

    public void actionPerformed(ActionEvent e) {
        saveData();
    }

    private void saveData(){
        File xmlFolder = accountings.getXmlFolder();
        xmlFolder.mkdirs();
        XMLWriter.writeCollection(accountings, xmlFolder, 0);

        File xslFolder = accountings.getXslFolder();
        File htmlFolder = accountings.getHtmlFolder();
        htmlFolder.mkdirs();

        XMLtoHTMLWriter.toHtml(accountings, xmlFolder, xslFolder, htmlFolder);

        // TODO: remove this by refactoring Extension and write methods
        // only used for Mortgages, does not work well
        for(Accounting accounting : accountings.getBusinessObjects()){
            File rootFolder = new File(accountings.getXmlFolder(), "Accountings");
            File subFolder = new File(rootFolder, accounting.getName());
            File mortgagesFolder = new File(subFolder, "Mortgages");
            Mortgages mortgages = accounting.getMortgages();
            for(BusinessObject businessObject : mortgages.getBusinessObjects()){
                Mortgage mortgage = (Mortgage) businessObject;
                MortgagesSAXParser.writeMortgage(mortgage, mortgagesFolder, XMLWriter.getXmlHeader(mortgage, 3));
            }
        }
    }
}
