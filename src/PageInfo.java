 import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class PageInfo {

	Label lblTitre;
	Label[] listeLblJoueur;
	Label[][][] listeTabLbl;
	Label lblTuileDuTour;
	Label[][] listeLblTuileDuTour;
	Label[] listeLblNomTuileDuTour;
	Label lblNexttuile;
	Label[][] listeLblNextTuile;
	Label[] listeLblNomNextTuile;

	int x;
	int y;
	int sx;
	int sy;

	Shell sh;

	List<Joueur> listeJoueurLoc;

	/**
	 * Creer la page info avec les composant necessaire.
	 * @param parent
	 * @param monJeu
	 */
	PageInfo(Shell parent, Jeu monJeu) {
		sh = new Shell(parent);
		sh.setSize(900, 600);
		sh.setText("Info");
		sh.open();
		createContents(monJeu);
		sh.layout();

		while (!sh.isDisposed()) {
			if (!sh.getDisplay().readAndDispatch()) {
				sh.getDisplay().sleep();
			}
		}

	}

	/**
	 * Open the window.
	 * @param monJeu
	 */
	public void open(Jeu monJeu) {
		Display display = Display.getDefault();
		createContents(monJeu);
		sh.open();
		sh.layout();
		while (!sh.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @param monJeu
	 */
	protected void createContents(Jeu monJeu) {

		List<String> listeCol = new ArrayList<String>();
		for (int i = 0; i < monJeu.getListeJoueurs().size(); i++) {
			listeCol.add(monJeu.getListeJoueurs().get(i).getColor());
		}
		Set<String> setCol = new HashSet<String>();
		setCol.addAll(listeCol);
		listeCol = new ArrayList<String>(setCol);

		// Recupere le nombre reel de joueurs
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

		// Initialisation des composants
		lblTitre = new Label(sh, SWT.NONE);
		lblTitre.setAlignment(SWT.CENTER);
		x = Math.toIntExact(Math.round(sh.getSize().x * 0.01));
		y = Math.toIntExact(Math.round(sh.getSize().y * 0.01));
		sx = Math.toIntExact(Math.round(sh.getSize().x * 0.98));
		sy = Math.toIntExact(Math.round(sh.getSize().y * 0.09));
		lblTitre.setBounds(x, y, sx, sy);
		lblTitre.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		lblTitre.setText("Information manche " + monJeu.getMancheNb());

		listeTabLbl = new Label[listeJoueurLoc
				.size()][(monJeu.getTailleTab() / 2 + 1)][(monJeu.getTailleTab() / 2 + 1)];
		listeLblJoueur = new Label[listeJoueurLoc.size()];
		int unite = Math.min(Math.toIntExact(Math.round(sh.getSize().y * 0.5 / (monJeu.getTailleTab() / 2 + 1))),
				Math.toIntExact(
						Math.round(sh.getSize().x / ((monJeu.getTailleTab() / 2 + 2) * listeJoueurLoc.size() + 1))));
		x = unite;
		y = Math.toIntExact(Math.round(sh.getSize().y * 0.15));
		int y2 = Math.toIntExact(Math.round(sh.getSize().y * 0.1));
		sx = 3 * x;
		sy = Math.toIntExact(Math.round(sh.getSize().y * 0.05));
		// Affiche les ryaumes des differents joueurs
		for (int numJoueur = 0; numJoueur < listeJoueurLoc.size(); numJoueur++) {
			Royaume unRoyaume = listeJoueurLoc.get(numJoueur).getRoyaume();
			listeLblJoueur[numJoueur] = new Label(sh, SWT.NORMAL);
			listeLblJoueur[numJoueur].setText(listeJoueurLoc.get(numJoueur).getName());
			listeLblJoueur[numJoueur].setBounds(x + numJoueur * (monJeu.getTailleTab() / 2 + 2) * x, y2, sx, sy);
			listeLblJoueur[numJoueur].setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
			for (int ligne = 0; ligne < monJeu.getTailleTab() / 2 + 1; ligne++) {
				for (int colonne = 0; colonne < monJeu.getTailleTab() / 2 + 1; colonne++) {
					final int n = numJoueur;
					final int l = ligne;
					final int c = colonne;
					final int ccase = unRoyaume.getEcartHorizontal()[0] + (monJeu.getTailleTab() / 2) + c;
					final int lcase = unRoyaume.getEcartVertical()[0] + (monJeu.getTailleTab() / 2) + l;
					listeTabLbl[numJoueur][ligne][colonne] = new Label(sh, SWT.WRAP);
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

		// Affiche les tuiles a jouer pour ce tour
		lblTuileDuTour = new Label(sh, SWT.NONE);
		x = Math.toIntExact(Math.round(sh.getSize().x * 0.05));
		y = Math.toIntExact(Math.round(sh.getSize().y * 0.7));
		sx = Math.toIntExact(Math.round(sh.getSize().x * 0.15));
		sy = Math.toIntExact(Math.round(sh.getSize().y * 0.1));
		lblTuileDuTour.setBounds(x, y, sx, sy);
		lblTuileDuTour.setFont(SWTResourceManager.getFont("Caladea", sy / 7, SWT.NORMAL));
		lblTuileDuTour.setText("Tuiles du tour en cour :");

		listeLblTuileDuTour = new Label[monJeu.getPioche().getNext().size()][2];
		listeLblNomTuileDuTour = new Label[monJeu.getPioche().getNext().size()];
		unite = Math.toIntExact(Math.round(sh.getSize().y * 0.07));
		x = Math.toIntExact(Math.round(sh.getSize().x * 0.2));
		y = Math.toIntExact(Math.round(sh.getSize().y * 0.7));
		y2 = Math.toIntExact(Math.round(sh.getSize().y * 0.67));
		sx = unite;
		sy = unite;
		int sy2 = Math.toIntExact(Math.round(sh.getSize().y * 0.03));
		for (int numTuile = 0; numTuile < monJeu.getPioche().getTour().size(); numTuile++) {
			listeLblNomTuileDuTour[numTuile] = new Label(sh, SWT.NORMAL);
			listeLblNomTuileDuTour[numTuile].setText(monJeu.getListeJoueurs().get(numTuile).getName());
			listeLblNomTuileDuTour[numTuile].setBounds(x + numTuile * unite * 3, y2, sx, sy2);

			listeLblTuileDuTour[numTuile][0] = new Label(sh, SWT.NORMAL);
			listeLblTuileDuTour[numTuile][0].setText(monJeu.getListeJoueurs().get(numTuile).getColor());
			listeLblTuileDuTour[numTuile][0].setBounds(x + numTuile * unite * 3, y, sx, sy);
			listeLblTuileDuTour[numTuile][0].setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
			listeLblTuileDuTour[numTuile][1] = new Label(sh, SWT.NORMAL);
			listeLblTuileDuTour[numTuile][1].setText(monJeu.getListeJoueurs().get(numTuile).getColor());
			listeLblTuileDuTour[numTuile][1].setBounds(x + unite + numTuile * unite * 3, y, sx, sy);
			listeLblTuileDuTour[numTuile][1].setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));

			String[][] T = monJeu.getPioche().getDomino(monJeu.getPioche().getTour().get(numTuile));
			ImageData img1 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[0][0])).getImageData()
					.scaledTo(unite, unite);
			Image image1 = new Image(listeLblTuileDuTour[numTuile][0].getDisplay(), img1);
			ImageData img2 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[1][0])).getImageData()
					.scaledTo(unite, unite);
			Image image2 = new Image(listeLblTuileDuTour[numTuile][1].getDisplay(), img2);

			if (numTuile < (monJeu.getPioche().getTour().size() - monJeu.getTempTuileNext().size())) {
				image1 = new Image(listeLblTuileDuTour[numTuile][0].getDisplay(), image1, SWT.IMAGE_GRAY);
				image2 = new Image(listeLblTuileDuTour[numTuile][1].getDisplay(), image2, SWT.IMAGE_GRAY);
			}

			listeLblTuileDuTour[numTuile][0].setBackgroundImage(image1);
			listeLblTuileDuTour[numTuile][0].setText(T[0][1]);
			listeLblTuileDuTour[numTuile][1].setBackgroundImage(image2);
			listeLblTuileDuTour[numTuile][1].setText(T[1][1]);

		}

		// Affiche les tuiles a choisir pendant ce tour
		lblNexttuile = new Label(sh, SWT.NONE);
		x = Math.toIntExact(Math.round(sh.getSize().x * 0.05));
		y = Math.toIntExact(Math.round(sh.getSize().y * 0.82));
		sx = Math.toIntExact(Math.round(sh.getSize().x * 0.15));
		sy = Math.toIntExact(Math.round(sh.getSize().y * 0.1));
		lblNexttuile.setBounds(x, y, sx, sy);
		lblNexttuile.setFont(SWTResourceManager.getFont("Caladea", sy / 7, SWT.NORMAL));
		lblNexttuile.setText("Tuiles du tour suivant :");

		listeLblNomNextTuile = new Label[monJeu.getPioche().getNext().size()];
		listeLblNextTuile = new Label[monJeu.getPioche().getNext().size()][2];
		unite = Math.toIntExact(Math.round(sh.getSize().y * 0.07));
		x = Math.toIntExact(Math.round(sh.getSize().x * 0.2));
		y = Math.toIntExact(Math.round(sh.getSize().y * 0.82));
		y2 = Math.toIntExact(Math.round(sh.getSize().y * 0.79));
		sx = unite;
		sy = unite;
		sy2 = Math.toIntExact(Math.round(sh.getSize().y * 0.03));
		for (int numNext = 0; numNext < monJeu.getListeJoueurs().size(); numNext++) {
			listeLblNextTuile[numNext][0] = new Label(sh, SWT.NORMAL);
			listeLblNextTuile[numNext][0].setText(monJeu.getListeJoueurs().get(numNext).getColor());
			listeLblNextTuile[numNext][0].setBounds(x + numNext * unite * 3, y, sx, sy);
			listeLblNextTuile[numNext][0].setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
			listeLblNextTuile[numNext][1] = new Label(sh, SWT.NORMAL);
			listeLblNextTuile[numNext][1].setText(monJeu.getListeJoueurs().get(numNext).getColor());
			listeLblNextTuile[numNext][1].setBounds(x + unite + numNext * unite * 3, y, sx, sy);
			listeLblNextTuile[numNext][1].setFont(SWTResourceManager.getFont("Caladea", sy / 3, SWT.NORMAL));
			String[][] T = monJeu.getPioche().getDomino(monJeu.getPioche().getNext().get(numNext));
			ImageData img1 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[0][0])).getImageData()
					.scaledTo(unite, unite);
			Image image1 = new Image(listeLblTuileDuTour[numNext][0].getDisplay(), img1);
			ImageData img2 = SWTResourceManager.getImage(monJeu.getListeImage().get(T[1][0])).getImageData()
					.scaledTo(unite, unite);
			Image image2 = new Image(listeLblTuileDuTour[numNext][1].getDisplay(), img2);

			listeLblNomNextTuile[numNext] = new Label(sh, SWT.NORMAL);
			if (!monJeu.getTempTuileNext().contains(monJeu.getPioche().getNext().get(numNext))) {
				// lbl nom
				image1 = new Image(listeLblNextTuile[numNext][0].getDisplay(), image1, SWT.IMAGE_GRAY);
				image2 = new Image(listeLblNextTuile[numNext][1].getDisplay(), image2, SWT.IMAGE_GRAY);
				for (int i = 0; i < monJeu.getListeJoueurs().size(); i++) {
					if (monJeu.getListeJoueurs().get(i).getNextTuile() == monJeu.getPioche().getNext().get(numNext)) {
						listeLblNomNextTuile[numNext].setText(monJeu.getListeJoueurs().get(i).getName());
					}
				}
				listeLblNomNextTuile[numNext].setBounds(x + numNext * unite * 3, y2, sx, sy2);
			}

			listeLblNextTuile[numNext][0].setBackgroundImage(image1);
			listeLblNextTuile[numNext][0].setText(T[0][1]);
			listeLblNextTuile[numNext][1].setBackgroundImage(image2);
			listeLblNextTuile[numNext][1].setText(T[1][1]);

		}

	}
}
