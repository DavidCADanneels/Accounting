package be.dafke.Accounting.BasicAccounting.PDFGeneration

import be.dafke.Accounting.BusinessModelDao.PDFCreator
import org.apache.fop.apps.FOPException

import javax.xml.transform.TransformerException

class MealsPDF {
    private static String xslPath = "data/accounting/xsl/food.xsl"

    static void main(String[] args) {
        String xmlPath = "data/accounting/xml/Accountings/Thai_NK-2019/MealDetails.xml"
        String pdfPath = "data/accounting/xml/MealsPDF.pdf"
        createPdf(xmlPath, pdfPath)
    }

    static void createPdf(String xmlPath, String pdfPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath)
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace()
        }
    }
}
