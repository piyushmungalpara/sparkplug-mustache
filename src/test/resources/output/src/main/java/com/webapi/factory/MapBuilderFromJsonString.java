package com.webapi.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.restassured.response.Response;

import java.util.logging.Logger;

public class MapBuilderFromJsonString<T> {
	private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private T instance;
	private static java.util.logging.Logger LOG = Logger.getLogger("");

	@SuppressWarnings("unchecked")
	public MapBuilderFromJsonString(Class<T> clazz, Response response) {

		try {
			instance = (T) gson.fromJson(response.getBody().asString(), clazz.newInstance().getClass());

		} catch (Exception e) {
			LOG.info(String.format("Error during Building Map from Json File !%s", e.getMessage()));
		}
	}

	public T get() {
		return instance;
	}

	public static <T> MapBuilderFromJsonString<T> buildFromJsonString(Class<T> clazz, Response response) {
		return new MapBuilderFromJsonString<>(clazz, response);
	}

}