import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Dictionary {
    public static void main(String[] args) {
        // Try-catch block for the thrown exception from several used methods.
        try {
            // Declare a Dictionary object.
            Dictionary D = null;
            // Display the main menu of methods.
            System.out.println("""
                    1. Make a Dictionary.
                    2. Add Word.
                    3. Find Word.
                    4. Find Similar Words.
                    5. Delete Word.
                    6. Export/Update to a File.
                    To Exit, Enter 'Q' or Press 'ENTER' Until The Arrow Disappear
                    """);
            // Display a cursor indicator position.
            System.out.print("\t->");
            Scanner input = new Scanner(System.in);
            // Choice1 is to hold the user input for the main menu.
            String choice1 = input.nextLine().toLowerCase();
            while (!choice1.equals("q") & !choice1.equals("")) {
                // Switch block to invoke the corresponding function of the user input.
                switch (choice1) {
                    case "1" -> {
                        // Display sub-menu for creation options.
                        System.out.println("""
                                \tA. Empty.
                                \tB. 1-word.
                                \tC. From File.
                                """);
                        System.out.print("\t\t->->");
                        // Choice2 is to hold the user input for the sub-menu.
                        String choice2 = input.nextLine().toUpperCase();
                        // A loop because all other function cannot be invoked unless the object dictionary is created.
                        while (!choice2.equals("Q") & !choice2.equals("")) {
                            // Switch block to invoke the corresponding constructor of the user input.
                            switch (choice2) {
                                case "A" -> {
                                    D = new Dictionary();
                                    choice2 = "Q"; // To leave this loop after successfully invoking the constructor.
                                }
                                case "B" -> {
                                    System.out.print("\t\t\tEnter the word: ");
                                    D = new Dictionary(input.nextLine());
                                    choice2 = "Q"; // To leave this loop after successfully invoking the constructor.
                                }
                                case "C" -> {
                                    System.out.print("\t\t\tEnter the file path: ");
                                    D = new Dictionary(new File(input.nextLine()));
                                    choice2 = "Q"; // To leave this loop after successfully invoking the constructor.
                                }
                                default -> {
                                    // If the user entered a wrong input. retake the input.
                                    System.out.println("\t\t\tPlease enter valid choice");
                                    choice2 = "";
                                }
                            }
                            if (choice2.equals("")) { // Stay in the loop.
                                System.out.print("\t\t->->");
                                choice2 = input.nextLine().toUpperCase();
                            }
                        }
                        choice1 = ""; // To stay in this loop after successfully invoking the method.
                    }
                    case "2" -> {
                        if (D == null) throw new NullPointerException("An Initialized Dictionary not found.");
                        // Take the input from the user, then call the chosen method.
                        System.out.print("\t\tEnter the word to add: ");
                        D.addWord(input.nextLine());
                        choice1 = ""; // To stay in this loop after successfully invoking the method.
                    }
                    case "3" -> {
                        if (D == null) throw new NullPointerException("An Initialized Dictionary not found.");
                        // Take the input from the user, then call the chosen method.
                        System.out.print("\t\tEnter the word to find: ");
                        D.findWord(input.nextLine());
                        choice1 = ""; // To stay in this loop after successfully invoking the method.
                    }
                    case "4" -> {
                        if (D == null) throw new NullPointerException("An Initialized Dictionary not found.");
                        // Take the input from the user, then call the chosen method.
                        System.out.print("\t\tEnter the word to find similar words: ");
                        System.out.println("\t\tThe similar words are: " +
                                Arrays.toString(D.findSimilar(input.nextLine())));
                        choice1 = ""; // To stay in this loop after successfully invoking the method.
                    }
                    case "5" -> {
                        if (D == null) throw new NullPointerException("An Initialized Dictionary not found.");
                        // Take the input from the user, then call the chosen method.
                        System.out.print("\t\tEnter the word to delete: ");
                        D.deleteWord(input.nextLine());
                        choice1 = ""; // To stay in this loop after successfully invoking the method.
                    }
                    case "6" -> {
                        if (D == null) throw new NullPointerException("An Initialized Dictionary not found.");
                        // Take the input from the user, then call the chosen method.
                        System.out.print("\t\tEnter the file name / Enter nothing to overwrite 'mydictionary': ");
                        D.updateFile(input.nextLine());
                        choice1 = ""; // To stay in this loop after successfully invoking the method.
                    }
                    default -> {
                        // If the user entered a wrong input. retake the input.
                        System.out.println("\tPlease enter valid choice");
                        System.out.print("\n\t->");
                    }
                }
                if (choice1.equals("")) // Stay in the loop.
                    System.out.print("\t->");
                choice1 = input.nextLine().toLowerCase();
            }
        } catch (Exception e) { // To handle all exceptions.
            System.out.println(e.getLocalizedMessage());
        }
    }

    private AVLTree<String> dict;

    public Dictionary(String str) {
        if (!(str == null || str.isBlank() || str.isEmpty())) { // To eliminate non valid inputs.
            dict = new AVLTree<>();
            dict.insertAVL(str.toLowerCase()); // Insert to the tree.
        } else
            throw new NullPointerException("Empty/Non-Valid String");
    }

    public Dictionary() {
        dict = new AVLTree<>();
    }

    public Dictionary(File wordsFile) throws IOException {
        // A BufferedReader to read the words from the file.
        try (BufferedReader inputR = new BufferedReader(new FileReader(wordsFile))) {
            dict = new AVLTree<>();
            long start = System.nanoTime(); // An auxiliary variable to calculate the elapsed time for reading the file.
            long count = 0L; // An auxiliary variable to count the words been inserted to the tree.
            while (inputR.ready()) {
                String str = inputR.readLine();
                if (!str.isBlank() || str.isEmpty()) { // To eliminate non valid inputs.
                    if (!dict.insertAVLNoDup(str.toLowerCase())) // Insert to the tree using non duplicates methods which
                        // is modified to return true if the word exist.
                        count++; // Count the words, if it is not duplicate.
                }
            }
            // Display a message to indicate that the method worked successfully and the time this method took to perform.
            System.out.printf("%.3f seconds to build the AVL-Tree from %s with %d words.\n", getExecutionTime(start)
                    , wordsFile.getName(), count);
        }
    }

    public void addWord(String str) throws WordAlreadyExistsException {
        if (!(str == null || str.isBlank() || str.isEmpty())) { // To eliminate non valid inputs.
            long start = System.nanoTime(); // An auxiliary variable to calculate the elapsed time to perform.
            if (dict.insertAVLNoDup(str.toLowerCase())) // The original method is modified to return true if the word exist.
                throw new WordAlreadyExistsException(str);
            // Display a message to indicate that the method worked successfully and the time this method took to perform.
            System.out.printf("The word '%s' is added successfully in %.3f seconds.\n", str, getExecutionTime(start));
        } else
            throw new NullPointerException("Empty/Non-Valid String");
    }

    public boolean findWord(String str) {
        long start = System.nanoTime(); // An auxiliary variable to calculate the elapsed time to perform.
        boolean result = dict.isInTree(str.toLowerCase()); // Using the super class method for searching.
        if (result)
            // Display a message to indicate that the method worked successfully and the time this method took to perform.
            System.out.printf("The word '%s' is found successfully in %.3f seconds.\n", str, getExecutionTime(start));
        else
            System.out.printf("The word '%s' is not found .\n", str);
        return result; // Return.
    }

    public void deleteWord(String str) throws WordNotFoundException {
        long start = System.nanoTime(); // An auxiliary variable to calculate the elapsed time to perform.
        if (!dict.deleteAVL(str.toLowerCase())) // Using the modified method that return false if the string is not found.
            throw new WordNotFoundException(str);
        // Display a message to indicate that the method worked successfully and the time this method took to perform.
        System.out.printf("The word '%s' is deleted successfully in %.3f seconds.\n", str, getExecutionTime(start));
    }

    public String[] findSimilar(String str) {
        str = str.toLowerCase(); // Change the letters to lower case because case sensitivity does not matter.
        int arraySize = 0; // A variable to indicate the actual size needed for the array.
        long start = System.nanoTime(); // An auxiliary variable to calculate the elapsed time to perform.
        BSTNode<String> node = dict.root; // Starting the comparison from the root node
        Queue<BSTNode<String>> nodes = new Queue<>(); // A queue to process all nodes of tree (left, right).
        Stack<String> simiWords = new Stack<>(); // A stack to collect the similar words.
        nodes.enqueue(node); // Enqueue the root to the queue.
        while (!nodes.isEmpty()) {
            node = nodes.dequeue();
            String simi = node.el.toLowerCase(); // Change the letters to lower case because case sensitivity does not matter.
            int diff = 0; // To count the number of different letters between the two words (accepting up to 1 difference).
            boolean checked = false; // A flag to indicate that current word is entered one of the comparison blocks.
            if ((str.length() - simi.length()) == 1) { // If the current word is 1 letter bigger than the given word.
                for (int i = 0; (i < (simi.length() + diff)) && (diff < 2); i++) { // Break when 'diff' >= 2.
                    // Compare each letter from the given word with the current word.
                    if (simi.charAt(i - diff) != str.charAt(i))
                        diff++; //
                }
                checked = true; //
            } else if ((str.length() - simi.length()) == 0) { // If the current word is equal in length to the given word.
                for (int i = 0; (i < simi.length()) && (diff < 2); i++) { // Break when 'diff' >= 2.
                    // Search for each letter from the given word, in the current word.
                    if (simi.charAt(i) != str.charAt(i))
                        diff++; //
                }
                checked = true; //
            } else if ((str.length() - simi.length()) == -1) { // If the current word is 1 letter smaller than the given word.
                for (int i = 0; (i < (str.length() + diff)) && (diff < 2); i++) { // Break when 'diff' >= 2.
                    // Search for each letter from the given word, in the current word.
                    if (simi.charAt(i) != str.charAt(i - diff))
                        diff++; //
                }
                checked = true; //
            }
            if (checked && (diff < 2) && (!simi.equals(str))) { // If the current word is checked, and it differs with
                // the given word in 1 letter.
                simiWords.push(simi); // Push the current word (similar) to the stack.
                arraySize++; // Increment the size needed for the array.
            }
            // Enqueue the following child nodes.
            if (node.left != null)
                nodes.enqueue(node.left);
            if (node.right != null)
                nodes.enqueue(node.right);
        }
        // Display a message to indicate that the method worked successfully and the time this method took to perform.
        System.out.printf("[%d] similar words of '%s' are found successfully in %.3f seconds.\n", arraySize, str, getExecutionTime(start));
        // initialize the array with the actual needed size.
        String[] similar = new String[arraySize];
        for (int i = 0; i < arraySize; i++) { // Build the array from the stack.
            similar[i] = simiWords.pop();
        }
        return similar;
    }

    private void toFile(File file) throws IOException {
        try (PrintWriter outFile = new PrintWriter(file)) { // Try with resources block.
            StringBuilder strB = new StringBuilder();
            // Using the recursive method strBuilderInOrder with a separation new line, it will build a string of all nodes in the tree.
            strBuilderInOrder(dict.root, strB, "\n");
            outFile.print(strB);
        }
    }

    public void updateFile(String name) throws IOException {
        // A binary condition to assign the given file for the project if the user did not specify a file name to update.
        name = (name.length() == 0) ? "src/mydictionary" : name;
        File file = new File(name + ".txt");
        if (file.exists() && (file.length() != 0)) { // If the file does not exist, or it is empty invoke toFile method.
            // To sort the file when updating, make a dictionary from the file then merge it with the current dictionary.
            // Dictionary oldDict = new Dictionary(file);
            BufferedReader inputR = new BufferedReader(new FileReader(file));
            while (inputR.ready()) {
                String str = inputR.readLine();
                if (!str.isBlank() || str.isEmpty()) // To eliminate non valid inputs.
                    dict.insertAVLNoDup(str.toLowerCase()); // Insert to the tree using non duplicates methods.
            }
            // dict.mergeTrees(oldDict.dict); // The new method 'merge' which merge two trees (In order).
            PrintWriter outFile = new PrintWriter(file); // Open the file to write, after reading all the data were in it.
            StringBuilder strB = new StringBuilder();
            // Using the recursive method strBuilderInOrder with a separation new line, it will build a string of all nodes in the tree.
            strBuilderInOrder(dict.root, strB, "\n");
            outFile.print(strB); // Write the new Dictionary strings to the file.
            outFile.close();
        } else
            toFile(file);
    }

    private void strBuilderInOrder(BSTNode<String> node, StringBuilder output, String sep) {
        // A recursive method to add the info of all the nodes from the tree to a StringBuilder object (inOrder).
        if (node != null) {
            strBuilderInOrder(node.left, output, sep);
            output.append(node.el).append(sep);
            strBuilderInOrder(node.right, output, sep);
        }
    }

    private double getExecutionTime(long startTime) {
        // An auxiliary method that calculate the elapsed time from a nano-time input and convert it to seconds.
        return ((System.nanoTime() - startTime) / 1e9);
    }

    private class WordAlreadyExistsException extends Exception {
        private String str; // A field to use it in formatting the message.

        private WordAlreadyExistsException(String str) {
            this.str = str;
        }

        // Override the super class method.
        @Override
        public String getLocalizedMessage() {
            // " \u26A0 " is an emoji.
            return (this.getClass().getName() + " \u26A0 " + "The word [" + str + "] already in the dictionary." + " \u26A0 ");
        }
    }

    private class WordNotFoundException extends Exception {
        private String str; // A field to use it in formatting the message.

        private WordNotFoundException(String str) {
            this.str = str;
        }

        // Override the super class method.
        @Override
        public String getLocalizedMessage() {
            // " \u26A0 " is an emoji.
            return (this.getClass().getName() + " \u26A0 " + "The word [" + str + "] not founded in the dictionary." + " \u26A0 ");
        }
    }
}