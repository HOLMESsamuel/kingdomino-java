
public class Joueur {

	Royaume royaume;
	String color;
	Boolean isIA;
	int nextTuile;
	String name;
	int[] score;

	/**
	 * Le constructeur cr�e un joueur avec les donn�es en param�tre
	 * @param col
	 * @param isIA
	 * @param name
	 */
	public Joueur(String col, Boolean isIA, String name) {
		this.color = col;
		this.isIA = isIA;
		score = new int[3]; //le tableau de 3 �l�ments est utilis� dans le mode dynastie qui se joue en 3 parties
		// dans le mode classique seul le premier �l�ment est utilis�
		score[0] = 0;
		score[1] = 0;
		score[2] = 0;
		if(name.equals("")) { // attribue automatiquement la couleur comme nom si aucun nom n'est entr�
			this.name=col;
		} else {
			this.name=name;
		}

	} // public Joueur()
	
	/**
	 * La fonction appelle le constructeur royaume pour attribuer un nouveau royaume vide au joueur
	 * @param tailleT la taille du royaume � creer (pour unn 5*5 ou 7*7)
	 */
	public void initRoyaume(int tailleT) {
		this.royaume = new Royaume(tailleT);		
	}
	
	/**
	 *
	 * @return le nom du joueur
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * 
	 * @return un boolean true si le joueur est une IA false sinon
	 */
	public Boolean isIA() {
		return isIA;
	}

	/**
	 * 
	 * @return Le royaume du joueur
	 */
	public Royaume getRoyaume() {
		return royaume;
	}
	
	/**
	 * Remplace le royaume du joueur par celui entr� en param�tre
	 * @param roy
	 */
	public void setRoyaume(Royaume roy) {
		royaume=roy;
	}

	/**
	 * 
	 * @return La couleur du joueur
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * Remplace la prochaine tuile du joueur par celle entr�e en param�tre
	 * @param nb
	 */
	public void setNextTuile(int nb) {
		nextTuile=nb;
	}
	
	/**
	 * 
	 * @return la prochaine tuile du joueur
	 */
	public int getNextTuile() {
		return nextTuile;
	}
	
	/**
	 * La fonction setScore donne le score entr� en param�tre � la partie �galement en param�tre au joueur
	 * @param score
	 * @param i
	 */
	public void setScore(int score, int i) {
		this.score[i] = score;
	}
	
	/**
	 * La fonction renvoie le score de la ieme partie du joueur
	 * @param i
	 * @return le score de la ieme partie
	 */
	public int getScore(int i) {
		return score[i];
	}
	
	/**
	 * 
	 * @return le score total des trois parties du joueur en mode dynastie
	 */
	public int getTotal() {
		return score[0]+score[1]+score[2];
	}

}
