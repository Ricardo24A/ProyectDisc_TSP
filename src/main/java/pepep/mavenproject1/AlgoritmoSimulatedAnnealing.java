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

public class AlgoritmoSimulatedAnnealing {
    private Grafo grafo;
    private ArrayList<Integer> mejorRuta;
    private double mejorDistancia;
    private double tiempoEjecucion; // Tiempo en segundos

    public AlgoritmoSimulatedAnnealing(Grafo grafo) {
        this.grafo = grafo;
        this.mejorRuta = new ArrayList<>();
        this.mejorDistancia = Double.MAX_VALUE;
    }

    public void resolver(double temperaturaInicial, double tasaEnfriamiento) {
        long inicio = System.nanoTime(); // Marca el inicio del tiempo

        ArrayList<Integer> rutaActual = new ArrayList<>();
        for (int i = 0; i < grafo.getCiudades().size(); i++) {
            rutaActual.add(i);
        }
        Collections.shuffle(rutaActual); // Genera una solución inicial aleatoria

        double distanciaActual = calcularDistanciaRuta(rutaActual);
        mejorRuta = new ArrayList<>(rutaActual);
        mejorDistancia = distanciaActual;

        double temperatura = temperaturaInicial;

        while (temperatura > 1) {
            // Generar una nueva solución intercambiando dos ciudades
            ArrayList<Integer> nuevaRuta = new ArrayList<>(rutaActual);
            int i = (int) (Math.random() * nuevaRuta.size());
            int j = (int) (Math.random() * nuevaRuta.size());
            Collections.swap(nuevaRuta, i, j);

            double nuevaDistancia = calcularDistanciaRuta(nuevaRuta);

            // Aceptar la nueva solución si es mejor o con cierta probabilidad
            if (nuevaDistancia < distanciaActual || Math.random() < Math.exp((distanciaActual - nuevaDistancia) / temperatura)) {
                rutaActual = nuevaRuta;
                distanciaActual = nuevaDistancia;
            }

            // Actualizar la mejor solución encontrada
            if (distanciaActual < mejorDistancia) {
                mejorRuta = new ArrayList<>(rutaActual);
                mejorDistancia = distanciaActual;
            }

            // Reducir la temperatura
            temperatura *= tasaEnfriamiento;
        }

        long fin = System.nanoTime(); // Marca el fin del tiempo
        tiempoEjecucion = (fin - inicio) / 1_000_000_000.0; // Convertir a segundos
    }

    private double calcularDistanciaRuta(ArrayList<Integer> ruta) {
        double distanciaTotal = 0;

        for (int i = 0; i < ruta.size() - 1; i++) {
            distanciaTotal += grafo.getDistancia(ruta.get(i), ruta.get(i + 1));
        }

        distanciaTotal += grafo.getDistancia(ruta.get(ruta.size() - 1), ruta.get(0)); // Regresar al inicio
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

