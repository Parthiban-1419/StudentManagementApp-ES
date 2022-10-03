package com.es;



import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultElement;
import org.antlr.v4.parse.ANTLRLexer;
import org.antlr.v4.parse.ANTLRParser;
import org.apache.http.HttpHost;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.RangeQueryBuilder;
import org.apache.lucene.document.LongRange;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.bouncycastle.util.Longs;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.common.xcontent.XContentType;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.plugin.dom.core.Element;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogManager {
	String index;
	SearchResponse searchResponse;
	IndexSearcher indexSearcher;

	public LogManager(String index) {
		this.index = index;
	}

	RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost", 9200, "http")));

	public static void main(String[] args) throws Exception {
		LogManager log = new LogManager("crud-log");
		System.out.println(log.luceneSearch("operation", "*"));
	}

	public String putLog(JSONObject json) throws IOException {
		IndexRequest indexRequest = new IndexRequest(index);
		indexRequest.source(json.toString(), XContentType.JSON);
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

		return indexResponse.getResult().name();
	}

	public Object checkLog(String key, Object value) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(index);
		searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchQuery(key, value)));
		searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println("hits : " + searchResponse.getHits().getTotalHits().value);
		return (searchResponse.getHits().getTotalHits().value > 0) ? true : false;
	}

	public JSONArray getLog(String key, Object value) throws Exception {
		JSONArray jArray = new JSONArray();
		JSONParser parser = new JSONParser();
		if ((Boolean) checkLog(key, value)) {
			SearchHit[] searchHit = searchResponse.getHits().getHits();
			for (SearchHit hit : searchHit)
				jArray.add((JSONObject) parser.parse(hit.getSourceAsString()));
		}
		return jArray;
	}

	public JSONArray luceneSearch(String field, Object value) throws Exception {
		Directory directory = FSDirectory.open(Paths.get("D:/ES/elasticsearch-7.9.2/data/nodes/0/indices/a9KuzLkPSBiZiJgxdKUNQA/0/index"));
		IndexReader reader = DirectoryReader.open(directory);
		indexSearcher = new IndexSearcher(reader);
		Term term = new Term(field, value.toString().toLowerCase());
		Query query = new WildcardQuery(term);
		TopDocs hits = indexSearcher.search(query, reader.maxDoc());
		System.out.println("max doc : '" + reader.maxDoc() + "' term : '" + term + "' query : '" + query + "' hits in luceneSearch : " + hits.totalHits.value);
		return getResult(hits);
	}
	public JSONArray getResult(TopDocs hits) throws IOException, ParseException {
		JSONArray jArray = new JSONArray();
		JSONParser parser = new JSONParser();
		JSONObject json;
		Document document;
		byte[] bytes;
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			document = indexSearcher.doc(scoreDoc.doc);
			bytes = document.getField("_source").binaryValue().bytes;
			json = (JSONObject) parser.parse(new String(bytes));
			jArray.add(json);
		}
		System.out.println(jArray);
		return jArray;
	}
}































/*

	public static void main(String[] args) throws Exception {
		LogManager log = new LogManager("crud-log");
//		Directory dir = (Directory) FSDirectory.open(Paths.get("index"));
//		Document document;
//		byte[] bytes;
		TermRangeQuery rq;
//		TopDocs hits = log.luceneSearch("operation", "l*");
//		for (ScoreDoc scoreDoc : hits.scoreDocs) {
//			document = log.indexSearcher.doc(scoreDoc.doc);
//			bytes = document.getField("_source").binaryValue().bytes;
//			System.out.println(new String(bytes));
//		}
	}

public JSONArray getLog() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(index);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.size(1000);
		searchSourceBuilder.sort(new FieldSortBuilder("time").order(SortOrder.ASC));
		searchRequest.source(searchSourceBuilder);
		JSONArray jArray = new JSONArray();
		JSONParser parser = new JSONParser();
		Map<String, Object> map = null;

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		if (searchResponse.getHits().getTotalHits().value > 0) {
			SearchHit[] searchHit = searchResponse.getHits().getHits();
			for (SearchHit hit : searchHit)
				jArray.add((JSONObject) parser.parse(hit.getSourceAsString()));
		}

		return jArray;
	}


public JSONArray luceneSearch(String field, long from, long to) throws Exception {
		Directory directory = (Directory) FSDirectory.open(Paths.get("D:/ES/elasticsearch-7.9.2/data/nodes/0/indices/a9KuzLkPSBiZiJgxdKUNQA/0/index"));
		IndexReader reader = DirectoryReader.open(directory);
		indexSearcher = new IndexSearcher(reader);
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		Query query  = new TermRangeQuery(field, new BytesRef(buffer.putLong(from).array()), new BytesRef(buffer.putLong(to).array()), true, true);
		TopDocs hits = indexSearcher.search(query, reader.maxDoc());
		System.out.println("max doc : '" + reader.maxDoc() + "' query : '" + query + "' hits in luceneSearch : " + hits.totalHits.value);
		return getResult(hits);
	}
 */



