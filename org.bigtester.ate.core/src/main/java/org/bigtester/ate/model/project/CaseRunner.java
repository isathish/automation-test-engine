/*******************************************************************************
 * ATE, Automation Test Engine
 *
 * Copyright 2014, Montreal PROT, or individual contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Montreal PROT.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.bigtester.ate.model.project;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import org.bigtester.ate.constant.LogbackTag;
import org.bigtester.ate.constant.TestCaseConstants;
import org.bigtester.ate.model.casestep.TestCase;
import org.bigtester.ate.model.data.TestParameters;
import org.bigtester.ate.model.data.exception.TestDataException;
import org.bigtester.ate.systemlogger.LogbackWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class CaseRunner runs a single test case
 * 
 * @author Peidong Hu
 */
public class CaseRunner implements IRunTestCase {

	/** The my tc. */
	private TestCase myTestCase;

	/** The current executing tc name. */
	protected String currentExecutingTCName; // must not be null

	/**
	 * @return the currentExecutingTCName
	 */
	public String getCurrentExecutingTCName() {
		return currentExecutingTCName;
	}

	/**
	 * @param currentExecutingTCName
	 *            the currentExecutingTCName to set
	 */
	public void setCurrentExecutingTCName(String currentExecutingTCName) {
		this.currentExecutingTCName = currentExecutingTCName;
	}

	/**
	 * @return the myTestCase
	 */
	public TestCase getMyTestCase() {
		return myTestCase;
	}

	/**
	 * @param myTestCase
	 *            the myTestCase to set
	 */
	public void setMyTestCase(final TestCase myTestCase) {
		this.myTestCase = myTestCase;
	}

	/**
	 * Gets the test data.
	 * 
	 * @param ctx
	 *            the ctx
	 * @return the test data
	 */
	@DataProvider(name = "dp")
	public Object[][] getTestData(ITestContext ctx) {
		Object[][] data = new Object[][] { { new TestParameters(ctx
				.getCurrentXmlTest().getName(), ctx.getCurrentXmlTest()
				.getName()) } };
		return data;
	}

	/**
	 * Test data.
	 * 
	 * @param method
	 *            the method
	 * @param testData
	 *            the test data
	 */
	@BeforeMethod(alwaysRun = true)
	public void testData(Method method, Object[] testData) {
		String testCase;
		if (testData != null && testData.length > 0) {
			TestParameters testParams;
			// Check if test method has actually received required parameters
			for (Object testParameter : testData) {
				if (testParameter instanceof TestParameters) {
					testParams = (TestParameters) testParameter;
					testCase = testParams.getTestName();
					this.currentExecutingTCName = String.format("%s", testCase);
					break;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTestName() {

		return this.currentExecutingTCName;
	}

	/**
	 * Test runner1.
	 * 
	 * @param ctx
	 *            the ctx
	 * @throws Throwable
	 */
	@Test(dataProvider = "dp")
	public void runTest(TestParameters testParams) throws Throwable {
		String testname = testParams.getTestFilename();
		ApplicationContext context;
		try {
			context = new FileSystemXmlApplicationContext(testname);
			myTestCase = (TestCase) context
					.getBean(TestCaseConstants.BEANID_TESTCASE);
			myTestCase.goSteps();
			((ConfigurableApplicationContext)context).close();
		} catch (FatalBeanException fbe) {
			if (fbe.getCause() instanceof FileNotFoundException) {
				context = new ClassPathXmlApplicationContext(testname);
				myTestCase = (TestCase) context
						.getBean(TestCaseConstants.BEANID_TESTCASE);
				myTestCase.goSteps();
				((ConfigurableApplicationContext)context).close();
			} else if (fbe instanceof BeanCreationException) {
				ITestResult itr = Reporter.getCurrentTestResult();

				if (itr.getThrowable() != null
						&& itr.getThrowable() instanceof TestDataException) {
					TestDataException tde = (TestDataException) itr
							.getThrowable();
					tde.setTestStepName(((BeanCreationException) fbe.getCause())
							.getBeanName());
					tde.setTestCaseName(((BeanCreationException) fbe)
							.getResourceDescription());
					tde.setMessage(tde.getMessage() + LogbackTag.TAG_SEPERATOR
							+ tde.getTestCaseName() + LogbackTag.TAG_SEPERATOR
							+ tde.getTestStepName());
					throw (TestDataException) itr.getThrowable();
				} else { // other test case bean creation errors. need to create
					// another exception to handle it.
					String[] fullST = Utils.stackTrace(fbe, false);
					LogbackWriter.writeSysError(fullST[1]);
					throw fbe;
				}
			} else {
				throw fbe;
			}
		} 
	}

}