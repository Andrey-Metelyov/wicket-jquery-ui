package com.googlecode.wicket.jquery.core.utils;

import java.util.List;

import org.apache.wicket.util.lang.Generics;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;

/**
 * Utility class for json objects
 * 
 * @author Sebastien Briquet - sebfz1
 *
 */
public class JsonUtils
{
	/**
	 * Utility class
	 */
	private JsonUtils()
	{
	}

	/**
	 * Converts an array of {@code String} to a {@link JSONArray} of (quoted) strings
	 * 
	 * @param values the array of strings
	 * @return a {@link JSONArray}
	 */
	public static JSONArray toArray(String[] values)
	{
		List<String> list = Generics.newArrayList(values.length);

		for (String value : values)
		{
			list.add(value);
		}

		return new JSONArray(list);
	}

	/**
	 * Converts an array of enums to a {@link JSONArray} of (quoted) strings
	 * 
	 * @param values the array of strings
	 * @return a {@link JSONArray}
	 */
	public static JSONArray toArray(Enum<?>[] values)
	{
		List<String> list = Generics.newArrayList(values.length);

		for (Enum<?> value : values)
		{
			list.add(value.name());
		}

		return new JSONArray(list);
	}

	/**
	 * Converts a {@link JSONArray} to a {@link List} of {@code Objects}
	 * 
	 * @param values the {@link JSONArray}
	 * @return a new {@link List}
	 */
	public static List<Object> toList(JSONArray values)
	{
		List<Object> list = Generics.newArrayList();

		if (values != null)
		{
			for (int i = 0; i < values.length(); i++)
			{
				list.add(values.get(i));
			}
		}

		return list;
	}

	/**
	 * Converts a {@link List} to its JSON string representation
	 * 
	 * @param list the {@code List} to convert
	 * @return a new JSON String
	 */
	public static String toString(List<?> list)
	{
		return new JSONArray(list).toString();
	}

	/**
	 * Converts a {@link Object} to its JSON string representation
	 * 
	 * @param object the {@code Object} to convert
	 * @return a new JSON String
	 */
	public static String toString(Object object)
	{
		return new JSONObject(object).toString();
	}
}
