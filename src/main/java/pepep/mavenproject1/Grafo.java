/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pepep.mavenproject1;
import java.util.ArrayList;
/**
 *
 * @author ricky
 */
public class Grafo {
    private ArrayList<Ciudad> ciudades;
    private double[][] matrizDistancias;

    public Grafo(ArrayList<Ciudad> ciudades) {
        this.ciudades = ciudades;
        this.matrizDistancias = new double[ciudades.size()][ciudades.size()];
        calcularMatrizDistancias();
    }

    private void calcularMatrizDistancias() {
        for (int i = 0; i < ciudades.size(); i++) {
            for (int j = 0; j < ciudades.size(); j++) {
                matrizDistancias[i][j] = ciudades.get(i).calcularDistancia(ciudades.get(j));
            }
        }
    }

    public ArrayList<Ciudad> getCiudades() {
        return ciudades;
    }

    public double getDistancia(int i, int j) {
        return matrizDistancias[i][j];
    }

}
