package com.fdmgroup.apmproject.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;

/**
 * Custom deserializer for converting JSON data into a List of {@link ForeignExchangeCurrency} objects.
 * This deserializer is specifically designed to handle JSON structures where currency data is
 * nested under arbitrary field names that do not directly map to any field in the
 * {@link ForeignExchangeCurrency} class.
 *
 * <p>Typical JSON structure expected:</p>
 * <pre>
 * {
 *     "USD": {
 *         "code": "USD",
 *         "name": "US Dollar",
 *         "rate": 1.234,
 *         ...
 *     },
 *     "EUR": {
 *         "code": "EUR",
 *         "name": "Euro",
 *         "rate": 0.987,
 *         ...
 *     }
 * }
 * </pre>
 *
 * <h2>Usage</h2>
 * This deserializer is intended to be registered with an {@link ObjectMapper} instance used for
 * deserializing JSON that does not fit the standard array or object structure typically expected.
 * It reads each sub-object in the root object as a separate currency, regardless of the key used.
 *
 * <h2>Implementation Details</h2>
 * <ul>
 *     <li>Iterates over all fields of the root JSON node. Each field represents a currency.</li>
 *     <li>Extracts the value node of each field, which should conform to the structure of
 *         {@link ForeignExchangeCurrency}.</li>
 *     <li>Converts each node to a {@link ForeignExchangeCurrency} instance using Jackson's
 *         {@link ObjectMapper#treeToValue} method.</li>
 *     <li>Adds each deserialized currency to the resultant list.</li>
 * </ul>
 *
 * <h2>Exception Handling</h2>
 * <p>Throws {@link IOException} or {@link JsonProcessingException} if an error occurs during the
 * JSON parsing or during the conversion of a JSON tree node to a {@link ForeignExchangeCurrency} object.</p>
 *
 * @author Your Name
 * @version 1.0
 * @see JsonDeserializer
 * @see ForeignExchangeCurrency
 * @see JsonParser
 * @see DeserializationContext
 */

public class CurrencyDeserializer extends JsonDeserializer<List<ForeignExchangeCurrency>> {
	
	@Override
	public List<ForeignExchangeCurrency> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
		List<ForeignExchangeCurrency> currencies = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		
		Iterator<Entry<String, JsonNode>> fields = rootNode.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			JsonNode currencyNode = field.getValue();
			ForeignExchangeCurrency currency = mapper.treeToValue(currencyNode, ForeignExchangeCurrency.class);
			currencies.add(currency);
		}
		return currencies;
	}

}
