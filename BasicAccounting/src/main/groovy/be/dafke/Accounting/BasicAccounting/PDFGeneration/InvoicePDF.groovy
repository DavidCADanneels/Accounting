package be.dafke.Accounting.BasicAccounting.PDFGeneration

import be.dafke.Accounting.BusinessModelDao.PDFCreator
import org.apache.fop.apps.FOPException

import javax.xml.transform.TransformerException

class InvoicePDF {
    static String xslPathInvoice = "data/accounting/xsl/invoice.xsl"
    static String xslPathCreditNote = "data/accounting/xsl/cn.xsl"

    static void main(String[] args) {
        String xmlPath = "data/accounting/xml/invoice.xml"
        String pdfPath = "data/accounting/xml/Invoice20.pdf"
        createInvoice(xmlPath, pdfPath)
    }

    static void createInvoice(String xmlPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPathInvoice, pdfPath)
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace()
        }
    }

    static void createCreditNote(String xmlPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPathCreditNote, pdfPath)
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace()
        }
    }
}
