package be.dafke.Accounting.GUI;

import be.dafke.Accounting.Dao.XML.AccountingSAXParser;
import be.dafke.Accounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.Accounting.Objects.Accounting.Accountings;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("ctx.xml");
//		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
//		BeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
//		reader.loadBeanDefinitions(new ClassPathResource("beans.xml"));
//		bf.getBean("accountingMainPanel");


//		Accountings accountings = (Accountings) ctx.getBean("accountings");


//        Accountings accountings = new Accountings();
//        accountings.fromXML();

        Accountings accountings = AccountingSAXParser.fromXML();
        AccountingGUIFrame frame = new AccountingGUIFrame(
				java.util.ResourceBundle.getBundle("Accounting").getString("BOEKHOUDING"), accountings);
        frame.setVisible(true);


//		((AccountingGUIFrame) ctx.getBean("accountingMainPanel")).setVisible(true);
	}
}
