package com.webapi.factory;

import com.webapi.enumtypes.BaseURL;
import com.webapi.enumtypes.ObjectType;

import java.util.logging.Logger;

public class BaseEndPointURLFactory {
	public interface BaseEndPointURLBuilder {
		String buildBaseEndPointURL();
	}

	public static class EndPointURLBuilder implements BaseEndPointURLBuilder {
		private static java.util.logging.Logger LOG = Logger.getLogger("");
		private String assetType;
		private String BASE_ENDPOINT_PREFIX;

		public EndPointURLBuilder() {
		}

		public EndPointURLBuilder(ObjectType type, BaseURL urlType) {
			this.assetType = ObjectType.valueOf(type.toString()).getEntityEndPointSuffixForAPIUrl();
			this.BASE_ENDPOINT_PREFIX = urlType.getAPIBaseEndPointPrefix();
		}

		@Override
		public String buildBaseEndPointURL() {
			String finalBaseEndPointURL = BASE_ENDPOINT_PREFIX + assetType;
			LOG.info(String.format("BaseEnd Point URL for %s is %s", assetType, finalBaseEndPointURL));
			return finalBaseEndPointURL;
		}
	}

	public static EndPointURLBuilder getBaseEndPointURLBuilderWith(ObjectType type, BaseURL urlType) {
		return new EndPointURLBuilder(type, urlType);
	}
}