import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.TreeMap;

/* author
   Shreyas Lakshminarayana
 */

public class IR_Project3 
{
	public static ArrayList<String> stopWords = new ArrayList<String>();
	public static HashMap<String, TreeMap<Long, Long>> v2 = new HashMap<String,TreeMap<Long, Long>>();
	public static long buildstartTime;
	public static long buildendTime;
	public static int doclen;
	public static int collectionSize;
	public static int avgdoclen;
	public static int[] fileSize= new int[1401];
	public static int[] maxtf= new int[1401];
	public static double [] w1 =new double[1400];
	public static  double [] w2 =new double[1400];
	
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		buildstartTime = System.currentTimeMillis();
		collectionSize = 1400;
		try
    	{
			FileReader fin= new FileReader("/people/cs/s/sanda/cs6322/resourcesIR/stopwords");
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
		String path = "/people/cs/s/sanda/cs6322/Cranfield";
		File dir = new File(path);
		String[] allFiles = dir.list(); 
		if (allFiles == null)
		{
			System.out.println("no such directory I am in Main");
		} 
		else 
		{ 
			for (int j=0; j<allFiles.length; j++) 
			{ 
				doclen = 0;
				maxtf[j] = -1;
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
								doclen++;
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
								if(maxtf[j] < freq)
									maxtf[j] = (int) freq;
								v2.put(line,temp);
							}
							else
							{
								temp.put(dcid, new Long (1));
								maxtf[j] = 1;
								v2.put(line,temp);
							}
						}
						else
						{
							temp.put(dcid,new Long (1));
							maxtf[j] = 1;
							v2.put(line, temp);
						}
					}
					st.close();
					fileSize[j] = doclen;
				}
			}
		}
		int docTokenCount=0;
		for(int i=0;i<fileSize.length;i++)
			docTokenCount += fileSize[i];
		avgdoclen = docTokenCount/collectionSize;
        try 
		{
			BufferedReader br = new BufferedReader(new FileReader("/people/cs/s/sanda/cs6322/hw3.queries"));
			String line="";
			while((line=br.readLine())!=null)
			{
				if(line.startsWith("Q"))
				{
					line=br.readLine();
					String str="";
					while(!line.isEmpty())
					{
						str=str+" "+line;
						line=br.readLine();
						if(line==null)
							break;
					}
					System.out.print("Query:"+str);
					String [] query=str.split(" ");
					compute(query);
				}
			}
			br.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		buildendTime = System.currentTimeMillis();
	}
	
	public  static void compute(String [] word)
	{
		System.out.print("\nIndexed form of the query: ");
		for(int i=0;i<word.length;i++)
		{
			Pattern pat = Pattern.compile("\\W"); 
			String strs[] = pat.split(word[i]);
			for (int k = 0; k < strs.length; k++) 
			{
				String key = strs[k];
				String words = key.toLowerCase();
				Stemmer s = new Stemmer();
				s.add(words.toCharArray(), words.length());
				s.stem();
				String stem = s.toString();
				if (stopWords.contains(stem)) 
				{
				} 
				else 
				{
					System.out.print(stem+" ");
					if (v2.containsKey(stem)) 
					{
						TreeMap<Long, Long> value = v2.get(stem);
						Iterator<Entry<Long, Long>> it = value.entrySet().iterator();
						while (it.hasNext()) 
						{
							Map.Entry<Long, Long> data = it.next();
							long filePtr = data.getKey();
							long tf = data.getValue();
							int df = value.size();
							int docLength = fileSize[(int) (filePtr - 1)];
							w1[(int) (filePtr - 1)] += (0.4 + (0.6 * Math.log(tf + 0.5)) / Math.log(maxtf[(int) (filePtr - 1)] + 1.0))* (Math.log(collectionSize / df) / Math.log(collectionSize));
							w2[(int) (filePtr - 1)] += (0.4 + 0.6 * (tf / (tf + 0.5 + 1.5 * (docLength / avgdoclen)))* Math.log(collectionSize / df) / Math.log(collectionSize));
						}
					} 
					else 
					{
						//System.out.println("Not there in the cranfield directory");
					}
				}
			}
		}
		printstats();
	}
	
	static public void printstats()
	{
		System.out.println("\nTotal number of Files: "+collectionSize);
        System.out.println("\nAs per W1:");
        for(int j=0; j<10; j++)
        {
        	double max = -1.0;
        	int doc = -1;
            for(int i=0; i<1400; i++)
            {
            	if(w1[i]>max)
                {
                    max=w1[i];
                    doc= i;
                }    
            }
            System.out.printf("\nRank: %-2d   Score: %1.5f   DocId: %-4d    Headline:", (j+1), w1[doc], (doc+1));
            headline(doc);
            w1[doc]=0.0;
        }
        System.out.println("\n\nAs per W2:");
        for(int j=0; j<10; j++) 
        {
        	double max = -1.0;
            int doc = 1;
            for(int i=1; i<1400; i++) 
            {
                if(w2[i]>max)
                {
                    max=w2[i];
                    doc= i;
                }    
            }
            System.out.printf("\nRank: %-2d   Score: %1.5f   DocId: %-4d    Headline:", (j+1), w2[doc], (doc+1));
            headline(doc);
            w2[doc]=0.0;
        }
       System.out.print("\n\n");
	}
	
	public static void headline(int doc)
	{
		try 
		{
			File dir = new File("/people/cs/s/sanda/cs6322/Cranfield");
			String[] allFiles = dir.list(); 
			if (allFiles == null)
				System.out.println("no such directory");
			else 
			{ 
				try
				{
					File folder=dir.listFiles()[doc] ;
					FileReader fin=new FileReader(folder);
                    Scanner sc=new Scanner(fin);
                    while(sc.hasNext())
                    {
                    	if("<title>".equalsIgnoreCase(sc.next()))
                    	{
                    		while(sc.hasNext())
                    		{
                    			String match= sc.next();
                                if("</title>".equalsIgnoreCase(match))
                                	break;
                                else
                                	 System.out.print("  "+match);
                    		}
                    	}
                    }
				}
                catch(Exception e)
                {
                }
			}			    
		}                    
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
