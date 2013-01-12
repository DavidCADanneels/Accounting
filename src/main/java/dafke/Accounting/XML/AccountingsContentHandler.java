package be.dafke.Accounting.XML;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;

public class AccountingsContentHandler extends DefaultHandler {
	private final File file;

	public AccountingsContentHandler(File file) {
		this.file = file;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("Accounting")) {
			String name = atts.getValue("name");
			File folder = FileSystemView.getFileSystemView().getChild(file.getParentFile(), name);
			File subFile = FileSystemView.getFileSystemView().getChild(folder, "object");
			System.out.println(name);
			Accounting acc = Accountings.openObject(subFile);
			if (acc != null) {
				Accountings.addAccounting(acc);
			}
		}
	}
}
