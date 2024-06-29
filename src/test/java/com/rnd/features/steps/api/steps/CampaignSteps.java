package com.rnd.features.steps.api.steps;

import com.rnd.factory.LoadTestData;
import com.rnd.factory.POJOBuilderFromJson;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;


public class CampaignSteps {
	
	@Steps
	POJOBuilderFromJson pojoBuilderFromJson;
	
	@Given("^test data is loaded from json file$")
    public void loadTestData() throws Throwable {
        LoadTestData.loadDataFromJsonFile();
    }
	
	@When("^user read json template from given path and make pojo$")
    public void readJsonTemplate() throws Throwable {
		pojoBuilderFromJson.buildJSON("test");
    }

}