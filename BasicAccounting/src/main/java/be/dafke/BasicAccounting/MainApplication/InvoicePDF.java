package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModelDao.PDFCreator;
import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class InvoicePDF {
    private static String xslPath = "data/accounting/xsl/invoice.xsl";

    public static void main(String[] args) {
        String xmlPath = "data/accounting/xml/invoice.xml";
        String pdfPath = "data/accounting/xml/Invoice20.pdf";
        createInvoice(xmlPath, pdfPath);
    }

    public static void createInvoice(String xmlPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath);
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace();
        }
    }
}