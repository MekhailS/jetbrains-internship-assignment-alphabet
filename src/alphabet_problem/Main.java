package alphabet_problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    static boolean PRINT_HINTS = true;

    /**
     * Read names from user input
     *
     * @param print_hints if true, print hints asking for input (example: "enter number of names total")
     * @return sorted array of names
     */
    private static String[] readNamesFromUser(boolean print_hints)
    {
        try
        {
            if (print_hints)
                System.out.println("enter number of names total");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int numNames = Integer.parseInt(br.readLine());
            String[] names = new String[numNames];

            if (print_hints)
                System.out.printf("enter %d names in format (name)%%(index) (index from 1 to %d)\n", numNames, numNames);

            for (int k = 0; k < numNames; k++)
            {
                String[] nameWithIndex = br.readLine().split("%");
                names[Integer.parseInt(nameWithIndex[1]) - 1] = nameWithIndex[0];
            }
            br.close();
            return names;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static void main(String[] args)
    {
        String[] names = readNamesFromUser(PRINT_HINTS);
        if (names == null)
            return;

        AlphabetGraph alphabetGraph = new AlphabetGraph();
        alphabetGraph.constructEdgesBySortedStringArr(names);

        String alphabet = alphabetGraph.getTopologicallySortedAlphabet();
        alphabet = alphabet == null ? "Impossible" : alphabet;

        System.out.println(alphabet);
    }
}
