package com.rnd.factory;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.rnd.utility.Constants;

public class LoadTestData {
	public static Map<String, Map<String, String>> apiData = new HashMap<String, Map<String, String>>();

	public static void loadDataFromJsonFile() {

		try {
			Gson gson = new Gson();

			Reader reader = Files.newBufferedReader(Paths.get(Constants.TEST_TEMPLATE_DATA));

			apiData = gson.fromJson(reader, Map.class);
			reader.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
