
import  java.io.*;
import javax.sound.sampled.*;
 
 
public class Audio extends Thread{ //la musique doit étre lue pendant que le reste du programme tourne
     
     
    AudioInputStream audioInputStream = null;
    SourceDataLine line;
    boolean running;
    /**
     * run définie la tache é axecuteren paralelle du premier thread
     */
    public void run(){
    	//la premiére partie sert é charger le fichier .wav sous forme d'audioInputStream : une classe qui comprend un INputStream un format et une longueur
    	//debut de la premiére partie
        File fichier = new File("./resources/musique.wav");
         
        try {
            audioInputStream = AudioSystem.getAudioInputStream(fichier);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fin de la premiére partie 
        
        
         AudioFormat audioFormat = audioInputStream.getFormat();
         //la classe Dataline.info ajoute le format du fichier et les dimensions du buffer é dataline
         DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat); 
         
         //crée une line adaptée au format récupéré ci-dessus
         try {
             line = (SourceDataLine) AudioSystem.getLine(info);
                        
             } catch (LineUnavailableException e) {
               e.printStackTrace();
               return;
             }
         
        //ouvre la line avec le bon format
        try {
                line.open(audioFormat);
        } catch (LineUnavailableException e) {
                    e.printStackTrace();
                    return;
        }
        //demarre la lecture de la line
        line.start();
        try {
            byte bytes[] = new byte[1024]; // tableau de 1024 octets donc 1ko
            int bytesRead=0;
            while ((bytesRead = audioInputStream.read(bytes, 0, bytes.length)) != -1) {
            	 //bytesRead = audioInputStream.read(bytes, 0, bytes.length) rempli bytes de 0 a bytesLength avec 
            	 //les données du fichier audio, renvoie le nombre d'octets lu ou -1 si il n'y a plus rien a lire
                 line.write(bytes, 0, bytesRead);//lis bytes de 0 é bytesRead

                }
        } catch (IOException io) {
            io.printStackTrace(); //l'exception intervient si la taille de l'element é lire dépasse l'octet
            return;
        }
    }
    
    /***
     * La fonction state permet de donner au boolean running la valeur entrée en paramétre
     * Elle doit étre vraie si la musique est en cours de lecture et fausse sinon
     * @param state
     */
    public void state(boolean state) {
    	running = state;
    }
    
    /**
     * La fonction getRunning permet de savoir si la musique est en cour de lecture ou non
     * @return un boolean corespondant a l'état de lecture de la musique
     */
    public boolean getRunning() {
    	return running;
    }
}
