package com.rnd.utility;

import java.io.File;

import com.rnd.enumtypes.QueryMethod;
import com.rnd.factory.BaseConfigFileFactory;

public class Constants {

	public static final File TESTDATACONFIG = new File(
			System.getProperty("user.dir") + "\\src\\test\\resources\\config\\testdataconfig.ini");

	public static final String TEST_TEMPLATE_DATA = BaseConfigFileFactory
			.getFileParamDataBuilderWith("TESTDATA_FILE_PATH", QueryMethod.TESTDATA).getFileParamData();

}