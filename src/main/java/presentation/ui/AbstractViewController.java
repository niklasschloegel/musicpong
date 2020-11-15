package presentation.ui;

import business.MP3Player;
import javafx.scene.layout.Pane;

public abstract class AbstractViewController<T> {
	protected Pane rootView;
	protected T application;
	protected MP3Player mp3Player;

	public AbstractViewController() {
	}

	public AbstractViewController(T application) {
		this.application = application;
	}

	public AbstractViewController(T application, MP3Player mp3Player) {
		this.application = application;
		this.mp3Player = mp3Player;
	}

	public Pane getRootView() {
		return rootView;
	}

	abstract public void initialize();

	public void setApplication(T application) {
		this.application = application;
	}
}
