/*
* Matthew Hrydil - mbh38
* CS1501, John Ramirez - Assignment 1
*/

import java.io.*;
import java.util.*;

public class Assig1
{
    private static TrieSTNew<String> dict;
    private static ArrayList<String> input;

    /*  solutions key is the number of words in the solution, the TreeSet contains all of the solutions.
        Using the TreeSet ensures that the solutions are sorted, so when it comes time to print the output to a file, the strings will be in sorted order.
        It also ensures that there are no duplicates in the final output.
     */
    private static HashMap<Integer, TreeSet<String>> solutions = new HashMap<>();
    private static PrintWriter output;

    public static void main(String[] args) throws Exception
    {
        if(args.length !=2) throw new IllegalArgumentException("Please enter an input and output file name.");

        double startTime = System.currentTimeMillis();
        dict = new TrieSTNew<>();
        BufferedReader dictionary = new BufferedReader(new FileReader("dictionary.txt"));
        BufferedReader inputFile = new BufferedReader(new FileReader(args[0]));
        output = new PrintWriter(new FileWriter(args[1]));
        input = new ArrayList<>();


        //fillTrie() initializes the Trie with all of the words from the dictionary
        fillTrie(dictionary, dict);
        /*
        fillInput initializes the ArrayList of input strings
         */
        fillInput(inputFile, input);
        for(String inputWord : input){
            findSolutions(inputWord);
        }
        output.close();

        double elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Total Time: " + elapsed/1000 + " seconds."); //prints the total runtime of the program on the command line (not in the output file)
    }

    private static void findSolutions(String key){
        find(new ArrayList<>(), new StringBuilder(spacesRemoved(key)));
        printSolutions(key);
        solutions.clear();
    }

    //find is the primary recursive method that finds each of the solutions for the anagrams.
    private static void find(ArrayList<StringBuilder> wordsInProgress, StringBuilder charsLeft){
        /*
            Base Case - if there are no characters left in the charsLeft StringBuilder, we have successfully used all the letters in the input string.
            Adds the solution to the HashMap of solutions if the final word in the string is a valid word
         */
        if(charsLeft.length() == 0){
            String lastWord = new String(wordsInProgress.get(wordsInProgress.size()-1));
            /*
            if the last word in the current string is a valid word, it is a solution, so add it to the hashmap of solutions
             */
            if(dict.searchPrefix(lastWord) >= 2) {
                StringBuilder sol = new StringBuilder();
                for (int i = 0; i < wordsInProgress.size(); i++) {
                    sol.append(wordsInProgress.get(i));
                    sol.append(" ");
                }
                if (solutions.get(wordsInProgress.size()) == null) { //add a new key in the hashmap for the number of words in the current solution
                    solutions.put(wordsInProgress.size(), new TreeSet<>());
                }
                solutions.get(wordsInProgress.size()).add(new String(sol));
            }
        }

        /*
            If there are still characters left, we go through characters, recursively adding them to last word in wordsInProgress
         */
        else{
            /*
                If this is the first time through the recursive algorithm, we loop through the characters of the input string, adding a new
                string builder for each one, recursing, then removing it and continuing to the next one
             */
            if(wordsInProgress.size() == 0){
                for(int currIndex = 0; currIndex < charsLeft.length(); currIndex++){
                    char currChar = charsLeft.charAt(currIndex);
                    StringBuilder currWord = new StringBuilder();
                    currWord.append(currChar);
                    charsLeft.deleteCharAt(currIndex);
                    wordsInProgress.add(currWord);
                    find(wordsInProgress, charsLeft);
                    charsLeft.insert(currIndex, currChar);
                    wordsInProgress.remove(wordsInProgress.size()-1);
                }
            }

            else{
                for(int currIndex = 0; currIndex<charsLeft.length(); currIndex++){
                    char currChar = charsLeft.charAt(currIndex);
                    StringBuilder currWord = wordsInProgress.get(wordsInProgress.size()-1);

                    /*
                    Before doing anything, first check to see if the currChar word is a complete word, if so, start a new word
                    and recurse with the remaining letters.
                     */
                    if (dict.searchPrefix(new String(currWord)) >= 2){
                        StringBuilder newWord = new StringBuilder();
                        newWord.append(currChar);
                        charsLeft.deleteCharAt(currIndex);
                        wordsInProgress.add(newWord);
                        find(wordsInProgress, charsLeft);
                        charsLeft.insert(currIndex, currChar);
                        wordsInProgress.remove(wordsInProgress.size()-1);

                    }
                    /*
                    The following attempts are made trying to add the current character to the current word
                     */
                    currWord.append(currChar);
                    charsLeft.deleteCharAt(currIndex);
                    String searchString = new String(currWord);

                    /*
                        the currChar string is a prefix, or a word/prefix, recurse trying to add to the same word
                     */
                    if(dict.searchPrefix(searchString) == 1 || dict.searchPrefix(searchString) == 3){
                        find(wordsInProgress, charsLeft);
                    }

                    /*
                        the currChar string is a word or word/prefix, recurse
                     */
                    if(dict.searchPrefix(searchString) == 2 || dict.searchPrefix(searchString) == 3){
                        if(charsLeft.length() == 0) {
                            find(wordsInProgress, charsLeft);
                        }

                        for(int i=0; i<charsLeft.length(); i++){
                            char newChar = charsLeft.charAt(i);
                            StringBuilder newWord = new StringBuilder();
                            newWord.append(newChar);
                            charsLeft.deleteCharAt(i);
                            wordsInProgress.add(newWord);
                            find(wordsInProgress, charsLeft);
                            charsLeft.insert(i, newChar);
                            wordsInProgress.remove(wordsInProgress.size()-1);
                        }
                    }

                    /*
                        Remove the character and backtrack.
                     */
                    charsLeft.insert(currIndex, currChar);
                    currWord.deleteCharAt(currWord.length()-1);
                }
            }

        }
    }

    private static void printSolutions(String key){
        output.println("Here are the results for '" + key + "':");
        int totalSol = 0;
        for(Integer set : solutions.keySet()) {
            TreeSet<String> curr = solutions.get(set);
            totalSol += curr.size();
            output.println("There were " + curr.size() + " " + set + "-word solutions:");
            for (String string : curr) {
                output.println(string);
            }
        }
        output.println("There were a total of " + totalSol + " solutions.");
        output.println();
    }

    private static void fillTrie(BufferedReader words, TrieSTNew<String> trie) throws Exception
    {
        while(words.ready()){
            String currWord = words.readLine();
            trie.put(currWord, currWord);
        }
    }

    private static void fillInput(BufferedReader inputFile, ArrayList<String> arrayList) throws Exception
    {
        while(inputFile.ready()){
            String currWord = inputFile.readLine();
            arrayList.add(currWord);
        }
    }

    private static String spacesRemoved(String fullPhrase){
        StringBuilder phrase = new StringBuilder(fullPhrase);
        for(int i=0; i<phrase.length(); i++){
            if (phrase.charAt(i) == ' ') {
                phrase.deleteCharAt(i);
                i--; //if we removed a character we need to stay at that current character in case there are multiple spaces in a row
            }
        }
        return new String(phrase);
    }
}