//	public Directory createDoc(String title, String body) throws IOException {
//		Directory memoryIndex = new RAMDirectory();
//		StandardAnalyzer analyzer = new StandardAnalyzer();
//		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//		IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
//		Document document = new Document();
//
//		document.add(new TextField("title", title, Field.Store.YES));
//		document.add(new TextField("body", body, Field.Store.YES));
//
//		writter.addDocument(document);
//		writter.close();
//
//		return memoryIndex;
//	}


//	public void luceneSearch2(String field, Object searchString) throws Exception {
////		SearchRequest searchRequest = new SearchRequest();
//		Directory directory = (Directory) FSDirectory.open(Paths.get("index"));
//		IndexReader reader = DirectoryReader.open(directory);
//		IndexSearcher indexSearcher = new IndexSearcher(reader);
//		System.out.println(searchString.getClass());
//		Term term = new Term(field, String.valueOf(searchString));
//
//		Query query = new WildcardQuery(term);
//		TopDocs hits = indexSearcher.search(query, reader.maxDoc());
////
//		System.out.println("max doc : '" + reader.maxDoc() + "' term : '" + term + "' query : '" + query + "' hits in luceneSearch : " + hits.totalHits.value);
//
//	}

//	public void luceneSearch(String field, String startString, String endString) throws Exception {
//		SearchRequest searchRequest = new SearchRequest();
//		Directory directory = (Directory) FSDirectory.open(Paths.get("index"));
//		IndexReader reader = DirectoryReader.open(directory);
//		IndexSearcher indexSearcher = new IndexSearcher(reader);
//		Term term1 = new Term(field, startString), term2 = new Term(field, endString);

//		Element e = new Element();
//
//		RangeQueryBuilder rq = new RangeQueryBuilder();// = new BooleanQuery().Builder().add();
//		DefaultElement de = new DefaultElement();
//		de.setAttribute(field, startString);
//		Query query = rq.getQuery(de);
//				new TermRangeQuery(field, startString, endString, true, true);
//		TopDocs hits = indexSearcher.search(query, reader.maxDoc());

//	}

//	public static void displayHits(IndexSearcher indexSearcher, TopDocs hits) throws Exception {
//		System.out.println("Number of hits: " + hits.length());
//		Iterator<ScrollableHitSource.Hit> it = hits.iterator();
//		while (it.hasNext()) {
//			Hit hit = it.next();
//			Document document = hit.getDocument();
//			String path = document.get("operation");
//			System.out.println("Hit: " + path);
//		}
//		ScoreDoc[] scoreDoc = hits.scoreDocs;
//		for(int i=0; i < hits.totalHits.value; i++){
//			System.out.println(i + "\t" + hits.totalHits);
//			System.out.println( "\t" + scoreDoc[i]);
//
//		}

//		List<Document> documents = new ArrayList<>();
//		List<IndexableField> iField;
//		Document document;
//		for (ScoreDoc scoreDoc : hits.scoreDocs) {
//			document = indexSearcher.doc(scoreDoc.doc);
//			iField = document.getFields();
//			documents.add(document);
//			System.out.println( "\t" + scoreDoc);
//			System.out.println("document : " + document);
//			System.out.println("iField : " + document.getFields());
//			for(IndexableField fiel : iField){
//				System.out.println(fiel.name());
//				System.out.println(document.getValues(fiel.name()) + "\t" + document.get(fiel.name()));
//				for(String f : document.getValues(fiel.name())){
//					System.out.println("f : " + f);
//				}
//				System.out.println("\n\n");
//			}
//
//		}

//		return documents;
//		System.out.println("**************\t" + hits.toString());
//		System.out.println( "\t" + scoreDoc[0].doc + "***************");
//		System.out.println(hits.totalHits.value);
//	}

//	public List<Document> getDoc(Directory memoryIndex, String inField, String queryString) throws ParseException, IOException {
//		StandardAnalyzer analyzer = new StandardAnalyzer();
//		Query query = new QueryParser(inField, analyzer).parse(queryString);
//
//		IndexReader indexReader = DirectoryReader.open(memoryIndex);
//		IndexSearcher searcher = new IndexSearcher(indexReader);
//		TopDocs topDocs = searcher.search(query, indexReader.maxDoc());
//		List<Document> documents = new ArrayList<>();
//		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//			documents.add(searcher.doc(scoreDoc.doc));
//		}
//		return documents;
//	}


	