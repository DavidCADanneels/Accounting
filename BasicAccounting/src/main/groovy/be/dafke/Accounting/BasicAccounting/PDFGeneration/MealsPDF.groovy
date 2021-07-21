package be.dafke.Accounting.BasicAccounting.PDFGeneration

import be.dafke.Accounting.BusinessModelDao.PDFCreator
import org.apache.fop.apps.FOPException

import javax.xml.transform.TransformerException

class MealsPDF {
    static String xslPathWithAllergenes = "data/accounting/xsl/meals/withAllergenes.xsl"
    static String xslPathWithInstructions = "data/accounting/xsl/meals/withInstructions.xsl"
    static String xslPathWithIngredients = "data/accounting/xsl/meals/withIngredients.xsl"

    static void main(String[] args) {
        String xmlPath = "data/accounting/xml/Accountings/Thai_NK-2019/MealDetails.xml"
        createPdf(xmlPath, "data/accounting/xml/MealsWithAllergenesTest.pdf", xslPathWithAllergenes)
        createPdf(xmlPath, "data/accounting/xml/MealsWithInstructionsTest.pdf", xslPathWithInstructions)
    }

    static void createPdf(String xmlPath, String pdfPath, String xslPath){
        try {
            PDFCreator.convertToPDF(xmlPath, xslPath, pdfPath)
        } catch (IOException | FOPException | TransformerException e) {
            e.printStackTrace()
        }
    }
}
