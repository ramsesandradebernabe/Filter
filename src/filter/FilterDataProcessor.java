package filter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterDataProcessor {

    public static void main(String[] args) {
        // Define rutas de entrada y salida
        String inputFile = System.getProperty("user.dir")+"/src/testData/usuarios.txt";
        String outputFile = System.getProperty("user.dir")+"/src/testData/usuarios_filtrados.txt";

        try {
            // Procesar datos
            List<String> filteredUsers = filterUsersByAge(inputFile, 25, 40);

            // Guardar resultados en un archivo
            saveToFile(filteredUsers, outputFile);

            System.out.println("Archivo procesado exitosamente. Resultados guardados en: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error procesando los archivos: " + e.getMessage());
        }
    }

    /**
     * Filtra usuarios por rango de edad.
     *
     * @param inputFile Ruta del archivo de entrada.
     * @param minAge    Edad mínima.
     * @param maxAge    Edad máxima.
     * @return Lista de usuarios filtrados.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static List<String> filterUsersByAge(String inputFile, int minAge, int maxAge) throws IOException {
        Path path = Paths.get(inputFile);

        try (Stream<String> lines = Files.lines(path).parallel()) {
            return lines
                .map(FilterDataProcessor::parseUser)
                .filter(user -> user != null && user.getAge() >= minAge && user.getAge() <= maxAge)
                .map(User::toString)
                .collect(Collectors.toList());
        }
    }

    /**
     * Guarda una lista de líneas en un archivo.
     *
     * @param lines     Lista de líneas a guardar.
     * @param outputFile Ruta del archivo de salida.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void saveToFile(List<String> lines, String outputFile) throws IOException {
        Path path = Paths.get(outputFile);
        Files.write(path, lines);
    }

    /**
     * Convierte una línea de texto en un objeto Usuario.
     *
     * @param line Línea de texto con formato "nombre,edad,email".
     * @return Objeto Usuario o null si el formato es inválido.
     */
    public static User parseUser(String line) {
        try {
            String[] parts = line.split(",");
            String name = parts[0].trim();
            int age = Integer.parseInt(parts[1].trim());
            String email = parts[2].trim();
            return new User(name, age, email);
        } catch (Exception e) {
            System.err.println("Línea inválida: " + line);
            return null;
        }
    }
}

/**
 * Clase Usuario que representa un registro.
 */
class User {
    private final String name;
    private final int age;
    private final String email;

    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return name + "," + age + "," + email;
    }
}
