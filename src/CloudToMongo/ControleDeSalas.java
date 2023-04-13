package CloudToMongo;

import java.util.HashMap;

public class ControleDeSalas {


    private HashMap<Integer, Integer> salas;

    public ControleDeSalas() {
        salas = new HashMap<Integer, Integer>();
        for (int i = 0; i < 10; i++) {
            salas.put(i, 0);
        }
    }

    public void move(int salaEntrada, int salaSaida) {
        int valorAtual = salas.get(salaEntrada);
        salas.put(salaEntrada, valorAtual + 1);


        int valorAnterior = salas.get(salaSaida);
        salas.put(salaSaida, valorAnterior - 1);
    }


    public void imprimirSalas() {
        System.out.println("Salas:");
        for (int i = 0; i < 10; i++) {
            System.out.printf("Sala %d: %d%n", i, salas.get(i));
        }
    }


}
