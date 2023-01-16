package com.example.simongame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game implements Runnable {

    //Tenemos 4 colores en nuestro juego color verde = 0, color rojo = 1, color amarillo = 2, color azul = 3;
    //Declaro la clase random para la secuencia de los numeros la secuencia va ir sumandose por cada acierto del jugador
    //Se va incrementado la velocidad y la secuencia por cada acierto del jugador


    //PLAYER'S DATA
    private int highScore;
    private int lowScore;
    private int ScoreNumber;

    private String message;

    private HashMap<String, Integer> playerData;
    private List<Integer> secuenceToGuess;
    private List<Integer> playerGuessSecuence;

    //MATCH'S DATA
    private int gamesPlayed;
    private final Random random = new Random();


    public Game() {

        playerData = new HashMap<>();
        secuenceToGuess = new ArrayList<>();
        playerGuessSecuence = new ArrayList<>();
    }


    //Creamos principales funciones como JUGAR ,
    //HILO QUE REPRODUZCA LA MISMA SECUENCIA QUE LLAME A OTRO Y OTRO.


    /**
     *
     * @param size the number of guesses
     */
    public void generateSecuence(int size) {
//        secuenceToGuess.add(0);
//        secuenceToGuess.add(1);
//        secuenceToGuess.add(2);
//        secuenceToGuess.add(3);
        for (int i = 0; i < size; i++) {
            secuenceToGuess.add(random.nextInt(4));
        }

    }


    public void startGame(List<Integer> playerGuessesSecuence) {
//        do {
//            gamesPlayed++;
//            generateSecuence(gamesPlayed);
//        } while (logic(playerGuessesSecuence));
        if (logic(playerGuessesSecuence)){
                message = "has ganado";
        }else{
            message = "has perdido";
        }
        //sin no es verdad el jugador pierde la partida.
    }


    /**
     *
     * @return true if the gamer's secuence it's equals otherwise returs false
     */
    public boolean logic(List<Integer> playerGuessSecuence) {
        if (secuenceToGuess.equals(playerGuessSecuence)) {
            ScoreNumber++;
            return true;
        }
        return false;
    }

    public void reset() {
        secuenceToGuess.clear();
    }

    public void displayStats() {

    }

    public List<Integer> getSecuenceToGuess() {
        return secuenceToGuess;
    }

    @Override
    public void run() {

    }

    public String getMessage() {
        return message;
    }
}
