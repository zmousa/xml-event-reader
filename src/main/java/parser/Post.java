package parser;

import java.util.ArrayList;

public class Post {
	private String id;
	private String title;
	private String body;
	private String tags;
	
	private static final String CSV_SEPERATOR = "|";
	
	public Post() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		cleanTitle();
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
		cleanBody();
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
		cleanTags();
	}
	
	private void cleanTags(){
		this.tags = tags.toLowerCase()
				.replaceAll("><",",")
				.replaceAll("[<>]","");
	}
	
	private String cleanText(String text){
		String[] words = text.toLowerCase()
				.replaceAll("[\\p{Punct}||\\p{Cntrl}&&[^.'-]]"," ")
				.replaceAll(" +",",").split(",");
		ArrayList<String> normalizedWords = new ArrayList<String>();
		for(String word : words)
	        if(!XMLParser.stopWords.contains(word) && !word.matches("[0-9\\p{Punct}]*"))
	        	normalizedWords.add(word.replaceAll("^[.']+|[.']+$",""));
		
		return String.join(",", normalizedWords);
	}
	
	private void cleanTitle(){
		this.title = cleanText(this.title);
	}
	
	private void cleanBody(){
		this.body = cleanText(this.body);
	}
	
	public String getCSVRow(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(id).append(CSV_SEPERATOR).append(title).append(CSV_SEPERATOR).append(body).append(CSV_SEPERATOR).append(tags).append("\r\n");
		return stringBuilder.toString();
	}
}