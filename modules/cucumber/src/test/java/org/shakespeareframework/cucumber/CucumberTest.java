package org.shakespeareframework.cucumber;

import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("org/shakespeareframework/cucumber")
class CucumberTest {}
