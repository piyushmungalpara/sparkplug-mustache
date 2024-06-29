package com.webapi.enumtypes;

public enum BaseURL {
	LOGIN {
		@Override
		public String getAPIBaseEndPointPrefix() {
			return "https://test-dev.auth0.com/";
		}
	};

	public String getAPIBaseEndPointPrefix() {
		return null;
	}

}