// imports go here
import java.util.Scanner; 
import java.io.PrintWriter;

/**
 *	This program is responsible for encrypting and decrypting a text file.
 *  The user enters an appropriate cipher consisting of letters and the 
 *  program is responsible to encrypt a file based on the cipher. Essentially,
 * 	each letter in the original file is shifted by a certain number of 
 *  characters depending on the cipher. The same is done in order to 
 *  decrypt the encrypted file and restore the file back to its original state.
 *	Requires Prompt and FileUtils classes.
 *	
 *	@author	Rishi Salvi
 *	@since	9/21
 */
public class MVCipher {
	// fields go here
	private int encrypter; // whether the user wants to encrypt or decrypt
	private String cipher; // the cipher the user enters
	private String inputFile; // the file the program reads from
	private String outputFile; // the file the program prints to
	private int cipherLetter; // what letter in the cipher should be added
		
	/** Constructor */
	public MVCipher() { 
		encrypter = 0; 
		cipher = "";
		inputFile = ""; 
		outputFile = ""; 
		cipherLetter = 0; 
	}
	
	public static void main(String[] args) {
		MVCipher mvc = new MVCipher();
		mvc.run();
	}
	
	/**
	 *	responsible for running the program, calls all of the methods
	 */
	public void run() {
		System.out.println("\n Welcome to the MV Cipher machine!\n");
		getCipher();
		finishPrompt(); 
		completeProcess();
		printFinalPrompt();
	}
	
	/**
	 * prompts user in order to get their desired cipher
	 * continues running until they provide a cipher that meets all of 
	 * the required conditions 
	 */
	public void getCipher(){
		String check = "";
		Prompt pr = new Prompt();
		boolean isCipher = false; 
		do{
			check = pr.getString("Please input a word to use as a key " + 
								  "(letters only)");
			check = check.toUpperCase(); 
			if (check.length() >= 3){
				for (int i = 0; i < check.length(); i++){
					char current = check.charAt(i); 
					if (current < 65 || current > 90)
						i = check.length(); // breaking out of for-loop
					if (i == check.length() - 1)
						isCipher = true; // valid cipher
				}
			}
			if (!isCipher)
				System.out.println("ERROR: Key must be all letters and " + 
					"at least 3 characters long");
		} while (!isCipher);
		cipher = check; 
	}
	
	/**
	 * asks user for the rest of the prompt in order to understand what 
	 * they want
	 * asks if they want to encrypt/decrypt, what file is the original, 
	 * and where the output should be printed to
	 */
	public void finishPrompt(){
		Prompt pr = new Prompt();
		System.out.println(); 
		encrypter = pr.getInt("Encrypt or decrypt?", 1, 2); 
		
		System.out.println(); 
		if (encrypter == 1)
			inputFile = pr.getString("Name of file to encrypt"); 
		else
			inputFile = pr.getString("Name of file to decrypt"); 
		outputFile = pr.getString("Name of output file"); 
	}
	
	/**
	 * is responsible for controlling the ciphering process
	 * makes the read to and write to files
	 * passes through the read to file line-by-line
	 * prints out the ciphered line using the PrintWriter
	 */
	public void completeProcess(){
		FileUtils fu = new FileUtils();
		Scanner s = fu.openToRead(inputFile); 
		PrintWriter pw = fu.openToWrite(outputFile); 

		int flipper = 1; 
		if (encrypter == 2) // to decrypt
			flipper = -1; // substract value instead of adding
			
		int[] ciphers = new int[cipher.length()]; 
		for (int i = 0; i < cipher.length(); i++)
			ciphers[i] = (cipher.charAt(i) - 64);
			
		while (s.hasNext()){
			String current = s.nextLine(); 
			pw.println(encryptLine(current, flipper, ciphers)); 
		}
		pw.close(); 
	}
	
	/**
	 * is responsible for going through the line char-by-char
	 * deciding if each char is uppercase, lowercase, or not a letter
	 * calling appropriate ciphering method based on result
	 * @param current		line from the original file that is to be changed
	 * @param flipper 		deciding if char should be added/subtracted depending
	 * 						on if the user wants to encrypt/decrypt
	 * @param ciphers		how much a char should be shifted by based on the cipher
	 * @return cipherLine	the line that has undergone the ciphering process
	 */
	public String encryptLine(String current, int flipper, int[] ciphers){
		String cipherLine = ""; 
		for (int i = 0; i < current.length(); i++){
			char oldChar = current.charAt(i); 
			char newChar = (char)(oldChar + flipper*ciphers[cipherLetter]);
			
			if (oldChar >= 'A' && oldChar <= 'Z') // within range of uppercase
				cipherLine += "" + changeUpperCase(newChar);
			else if (oldChar >= 'a' && oldChar <= 'z') // within range of lowercase
				cipherLine += "" + changeLowerCase(newChar); 
			else // not a character (shouldn't be changed)
				cipherLine += "" + oldChar; 
			cipherLetter = (cipherLetter++) % ciphers.length;
		}
		return cipherLine; 
	}
	
	/**
	 * responsible for changing the char if it is outside the required
	 * boundaries to make it within them (UPPERCASE)
	 * @param newChar		current char after the ciphered change
	 * @return 				ciphered char that fits within the range
	 */
	public char changeUpperCase(char newChar){
		if (newChar > 'Z')
			newChar = (char)('A' + (newChar - ('Z' + 1)));
		else if (newChar < 'A')
			newChar = (char)('Z' - (('A' - 1) - newChar));
		return newChar; 
	}
	
	/**
	 * responsible for changing the char if it is outside the required
	 * boundaries to make it within them (lowercase)
	 * @param newChar		current char after the ciphered change
	 * @return 				ciphered char that fits within the range
	 */
	public char changeLowerCase(char newChar){
		if (newChar > 'z')
			newChar = (char)('a' + (newChar - ('z' + 1)));
		else if (newChar < 'a')
			newChar = (char)('z' - (('a' - 1) - newChar));
		return newChar;
	}
	
	/**
	 * prints the final statement that announces the program has been
	 * successful, specifying its goal, cipher, and what file the output
	 * is in
	 */
	public void printFinalPrompt(){
		String whatChanged = "encrypted"; 
		if (encrypter == 2)
			whatChanged = "decrypted"; 
		System.out.println("\nThe " + whatChanged + " file " + outputFile + 
				" has been created using the keyword -> " + cipher); 
			
	}
}
