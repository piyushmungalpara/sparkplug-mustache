package com.webapi.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

public class MapBuilderFromJsonFile<T> {
	private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private String jsonFilePath;
	private T instance;
	private List<T> instanceList;
	private static java.util.logging.Logger LOG = Logger.getLogger("");

	@SuppressWarnings("unchecked")
	public MapBuilderFromJsonFile(Class<T> clazz, String jsonFilePath, boolean isJsonObjectArray) {
		this.setJsonFilePath(jsonFilePath);
		try {
			// BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath));
			BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(
					new FileInputStream(jsonFilePath), false, ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE,
					ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE)));
			if (isJsonObjectArray) {
				Type collectionType = TypeToken.getParameterized(List.class, clazz.newInstance().getClass()).getType();
				instanceList = (List<T>) gson.fromJson(reader, collectionType);
				reader.close();
			} else {
				instance = (T) gson.fromJson(reader, clazz.newInstance().getClass());
				reader.close();
			}
		} catch (JsonSyntaxException | JsonIOException | InstantiationException | IllegalAccessException
				| IOException e) {
			LOG.info(String.format("Error during Building Map from Json File !%s", e.getMessage()));
		}
	}

	public T get() {
		return instance;
	}

	public static <T> MapBuilderFromJsonFile<T> buildFromTemplate(Class<T> clazz, String jsonFilePath,
			boolean isJsonObjectArray) {
		return new MapBuilderFromJsonFile<>(clazz, jsonFilePath, isJsonObjectArray);
	}

	public void setJsonFilePath(String jsonFilePath) {
		this.jsonFilePath = jsonFilePath;
	}
}