package be.dafke.Accounting.Objects;

import org.springframework.context.ApplicationEvent;

public class RefreshEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefreshEvent(Object source) {
		super(source);
//		super.notifyAll();
		// TODO Auto-generated constructor stub
	}

}
