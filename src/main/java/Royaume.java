import java.util.ArrayList;

/**
 * La classe royaume gére le plateau de jeux que posséde chaque joueur.
 *
 */
public class Royaume {
	String[][][] royaume;
	int[] ecartHorizontal;
	int[] ecartVertical;
	int tailleTab;
	ArrayList<Integer> casesDejaTraitees;

	/**
	 * Constructeur de la classe Royaume new Royaume() va créer un royaume vierge
	 * avec le chateau au centre
	 * 
	 * @param tailleT taille du royaume
	 */
	public Royaume(int tailleT) {
		tailleTab = tailleT;
		this.royaume = new String[tailleTab][tailleTab][2];
		this.royaume[tailleTab / 2][tailleTab / 2][0] = "chateau";
		this.royaume[tailleTab / 2][tailleTab / 2][1] = "chateau";
		ecartHorizontal = new int[2];
		ecartVertical = new int[2];
	} // fin du constructeur

	public int getTailleTab() {
		return tailleTab;
	}

	/**
	 * Parcoure toutes les cases du royaume et l'affiche.
	 */
	public void afficheRoyaume() {
		System.out.printf("%11s%10s%13s%11s%13s%13s%13s%11s%11s", "0", "1", "2", "3", "4", "5", "6", "7", "8");
		for (int y = 0; y < tailleTab; y++) {
			System.out.println("");
			System.out.print(y + "->");
			for (int x = 0; x < tailleTab; x++) {
				if (royaume[x][y][0] == null) {
					System.out.print("|");
					System.out.printf("%11s", "0");
				} else {
					if (royaume[x][y][0].equals("chateau")) {
						System.out.print("|");
						System.out.printf("%-8s %2s", "Chateau", "0");
					} else {
						System.out.print("|");
						System.out.printf("%-8s %2s", royaume[x][y][0], royaume[x][y][1]);
					}
				}

			}
		}
		System.out.println(" ");
	}

	/**
	 * Fonction recursive qui scanne un domaine (groupe de cases du meme type) é
	 * partir d'une premiere case. Le cas de base étant une case déjé traitée, un
	 * chateau ou une case vide.
	 * 
	 * On ajoute la case é la liste des cases déjé traitées pour étre certain de ne
	 * pas la compter deux fois. La fonction 10*x+y réalise une bijection entre les
	 * cases et les entiers entre 0 et 88 la table contient donc ces entiers plutét
	 * que les coordonées ce qui facilite la détection d'une case déjé traitée.
	 * 
	 * Avant de regarder autour de la case x,y on vérifie successivement que : -la
	 * case n'est pas sur un bord et donc qu'il y a une case a cote -que la case a
	 * cote n'est pas vide -que la case a cote n'est pas deja traitee -que le
	 * domaine est le meme
	 *
	 * si rien autour n'est du meme type on renvoie le tableau {dimension couronne}
	 * si une case est du meme type on incremente les valeur de dimension et
	 * couronne en rappelant la fonction avec les nouveaux parametres
	 * 
	 * @param x                 abscisse de la case
	 * @param y                 ordonnée de la case
	 * @param casesDejaTraitees liste des cases déjé traitées par le comptage
	 * @return un tableau contenant la dimension du domaine et le nombre de
	 *         couronnes qu'il contient
	 */
	public int[] points(int x, int y, ArrayList<Integer> casesDejaTraitees) {
		String domaine = royaume[x][y][0];
		int d = 1;	//dimension du domaine en cours d'analyse

		int c = 0; // nombre de couronne sur le domaine en cours d'analyse

		int couronne = 0;
		try {
			couronne = Integer.valueOf(royaume[x][y][1]);
		} catch (java.lang.NumberFormatException e) { // erreur due a la lecture de null comme string
			couronne = 0;
		}
		int[] res = new int[2];
		casesDejaTraitees.add(100 * x + y);
		if (y > 0 && royaume[x][y - 1][0] != null && !casesDejaTraitees.contains(100 * x + (y - 1))
				&& royaume[x][y - 1][0].equals(domaine)) {
			res = points(x, y - 1, casesDejaTraitees);
			d += res[0];
			c += res[1];
		}
		if (x > 0 && royaume[x - 1][y][0] != null && !casesDejaTraitees.contains(100 * (x - 1) + y)
				&& royaume[x - 1][y][0].equals(domaine)) {
			res = points(x - 1, y, casesDejaTraitees);
			d += res[0];
			c += res[1];
		}
		if (y < tailleTab - 1 && royaume[x][y + 1][0] != null && !casesDejaTraitees.contains(100 * (x) + y + 1)
				&& royaume[x][y + 1][0].equals(domaine)) {
			res = points(x, y + 1, casesDejaTraitees);
			d += res[0];
			c += res[1];
		}
		if (x < tailleTab - 1 && royaume[x + 1][y][0] != null && !casesDejaTraitees.contains(100 * (x + 1) + y)
				&& royaume[x + 1][y][0].equals(domaine)) {
			res = points(x + 1, y, casesDejaTraitees);
			d += res[0];
			c += res[1];
		}

		int[] DimensionCouronne = { d, c + couronne };
		return (DimensionCouronne);

	} // fin points

