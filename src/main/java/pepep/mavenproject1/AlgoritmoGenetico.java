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
import java.util.Random;

public class AlgoritmoGenetico {
    private Grafo grafo;
    private int tamanoPoblacion;
    private int generaciones;
    private double tasaMutacion;
    private ArrayList<ArrayList<Integer>> poblacion;
    private ArrayList<Integer> mejorRuta;
    private double mejorDistancia;
    private double tiempoEjecucion; // Tiempo en segundos

    public AlgoritmoGenetico(Grafo grafo, int tamanoPoblacion, int generaciones, double tasaMutacion) {
        this.grafo = grafo;
        this.tamanoPoblacion = tamanoPoblacion;
        this.generaciones = generaciones;
        this.tasaMutacion = tasaMutacion;
        this.poblacion = new ArrayList<>();
        this.mejorRuta = new ArrayList<>();
        this.mejorDistancia = Double.MAX_VALUE;
    }

    public void resolver() {
        long inicio = System.nanoTime(); // Marca el inicio del tiempo

        inicializarPoblacion();

        for (int gen = 0; gen < generaciones; gen++) {
            ArrayList<ArrayList<Integer>> nuevaPoblacion = new ArrayList<>();

            for (int i = 0; i < tamanoPoblacion; i++) {
                ArrayList<Integer> padre1 = seleccionarRuta();
                ArrayList<Integer> padre2 = seleccionarRuta();

                ArrayList<Integer> hijo = cruzarRutas(padre1, padre2);

                if (Math.random() < tasaMutacion) {
                    mutarRuta(hijo);
                }

                nuevaPoblacion.add(hijo);

                double distanciaHijo = calcularDistanciaRuta(hijo);
                if (distanciaHijo < mejorDistancia) {
                    mejorDistancia = distanciaHijo;
                    mejorRuta = hijo;
                }
            }

            poblacion = nuevaPoblacion;
        }

        long fin = System.nanoTime(); // Marca el fin del tiempo
        tiempoEjecucion = (fin - inicio) / 1_000_000_000.0; // Convertir a segundos
    }

    private void inicializarPoblacion() {
        for (int i = 0; i < tamanoPoblacion; i++) {
            ArrayList<Integer> ruta = new ArrayList<>();
            for (int j = 0; j < grafo.getCiudades().size(); j++) {
                ruta.add(j);
            }
            Collections.shuffle(ruta);
            poblacion.add(ruta);

            double distancia = calcularDistanciaRuta(ruta);
            if (distancia < mejorDistancia) {
                mejorDistancia = distancia;
                mejorRuta = ruta;
            }
        }
    }

    private ArrayList<Integer> seleccionarRuta() {
        Random random = new Random();
        ArrayList<Integer> ruta1 = poblacion.get(random.nextInt(tamanoPoblacion));
        ArrayList<Integer> ruta2 = poblacion.get(random.nextInt(tamanoPoblacion));
        return calcularDistanciaRuta(ruta1) < calcularDistanciaRuta(ruta2) ? ruta1 : ruta2;
    }

    private ArrayList<Integer> cruzarRutas(ArrayList<Integer> padre1, ArrayList<Integer> padre2) {
        Random random = new Random();
        int puntoCorte = random.nextInt(padre1.size());

        ArrayList<Integer> hijo = new ArrayList<>(padre1.subList(0, puntoCorte));
        for (int ciudad : padre2) {
            if (!hijo.contains(ciudad)) {
                hijo.add(ciudad);
            }
        }

        return hijo;
    }

    private void mutarRuta(ArrayList<Integer> ruta) {
        Random random = new Random();
        int i = random.nextInt(ruta.size());
        int j = random.nextInt(ruta.size());
        Collections.swap(ruta, i, j);
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

