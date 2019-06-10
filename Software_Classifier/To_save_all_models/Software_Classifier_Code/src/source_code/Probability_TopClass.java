package source_code;

import java.util.*;

public class Probability_TopClass {
	private static double[] fcdlist1;
	
	private static String[] classlist1;

	public static void setfcdist(double[] fcdlist) {
		// TODO Auto-generated method stub
		fcdlist1 = fcdlist;
	}

	public static void setclasslist(ArrayList classlist) {
		// TODO Auto-generated method stub
		String[] cl = new String[classlist.size()];
		cl = (String[]) classlist.toArray(cl);

		classlist1 = cl;
	}



	private static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {
		// TODO Auto-generated method stub
		// 1. Convert Map to List of Map
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
	}

	public static Map probabilityCL(){
		String[] classes = classlist1;

		Map<String, Double> unsortMap = new HashMap<String, Double>();
		for (int i = 0; i < fcdlist1.length; i++) {
		unsortMap.put(classes[i], fcdlist1[i]);
		}
		
		Map<String, Double> sortedMap = sortByValue(unsortMap);
		
		//print hashmap
		for (String name: sortedMap.keySet()){

            String key =name.toString();
            String value = sortedMap.get(name).toString();  
            


}
		return sortedMap; 
		
	}
}
