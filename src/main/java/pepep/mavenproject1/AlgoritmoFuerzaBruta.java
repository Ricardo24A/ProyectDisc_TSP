/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pepep.mavenproject1;

/**
 *
 * @author ricky
 */
import java.util.ArrayList;
import java.util.Collections;

public class AlgoritmoFuerzaBruta {
    private Grafo grafo;
    private ArrayList<Integer> mejorRuta;
    private double menorDistancia;
    private double tiempoEjecucion; // Tiempo en segundos

    public AlgoritmoFuerzaBruta(Grafo grafo) {
        this.grafo = grafo;
        this.mejorRuta = new ArrayList<>();
        this.menorDistancia = Double.MAX_VALUE;
        this.tiempoEjecucion = 0;
    }

    public void resolver() {
        long inicio = System.nanoTime(); // Marca el inicio del tiempo

        ArrayList<Integer> indicesCiudades = new ArrayList<>();
        for (int i = 0; i < grafo.getCiudades().size(); i++) {
            indicesCiudades.add(i);
        }

        // Generar todas las permutaciones posibles
        permutar(indicesCiudades, 0);

        long fin = System.nanoTime(); // Marca el fin del tiempo
        tiempoEjecucion = (fin - inicio) / 1_000_000_000.0; // Convertir a segundos
    }

    private void permutar(ArrayList<Integer> ruta, int inicio) {
        if (inicio == ruta.size()) {
            calcularDistanciaRuta(ruta);
            return;
        }

        for (int i = inicio; i < ruta.size(); i++) {
            Collections.swap(ruta, inicio, i);
            permutar(ruta, inicio + 1);
            Collections.swap(ruta, inicio, i);
        }
    }

    private void calcularDistanciaRuta(ArrayList<Integer> ruta) {
        double distanciaTotal = 0;

        for (int i = 0; i < ruta.size() - 1; i++) {
            distanciaTotal += grafo.getDistancia(ruta.get(i), ruta.get(i + 1));
        }

        distanciaTotal += grafo.getDistancia(ruta.get(ruta.size() - 1), ruta.get(0));

        if (distanciaTotal < menorDistancia) {
            menorDistancia = distanciaTotal;
            mejorRuta = new ArrayList<>(ruta);
        }
    }

    public ArrayList<Integer> getMejorRuta() {
        return mejorRuta;
    }

    public double getMenorDistancia() {
        return menorDistancia;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }
}

