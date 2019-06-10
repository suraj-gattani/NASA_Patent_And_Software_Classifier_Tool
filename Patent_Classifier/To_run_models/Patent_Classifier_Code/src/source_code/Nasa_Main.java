package source_code;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

public class Nasa_Main {

	private static String classname_software;
	private static String classname_patent;
	private static int counter = 0;
	private static double[] fcdlist1;
	static String inputFileName = null;
	static String inputFilePath = null;
	private String inputFileName1;
	private static Properties prop;
	private static String[] classlist1;
	static Map<Object, Set<String>> map3 = new HashMap<Object, Set<String>>();
	private ArrayList<String> stopwordslist;

	// Map for displaying tag words
	public static void setmap(Map<Object, Set<String>> map) {
		// TODO Auto-generated method stub
		map3 = map;
	}

	// List for displaying classes that are defined in the classification
	public static void setclasslist(ArrayList classlist) {
		// TODO Auto-generated method stub
		String[] cl = new String[classlist.size()];
		cl = (String[]) classlist.toArray(cl);

		classlist1 = cl;
	}

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		InputStream propinput = null;
		prop = new Properties();
		propinput = Nasa_Main.class.getResourceAsStream("/resources/" + "propertiesFile.properties");
		prop.load(propinput);
		propinput.close();
		// System.out.println(prop.getProperty("trainingFileName"));

		File absPathFile = new File("");
		File workingDir = new File(absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory"));
		try {
			FileUtils.deleteDirectory(workingDir);
			if (!workingDir.exists()) {
				workingDir.mkdirs();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
		
			// For commandline application processing
			// if the application is called from the command line with
			// the file location as arguments then the below code comes
			// into picture

			if (args.length > 0) {
				try {
					Path p = Paths.get(args[0]);
					String file = p.getFileName().toString();
					System.out.println("File name: " + file);

					storefilename(file);
					filepath(p.toString());
					String output_toFile;
					
					output_toFile= fromCommandLine_software();
					
					Options options = new Options();
					options.addOption("p", false, "prediction file");
					options.addOption("o", false, "write to output file");
					options.addOption("t", false, "tag words");
					CommandLineParser parser = new GnuParser();
					CommandLine cmd = parser.parse(options, args);
					if (cmd.hasOption("p")) {
						PrintWriter out = new PrintWriter(new FileWriter(absPathFile.getAbsolutePath()+ prop.getProperty("outputfolder") + "Prediction_" + file));
						out.write(file+"\r\n");
						out.write(output_toFile+"\r\n");
						
						out.write("Probabilities:"+ "\r\n");
						
						Map<String, Double> probabilitymap = Probability_TopClass.probabilityCL();
						
						for (String name : probabilitymap.keySet()) {

							String key = name.toString();
							String value = probabilitymap.get(name).toString();
							System.out.println(key+" : "+value);
							out.write(key + " " + value);
							out.append("\r\n");
						}
						
						
						out.close();

					}
					else if (cmd.hasOption("t")) {
						
						
						File outputDir = new File(absPathFile.getAbsolutePath() + prop.getProperty("outputfolder"));
						//FileUtils.deleteDirectory(outputDir);
						if (!outputDir.exists()) {
							outputDir.mkdirs();
						}
						
						// writing the output to a text file
						PrintWriter out = new PrintWriter(absPathFile.getAbsolutePath()+ prop.getProperty("outputfolder") + "Prediction_" + file);

						out.write("Patent : ");
						out.write(file);
						out.append("\r\n");
						out.write(output_toFile);
						out.close();
						// System.out.println("writing to output file");

						ArrayList<String> tag = TagWords_InputFile.filter();

						// input file tag words

						PrintWriter outInputtag = new PrintWriter(absPathFile.getAbsolutePath()+ prop.getProperty("outputfolder") + "InputTagWords_" + file);

						for (int i = 0; i < tag.size(); i++) {
							String inputtags = tag.get(i);
							//System.out.println("InputFile Tag words");
							//System.out.println(inputtags);
							outInputtag.write(inputtags);
							outInputtag.append("\r\n");

						}

						outInputtag.close();

						// training file tag words Nasa_Main.classname

						PrintWriter outTraintag = new PrintWriter(absPathFile.getAbsolutePath()+ prop.getProperty("outputfolder") + "CategoryTagWords_" + file);

						Map<Object, Set<String>> mapTrainTagwords = TagWords_TrainFile.filter();
						Set<String> set = mapTrainTagwords.get(Nasa_Main.classname_software);

						for (String s : set) {
							outTraintag.write(s);
							outTraintag.append("\r\n");
						}
						outTraintag.close();
					}

				} catch (Exception e) {

					// System.out.println(e.getClass());
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	

	// code for the command line access for the application
	protected static String fromCommandLine_software() {
		// TODO Auto-generated method stub
		
		StringBuffer outputReturn_patent = new StringBuffer();
		Nasa_Main gt = new Nasa_Main();

		// once the file is selected it is moved into working directory for
		// converting the input file into arff file
		try {
			TextDirectoryToArff.movefile(inputFilePath, inputFileName);
		} catch (Exception e) {

			// System.out.println(e.getClass());
		}

		/*
		 * counter == 0 implies that the classification is done for the top
		 * level category , once the top level category classification id
		 * completed the counter is set to 1. once the counter is set to one ,
		 * new training file for the process of sub category level
		 * classification is made After the completion of the classification ,
		 * output is displayed on the console screen
		 */

		try {
				
			outputReturn_patent.append("Top category_patent: ");
			outputReturn_patent.append(Process_patent.smo(prop.getProperty("trainingFileName")));
			outputReturn_patent.append(System.getProperty("line.separator"));
			System.out.println(outputReturn_patent);
			//System.out.println(outputReturn_patent);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return outputReturn_patent.toString();
	}



	public static void storefilename(String filename2) {
		// TODO Auto-generated method stub
		inputFileName = filename2;
	}

	public static void filepath(String filepath2) {
		// TODO Auto-generated method stub
		inputFilePath = filepath2;

	}

	public static void setclassname(String classname) {
		// TODO Auto-generated method stub
		Nasa_Main.classname_software = classname;
		// System.out.println(classname);
	}
}
