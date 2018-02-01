import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ServerOperations {


	private PrintWriter serverOut;
	private Scanner serverInput;

	public ServerOperations(Scanner serverInput, PrintWriter serverOut) {
		this.serverInput = serverInput;
		this.serverOut = serverOut;

	}

	public StringBuilder submit(ConcurrentHashMap<Long, HashMap<String,String>> booklist) {
		StringBuilder response = new StringBuilder();
		HashMap<String,String> clientInput = null;

		try {
			clientInput = parseInput();
		} catch (IOException e) {
			response.append(e.getMessage());
		}

		if (clientInput != null && !clientInput.isEmpty()) {
			try {
				if(validSubmit(clientInput, booklist)) {
					System.out.println("New booklist: " + booklist.toString());
					response.append("New booklist: " + booklist.values());
				} else {
					response.append("Error - Duplicate ISBN Found, no change made.\n");
				}
			} catch(NumberFormatException e) {
				response.append("Error - Invalid Message sent: ISBN must be numerical, and 13 digits\n");
			}
		}else {
			response.append("No change made to booklist.\n");
		}
		return response;
	}

	public StringBuilder get(ConcurrentHashMap<Long, HashMap<String,String>> booklist) {
		StringBuilder response = new StringBuilder();
		Map<Long,HashMap<String,String>> matches = null;
		HashMap<String,String> clientInput = null;
		if(!this.serverInput.hasNext("ALL")) {
			try {
				clientInput = parseInput();

			} catch (IOException e) {
				response.append(e.getMessage());
			}

			matches = getMatches(clientInput, booklist);


		} else {
			this.serverInput.nextLine();
			matches = booklist;
		}

		if(matches != null && !matches.isEmpty()) {
			//serverOut.println("SEQUENCESIZE " + messageSize);
			for(HashMap<String,String> book : matches.values()){
				response.append(toBibTextFormat(book) + "\n");
			}
			//sendToClient(response.toString());
			//int messageSize = message.split("\n").lenght();
			//serverOut.println("SEQUENCESIZE " + messageSize);
			//serverOut.print(message.toString());
			//serverOut.flush();
		} else {
			response.append("Error - No Matches Found");
		}
		return response;
	}

	public StringBuilder remove(ConcurrentHashMap<Long, HashMap<String,String>> booklist) {
		StringBuilder response = new StringBuilder();
		HashMap<String,String> clientInput = null;
		Map<Long,HashMap<String,String>> removals = null;

		try {
			clientInput = parseInput();
		} catch (IOException e) {
			response.append(e.getMessage());
		}
		removals = getMatches(clientInput, booklist);

		for (Long bookISBN : removals.keySet()) {
			booklist.remove(bookISBN);
			response.append("Removed: " + toBibTextFormat(removals.get(bookISBN)) + "\n");
		}

		if (removals.isEmpty()) {
			System.out.println("Nothing Removed.");
			response.append("No matches found. Nothing removed");
		}

		return response;
	}

	public StringBuilder update(ConcurrentHashMap<Long, HashMap<String,String>> booklist) {
		StringBuilder response = new StringBuilder();
		HashMap<String,String> clientInput = null;

		try {
			clientInput = parseInput();
		} catch (IOException e) {
			response.append(e.getMessage());
		}

		try{
			Long isbn;
			if (clientInput.containsKey("ISBN") && booklist.containsKey((isbn = Long.parseLong(clientInput.get("ISBN"))))) {
				booklist.put(isbn, clientInput);
			}
			else{
				response.append("No matching ISBN found.");
			}
		} catch(NumberFormatException e){
			response.append("Invalid ISBN.");
		}

		return response;
	}

	private boolean validSubmit(HashMap<String,String> clientInput, ConcurrentHashMap<Long, HashMap<String,String>> booklist) throws NumberFormatException{
		Long isbn;
		if(clientInput.get("ISBN") != null && !booklist.containsKey(isbn = Long.parseLong(clientInput.get("ISBN")))  ) {
			booklist.put(isbn, clientInput);

			return true;
		}

		return false;

	}

	private Map<Long,HashMap<String,String>> getMatches(HashMap<String,String> clientInput, ConcurrentHashMap<Long, HashMap<String,String>> booklist) {

		Map<Long,HashMap<String,String>> matches = new ConcurrentHashMap<Long,HashMap<String,String>>(booklist);

		// For each value searched for
		for (Entry<String,String> entry : clientInput.entrySet()) {
			// For each book in the current matching list
			for (Entry<Long, HashMap<String,String>> book : matches.entrySet()) {
				HashMap<String, String> currentBook = book.getValue();
				if (entry.getKey() == "AUTHOR") {
					// TODO: Implement a subset method instead of using the conversion to arrays
					List<String> authorsToMatch = Arrays.asList(entry.getValue().split(" and "));
					List<String> bookAuthors = Arrays.asList(currentBook.get(entry.getKey()).split(" and "));
					if (bookAuthors.contains(authorsToMatch)) {
						matches.remove(book.getKey());
					}
				}else {
					if (!(currentBook.containsKey(entry.getKey()) && currentBook.get(entry.getKey()).equals(entry.getValue()))) {
						matches.remove(book.getKey());
					}
				}

			}
		}
		return matches;
	}

	private HashMap<String,String> parseInput() throws IOException {
		List<String> acceptedInputs = new ArrayList<String>();
		HashMap<String,String> clientInput = new HashMap<String,String>();
		String line;
		int index;
		boolean match;
		String err = "";
		String delimeter = " ";

		acceptedInputs.add("ISBN");
		acceptedInputs.add("TITLE");
		acceptedInputs.add("AUTHOR");
		acceptedInputs.add("PUBLISHER");
		acceptedInputs.add("YEAR");

		while(this.serverInput.hasNextLine() && !(line = this.serverInput.nextLine()).isEmpty()) {
			index = 0;
			match = false;
			while(!match && index < acceptedInputs.size()) {
				if(line.startsWith(acceptedInputs.get(index)+delimeter)) {
					int beginIndex = acceptedInputs.get(index).length() + delimeter.length();
					if (beginIndex < line.length()) {
						clientInput.put(acceptedInputs.get(index), line.substring(beginIndex));
						match = true;
					}else {
						match = false;
					}
				}
				index++;
			}
			if (!match) {
				System.out.println("Invalid Request");
				if(err.isEmpty()) {
					err = ("Error - Invalid Messages Recieved: \n");
				}
				if (line.trim().isEmpty()) {
					line = "Empty line with " + line.length() + " spaces.";
				}
				err = err.concat(line + "\n");
			}
		}

		if(!err.isEmpty()) {
			throw new IOException(err);
		}

		return clientInput;
	}



	private String toBibTextFormat(HashMap<String,String> book){

		StringBuilder strBuild = new StringBuilder();
		strBuild.append("@book {");
		if (book.containsKey("AUTHOR") && book.containsKey("YEAR")) {
			String[] authors = book.get("AUTHOR").split(" and ");
			String[] author1 = authors[0].split(",");

			strBuild.append(author1[0]);
			strBuild.append(book.get("YEAR") + ",");
		}
		strBuild.append("\n");
		for (Entry<String,String> attributes : book.entrySet()) {
			strBuild.append("\t" +attributes.getKey()+ " = {" + attributes.getValue() + "},\n");
		}
		strBuild.deleteCharAt(strBuild.length() - 2);
		strBuild.append("}");

		return strBuild.toString();
	}

	public void sendToClient(String text) {
		String[] lines = text.split("\\r?\\n");
		int messageSize = lines.length;
		this.serverOut.println("SEQUENCESIZE " + messageSize);
		for(String line : lines){
			this.serverOut.println(line);
		}
		this.serverOut.flush();

	}
}
