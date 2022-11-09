package be.dafke.Accounting.BusinessModelDao

import org.apache.fop.cli.InputHandler

class XMLtoHTMLWriter {

    static void xmlToHtml(File xmlFile, File xslFile, File htmlFile, Vector params) {
        InputHandler inputHandler = new InputHandler(xmlFile, xslFile, params)
        try {
            if (!htmlFile.exists()) {
//                htmlFile.getParentFile().mkdirs()
                if(htmlFile.createNewFile()){
                    System.out.println("${htmlFile} has been created")
                }
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(htmlFile))
            inputHandler.transformTo(out)
        } catch (Exception e) {
            e.printStackTrace()
        }
    }


}