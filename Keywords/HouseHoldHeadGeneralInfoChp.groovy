import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


import core.RaceUtils
import core.DateOfBirthUtils
import core.DropdownUtils
import core.VerifyUtils
import core.YesNoSelectionUtils
import core.DataFileUploadUtils
import core.ButtonClickUtils
import core.ClickUtils
import core.OverlayUtils
import core.CloseDropdownUtils
import core.BirthDateUtils
import core.DatePickerUtils
import utilities.FormFieldUtils
import core.MuiDropdownUtils

import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import internal.GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration
import java.util.List
import java.time.Duration
import java.util.Random

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait



class HouseHoldHeadGeneralInfoChp {

	private static final int TIMEOUT = 15

	@Keyword
	static void addChpHouseHoldHeadGeneralInfo() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)


		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Step 1 - My Info']", "Step 1 - My Info")

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.clsheading_step")))

		List<String> expectedSteps = [
			"General Info (Required)",
			"Contact (Required)",
			"Address (Required)",
			"Identification (Required)",
			"Insurance (Required)",
			"Disability (Required)",
			"More Info (Required)"
		]

		List<WebElement> stepLabels = driver.findElements(By.cssSelector("span.clsheading_step"))

		WebUI.verifyEqual(stepLabels.size(), expectedSteps.size())

		for (int i = 0; i < expectedSteps.size(); i++) {
			String actual = stepLabels.get(i).getText().trim()
			String expected = expectedSteps.get(i)
			WebUI.verifyEqual(actual, expected)
			println("Verified step: " + actual)
		}

		VerifyUtils.verifyTextByXPath("//p[normalize-space()='PLEASE REVIEW ALL THE INFORMATION AND MAKE THE NECESSARY CHANGES']",
				"PLEASE REVIEW ALL THE INFORMATION AND MAKE THE NECESSARY CHANGES")

		VerifyUtils.verifyTextByXPath(
				"//h3[normalize-space()='General Info']",
				"General Info")

		TestObject mandatoryMessage = new TestObject('mandatoryMessage')
		mandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		VerifyUtils.verifyTextByXPath(
				"//*[normalize-space()='Which program(s) are you applying to for yourself?']",
				"Which program(s) are you applying to for yourself?")

		By dentalCheckbox = By.xpath("//input[@type='checkbox' and @name='isSeniorDental']")

		WebElement dentalCheckboxElement = wait.until(
				ExpectedConditions.presenceOfElementLocated(dentalCheckbox)
				)

		if (!dentalCheckboxElement.isSelected()) {
			js.executeScript("arguments[0].click();", dentalCheckboxElement)
		}

		WebUI.verifyEqual(driver.findElement(dentalCheckbox).isSelected(), true)



		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='Prefix']",
				"Prefix")

		By chpHouseholdHeadPrefixDropdown = By.xpath("//div[@id='mui-component-select-prefix']")
		DropdownUtils.selectDropDownOption(chpHouseholdHeadPrefixDropdown, "Mr.", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)


		VerifyUtils.verifyTextByXPath(
				"//label[@for='firstName' and .//span[normalize-space()='*'] and .//span[normalize-space()='First Name Legally as it appears on your ID']]",
				"* First Name Legally as it appears on your ID")

		By chpHouseholdHeadFirstName = By.xpath("//input[@id='firstName']")
		FormFieldUtils.populateOrAssertTextField(
				driver, chpHouseholdHeadFirstName, ".*", "Adult")

		VerifyUtils.verifyTextByXPath(
				"//label[@for='middleName' and normalize-space()='Middle Name']",
				"Middle Name")

		VerifyUtils.verifyTextByXPath(
				"//label[@for='lastName' and .//span[normalize-space()='*'] and .//span[normalize-space()='Last Name Legally as it appears on your ID']]",
				"* Last Name Legally as it appears on your ID")

		By chpHouseholdHeadLastName = By.xpath("//input[@id='lastName']")
		FormFieldUtils.populateOrAssertTextField(
				driver, chpHouseholdHeadLastName, ".*", "Auto")

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='Suffix']",
				"Suffix")

		By chpSuffixDropdown = By.xpath("//div[@id='mui-component-select-suffix']")
		DropdownUtils.selectDropDownOption(chpSuffixDropdown, "Sr.", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		/*BirthDateUtils.selectDateFromCalendar(
		 driver,
		 wait,
		 "//label[@class='clsFormDateLabel' and .//span[normalize-space()='*'] and contains(normalize-space(.), 'Date of Birth')]",
		 By.xpath("//button[@aria-label='Date of Birth']"),
		 By.xpath("//input[@id='date-picker-dialog']"),
		 "* Date of Birth",
		 "1976",
		 "January 1976",
		 "1",
		 TIMEOUT)*/
		/*By chpHouseholdHeadDoBField = By.xpath("//input[@id='date-picker-dialog']")
		 DateOfBirthUtils.enterDateDirectly(chpHouseholdHeadDoBField, "01/01/1976", TIMEOUT)*/
		TestObject dobInput = new TestObject('Date of Birth input').addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//input[@id='date-picker-dialog']"
				)

		WebUI.waitForElementPresent(dobInput, 15)

		String actualDob = WebUI.getAttribute(dobInput, 'value').trim()

		println "Actual Date of Birth value is: '${actualDob}'"

		WebUI.verifyMatch(actualDob, '01/01/1976', false)

		TestObject dobFormattedText = new TestObject('DOB formatted text').addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//p[normalize-space(.)='(January 01, 1976)']"
				)

		WebUI.waitForElementVisible(dobFormattedText, 10)
		WebUI.verifyElementVisible(dobFormattedText)


		WebUI.delay(1)

		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='*'] and .//span[normalize-space()='What is your country of birth?']]",
				"* What is your country of birth?")

		By chpHouseHoldHeadCountryDropdown = By.xpath("//input[@id='country-select-demo']")
		DropdownUtils.selectDropDownOption(chpHouseHoldHeadCountryDropdown, "USA", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='*'] and .//span[normalize-space()='What is your ethnicity?']]",
				"* What is your ethnicity?")

		By chpHouseHoldHeadEthnicityDropdown = By.xpath("//div[@id='mui-component-select-ethinicity']")
		DropdownUtils.selectDropDownOption(chpHouseHoldHeadEthnicityDropdown, "Not Hispanic or Latino", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)


		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='*'] and .//span[normalize-space()='What is your race?']]",
				"* What is your race?")

		By chpHouseHoldHeadRaceOption = By.xpath("//div[@id='mui-component-select-race']")
		RaceUtils.clearAndSelectTwoOrThreeRaceOptions(chpHouseHoldHeadRaceOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='*'] and .//span[normalize-space()='Gender']]",
				"* Gender")

		By chpHouseHoldHeadGenderDropdown = By.xpath("//div[@id='mui-component-select-gender']")
		DropdownUtils.selectDropDownOption(chpHouseHoldHeadGenderDropdown, "Male", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		String chpHouseholdHeadSsnQuestion = "//p[contains(@class,'input-label') and normalize-space()='Do you have a SSN?']"

		String chpHouseholdHeadSsnYesXpath = chpHouseholdHeadSsnQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpHouseholdHeadSsnSelectedYesXpath = chpHouseholdHeadSsnQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadSsnYesXpath,
				chpHouseholdHeadSsnSelectedYesXpath,
				"Yes")

		VerifyUtils.verifyTextByXPath("//label[normalize-space()='SSN']", "SSN")

		By chpHouseholdHeadSocialSecurityNumber = By.xpath("//input[@id='ssn' and @name='ssn' and @type='password']")

		FormFieldUtils.populateOrAssertMaskedTextField(
				driver,
				chpHouseholdHeadSocialSecurityNumber,
				"regex:\\d{3}-\\d{2}-\\d{4}",
				generateRandomSSN())


		VerifyUtils.verifyTextByXPath("//label[.//span[normalize-space()='*'] and .//span[normalize-space()='What is the highest level of education that you have completed?']]",
				"* What is the highest level of education that you have completed?")

		By chpHouseholdHeadEducationLevelDropdown = By.xpath("//div[@id='mui-component-select-graduationLevel']")
		DropdownUtils.selectDropDownOption(chpHouseholdHeadEducationLevelDropdown, "Some College/Associates Degree", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)
		addChpHouseHoldHeadContactInfo()
	}




	@Keyword
	static void addChpHouseHoldHeadContactInfo() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)


		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Tell us about your contact information']",
				"Tell us about your contact information")

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Fields marked with * are mandatory']",
				"Fields marked with * are mandatory")

		VerifyUtils.verifyTextByXPath("//label[@for='mobilePhone' and .//span[normalize-space()='*'] and .//span[normalize-space()='Mobile Number']]",
				"* Mobile Number")

		By chpHouseholdHeadMobileNumber = By.xpath("//input[@id='mobilePhone']")

		FormFieldUtils.populateOrAssertTextField(driver, chpHouseholdHeadMobileNumber, "regex:\\d{3}-\\d{3}-\\d{4}", generateRandomMobile())

		VerifyUtils.verifyTextByXPath("//label[@for='emailAddress1' and .//span[normalize-space()='*'] and .//span[normalize-space()='Email Address']]",
				"* Email Address")
		String chpHouseHoldHeadEmailAddress = generateRandomEmail()
		By chpHouseholdHeadEmailAddressField = By.xpath("//input[@id='emailAddress1']")

		FormFieldUtils.populateOrAssertTextField(driver, chpHouseholdHeadEmailAddressField, ".*", chpHouseHoldHeadEmailAddress)

		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'input-label') and .//span[normalize-space()='*'] and .//span[normalize-space()='Preferred Language']]",
				"* Preferred Language")

		By chpHouseHoldHeadPreferredLanguageDropdown =
				By.xpath("//div[@id='mui-component-select-userLanguageID' and @role='button']")

		DropdownUtils.selectDropDownOption(chpHouseHoldHeadPreferredLanguageDropdown, "French", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		String chpHouseHoldHeadInterpreterQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Do you need an interpreter?']]"

		String chpHouseHoldHeadInterpreterNoXpath =
				chpHouseHoldHeadInterpreterQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='No']"

		String chpHouseHoldHeadInterpreterNoSelectedXpath =
				chpHouseHoldHeadInterpreterQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='No']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseHoldHeadInterpreterNoXpath,
				chpHouseHoldHeadInterpreterNoSelectedXpath,
				"No")

		String chpHouseHoldHeadCommunicationTimeQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='How do you want us to communicate with you during business hours?']]"


		String chpHouseHoldHeadcommunicationPhoneXpath =
				chpHouseHoldHeadCommunicationTimeQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='Phone']"

		String chpHouseHoldHeadcommunicationPhoneSelectedXpath =
				chpHouseHoldHeadCommunicationTimeQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Phone']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseHoldHeadcommunicationPhoneXpath,
				chpHouseHoldHeadcommunicationPhoneSelectedXpath,
				"Phone")
		ButtonClickUtils.clickEnabledSaveNext(driver, wait)

		addChpHouseHoldHeadAddressInfo()
	}

	@Keyword
	static void addChpHouseHoldHeadAddressInfo() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Tell us about where you live']",
				"Tell us about where you live")


		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Fields marked with * are mandatory']",
				"Fields marked with * are mandatory")

		VerifyUtils.verifyTextByXPath(
				"//label[@for='addressLine1' and .//span[normalize-space()='*'] and .//span[normalize-space()='Address Line 1']]",
				"* Address Line 1"
				)

		By chpAddressLine1Field = By.xpath("//input[@id='addressLine1']")

		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpAddressLine1Field,
				"regex:.+",
				"66 Research Court"
				)
		VerifyUtils.verifyTextByXPath(
				"//label[@for='addressLine2']",
				"Address Line 2 (Suite/Apt#/Building#)")
		VerifyUtils.verifyTextByXPath("//label[@for='city' and .//span[normalize-space()='*'] and .//span[normalize-space()='City']]", "* City")

		By chpHouseholdHeadCity = By.xpath("//input[@id='city']")
		FormFieldUtils.populateOrAssertTextField(driver, chpHouseholdHeadCity, "regex:.+", "Rockville")

		VerifyUtils.verifyTextByXPath("//label[@for='state' and .//span[normalize-space()='*'] and .//span[normalize-space()='State']]", "* State")

		By chpHouseholdHeadState = By.xpath("//input[@id='state']")
		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseholdHeadState,
				"regex:.+",
				"MD")

		VerifyUtils.verifyTextByXPath("//label[@for='zipCode' and .//span[normalize-space()='*'] and .//span[normalize-space()='Zip Code']]", "* Zip Code")

		By chpHouseHoldHeadZipCode = By.xpath("//input[@id='zipCode']")
		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseHoldHeadZipCode,
				"regex:.+",
				"20852")


		/*VerifyUtils.verifyTextByXPath(
		 "//p[@class='doc-head' and .//span[normalize-space()='*'] and .//span[normalize-space()='Upload Proof of Address']]",
		 "* Upload Proof of Address")
		 String hhProofAddressFile = RunConfiguration.getProjectDir() + "/Data Files/HouseholdHeadProofOfAddress.pdf"
		 DataFileUploadUtils.uploadDocumentBySectionLabel(
		 driver,
		 "Upload Proof of Address",
		 hhProofAddressFile,
		 TIMEOUT)*/

		VerifyUtils.verifyTextByXPath(
				"//p[@class='doc-head' and .//span[normalize-space()='*'] and .//span[normalize-space()='Upload Proof of Address']]",
				"* Upload Proof of Address")

		String sectionLabel = "Upload Proof of Address"
		String hhProofAddressFile = RunConfiguration.getProjectDir() + "/Data Files/HouseholdHeadProofOfAddress.pdf"

		if (DataFileUploadUtils.isDocumentAlreadyUploaded(driver, sectionLabel, TIMEOUT)) {
			WebUI.comment("Proof of Identity file already uploaded. Skipping upload.")
		} else {
			DataFileUploadUtils.uploadDocumentBySectionLabel(
					driver,
					sectionLabel,
					hhProofAddressFile,
					TIMEOUT
					)
		}

		String chpHouseholdHeadSeparateMailingAddressQuestion = "//p[contains(@class,'input-label') and normalize-space()='Do you have a separate mailing address?']"

		String chpHouseholdHeadSeparateMailingAddressNoXpath =
				chpHouseholdHeadSeparateMailingAddressQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='No']"

		String chpHouseholdHeadSeparateMailingAddressNoSelectedXpath =
				chpHouseholdHeadSeparateMailingAddressQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='No']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadSeparateMailingAddressNoXpath,
				chpHouseholdHeadSeparateMailingAddressNoSelectedXpath,
				"No")

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)
		addChpHouseHoldHeadIdentityInfo()
	}
	static void addChpHouseHoldHeadIdentityInfo() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Provide proof of identity']",
				"Provide proof of identity")

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Fields marked with * are mandatory']",
				"Fields marked with * are mandatory")


		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='*'] and .//span[normalize-space()='Photo Identification']]",
				"* Photo Identification")

		By chpHouseholdHeadPhotoIdentificationDropdown = By.xpath("//div[@id='mui-component-select-idCardType']")

		DropdownUtils.selectDropDownOption(
				chpHouseholdHeadPhotoIdentificationDropdown,
				"Driver’s license",
				TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[@for='idCardNumber' and contains(normalize-space(.), '*') and contains(normalize-space(.), 'Identification Number')]",
				"* Identification Number")
		By chpHouseholdHeadIdNumber = By .xpath("//input[@id='idCardNumber']")

		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseholdHeadIdNumber,
				"regex:.+",
				"MD-123456")

		VerifyUtils.verifyTextByXPath("//label[@for='idCardIssueAuthority' and normalize-space()='Who issued this identification? (example: State of Maryland, United States, etc.)']",
				"Who issued this identification? (example: State of Maryland, United States, etc.)")
		By chpHouseholdHeadIdIssueAuthority = By.xpath("//input[@id='idCardIssueAuthority']")
		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseholdHeadIdIssueAuthority,
				"regex:.+",
				"State of Maryland")


		String chpHouseholdHeadIdDateQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Does this proof of ID have an issue date and expiration date?']]"

		String chpHouseholdHeadIdDateNoXpath = chpHouseholdHeadIdDateQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='No']"

		String chpHouseholdHeadIdDateNoSelectedXpath = chpHouseholdHeadIdDateQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='No']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadIdDateNoXpath,
				chpHouseholdHeadIdDateNoSelectedXpath,
				"No")

		/*VerifyUtils.verifyTextByXPath(
		 "//label[@class='clsFormDateLabel' and normalize-space(.)='* Issue Date']",
		 "* Issue Date")
		 DatePickerUtils.selectNextDayByFieldLabel(driver, wait, "* Issue Date")
		 VerifyUtils.verifyTextByXPath(
		 "//label[@class='clsFormDateLabel' and normalize-space(.)='* Expiration Date']",
		 "* Expiration Date")
		 DatePickerUtils.selectNextDayByFieldLabel(driver, wait, "* Expiration Date")*/

		/*VerifyUtils.verifyTextByXPath(
		 "//p[@class='doc-head' and .//span[normalize-space()='*'] and .//span[normalize-space()='Upload Proof of Identity']]",
		 "* Upload Proof of Identity ")
		 String hhProofIdentityFile = RunConfiguration.getProjectDir() + "/Data Files/HouseholdHeadProofOfIdentity.pdf"
		 DataFileUploadUtils.uploadDocumentBySectionLabel(
		 driver,
		 "Upload Proof of Identity",
		 hhProofIdentityFile,
		 TIMEOUT)*/

		VerifyUtils.verifyTextByXPath(
				"//p[@class='doc-head' and .//span[normalize-space()='*'] and .//span[normalize-space()='Upload Proof of Identity']]",
				"* Upload Proof of Identity "
				)

		String sectionLabel = "Upload Proof of Identity"
		String hhProofIdentityFile = RunConfiguration.getProjectDir() + "/Data Files/HouseholdHeadProofOfIdentity.pdf"

		if (DataFileUploadUtils.isDocumentAlreadyUploaded(driver, sectionLabel, TIMEOUT)) {
			WebUI.comment("Proof of Identity file already uploaded. Skipping upload.")
		} else {
			DataFileUploadUtils.uploadDocumentBySectionLabel(
					driver,
					sectionLabel,
					hhProofIdentityFile,
					TIMEOUT
					)
		}

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)
		addChpHouseHoldHeadInsurance()
	}

	static void addChpHouseHoldHeadInsurance() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)



		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Insurance']", "Insurance")

		VerifyUtils.verifyTextByXPath("//span[normalize-space()='Fields marked with * are mandatory']", "Fields marked with * are mandatory")

		String chpHouseholdHeadHealthInsuranceQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Does this adult have health insurance?']]"

		String chpHouseholdHeadHealthInsuranceYesXpath = chpHouseholdHeadHealthInsuranceQuestion  + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpHouseholdHeadHealthInsuranceSelectedYesXpath = chpHouseholdHeadHealthInsuranceQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadHealthInsuranceYesXpath,
				chpHouseholdHeadHealthInsuranceSelectedYesXpath,
				"Yes")

		/*VerifyUtils.verifyTextByXPath(
		 "//label[.//span[normalize-space()='*'] and .//span[normalize-space()='Select type of insurance']]",
		 "* Select type of insurance")
		 By chpHouseHoldHeadInsuranceDropdown = By.xpath("//div[@id='mui-component-select-insuranceCompanyName']")
		 DropdownUtils.selectDropDownOption(chpHouseHoldHeadInsuranceDropdown, "Insurance paid out of pocket", TIMEOUT)
		 CloseDropdownUtils.closeMuiDropdown(driver, wait)
		 By chpHouseholdHeadInsuranceId = By.xpath("//input[@id='InsuranceID']")
		 FormFieldUtils.populateOrAssertTextField(
		 driver,
		 chpHouseholdHeadInsuranceId,
		 "regex:.+",
		 "OutOfPocketInsurance-123")
		 ButtonClickUtils.clickEnabledSaveNext(driver, wait)*/
		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='*'] and .//span[normalize-space()='Select type of insurance']]",
				"* Select type of insurance"
				)

		WebUI.delay(15)
		By chpHouseHoldHeadInsuranceDropdown = By.xpath(
				"//label[.//span[normalize-space()='Select type of insurance']]" +
				"/following::div[@role='button' and @aria-haspopup='listbox'][1]")

		MuiDropdownUtils.selectMuiDropdownOption(
				driver,
				wait,
				chpHouseHoldHeadInsuranceDropdown,
				"Insurance paid out of pocket")

		By chpHouseholdHeadInsuranceId = By.xpath("//input[@id='InsuranceID']")

		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseholdHeadInsuranceId,
				"regex:.+",
				"OutOfPocketInsurance-123")

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)

		addChpHouseHoldHeadDisabilityInfo()
	}

	static void addChpHouseHoldHeadDisabilityInfo() {

		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)



		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Disability']", "Disability")

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Fields marked with * are mandatory']",
				"Fields marked with * are mandatory")

		String chpHouseholdHeadDisabilityQuestionXpath =
				"//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Are you disabled?']]"

		String chpHouseholdHeadDisabilityYesXpath = chpHouseholdHeadDisabilityQuestionXpath + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpHouseholdHeadDisabilitySelectedYesXpath = chpHouseholdHeadDisabilityQuestionXpath + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadDisabilityYesXpath,
				chpHouseholdHeadDisabilitySelectedYesXpath,
				"Yes")


		String chpHouseholdHeadBlindQuestionXpath =
				"//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Are you blind?']]"

		String chpHouseholdHeadBlindNoXpath = chpHouseholdHeadDisabilityYesXpath + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='No']"

		String chpHouseholdHeadBlindNoSelectedXpath = chpHouseholdHeadDisabilitySelectedYesXpath + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='No']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadBlindNoXpath,
				chpHouseholdHeadBlindNoSelectedXpath,
				"No")

		String chpHouseholdHeadAccommodationsQuestionXpath =
				"//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Do you need any accommodations?']]"

		String chpHouseholdHeadAccommodationsYesXpath = chpHouseholdHeadAccommodationsQuestionXpath + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpHouseholdHeadAccommodationsSelectedYesXpath = chpHouseholdHeadAccommodationsQuestionXpath + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadAccommodationsYesXpath,
				chpHouseholdHeadAccommodationsSelectedYesXpath,
				"Yes")

		WebUI.delay(15)

		By chpHouseholdHeadAccommodationOption = By.xpath("//div[@id='mui-component-select-accommodationType']")
		RaceUtils.clearAndSelectTwoOrThreeRaceOptions(chpHouseholdHeadAccommodationOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)
		addChpHouseHoldHeadMoreInfo()
	}

	static void addChpHouseHoldHeadMoreInfo() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='More Info']", "More Info")

		TestObject mandatoryMessage = new TestObject('mandatoryMessage')
		mandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']"
				)


		VerifyUtils.verifyTextByXPath("//label[.//span[normalize-space()='*'] and .//span[normalize-space()='What is your marital status?']]",
				"* What is your marital status?")

		By chpHouseholdHeadMarriageStatusDropdown = By.xpath("//div[@id='mui-component-select-familyStatusCode']")

		DropdownUtils.selectDropDownOption(chpHouseholdHeadMarriageStatusDropdown, "Married", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath("//label[normalize-space()='What is your United States military status?']",
				"What is your United States military status?")
		By chpHouseholdHeadVeteranStatusDropdown = By.xpath("//div[@id='mui-component-select-veteranStatusCode']")

		DropdownUtils.selectDropDownOption(chpHouseholdHeadVeteranStatusDropdown, "Reserve", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		String chpHouseholdHeadCountyHealthProgramsQuestion =  "//p[normalize-space()='Have you ever received a benefit through County Health Programs?']"

		String chpHouseholdHeadSchoolEnrollmentYesXpath = chpHouseholdHeadCountyHealthProgramsQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpHouseholdHeadSchoolEnrollmentSelectedYesXpath = chpHouseholdHeadCountyHealthProgramsQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpHouseholdHeadSchoolEnrollmentYesXpath,
				chpHouseholdHeadSchoolEnrollmentSelectedYesXpath,
				"Yes")

		VerifyUtils.verifyTextByXPath("//label[@id='nameBenefitUnder-label']", "Under what name?")

		By chpHouseholdHeadBenefitName = By.xpath("//input[@id='nameBenefitUnder']")

		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseholdHeadBenefitName,
				"regex:.+",
				"HouseholdFirst HouseholdLast")

		By chpHouseholdHeadMoreInfoSaveBtn = By.xpath("//button[@type='button' and .//span[normalize-space()='Save']]");
		ClickUtils.waitAndClick(chpHouseholdHeadMoreInfoSaveBtn, TIMEOUT)
		WebUI.delay(5)

		By chpHouseholdHeadMoreInfoYesBtn = By.xpath("//button[normalize-space()='Yes']")
		ClickUtils.waitAndClick(chpHouseholdHeadMoreInfoYesBtn, TIMEOUT)
	}
	//}

	// -------------------------
	// Helper Reusable Methods
	// -------------------------

	private static String generateRandomSSN() {
		Random random = new Random()
		int ssnFirstPart = 100 + random.nextInt(900)
		int ssnSecondPart = 10 + random.nextInt(90)
		int ssnThirdPart = 1000 + random.nextInt(9000)
		return "${ssnFirstPart}-${ssnSecondPart}-${ssnThirdPart}"
	}
	private static final Random randomPhoneNum = new Random()
	private static final List<String> MOBILE_NUMBERS = [
		"301-709-8765",
		"202-760-3456",
		"610-772-3456"
	]

	private static String generateRandomMobile() {
		return MOBILE_NUMBERS[randomPhoneNum.nextInt(MOBILE_NUMBERS.size())]
	}

	private static final Random randomEmailAddress = new Random()

	private static final String[] EMPLOYER_EMAILS = [
		"qiru.zhu+seleniumautoversion6@hotmail.com",
		"qiru.zhu+seleniumautoversion6@hotmail.com",
		"qiru.zhu+seleniumautoversion6@hotmail.com",
		"qiru.zhu+seleniumautoversion6@hotmail.com",
		"qiru.zhu+seleniumautoversion6@hotmail.com",
		"qiru.zhu+seleniumautoversion6@hotmail.com"
	]

	private static String generateRandomEmail() {
		return EMPLOYER_EMAILS[randomEmailAddress.nextInt(EMPLOYER_EMAILS.length)]
	}
}