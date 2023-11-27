package com.example.config;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class JsonCustomSerializer extends BeanSerializerModifier {

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		for (BeanPropertyWriter writer : beanProperties) {
			com.fasterxml.jackson.databind.JsonSerializer<Object> js = judgeType(writer);
			if (js != null) {
				writer.assignNullSerializer(js);
			}
		}
		return beanProperties;
	}

	public com.fasterxml.jackson.databind.JsonSerializer<Object> judgeType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getPropertyType();

		if (String.class.isAssignableFrom(clazz)) {
			return new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
						throws IOException, JsonProcessingException {
					jgen.writeString("");
				}

			};
		}

		if (Number.class.isAssignableFrom(clazz)) {
			return new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
						throws IOException {
					jgen.writeString("");
				}

			};
		}

		if (Boolean.class.isAssignableFrom(clazz)) {
			return new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
						throws IOException, JsonProcessingException {
					jgen.writeBoolean(false);
				}

			};
		}
		
		if (java.util.Date.class.isAssignableFrom(clazz)) {
			return new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
						throws IOException, JsonProcessingException {
					jgen.writeString("");
				}

			};
		}
		if (clazz.equals(DateTime.class)) {
			return new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
						throws IOException, JsonProcessingException {
					jgen.writeString("");
				}

			};
		}
		if (clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class)) {
			return new com.fasterxml.jackson.databind.JsonSerializer<Object>() {

				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers)
						throws IOException {
					jgen.writeStartArray();
					jgen.writeEndArray();
				}

			};
		}

		return null;
	}

}
