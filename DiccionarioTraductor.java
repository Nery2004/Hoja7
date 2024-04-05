import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class DiccionarioTraductor {

    public static void main(String[] args) {
        // Construir los árboles BST
        TreeNode<String, String> arbolIngles = new TreeNode<>(null, null);
        TreeNode<String, String> arbolEspanol = new TreeNode<>(null, null);
        TreeNode<String, String> arbolFrances = new TreeNode<>(null, null);

        try {
            // Leer el archivo diccionario.txt para construir los árboles BST
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

            // Realizar un recorrido In-order en el árbol según lo solicitado por el usuario
            System.out.println("Recorrido In-order en el árbol inglés:");
            recorridoInOrder(arbolIngles);
            System.out.println("\nRecorrido In-order en el árbol español:");
            recorridoInOrder(arbolEspanol);
            System.out.println("\nRecorrido In-order en el árbol francés:");
            recorridoInOrder(arbolFrances);

            // Procesar el archivo texto.txt para traducir las palabras
            System.out.println("\nTraducción del archivo texto.txt:");
            traducirTexto("texto.txt", arbolIngles, arbolEspanol, arbolFrances);

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Método para insertar una palabra en el árbol BST
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
            // La palabra ya existe, se puede manejar según lo necesites
            // En este caso, se puede ignorar o actualizar el valor
            raiz.set_value(valor);
        }

        return raiz;
    }

    // Recorrido In-order para imprimir las palabras ordenadas
    private static void recorridoInOrder(TreeNode<String, String> raiz) {
        if (raiz != null) {
            recorridoInOrder(raiz.get_left());
            System.out.println("(" + raiz.get_priority() + ", " + raiz.get_value() + ")");
            recorridoInOrder(raiz.get_right());
        }
    }

    // Método para traducir el texto del archivo
    private static void traducirTexto(String nombreArchivo, TreeNode<String, String> arbolIngles,
                                      TreeNode<String, String> arbolEspanol, TreeNode<String, String> arbolFrances) {
        try {
            BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (String palabra : palabras) {
                    String palabraTraducida = traducirPalabra(palabra, arbolIngles, arbolEspanol, arbolFrances);
                    System.out.print(palabraTraducida + " ");
                }
                System.out.println();
            }
            lector.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Método para traducir una palabra según el árbol BST correspondiente
    private static String traducirPalabra(String palabra, TreeNode<String, String> arbolIngles,
                                          TreeNode<String, String> arbolEspanol, TreeNode<String, String> arbolFrances) {
        String palabraMinuscula = palabra.toLowerCase(Locale.ROOT);
        TreeNode<String, String> nodoTraduccion = buscarEnABB(arbolIngles, palabraMinuscula);
        if (nodoTraduccion != null) {
            return nodoTraduccion.get_value();
        } else {
            nodoTraduccion = buscarEnABB(arbolEspanol, palabraMinuscula);
            if (nodoTraduccion != null) {
                return nodoTraduccion.get_value();
            } else {
                nodoTraduccion = buscarEnABB(arbolFrances, palabraMinuscula);
                if (nodoTraduccion != null) {
                    return nodoTraduccion.get_value();
                } else {
                    // Si la palabra no está en el diccionario, se imprime entre asteriscos
                    return "*" + palabra + "*";
                }
            }
        }
    }

    // Método de búsqueda en un árbol BST
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
}
