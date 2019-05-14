import grooptown.ia.PlayerConnector;
import grooptown.ia.model.AvailableMoves;
import grooptown.ia.model.Game;
import grooptown.ia.model.GameState;

/**
 * Launches a Random game.
 * Created by thibautdebroca on 09/01/2019.
 */
public class main2 {

    public static void main(String[] args) throws Exception {
        // With JDK inferior to 8u101 you need to disable SSL validation.
        // disableSSLValidation();
        PlayerConnector.baseUrl = "https://domi-nation.grooptown.com";
        int playerCount = 3;
        int maxTurnToPlay = 20;

        PlayerConnector[] playerConnectors = new PlayerConnector[playerCount];

        Game newGame = PlayerConnector.createNewGame(playerCount);

        for (int i = 0; i < playerConnectors.length; i++) {
            playerConnectors[i] = new PlayerConnector(newGame.getUuid());
            playerConnectors[i].joinGame("Sam" + i);
        }

        String gameStateAsString = PlayerConnector.getGameStateAsString(newGame.getUuid());
        System.out.println(gameStateAsString);

        GameState gameState = PlayerConnector.getGameState(newGame.getUuid());
        System.out.println(gameState);

        for (int i = 0; i < maxTurnToPlay; i++) {
            PlayerConnector playerConnector = getCurrentPlayerConnector(playerConnectors, gameState);
            AvailableMoves availableMove = playerConnector.getAvailableMove();
            playerConnector.playMove(0);
            System.out.println("Available Moves are: " + availableMove);
            gameState = PlayerConnector.getGameState(newGame.getUuid());
            System.out.println("Game State is " + gameState);
        }
    }


    /**
     * Gets the Player that should play the current Turn. If not found, return null.
     * @param playerConnectors All player Connectors.
     * @param gameState State of the Game.
     * @return The Player that should play the current Turn. If not found, return null.
     */
    private static PlayerConnector getCurrentPlayerConnector(PlayerConnector[] playerConnectors,
                                                             GameState gameState) throws Exception {
        for (PlayerConnector playerConnector : playerConnectors) {
            if (gameState.getCurrentPlayer().getName().equals(playerConnector.getPlayer().getName())) {
                return playerConnector;
            }
        }
        throw new Exception("There is no player who should play.");
    }

}