/*
 This helps to run oil-rig-api project
 <p>
 Copyright (C) 2017 Clearstream.TV, Inc. All Rights Reserved.
 Proprietary and confidential.
 */

package com.rnd.features.steps.runner;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "src/test/resources/features/api/MakePOJO.feature", glue = {
		"com.rnd.features.steps.api.steps", "com.rnd.hooks" }, format = {
				"json:target/cucumber-report/cucumber.json" }, plugin = { "json:target/cucumber-report/cucumber.json" })
public class TestRunner {
}