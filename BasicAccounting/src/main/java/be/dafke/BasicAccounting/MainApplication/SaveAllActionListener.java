package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accountings;
import be.dafke.ObjectModelDao.XMLWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class SaveAllActionListener extends WindowAdapter implements ActionListener {
    private Accountings accountings;
    private static final ArrayList<JFrame> disposableComponents = new ArrayList<>();

    public SaveAllActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        closeAllFrames();
        saveData();
    }

    public static void closeAllFrames(){
        for(JFrame frame: disposableComponents){
            frame.dispose();
        }
    }

    public static void addFrame(JFrame frame) {
        disposableComponents.add(frame);
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

//        XMLtoHTMLWriter.toHtml(accountings, xmlFolder, xslFolder, htmlFolder);
/*
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
    */
    }
}
