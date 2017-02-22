/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.wicket.jquery.core.converter;

import java.util.List;

import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.util.io.IClusterable;

import com.googlecode.wicket.jquery.core.utils.JsonUtils;

/**
 * Provides a serializable converter for building {@link T}{@code s} as {@link JSONObject}, and vice-versa
 * 
 * @param <T> the object type
 * @author Sebastien Briquet - sebfz1
 *
 */
public abstract class JsonConverter<T> implements IJsonConverter<T>, IClusterable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Converts a {@link T} to a JSON String
	 *
	 * @param object the {@link T}
	 * @return the JSON String
	 */
	 @Override
	public String toString(List<T> objects)
	{
		return JsonUtils.toString(objects);
	}
}
