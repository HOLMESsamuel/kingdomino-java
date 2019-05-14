import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class PageAcceuil {

	protected Shell shell;

	private List<Joueur> listeJoueur;
	private Jeu monJeu;

	Button joueurRouge;
	Button joueurBleu;
	Button joueurVert;
	Button joueurJaune;
	Button IARouge;
	Button IABleu;
	Button IAVert;
	Button IAJaune;

	Button btnQuitter;
	Button btnRegles;
	Button btnStart;
	Button btn77;
	Button btnDynastie;
	Button btnOpp;

	Label lblJoueurJaune;
	Label lblJoueurVert;
	Label lblJoueurBleu;
	Label lblJoueurRouge;
	Text textRouge;
	Text textBleu;
	Text textVert;
	Text textJaune;

	Label lblDomination;
	Audio son;
	int x;
	int x2;
	int y;
	int sx;
	int sx2;
	int sy;

	/**
	 * Open the window.
	 */
	public void open(Shell sh, Audio son) {
		shell = sh;
		this.son = son;
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * La fonction retour permet de relancer la page d'acceuil depuis une autre fenetre sans la refermer
	 * @param shell
	 */
	public void retour(Shell shell) {
		Display display = Display.getDefault();
		setShell(shell);
		createContents();
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
	protected void createContents() {
		listeJoueur = new ArrayList<Joueur>();

		shell.setText("Dominations");
		shell.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				refresh();
			}
		});
		
		SelectionListener selectionListenerRouge;
		SelectionListener selectionListenerBleu;
		SelectionListener selectionListenerVert;
		SelectionListener selectionListenerJaune;
		SelectionListener selectionListenerIARouge;
		SelectionListener selectionListenerIABleu;
		SelectionListener selectionListenerIAVert;
		SelectionListener selectionListenerIAJaune;
		
		//Les boutons permettant de préciser si un joueur est une IA ou un humain sont des checkbox
		selectionListenerRouge = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				joueurRouge = ((Button) event.widget);
				//si le bouton joueur est coché il faut décocher le bouton IA si celui ci est déjà cochée
				if (IARouge.getSelection()) {
					IARouge.setSelection(false);
				}
			};
		};
		selectionListenerBleu = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				joueurBleu = ((Button) event.widget);
				if (IABleu.getSelection()) {
					IABleu.setSelection(false);
				}
			};
		};
		selectionListenerVert = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				joueurVert = ((Button) event.widget);
				if (IAVert.getSelection()) {
					IAVert.setSelection(false);
				}
			};
		};
		selectionListenerJaune = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				joueurJaune = ((Button) event.widget);
				if (IAJaune.getSelection()) {
					IAJaune.setSelection(false);
				}
			};
		};
		selectionListenerIARouge = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IARouge = ((Button) event.widget);
				//Si le bouton IA est coché il faut décocher le bouton joueur si celui ci est déjà coché
				if (joueurRouge.getSelection()) {
					joueurRouge.setSelection(false);
				}
			};
		};
		selectionListenerIABleu = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IABleu = ((Button) event.widget);
				if (joueurBleu.getSelection()) {
					joueurBleu.setSelection(false);
				}
			};
		};
		selectionListenerIAVert = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IAVert = ((Button) event.widget);
				if (joueurVert.getSelection()) {
					joueurVert.setSelection(false);
				}
			};
		};
		selectionListenerIAJaune = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IAJaune = ((Button) event.widget);
				if (joueurJaune.getSelection()) {
					joueurJaune.setSelection(false);
				}
			};
		};
		//On prend les dimension de la fenetre (shell)
		x = 0;
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.07));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.5));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.15));
		lblDomination = new Label(shell, SWT.CENTER);
		lblDomination.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		//x est l'abscisse y l'ordonnée sx la largeur sy la hauteur
		lblDomination.setBounds(x, y, sx, sy);
		lblDomination.setText("Dominations");

		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.07));

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.3));
		btnStart = new Button(shell, SWT.NONE);
		btnStart.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					startAction();
					if (listeJoueur.size() < 2) { //il faut au moins deux joueurs pour jouer qu'ils soient humain ou IA
						MessageBox dialog = new MessageBox(shell, SWT.OK);
						dialog.setText("Warning");
						dialog.setMessage("Merci de selectionner 2 a 4 joueurs.");
						dialog.open();
					} else {
						for (Joueur j : listeJoueur) {
							//Les options sont correctes on crée un royaume vide à chaque joueur
							j.initRoyaume(9);
						}
						//dans le cas ou il y a deux joueurs on en ajoute deux de plus qui seront des copies des premiers
						//ceci permet de jouer en 6 manches avec 4 tuiles par Manche au lieu de 12 manche de 2 tuiles
						if (listeJoueur.size() == 2) {
							listeJoueur.add(new Joueur(listeJoueur.get(0).getColor(), listeJoueur.get(0).isIA(),
									listeJoueur.get(0).getName()));
							listeJoueur.get(2).setRoyaume(listeJoueur.get(0).getRoyaume());
							listeJoueur.add(new Joueur(listeJoueur.get(1).getColor(), listeJoueur.get(1).isIA(),
									listeJoueur.get(1).getName()));
							listeJoueur.get(3).setRoyaume(listeJoueur.get(1).getRoyaume());
						}
						System.out.println("Liste des joueurs : ");
						for(Joueur j : listeJoueur) {
							System.out.print(j.getName()+" ");
						}
						listeJoueur=shuffelJoueurs(listeJoueur);
						System.out.println("\nApres melange : ");
						for(Joueur j : listeJoueur) {
							System.out.print(j.getName()+" ");
						}
						System.out.println(" ");
						monJeu = new Jeu(listeJoueur, 9, 0);
						monJeu.setListeJoueur(listeJoueur);

						Shell sh = new Shell();
						sh.setSize(shell.getSize());
						sh.setLocation(shell.getLocation());
						//permet de verifier que l'utilisateur veut bel et bien fermer la fenêtre si il clique sur la croix
						sh.addListener(SWT.Close, new Listener() {
							public void handleEvent(Event event) {
								MessageBox messageBox = new MessageBox(sh, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
								messageBox.setText("Warning");
								messageBox.setMessage("Quiter le jeu?");
								event.doit = messageBox.open() == SWT.YES;
							}
						});
						shell.dispose();
						//on lance la premiere page de jeu
						PageDeJeu pageDeJeu = new PageDeJeu();
						disposeContents();
						pageDeJeu.open(sh, monJeu, listeJoueur.get(0), son);
					}
				}
			}
		});
		btnStart.setFont(SWTResourceManager.getFont("Caladea", 15, SWT.NORMAL));
		btnStart.setBounds(x, y, sx, sy);
		btnStart.setText("Start");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.42));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.09));
		//bouton du mode 7*7
		btn77 = new Button(shell, SWT.NONE);
		btn77.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					startAction();
					if (listeJoueur.size() != 2) {
						MessageBox dialog = new MessageBox(shell, SWT.OK);
						dialog.setText("Warning");
						dialog.setMessage("Ce mode de jeu se joue seulment a deux.");
						dialog.open();
					} else {
						for (Joueur j : listeJoueur) {
							j.initRoyaume(13);//Le plateau est 13*13 au lieu de 9*9 pour permettre un royaume 7*7
						}
						listeJoueur.add(new Joueur(listeJoueur.get(0).getColor(), listeJoueur.get(0).isIA(),
								listeJoueur.get(0).getName()));
						listeJoueur.get(2).setRoyaume(listeJoueur.get(0).getRoyaume());
						listeJoueur.add(new Joueur(listeJoueur.get(1).getColor(), listeJoueur.get(1).isIA(),
								listeJoueur.get(1).getName()));
						listeJoueur.get(3).setRoyaume(listeJoueur.get(1).getRoyaume());
						System.out.println("Liste des joueurs : ");
						for(Joueur j : listeJoueur) {
							System.out.print(j.getName());
						}
						listeJoueur=shuffelJoueurs(listeJoueur);
						System.out.println("\nApres melange : ");
						for(Joueur j : listeJoueur) {
							System.out.print(j.getName());
						}
						System.out.println(" ");
						monJeu = new Jeu(listeJoueur, 13, 0);
						monJeu.setListeJoueur(listeJoueur);

						Shell sh = new Shell();
						sh.setSize(shell.getSize());
						sh.setLocation(shell.getLocation());
						sh.addListener(SWT.Close, new Listener() {
							public void handleEvent(Event event) {
								MessageBox messageBox = new MessageBox(sh, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
								messageBox.setText("Warning");
								messageBox.setMessage("Quiter le jeu?");
								event.doit = messageBox.open() == SWT.YES;
							}
						});
						shell.dispose();

						PageDeJeu pageDeJeu = new PageDeJeu();
						disposeContents();
						pageDeJeu.open(sh, monJeu, listeJoueur.get(0), son);
					}
					break;
				}
			}
		});
		btn77.setFont(SWTResourceManager.getFont("Caladea", 15, SWT.NORMAL));
		btn77.setBounds(x, y, sx, sy);
		btn77.setText("7*7");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.26));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.42));
		btnDynastie = new Button(shell, SWT.NONE);
		btnDynastie.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					startAction();
					if (listeJoueur.size() < 2) {
						MessageBox dialog = new MessageBox(shell, SWT.OK);
						dialog.setText("Warning");
						dialog.setMessage("Merci de selectionner 2 a 4 joueurs.");
						dialog.open();
					} else {
						for (Joueur j : listeJoueur) {
							j.initRoyaume(9);
						}
						if (listeJoueur.size() == 2) {
							listeJoueur.add(new Joueur(listeJoueur.get(0).getColor(), listeJoueur.get(0).isIA(),
									listeJoueur.get(0).getName()));
							listeJoueur.get(2).setRoyaume(listeJoueur.get(0).getRoyaume());
							listeJoueur.add(new Joueur(listeJoueur.get(1).getColor(), listeJoueur.get(1).isIA(),
									listeJoueur.get(1).getName()));
							listeJoueur.get(3).setRoyaume(listeJoueur.get(1).getRoyaume());
						}
						System.out.println("Liste des joueurs : ");
						for(Joueur j : listeJoueur) {
							System.out.print(j.getName());
						}
						listeJoueur=shuffelJoueurs(listeJoueur);
						System.out.println("\nApres melange : ");
						for(Joueur j : listeJoueur) {
							System.out.print(j.getName());
						}
						System.out.println(" ");
						monJeu = new Jeu(listeJoueur, 9, 3);
						monJeu.setListeJoueur(listeJoueur);

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

						PageDeJeu pageDeJeu = new PageDeJeu();
						disposeContents();
						pageDeJeu.open(sh, monJeu, listeJoueur.get(0), son);
					}
					break;
				}
			}
		});
		btnDynastie.setFont(SWTResourceManager.getFont("Caladea", 15, SWT.NORMAL));
		btnDynastie.setBounds(x, y, sx, sy);
		btnDynastie.setText("Dynastie");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.53));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		btnRegles = new Button(shell, SWT.NONE);
		btnRegles.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					disposeContents();
					PageRegle pageRegles = new PageRegle();
					pageRegles.open(shell, son);
					break;
				}
			}
		});
		btnRegles.setFont(SWTResourceManager.getFont("Caladea", 15, SWT.NORMAL));
		btnRegles.setBounds(x, y, sx, sy);
		btnRegles.setText("Regles");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.64));
		btnOpp = new Button(shell, SWT.NONE);
		btnOpp.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					disposeContents();
					PageOption pageOpp = new PageOption();
					pageOpp.open(shell, son);
					break;
				}
			}
		});
		btnOpp.setFont(SWTResourceManager.getFont("Caladea", 15, SWT.NORMAL));
		btnOpp.setBounds(x, y, sx, sy);
		btnOpp.setText("Options");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		btnQuitter = new Button(shell, SWT.NONE);
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
		btnQuitter.setFont(SWTResourceManager.getFont("Caladea", 15, SWT.NORMAL));
		btnQuitter.setBounds(x, y, sx, sy);
		btnQuitter.setText("Quitter");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.5));
		x2 = Math.toIntExact(Math.round(shell.getSize().x * 0.65));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.12));
		sx2 = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.3));
		lblJoueurRouge = new Label(shell, SWT.NONE);
		lblJoueurRouge.setFont(SWTResourceManager.getFont("Caladea", 13, SWT.NORMAL));
		lblJoueurRouge.setBounds(x, y, sx, sy);
		lblJoueurRouge.setText("Joueur Rouge");
		textRouge = new Text(shell, SWT.BORDER);
		textRouge.setBounds(x2, y, sx2, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.45));
		lblJoueurBleu = new Label(shell, SWT.NONE);
		lblJoueurBleu.setText("Joueur Bleu");
		lblJoueurBleu.setFont(SWTResourceManager.getFont("Caladea", 13, SWT.NORMAL));
		lblJoueurBleu.setBounds(x, y, sx, sy);
		textBleu = new Text(shell, SWT.BORDER);
		textBleu.setBounds(x2, y, sx2, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.6));
		lblJoueurVert = new Label(shell, SWT.NONE);
		lblJoueurVert.setText("Joueur Vert");
		lblJoueurVert.setFont(SWTResourceManager.getFont("Caladea", 13, SWT.NORMAL));
		lblJoueurVert.setBounds(x, y, sx, sy);
		textVert = new Text(shell, SWT.BORDER);
		textVert.setBounds(x2, y, sx2, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		lblJoueurJaune = new Label(shell, SWT.NONE);
		lblJoueurJaune.setText("Joueur Jaune");
		lblJoueurJaune.setFont(SWTResourceManager.getFont("Caladea", 13, SWT.NORMAL));
		lblJoueurJaune.setBounds(x, y, sx, sy);
		textJaune = new Text(shell, SWT.BORDER);
		textJaune.setBounds(x2, y, sx2, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		x2 = Math.toIntExact(Math.round(shell.getSize().x * 0.7));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.34));
		IARouge = new Button(shell, SWT.CHECK);
		IARouge.addSelectionListener(selectionListenerIARouge);
		IARouge.setBounds(x, y, sx, sy);
		IARouge.setText("IA");

		joueurRouge = new Button(shell, SWT.CHECK);
		joueurRouge.addSelectionListener(selectionListenerRouge);
		joueurRouge.setBounds(x2, y, sx, sy);
		joueurRouge.setText("Joueur");

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.5));
		IABleu = new Button(shell, SWT.CHECK);
		IABleu.addSelectionListener(selectionListenerIABleu);
		IABleu.setBounds(x, y, sx, sy);
		IABleu.setText("IA");

		joueurBleu = new Button(shell, SWT.CHECK);
		joueurBleu.addSelectionListener(selectionListenerBleu);
		joueurBleu.setBounds(x2, y, sx, sy);
		joueurBleu.setText("Joueur");

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.64));
		IAVert = new Button(shell, SWT.CHECK);
		IAVert.addSelectionListener(selectionListenerIAVert);
		IAVert.setBounds(x, y, sx, sy);
		IAVert.setText("IA");

		joueurVert = new Button(shell, SWT.CHECK);
		joueurVert.addSelectionListener(selectionListenerVert);
		joueurVert.setBounds(x2, y, sx, sy);
		joueurVert.setText("Joueur");

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.8));
		IAJaune = new Button(shell, SWT.CHECK);
		IAJaune.addSelectionListener(selectionListenerIAJaune);
		IAJaune.setBounds(x, y, sx, sy);
		IAJaune.setText("IA");

		joueurJaune = new Button(shell, SWT.CHECK);
		joueurJaune.addSelectionListener(selectionListenerJaune);
		joueurJaune.setBounds(x2, y, sx, sy);
		joueurJaune.setText("Joueur");
	} // protected void createContents(
	
	/**
	 * La fonction start Action est appelée quand l'utilisateur clique sur "start" elle recupère l'état de toutes les checkbox
	 * et crée la liste des joueurs en conséquence en appelant le constructeur joueur
	 * @return la liste des joueurs 
	 */
	public List<Joueur> startAction() {

		try {
			if (joueurRouge.getSelection()) {
				listeJoueur.add(new Joueur("Rouge", false, textRouge.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (IARouge.getSelection()) {
				listeJoueur.add(new Joueur("Rouge", true, textRouge.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (joueurBleu.getSelection()) {
				listeJoueur.add(new Joueur("Bleu", false, textBleu.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (IABleu.getSelection()) {
				listeJoueur.add(new Joueur("Bleu", true, textBleu.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (joueurVert.getSelection()) {
				listeJoueur.add(new Joueur("Vert", false, textVert.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (IAVert.getSelection()) {
				listeJoueur.add(new Joueur("Vert", true, textVert.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (joueurJaune.getSelection()) {
				listeJoueur.add(new Joueur("Jaune", false, textJaune.getText()));
			}
		} catch (NullPointerException exception) {
		}
		try {
			if (IAJaune.getSelection()) {
				listeJoueur.add(new Joueur("Jaune", true, textJaune.getText()));
			}
		} catch (NullPointerException exception) {
		}
		return listeJoueur;
	} // public List<Joueur> startAction()
	
	/**
	 * efface tous les elements de la page pour pouvoir passer à la fenetre suivante sans la fermer
	 */
	public void disposeContents() {
		joueurJaune.dispose();
		IAJaune.dispose();
		lblJoueurJaune.dispose();
		textJaune.dispose();

		joueurVert.dispose();
		IAVert.dispose();
		lblJoueurVert.dispose();
		textVert.dispose();

		joueurBleu.dispose();
		IABleu.dispose();
		lblJoueurBleu.dispose();
		textBleu.dispose();

		joueurRouge.dispose();
		IARouge.dispose();
		lblJoueurRouge.dispose();
		textRouge.dispose();

		btnQuitter.dispose();
		btnRegles.dispose();
		btnStart.dispose();
		btnOpp.dispose();
		btn77.dispose();
		btnDynastie.dispose();
		lblDomination.dispose();
	}//public void disposeContents()
	
	/**
	 * Donne a la page le shell (noyau de la fenetre) passé en argument
	 * @param shell
	 */
	public void setShell(Shell shell) {
}
	/**
	 * La fonction est appelée quand l'utilisateur redimenssionne la page, elle permet d'adapter la taille des objets à celle de la page
	 */
	public void refresh() {
		x = 0;
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.07));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.5));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.15));
		lblDomination.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblDomination.setBounds(x, y, sx, sy);

		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.07));

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.3));
		btnStart.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.42));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.09));
		btn77.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.26));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.42));
		btnDynastie.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.53));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		btnRegles.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.64));
		btnOpp.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.15));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		btnQuitter.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.5));
		x2 = Math.toIntExact(Math.round(shell.getSize().x * 0.65));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.12));
		sx2 = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.3));
		lblJoueurRouge.setBounds(x, y, sx, sy);
		textRouge.setBounds(x2, y, sx2, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.45));
		lblJoueurBleu.setBounds(x, y, sx, sy);
		textBleu.setBounds(x2, y, sx2, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.6));
		lblJoueurVert.setBounds(x, y, sx, sy);
		textVert.setBounds(x2, y, sx2, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		lblJoueurJaune.setBounds(x, y, sx, sy);
		textJaune.setBounds(x2, y, sx2, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		x2 = Math.toIntExact(Math.round(shell.getSize().x * 0.7));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.34));
		IARouge.setBounds(x, y, sx, sy);
		joueurRouge.setBounds(x2, y, sx, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.5));
		IABleu.setBounds(x, y, sx, sy);
		joueurBleu.setBounds(x2, y, sx, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.64));
		IAVert.setBounds(x, y, sx, sy);
		joueurVert.setBounds(x2, y, sx, sy);

		y = Math.toIntExact(Math.round(shell.getSize().y * 0.8));
		IAJaune.setBounds(x, y, sx, sy);
		joueurJaune.setBounds(x2, y, sx, sy);
	} // public void refresh()

	public List<Joueur> shuffelJoueurs(List<Joueur> l){
		List<Joueur> lCopy=new ArrayList<Joueur>(l);
		Random r = new Random();
		int rd;
		for(int i=0; i<l.size(); i++) {
			Collections.copy(lCopy,l);
			rd = r.nextInt(l.size());
			lCopy.set(rd, l.get(i));
			lCopy.set(i, l.get(rd));
			Collections.copy(l,lCopy);
			
		}
		
		return l;
	}
}
