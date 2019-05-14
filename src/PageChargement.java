
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class PageChargement {

	static int count = 0;
	static Label lblChargement;
	static Button btn;

	static int x;
	static int y;
	static int sx;
	static int sy;
	
	Audio son;

	Boolean isVisible = true;

	Shell shell;

	/**
	 * Ouvre la page utilisée pour le chargement
	 * @param shell
	 */
	public PageChargement(Shell shell) {
		final Display display = shell.getDisplay();
		
		son = new Audio();
		son.state(false);

		lblChargement = new Label(shell, SWT.NO_BACKGROUND);
		lblChargement.setAlignment(SWT.CENTER);
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.35));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.7));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		lblChargement.setBounds(x, y, sx, sy);
		lblChargement.setFont(SWTResourceManager.getFont("Caladea", sy / 4, SWT.NORMAL));
		lblChargement.setText("Chargement...");
		BufferedImage imgB;
		try {
			// creer l'image rogné qui sert de fond pour le text
			imgB = ImageIO.read(new File("./img/charge.PNG"));
			int cx = Math.toIntExact(Math.round(imgB.getWidth() * 0.35));
			int cy = Math.toIntExact(Math.round(imgB.getHeight() * 0.7));
			int csx = Math.toIntExact(Math.round(imgB.getWidth() * 0.3));
			int csy = Math.toIntExact(Math.round(imgB.getHeight() * 0.2));
			BufferedImage out = imgB.getSubimage(cx, cy, csx, csy);
			File outF = new File("img/chargeCrop.PNG");
			ImageIO.write(out, "PNG", outF);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ImageData img = SWTResourceManager.getImage("./img/chargeCrop.PNG").getImageData().scaledTo(sx, sy);
		Image image = new Image(lblChargement.getDisplay(), img);
		lblChargement.setBackgroundImage(image);

		x = 0;
		y = 0;
		sx = Math.toIntExact(Math.round(shell.getSize().x));
		sy = Math.toIntExact(Math.round(shell.getSize().y));
		btn = new Button(shell, SWT.NONE);
		btn.setBounds(x, y, sx, sy);
		ImageData img2 = SWTResourceManager.getImage("./img/charge.PNG").getImageData().scaledTo(sx, sy);
		Image image2 = new Image(btn.getDisplay(), img2);
		btn.setImage(image2);

		// met en place l'action du bouton / fond
		btn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					Shell sh = new Shell();
					sh.setSize(shell.getSize());
					sh.setLocation(shell.getLocation());
					sh.addListener(SWT.Close, new Listener() {
						public void handleEvent(Event event) {
							MessageBox messageBox = new MessageBox(sh, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
							messageBox.setText("Warning");
							messageBox.setMessage("Quiter le jeu?");
							event.doit = messageBox.open() == SWT.YES;
							
							try {
								son.stop();
							} catch(Exception ex) {
								
							}
						}
					});
					shell.close();
					PageAcceuil maPageDeJeu = new PageAcceuil();
					maPageDeJeu.open(sh, son);
					break;
				}
			}
		});

		// creation d'un timer pour fair clignoter le texte
		final int time = 1000;
		final Runnable timer = new Runnable() {
			public void run() {
				if (!shell.isDisposed()) {
					isVisible = lblChargement.getVisible() == true ? false : true;
					lblChargement.setVisible(isVisible);
					count++;
					if (count == 6) {
						lblChargement.setText("Cliquer pour continuer");
					}
					display.timerExec(time, this);
				}
			}
		};
		display.timerExec(time, timer);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