	/**
	 * La fonction score appelle la fonction point sur chaque case et crée une liste
	 * de comptage contenant les couples dimension/couronne de chaque domaine, il
	 * suffit ensuite de sommer les produits de ces couples pour avoir le score.
	 * 
	 * unroyaume.score() sera le score du royaume "unroyaume"
	 * @param compteBonus true si on veut compter les bonus
	 * @return le score du royaume, le nombre de domaine, le nombre de couronne et
	 *         la taille du plus grand domaine
	 */
	public int[] score(Boolean compteBonus) {
		casesDejaTraitees = new ArrayList<Integer>();
		ArrayList<int[]> listeDeComptage = new ArrayList<int[]>();
		for (int x = 0; x < tailleTab; x++) {
			for (int y = 0; y < tailleTab; y++) {
				try {
					String domaine = royaume[x][y][0];
					if (!(domaine == null || casesDejaTraitees.contains(100 * x + y) || domaine.equals("chateau"))) {
						listeDeComptage.add(points(x, y, casesDejaTraitees));
					}
				} catch (NullPointerException e) { // erreur due a la lecture de null comme string
				}
				// 1,0 car chaque case est un domaine mais ne contient potentielement pas de
				// couronne
			}
		}
		// debut de la partie qui compte les points une fois que la liste de comptage
		// est complete
		int score = 0;
		int nbCouronnes = 0;
		int maxTaille = 0;
		for (int i = 0; i < listeDeComptage.size(); i++) {
			score = score + listeDeComptage.get(i)[0] * listeDeComptage.get(i)[1];

			nbCouronnes = nbCouronnes + listeDeComptage.get(i)[1];
			if (listeDeComptage.get(i)[0] > maxTaille) {
				maxTaille = listeDeComptage.get(i)[0];
			}
		}

		if (compteBonus) {
			// fin de la partie qui compte les points une fois que la liste de comptage est
			// complete
			if (empireDuMilieu()) {
				score = score + 5;
			}
			if (harmonie()) {
				score = score + 10;
			}
		}

		int[] res = new int[] { score, listeDeComptage.size(), nbCouronnes, maxTaille };

		return res;
	} // fin score

	/**
	 * La fonction empire du milieu renvoie true si le chateau est au centre du
	 * royaume et false sinon
	 * 
	 * @return
	 */
	public boolean empireDuMilieu() {
		return (ecartVertical[0] == -ecartVertical[1] && ecartHorizontal[0] == -ecartHorizontal[1]);
		// si il y a autant de case au dessus en bas a gauche et a droite du chateau il
		// est au milieu
	}

	/**
	 * La fonction harmonie renvoie true si il n'y a aucun trou dans le royaume et
	 * false sinon Elle parcoure le royaume ligne par ligne chaque case non vide
	 * incremente ligne, si ligne est egal a 5 ou 7 en mode 7*7 le compteur est
	 * incremente. Il faut avoir compteur = ou 7 pour renvoyer true.
	 * 
	 * @return true si harmonie est verifié false sinon
	 */
	public boolean harmonie() {
		int compteur = 0;
		int ligne = 0;
		for (int y = 0; y < tailleTab; y++) {
			if (ligne == tailleTab / 2 + 1) {// tailleTab/2 + 1 pour s'adapter au cas 7*7
				compteur += 1;
			}
			ligne = 0;
			for (int x = 0; x < tailleTab; x++) {
				if (royaume[x][y][0] != null) {
					ligne += 1;
				}
			}
		}
		return (compteur == tailleTab / 2 + 1);
	}

