import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class PageDeFin {

	protected Shell shell;
	Label lblFinDeLa;
	Label[] listeLblJoueur;
	Label[] listeLblGagnant;
	Label[] listeLblNbTerrain;
	Label[] listeLblCouronne;
	Label[] listeLblGrand;
	Label[][][] listeTabLbl;

	Button btnRestart;
	Button btnMenu;
	Button btnQuitter;
	
	Audio son;

	Jeu monJeu;
	List<Joueur> listeJoueurLoc;

	int x;
	int y;
	int sx;
	int sy;

	/**
	 * Open the window.
	 */
	public void open(Shell shell, Jeu jeu, Audio son) {
		monJeu = jeu;
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
	 */
	public void createContents(Shell shell) {

		List<String> listeCol = new ArrayList<String>();
		for (int i = 0; i < monJeu.getListeJoueurs().size(); i++) {
			listeCol.add(monJeu.getListeJoueurs().get(i).getColor());
		}
		Set<String> setCol = new HashSet<String>();
		setCol.addAll(listeCol);
		listeCol = new ArrayList<String>(setCol);

		if (listeCol.size() == 2) {
			listeJoueurLoc = new ArrayList<Joueur>();
			listeJoueurLoc.add(monJeu.getListeJoueurs().get(0));
			if (!listeJoueurLoc.get(0).getName().equals(monJeu.getListeJoueurs().get(1).getName())) {
				listeJoueurLoc.add(monJeu.getListeJoueurs().get(1));
			} else if (!listeJoueurLoc.get(0).getName().equals(monJeu.getListeJoueurs().get(2).getName())) {
				listeJoueurLoc.add(monJeu.getListeJoueurs().get(2));
			} else if (!listeJoueurLoc.get(0).getName().equals(monJeu.getListeJoueurs().get(3).getName())) {
				listeJoueurLoc.add(monJeu.getListeJoueurs().get(3));
			}
		} else {
			listeJoueurLoc = monJeu.getListeJoueurs();
		}

		//Si l'utilisateur change la taille de la fenétre la fonction refresh est appelée pour actualiser la taille des éléments
		shell.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				refresh();
			}
		});

		lblFinDeLa = new Label(shell, SWT.NONE);
		listeLblJoueur = new Label[listeJoueurLoc.size()];
		listeLblGagnant = new Label[listeJoueurLoc.size()];
		listeLblNbTerrain = new Label[listeJoueurLoc.size()];
		listeLblCouronne = new Label[listeJoueurLoc.size()];
		listeLblGrand = new Label[listeJoueurLoc.size()];
		for (int i = 0; i < listeJoueurLoc.size(); i++) {
			listeLblJoueur[i] = new Label(shell, SWT.NONE);
			listeLblGagnant[i] = new Label(shell, SWT.NONE);
			listeLblNbTerrain[i] = new Label(shell, SWT.NONE);
			listeLblCouronne[i] = new Label(shell, SWT.NONE);
			listeLblGrand[i] = new Label(shell, SWT.NONE);
		}
		btnRestart = new Button(shell, SWT.NONE);
		btnMenu = new Button(shell, SWT.NONE);
		btnQuitter = new Button(shell, SWT.NONE);

		// bandeau supp
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.01));
		y = 0;
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.59));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblFinDeLa.setAlignment(SWT.CENTER);
		lblFinDeLa.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblFinDeLa.setBounds(x, y, sx, sy);
		lblFinDeLa.setText("Fin de la partie !");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.65));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.09));
		btnRestart.setBounds(x, y, sx, sy);
		// le cas suivant est le cas d'une partie classique qui n'est donc pas dans le mode dynastie
		if (monJeu.getDynastie() == 0) {
			btnRestart.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					switch (e.type) {
					case SWT.Selection:
						disposeContent(); 
						for (Joueur j : listeJoueurLoc) { //On crée un nouveau royaume vide pour chaque joueur
							j.initRoyaume(monJeu.getTailleTab());
						}
						if (listeJoueurLoc.size() == 2) { //On copie la liste des joueurs pour l'integrer dans la nouvelle partie sans avoir a remdemander ces informations
							listeJoueurLoc.add(new Joueur(listeJoueurLoc.get(0).getColor(),
									listeJoueurLoc.get(0).isIA(), listeJoueurLoc.get(0).getName()));
							listeJoueurLoc.get(2).setRoyaume(listeJoueurLoc.get(0).getRoyaume());
							listeJoueurLoc.add(new Joueur(listeJoueurLoc.get(1).getColor(),
									listeJoueurLoc.get(1).isIA(), listeJoueurLoc.get(1).getName()));
							listeJoueurLoc.get(3).setRoyaume(listeJoueurLoc.get(1).getRoyaume());
						}
						Jeu nvJeu = new Jeu(listeJoueurLoc, monJeu.getTailleTab(), 0);
						Collections.shuffle(listeJoueurLoc);
						nvJeu.setListeJoueur(listeJoueurLoc);

						Shell sh = new Shell();
						sh.setSize(shell.getSize());
						sh.setLocation(shell.getLocation());
						sh.addListener(SWT.Close, new Listener() {
							//verifie que l'utilisateur veut vraiment quitter le jeu
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
						shell.dispose();

						PageDeJeu pageDeJeu = new PageDeJeu();
						//On relance une partie vierge avec les méme paramétres
						pageDeJeu.open(sh, nvJeu, nvJeu.getListeJoueurs().get(0), son);
						break;
					}
				}
			});

			btnRestart.setText("Restart");
		} else { //cas ou on est dans la derniere manche du mode dynastie, le bouton restart devient un bouton score final permettant de lancer la page finDynastie
			if(monJeu.getDynastie() == 1) {
				btnRestart.setText("Score final");
				btnRestart.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						switch (e.type) {
						case SWT.Selection:
							disposeContent();

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
							shell.dispose();
							//On ouvre la page FinDynastie
							PageFinDynastie finDynastie = new PageFinDynastie();
							finDynastie.open(sh, monJeu.getListeJoueurs(), son);
							break;
						}
					}
				});
			} else { // cas ou on est dans une manche intermédiaire du mode dynastie le bouton restart est remplacé par un bouton continuer
				btnRestart.setText("Continuer");
				btnRestart.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						switch (e.type) {
						case SWT.Selection:
							disposeContent();
							//on decremente le compteur de tour dynastie, on relance une partie avec la meme liste de joueurs
							Jeu nvJeu = new Jeu(listeJoueurLoc, monJeu.getTailleTab(), monJeu.getDynastie()-1);
							Collections.shuffle(listeJoueurLoc);
							nvJeu.setListeJoueur(listeJoueurLoc);
							for(int i = 0; i<listeJoueurLoc.size(); i++) {
								nvJeu.getListeJoueurs().get(i).setRoyaume(new Royaume(9));
							}

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
							shell.dispose();
							// On ouvre la page de Jeu de la manche suivante
							PageDeJeu pageDeJeu = new PageDeJeu();
							pageDeJeu.open(sh, nvJeu, nvJeu.getListeJoueurs().get(0), son);
							break;
						}
					}
				});
			}
			
		}

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.75));
		btnMenu.setBounds(x, y, sx, sy);
		//si le bouton menu est cliqué on ferme la fenetre actuelle et on crée puis affiche la page d'acceuil
		btnMenu.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					disposeContent();
					PageAcceuil maPageDeJeu = new PageAcceuil();
					maPageDeJeu.open(shell, son);
					break;
				}
			}
		});
		btnMenu.setText("Menu");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.85));
		btnQuitter.setBounds(x, y, sx, sy);
		btnQuitter.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					try {
						son.stop();
					} catch(Exception ex) {
						
					}
					shell.close();
					break;
				}
			}
		});
		btnQuitter.setText("Quitter");

		// calcul des meilleurs scores
		//on initialise les meilleures caracéristiques des royaumes
		int scoreMax = listeJoueurLoc.get(0).getRoyaume().score(true)[0];
		int maxTerrain = listeJoueurLoc.get(0).getRoyaume().score(true)[1];
		int maxCouronnes = listeJoueurLoc.get(0).getRoyaume().score(true)[2];
		int tailleMax = listeJoueurLoc.get(0).getRoyaume().score(true)[3];
		int dynastie = monJeu.getDynastie();
		//on parcoure les royaumes de chaque joueur
		for (int i = 0; i < listeJoueurLoc.size(); i++) {
			int[] res = listeJoueurLoc.get(i).getRoyaume().score(true);//appelle la fonction score qui calcule le score le plus grand terrain etc
			if (dynastie == 0) {//cas ou on est dans le mode de jeu classique
				listeJoueurLoc.get(i).setScore(res[0], 0);//on donne le score au joueur
				listeLblJoueur[i].setText("Le joueur " + listeJoueurLoc.get(i).getName() + " a "
						+ listeJoueurLoc.get(i).getScore(0) + " points");
			}else {// cas ou on est dans le mode dynastie
				listeJoueurLoc.get(i).setScore(res[0], 3-dynastie);//on donne le score de la manche au bon endroit dans lle tableu des scores du joueur
				listeLblJoueur[i].setText("Le joueur " + listeJoueurLoc.get(i).getName() + " a "
						+ listeJoueurLoc.get(i).getScore(3-dynastie) + " points");
			}
			
			
			listeLblJoueur[i].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
			listeLblJoueur[i].setBounds(x, y + (i) * sy, sx, sy);
			//les if suivants sont caractériqtiques d'une fonction de recherche de maximum
			//on compare les elements des listes au max actuel pour eventuellement en changer la valeur
			if (res[0]  > scoreMax) {
				scoreMax = res[0] ;
			}
			if (res[1] > maxTerrain) {
				maxTerrain = res[1];
			}
			if (res[2] > maxCouronnes) {
				maxCouronnes = res[2];
			}
			if (res[3] > tailleMax) {
				tailleMax = res[3];
			}
		}
		System.out.println(" score : " + scoreMax);
		System.out.println(" nb terrian : " + maxTerrain);
		System.out.println(" couronnes : " + maxCouronnes);
		System.out.println(" taillemax : " + tailleMax);

		// tableau  des resultats
		listeTabLbl = new Label[listeJoueurLoc
				.size()][(monJeu.getTailleTab() / 2 + 1)][(monJeu.getTailleTab() / 2 + 1)];

		int unite = Math.min(Math.toIntExact(Math.round(shell.getSize().y * 0.5 / (monJeu.getTailleTab() / 2 + 1))),
				Math.toIntExact(
						Math.round(shell.getSize().x / ((monJeu.getTailleTab() / 2 + 2) * listeJoueurLoc.size() + 1))));
		x = unite;
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.15));
		int y2 = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		sx = 3 * x;
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));

		for (int numJoueur = 0; numJoueur < listeJoueurLoc.size(); numJoueur++) {
			int[] res = listeJoueurLoc.get(numJoueur).getRoyaume().score(true);
			listeLblJoueur[numJoueur].setText(listeJoueurLoc.get(numJoueur).getName());
			listeLblJoueur[numJoueur].setBounds(x + numJoueur * x * (monJeu.getTailleTab() / 2 + 2), y2, sx, sy);
			if (res[0] == scoreMax) {// le meilleur score est affiché en gras
				listeLblJoueur[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.BOLD));
			} else {
				listeLblJoueur[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
			}

			listeLblGagnant[numJoueur].setText("Score : " + res[0]);
			listeLblGagnant[numJoueur].setBounds(x + numJoueur * x * (monJeu.getTailleTab() / 2 + 2),
					y + x * (monJeu.getTailleTab() / 2 + 1), 4 * x, sy);
			if (res[0] == scoreMax) {
				listeLblGagnant[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.BOLD));
			} else {
				listeLblGagnant[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
			}
			listeLblNbTerrain[numJoueur].setText("Nb terrain : " + res[1]);
			System.out.println(res[1] + " " + maxTerrain + " soit " + (res[1] == maxTerrain));
			listeLblNbTerrain[numJoueur].setBounds(x + numJoueur * x * (monJeu.getTailleTab() / 2 + 2),
					y + x * (monJeu.getTailleTab() / 2 + 1) + sy, 4 * x, sy);
			if (res[1] == maxTerrain) {
				listeLblNbTerrain[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.BOLD));
			} else {
				listeLblNbTerrain[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
			}
			listeLblCouronne[numJoueur].setText("Nb couronnes : " + res[2]);
			System.out.println(res[2] + " " + maxCouronnes + " soit " + (res[2] == maxCouronnes));
			listeLblCouronne[numJoueur].setBounds(x + numJoueur * x * (monJeu.getTailleTab() / 2 + 2),
					y + x * (monJeu.getTailleTab() / 2 + 1) + sy * 2, 4 * x, sy);
			if (res[2] == maxCouronnes) {
				listeLblCouronne[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.BOLD));
			} else {
				listeLblCouronne[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
			}
			listeLblGrand[numJoueur].setText("Taille max : " + res[3]);
			System.out.println(res[3] + " " + tailleMax + " soit " + (res[3] == tailleMax));
			listeLblGrand[numJoueur].setBounds(x + numJoueur * x * (monJeu.getTailleTab() / 2 + 2),
					y + x * (monJeu.getTailleTab() / 2 + 1) + sy * 3, 4 * x, sy);
			if (res[3] == tailleMax) {
				listeLblGrand[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.BOLD));
			} else {
				listeLblGrand[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
			}

			Royaume unRoyaume = listeJoueurLoc.get(numJoueur).getRoyaume();
			for (int ligne = 0; ligne < monJeu.getTailleTab() / 2 + 1; ligne++) {
				for (int colonne = 0; colonne < monJeu.getTailleTab() / 2 + 1; colonne++) {
					final int n = numJoueur;
					final int l = ligne;
					final int c = colonne;
					final int ccase = unRoyaume.getEcartHorizontal()[0] + (monJeu.getTailleTab() / 2) + c;
					final int lcase = unRoyaume.getEcartVertical()[0] + (monJeu.getTailleTab() / 2) + l;

					listeTabLbl[numJoueur][ligne][colonne] = new Label(shell, SWT.WRAP);
					listeTabLbl[numJoueur][ligne][colonne].setBounds(
							x + numJoueur * (monJeu.getTailleTab() / 2 + 2) * x + colonne * unite, y + ligne * unite,
							unite, unite);

					listeTabLbl[numJoueur][ligne][colonne].setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
					if (unRoyaume.getType(ccase, lcase) != null) {
						ImageData img = SWTResourceManager
								.getImage(monJeu.getListeImage()
										.get(listeJoueurLoc.get(n).getRoyaume().getType(ccase, lcase)))
								.getImageData().scaledTo(unite, unite);
						Image image = new Image(listeTabLbl[n][l][c].getDisplay(), img);

						listeTabLbl[numJoueur][ligne][colonne].setBackgroundImage(image);
						if (!listeJoueurLoc.get(n).getRoyaume().getCouronne(ccase, lcase).equals("chateau")) {
							listeTabLbl[numJoueur][ligne][colonne]
									.setText(listeJoueurLoc.get(n).getRoyaume().getCouronne(ccase, lcase));
						}
					}
				}
			}
		}

	}

	/**
	 * La fonction efface tous les elements de la page pour pouvoir en relancer une autre sans fermer de page 
	 */
	public void disposeContent() {
		lblFinDeLa.dispose();
		for (int numJoueur = 0; numJoueur < listeJoueurLoc.size(); numJoueur++) {
			listeLblJoueur[numJoueur].dispose();
			for (int ligne = 0; ligne < monJeu.getTailleTab() / 2 + 1; ligne++) {
				for (int colonne = 0; colonne < monJeu.getTailleTab() / 2 + 1; colonne++) {
					listeTabLbl[numJoueur][ligne][colonne].dispose();
				}
			}
		}

		for (int i = 0; i < listeJoueurLoc.size(); i++) {
			listeLblJoueur[i].dispose();
			listeLblGagnant[i].dispose();
			listeLblNbTerrain[i].dispose();
			listeLblGrand[i].dispose();
			listeLblCouronne[i].dispose();

		}
		btnRestart.dispose();
		btnMenu.dispose();
		btnQuitter.dispose();

	}

	/**
	 * La fonction refresh actualise la taille des éléments en fonction de la taille de la fenétre
	 */
	public void refresh() {

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.30));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.40));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.4));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.01));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.01));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.98));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblFinDeLa.setAlignment(SWT.CENTER);
		lblFinDeLa.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblFinDeLa.setBounds(x, y, sx, sy);

		for (int i = 1; i < listeJoueurLoc.size(); i++) {
			x = Math.toIntExact(Math.round(shell.getSize().x * 0.30));
			y = Math.toIntExact(Math.round(shell.getSize().y * 0.40));
			sx = Math.toIntExact(Math.round(shell.getSize().x * 0.4));
			sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
			listeLblJoueur[i].setAlignment(SWT.CENTER);
			listeLblJoueur[i].setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
			listeLblJoueur[i].setBounds(x, y + 2 * sy * (i + 1), sx, sy);
		}

	}
}
