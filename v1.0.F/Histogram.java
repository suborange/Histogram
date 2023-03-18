/**
 * @author Ethan Bonavida *
 * @version 1.01.F ( possibly use a variable somehow?? )
 * @since   5 March 2023
 *
 * DESCRIPTION:
 * Displays a count of distinct characters from an input file, and the occurrences of each character.
 *  * First, read an input filename from the user
 *  * Then, read characters from the file and then display a list of distinct characters and their frequency.
 *  * Finally, the program will draw a vertical bar made up of the letters for each count.
 *  * Assume: input file is <= 200; each line is only one char long;
 *  * no extra space after each character; assume chars go from 'A' to 'K'
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
public class Histogram {
    // TODO change all variable names back to rubric defaults!!
    static final int EXIT=0; // to exit the program if there are any errors.
    static final char SPACE = ' '; // empty space
    //static final char VBAR = '|';
    static final char NULL_LETTER='*';
    static final int NULL_COUNT = -1;
   private static final char[] KEY_SET = {
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K'
    };
    public static String getFileName()
    {
        System.out.print("Input filename : ");

        // ** Get user input for file **
        Scanner user_input = new Scanner(System.in);
        String input_str = user_input.nextLine(); // get full text path entered, every char is on newline. no string mods

        return input_str; // to later be passed to other methods as an argument
    }

    /***************************************************************
     * @param letter
     * array of current letters read from filename
     * @param letterCount
     * array of current count of each letter read from filename
     * @param filename
     * the filename to be read from @GetFileName()
     ***************************************************************/
    public static void read(char [] letter, int[] letterCount, String filename)
    {
        // ** first set array to special character **
        for ( byte i=0; i < letter.length; i++) {
            letter[i] = NULL_LETTER; // special character to later determine when to stop, when letter is "null"
            letterCount[i] = NULL_COUNT; // special number to later determine when to stop, when count is "null"
        }

        // ** read file name(from method), populate letter, and have a count for each of the letters. **
        // ** so the size of @letterCount should be how many @letter there are. first, in read order? **
        File in = new File(filename);
        Scanner input_file=null; // for scope purposes outside of try block
        try {
            // print out for testing purposes
                if (in.createNewFile()){
                    //System.out.println("file " + filename + " created successfully.");  // ?DEPRECATE?
                }
                else {
                    //System.out.println("file " + filename + " already exists."); // ?DEPRECATE?
                }

                input_file = new Scanner(in); // open the file for reading

        } catch (FileNotFoundException e) {
            // this is the error or the scanner class.
            System.out.println("Error - could not find file. Exiting program... "+e);
            e.printStackTrace();
            System.exit(EXIT); // do i want this?
        } catch (IOException e) {
            System.out.println("Error - I/O broke. Exiting program... "+e);
            e.printStackTrace();
            System.exit(EXIT);
        }

        byte letter_index = 0;
        byte index=0;
        char temp_l = 0; // set to nothing first
        boolean found_letter =false; // default to false since first character to be read

        // should i refactor to function? while( File_Has_Input)
        // ** loop through the whole file, grab each char from each line, and insert them into @letter[] **
        // ** also keep count for each letter read in the file, increment counts in @letterCount[] **
        while (input_file != null && input_file.hasNext())
        {
            temp_l=input_file.nextLine().charAt(0); // assumptions allow for one char each line
            // find if temp_l exists within @letter[]
            // loop through all the current read letters from the file
            for ( index=0; index < letter_index; index++ ) { // wont run the first time which is fine ( both == 0 )
                if ( temp_l == letter[index]) {
                    // exists, so increment count for that letter
                    letterCount[index]++; // this should only increment after initial addition. 1 -> 2, 2-> 3, etc.
                    found_letter =true; // enable the flag since a similar letter was found, as to NOT add duplicates
                    break; // should be done here, one character counted for, so break out to read next input from file
                }
            }

            // ** now can add it, since successfully did NOT find already **
            // ** first time @found_letter will be initialized to false, so adds first char
            if (found_letter == false ){ // only when the @letter[] has not been found, then add it.
                letter[letter_index]=temp_l; // if not the first one, then add new char onto the array.
                letterCount[letter_index]+=2; // increment twice to add first count, only ever -1 -> 1
                letter_index++; // move onto the next letter
            }

            // reset for next input to read
            found_letter = false;
        }
        input_file.close(); // completely done with the file.
    }

    /***************************************************************
     * @param letter
     * array of stored letters from the read file, that need to be sorted ( by count, then alphabetically )
     * @param letterCount
     * array of the stored counts of each @letter[], that will be sorted from lowest to highest count
     ***************************************************************/
    public static void sort(char[] letter, int[] letterCount)
    {
        // ** sort the letters and count of each letter first by count, then by alphabetical. **
        // ** bubble sort min -> max **
        // ** USED BUBBLE SORT ALGORITHM **
        for (byte pass_index=0; pass_index < letter.length-1; pass_index++) {
            for (byte index=0; index < letter.length- pass_index-1; index++) {
                // ** first, save some processing power and just skip when its invalid/null **
                if ((letterCount[index+1] == NULL_COUNT)) { // is this possible to add within the for loop parameters?
                    break;
                }

                // save memory and leave within this scope
                int temp_c = 0; // temp count
                char temp_l =0; // temp letter

                if (letterCount[index] > letterCount[index+1] ) { //swap to min, max; > makes this swap from min to max
                    // ** COUNTS **
                    temp_c=letterCount[index]; // setup to swap
                    letterCount[index]=letterCount[index+1]; // swap 1st index with 2nd
                    letterCount[index+1]= temp_c;            // swap 2nd index with 1st ( from temp_c)

                    // ** LETTERS **
                    temp_l=letter[index];
                    letter[index]=letter[index+1];  // swap 1st index with 2nd
                    letter[index+1] = temp_l;       // swap 2nd index with 1st ( from temp_l)
                }
            }
        } // finished all passes of bubble sort, should have both arrays parallel in order, min -> max
        // now need to sort the @letter in order, for each with the same @letterCount
        // ** bubble sort from min(A) -> max (K) **
        for (int count_index=0; count_index < letter.length-1; count_index++) {
            for (byte index=0; index < letter.length- count_index-1; index++) {
                // save some processing power and just skip when its invalid/null
                if ((letter[index+1] == NULL_LETTER)) { // is this possible to add within the for loop parameters?
                    break;
                }

                // save memory and leave within this scope
                char temp_l =0;

                // ** find if the first letter of this specific count is the min or max, and then continue to sort **
                if ( (letter[index] > letter[index+1]) && (letterCount[index]== letterCount[index+1]) ) { //swap to min -> max, A > B.. etc

                    // ** LETTERS **
                    temp_l=letter[index];
                    letter[index]=letter[index+1];  // swap 1st index of this count with 2nd
                    letter[index+1] = temp_l;       // swap 2nd index with 1st ( from temp_l)
                }
            }
        } // end for
    } // end sort method
    public static void display(char[] letter, int[] letterCount)
    {
        /** tq3.txt
         * Char occurrence
         * A 1
         * K 1
         * B 2
         * D 2
         * H 2
         */

        int print_index=0;
        boolean not_done_displaying = true;
        /// display char occurrence, with one chars + its count on each newline
        System.out.println("Char occurrence");

        // ** Display the top portion - the @letter followed by the @letterCount of the @letter **
        while ( not_done_displaying ) {
            if (letter[print_index] != NULL_LETTER) { // Only when it is not our null char ( meaning it is our char we want )
                System.out.print(letter[print_index]);
                System.out.print(SPACE);
                System.out.println(letterCount[print_index]); // A 1 \n -> etc
                // so after sorting, should be printing min ->, parallel data
                print_index++; // next char and count in order
            }
            else { // leave the while loop for some conditions: if there is nothing in the file, or if some error overflowed to this point, or the array would be "empty"
                not_done_displaying = false; // safety
                break;
            }
        } // end first portion of display

        /** tq3.txt
         * ==============================
         * |     2|                B D H
         * |     1|            A K B D H
         */
        // bar ==============================
        System.out.println("==============================");  // len 30
        // reset index
        print_index=(int)(letterCount.length-1); // we want to start from the end.
        // not_done_displaying = true;
        String left_half = "|%1$6s|"; // displays first left half
        String right_half = "%22s"; // displays the second right half ( justified right)
        String temp_max=""; // will temp hold the concatenated string for that current line - gives to final_str
        String final_str=""; // will hold the final string for that current line

        // ** find the maximum of the count of @letter, then with the max, start displaying backwards **
        // for -> letterCount; -- ( find this max count first)
        int max_count =0;
        for (byte max_index=0; max_index < letterCount.length-1; max_index++ ) {
            // compare each count and grab the higher count to set as max
            if (letterCount[max_index] > letterCount[max_index+1]  ) {
                max_count = letterCount[max_index];
            }
            // when first comparison is not greater, then second must be so set that as max
            else if( (letterCount[max_index+1] != -1))  {
                max_count=letterCount[max_index+1];
            }
        }

        // ** with the max, display the first @letterCount formatted, then find all the @letter with at least that many counted, and append to a string
        for (int i=0; i < letterCount.length; i++) { // to start the loop looking for the similar letter counts

            // ** find if the count is our max count ( or greater) to display, then loop through all the @letters with this count( or greater) in alphabetical order **
            if (letterCount[i] >= max_count && max_count > 0) { // finds our current max count(or greater), so starting at the top count and letters

                for (i=i; i <letterCount.length; i++) { // now loop through this max count, get all the chars, display all the chars, then reset, and dec max.

                    // ** if the @letterCount is greater than our max, then it can be printed in that alphabetical order **
                    if (letterCount[i] >= max_count ) { // when greater than max count, we want to add to this group to display. has AT LEAST this many counted
                        String temp_s=""; // blank temp to hold alphabetical order
                        temp_s+= letter[i]; // grab each individual char in order and concatenate
                        temp_s+=SPACE; // concatenate a space
                        temp_max+=temp_s; // concatenate each letter in order to prev held string
                    }
                    else { // when the @letterCount is less than the @max_count
                        System.out.format(left_half, max_count); // print the left half |   3 |
                        print_index--; // next char in order
                        i=-1; // reset @i to 0, because in a for loop will be incremented, so -1++ == 0
                        max_count--; // go to next @max_count
                        final_str = temp_max; // hold the @temp_max for the line, so it can be reset, but still displayed and saved for the last line ( concatenated to @used_chars below)
                        temp_max=""; // reset for next line to be displayed
                        System.out.format(right_half, final_str); // print out the right half |          X Y Z
                        System.out.println(); // go next line ( can maybe add a new line to final_str here?)
                        break;
                    }
                }
            }
        } // end second portion of display


        /** tq3.txt
         * ______________________________
         *         C E F G I J A K B D H
         */
        // bottom bar ______________________________
        System.out.println("______________________________");

        // ** letters displayed justified right side, going by unused letters first, then used letters last. **
        String unused_chars = ""; // holds all the unused characters
        boolean  found =false; // flag for when a character has been found or not

        // ** loop through @KEY_SET and find the unused characters
        for (byte notused_index = 0; notused_index< KEY_SET.length; notused_index++) {
            for (byte used_index=0; used_index < letter.length; used_index++) {
                if (KEY_SET[notused_index] == letter[used_index] && found == false) {
                    found=true; // found it, skip to next compare
                    break;
                }
            }
            // when this one char is not in our read list at all, and is not a star(special/null char) then add it ( add the un-used chars)
            if (found == false ) { // not ever found for this key char ( not used), add it
                unused_chars+= KEY_SET[notused_index];
                unused_chars+=SPACE;
            }
            // reset for next group to compare
            found=false;
        }

        // ** display the last line of chars now **
        String right = "%30s"; // 30 spaces justified to the right
        String final_chars = unused_chars+ final_str; // first concatenate @unused_chars, then concat the @final_str from above ^
        System.out.format(right, final_chars); // display the last line of chars and finish!!!
        // end third and final portion of display
    }

    // *** MAIN DRIVER ***
    public static void main (String[] args)
    {
        final int SIZE = 13;            // 11/12 + 1 for safety.
        char[] letter = new char[SIZE]; // should initialize to 13 elements, enough for 'A' - 'K'
        int[] letter_count = new int[SIZE]; // should be enough to count for 'A' - 'K'
        String filename = getFileName();

        // *** FUNCTION CALLS ***
        read(letter, letter_count, filename);
        sort(letter, letter_count);
        display(letter, letter_count);
    }
}


/**
 * my notes during this:
 *     // so maps are generic and only take in Objects,
 *     // private static final Map<Character, Integer> key_set= new HashMap<Character, Integer>();
 *     //** when this is static, don't use "this", it all access same _filename since its static
 *
 *     import java.util.Arrays; // why doesnt this work?
 *     import static java.util.Arrays.copyOf;
 *     import static java.util.Arrays.sort; // can we use this sorting?
 *
 *         // -- private static String _filename = ""; //** y is this static?
 *     //** uh it doesnt have to be, static is unique in java.
 *     //** its because the function is static and need to reference a static member.
 *     /*private static int[] _letterCount;
 *     private static char[] _letter;
 */