	/**
	 * Place la tuile décrite en paramétre dans le royaume aux coordonnées indiquées
	 * 
	 * Cette fonction sera utilisée suite é la vérification du placement de la tuile
	 * par la methode vérification.
	 * 
	 * @param x1    abscisse de la premiére case de la tuile
	 * @param y1    ordonnee de la premiére case de la tuile
	 * @param compteEcart
	 * @param case1 couple type/couronne de la premiére case de la tuile
	 */
	public void poseCase(int x1, int y1, String[] case1, Boolean compterEcart) {
		royaume[x1][y1] = case1;
		if (compterEcart) {
			ecartHorizontal[0] = min(ecartHorizontal[0], x1 - tailleTab / 2);
			ecartHorizontal[1] = max(ecartHorizontal[1], x1 - tailleTab / 2);
			ecartVertical[0] = min(ecartVertical[0], y1 - tailleTab / 2);
			ecartVertical[1] = max(ecartVertical[1], y1 - tailleTab / 2);
		}
	}// fin poseTuile

	/**
	 * La fonction vérifie que la case entrée en paramétre peut étre placée dans le
	 * royaume aux coordonnées entrées en paramétre.
	 * 
	 * Elle vérifie successivement que la case n'est pas déjé occupée, qu'une case
	 * autour é le méme type que celui de la case é placer ou bien est un chateau,
	 * que le royaume reste 5X5 et que le placement n'est pas hors du royaume.
	 *
	 * 
	 * @param x1    abscisse de la case
	 * @param y1    ordonnée de la case
	 * @param case1 type et nombre de couronne de la case
	 * @param  nbCase taille du royaume
	 * @return true si le placement est possible false sinon
	 */
	public boolean verification(int x1, int y1, String[] case1, int nbCase) {
		String typex1y1 = royaume[x1][y1][0];
		String typeCase1 = case1[0];
		if (typex1y1 != null) {

			return (false);
		} else {
			if (nbCase == 1) {
				boolean frontiereCommune = false;
				// verifie si une case autour a le meme type ou le type chateau
				try {
					String caseDessus1 = royaume[x1 + 1][y1][0];
					frontiereCommune = frontiereCommune || caseDessus1.equals(typeCase1);
					frontiereCommune = frontiereCommune || caseDessus1.equals("chateau");
				} catch (Exception e) {
					frontiereCommune = frontiereCommune || false;
				}
				try {
					String caseDroite1 = royaume[x1][y1 + 1][0];
					frontiereCommune = frontiereCommune || caseDroite1.equals(typeCase1);
					frontiereCommune = frontiereCommune || caseDroite1.equals("chateau");
				} catch (Exception e) {
					frontiereCommune = frontiereCommune || false;
				}
				try {
					String caseBas1 = royaume[x1 - 1][y1][0];
					frontiereCommune = frontiereCommune || caseBas1.equals(typeCase1);
					frontiereCommune = frontiereCommune || caseBas1.equals("chateau");
				} catch (Exception e) {
					frontiereCommune = frontiereCommune || false;
				}
				try {
					String caseGauche1 = royaume[x1][y1 - 1][0];
					frontiereCommune = frontiereCommune || caseGauche1.equals(typeCase1);
					frontiereCommune = frontiereCommune || caseGauche1.equals("chateau");
				} catch (Exception e) {
					frontiereCommune = frontiereCommune || false;
				}

				if (!frontiereCommune) {

					return (false);
				}
			}
			if (!resteValide(x1, y1)) {
				return false;
			}

			if (nbCase == 1) {
				// on verifie si il est posible de poser au moins une case autour
				int conteur = 0;
				try {
					royaume[x1][y1 + 1][0].getClass();
				} catch (Exception e) {
					if (resteValide(x1, y1 + 1)) { // verifie si la case en question reste dans le royaume
						conteur++;
					}
				}
				try {
					royaume[x1][y1 - 1][0].getClass();
				} catch (Exception e) {
					if (resteValide(x1, y1 - 1)) {
						conteur++;
					}
				}
				try {
					royaume[x1 + 1][y1][0].getClass();
				} catch (Exception e) {
					if (resteValide(x1 + 1, y1)) {
						conteur++;
					}
				}
				try {
					royaume[x1 - 1][y1][0].getClass();
				} catch (Exception e) {
					if (resteValide(x1 - 1, y1)) {
						conteur++;
					}
				}
				if (conteur == 0) {
					return false;
				}
			}
			return true;
		}
	}// fin verification

