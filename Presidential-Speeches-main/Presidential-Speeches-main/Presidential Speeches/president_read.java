
/**
 * Write a description of class president_read here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class president_read
{
    
    /**
     * Extracts the String value associated with a JSON key.
     * Example: extractJsonValue(jsonString, "president") returns "George Washington"
     */
    public static String extractJsonValue(String json, String key) {
        String keyTarget = "\"" + key + "\":";
        int keyIndex = json.indexOf(keyTarget);
        if (keyIndex == -1) {
            return "";
        }
    
        // Locate the first opening quote after the key
        int startQuote = json.indexOf("\"", keyIndex + keyTarget.length());
        if (startQuote == -1) {
            return "";
        }
    
        // Locate the matching closing quote
        int endQuote = json.indexOf("\"", startQuote + 1);
        
        // Handle escaped quotes inside the speech text (e.g., \")
        while (endQuote != -1 && json.charAt(endQuote - 1) == '\\') {
            endQuote = json.indexOf("\"", endQuote + 1);
        }
    
        if (endQuote == -1) {
            return "";
        }
    
        return json.substring(startQuote + 1, endQuote);
    }
    
    public static void main(String[] args) throws IOException{
        int line = 0, speechLength;
        int[] counters = new int[4];
        String[] targetWords = {"people", "government", "united", "our"};
        String rawJson,president,date,speechText;
        
        //it goes through the speeches and read them
        
        String[] files = {"George_Washington_1796-12-07T13_03_58-04_56_December_7,_1796__Eighth_Annual_Message_to_Congress", "Abraham_Lincoln_1861-03-04T13_03_58-04_56_March_4,_1861__First_Inaugural_Address","Franklin_Pierce_1853-03-04T13_03_58-04_56_March_4,_1853__Inaugural_Address","Grover_Cleveland_1887-02-16T15_28_00-05_00_February_16,_1887__Veto_of_Texas_Seed_Bill","Richard_M._Nixon_1969-11-03T13_00_00-05_00_November_3,_1969__Address_to_the_Nation_on_the_War_in_Vietnam","Barack_Obama_2014-11-20T01_00_00-05_00_November_20,_2014__Address_to_the_Nation_on_Immigration"};
        
        for(int j=0; j<files.length;j++){  
            counters = new int[4];
            System.out.println("\nFile #" + (j+1) + ": ");
            File SelectedFile = new File("speeches/" + files[j] + ".json");
            
            Scanner fileRead = new Scanner(SelectedFile);
            rawJson = "";
            
            while (fileRead.hasNextLine()) {
                rawJson += fileRead.nextLine() + " ";
            }   
        
            president = extractJsonValue(rawJson, "president");
            date = extractJsonValue(rawJson, "date");
            speechText = extractJsonValue(rawJson, "transcript");
            //System.out.println(speechText);
            
            speechText = speechText.replaceAll("[^a-zA-Z ]", "");
            fileRead.close();
            
            speechLength = speechText.length();
            
            String[] temp = new String[speechLength];
            System.out.println();
            int prev = 0, count = 0;
            
            // It counts how many times specific words appear in the speech
            for(int i =0; i < speechLength; i++){
                if(speechText.substring(i,i+1).equals(" ")){
                    temp[count] = speechText.substring(prev,i+1);
                    prev = i;
                    count++;
                }
            }
            // Make a list of valid words
            double average = 0;
            String[] Wordlist = new String[count];
            for(int i =0; i < temp.length; i++){
                if(temp[i] != null){
                    Wordlist[i] = temp[i];
                    average += Wordlist[i].length(); //Grab
                }
            }
            
            for(String word : Wordlist){
                for(int i=0; i< targetWords.length;i++){
                    //System.out.println(targetWords[i] + "\t\t" +   word.toLowerCase());
                    if(targetWords[i].equals(word.toLowerCase())){
                        counters[i]++;//THIS SHOULD WORK. FILE ISSUE.
                    }
                }
            }
            
            average /= Wordlist.length;
            
            
            System.out.println("\n" + president + "\n" +date +  "\nSpeech length: \t" + Wordlist.length +  " words.");
            System.out.printf("Average Word Length: %.2f Characters.\n\n",average);
            
            for(int i=0;i<targetWords.length;i++){
                System.out.println(targetWords[i] + " appeared " + counters[i] + " times.");
            }
            System.out.println("\n\n" + speechText);
        } //Main For loop
    }
}
