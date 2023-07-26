import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PageOption {

	protected Shell shell;
	Label lblOption;
	Label lblTaille;
	Label lblMusique;

	Button btnFullScreen;
	Button btn600400;
	Button btn1200800;
	Button btnMusique;

	Button btnApply;

	int x;
	int y;
	int sx;
	int sy;

	Audio son;

	/**
	 * Open the window.
	 * @param shell
	 * @param son
	 */
	public void open(Shell shell, Audio son) {
		Display display = Display.getDefault();
		this.son = son;
		createContents(shell);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	/**
	 * Create contents of the window.
	 * @param shell
	 */
	public void createContents(Shell shell) {

		//initialise les composent de la fenetre
		lblOption = new Label(shell, SWT.NONE);
		lblTaille = new Label(shell, SWT.NONE);
		lblMusique = new Label(shell, SWT.NONE);

		btnFullScreen = new Button(shell, SWT.CHECK);
		btn600400 = new Button(shell, SWT.CHECK);
		btn1200800 = new Button(shell, SWT.CHECK);
		btnMusique = new Button(shell, SWT.CHECK);
		btnApply = new Button(shell, SWT.NONE);

		// Bandeau titre
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.01));
		y = 0;
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.98));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblOption.setAlignment(SWT.CENTER);
		lblOption.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblOption.setBounds(x, y, sx, sy);
		lblOption.setText("Option");
		
		// Taille de fenetre
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.5));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblTaille.setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
		lblTaille.setBounds(x, y, sx, sy);
		lblTaille.setText("Taille de la fenetre : ");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.3));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		btnFullScreen.setBounds(x, y, sx, sy);
		btnFullScreen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (btn1200800.getSelection()) {
					btn1200800.setSelection(false);
				}
				if (btn600400.getSelection()) {
					btn600400.setSelection(false);
				}
			};
		});
		btnFullScreen.setText("Full Screen");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.4));
		btn600400.setBounds(x, y, sx, sy);
		btn600400.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (btnFullScreen.getSelection()) {
					btnFullScreen.setSelection(false);
				}
				if (btn1200800.getSelection()) {
					btn1200800.setSelection(false);
				}
			};
		});
		btn600400.setText("100*500");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.6));
		btn1200800.setBounds(x, y, sx, sy);
		btn1200800.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (btnFullScreen.getSelection()) {
					btnFullScreen.setSelection(false);
				}
				if (btn600400.getSelection()) {
					btn600400.setSelection(false);
				}
			};
		});
		btn1200800.setText("1200*700");

		// Musique
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.5));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblMusique.setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
		lblMusique.setBounds(x, y, sx, sy);
		lblMusique.setText("Musique : ");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.5));
		btnMusique.setBounds(x, y, sx, sy);
		btnMusique.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

			};
		});

		btnMusique.setText("On");

		// Apply
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.4));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.7));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		btnApply.setBounds(x, y, sx, sy);
		btnApply.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					Shell sh = new Shell();
					if (btnFullScreen.getSelection()) {
						sh.setMaximized(true);
					} else if (btn600400.getSelection()) {
						sh.setSize(1000, 500);
					} else {
						sh.setSize(1200, 700);
					}
					if (btnMusique.getSelection() && !son.getRunning()) {
						son = new Audio();
						son.start();
						son.state(true);
					} else {
						if (son.getRunning() && !btnMusique.getSelection()) {
							son.stop();
							son.state(false);
						}

					}
					sh.addListener(SWT.Close, new Listener() {
						public void handleEvent(Event event) {
							MessageBox messageBox = new MessageBox(sh, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
							messageBox.setText("Warning");
							messageBox.setMessage("Quiter le jeu?");
							event.doit = messageBox.open() == SWT.YES;
							try {
								son.stop();

							} catch (Exception ex) {

							}
						}
					});
					shell.dispose();

					PageAcceuil maPageDeJeu = new PageAcceuil();
					maPageDeJeu.open(sh, son);
				}
			}
		});
		btnApply.setText("Apply");

	}

}
