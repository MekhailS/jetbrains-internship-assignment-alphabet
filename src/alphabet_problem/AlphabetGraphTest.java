package alphabet_problem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class AlphabetGraphTest
{
    /**
     * check if given alphabet contains all characters and only them
     * @param alphabet
     * @return true if alphabet is complete false otherwise
     */
    static boolean isCompleteAlphabet(String alphabet)
    {
        int alphabetReqLen = 'z' - 'a' + 1;
        if (alphabetReqLen != alphabet.length())
            return false;

        char[] characters = alphabet.toCharArray();
        Arrays.sort(characters);
        for (int i = 0; i < alphabetReqLen; i++)
        {
            if ((char) ('a' + i) != characters[i])
                return false;
        }

        return true;
    }

    /**
     * check if given alphabet corresponds to given character order
     * @param alphabet
     * @param charOrderArr - array of strings representing order of characters
     * @return true if alphabet corresponds to character order, false otherwise
     */
    static boolean checkCharOrderInAlphabet(String alphabet, String[] charOrderArr)
    {
        for (String charOrder : charOrderArr)
        {
            for (int i = 1; i < charOrder.length(); i++)
            {
                if (alphabet.indexOf(charOrder.charAt(i - 1)) > alphabet.indexOf(charOrder.charAt(i)))
                    return false;
            }
        }
        return true;
    }

    void testSortedAlphabet(String[] names, String[] charOrderArr)
    {
        AlphabetGraph alphabetGraph = new AlphabetGraph();
        alphabetGraph.constructEdgesBySortedStringArr(names);

        String alphabet = alphabetGraph.getTopologicallySortedAlphabet();

        Assertions.assertTrue(isCompleteAlphabet(alphabet));
        Assertions.assertTrue(checkCharOrderInAlphabet(alphabet, charOrderArr));
    }

    void testAlphabetIsNull(String[] names)
    {
        AlphabetGraph alphabetGraph = new AlphabetGraph();
        alphabetGraph.constructEdgesBySortedStringArr(names);

        String alphabet = alphabetGraph.getTopologicallySortedAlphabet();

        Assertions.assertNull(alphabet);
    }

    @Test
    void getTopologicallySortedAlphabet_BasicAlphabet1stCharDistinct_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "bdf",
                "coh",
                "dij",
                "ekl"
        };
        // a > b > c > d > e
        String[] charOrderArr = {
                "abcde"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_2ndCharDistinctCase1_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "adf",
                "coh",
                "dij",
                "ekl"
        };
        // b > d
        // a > c > d > e
        String[] charOrderArr = {
                "bd",
                "acde"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_2ndCharDistinctCase2_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "adf",
                "coh",
                "dij",
                "dkl"
        };
        // b > d
        // a > c > d
        // i > k
        String[] charOrderArr = {
                "bd",
                "acd",
                "ik"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_2ndCharDistinctCase3_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "adf",
                "coh",
                "cbj",
                "ckl"
        };
        // b > d
        // a > c
        // o > b > k
        String[] charOrderArr = {
                "bd",
                "ac",
                "obk"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_2ndCharDistinctCase4_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "adf",
                "coh",
                "cbj",
                "cdl"
        };
        // b > d
        // a > c
        // o > b
        String[] charOrderArr = {
                "bd",
                "ac",
                "ob"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_3rdCharDistinctCase1_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "abf",
                "foh",
                "faj",
                "udl"
        };
        // c > f
        // a > f > u
        // o > a
        String[] charOrderArr = {
                "cf",
                "afu",
                "oa"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_3rdCharDistinctCase2_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "abf",
                "foh",
                "foj",
                "fok"
        };
        // c > f
        // a > f
        // h > j > k
        String[] charOrderArr = {
                "cf",
                "af",
                "hjk"
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_3rdCharDistinctCase4_CorrectAlphabet()
    {
        String[] names = {
                "abc",
                "abf",
                "aoh",
                "apj",
                "apl"
        };
        // c > f
        // b > o > p
        // j > l
        String[] charOrderArr = {
                "cf",
                "bop",
                "jl",
        };
        testSortedAlphabet(names, charOrderArr);
    }

    @Test
    void getTopologicallySortedAlphabet_NotValidOrderCase1_NullAlphabet()
    {
        String[] names = {
                "abc",
                "abf",
                "aoh",
                "afj",
                "acl"
        };
        // c > f
        // b > o > f > c
        // => f > c > f !!!
        testAlphabetIsNull(names);
    }

    @Test
    void getTopologicallySortedAlphabet_NotValidOrderCase2_NullAlphabet()
    {
        String[] names = {
                "abc",
                "acc",
                "acb",
                "afj",
                "arl"
        };
        // b > c
        // c > b
        // => c > f > r !!!
        testAlphabetIsNull(names);
    }

    @Test
    void getTopologicallySortedAlphabet_NotValidOrderCase3_NullAlphabet()
    {
        String[] names = {
                "abk",
                "abc",
                "acb",
                "afj",
                "akl"
        };
        // k > c
        // b > c
        // c > f > k
        // => c > k > c !!!
        testAlphabetIsNull(names);
    }

    @Test
    void getTopologicallySortedAlphabet_NotValidOrderCase4_NullAlphabet()
    {
        String[] names = {
                "abk",
                "bbc",
                "bdb",
                "bfj",
                "bal"
        };
        // a > b
        // b > d > f > a
        // => b > a > b !!!
        testAlphabetIsNull(names);
    }
}