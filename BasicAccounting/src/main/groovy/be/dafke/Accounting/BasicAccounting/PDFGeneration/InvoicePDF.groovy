package be.dafke.Accounting.BasicAccounting.PDFGeneration

import be.dafke.Accounting.BusinessModelDao.PDFCreator
import org.apache.fop.apps.FOPException

import javax.xml.transform.TransformerException

class InvoicePDF {
    static String xslPath = "data/accounting/xsl/invoice.xsl"

    static void main(String[] args) {
        String xmlPath = "data/accounting/xml/invoice.xml"
        String pdfPath = "data/accounting/xml/Invoice20.pdf"
        createInvoice(xmlPath, pdfPath)
    }

    static void createInvoice(String xmlPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath)
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace()
        }
    }
}
