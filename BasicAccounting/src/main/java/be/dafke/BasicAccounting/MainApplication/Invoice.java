package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModelDao.PDFCreator;
import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Invoice {
    public static void main(String[] args) {
        String xmlPath = "data/accounting/xml/invoice.xml";
        String xslPath = "data/accounting/xsl/invoice.xsl";
        String pdfPath = "data/accounting/xml/Invoice.pdf";
        createInvoice(xmlPath, xslPath, pdfPath);
    }


    public static void createInvoice(String xmlPath, String xslPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath);
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace();
        }
    }
}