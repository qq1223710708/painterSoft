package com.example.picturedemo820.Thread;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class KetangListParser {

	public static List<String> getKetangList(String data) {
		List<String> ketangList = new ArrayList<String>();
		
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		Element KetangList = null;
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource
					                (new StringReader(data)));
			KetangList = document.getDocumentElement();
			NodeList list = KetangList.getChildNodes();
			for (int index = 0; index <= list.getLength() - 1; index++) {
				Node KetangName = list.item(index);
				ketangList.add(KetangName.getTextContent());				
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return ketangList;
	}
}

