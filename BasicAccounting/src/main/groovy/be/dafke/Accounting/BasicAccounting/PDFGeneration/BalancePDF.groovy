package be.dafke.Accounting.BasicAccounting.PDFGeneration

import be.dafke.Accounting.BusinessModelDao.PDFCreator
import org.apache.fop.apps.FOPException

import javax.xml.transform.TransformerException

class BalancePDF {
    static void main(String[] args) {
        String xslFile = "data/accounting/xsl/goods.xsl"
        String xmlFile = "data/accounting/xml/invoice.xml"
        String pdfFile = "data/accounting/xml/Scan.pdf"
        createInvoice(xmlFile, xslFile, pdfFile)
    }


    static void createInvoice(String xmlPath, String xslPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath)
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace()
        }
    }
}
