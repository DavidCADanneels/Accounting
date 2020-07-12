package be.dafke.Accounting.BusinessModelDao

import org.apache.fop.apps.FOPException
import org.apache.fop.apps.FOUserAgent
import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants

import javax.xml.transform.Result
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class PDFCreator {

    static void main(String[] args) {
        try {
            PDFCreator.convertToPDF("F:\\Temp\\Employees.xml","F:\\Temp\\template.xsl", "F:\\Temp\\employee.pdf")
        } catch (FOPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }


    /**
     * Method that will convert the given XML to PDF 
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    static void convertToPDF(String xmlPath, String xslPath, String pdfPath)  throws IOException, FOPException, TransformerException {
        // the XSL FO file
        File xsltFile = new File(xslPath)
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File(xmlPath))
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI())
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent()
        // Setup output
        OutputStream out
        out = new FileOutputStream(pdfPath)

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out)

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance()
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile))

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler())

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then 
            // PDF is created
            transformer.transform(xmlSource, res)
        } finally {
            out.close()
        }
    }

    /**
     * This method will convert the given XML to XSL-FO
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    void convertToFO(String xmlPath, String xslPath, String xmlFoPath)  throws IOException, FOPException, TransformerException {
        // the XSL FO file
        File xsltFile = new File(xslPath)


        /*TransformerFactory factory = TransformerFactory.newInstance()
        Transformer transformer = factory.newTransformer(new StreamSource("F:\\Temp\\template.xsl"))*/

        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File(xmlPath))

        // a user agent is needed for transformation
        /*FOUserAgent foUserAgent = fopFactory.newFOUserAgent()*/
        // Setup output
        OutputStream out

        out = new java.io.FileOutputStream(xmlFoPath)

        try {
            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance()
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile))

            // Resulting SAX events (the generated FO) must be piped through to FOP
            //Result res = new SAXResult(fop.getDefaultHandler())

            Result res = new StreamResult(out)

            //Start XSLT transformation and FOP processing
            transformer.transform(xmlSource, res)


            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then 
            // PDF is created
            transformer.transform(xmlSource, res)
        } finally {
            out.close()
        }
    }
}
