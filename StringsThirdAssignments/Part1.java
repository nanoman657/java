import java.util.Arrays;
import java.lang.Iterable;
import edu.duke.StorageResource;
import edu.duke.FileResource;
import java.lang.Math;
/**
 * Write a description of Part1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Part1 {
    public static void main(){
        System.out.println("Base case test passed: " + (findStopCodon("ATGGACAGTCCTGTATGA", 0, "TGA") == 15));
        System.out.println("Wrong startIndex test passed: " + (findStopCodon("ATGGACAGTCCTGTATGA", 1, "TGA") == 18));
        System.out.println("No stopcodon test passed: " + (findStopCodon("ATGGACAGTCCTGTA", 1, "TGA") == 15));
        System.out.println("Second stop codon case test passed: " + (findStopCodon("ATGGACAGTCCTGTATAG", 0, "TAG") == 15));
        System.out.println("Sus");

        StorageResource sr_test1 = new StorageResource();
        StorageResource sr_test2 = new StorageResource();
        
        sr_test1.add("ATGGACAGTCCTTGA");
        sr_test1.add("ATGGACAGTCCTTGA");
        
        sr_test2.add("ATGCTAACTAGCTGA");
        System.out.println("A");
        System.out.println(getAllGenes("ATGGACAGTCCTTGATAGATGGACAGTCCTTGATAG").data().equals(sr_test1.data()));
        System.out.println(getAllGenes("AATGCTAACTAGCTGACTAAT").data().equals(sr_test2.data()));
        System.out.println(getAllGenes("ATGCTAACTAGCTGA").data().equals(sr_test2.data())); // Test case for when the entire string is a valid gene
        System.out.println(cgRatio("ATGCCATAG") == (float)4/9);
        testProcessGenes();
    }  
    
    public static void testProcessGenes() {
        StorageResource test_sr = new StorageResource();
        test_sr.add("CGCG"); // CG ratio is higher than 0.35
        test_sr.add("AATGCTAACTAGCTGACTAAT"); // Has gene longer than 9 characters
        test_sr.add("AATGAGCTGACTAAT"); // Has no genes longer than 9 characters
        //processGenes(test_sr);
        
        System.out.println("Next test");
        FileResource fr = new FileResource("GRch38dnapart.fa");
        String dna = fr.asString();
        StorageResource test_sr2  = getAllGenes(dna.toUpperCase());
        processGenes(test_sr2);
    }
    
    public static int countString(String search_string, String string) {
        int counter = 0;
        
        for (int i = 0; i + search_string.length() <= string.length(); i = i + search_string.length()) {
            String substring = string.substring(i, i + search_string.length());
            counter = substring.contains(search_string) ? counter + 1 : counter;
        }

        return counter;
    }
    
    public static int countCTG(String dna) {
        int counter = countString("CTG", dna);
        return counter;
    }
    
    public static float cgRatio(String string) {
        int c_counter = countString("C", string);
        int g_counter = countString("G", string);
        
        return (float)(c_counter + g_counter) / string.length();
    }
    
    public static String[] storageResourceToArr(StorageResource sr) {
        String[] arr = new String[sr.size()];
        int i = 0;

        for (String string : sr.data()) {
            arr[i] = string;
            i++;
        }

        return arr;
    }
    
    public static String getLongestGene(String dna) {
        StorageResource genes = getAllGenes(dna);
        String longest_gene = "";
        for (String gene : genes.data()) {
            longest_gene = gene.length() > longest_gene.length() ? gene : longest_gene;
        }
        
        return longest_gene;
    }
    
    public static void processGenes(StorageResource sr) {
        StorageResource long_strings = new StorageResource();
        StorageResource highCG = new StorageResource();
        int longest_gene_counter = 0;
        int longest_gene_length = 0;
        for (String string : sr.data()) {
            if (string.length() > 9){
                long_strings.add(string);
            }
            
            if (cgRatio(string) > 0.35){
                highCG.add(string);
            }
            System.out.println(getLongestGene(string));
            longest_gene_length = getLongestGene(string).length();
            longest_gene_counter = Math.max(longest_gene_length, longest_gene_counter);
        }
        
        String[] long_strings_arr= storageResourceToArr(long_strings);
        String[] highCG_arr = storageResourceToArr(highCG);
        int j = 0;
        
        System.out.println("Strings longer than 9 char: " + Arrays.toString(long_strings_arr));
        System.out.println("Number of strings longer than 9 char: " + long_strings_arr.length);
        System.out.println("Strings with CG ratio higher than 0.35: " + Arrays.toString(highCG_arr));
        System.out.println("Number of strings with CG ratio higher than 0.35: " + highCG_arr.length);
        System.out.println("The length of the longest gene in sr is: " + longest_gene_counter);
    }
    
    public static void testFindGene() {
        System.out.println("findGene Base case test passed: " + findGene("ATGGACAGTCCTGTATAG").equals("GACAGTCCTGTA"));
        System.out.println("findGene Stop codon off by 1 case test passed: " + findGene("ATGGACAGTCCTGTTAG").equals(""));
        System.out.println("findGene Two stop codon case test passed: " + findGene("ATGGACAGTCCTTAGTAG").equals("GACAGTCCT"));
        System.out.println("findGene No stop codon case test passed: " + findGene("ATGGACAGTCCTGTAAAG").equals(""));
        System.out.println("findGene Two different stop codon case test passed: " + findGene("ATGGACAGTCCTTGATAG").equals("GACAGTCCT"));
    }
    
    /**
     * Given a DNA string, searches for the first occurrence of a stop codon ("TAA", "TAG", or "TGA") starting from the specified startIndex.
     * If a valid stop codon is found (i.e., the index is a multiple of 3 greater than the startIndex), returns its index.
     * Otherwise, returns the length of the DNA string.
     *
     * @param dna the DNA string to search for a stop codon
     * @param startIndex the starting index for the search
     * @param stopCodon the stop codon sequence to search for
     * @return the index of the first occurrence of the stop codon, or the length of the DNA string if no valid stop codon is found
     */
    public static int findStopCodon (String dna, int startIndex, String stopCodon) {
        int index = dna.indexOf(stopCodon, startIndex);
        boolean valid_index = (index - startIndex) % 3 == 0 && index != -1;
        return valid_index ? index : dna.length();
    }
    
    public static StorageResource getAllGenes(String dna) {
        // initialize left pointer at 0
        // initialize right pointer at 1
        // intialize array pointer
        // initialize result at "something"
        // Initialize string Array of size dna.length / 3
        // While right pointer is not equal to 0
            // Find gene at substring of left to right
            // If gene is found
                // Add it to array
                // increment array pointer
        // print all array values that are not null
        StorageResource storage = new StorageResource();
        int left = 0;
        int right = 1;
        int arr_pos = 0;
        String[] potential_genes = new String[dna.length() / 3];
        
        while(right <= dna.length()) {
            String substring = dna.substring(left, right);
            String potential_gene = findGene(substring);
                     
            if (potential_gene.length() > 0) {
                storage.add(potential_gene);
                potential_genes[arr_pos] = potential_gene;
                arr_pos++;
                
                left = right;
                
            }
            right++;
        }
        String[] genes = Arrays.copyOfRange(potential_genes, 0, arr_pos);
        return storage;
    }
    
    /**
     * Given a DNA string, finds and returns the gene that starts with the codon "ATG" and ends with the nearest stop codon "TAA", "TAG", or "TGA".
     * If no such gene exists, returns an empty string.
     *
     * @param dna the DNA string to search for a gene
     * @return the gene that starts with "ATG" and ends with the nearest stop codon, or an empty string if no gene is found
     */
    public static String findGene(String dna) {
        int start_index = dna.indexOf("ATG");
        String[] stop_codons = {"TAA", "TAG", "TGA"};
        int[] stop_indices = {dna.length(), dna.length(), dna.length()};
        String gene = "";
        
        if (start_index == -1) {
            return "";
        }
       
        for (int i = 0; i < stop_codons.length; i++) {
            stop_indices[i] = findStopCodon(dna, start_index, stop_codons[i]);
           
          }
        
        int stop_index = Arrays.stream(stop_indices).min().getAsInt();
        return stop_index != dna.length() ? dna.substring(start_index, stop_index + 3) : "";
}
}