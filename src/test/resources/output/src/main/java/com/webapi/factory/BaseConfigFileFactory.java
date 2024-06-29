package com.webapi.factory;

import com.webapi.enumtypes.QueryMethod;
import com.webapi.utility.api.Constants;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.logging.Logger;

public class BaseConfigFileFactory {
    public interface BaseFileParamDataBuilder {
        String getFileParamData();
    }

    public static class FileParamDataBuilder implements BaseFileParamDataBuilder {
        private static java.util.logging.Logger LOG = Logger.getLogger("");
        private String entityType;
        private QueryMethod method;
        Ini testDataIni = new Ini();

        public FileParamDataBuilder(String entityType, QueryMethod method) {
            this.entityType = entityType;
            this.method = method;
            try {
                testDataIni.load(Constants.TESTDATACONFIG);
            } catch (IOException e) {
                LOG.info(String.format("Error during reading from ini file !%s", e.getMessage()));
            }
        }

        @Override
        public String getFileParamData() {
            Ini.Section section = testDataIni.get(method.toString());
            return section.get(entityType);
        }
    }

    public static FileParamDataBuilder getFileParamDataBuilderWith(String type, QueryMethod method) {
        return new FileParamDataBuilder(type, method);
    }
}