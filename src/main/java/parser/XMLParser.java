package parser;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLParser {
	
	private static final int MYTHREADS = 30;
	private String POSTS_XML_FILE_NAME;
	private String OUTPUT_FILE_NAME;
	private String STOPWORDS_FILE_NAME;
	
	public static ArrayList<String> stopWords;
	private ExecutorService executorService;
	
	private static final String ATTR_POST_ID = "Id";
	private static final String ATTR_POST_TYPE_ID = "PostTypeId";
	private static final String ATTR_BODY = "Body";
	private static final String ATTR_TITLE = "Title";
	private static final String ATTR_TAGS = "Tags";
	
	private static final String POST_TAG_NAME = "row";
	
	
	public XMLParser() {
//		POSTS_XML_FILE_NAME = XMLParser.class.getClassLoader().getResource("Posts.xml").getPath();
		POSTS_XML_FILE_NAME = "/media/qimia/546cdc54-8498-4351-af4b-7fb03f62d2fb/Tagging/SO_Dataset/PostsCleaned.xml";
		OUTPUT_FILE_NAME = XMLParser.class.getClassLoader().getResource("output.csv").getPath();
		STOPWORDS_FILE_NAME = XMLParser.class.getClassLoader().getResource("stopwords.txt").getPath();
		
		initExecutors();
		loadStopwords();
	}
	
	public void doEventParser(){
		long startTime = System.currentTimeMillis();
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
		    Reader fileReader = new FileReader(POSTS_XML_FILE_NAME);
		    XMLEventReader reader = factory.createXMLEventReader(fileReader);
		    int counter = 0;
		    XMLEvent event;
		    Iterator<?> iterator;
		    Post rowPost;
		    
		    outerLoop:
		    while (reader.hasNext()) {
		    	event = reader.nextEvent();
		    	if (event.isStartDocument() || event.isEndDocument())       
		    		continue;
		    	if (event.isStartElement() && event.asStartElement().getName().toString().equals(POST_TAG_NAME)) {
	
			        iterator = ((StartElement) event).getAttributes();
			        rowPost = new Post();
			        while (iterator.hasNext()) {
			          Attribute attribute = (Attribute) iterator.next();
			          QName name = attribute.getName();
			          if (name.toString().equals(ATTR_POST_ID))
			        	  rowPost.setId(attribute.getValue());
			          else if (name.toString().equals(ATTR_POST_TYPE_ID)){
			        	  if (!attribute.getValue().equals("1"))
			        		  continue outerLoop;
			          }
			          else if (name.toString().equals(ATTR_TITLE))
			        	  rowPost.setTitle(attribute.getValue());
			          else if (name.toString().equals(ATTR_BODY))
			        	  rowPost.setBody(attribute.getValue());
			          else if (name.toString().equals(ATTR_TAGS))
			        	  rowPost.setTags(attribute.getValue());
			        }
			        System.out.println(counter++);
			        runWriteExecutor(OUTPUT_FILE_NAME, rowPost.getCSVRow());
			        rowPost = null;
		    	}
		    	if (event.isEndElement()) {
		    		continue;
		    	}
	    	}
		    executorService.shutdown();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}
	
	private void initExecutors(){
		executorService = Executors.newFixedThreadPool(MYTHREADS);
	}
	
	private void runWriteExecutor(String fileName, String raw){
		Runnable worker = new CsvRawWriter(fileName, raw);
		executorService.execute(worker);
	}
	
	private void loadStopwords(){
		try {
			Scanner s = new Scanner(new File(STOPWORDS_FILE_NAME));
			stopWords = new ArrayList<String>();
			while (s.hasNext()){
				stopWords.add(s.next());
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
