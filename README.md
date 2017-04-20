XML Event Reader
================
Simple Java application to read big XML files as Event-Stream, provided features:
 * Read XML using StaX dependency
 * Clean XML file from stopwods, and remove irrelevant characters.
 * Save parsed cleaned data into CSV file.
 
Dependencies
------------
* StAX API, the standard java XML processing API
	```xml
	   <dependency>
				<groupId>javax.xml.stream</groupId>
				<artifactId>stax-api</artifactId>
				<version>1.0-2</version>
	   </dependency>
	```

Notes
-----
* Processed file format
	```xml
		<posts>
		  <row Id="1" PostTypeId="1" AcceptedAnswerId="13" CreationDate="2010-09-13T19:16:26.763" Score="255" ViewCount="424389" Body="This is a common question by those who have just rooted their phones.  What apps, ROMs, benefits, etc. do I get from rooting?  What should I be doing now?" OwnerUserId="10" LastEditorUserId="16575" LastEditDate="2013-04-05T15:50:48.133" LastActivityDate="2015-11-21T04:55:50.150" Title="I've rooted my phone.  Now what?  What do I gain from rooting?" Tags="<rooting><root-access>" AnswerCount="2" CommentCount="0" FavoriteCount="170" CommunityOwnedDate="2011-01-25T08:44:10.820" />
		  ....
		</posts>
	```