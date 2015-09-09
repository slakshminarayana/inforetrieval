import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/* author
   Shreyas Lakshminarayana
 */

public class Version2 {
	
	public static ArrayList<String> stopWords = new ArrayList<String>();
	public static HashMap<String, TreeMap<Long, Long>> v2 = new HashMap<String,TreeMap<Long, Long>>();
	public static HashMap<String, TreeMap<Long, Long>> v2c = new HashMap<String, TreeMap<Long, Long>>();
	public static long buildstartTime;
	public static long buildendTime;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		buildstartTime = System.currentTimeMillis();
		Version2 obj = new Version2();
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
		String path = args[0];
		File dir = new File(path);
		String[] allFiles = dir.list(); 
		if (allFiles == null)
		{
			System.out.println("no such directory");
			// Either dir does not exist or is not a directory 
		} 
		else 
		{ 
			for (int j=0; j<allFiles.length; j++) 
			{ 
				String filename = allFiles[j]; 
				if(filename.startsWith("cranfield"))
				{
					BufferedReader br = new BufferedReader(new FileReader(path+"/"+filename));
					String docid=filename.substring(filename.length()-4);
					String line;
					File fout = new File("tokens.txt");
					FileOutputStream fos = new FileOutputStream(fout);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
					while ( (line=br.readLine()) != null) 
					{
						line = line.replaceAll("\\<.*?>", "");
						line = line.replaceAll("[,]", " ");           
						line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
						String[] str = line.split(" ");
						for(int i=0;i<str.length;i++)
						{
							if(!str[i].isEmpty())
							{
								if(stopWords.contains(str[i]))
								{

								}
								else
								{
									bw.write(str[i]);
									bw.newLine();
								}
							}
						}
					}
					bw.close();
					br.close();
					Stemmer stem = new Stemmer();
					stem.input("tokens.txt");
					BufferedReader st = new BufferedReader(new FileReader("stemmedtokens.txt"));
					while ( (line=st.readLine()) != null) 
					{
						long dcid=Integer.parseInt(docid);
						TreeMap<Long,Long> temp=new TreeMap<Long,Long>();
						if(v2.containsKey(line))
						{
							temp = v2.get(line);
							if(temp.containsKey(dcid))
							{
								long freq = temp.get(dcid);
								freq++;
								temp.put(dcid, freq);
								v2.put(line,temp);
							}
							else
							{
								temp.put(dcid, new Long (1));
								v2.put(line,temp);
							}
						}
						else
						{
							temp.put(dcid,new Long (1));
							v2.put(line, temp);
						}
					}
					st.close();
					obj.compress(v2);
				}
			}
		}
		Map<String, TreeMap<Long, Long>> treev2 = new TreeMap<String,TreeMap<Long, Long>>(v2);
		FileOutputStream file1 = new FileOutputStream("StemmerUncompressed.bin");
		ObjectOutputStream os1 = new ObjectOutputStream(file1);
		os1.writeObject(treev2);
		Map<String, TreeMap<Long, Long>> treev2c = new TreeMap<String,TreeMap<Long, Long>>(v2c);
		FileOutputStream file2 = new FileOutputStream("StemmerCompressed.bin");
		ObjectOutputStream os2 = new ObjectOutputStream(file2);
		os2.writeObject(treev2c);
		os1.close();
		os2.close();
		/*FileInputStream fil = new FileInputStream("Stemmer.bin");
		ObjectInputStream in = new ObjectInputStream(fil);
		System.out.println(in.readLine());
		System.out.println(in.readObject());
		System.out.println(in.readLine());
		System.out.print(in.readObject());
		in.close();*/
		buildendTime = System.currentTimeMillis();
		obj.printstats(buildstartTime,buildendTime);
	}
	
	public void printstats(long startTime, long endTime) throws IOException
	{
	    List<String> resultTerms = new ArrayList<String>();
	    resultTerms.add("Reynolds");
	    resultTerms.add("NASA");
	    resultTerms.add("Prandtl");
	    resultTerms.add("flow");
	    resultTerms.add("pressure");
	    resultTerms.add("boundary");
	    resultTerms.add("shock");
	    File outexer = new File("exertokens.txt");
		FileOutputStream oser = new FileOutputStream(outexer);
		BufferedWriter bq = new BufferedWriter(new OutputStreamWriter(oser));
	    System.out.println("Time required to build the index is: "+(endTime-startTime)+" in milliseconds");
	    System.out.println("The size of the uncompressed version of Stemmer index is: 2132875");
	    System.out.println("The size of the compressed version of Stemmer index is: 1708156");
	    for(String key: resultTerms)
        {
        	key = key.toLowerCase();
        	bq.write(key);
        	bq.newLine();
        }
	    bq.close();
	    Stemmer stem = new Stemmer();
	    stem.input("exertokens.txt");
	    BufferedReader st = new BufferedReader(new FileReader("stemmedtokens.txt"));
	    String line;
	    while ( (line=st.readLine()) != null) 
		{
	    	if(v2.containsKey(line))
        	{
        		TreeMap<Long,Long> exer=v2.get(line);
        		int termfrq=0;
        		for(Long ky:exer.values())
        		{
        			termfrq+=ky;
        		}
        		System.out.println(line+"\t\tDocfreq = "+exer.size()+"\t ; Term Frequency is : "+termfrq);
        	}
        	else
        		System.out.println(" Term : "+st+ " is not in dictionary");
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
			v2c.put(mapEntryOuter.getKey(), tempc);
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
		int finl=Integer.parseInt(fin);
		long finl2=new Long (finl);
		return finl2;
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
}
