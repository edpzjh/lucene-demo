package com.bulain.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class SearchDemo {

    @Test
    public void testSearch() throws IOException, ParseException {
        String index = "target/lucene";

        FSDirectory fs = FSDirectory.open(new File(index));
        IndexSearcher searcher = new IndexSearcher(fs);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_32);

        QueryParser parser = new QueryParser(Version.LUCENE_32, "name", analyzer);
        Query query = parser.parse("Test*");

        TopDocs topDocs = searcher.search(query, null, 100);

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String name = doc.get("name");
            String modified = doc.get("modified");
            String contents = doc.get("contents");

            System.out.printf("%s %s %s\n", name, modified, contents);
        }

    }

}
