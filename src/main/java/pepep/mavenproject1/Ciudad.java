/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pepep.mavenproject1;

/**
 *
 * @author ricky
 */
public class Ciudad {
    private int id;
    private String nombre;
    private double lat; // Latitud
    private double lng; // Longitud

    public Ciudad(int id, String nombre, double lat, double lng) {
        this.id = id;
        this.nombre = nombre;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    // Calcula la distancia en línea recta entre dos ciudades
    public double calcularDistancia(Ciudad otra) {
        return Math.sqrt(Math.pow(this.lat - otra.lat, 2) + Math.pow(this.lng - otra.lng, 2));
    }

    @Override
    public String toString() {
        return nombre + " (" + lat + ", " + lng + ")";
    }
}
