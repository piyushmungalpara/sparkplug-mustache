package com.webapi.enumtypes;

public enum ObjectType {
	LOGIN {
		@Override
		public String getEntityEndPointSuffixForAPIUrl() {
			return "co/authenticate";
		}
	};

	public String getEntityEndPointSuffixForAPIUrl() {
		return null;
	}
}