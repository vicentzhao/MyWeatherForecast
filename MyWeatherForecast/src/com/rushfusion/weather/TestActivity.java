package com.rushfusion.weather;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TestActivity extends Activity {

	Activity mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ViewGroup mRoot = (ViewGroup) getWindow().getDecorView();
		// Node params = parseXml("flipper.xml");
		MyWeatherActivity y = new MyWeatherActivity();
		y.init(mContext, mRoot, null);
		y.run();

	}

	public Node parseXml(String assetsFile) {
		try {
			InputStream inputStream = getResources().getAssets().open(
					assetsFile);
			Document doc = null;
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(inputStream);
			inputStream.close();

			Node item = doc.getFirstChild();
			while (item != null) {
				if (item.getNodeType() == Node.ELEMENT_NODE
						&& "iapplication".equals(item.getNodeName())) {
					return item;
				}
				item = item.getNextSibling();
			}

		} catch (IOException e) {

			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
		return null;
	}
}