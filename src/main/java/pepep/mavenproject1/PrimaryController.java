package pepep.mavenproject1;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimaryController {
    @FXML
    private WebView webView; // WebView para mostrar el mapa interactivo
    @FXML
    private ComboBox<String> algorithmSelector; // Selector de algoritmos

    private WebEngine webEngine; // Motor de carga del WebView
    private ArrayList<Ciudad> ciudadesSeleccionadas = new ArrayList<>();
    private Grafo grafo;

    @FXML
    public void initialize() {
        try {
            // Inicializa el WebView cargando el archivo HTML
            webEngine = webView.getEngine();
            String path = getClass().getResource("/pepep/mavenproject1/map.html").toExternalForm();
            webEngine.load(path);
            System.out.println("Cargando mapa desde: " + path);
            webEngine.load(path);

            // Captura errores de carga o problemas de JavaScript
            webEngine.setOnError(event -> System.err.println("Error en WebView: " + event.getMessage()));
            webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null) {
                    System.err.println("Excepción en WebView: " + newValue.getMessage());
                }
            });

            // Configura el selector de algoritmos
            algorithmSelector.getItems().addAll(
                    "Fuerza Bruta",
                    "Búsqueda Local",
                    "Algoritmo Genético",
                    "Simulated Annealing"
            );
            algorithmSelector.setValue("Fuerza Bruta"); // Valor por defecto
        } catch (Exception e) {
            System.err.println("Error al inicializar el controlador: " + e.getMessage());
        }
    }

    @FXML
    public void calcularRuta() {
        try {
            // Obtén las ciudades seleccionadas desde el mapa interactivo
            String jsonCiudades = (String) webEngine.executeScript("getSelectedCities()");
            procesarCiudadesSeleccionadas(jsonCiudades);

            // Ejecuta el algoritmo seleccionado
            String algoritmoSeleccionado = algorithmSelector.getValue();
            ArrayList<Integer> mejorRuta = null;
            double mejorDistancia = 0;
            double tiempoEjecucion = 0;

            switch (algoritmoSeleccionado) {
                case "Fuerza Bruta":
                    AlgoritmoFuerzaBruta fuerzaBruta = new AlgoritmoFuerzaBruta(grafo);
                    fuerzaBruta.resolver();
                    mejorRuta = fuerzaBruta.getMejorRuta();
                    mejorDistancia = fuerzaBruta.getMenorDistancia();
                    tiempoEjecucion = fuerzaBruta.getTiempoEjecucion();
                    break;
                case "Búsqueda Local":
                    AlgoritmoBusquedaLocal busquedaLocal = new AlgoritmoBusquedaLocal(grafo);
                    busquedaLocal.resolver();
                    mejorRuta = busquedaLocal.getMejorRuta();
                    mejorDistancia = busquedaLocal.getMejorDistancia();
                    tiempoEjecucion = busquedaLocal.getTiempoEjecucion();
                    break;
                case "Algoritmo Genético":
                    AlgoritmoGenetico genetico = new AlgoritmoGenetico(grafo, 50, 100, 0.1);
                    genetico.resolver();
                    mejorRuta = genetico.getMejorRuta();
                    mejorDistancia = genetico.getMejorDistancia();
                    tiempoEjecucion = genetico.getTiempoEjecucion();
                    break;
                case "Simulated Annealing":
                    AlgoritmoSimulatedAnnealing annealing = new AlgoritmoSimulatedAnnealing(grafo);
                    annealing.resolver(10000, 0.99);
                    mejorRuta = annealing.getMejorRuta();
                    mejorDistancia = annealing.getMejorDistancia();
                    tiempoEjecucion = annealing.getTiempoEjecucion();
                    break;
                default:
                    throw new IllegalArgumentException("Algoritmo no reconocido: " + algoritmoSeleccionado);
            }

            // Mostrar los resultados en el mapa y en un cuadro de diálogo
            mostrarResultadosEnMapa(mejorRuta);
            mostrarResultadosEnDialogo(mejorRuta, mejorDistancia, tiempoEjecucion);
        } catch (Exception e) {
            mostrarError("Error al calcular la ruta: " + e.getMessage());
        }
    }

    private void procesarCiudadesSeleccionadas(String jsonCiudades) {
        try {
            // Procesar las ciudades seleccionadas desde el mapa (JSON)
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Double>>>() {}.getType();
            List<Map<String, Double>> coords = gson.fromJson(jsonCiudades, listType);

            ciudadesSeleccionadas.clear();
            int id = 0;
            for (Map<String, Double> coord : coords) {
                double lat = coord.get("lat");
                double lng = coord.get("lng");
                ciudadesSeleccionadas.add(new Ciudad(id++, "Ciudad " + id, lat, lng));
            }

            grafo = new Grafo(new ArrayList<>(ciudadesSeleccionadas));
        } catch (Exception e) {
            mostrarError("Error al procesar las ciudades seleccionadas: " + e.getMessage());
        }
    }

    private void mostrarResultadosEnMapa(ArrayList<Integer> mejorRuta) {
        try {
            // Convierte la mejor ruta a JSON y llama a la función dibujarRuta en el mapa
            ArrayList<Map<String, Double>> rutaCoords = new ArrayList<>();
            for (int index : mejorRuta) {
                Ciudad ciudad = ciudadesSeleccionadas.get(index);
                Map<String, Double> coord = new HashMap<>();
                coord.put("lat", ciudad.getLat());
                coord.put("lng", ciudad.getLng());
                rutaCoords.add(coord);
            }

            String rutaJson = new Gson().toJson(rutaCoords);
            webEngine.executeScript("dibujarRuta(" + rutaJson + ")");
        } catch (Exception e) {
            mostrarError("Error al mostrar la ruta en el mapa: " + e.getMessage());
        }
    }

    private void mostrarResultadosEnDialogo(ArrayList<Integer> mejorRuta, double mejorDistancia, double tiempoEjecucion) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resultados del Algoritmo");
        alert.setHeaderText("Resultados:");
        alert.setContentText(String.format("Mejor ruta: %s\nDistancia mínima: %.2f\nTiempo de ejecución: %.2f segundos",
                mejorRuta, mejorDistancia, tiempoEjecucion));
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

