package source_code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stopwords.WordsFromFile;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Process_software {
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

			InputStream input = Process.class.getResourceAsStream("/resources/" + filename);
			OutputStream out = new FileOutputStream(file);
			int read;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.close();
			file.deleteOnExit();
			
			
			File stopwordsfile=new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"stop_words.txt");
			InputStream input1=	Process.class.getResourceAsStream("/resources/"+"stop_words.txt");
					OutputStream out1 = new FileOutputStream(stopwordsfile);
					int read1;
					byte[] bytes1 = new byte[1024];

					while ((read1 = input1.read(bytes1)) != -1) {
						out1.write(bytes1, 0, read1);
					}
					out1.close();
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Instances train = new Instances(reader);
		reader.close();

		BufferedReader reader1 = new BufferedReader(new FileReader(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"testarff.arff"));
		Instances test = new Instances(reader1);
		reader1.close();
		train.setClassIndex(1);
		test.setClassIndex(1);
		StringToWordVector filter = new StringToWordVector();

		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setDebug(false);
		filter.setDoNotCheckCapabilities(false);
		filter.setDoNotOperateOnPerClassBasis(false);
		filter.setInvertSelection(false);
		filter.setLowerCaseTokens(false);
		filter.setMinTermFreq(1);
		filter.setPeriodicPruning(-1.0);
		filter.setSaveDictionaryInBinaryForm(false);
		//SnowballStemmer snowball=new SnowballStemmer();
		//filter.setStemmer(snowball);
		//filter.setStopwordsHandler(null);
		// filter.setTokenizer(null);
		filter.setNormalizeDocLength(
				new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));
		filter.setOutputWordCounts(true);
		filter.setWordsToKeep(1000);
		WordsFromFile stopwordsfile=new WordsFromFile();
	
		//stopwordsfile.setStopwords(new File("C:\\Users\\workspace\\NASA_Patent_Classifier\\stop_words.txt"));
		//filter.setStopwordsHandler(new RegExStopwords("([0-9]|@|n\\/a|[\\%\\€\\$\\£])"));
		
		//stopwordsfile.setStopwords(new File(Process.class.getResource("/resources/" + "stop_words.txt").getPath()));
		stopwordsfile.setStopwords(new File(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"stop_words.txt"));
		filter.setStopwordsHandler(stopwordsfile);
		//filter.setStemmer(snowballstemmer);
		filter.setInputFormat(train); 
		//FilteredClassifier fc1 = new FilteredClassifier();

		Instances newTrain = Filter.useFilter(train, filter); 
		newTrain.setClassIndex(0);
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"fc_software.model"));
	    outputStream.writeObject(filter);
	    outputStream.flush();
	    outputStream.close();
		//weka.core.SerializationHelper.write(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"fc1.model",fc1);
		
		Instances newTest = Filter.useFilter(test, filter); 
		
		//newTrain.setClassIndex(0);
		newTest.setClassIndex(0);

		AttributeSelection AS_filter = new AttributeSelection();
		InfoGainAttributeEval igae = new InfoGainAttributeEval();
		igae.setDoNotCheckCapabilities(false);
		AS_filter.setEvaluator(igae);
		Ranker ranker = new Ranker();
		ranker.setThreshold(0.0);

		AS_filter.setSearch(ranker);
		Instances train1 = new Instances(newTrain);
		AS_filter.setInputFormat(train1);
		Instances newTrain1 = Filter.useFilter(newTrain, AS_filter); 
		
		//AttributeSelectedClassifier ac=new AttributeSelectedClassifier();
		//ac.buildClassifier(newTrain1);
		//weka.core.SerializationHelper.write(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"ac.model",ac);
		Instances newTest1 = Filter.useFilter(newTest, AS_filter); 
		ObjectOutputStream outputStream1 = new ObjectOutputStream(new FileOutputStream(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"ac_software.model"));
	    outputStream1.writeObject(AS_filter);
	    outputStream1.flush();
	    outputStream1.close();
		output.append(newTrain1);
		PrintWriter out = new PrintWriter(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"train.arff");
		out.println(output);
		out.close();

		output1.append(newTest1);
		
		PrintWriter out1 = new PrintWriter(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"test.arff");
		out1.println(output1);
		out1.close();
		
		
		PrintWriter outForTagWords=new PrintWriter(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"InputFileTagWordsArff.arff");
		outForTagWords.println(output1);
		outForTagWords.close();
		
		
		BufferedReader readerTrain = new BufferedReader(new FileReader(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"train.arff"));
		Instances trainFinal = new Instances(readerTrain);
		reader.close();
		BufferedReader readerTest = new BufferedReader(new FileReader(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"test.arff"));
		Instances testFinal = new Instances(readerTest);
		Remove rm = new Remove();
		rm.setAttributeIndices("1");

		SMO cls = new SMO();
		cls.setBatchSize("100");
		cls.setBuildCalibrationModels(false);
		cls.setC(1.0);
		cls.setCalibrator(new Logistic());
		cls.setChecksTurnedOff(false);
		cls.setDebug(false);
		cls.setDoNotCheckCapabilities(false);
		cls.setEpsilon(1.0E-12);
		cls.setNumDecimalPlaces(2);
		cls.setNumFolds(-1);
		cls.setRandomSeed(1);
		cls.setToleranceParameter(0.001);

		PolyKernel pk = new PolyKernel();

		pk.setCacheSize(250007);
		pk.setChecksTurnedOff(false);
		pk.setDebug(false);
		pk.setDoNotCheckCapabilities(false);
		pk.setExponent(1);
		pk.setUseLowerOrder(false);
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(rm);
		fc.setClassifier(cls);

		trainFinal.setClassIndex(newTrain1.numAttributes() - 1);
		testFinal.setClassIndex(newTest1.numAttributes() - 1);

		fc.buildClassifier(trainFinal);
		weka.core.SerializationHelper.write(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"SMO_software.model",fc);
		Evaluation eval = new Evaluation(trainFinal);
		eval.evaluateModel(fc, testFinal);
		fcdlist = fc.distributionForInstance(testFinal.instance(0));
		Enumeration<Object> e = trainFinal.attribute(trainFinal.classIndex()).enumerateValues();
		classlist.clear();
		for (Object key : Collections.list(e)) {
			classlist.add(key);

		}
		for (int i = 0; i < fcdlist.length; i++) {
			Probability_TopClass.setclasslist(classlist);
			Probability_TopClass.setfcdist(fcdlist);
		
			counter = 1;
		}
		
		
		for (int i = 0; i < testFinal.numInstances(); i++) {
			double pred = fc.classifyInstance(testFinal.instance(i));
			Nasa_Main.setclassname(testFinal.classAttribute().value((int) pred));

			outputreturn ="Predicted: "
					+ testFinal.classAttribute().value((int) pred);

		}
		return outputreturn;

	}

}