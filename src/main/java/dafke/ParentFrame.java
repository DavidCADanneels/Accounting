package be.dafke;

import java.util.ArrayList;

import javax.swing.JFrame;

public class ParentFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<RefreshableFrame> frames;

	public ParentFrame(String title) {
		super(title);
		frames = new ArrayList<RefreshableFrame>();
	}

	public void closeChildFrame(RefreshableFrame frame) {
		frames.remove(frame);
		// frame = null;
	}

	public void addChildFrame(RefreshableFrame frame) {
		// if(!frames.contains(frame)){
		frames.add(frame);
		// }
	}

	public void repaintAllFrames() {
		for(int i = 0; i < frames.size(); i++) {
			RefreshableFrame frame = frames.get(i);
			frame.refresh();
		}
		repaint();
	}

	public void closeAllFrames() {
		for(int i = 0; i < frames.size(); i++) {
			frames.get(i).dispose();
		}
	}

	public boolean containsFrame(RefreshableFrame frame) {
		return frames.contains(frame);
	}
}
