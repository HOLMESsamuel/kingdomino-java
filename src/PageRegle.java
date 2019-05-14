import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;

public class PageRegle {

	Shell shell;
	Button btnRetour;
	Text regles;
	Display display;
	Audio son;

	int x;
	int y;
	int sx;
	int sy;

	/**
	 * Open the window.
	 * @param shell
	 */
	public void open(Shell shell, Audio son) {
		this.shell = shell;
		this.son = son;
		display = Display.getDefault();
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
		// Si l'utilisateur redimensionne la page la fonction refresh est apelée pour
		// adapter la taille des éléments
		shell.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				refresh();
			}
		});

		btnRetour = new Button(shell, SWT.NONE);
		btnRetour.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					disposeContents();
					PageAcceuil pageAcceuil = new PageAcceuil();
					pageAcceuil.open(shell, son);
					break;
				}
			}
		});
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.8));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.8));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.1));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		btnRetour.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		btnRetour.setBounds(x, y, sx, sy);
		btnRetour.setText("Retour");

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.05));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.85));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.70));
		// Multi permet de mettre plusieurs parametres
		// Border ajoute un cadre autour des regles
		// V_scroll ajoute une barre de défilement vertical
		// wrap permet un retour à la ligne automatique
		regles = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		regles.setEditable(false);
		regles.setFont(SWTResourceManager.getFont("Caladea", sy / 30, SWT.NORMAL));
		regles.setBounds(x, y, sx, sy);
		regles.setText("Deroulement d'un Tour : \n \n 1. Le joueur choisit une case a placer puis la pose sur "
				+ "un des emplacements proposés, des choix s'offrent alors a lui pour poser la seconde case de sa tuile. \n"
				+ "2. Il selectionne ensuite la tuile qu'il jouera a son prochain tour parmi la liste proposee "
				+ "sachant que plus la tuile a de la valeur plus il attendra avant de la jouer.\n" + "\n"
				+ "Comptage des points : \n \n"
				+ "Le comptage des points s'effectue par zone, on multiplie le nombre total de couronnes presentes sur "
				+ "la zone par le nombre total de cases sur cette meme zone, une zone etant l'ensemble des cases de meme type "
				+ "ayant une frontiere commune.\n"
				+ "un bonus de 5 points est accorde si le chateau est au centre du royaume final "
				+ "et un bonus de 10 points est accorde si le royaume est complet (sans trou).\n"
				+ "\nPlacement des cases : \n \n"
				+ "1. On ne peut pas placer une case sur un emplacemnt deja occupee.\n"
				+ "2. On peut placer n'importe quelle case a cote du  chateau.\n"
				+ "3. On ne peut pas placer une case sur un emplacement qui n'a pas de frontiere avec le chateau "
				+ "ou avec une case du meme type.\n"
				+ "4. Si il est impossible de poser la tuile en suivant ces regles le tour se limite au choix de la tuile suivante."
				+ "\n \n" + "Mode 7*7 : \n\n"
				+ "Ce mode se joue uniquement à deux, la limite du royaume de chaque joueur etant habituellement"
				+ " de 5*5 elle est maintenant elevee a 7*7.\n\n" + "Mode dynastie : \n\n"
				+ "En mode dynastie les joueurs se lance dans une suite de trois parties, les points sont accumules"
				+ " au fil des parties et le gagnant est celui qui aura su etre constant.");

	}

	/**
	 * efface tous les éléments de la page pour pouvoir rouvrir une page sans fermer
	 * la fenetre
	 */
	public void disposeContents() {
		regles.dispose();
		btnRetour.dispose();
	}

	/**
	 * adapte la taille des éléments à celle de la fenêtre
	 */
	public void refresh() {
		x = Math.toIntExact(Math.round(shell.getSize().x * 0.55));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.9));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.2));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.05));
		btnRetour.setFont(SWTResourceManager.getFont("Caladea", sy / 2, SWT.NORMAL));
		btnRetour.setBounds(x, y, sx, sy);

		x = Math.toIntExact(Math.round(shell.getSize().x * 0.05));
		y = Math.toIntExact(Math.round(shell.getSize().y * 0.1));
		sx = Math.toIntExact(Math.round(shell.getSize().x * 0.85));
		sy = Math.toIntExact(Math.round(shell.getSize().y * 0.70));

		regles.setFont(SWTResourceManager.getFont("Caladea", sy / 30, SWT.NORMAL));
		regles.setBounds(x, y, sx, sy);

	}

}