	/**
	 * La fonction resteValide vérifie que le fait de poser une case aux coordonnées
	 * en argment n'enfreins pas la régle qui définie les limites du royaume (5*5 ou
	 * 7*7)
	 * 
	 * @param x1
	 * @param y1
	 * @return Le boolean correspondant é la réponse
	 */
	public boolean resteValide(int x1, int y1) {
		// verifie que le plateau reste 5*5 max (ou 7*7)
		int decalageH = x1 - (tailleTab / 2);
		if (decalageH < ecartHorizontal[0]) {
			if ((ecartHorizontal[1] - decalageH + 1) > (tailleTab / 2 + 1)) {
				// System.out.println("le royaume doit etre 5X5");
				return (false);
			}
		} else {
			if (decalageH > ecartHorizontal[1]) {
				if ((decalageH - ecartHorizontal[0] + 1) > (tailleTab / 2 + 1)) {
					// System.out.println("le royaume doit etre 5X5");
					return (false);
				}
			}
		}

		int decalageV = y1 - (tailleTab / 2);
		if (decalageV < ecartVertical[0]) {
			if ((ecartVertical[1] - decalageV + 1) > (tailleTab / 2 + 1)) {
				// System.out.println("le royaume doit etre 5X5");
				return (false);
			}
		} else {
			if (decalageV > ecartVertical[1]) {
				if ((decalageV - ecartVertical[0] + 1) > (tailleTab / 2 + 1)) {
					// System.out.println("le royaume doit etre 5X5");
					return (false);
				}
			}

		}
		return true;
	}

	private int min(int i, int j) {
		if (i < j) {
			return i;
		} else {
			return j;
		}
	}

	private int max(int i, int j) {
		if (i > j) {
			return i;
		} else {
			return j;
		}
	}

	/**
	 * Renvoie le type de la case du royaume dont les coordonnées sont en paramétre.
	 * 
	 * @param abscisse de la case
	 * @param ordonnee de la case
	 * @return le type de la case
	 */
	public String getType(int abscisse, int ordonnee) {
		return royaume[abscisse][ordonnee][0];
	}

	/**
	 * Renvoie le nombre de couronne de la case du royaume dont les coordonnées sont
	 * en paramétre.
	 * 
	 * @param abscisse de la case
	 * @param ordonnee de la case
	 * @return le nombre de couronne de la case
	 */
	public String getCouronne(int abscisse, int ordonnee) {
		return royaume[abscisse][ordonnee][1];
	}

	/**
	 * 
	 * @return Le tableau contenant les écarts horizontaux maximums avec le chateau
	 */
	public int[] getEcartHorizontal() {
		return ecartHorizontal;
	}

	/**
	 * 
	 * @return Le tableau contenant les écarts verticaux maximums avec le chateau
	 */
	public int[] getEcartVertical() {
		return ecartVertical;
	}

	/**
	 * Permet de réinitialiser l'écart lorque un joueur annule son coup Pour ca le
	 * tableau est parcouru et les valeurs enrengistrées corespondent aux premieres
	 * et dernieres lignes/colones ou on trouve une case utilisée.
	 */
	public void majEcart() {
		int line;
		int col;
		int sum;
		ecartVertical[0] = -1;
		for (line = 0; line < tailleTab; line++) {
			sum = 0;
			for (col = 0; col < tailleTab; col++) {
				try {
					getType(col, line).equals(null);
					sum++;
				} catch (Exception e) {

				}
			}
			if (sum != 0) {
				ecartVertical[1] = line - 4;
				if (ecartVertical[0] == -1) {
					ecartVertical[0] = line - 4;
				}
			}
		}
		ecartHorizontal[0] = -1;
		for (col = 0; col < tailleTab; col++) {
			sum = 0;
			for (line = 0; line < tailleTab; line++) {
				try {
					getType(col, line).equals(null);
					sum++;
				} catch (Exception e) {

				}
			}
			if (sum != 0) {
				ecartHorizontal[1] = col - 4;
				if (ecartHorizontal[0] == -1) {
					ecartHorizontal[0] = col - 4;
				}
			}
		}
	}

	/**
	 * La fonction verifie que la pose de la secconde case ne dépace pas les limites
	 * du royaume.
	 * 
	 * @param direction
	 * @param nvCoor
	 * @return
	 */
	public boolean depacementValide(String direction, int nvCoor) {
		int[] pastCoor;
		if (direction.equals("vertical")) {
			pastCoor = ecartVertical;
		} else {
			pastCoor = ecartHorizontal;
		}
		int testCoorMin = Math.min(pastCoor[0], nvCoor);
		int testCoorMax = Math.max(pastCoor[1], nvCoor);
		System.out.println(testCoorMax - testCoorMin);
		return (testCoorMax - testCoorMin) >= tailleTab / 2 + 1;
	}

}
