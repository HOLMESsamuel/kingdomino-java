import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

public class PageDeJeu {

	Shell shell;
	Jeu monJeu;
	Joueur joueur;
	String[][] tuile;
	int tailleTab;
	int choix;
	int ordonnee;
	int abscisse;
	List<int[]> actionFaite = new ArrayList<int[]>();

	Label lblNomJoueur;
	Label lblTuile;
	Label lblChoixPourLe;
	Label lblFin;

	Button btnCase1;
	Button btnCase2;
	int lBtn;
	Button[][] tableauBtn;
	Button[] listeBtnTuile;
	Label[] listeLblTuile;
	Button btnRoyaume;
	Button btnAnnuler;
	Button btnFinTour;

	Audio son;

	int x;
	int y;
	int sx;
	int sy;
	int test = 0;

	/**
	 * Open the window
	 * 
	 * @param shell
	 * @param monJeu
	 * @param joueur
	 * @param son
	 */
	public void open(Shell shell, Jeu monJeu, Joueur joueur, Audio son) {
		this.shell = shell;
		this.son = son;
		this.monJeu = monJeu;
		this.joueur = joueur;
		int numTuile = monJeu.getPioche().getTour().get(monJeu.getListeJoueurs().indexOf(joueur));
		this.tuile = monJeu.getPioche().getDomino(numTuile);
		this.tailleTab = monJeu.getTailleTab();

		if (monJeu.getListeJoueurs().indexOf(joueur) == 0) {
			System.out.println("Il reste " + monJeu.getMancheNb()+" manches \nLordre des joueurs est :");
			for (Joueur j : monJeu.getListeJoueurs()) { // affiche l'ordre des joueurs en debut de manche
				System.out.println(j.getColor() + " " + j.getName());
			}
			System.out.println("Liste des tuiles a choisir "+monJeu.getPioche().getNext()+"\n"); // affichage des tuilles prochain tour
		}
		System.out.println("Au joueur : "+joueur.getColor() + " " + joueur.getName());
		System.out.println("Tuile a jouer :"+numTuile + " \nTerrain actuel : ");
		joueur.getRoyaume().afficheRoyaume();
		
		Display display = Display.getDefault();
		createContents();
		this.shell.open();
		this.shell.layout();
		actionAutomatique();

		Runnable runnable = new Runnable() {
			public void run() {
				majIA();
				Display.getDefault().timerExec(1000, this); // permet de changer le temps de reponse de l'IA
			}
		};
		if (joueur.isIA()) {
			runnable.run();
		}

		while (!this.shell.isDisposed()) {

			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	/**
	 * Creat the contents
	 */
	protected void createContents() {
		shell.setText("Domination");
		shell.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				refresh();
			}
		});

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				try {
					son.stop();
				} catch (Exception ex) {

				}
			}
		});

		lblNomJoueur = new Label(shell, SWT.NONE);
		lblTuile = new Label(shell, SWT.NONE);
		lblChoixPourLe = new Label(shell, SWT.NONE);
		lblFin = new Label(shell, SWT.NONE);
		btnCase1 = new Button(shell, SWT.FLAT);
		btnCase2 = new Button(shell, SWT.FLAT);
		lBtn = 300 / tailleTab;
		tableauBtn = new Button[tailleTab][tailleTab];
		listeBtnTuile = new Button[monJeu.getTempTuileNext().size()];
		listeLblTuile = new Label[monJeu.getTempTuileNext().size()];
		btnRoyaume = new Button(shell, SWT.NONE);
		btnAnnuler = new Button(shell, SWT.NONE);
		btnFinTour = new Button(shell, SWT.NONE);

		lblNomJoueur.setAlignment(SWT.CENTER);
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.01));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.01));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.98));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblNomJoueur.setBounds(x, y, sx, sy);
		lblNomJoueur.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		if (monJeu.getMancheNb() == 1) {
			lblNomJoueur.setText("Derniere manche : " + joueur.getName());
		} else {
			lblNomJoueur.setText("Il reste " + monJeu.getMancheNb() + " manches : " + joueur.getName());
		}

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.7));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		sx = Math.max(Math.toIntExact(Math.round(shell.getSize().x * 0.06)),
				Math.toIntExact(Math.round(shell.getSize().y * 0.06)));
		sy = sx;
		btnCase1.setBounds(x, y, sx, sy);
		btnCase1.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent event) {
				event.gc.fillRectangle(event.x, event.y, event.width, event.height);
				ImageData img = SWTResourceManager.getImage(monJeu.getListeImage().get(tuile[0][0])).getImageData()
						.scaledTo(event.width, event.height);
				Image image = new Image(btnCase1.getDisplay(), img);
				event.gc.drawImage(image, 0, 0);
				event.gc.drawText(tuile[0][1], 2, 2);
			}
		});
		btnCase1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					System.out.println("case 1 choisi");
					choix = 0;
					enableCase(tuile[0]);
					caseApparence(1);
					break;
				}
			}
		});

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.8));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		sx = Math.max(Math.toIntExact(Math.round(shell.getSize().x * 0.06)),
				Math.toIntExact(Math.round(shell.getSize().y * 0.06)));
		sy = sx;
		btnCase2.setBounds(x, y, sx, sy);
		btnCase2.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent event) {
				event.gc.fillRectangle(event.x, event.y, event.width, event.height);
				ImageData img = SWTResourceManager.getImage(monJeu.getListeImage().get(tuile[1][0])).getImageData()
						.scaledTo(event.width, event.height);
				Image image = new Image(btnCase2.getDisplay(), img);
				event.gc.drawImage(image, 0, 0);
				event.gc.drawText(tuile[1][1], 2, 2);
			}
		});
		btnCase2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					System.out.println("case 2 choisi");
					choix = 1;
					enableCase(tuile[1]);
					caseApparence(2);
					break;
				}
			}
		});

		// tableau de boutons
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.05));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.12));
		lBtn = Math.min(Math.toIntExact(Math.round(shell.getSize().x * 0.5) / tailleTab),
				Math.toIntExact(Math.round(shell.getSize().y * 0.75) / tailleTab));
		for (int ligne = 0; ligne < tailleTab; ligne++) {
			for (int colonne = 0; colonne < tailleTab; colonne++) {
				final int l = ligne;
				final int c = colonne;
				tableauBtn[ligne][colonne] = new Button(shell, SWT.WRAP);
				tableauBtn[ligne][colonne].setBounds(x + colonne * lBtn, y + ligne * lBtn, lBtn, lBtn);
				tableauBtn[ligne][colonne].setEnabled(false);

				tableauBtn[l][c].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent event) {

						if (joueur.getRoyaume().getType(c, l) != null) {
							event.gc.fillRectangle(event.x, event.y, event.width, event.height);
							ImageData img = SWTResourceManager
									.getImage(monJeu.getListeImage().get(joueur.getRoyaume().getType(c, l)))
									.getImageData().scaledTo(event.width, event.height);
							Image image = new Image(tableauBtn[l][c].getDisplay(), img);
							event.gc.drawImage(image, 0, 0);
							if (joueur.getRoyaume().getCouronne(c, l) != "chateau") {
								event.gc.drawText(joueur.getRoyaume().getCouronne(c, l), 2, 2);
							}
						}
					}
				});

				tableauBtn[ligne][colonne].addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						switch (e.type) {
						case SWT.Selection:
							if (btnCase1.getEnabled()) {
								poser1case(c, l);
								enableCase(tuile[1 - choix]);
							} else {
								poser2case(c, l);
							}
							break;
						}
					}
				});
			}
		}

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.12));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		lblTuile.setBounds(x, y, sx, sy);
		lblTuile.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblTuile.setText("Tuile a placer pour ce tour :");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.40));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		lblChoixPourLe.setBounds(x, y, sx, sy);
		lblChoixPourLe.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblChoixPourLe.setText("Prochain tour :");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.6));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.45));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		if (monJeu.getMancheNb() != 1) {
			for (int i = 0; i < monJeu.getTempTuileNext().size(); i++) {
				final int index = i;
				listeBtnTuile[i] = new Button(shell, SWT.BORDER);
				listeBtnTuile[i].setBounds(x, y + i * (sy + 5), sx, sy);
				listeBtnTuile[i].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent event) {
						String[][] T = monJeu.getPioche().getDomino(monJeu.getTempTuileNext().get(index));
						event.gc.fillRectangle(event.x, event.y, event.width, event.height);
						ImageData img1 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[0][0])).getImageData()
								.scaledTo(event.width / 2, event.height);
						Image image1 = new Image(listeBtnTuile[index].getDisplay(), img1);
						if (monJeu.getTempTuileNext().size() == 1) {
							image1 = new Image(listeBtnTuile[index].getDisplay(), image1, SWT.IMAGE_GRAY);
						}
						event.gc.drawImage(image1, 0, 0);
						ImageData img2 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[1][0])).getImageData()
								.scaledTo(event.width / 2, event.height);
						Image image2 = new Image(listeBtnTuile[index].getDisplay(), img2);
						if (monJeu.getTempTuileNext().size() == 1) {
							image2 = new Image(listeBtnTuile[index].getDisplay(), image2, SWT.IMAGE_GRAY);
						}
						event.gc.drawImage(image2, event.width / 2, 0);
						event.gc.drawText(T[0][1], 2, 2);
						event.gc.drawText(T[1][1], event.width / 2 + 2, 2);
					}
				});
				listeBtnTuile[i].addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						switch (event.type) {
						case SWT.Selection:
							btnFinTour.setEnabled(true);
							joueur.setNextTuile(monJeu.getTempTuileNext().get(index));

							String[][] T = monJeu.getPioche().getDomino(monJeu.getTempTuileNext().get(index));
							tuileApparence(T, index);
							break;
						}
					}
				});
				listeLblTuile[i] = new Label(shell, SWT.NONE);
				listeLblTuile[i].setBounds(x - 30, (y + 2) + i * (sy + 5), 30, 30);
				listeLblTuile[i].setText(monJeu.getTempTuileNext().get(i).toString());
			}
		} else {
			lblFin.setBounds(x, y, sx, sy);
			lblFin.setFont(SWTResourceManager.getFont("Caladea", sy / 5, SWT.NORMAL));
			lblFin.setText("Dernier tour");

		}

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.8));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.45));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		btnRoyaume.setBounds(x, y, sx, sy);
		btnRoyaume.setText("Info");
		btnRoyaume.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					PageInfo maPageInfo = new PageInfo(shell, monJeu);
					// maPageInfo.createContents(monJeu);
					break;
				}
			}
		});

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.6));
		btnAnnuler.setBounds(x, y, sx, sy);
		btnAnnuler.setText("Annuler");
		btnAnnuler.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					if (actionFaite.size() >= 1) {
						for (int i = 0; i < actionFaite.size(); i++) {
							System.out.println("enlever " + actionFaite.get(i)[0] + " " + actionFaite.get(i)[1]);
							String[] t = new String[2];
							joueur.royaume.poseCase(actionFaite.get(i)[0], actionFaite.get(i)[1], t, true);
							tableauBtn[actionFaite.get(i)[1]][actionFaite.get(i)[0]]
									.addPaintListener(new PaintListener() {
										@Override
										public void paintControl(PaintEvent event) {
										}
									});
							tableauBtn[actionFaite.get(i)[1]][actionFaite.get(i)[0]].redraw();
						}
						btnCase1.setEnabled(true);
						btnCase2.setEnabled(true);
						caseApparence(0);
						for (int ligne = 0; ligne < tailleTab; ligne++) {
							for (int colonne = 0; colonne < tailleTab; colonne++) {
								tableauBtn[ligne][colonne].setEnabled(false);
							}
						}
						System.out.println("h: " + joueur.royaume.getEcartHorizontal()[0] + " "
								+ joueur.royaume.getEcartHorizontal()[1]);
						System.out.println("v: " + joueur.royaume.getEcartVertical()[0] + " "
								+ joueur.royaume.getEcartVertical()[1]);
						joueur.royaume.majEcart();
						System.out.println("apres maj :");
						System.out.println("h: " + joueur.royaume.getEcartHorizontal()[0] + " "
								+ joueur.royaume.getEcartHorizontal()[1]);
						System.out.println("v: " + joueur.royaume.getEcartVertical()[0] + " "
								+ joueur.royaume.getEcartVertical()[1]);
						actionFaite = new ArrayList<int[]>();
					}
					break;
				}
			}
		});

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		btnFinTour.setBounds(x, y, sx, sy);
		if (monJeu.getMancheNb() == 1) {
			btnFinTour.setText("Fin de jeu");
			btnFinTour.setEnabled(true);
		} else {
			btnFinTour.setText("Fin de tour");
			btnFinTour.setEnabled(false);
		}
		btnFinTour.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					actionBtnFin();
					break;
				}
			}
		});

	} // protected void createContents()

	/**
	 * Permet de suprimer les element qui compose le shell.
	 */
	public void disposeContents() {
		lblNomJoueur.dispose();
		lblTuile.dispose();
		lblChoixPourLe.dispose();
		lblFin.dispose();
		btnCase1.dispose();
		btnCase2.dispose();
		lBtn = 300 / tailleTab;
		for (int ligne = 0; ligne < tailleTab; ligne++) {
			for (int colonne = 0; colonne < tailleTab; colonne++) {
				tableauBtn[ligne][colonne].dispose();
			}
		}
		if (monJeu.getMancheNb() != 1) {
			for (int i = 0; i < monJeu.getTempTuileNext().size(); i++) {
				listeBtnTuile[i].dispose();
				listeLblTuile[i].dispose();
			}
		} else {
			lblFin.dispose();
		}
		btnRoyaume.dispose();
		btnAnnuler.dispose();
		btnFinTour.dispose();
	} // public void disposeContents()

	/**
	 * Met a jour les objets qui compose le shell en fonction de la taille de la
	 * fenetre.
	 */
	public void refresh() {

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.01));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.01));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.98));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		lblNomJoueur.setBounds(x, y, sx, sy);
		lblNomJoueur.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.7));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		sx = Math.max(Math.toIntExact(Math.round(shell.getSize().x * 0.06)),
				Math.toIntExact(Math.round(shell.getSize().y * 0.06)));
		sy = sx;
		btnCase1.setBounds(x, y, sx, sy);
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.8));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		sx = Math.max(Math.toIntExact(Math.round(shell.getSize().x * 0.06)),
				Math.toIntExact(Math.round(shell.getSize().y * 0.06)));
		sy = sx;
		btnCase2.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.05));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.12));
		lBtn = Math.min(Math.toIntExact(Math.round(shell.getSize().x * 0.5) / tailleTab),
				Math.toIntExact(Math.round(shell.getSize().y * 0.75) / tailleTab));
		for (int ligne = 0; ligne < tailleTab; ligne++) {
			for (int colonne = 0; colonne < tailleTab; colonne++) {
				tableauBtn[ligne][colonne].setBounds(x + colonne * lBtn, y + ligne * lBtn, lBtn, lBtn);
			}
		}

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.12));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		lblTuile.setBounds(x, y, sx, sy);
		lblTuile.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.40));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		lblChoixPourLe.setBounds(x, y, sx, sy);
		lblChoixPourLe.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.6));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.45));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		if (monJeu.getMancheNb() != 1) {
			for (int i = 0; i < monJeu.getTempTuileNext().size(); i++) {
				listeBtnTuile[i].setBounds(x, y + i * (sy + 5), sx, sy);
				listeLblTuile[i].setBounds(x - 30, (y + 2) + i * (sy + 5), 30, 30);
			}
		} else {
			lblFin.setBounds(x, y, sx, sy);
			lblFin.setFont(SWTResourceManager.getFont("Caladea", sy / 5, SWT.NORMAL));
		}

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.8));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.45));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		btnRoyaume.setBounds(x, y, sx, sy);
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.6));
		btnAnnuler.setBounds(x, y, sx, sy);
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		btnFinTour.setBounds(x, y, sx, sy);
	}

	/**
	 * Permet le clic sur les case jouable est le bloc la ou les regles l'empeche.
	 * 
	 * @param caseJoue
	 */
	public void enableCase(String[] caseJoue) {
		// bloc toute les case
		for (int colonne = 0; colonne < tableauBtn[0].length; colonne++) {
			for (int ligne = 0; ligne < tableauBtn[0].length; ligne++) {
				tableauBtn[ligne][colonne].setEnabled(false);
				if (btnCase1.getEnabled()) {
					if (joueur.getRoyaume().verification(colonne, ligne, caseJoue, 1)) {
						tableauBtn[ligne][colonne].setEnabled(true);
					}
				}
			}
		}

		if (!btnCase1.getEnabled()) {
			// verifi sur chaque case si elle repond au regles
			try {
				if (joueur.getRoyaume().verification(abscisse, ordonnee - 1, caseJoue, 2)) {
					tableauBtn[ordonnee - 1][abscisse].setEnabled(true);
				}
			} catch (Exception e) {
			}
			try {
				if (joueur.getRoyaume().verification(abscisse, ordonnee + 1, caseJoue, 2)) {
					tableauBtn[ordonnee + 1][abscisse].setEnabled(true);
				}
			} catch (Exception e) {
			}
			try {
				if (joueur.getRoyaume().verification(abscisse - 1, ordonnee, caseJoue, 2)) {
					tableauBtn[ordonnee][abscisse - 1].setEnabled(true);
				}
			} catch (Exception e) {
			}
			try {
				if (joueur.getRoyaume().verification(abscisse + 1, ordonnee, caseJoue, 2)) {
					tableauBtn[ordonnee][abscisse + 1].setEnabled(true);
				}
			} catch (Exception e) {
			}
		}

	} // public void enableCase()

	/**
	 * Place une case dans le royaume du joueur.
	 * 
	 * @param x
	 * @param y
	 */
	public void poser1case(int x, int y) {
		int[] t = new int[] { x, y };
		actionFaite.add(t);
		System.out.println("poser la premiere case");
		joueur.getRoyaume().poseCase(x, y, tuile[choix], true);

		tableauBtn[y][x].getParent().layout();
		btnCase1.setEnabled(false);
		btnCase2.setEnabled(false);
		ordonnee = y;
		abscisse = x;
	} // public void poser1case(int x, int y)

	/**
	 * Place une case dans le royaume du joueur.
	 * 
	 * @param x
	 * @param y
	 */
	public void poser2case(int x, int y) {
		int[] t = new int[] { x, y };
		actionFaite.add(t);
		System.out.println("poser la deuxieme case");
		joueur.getRoyaume().poseCase(x, y, tuile[1 - choix], true);

		for (int i = 0; i < tableauBtn[0].length; i++) {
			for (Button button : tableauBtn[i]) {
				button.setEnabled(false);
			}
		}
		caseApparence(3);

	} // public void poser2case(int x, int y)

	/**
	 * Regroupe les actions de fin dans une fonction.
	 */
	private void actionBtnFin() {
		if (actionFaite.size() == 1) {
			// si jamais le joueur n'a joué q'une demi tuile
			MessageBox dialog = new MessageBox(shell, SWT.OK);
			dialog.setText("Merci de jouer les deux cotes de la case");
			dialog.open();
		} else {
			disposeContents();
			monJeu.passerTour(joueur);
			if (monJeu.getListeJoueurs().indexOf(joueur) == monJeu.getListeJoueurs().size() - 1) {
				// si c'est le dernier joueur du tour
				if (monJeu.getMancheNb() == 1) {
					PageDeFin pageDeFin = new PageDeFin();
					pageDeFin.open(shell, monJeu, son);
					monJeu.fin();
					
					// si c'etait le dernier tour
				} else {
					monJeu.passerManche();
					PageDeJeu maPageDeJeu = new PageDeJeu();
					maPageDeJeu.open(shell, monJeu, monJeu.getListeJoueurs().get(0), son);
				}
			} else {
				PageDeJeu maPageDeJeu = new PageDeJeu();
				maPageDeJeu.open(shell, monJeu,
						monJeu.getListeJoueurs().get(monJeu.getListeJoueurs().indexOf(joueur) + 1), son);

			}

		}
	}

	/**
	 * Action a réaliser sous certzine condition (atribution de la derniere tuile /
	 * possiilité de jouer)
	 */
	public void actionAutomatique() {
		// atribution automatique en cas de tuile unique
		if (monJeu.getTempTuileNext().size() == 1) {
			joueur.setNextTuile(monJeu.getTempTuileNext().get(0));
			btnFinTour.setEnabled(true);
		}

		// verification possibilité de placer tuile
		List<List<int[]>> placePossible = new ArrayList<List<int[]>>();
		List<int[]> placeCase1 = new ArrayList<int[]>();
		List<int[]> placeCase2 = new ArrayList<int[]>();
		int compteurPlacePossible = 0;
		for (int colonne = 0; colonne < tableauBtn[0].length; colonne++) {
			for (int ligne = 0; ligne < tableauBtn[0].length; ligne++) {
				if (joueur.getRoyaume().verification(colonne, ligne, tuile[0], 1)) {
					placeCase1.add(new int[] { colonne, ligne });
					compteurPlacePossible++;
				}
				if (joueur.getRoyaume().verification(colonne, ligne, tuile[1], 1)) {
					placeCase2.add(new int[] { colonne, ligne });
					compteurPlacePossible++;
				}
			}
		}
		placePossible.add(placeCase1);
		placePossible.add(placeCase2);

		if (compteurPlacePossible == 0 && !joueur.isIA()) {
			MessageBox dialog = new MessageBox(shell, SWT.OK);
			dialog.setText("No possible move");
			dialog.open();
		}

	} // public void actionAutomatique()

	/**
	 * Permet de changer la couleur des case de la tuile a joueur pour mettre en
	 * avant celle qui ont été ou reste a jouer.
	 * 
	 * @param c
	 */
	public void caseApparence(int c) {
		// 0 pas de case grise
		// 1 case 1 grise
		// 2 case 2 grise
		// 3 case 1 et 2 grise
		btnCase1.setEnabled(true);
		btnCase1.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent event) {
				event.gc.fillRectangle(event.x, event.y, event.width, event.height);
				// recreer l'image en la mettant en gris ou non
				ImageData img = SWTResourceManager.getImage(monJeu.getListeImage().get(tuile[0][0])).getImageData()
						.scaledTo(event.width, event.height);
				Image image = new Image(btnCase1.getDisplay(), img);
				if (c == 1 || c == 3) {
					image = new Image(btnCase1.getDisplay(), image, SWT.IMAGE_GRAY);
				}
				event.gc.drawImage(image, 0, 0);
				event.gc.drawText(tuile[0][1], 2, 2);
			}
		});
		btnCase1.redraw();
		btnCase2.setEnabled(true);
		btnCase2.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent event) {
				event.gc.fillRectangle(event.x, event.y, event.width, event.height);
				ImageData img = SWTResourceManager.getImage(monJeu.getListeImage().get(tuile[1][0])).getImageData()
						.scaledTo(event.width, event.height);
				Image image = new Image(btnCase2.getDisplay(), img);
				if (c == 2 || c == 3) {
					image = new Image(btnCase1.getDisplay(), image, SWT.IMAGE_GRAY);
				}
				event.gc.drawImage(image, 0, 0);
				event.gc.drawText(tuile[1][1], 2, 2);
			}
		});
		btnCase2.redraw();
	} // public void caseApparence(int c)

	/**
	 * Permet de modifier l'apparence des tuiles a choisir.
	 * 
	 * @param T
	 * @param index
	 */
	public void tuileApparence(String[][] T, int index) {
		System.out.println("Choisit tuile en position " + index+"\n");
		for (int i = 0; i < monJeu.getTempTuileNext().size(); i++) {
			if (i != index) {
				final int ind = i;
				listeBtnTuile[ind].addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent event) {
						String[][] T = monJeu.getPioche().getDomino(monJeu.getTempTuileNext().get(ind));
						event.gc.fillRectangle(event.x, event.y, event.width, event.height);
						ImageData img1 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[0][0])).getImageData()
								.scaledTo(event.width / 2, event.height);
						Image image1 = new Image(listeBtnTuile[ind].getDisplay(), img1);
						event.gc.drawImage(image1, 0, 0);
						ImageData img2 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[1][0])).getImageData()
								.scaledTo(event.width / 2, event.height);
						Image image2 = new Image(listeBtnTuile[ind].getDisplay(), img2);
						event.gc.drawImage(image2, event.width / 2, 0);
						event.gc.drawText(T[0][1], 2, 2);
						event.gc.drawText(T[1][1], event.width / 2 + 2, 2);
					}
				});
				listeBtnTuile[ind].redraw();
			}
		}
		listeBtnTuile[index].addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent event) {
				String[][] T = monJeu.getPioche().getDomino(monJeu.getTempTuileNext().get(index));
				event.gc.fillRectangle(event.x, event.y, event.width, event.height);
				ImageData img1 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[0][0])).getImageData()
						.scaledTo(event.width / 2, event.height);
				Image image1 = new Image(listeBtnTuile[index].getDisplay(), img1);
				Image disable1 = new Image(listeBtnTuile[index].getDisplay(), image1, SWT.IMAGE_GRAY);
				event.gc.drawImage(disable1, 0, 0);
				ImageData img2 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[1][0])).getImageData()
						.scaledTo(event.width / 2, event.height);
				Image image2 = new Image(listeBtnTuile[index].getDisplay(), img2);
				Image disable2 = new Image(listeBtnTuile[index].getDisplay(), image2, SWT.IMAGE_GRAY);
				event.gc.drawImage(disable2, event.width / 2, 0);
				event.gc.drawText(T[0][1], 2, 2);
				event.gc.drawText(T[1][1], event.width / 2 + 2, 2);
			}
		});
		listeBtnTuile[index].redraw();

	} // public void tuileApparence(String[][] T, int index)

	/**
	 * Permet de temporiser les actyion de L'IA.
	 */
	public void majIA() {
		if (test == 3) {
			System.out.println("IA ferme la page\n");
			actionBtnFin();
			test++;
		} else if (test == 2) {
			System.out.println("IA choisi sa tuile");
			// on creer l'ia pour ce tour
			IA joueurIA = new IA();
			// on choisi la prochaine tuile
			if (monJeu.getTempTuileNext().size() >= 2) {
				joueur.setNextTuile(joueurIA.choisirTuile(joueur.getRoyaume(), monJeu.getTempTuileNext()));
			}
			try {
				tuileApparence(Pioche.dominos.get(joueur.getNextTuile()),
						monJeu.getTempTuileNext().indexOf(joueur.getNextTuile()));
			} catch (Exception e) {
			}
			test++;
		} else if (test == 1) {
			System.out.println("IA pose sa tuile");
			// on creer l'ia pour ce tour
			IA joueurIA = new IA();
			// on pose la tuile a jouer
			joueur.setRoyaume(joueurIA.poseTuile(joueur.getRoyaume(),
					joueurIA.trouverPlacePossible(joueur.getRoyaume(), tuile), tuile));
			btnFinTour.setEnabled(true);

			for (int ligne = 0; ligne < tailleTab; ligne++) {
				for (int colonne = 0; colonne < tailleTab; colonne++) {
					final int l = ligne;
					final int c = colonne;
					tableauBtn[l][c].addPaintListener(new PaintListener() {
						@Override
						public void paintControl(PaintEvent event) {

							if (joueur.getRoyaume().getType(c, l) != null) {
								event.gc.fillRectangle(event.x, event.y, event.width, event.height);
								ImageData img = SWTResourceManager
										.getImage(monJeu.getListeImage().get(joueur.getRoyaume().getType(c, l)))
										.getImageData().scaledTo(event.width, event.height);
								Image image = new Image(tableauBtn[l][c].getDisplay(), img);
								event.gc.drawImage(image, 0, 0);
								if (joueur.getRoyaume().getCouronne(c, l) != "chateau") {
									event.gc.drawText(joueur.getRoyaume().getCouronne(c, l), 2, 2);
								}
							}
						}
					});
					tableauBtn[l][c].redraw();
				}
			}
			test++;
		} else {
			test++;
		}
	} // public void majIA(long startTime)

}
