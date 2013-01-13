package be.dafke.Accounting;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("ctx.xml");
//		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
//		BeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
//		reader.loadBeanDefinitions(new ClassPathResource("beans.xml"));
//		bf.getBean("accountingMainPanel");
//		Accountings accountings = (Accountings) ctx.getBean("accountings");
//
//		new AccountingGUIFrame(
//				java.util.ResourceBundle.getBundle("Accounting").getString("BOEKHOUDING"), accountings);
		((AccountingGUIFrame) ctx.getBean("accountingMainPanel")).setVisible(true);
	}
}
