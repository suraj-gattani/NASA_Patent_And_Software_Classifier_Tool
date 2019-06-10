package source_code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import weka.core.Instances;

public class TagWords_InputFile {
	private static Properties prop;

	public static ArrayList<String> filter() throws Exception {
		InputStream propinput = null;
		prop = new Properties();
		propinput = Nasa_Main.class.getResourceAsStream("/resources/" + "propertiesFile.properties");
		prop.load(propinput);
		propinput.close();

		StringBuffer output = new StringBuffer();
		File absPathFile = new File("");
		BufferedReader reader = new BufferedReader(new FileReader(
				absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory") + "InputFileTagWordsArff.arff"));
		Instances train = new Instances(reader);
		reader.close();
		train.setClassIndex(train.numAttributes() - 1);

		Enumeration<Object> e = train.attribute(train.classIndex()).enumerateValues();

		int numOfAttri = train.numAttributes();
		//System.out.println(numOfAttri);
		for (int n = 0; n < numOfAttri; n++) {

			double[] t1 = train.attributeToDoubleArray(n);
			for (int j = 0; j < t1.length; j++) {

				if (t1[j] > 0.5) {
					output.append(train.attribute(n).name());
					output.append("\n");
				}
			}

		}

		PrintWriter out = new PrintWriter(
				absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory") + "tagwordsInputFile.txt");
		out.println(output);
		out.close();
		ArrayList<String> valuesOut = new ArrayList<String>();
		return valuesOut = repetetions();

	}

	public static ArrayList<String> repetetions() {
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
		ArrayList<String> valuesOut = new ArrayList<String>();
		BufferedReader reader = null;
		File absPathFile = new File("");
		try {
			// Creating BufferedReader object

			reader = new BufferedReader(new FileReader(
					absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory") + "tagwordsInputFile.txt"));

			// Reading the first line into currentLine

			String currentLine = reader.readLine();

			while (currentLine != null) {
				// splitting the currentLine into words

				String[] words = currentLine.toLowerCase().split(" ");

				// Iterating each word

				for (String word : words) {
					// if word is already present in wordCountMap, updating its
					// count

					if (wordCountMap.containsKey(word)) {
						wordCountMap.put(word, wordCountMap.get(word) + 1);
					}

					// otherwise inserting the word as key and 1 as its value
					else {
						wordCountMap.put(word, 1);
					}
				}

				// Reading next line into currentLine

				currentLine = reader.readLine();
			}

			// Getting all the entries of wordCountMap in the form of Set

		//	Set<Entry<String, Integer>> entrySet = wordCountMap.entrySet();

			// Creating a List by passing the entrySet

			//List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(entrySet);
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(wordCountMap.entrySet());
			// Sorting the list in the decreasing order of values

		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
				@Override
				public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
					return (e2.getValue().compareTo(e1.getValue()));
				}
			});

			// Printing the repeated words in input file along with their
			// occurrences

			// System.out.println("Repeated Words In Input File Are :");

			for (Entry<String, Integer> entry : list) {
				//
				if (entry.getValue() > 1) {
					if(entry.getKey().length()>0){
						//System.out.println(entry.getKey().length());
						valuesOut.add(entry.getKey());
						}
				}
				else{
					if(valuesOut.size()<5){
					if(entry.getKey().length()>0){
					//	System.out.println(entry.getKey().length());
					valuesOut.add(entry.getKey());
					}
					}
					else
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close(); // Closing the reader
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//java.util.Collections.sort(valuesOut);
		return valuesOut;
	}

}
