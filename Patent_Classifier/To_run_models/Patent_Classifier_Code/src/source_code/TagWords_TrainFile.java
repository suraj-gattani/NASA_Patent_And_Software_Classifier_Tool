package source_code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

import weka.core.Instances;
import weka.core.stopwords.WordsFromFile;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class TagWords_TrainFile {
	private static Properties prop;
	static ArrayList classlist = new ArrayList<String>();
	static Map<String, int[]> map1 = new HashMap<String, int[]>();
	static Map<String, String> map2 = new HashMap<String, String>();
	static Map<Object, Set<String>> map3 = new HashMap<Object, Set<String>>();

	public static Map<Object, Set<String>> filter() throws Exception {

		InputStream propinput = null;
		prop = new Properties();
		propinput = Nasa_Main.class.getResourceAsStream("/resources/" + "propertiesFile.properties");
		prop.load(propinput);
		propinput.close();
		File absPathFile = new File("");
		
		File file = new File(prop.getProperty("tagWordsTraningFile"));
		File file1=new File(absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory") + prop.getProperty("tagWordsTraningFile"));
		StringBuffer output = new StringBuffer();
		try {

			InputStream input = TagWords_TrainFile.class.getResourceAsStream("/resources/" + file);
			// System.out.println(file.getAbsolutePath());

			OutputStream out = new FileOutputStream(file1);
			int read;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			file.deleteOnExit();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(new FileReader(file1));
		Instances train = new Instances(reader);
		reader.close();

		train.setClassIndex(train.numAttributes() - 1);
		StringToWordVector filter = new StringToWordVector();
	/*	
		WordsFromFile stopwordsfile=new WordsFromFile();
		stopwordsfile.setStopwords(new File(Process.class.getResource("/resources/" + "stop_words.txt").getPath()));
	    filter.setStopwordsHandler(stopwordsfile);
		*/
		
		Enumeration<Object> e = train.attribute(train.classIndex()).enumerateValues();

		for (Object key : Collections.list(e)) {
			classlist.add(key);
			Nasa_Main.setclasslist(classlist);
			output.append(key);
			output.append(" ");

		}

		List<Set<String>> lists = new ArrayList<Set<String>>();
		for (int i = 0; i < classlist.size(); i++) {
			Set<String> list = new HashSet<String>();
			lists.add(list);

		}

		for (int m = 0; m < classlist.size(); m++) {
			map3.put(classlist.get(m), lists.get(m));

		}

		// System.out.println(map3.get("Aeronautics"));

		output.append("\n");
		// System.out.println("index "+classlist.indexOf("Sensors"));

		/*
		 * Iterator it1 = map2.entrySet().iterator();
		 * System.out.println(map2.size()); while (it1.hasNext()) { Map.Entry
		 * pair = (Map.Entry)it1.next(); System.out.println(pair.getKey() +
		 * " = " + pair.getValue()); it1.remove(); }
		 */

		int numOfAttri = train.numAttributes();

		for (int n = 0; n < numOfAttri; n++) {
		//	int[] i = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			int[] i = new int[classlist.size()];
			//int[] i = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			double[] t1 = train.attributeToDoubleArray(n);

			for (int j = 0; j < t1.length; j++) {
				if (t1[j] > 0.0) {
					
					String cls = train.classAttribute().value((int) train.instance(j).classValue());

					int ind = classlist.indexOf(cls);

					i[ind] = i[ind] + 1;
					if (i[ind] > 10) {
						if(map3.get(train.classAttribute().value((int) train.instance(j).classValue())).size()<30){
						
						map3.get(train.classAttribute().value((int) train.instance(j).classValue()))
								.add(train.attribute(n).name());
						}
						else
							break;
					}
					
				}

				map1.put(train.attribute(n).name(), i);
			}

			/*
			 * Iterator it = map2.entrySet().iterator(); while (it.hasNext()) {
			 * Map.Entry pair = (Map.Entry)it.next();
			 * output.append(pair.getKey() + " = " + pair.getValue()+"\n");
			 * it.remove(); }
			 */

			output.append(train.attribute(n).name() + "\t");
			for (int k = 0; k < i.length; k++) {
				int outp = i[k];

				output.append(outp);
				output.append("\t");
			}
			output.append("\n");

		}
		Nasa_Main.setmap(map3);
		// System.out.println(map3.get("Aeronautics"));
		/*System.out.println("Aeronautics"+"   "+map3.get("Aeronautics"));
		System.out.println("Communications"+"   "+map3.get("Communications"));
		System.out.println("Electrical_and_Electronics"+"   "+map3.get("Electrical_and_Electronics"));
		System.out.println("Environment"+"   "+map3.get("Environment"));
		System.out.println("Health_Medicine_and_Biotechnology"+"   "+map3.get("Health_Medicine_and_Biotechnology"));
		System.out.println("Information_Technology_and_Software"+"   "+map3.get("Information_Technology_and_Software"));
		System.out.println("Instrumentation"+"   "+map3.get("Instrumentation"));
		System.out.println("Manufacturing"+"   "+map3.get("Manufacturing"));
		System.out.println("Materials_and_Coatings"+"   "+map3.get("Materials_and_Coatings"));
		System.out.println("Mechanical_and_Fluid_Systems"+"   "+map3.get("Mechanical_and_Fluid_Systems"));
		System.out.println("Optics"+"   "+map3.get("Optics"));
		System.out.println("Power_Generation_and_Storage"+"   "+map3.get("Power_Generation_and_Storage"));
		System.out.println("Propulsion"+"   "+map3.get("Propulsion"));
		System.out.println("Robotics_Automation_and_Control"+"   "+map3.get("Robotics_Automation_and_Control"));
		System.out.println("Sensors"+"   "+map3.get("Sensors"));
*/		// System.out.println(map2.get(e));

		/*
		 * double[] t=train.attributeToDoubleArray(1); for (int
		 * p=0;p<t.length;p++){
		 * output.append(train.classAttribute().value((int)train.instance(p).
		 * classValue())+" "+t[p]); output.append("\n"); }
		 */
//		File absPathFile = new File("");
		
		
		/*PrintWriter out = new PrintWriter(
				absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory") + "inputTags.txt");
		out.println(output);
		out.close();
		*/
		return map3;
		


	}

}