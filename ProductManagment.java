import java.io.*;
import java.util.*;
public class ProductManagement {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();

        // Load the file from resources
        String fileName = "amazon-product-data.csv";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                ProductManagement.class.getClassLoader().getResourceAsStream(fileName)))) {

            if (br == null) {
                throw new FileNotFoundException("Resource file '" + fileName + "' not found.");
            }

            String line;
            boolean isFirstLine = true; // Flag to skip the header
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the first line (header)
                    continue;
                }

                // Split by commas, but account for quoted fields
                String[] values = parseCSVLine(line);

                // Ensure the row has at least 4 fields
                if (values.length < 4) {
                    System.err.println("Skipping malformed row: " + Arrays.toString(values));
                    continue;
                }

                String productId = values[0].trim();
                String name = values[1].trim();
                String category = values[2].trim();
                String price;

                // New parser logic to handle invalid price values and "$" symbol
                try {
                    String priceString = values[3].trim(); // Extract the price field

                    // Remove $ symbol if present
                    if (priceString.startsWith("$")) {
                        priceString = priceString.substring(1); // Strip the $ symbol
                    }

                    priceString = priceString.replace(",", ""); // Remove commas for thousand separators

                    // Directly use the price string without checking for invalid format
                    Product product = new Product(productId, name, category, "$" + priceString);
                    tree.insert(product);

                } catch (Exception e) {
                    System.err.println("Error processing product: " + Arrays.toString(values));
                    e.printStackTrace();
                }
            }

            System.out.println("Products loaded successfully!");

        } catch (FileNotFoundException e) {
            System.err.println("Error: The resource file '" + fileName + "' was not found.");
            return; // Exit the program
        } catch (IOException e) {
            System.err.println("Error reading the resource file: " + fileName);
            e.printStackTrace();
            return; // Exit the program
        }

        // Main interaction loop
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Insert a new product");
            System.out.println("2. Search for a product");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left by nextInt()

            if (choice == 1) {
                // Insert a new product
                System.out.print("Enter Product ID: ");
                String productId = scanner.nextLine();
                System.out.print("Enter Product Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Product Category: ");
                String category = scanner.nextLine();
                System.out.print("Enter Product Price (e.g., $49.00): ");
                String price = scanner.nextLine();

                // Create a new Product
                Product newProduct = new Product(productId, name, category, price);

                try {
                    tree.insert(newProduct); // Insert into Red-Black Tree
                    System.out.println("Product inserted successfully!");
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage()); // Notify about duplicate insertions
                }
            } else if (choice == 2) {
                // Search for a product
                System.out.print("Enter product ID to search: ");
                String productId = scanner.nextLine();
                Product result = tree.search(productId);
                if (result != null) {
                    System.out.println(result);
                } else {
                    System.out.println("Product not found.");
                }
            } else if (choice == 3) {
                // Exit the program
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please choose again.");
            }
        }

        scanner.close();
    }

    // A utility function to handle quoted CSV fields
    public static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"' && (currentField.length() == 0 || currentField.charAt(currentField.length() - 1) != '\\')) {
                inQuotes = !inQuotes; // Toggle inQuotes state
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField = new StringBuilder(); // Reset for next field
            } else {
                currentField.append(c);
            }
        }

        // Add the last field
        if (currentField.length() > 0) {
            result.add(currentField.toString().trim());
        }

        return result.toArray(new String[0]);
    }
}
