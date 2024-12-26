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

public class AlgoritmoBusquedaLocal {
    private Grafo grafo;
    private ArrayList<Integer> mejorRuta;
    private double mejorDistancia;
    private double tiempoEjecucion; // Tiempo en segundos

    public AlgoritmoBusquedaLocal(Grafo grafo) {
        this.grafo = grafo;
        this.mejorRuta = new ArrayList<>();
        this.mejorDistancia = Double.MAX_VALUE;
    }

    public void resolver() {
        long inicio = System.nanoTime(); // Marca el inicio del tiempo

        // Generar una solución inicial (orden de las ciudades)
        mejorRuta = new ArrayList<>();
        for (int i = 0; i < grafo.getCiudades().size(); i++) {
            mejorRuta.add(i);
        }

        mejorDistancia = calcularDistanciaRuta(mejorRuta);

        boolean mejora = true;
        while (mejora) {
            mejora = false;
            for (int i = 1; i < mejorRuta.size() - 1; i++) {
                for (int j = i + 1; j < mejorRuta.size(); j++) {
                    Collections.swap(mejorRuta, i, j);
                    double nuevaDistancia = calcularDistanciaRuta(mejorRuta);

                    if (nuevaDistancia < mejorDistancia) {
                        mejorDistancia = nuevaDistancia;
                        mejora = true;
                    } else {
                        Collections.swap(mejorRuta, i, j);
                    }
                }
            }
        }

        long fin = System.nanoTime(); // Marca el fin del tiempo
        tiempoEjecucion = (fin - inicio) / 1_000_000_000.0; // Convertir a segundos
    }

    private double calcularDistanciaRuta(ArrayList<Integer> ruta) {
        double distanciaTotal = 0;

        for (int i = 0; i < ruta.size() - 1; i++) {
            distanciaTotal += grafo.getDistancia(ruta.get(i), ruta.get(i + 1));
        }

        distanciaTotal += grafo.getDistancia(ruta.get(ruta.size() - 1), ruta.get(0));
        return distanciaTotal;
    }

    public ArrayList<Integer> getMejorRuta() {
        return mejorRuta;
    }

    public double getMejorDistancia() {
        return mejorDistancia;
    }

    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }
}
