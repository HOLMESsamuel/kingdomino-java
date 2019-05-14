import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IA {

	/**
	 * @param royaume
	 * @param tuile
	 * @return la liste de toute les coordonées ou il est possible de place la
	 *         premiere case
	 */
	public List<List<int[]>> trouverPlacePossible(Royaume royaume, String[][] tuile) {
		List<List<int[]>> placePossible = new ArrayList<List<int[]>>();
		List<int[]> placeCase1 = new ArrayList<int[]>();
		List<int[]> placeCase2 = new ArrayList<int[]>();
		// parcour le tableau
		for (int colonne = 0; colonne < royaume.getTailleTab(); colonne++) {
			for (int ligne = 0; ligne < royaume.getTailleTab(); ligne++) {
				// verifie si la premiere case est posable
				if (royaume.verification(colonne, ligne, tuile[0], 1)) {
					placeCase1.add(new int[] { colonne, ligne });
				}
				// pareille pour la deuxieme
				if (royaume.verification(colonne, ligne, tuile[1], 1)) {
					placeCase2.add(new int[] { colonne, ligne });
				}
			}
		}
		placePossible.add(placeCase1);
		placePossible.add(placeCase2);
		return placePossible;
	} // public List<List<int[]>> trouverPlacePossible(Royaume royaume, String[][]
		// tuile)

	/**
	 * @param royaume
	 * @param placePossible
	 * @param tuile
	 * @return une liste associant chaque places de tuile a un nombre de point
	 */
	public List<List<List<Double>>> pointsParPlace(Royaume royaume, List<List<int[]>> placePossible, String[][] tuile) {
		List<List<List<Double>>> pointsParPlace = new ArrayList<List<List<Double>>>();
		List<List<Double>> pointPlaceCase;

		String[] del = new String[2]; // utilisé pour suprimer les tuille apres avoir compté les points
		Double b;
		int[][] direction = new int[][] { { 0, -1 }, { 0, 1 }, { 1, 0 }, { -1, 0 } }; // nord sud est ouest
		for (int nbCase = 0; nbCase < 2; nbCase++) {
			pointPlaceCase = new ArrayList<List<Double>>();
			for (int[] unePlace : placePossible.get(nbCase)) {
				List<Double> point = new ArrayList<Double>();
				royaume.poseCase(unePlace[0], unePlace[1], tuile[nbCase], false);

				// pour chaque case un compte les points dans les quatres direction (-1 etant
				// l'impossibilité de jouer dans cette direction)
				for (int[] d : direction) {
					try {
						if (royaume.verification(unePlace[0] + d[0], unePlace[1] + d[1], tuile[1 - nbCase], 2)) {
							royaume.poseCase(unePlace[0] + d[0], unePlace[1] + d[1], tuile[1 - nbCase], false);
							b = pointBonus(royaume, unePlace[0], unePlace[1], unePlace[0] + d[0], unePlace[1] + d[1]);
							point.add(royaume.score(false)[0] + b);
							royaume.poseCase(unePlace[0] + d[0], unePlace[1] + d[1], del, false);
						} else {
							point.add(-1.0);
						}
					} catch (Exception e) {
						point.add(-1.0);
					}
				}
				royaume.poseCase(unePlace[0], unePlace[1], del, false);
				pointPlaceCase.add(point);
			}
			pointsParPlace.add(pointPlaceCase);
		}
		return pointsParPlace;
	} // public List<List<List<Integer>>> pointsParPlace(Royaume royaume,
		// List<List<int[]>> placePossible)

	/**
	 * @param royaume
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return un double qui permet de mettre en valeur cetain placement par rapport
	 *         a d'autre (agrandissement de terrain, sans trou, centré...)
	 */
	public Double pointBonus(Royaume royaume, int x1, int y1, int x2, int y2) {
		Double bonus = 0.0;
		int[][] direction = new int[][] { { 0, -1 }, { 0, 1 }, { 1, 0 }, { -1, 0 } }; // nord sud est ouest
		// Agrandir le terrain
		for (int[] d : direction) {
			try {
				if (royaume.getType(x1 + d[0], y1 + d[1]).equals(royaume.getType(x1, y1))) {
					bonus += 0.1;
				}
			} catch (Exception e) {
			}
			try {
				if (royaume.getType(x2 + d[0], y2 + d[1]).equals(royaume.getType(x2, y2))) {
					bonus += 0.1;
				}
			} catch (Exception e) {
			}
		}
		// Eviter un trou
		for (int[] d : direction) {
			try {
				// Deux cases vides du meme coté ne cimpte pas comme un trou
				if ((royaume.getType(x1 + d[0], y1 + d[1]) == null
						&& royaume.getType(x1 + d[0] * 2, y1 + d[1] * 2) != null)
						^ (royaume.getType(x2 + d[0], y2 + d[1]) == null
								&& royaume.getType(x2 + d[0] * 2, y2 + d[1] * 2) != null)) {
					bonus -= 0.2;
				}
			} catch (Exception e) {
			}
		}
		// Garder un royaume centré (si c'est encore possible)
		int ecartChateau = royaume.getTailleTab() / 4;
		if (!(royaume.getEcartHorizontal()[1] > ecartChateau || royaume.getEcartHorizontal()[0] < -ecartChateau)
				&& !(royaume.getEcartVertical()[1] > ecartChateau || royaume.getEcartVertical()[0] < -ecartChateau)) {
			if (x1 - (royaume.getTailleTab() / 2) < -ecartChateau
					|| x1 - (royaume.getTailleTab() / 2 + 1) > ecartChateau) {
				bonus -= 0.1;
			}
			if (x2 - (royaume.getTailleTab() / 2) < -ecartChateau || x2 - (royaume.getTailleTab() / 2) > ecartChateau) {
				bonus -= 0.1;
			}
			if (y1 - (royaume.getTailleTab() / ecartChateau) < -2 || y1 - (royaume.getTailleTab() / 2) > ecartChateau) {
				bonus -= 0.1;
			}
			if (y2 - (royaume.getTailleTab() / 2) < -ecartChateau || y2 - (royaume.getTailleTab() / 2) > ecartChateau) {
				bonus -= 0.1;
			}
		} else { // eviter le damier de perte
		}

		// Tuile suivante

		// Eviter de posser a coté du chateau
		for (int[] d : direction) {
			try {
				if (royaume.getType(x1 + d[0], y1 + d[1]).equals("chateau")) {
					bonus -= 0.05;
				}
			} catch (Exception e) {
			}
		}
		for (int[] d : direction) {
			try {
				if (royaume.getType(x2 + d[0], y2 + d[1]).equals("chateau")) {
					bonus -= 0.05;
				}
			} catch (Exception e) {
			}
		}

		return bonus;
	}

	/**
	 * @param royaume
	 * @param placePossible
	 * @param tuile
	 * @return le royaume apres avoir fait les calcul necessaire pour poser la tuile
	 */
	public Royaume poseTuile(Royaume royaume, List<List<int[]>> placePossible, String[][] tuile) {
		if (placePossible.get(0).size() + placePossible.get(1).size() != 0) {
			// Verifi qu'il est possible de jouer
			List<List<List<Double>>> pointsParPlace = pointsParPlace(royaume, placePossible, tuile);
			// x, y, direction (n/s/e/o), case
			Double[] coord = maxDePointCoord(pointsParPlace, placePossible);
			int coordX = Math.toIntExact(Math.round(coord[0]));
			int coordY = Math.toIntExact(Math.round(coord[1]));
			int coordDir = Math.toIntExact(Math.round(coord[2]));
			int coordCase = Math.toIntExact(Math.round(coord[3]));

			royaume.poseCase(coordX, coordY, tuile[coordCase], true);
			if (coordDir == 0) {
				royaume.poseCase(coordX, coordY - 1, tuile[1 - coordCase], true);
			} else if (coordDir == 1) {
				royaume.poseCase(coordX, coordY + 1, tuile[1 - coordCase], true);
			} else if (coord[2] == 2) {
				royaume.poseCase(coordX + 1, coordY, tuile[1 - coordCase], true);
			} else if (coord[2] == 3) {
				royaume.poseCase(coordX - 1, coordY, tuile[1 - coordCase], true);
			}
		}
		return royaume;
	} // public Royaume poseTuile (Royaume royaume, List<List<int[]>> placePossible,
		// String[][] tuile)

	/**
	 * Prend en argument le nombre de point par place et liste toutes les place =s
	 * qui rapportent le maximum de point puis choisit alléatoirement l'une de ces
	 * places
	 * 
	 * @param pointsParPlace
	 * @param placePossible
	 * @return la place choisi
	 */
	public Double[] maxDePointCoord(List<List<List<Double>>> pointsParPlace, List<List<int[]>> placePossible) {
		List<Double[]> listeDesMax = new ArrayList<Double[]>(); // liste les place maximisant les point
		// coord x, cord y, direction, premiere case, score
		Double max = -1.0;
		Double temp;
		int d;

		for (int nbCase = 0; nbCase < 2; nbCase++) {
			if (pointsParPlace.get(nbCase).size() != 0) {
				for (int i = 0; i < pointsParPlace.get(nbCase).size(); i++) {
					for (d = 0; d < 4; d++) {
						temp = pointsParPlace.get(nbCase).get(i).get(d);
						if (temp > max) { // si on trouve une valeur plus grande que l'ancien max on réinitialise la
											// liste
							listeDesMax = new ArrayList<Double[]>();
						}
						if (temp >= max) {
							Double[] coord = new Double[5];
							coord[0] = placePossible.get(nbCase).get(i)[0] * 1.0;
							coord[1] = placePossible.get(nbCase).get(i)[1] * 1.0;
							coord[2] = d * 1.0;
							coord[3] = nbCase * 1.0;
							coord[4] = temp;
							max = temp;
							listeDesMax.add(coord);
						}
					}
				}
			}
		}
		if (listeDesMax.size() == 0) {
			listeDesMax.add(new Double[] { 0.0, 0.0, 0.0, 0.0, -1.0 });// si jamais une tuile n'est pas jouable
			return listeDesMax.get(0);
		}
		System.out.println("Les places maximisant les point sont : ");
		for (Double[] unePlace : listeDesMax) {
			System.out.printf("%.0f %.0f dir:%.0f case:%.0f score:%.2f \n", unePlace[0], unePlace[1], unePlace[2],
					unePlace[3], unePlace[4]);
		}
		Collections.shuffle(listeDesMax);
		System.out.printf(" Choix : %.0f %.0f dir:%.0f case:%.0f score:%.2f \n\n", listeDesMax.get(0)[0],
				listeDesMax.get(0)[1], listeDesMax.get(0)[2], listeDesMax.get(0)[3], listeDesMax.get(0)[4]);
		return listeDesMax.get(0);
	} // public int maxDePoint(List<List<int[]>> pointsParPlace)

	/**
	 * @param royaume
	 * @param tempNextTuile
	 * @return le numero de la tuile selectionnée
	 */
	public int choisirTuile(Royaume royaume, List<Integer> tempNextTuile) {
		Double max = -1.0;
		int meilleurNb = -1; // selectionne par default la premiere
		Double tempPoint;
		for (int nbTuile : tempNextTuile) {
			String[][] tuile = Pioche.dominos.get(nbTuile);
			List<List<int[]>> pPossible = trouverPlacePossible(royaume, tuile);
			List<List<List<Double>>> pointPossible = pointsParPlace(royaume, pPossible, tuile);
			tempPoint = maxDePoint(pointPossible, pPossible);
			System.out.println(nbTuile + " permet de marquer " + tempPoint + " points");
			if (tempPoint > max) {
				max = tempPoint;
				meilleurNb = nbTuile;
			}
		}
		if (max == -1) { // si jamais occune des tuile n'est posable prend la premiere
			meilleurNb = tempNextTuile.get(0);
		}
		System.out.println(" Tuile choisi : " + meilleurNb);
		return meilleurNb;
	} // public int choisirTuile (Royaume royaume, List<Integer> tempNextTuile,
		// String[][] tuile)

	/**
	 * @param pointPossible
	 * @param placePossible
	 * @return le nombre de point maximum que peut marquer une tuile si on la jouais
	 */
	public Double maxDePoint(List<List<List<Double>>> pointPossible, List<List<int[]>> placePossible) {
		Double max = -1.0;
		Double temp;
		// pour chaque case on verifi qu'on ne dépasse pas le maximum enrengistré
		if (pointPossible.get(0).size() != 0) {
			for (int i = 0; i < pointPossible.get(0).size(); i++) {
				temp = Collections.max(pointPossible.get(0).get(i));
				if (temp > max) {
					max = temp;
				}
			}
		}
		if (pointPossible.get(1).size() != 0) {
			for (int i = 0; i < pointPossible.get(1).size(); i++) {
				temp = Collections.max(pointPossible.get(1).get(i));
				if (temp > max) {
					max = temp;
				}
			}
		}
		return max;
	} // public int maxDePoint(List<List<int[]>> pointsParPlace)

}
