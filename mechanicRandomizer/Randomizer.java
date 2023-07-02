import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.ArrayList;
import java.lang.Math;
import java.util.Scanner; // Import the Scanner class to read text files

public class Randomizer {
    private static ArrayList<String> categories = new ArrayList<String>();
    private static ArrayList<ArrayList<String>> allMechanics = new ArrayList<ArrayList<String>>();

    public static void main(String args[]) {
        create();
        combine();
    }

    private static void create() {
        Scanner reader = null;
        String data, category;
        ArrayList<String> tempMechanics = new ArrayList<String>();
        try {
            File mechanics = new File("mechanics.txt");
            reader = new Scanner(mechanics);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        while (reader.hasNextLine()) {
            data = reader.nextLine();
            // check if at a category line
            if (data.length() > 7 && data.substring(0, 8).equalsIgnoreCase("category")) {
                // add categories to categories list
                category = data.substring(10, data.length());
                categories.add(category);
                // starting a new category means the current category is complete
                ArrayList<String> copiedMechanics = new ArrayList<String>(tempMechanics);
                allMechanics.add(copiedMechanics);
                // System.out.println(copiedMechanics.toString());
                tempMechanics.clear();
            } else {
                // if data isn't a category then its a data line
                if (data.length() > 0) {
                    tempMechanics.add(data);
                }
            }
        }
        ArrayList<String> copiedMechanics = new ArrayList<String>(tempMechanics);
        allMechanics.add(copiedMechanics);
        // System.out.println(copiedMechanics.toString());
        tempMechanics.clear();
        // remove empty list at index 0
        allMechanics.remove(0);
        // System.out.println(allMechanics.toString());
        // close scanner
        reader.close();
    }

    private static void combine() {
        int rand;
        for (ArrayList<String> a : allMechanics) {
            rand = (int) (Math.random() * a.size());
            System.out.print(a.get(rand) + " ");
        }
    }
}