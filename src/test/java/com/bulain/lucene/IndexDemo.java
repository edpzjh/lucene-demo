package com.bulain.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class IndexDemo {

	@Test
	public void testIndex() throws IOException {
		String indexPath = "target/lucene";
		boolean create = false;

		Directory dir = FSDirectory.open(new File(indexPath));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_32);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_32, analyzer);

		if (create) {
			iwc.setOpenMode(OpenMode.CREATE);
		} else {
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}

		IndexWriter writer = new IndexWriter(dir, iwc);

		String[][] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i][0];
			long length = i;
			String contents = fields[i][1];

			Document doc = new Document();

			Field nameField = new Field("name", name, Field.Store.YES, Field.Index.ANALYZED);
			nameField.setOmitTermFreqAndPositions(true);
			doc.add(nameField);

			NumericField modifiedField = new NumericField("modified", Field.Store.YES, false);
			modifiedField.setLongValue(length);
			doc.add(modifiedField);

			doc.add(new Field("contents", contents, Field.Store.YES, Field.Index.NO));

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				writer.addDocument(doc);
			} else {
				writer.updateDocument(new Term("name", name), doc);
			}

		}

		writer.close();

	}

	private String[][] getFields() {
		return new String[][]{{"Test001", "This is a test 001."}, {"Test002", "This is a test 002."},
		        {"Test003", "This is a test 003."}};
	}

}
