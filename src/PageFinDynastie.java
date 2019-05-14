import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class PageFinDynastie {

	Label lblScoreDynatie;
	Label lblM1;
	Label lblM2;
	Label lblM3;
	Label lblTotal;
	Label[] listeLblJoueur;
	Label[] listeLblM1;
	Label[] listeLblM2;
	Label[] listeLblM3;
	Label[] listeLblTotal;
	Button btnMenu;
	Button btnQuitter;
	List<Joueur> listeJoueurs;
	Audio son;
	
	int x;
	int y;
	int sx;
	int sy;

	/**
	 * Open the window.
	 * @param shell
	 * @param listeJoueurs
	 * @param son
	 */
	public void open(Shell shell, List<Joueur> listeJoueurs, Audio son) {
		Display display = Display.getDefault();
		this.son = son;
		this.listeJoueurs = listeJoueurs;
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
	protected void createContents(Shell shell) {
		
		int size= listeJoueurs.size();
		
		lblScoreDynatie = new Label(shell, SWT.NONE);
		lblM1 = new Label(shell, SWT.NONE);
		lblM2 = new Label(shell, SWT.NONE);
		lblM3 = new Label(shell, SWT.NONE);
		lblTotal = new Label(shell, SWT.NONE);
		btnMenu = new Button(shell, SWT.NONE);
		btnQuitter = new Button(shell, SWT.NONE);

		listeLblJoueur = new Label[size];
		listeLblM1 = new Label[size];
		listeLblM2 = new Label[size];
		listeLblM3 = new Label[size];
		listeLblTotal = new Label[size];
		for (int i = 0; i < size; i++) {
			listeLblJoueur[i] = new Label(shell, SWT.NONE);
			listeLblM1[i] = new Label(shell, SWT.NONE);
			listeLblM2[i] = new Label(shell, SWT.NONE);
			listeLblM3[i] = new Label(shell, SWT.NONE);
			listeLblTotal[i] = new Label(shell, SWT.NONE);
		}

		//bandeau supp
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.01));
		y = 0;
		sx = Math.toIntExact(Math.round(shell.getSize().x*0.9));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblScoreDynatie.setAlignment(SWT.CENTER);
		lblScoreDynatie.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblScoreDynatie.setBounds(x, y, sx, sy);
		lblScoreDynatie.setText("Score dynastie");
		

		// premiere colone
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.2));
		sx=Math.toIntExact(Math.round(shell.getSize().x*0.8/(size+1)));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		lblM1.setBounds(x, y+sy, sx, sy);
		lblM1.setText("Manche 1");
		
		lblM2.setBounds(x, y+2*sy, sx, sy);
		lblM2.setText("Manche 2");
		
		lblM3.setBounds(x, y+3*sy, sx, sy);
		lblM3.setText("Manche 3");
		
		lblTotal.setBounds(x, y+4*sy, sx, sy);
		lblTotal.setText("Total");
		
		// un tableau contenant les scores de chaque joueur est crée pour chaque manche du mode dynastie
		int[] scorePremierTour = new int[listeJoueurs.size()];
		int[] scoreSecondTour = new int[listeJoueurs.size()];
		int[] scoreTroisiemeTour = new int[listeJoueurs.size()];
		
			for(int j = 0; j<listeJoueurs.size(); j++) {
				scorePremierTour[j] = listeJoueurs.get(j).getScore(0);
				scoreSecondTour[j] = listeJoueurs.get(j).getScore(1);
				scoreTroisiemeTour[j] = listeJoueurs.get(j).getScore(2);
			}
		
		//On determine le maximum de chaque tableau pour retrouver le vainqueur de chaque manche
		int maxPremierTour = max(scorePremierTour);
		int maxSecondTour = max(scoreSecondTour);
		int maxTroisiemeTour = max(scoreTroisiemeTour);

		
		
		// tableau de score
		for (int numJoueur=0; numJoueur<size; numJoueur++) {
			listeLblJoueur[numJoueur].setBounds(x+sx+sx*numJoueur, y, sx, sy);
			listeLblJoueur[numJoueur].setText(listeJoueurs.get(numJoueur).getName());
			listeLblM1[numJoueur].setBounds(x+sx+sx*numJoueur, y+sy, sx, sy);
			listeLblM1[numJoueur].setText(String.valueOf(listeJoueurs.get(numJoueur).getScore(0)));
			//si le score du joueur est le score maximum de la manche concernée on l'affiche en gras
			if(listeJoueurs.get(numJoueur).getScore(0) == maxPremierTour) {
				listeLblM1[numJoueur].setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			}
			listeLblM2[numJoueur].setBounds(x+sx+sx*numJoueur, y+2*sy, sx, sy);
			listeLblM2[numJoueur].setText(String.valueOf(listeJoueurs.get(numJoueur).getScore(1)));
			if(listeJoueurs.get(numJoueur).getScore(1) == maxSecondTour) {
				listeLblM2[numJoueur].setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			}
			listeLblM3[numJoueur].setBounds(x+sx+sx*numJoueur, y+3*sy, sx, sy);
			listeLblM3[numJoueur].setText(String.valueOf(listeJoueurs.get(numJoueur).getScore(2)));
			if(listeJoueurs.get(numJoueur).getScore(2) == maxTroisiemeTour) {
				listeLblM3[numJoueur].setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			}
			listeLblTotal[numJoueur].setBounds(x+sx+sx*numJoueur, y+4*sy, sx, sy);
			listeLblTotal[numJoueur].setText(String.valueOf(listeJoueurs.get(numJoueur).getTotal()));
			
		}
		

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.3));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.75));
		sx=Math.toIntExact(Math.round(shell.getSize().x*0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		btnMenu.setBounds(x, y, sx, sy);
		btnMenu.setText("Menu");
		//si le bouton menu est cliqué on ferme la fenetre actuelle et on crée puis affiche la page d'acceuil
		btnMenu.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					shell.close();
					PageAcceuil acceuil = new PageAcceuil();
					Shell shell1 = new Shell();
					acceuil.open(shell1, son);
				}
			}
		});

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.5));
		btnQuitter.setBounds(x, y, sx, sy);
		btnQuitter.addListener(SWT.Selection, new Listener() {
			//si le bouton quitter est cliqué on coupe le son et on ferme la fenetre
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

	}
	
	/**
	 * La fonction max renvoie le maximum du tableau d'entier entré en paramètre
	 * Elle n'est utilisée que dans la classe PageFinDynastie
	 * @param tab
	 * @return le maximum du tableau
	 */
	private int max(int[] tab) {
		int max = tab[0];
		for(int i = 0; i<tab.length; i++) {
			if(tab[i]>max) {
				max = tab[i];
			}
		}
		return max;
	}

}
