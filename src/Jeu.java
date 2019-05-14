import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Jeu {

	List<Joueur> listeJoueurs;
	Pioche pioche;
	int tailleTab;
	int mancheNb;
	Map<String, String> listeImage;
	Joueur[] temp;
	List<Integer> tempTuileNext;
	int dynastie; // 0 normal game; from 1 to 3 dynastie
	boolean isDynastie;

	Scanner scan = new Scanner(System.in);

	/**
	 * Constructeur d'un jeu/d'une partie
	 * @param listeJoueurs
	 * @param tailleT
	 * @param dynastie
	 */
	public Jeu(List<Joueur> listeJoueurs, int tailleT, int dynastie) {
		this.dynastie= dynastie; // par default la partie ne fait pas partie du mode dynastie
		if(dynastie > 0) {
			isDynastie = true;
		}
		pioche = new Pioche(listeJoueurs.size());
		pioche.setProchaineTuiles(listeJoueurs.size()); // Tirage des nouvelle tuilles

		tailleTab = tailleT;
		if (listeJoueurs.size() == 4 && tailleTab == 9
				&& (listeJoueurs.get(0).getColor().equals(listeJoueurs.get(1).getColor())
						|| listeJoueurs.get(0).getColor().equals(listeJoueurs.get(2).getColor())
						|| listeJoueurs.get(0).getColor().equals(listeJoueurs.get(3).getColor()))) { 
			mancheNb = 6;
		} else {
			mancheNb = 12;
		}
		listeImage = new HashMap<String, String>() {
			{
				put("Champs", "./img/champs0.PNG");
				put("Prairie", "./img/prairie0.PNG");
				put("Foret", "./img/foret0.PNG");
				put("Mer", "./img/mer0.PNG");
				put("Montagne", "./img/montagne0.PNG");
				put("Mine", "./img/mine0.PNG");
				put("chateau", "./img/chateau.PNG");
			}
		};
		temp = new Joueur[listeJoueurs.size()];
		tempTuileNext = new ArrayList<Integer>(pioche.getNext());

		System.out.println(pioche.getReste());

	} // public void main(String[] args)
	
	/**
	 * Fait evoluer les variables qui le necessite en fin de manche.
	 */
	public void passerManche() {
		mancheNb--;
		listeJoueurs = Arrays.asList(temp);
		pioche.setProchaineTuiles(listeJoueurs.size());
		temp = new Joueur[listeJoueurs.size()];
		tempTuileNext = new ArrayList<Integer>(pioche.getNext());

	}

	/**
	 * Met a jour les variable temporaire en fonction des choix du joueur.
	 * @param joueur
	 */
	public void passerTour(Joueur joueur) {
		if (mancheNb != 1) {
			// restriction tuille deja utilise
			tempTuileNext.remove(tempTuileNext.indexOf(joueur.getNextTuile()));
			// stocke le choix du joueur dans la liste temporaire
			temp[pioche.getNext().indexOf(joueur.getNextTuile())] = joueur;
		}
	}
	
	/**
	 * Incremente la variable dynastie ce qui permet de savoir si on est dans ce mode de jeu et si oui a quelle manche.
	 */
	public void setDynastie() {
		dynastie++;
	}

	/**
	 * Permet de recuperer la variable dynastie.
	 * @return
	 */
	public int getDynastie() {
		return dynastie;
	}
	
	/**
	 * Permet de recuperer la liste de joueur de la partie
	 * @return
	 */
	public List<Joueur> getListeJoueurs() {
		return listeJoueurs;
	}

	/**
	 * Permet de recuperer la pioche.
	 * @return
	 */
	public Pioche getPioche() {
		return pioche;
	}

	/**
	 * @return la taille du tableau 9 ou 13 suivant le moe de jeu
	 */
	public int getTailleTab() {
		return tailleTab;
	}

	/**
	 * @return le nombre de manche qu'il reste avant la fin du jeu
	 */
	public int getMancheNb() {
		return mancheNb;
	}

	/**
	 * @return permet de recuperer les fichier ou son enrengistré les images
	 */
	public Map<String, String> getListeImage() {
		return listeImage;
	}

	/**
	 * @return la liste des joueur dans l'ordre de la prochaine manche
	 */
	public Joueur[] getTemp() {
		return temp;
	}

	/**
	 * @return la liste des tuile pas encore choisit durant cette manche
	 */
	public List<Integer> getTempTuileNext() {
		return tempTuileNext;
	}

	/**
	 * Affichage console des resultats je jeu.
	 */
	public void fin() {
		String gagnant = listeJoueurs.get(0).getColor();
		int scoreMax = listeJoueurs.get(0).getRoyaume().score(true)[0];
		System.out.println(gagnant + " a un score de " + scoreMax);
		for (int i = 1; i < listeJoueurs.size(); i++) {
			listeJoueurs.get(i).setScore(listeJoueurs.get(i).getRoyaume().score(true)[0]+listeJoueurs.get(i).getScore(0), 0); 
			System.out.println(listeJoueurs.get(i).getColor() + " a un score de " + listeJoueurs.get(i).getScore(0));
			if (listeJoueurs.get(i).getScore(0) > scoreMax) {
				scoreMax = listeJoueurs.get(i).getScore(0);
				gagnant = listeJoueurs.get(i).getColor();
			}
		}
		if (dynastie == 0) {
		System.out.println("Le gagnant est le joueur " + gagnant + " avec un score de " + scoreMax);
		} 	

	} // public void fin()
	
	/**
	 * Permet de definir les joueur pour une partie
	 * @param listeJoueur
	 */
	public void setListeJoueur(List<Joueur> listeJoueur) {
		this.listeJoueurs = listeJoueur;
	}
}
