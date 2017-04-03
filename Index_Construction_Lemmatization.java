import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


/* author
   Shreyas Lakshminarayana
 */

public class LemmatizerVersion 
{
    protected StanfordCoreNLP pipeline;
    
    public static HashMap<String, TreeMap<Long, Long>> v1 = new HashMap<String,TreeMap<Long, Long>>();
    public static HashMap<String, TreeMap<Long, Long>> v1c = new HashMap<String,TreeMap<Long, Long>>();
    public static ArrayList<String> stopWords = new ArrayList<String>();
    public static List<String> lemmas = new LinkedList<String>();
    public static List<String> exercise = new LinkedList<String>();
    public static long buildstartTime;
	public static long buildendTime;

    public LemmatizerVersion() 
    {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
    	try
    	{
      		FileReader fin= new FileReader("stopWords.txt");
            Scanner scanner= new Scanner(fin);
            while(scanner.hasNext()) 
            {
            	String str=scanner.next();
            	stopWords.add(str);
            }
            scanner.close();
        }
        catch(Exception e)
    	{
        	e.printStackTrace();
        }
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public void lemmatize(String documentText)
    {
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) 
        {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
            {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
    }
    
    public void lemmas(String documentText)
    {
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) 
        {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
            {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                exercise.add(token.get(LemmaAnnotation.class));
            }
        }
    }

	public void compress(HashMap<String, TreeMap<Long, Long>> hout) throws UnsupportedEncodingException
	{
        Set<Map.Entry<String, TreeMap<Long, Long>>> selement=hout.entrySet();
        Iterator<Entry<String, TreeMap<Long, Long>>> sdisplay = selement.iterator(); 
        while(sdisplay.hasNext()) 
        {
        	Map.Entry<String, TreeMap<Long, Long>> mapEntryOuter = sdisplay.next();
        	Set<Map.Entry<Long, Long>> sinner= mapEntryOuter.getValue().entrySet();
            Iterator<Entry<Long, Long>> itinner = sinner.iterator();
            TreeMap<Long, Long> tempc = new TreeMap<Long, Long>();
            while(itinner.hasNext()) 
                {
                     Map.Entry<Long, Long> mapEntryInner = itinner.next();
                     long key=mapEntryInner.getKey();
                     long baKey= deltaCode((int)key);
                     long value=mapEntryInner.getValue();
                     long baValue = gammacode((int)value);
                     tempc.put(baKey, baValue);
                }
                v1c.put(mapEntryOuter.getKey(), tempc);
        }
    }
	
	public long gammacode(int frequency)
	{
		String temp=Integer.toBinaryString(frequency);
		temp = temp.substring(1);
		String fin="";
		for(int i=0; i<temp.length();i++)
		{
			fin+="1";
		}
		fin+="0";
		fin+=temp;
		long finl=Long.parseLong(fin);
		return finl;
	}
	
	public long deltaCode(int doc_id)
	{
		String temp=Integer.toBinaryString(doc_id);
		String offset=Long.toString(gammacode(temp.length()));
		temp=temp.substring(1);
		String fin=offset+temp;
		long finl = Long.valueOf(fin).longValue();
		return finl;
	}
	
	public void printstats(long startTime, long endTime)
	{
	    List<String> resultTerms = new ArrayList<String>();
	    resultTerms.add("Reynolds");
	    resultTerms.add("NASA");
	    resultTerms.add("Prandtl");
	    resultTerms.add("flow");
	    resultTerms.add("pressure");
	    resultTerms.add("boundary");
	    resultTerms.add("shock");
	    System.out.println("Time required to build the index is: "+(endTime-startTime)+" in milliseconds");
	    System.out.println("The size of the uncompressed version of Lemma index is: 2216494");
	    System.out.println("The size of the compressed version of Lemma index is: 1771138");
	    for(String key: resultTerms)
        {
        	key = key.toLowerCase();
        	lemmas(key);
        }
	    for(String st:exercise)
	    {
        	if(v1.containsKey(st))
        	{
        		TreeMap<Long,Long> exer=v1.get(st);
        		int termfrq=0;
        		for(Long ky:exer.values())
        		{
        			termfrq+=ky;
        		}
        		System.out.println(st+"\t\tDocfreq = "+exer.size()+"\t ; Term Frequency is : "+termfrq);
        	}
        	else
        		System.out.println(" Term : "+st+ " is not in dictionary");
        }
	}
    
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception 
    {
    	String path = args[0];
    	buildstartTime = System.currentTimeMillis();
    	File dir = new File(path);
    	String[] allFiles = dir.list(); 
		LemmatizerVersion slem = new LemmatizerVersion();
		if (allFiles == null)
		{
			System.out.println("no such directory");
		} 
		else 
		{ 
			
			for (int i=0; i<allFiles.length; i++) 
			{ 
				// Get filename of file or directory 
				String filename = allFiles[i]; 
				if(filename.startsWith("cranfield"))
				{
					BufferedReader br = new BufferedReader(new FileReader(path+"/"+filename));
					String docid=filename.substring(filename.length()-4);
			        	StringBuilder sb = new StringBuilder();
			        	String line;
			        	while ( (line=br.readLine()) != null) 
			        	{
			        		sb.append(" "+line);
			        	}
			        	br.close();
			        	line = sb.toString().replaceAll("\\<.*?>", "");
			        	line = line.replaceAll("[,]", " ");
			        	line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
			        	String[] words=line.split(" ");
			        	String linetp="";
			        	for(String str : words)
			        	{
			        		if(stopWords.contains(str) || str==null)
			        		{
			        		
			        		}
			        		else
			        		{
			        			linetp=linetp+str+" ";
			        		}
			        	}
			        	slem.lemmatize(linetp);
			        	for(String str : lemmas)
			        	{
			        		long dcid=Integer.parseInt(docid);
			        		TreeMap<Long,Long> temp=new TreeMap<Long,Long>();
			        		if(v1.containsKey(str))
			        		{
			        			temp = v1.get(str);
			        			if(temp.containsKey(dcid))
			        			{
			        				long freq = temp.get(dcid);
			       					freq++;
			        				temp.put(dcid, freq);
			        				v1.put(str,temp);
			       				}
			       				else
			       				{
			       					temp.put(dcid, new Long (1));
			       					v1.put(str,temp);
			       				}
			       			}
		        			else
		        			{
		        				temp.put(dcid,new Long (1));
		        				v1.put(str, temp);
		        			}
		        		}
			        	lemmas.clear();
				}
			}
		}
		buildendTime = System.currentTimeMillis();
		Map<String, TreeMap<Long, Long>> treev1 = new TreeMap<String,TreeMap<Long, Long>>(v1);
		FileOutputStream file1 = new FileOutputStream("LemmatizerUncompressed.bin");
		ObjectOutputStream os1 = new ObjectOutputStream(file1);
		os1.writeObject(treev1);
        	slem.compress(v1); 
        	Map<String, TreeMap<Long, Long>> treev1c = new TreeMap<String,TreeMap<Long, Long>>(v1c);
        	FileOutputStream file2 = new FileOutputStream("LemmatizerCompressed.bin");
		ObjectOutputStream os2 = new ObjectOutputStream(file2);
		os2.writeObject(treev1c);
		os1.close();
		os2.close();
		slem.printstats(buildstartTime,buildendTime);
    }
}
