import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class DiccionarioTraductor {

    private static TreeNode<String, String> arbolIngles = new TreeNode<>(null, null);
    private static TreeNode<String, String> arbolEspanol = new TreeNode<>(null, null);
    private static TreeNode<String, String> arbolFrances = new TreeNode<>(null, null);

    public static void main(String[] args) {
        cargarDiccionario();

        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\nSeleccione el idioma al que desea traducir:");
            System.out.println("1. Inglés");
            System.out.println("2. Español");
            System.out.println("3. Francés");
            System.out.println("4. Traducción automática");
            System.out.println("5. Salir");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\nRecorrido In-order en el árbol inglés:");
                    recorridoInOrder(arbolIngles);
                    break;
                case 2:
                    System.out.println("\nRecorrido In-order en el árbol español:");
                    recorridoInOrder(arbolEspanol);
                    break;
                case 3:
                    System.out.println("\nRecorrido In-order en el árbol francés:");
                    recorridoInOrder(arbolFrances);
                    break;
                case 4:
                    scanner.nextLine(); // Consumir el salto de línea
                    System.out.println("\nIngrese el texto a traducir:");
                    String texto = scanner.nextLine();
                    traduccionAutomatica(texto);
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
            }

        } while (opcion != 5);

        scanner.close();
    }

    private static void cargarDiccionario() {
        try {
            BufferedReader lector = new BufferedReader(new FileReader("diccionario.txt"));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3 && !partes[0].trim().equalsIgnoreCase("null")) {
                    arbolIngles = insertarEnABB(arbolIngles, partes[0].trim(), partes[1].trim());
                    arbolEspanol = insertarEnABB(arbolEspanol, partes[1].trim(), partes[0].trim());
                    arbolFrances = insertarEnABB(arbolFrances, partes[2].trim(), partes[0].trim());
                }
            }
            lector.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static TreeNode<String, String> insertarEnABB(TreeNode<String, String> raiz, String clave, String valor) {
        if (raiz == null) {
            return new TreeNode<>(clave, valor);
        }

        String claveRaiz = raiz.get_priority() != null ? raiz.get_priority() : "";
        String claveInsertar = clave != null ? clave : "";

        int comparacion = claveInsertar.compareToIgnoreCase(claveRaiz);
        if (comparacion < 0) {
            raiz.set_left(insertarEnABB(raiz.get_left(), clave, valor));
        } else if (comparacion > 0) {
            raiz.set_right(insertarEnABB(raiz.get_right(), clave, valor));
        } else {
            raiz.set_value(valor);
        }

        return raiz;
    }

    private static void recorridoInOrder(TreeNode<String, String> raiz) {
        if (raiz != null) {
            recorridoInOrder(raiz.get_left());
            if (raiz.get_priority() != null) {
                System.out.println("(" + raiz.get_priority() + ", " + raiz.get_value() + ")");
            }
            recorridoInOrder(raiz.get_right());
        }
    }

    private static TreeNode<String, String> buscarEnABB(TreeNode<String, String> raiz, String clave) {
        if (raiz == null || raiz.get_priority() == null) {
            return null;
        }

        int comparacion = clave.compareToIgnoreCase(raiz.get_priority());
        if (comparacion < 0) {
            return buscarEnABB(raiz.get_left(), clave);
        } else if (comparacion > 0) {
            return buscarEnABB(raiz.get_right(), clave);
        } else {
            return raiz;
        }
    }

    private static void traduccionAutomatica(String texto) {
        String[] palabras = texto.split("\\s+");
        for (String palabra : palabras) {
            String idioma = detectarIdioma(palabra.toLowerCase(Locale.ROOT));
            String palabraTraducida;
            switch (idioma) {
                case "ingles":
                    palabraTraducida = traducirPalabra(palabra.toLowerCase(Locale.ROOT), arbolIngles, arbolEspanol, arbolFrances);
                    break;
                case "espanol":
                    palabraTraducida = traducirPalabra(palabra.toLowerCase(Locale.ROOT), arbolEspanol, arbolIngles, arbolFrances);
                    break;
                case "frances":
                    palabraTraducida = traducirPalabra(palabra.toLowerCase(Locale.ROOT), arbolFrances, arbolIngles, arbolEspanol);
                    break;
                default:
                    palabraTraducida = "*" + palabra + "*"; // Palabra no encontrada en el diccionario
            }
            System.out.print(palabraTraducida + " ");
        }
        System.out.println();
    }

    private static String detectarIdioma(String palabra) {
        String[] palabrasIngles = { "the", "woman", "asked", "me", "to", "do", "my", "homework", "about", "town" };
        String[] palabrasEspanol = { "mujer", "preguntó", "me", "hacer", "mi", "tarea", "sobre", "pueblo" };
        String[] palabrasFrances = { "femme", "demandé", "moi", "faire", "mon", "devoirs", "sur", "ville" };

        if (containsIgnoreCase(palabrasIngles, palabra)) {
            return "ingles";
        } else if (containsIgnoreCase(palabrasEspanol, palabra)) {
            return "espanol";
        } else if (containsIgnoreCase(palabrasFrances, palabra)) {
            return "frances";
        } else {
            return "desconocido";
        }
    }

    private static boolean containsIgnoreCase(String[] palabras, String palabra) {
        for (String p : palabras) {
            if (p.equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }

    private static String traducirPalabra(String palabra, TreeNode<String, String> arbolOrigen,
            TreeNode<String, String> arbolDestino1, TreeNode<String, String> arbolDestino2) {
        TreeNode<String, String> nodoTraduccion = buscarEnABB(arbolOrigen, palabra);
        if (nodoTraduccion != null) {
            String traduccion = nodoTraduccion.get_value();
            TreeNode<String, String> nodoDestino1 = buscarEnABB(arbolDestino1, traduccion);
            TreeNode<String, String> nodoDestino2 = buscarEnABB(arbolDestino2, traduccion);
            if (nodoDestino1 != null) {
                return "*" + traduccion + "*";
            } else if (nodoDestino2 != null) {
                return traduccion;
            } else {
                return "*" + traduccion + "*";
            }
        } else {
            return "*" + palabra + "*"; // Palabra no encontrada en el diccionario
        }
    }
}
