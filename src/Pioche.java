import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pioche {
	List<Integer> keysTour;
	List<Integer> keysNext;
	List<Integer> keysReste;

	public static Map<Integer, String[][]> dominos;

	/**
	 * Ce constructeur initialise la pioche pour chaque jeu. Il utilise le nombre de
	 * joueurs afin de créer des liste de tuiles a chisir de la bonne dimention.
	 * 
	 * @param nbJoueurs
	 */
	public Pioche(int nbJoueurs) {
		this.keysReste = IntStream.rangeClosed(1, 48).boxed().collect(Collectors.toList());
		System.out.println("Pioche avant melange :" + keysReste);
		Collections.shuffle(keysReste);
		System.out.println("Pioche apres melange :" + keysReste);
		keysNext = keysReste.subList(0, nbJoueurs);
		keysNext.sort(Comparator.naturalOrder());
		keysReste = keysReste.subList(nbJoueurs, keysReste.size());
		this.keysTour = null;
		initDomino();
	} // public Pioche()

	/**
	 * Cette fonction utilise un scanner avec comme entree le fichier csv fournit
	 * afin de stocker les informations des dominos dans une Map avec en key leur
	 * numeros et en attribut une liste de deux liste de string corespondant au type
	 * et au nombre de couronnes.
	 */
	public void initDomino() {
		try {
			Scanner scanner = new Scanner(new File("./dominos.csv"));
			dominos = new HashMap<>();
			String line = "";
			line = scanner.next();
			String[] elem = new String[5];
			String[][] cases = new String[2][2];
			for (int key = 1; key <= 48; key++) { // iteration a travers les lignes du fichier
				line = scanner.next();
				elem = line.split(",");
				cases[0] = Arrays.copyOf(elem, 2);
				cases[1] = Arrays.copyOfRange(elem, 2, 4);
				Collections.reverse(Arrays.asList(cases[0]));
				Collections.reverse(Arrays.asList(cases[1]));
				dominos.put(key, Arrays.copyOf(cases, 2));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//affichage
		System.out.println("Recuperation des dominos :");
		Set<Entry<Integer, String[][]>> entries = dominos.entrySet();
		for (Map.Entry<?, ?> entry : entries) {
			String[][] list = dominos.get(entry.getKey());
			System.out.format("%8d", entry.getKey());
			System.out.format("%8s %6s %8s %6s \n", list[0][0], list[0][1], list[1][0], list[1][1]);
		}
	}

	/**Permet de recupere les atribut d'un domino en fonction dont son numero.
	 * @param key
	 * @return
	 */
	public String[][] getDomino(int key) {
		return dominos.get(key);
	}

	/**Permet de recuperer tout les numeros qu'il reste dans la pioche
	 * @return
	 */
	public List<Integer> getReste() {
		return keysReste;
	} // public int[] getReste()

	/**Permet de tirer les prochaines tuiles a choisir et de passer celles qu'on vient de choisir en tuiles a poser.
	 * @param nbJoueurs
	 */
	public void setProchaineTuiles(int nbJoueurs) {
		keysTour = keysNext;
		if (keysReste.size() >= nbJoueurs) {
			keysNext = keysReste.subList(0, nbJoueurs);
			keysNext.sort(Comparator.naturalOrder());
			keysReste = keysReste.subList(nbJoueurs, keysReste.size());
		}
	} // public void change()

	/**
	 * @return les tuiles a jouer pour le tour en cour
	 */
	public List<Integer> getTour() {
		return keysTour;
	} // public int[] getTour()

	/**
	 * @return les tuiles parmis lesquel il faut choisir
	 */
	public List<Integer> getNext() {
		return keysNext;
	} // public int[] getNext()


}
