package source_code;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

public class TextDirectoryToArff {
	private static Properties prop;
	
	public static void movefile(String filepath, String filename) throws Exception {

		InputStream propinput = null;
		prop = new Properties();
		propinput = Nasa_Main.class.getResourceAsStream("/resources/" + "propertiesFile.properties");
		prop.load(propinput);
		propinput.close();
		//System.out.println(prop.getProperty("inputFileArffDir"));
		
		File absPathFile = new File("");
		File source = new File(filepath);
		File destdir = new File(absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory")+prop.getProperty("inputFileArffDir"));
		File dest = new File(destdir + "/" + filename);
		FileUtils.deleteDirectory(destdir);
		
		if (!destdir.exists()) {
			try {
			
				destdir.mkdirs();
			
				if (!dest.exists()) {
				
					try{
					Files.copy(source.toPath(), dest.toPath());
					}
					catch (Exception e) {
						System.out.println("File not found, Please make sure you provide the exact file path.");
						//e.printStackTrace();
						 System.exit(1);
					}		
				}
				
				arffconverter();
			
			} catch (Exception e) {
				
			//e.printStackTrace();
			}
		} else {
			//System.out.println("in else");
			try{
				
			arffconverter();
			}
			catch (Exception e) {
				
				//e.printStackTrace();
				}
			}
	}

	public Instances createDataset(String directoryPath) throws Exception {
		// System.out.println(directoryPath);
		FastVector atts = new FastVector(2);
		atts.addElement(new Attribute("text", (FastVector) null));
		atts.addElement(new Attribute("@@class@@", (FastVector) null));
		Instances data = new Instances(directoryPath, atts, 0);

		File dir = new File(directoryPath);
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			//if (files[i].endsWith(".html")) {
			if (files[i].endsWith(".html")||files[i].endsWith(".txt")) {
				try {
					// System.out.println("entered try block");
					double[] newInst = new double[2];
					// newInst[0] =
					// (double)data.attribute(0).addStringValue(files[i]);
					// newInst[0] =
					// (double)data.attribute(0).addStringValue(files[i]);
					File txt = new File(directoryPath + File.separator + files[i]);

					InputStreamReader is;
					is = new InputStreamReader(new FileInputStream(txt));
					StringBuffer txtStr = new StringBuffer();
					int c;
					while ((c = is.read()) != -1) {
						txtStr.append((char) c);
					}
					newInst[0] = (double) data.attribute(0).addStringValue(txtStr.toString());
					String className = "?";
					newInst[1] = (double) data.attribute(1).addStringValue(className);

					data.add(new DenseInstance(1.0, newInst));
				}

				catch (Exception e) {

				}
			}
			else{
				System.out.println("Not a valid file, Please provide .txt or .html file");
				System.exit(1);
			}
			
		}
		return data;
	}

	public static void arffconverter() {
		StringBuffer Arffoutput = new StringBuffer();
		File absPathFile = new File("");

		String path = absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory")+prop.getProperty("inputFileArffDir");
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		 
		try {
			Instances dataset = tdta.createDataset(path);
			Arffoutput.append(dataset);
			PrintWriter out = new PrintWriter(absPathFile.getAbsolutePath()+prop.getProperty("NasaWorkingDirectory")+"testarff.arff");
			out.println(Arffoutput);
			out.close();

		} catch (Exception e) {
			//System.err.println(e.getMessage());
			//e.printStackTrace();
			System.out.println("Not a valid file. Please provide .txt or .html files");
		}
		
	}
}
