package source_code;

import java.io.*;
import java.util.*;

import weka.classifiers.Evaluation;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.Filter;

public class Process_patent {
	private static Properties prop;
	static ArrayList classlist = new ArrayList<String>();
	
	static String classname;
	private static int counter = 0;
	//public static int tagcounter=0;
	
	public static void setcounter(int counter1) {
		// TODO Auto-generated method stub
		counter = counter1;
	}
	
	
	public static String smo(String filename) throws Exception {
		double[] fcdlist = {};
		InputStream propinput = null;
		prop = new Properties();
		propinput = Nasa_Main.class.getResourceAsStream("/resources/" + "propertiesFile.properties");
		prop.load(propinput);
		propinput.close();
		
		File absPathFile=new File("");
		String outputreturn = null;
		StringBuffer output = new StringBuffer();
		StringBuffer output1 = new StringBuffer();
		File file = new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+filename);
		try {

			InputStream input = Process_patent.class.getResourceAsStream("/resources/" + filename);
			OutputStream out = new FileOutputStream(file);
			int read;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.close();
			file.deleteOnExit();
			
			
			File stopwordsfile=new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"stop_words.txt");
			InputStream input1=	Process_patent.class.getResourceAsStream("/resources/"+"stop_words.txt");
					OutputStream out1 = new FileOutputStream(stopwordsfile);
					int read1;
					byte[] bytes1 = new byte[1024];

					while ((read1 = input1.read(bytes1)) != -1) {
						out1.write(bytes1, 0, read1);
					}
					out1.close();
			
			File fc1_model=new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"fc_patent.model");
			InputStream input2=	Process_patent.class.getResourceAsStream("/resources/"+"fc_patent.model");
					OutputStream out2 = new FileOutputStream(fc1_model);
					int read2;
					byte[] bytes2 = new byte[1024];

					while ((read2 = input2.read(bytes2)) != -1) {
						out2.write(bytes2, 0, read2);
					}
					out2.close();
			
			File ac_model=new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"ac_patent.model");
			InputStream input3=	Process_patent.class.getResourceAsStream("/resources/"+"ac_patent.model");
					OutputStream out3 = new FileOutputStream(ac_model);
					int read3;
					byte[] bytes3 = new byte[1024];

					while ((read3 = input3.read(bytes3)) != -1) {
						out3.write(bytes3, 0, read3);
					}
					out3.close();
			
			File SMO_model=new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"SMO_patent.model");
			InputStream input4=	Process_patent.class.getResourceAsStream("/resources/"+"SMO_patent.model");
					OutputStream out4 = new FileOutputStream(SMO_model);
					int read4;
					byte[] bytes4 = new byte[1024];

					while ((read4 = input4.read(bytes4)) != -1) {
						out4.write(bytes4, 0, read4);
					}
					out4.close();

			File trainFinal=new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"train_patent.arff");
			InputStream input5=	Process_patent.class.getResourceAsStream("/resources/"+"train_patent.arff");
					OutputStream out5 = new FileOutputStream(trainFinal);
					int read5;
					byte[] bytes5 = new byte[1024];

					while ((read5 = input5.read(bytes5)) != -1) {
						out5.write(bytes5, 0, read5);
					}
					out5.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		BufferedReader reader1 = new BufferedReader(new FileReader(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"testarff.arff"));
		Instances test = new Instances(reader1);
		reader1.close();
		test.setClassIndex(1);
		
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"fc_patent.model"));
		Filter fc1=(Filter) inputStream.readObject();
		 
		Instances newTest = Filter.useFilter(test, fc1);
		inputStream.close();
		
		newTest.setClassIndex(0);
		Filter ac= (Filter) weka.core.SerializationHelper.read(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"ac_patent.model");
		Instances newTest1 = Filter.useFilter(newTest, ac);
		
		output1.append(newTest1);
		
		PrintWriter out1 = new PrintWriter(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"test.arff");
		out1.println(output1);
		out1.close();
		
		PrintWriter outForTagWords=new PrintWriter(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"InputFileTagWordsArff.arff");
		outForTagWords.println(output1);
		outForTagWords.close();
		BufferedReader readerTrain = new BufferedReader(new FileReader(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"train_patent.arff"));
		ArffReader arff = new ArffReader(readerTrain);
		Instances data = arff.getData();
		BufferedReader readerTest = new BufferedReader(new FileReader(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"test.arff"));
		Instances testFinal = new Instances(readerTest);
		
		data.setClassIndex(data.numAttributes() - 1);
		testFinal.setClassIndex(newTest1.numAttributes() - 1);
		SerializedClassifier cls = new SerializedClassifier();
		cls.setModelFile(new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"SMO_patent.model"));
		Enumeration<Object> e = (data).attribute((data).classIndex()).enumerateValues();
		classlist.clear();
		
		Evaluation eval= new Evaluation(data);
		eval.evaluateModel(cls, testFinal);
		fcdlist=cls.distributionForInstance(testFinal.instance(0));
		
		for (Object key : Collections.list(e)) {
			classlist.add(key);

		}
		for (int i = 0; i < fcdlist.length; i++) {
			Probability_TopClass_Patent.setclasslist(classlist);
			Probability_TopClass_Patent.setfcdist(fcdlist);
		}
		Probability_TopClass_Patent.probabilityCL();
		
		for (int i = 0; i < testFinal.numInstances(); i++) {
			double pred = cls.classifyInstance(testFinal.instance(i));
			Nasa_Main.setclassname_patent(testFinal.classAttribute().value((int) pred));

			outputreturn ="Predicted: "
					+ testFinal.classAttribute().value((int) pred);

		}
		return outputreturn;

	}

}