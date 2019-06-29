package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModelDao.PDFCreator;
import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class BalancePDF {
    public static void main(String[] args) {
        String xslFile = "data/accounting/xsl/goods.xsl";
        String xmlFile = "data/accounting/xml/invoice.xml";
        String pdfFile = "data/accounting/xml/Raoul.pdf";
        createInvoice(xmlFile, xslFile, pdfFile);
    }


    public static void createInvoice(String xmlPath, String xslPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath);
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace();
        }
    }
}