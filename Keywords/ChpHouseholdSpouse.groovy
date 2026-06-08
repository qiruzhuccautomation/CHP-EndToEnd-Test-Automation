import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.testobject.ConditionType
import internal.GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.driver.DriverFactory

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
import core.WaitUtils

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import java.time.Duration
import java.util.Random

public class ChpHouseholdSpouse {

	private static final int TIMEOUT = 15

	@Keyword
	static void addChpHouseholdSpouse() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		VerifyUtils.verifyTextByXPath("//h3[@class='about-yourself-header' and normalize-space()='Tell us about your household']",
				"Tell us about your household")

		TestObject mandatoryMessage = new TestObject('mandatoryMessage')
		mandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		String householdAdultMainQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Are you adding any adults who are currently living in your household?']]"

		String householdAdultMainYesXpath = householdAdultMainQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdAdultMainSelectedYesXpath = householdAdultMainQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdAdultMainYesXpath,
				householdAdultMainSelectedYesXpath,
				"Yes")

		By addHouseHoldAdultBtn = By.xpath("//button[contains(@class,'add-btn') and .//div[normalize-space()='Add Adult']]")

		ClickUtils.waitAndClick(addHouseHoldAdultBtn, TIMEOUT)

		// Header / Relationship
		VerifyUtils.verifyTextByXPath("//h4[normalize-space()='Household Adults']", "Household Adults")


		/*List<String> expectedSteps = [
		 "PROFILE INFORMATION (REQUIRED)",
		 "IDENTIFICATION (REQUIRED)",
		 "INSURANCE (REQUIRED)",
		 "DISABILITY (REQUIRED)"
		 ]
		 expectedSteps.each { expectedStep ->
		 By stepLocator = By.xpath(
		 "//span[normalize-space()='" + expectedStep + "']")
		 WebElement stepElement = wait.until(
		 ExpectedConditions.visibilityOfElementLocated(stepLocator))
		 String actualStep = stepElement.getText().trim()
		 WebUI.verifyEqual(actualStep, expectedStep)
		 println "Verified step: ${actualStep}"}*/


		TestObject mandatoryMessageCopy = new TestObject('mandatoryMessageCopy')
		mandatoryMessageCopy.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")


		VerifyUtils.verifyTextByXPath(
				"//label[contains(normalize-space(), '* How is this adult related to you?')]",
				"* How is this adult related to you?")

		By relationshipDropdown = By.xpath("//div[@id='mui-component-select-relationshipToHHH']")
		DropdownUtils.selectDropDownOption(relationshipDropdown, "Spouse", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Prefix
		VerifyUtils.verifyTextByXPath("//label[normalize-space()='Prefix']", "Prefix")
		By prefixDropdown = By.xpath("//div[@id='mui-component-select-prefix']")
		DropdownUtils.selectDropDownOption(prefixDropdown, "Mrs.", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Names
		String firstName = generateRandomName()
		String lastName  = generateRandomName()

		VerifyUtils.verifyTextByXPath("//label[@id='firstName-label']", "* First Name Legally as it appears on your ID")
		By firstNameField = By.xpath("//input[@id='firstName']")
		FormFieldUtils.populateOrAssertTextField(driver, firstNameField, ".*", firstName)

		VerifyUtils.verifyTextByXPath("//label[@id='middleName-label']", "Middle Name")


		VerifyUtils.verifyTextByXPath("//label[@id='lastName-label']", "* Last Name Legally as it appears on your ID")
		By lastNameField = By.xpath("//input[@id='lastName']")
		FormFieldUtils.populateOrAssertTextField(driver, lastNameField, ".*", lastName)

		// Suffix
		VerifyUtils.verifyTextByXPath("//label[normalize-space()='Suffix']", "Suffix")
		By suffixDropdown = By.xpath("//div[@id='mui-component-select-suffix']")
		DropdownUtils.selectDropDownOption(suffixDropdown, "Sr.", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Date of Birth
		BirthDateUtils.selectDateFromCalendar(
				driver,
				wait,
				"//label[@class='clsFormDateLabel' and .//span[normalize-space()='*'] and contains(normalize-space(.), 'Date of Birth')]",
				By.xpath("//button[@aria-label='Date of Birth']"),
				By.xpath("//input[@id='date-picker-dialog']"),
				"* Date of Birth",
				"1977",
				"October 1977",
				"10",
				TIMEOUT)

		/*TestObject dobInput = new TestObject('Date of Birth input').addProperty(
		 'xpath',
		 ConditionType.EQUALS,
		 "//input[@id='date-picker-dialog']"
		 )
		 WebUI.waitForElementPresent(dobInput, 15)
		 String actualDob = WebUI.getAttribute(dobInput, 'value').trim()
		 println "Actual Date of Birth value is: '${actualDob}'"
		 WebUI.verifyMatch(actualDob, '10/10/1977', false)
		 TestObject dobFormattedText = new TestObject('DOB formatted text').addProperty(
		 'xpath',
		 ConditionType.EQUALS,
		 "//p[normalize-space(.)='(October 10, 1977)']")
		 WebUI.waitForElementVisible(dobFormattedText, 10)
		 WebUI.verifyElementVisible(dobFormattedText)*/

		/*By householdSpouseDateOfBirth = By.xpath("//input[@id='date-picker-dialog']")
		 DateOfBirthUtils.enterDateDirectly(householdSpouseDateOfBirth, "10/10/1977", TIMEOUT)*/

		// Country of Birth
		VerifyUtils.verifyTextByXPath(
				"//label[contains(normalize-space(), '* What is this adult’s country of birth?')]",
				"* What is this adult’s country of birth?")
		By countryDropdown = By.xpath("//input[@id='country-select-demo']")
		DropdownUtils.selectDropDownOption(countryDropdown, "USA", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Ethnicity
		VerifyUtils.verifyTextByXPath(
				"//label[contains(normalize-space(), \"* What is this adult's ethnicity?\")]",
				"* What is this adult's ethnicity?")
		By householdAdultEthnicityDropdown = By.xpath("//div[@id='mui-component-select-ethnicity']")
		DropdownUtils.selectDropDownOption(householdAdultEthnicityDropdown, "Not Hispanic or Latino", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Race
		VerifyUtils.verifyTextByXPath(
				"//label[contains(normalize-space(), \"* What is this adult's race?\")]",
				"* What is this adult's race?")
		By houseHoldAdultRaceOption = By.xpath("//div[@id='mui-component-select-race']")
		RaceUtils.clearAndSelectTwoOrThreeRaceOptions(houseHoldAdultRaceOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Gender
		VerifyUtils.verifyTextByXPath("//label[contains(normalize-space(), '* Gender')]", "* Gender")
		By householdAdultGenderDropdown = By.xpath("//div[@id='mui-component-select-gender']")
		DropdownUtils.selectDropDownOption(householdAdultGenderDropdown, "Female", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Marital Status
		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'input-label') and .//span[normalize-space()=\"What is this adult's marital status?\"]]",
				"What is this adult's marital status?")

		By householdAdultMaritalDropdown = By.xpath("//div[@id='mui-component-select-maritalstatusType']")

		DropdownUtils.selectDropDownOption(
				householdAdultMaritalDropdown,
				"Married",
				TIMEOUT)

		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		WebUI.delay(5)

		// SSN question + field
		VerifyUtils.verifyTextByXPath(
				"//p[normalize-space()='Does this household member have a SSN?']",
				"Does this household member have a SSN?")

		By yesOptionLocator = By.xpath("(//div[contains(@class,'gender')]//li[normalize-space()='Yes'])[3]")
		ClickUtils.waitAndClick(yesOptionLocator, TIMEOUT)

		VerifyUtils.verifyTextByXPath("//label[normalize-space()='SSN']", "SSN")
		By householdAdultSsnInput = By.xpath("//input[@id='socialSecurityNumber']")

		FormFieldUtils.populateOrAssertMaskedTextField(
				driver,
				householdAdultSsnInput,
				"regex:\\d{3}-\\d{2}-\\d{4}",
				generateRandomSSN())


		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'input-label')" +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='What is the highest level of education that this adult has?']]", "* What is the highest level of education that this adult has?")
		By householdSpouseEducationLevelDropdown = By.xpath("//div[@id='mui-component-select-graduationLevel']")
		DropdownUtils.selectDropDownOption(householdSpouseEducationLevelDropdown, "High School Diploma/GED", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		By spouseDentalCheckbox = By.xpath("//input[@type='checkbox' and @name='isSeniorDental']")

		WebElement dentalCheckboxElement = wait.until(
				ExpectedConditions.presenceOfElementLocated(spouseDentalCheckbox))

		if (!dentalCheckboxElement.isSelected()) {
			js.executeScript("arguments[0].click();", dentalCheckboxElement)
		}

		WebUI.verifyEqual(driver.findElement(spouseDentalCheckbox).isSelected(), true)

		// Save & Next
		//clickEnabledSaveNext(driver, wait)
		ButtonClickUtils.clickEnabledSaveNext(driver, wait)


		// Photo Identification
		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'input-label') and normalize-space()='Photo Identification']",
				"Photo Identification")
		By photoIdDropdown = By.xpath("//div[@id='mui-component-select-idCardType']")
		DropdownUtils.selectDropDownOption(photoIdDropdown, "Identification card", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		WebUI.delay(1)

		By householdSpouseIdNumberInput = By.xpath("//input[@id='idCardNumber']")
		FormFieldUtils.populateOrAssertTextField(driver, householdSpouseIdNumberInput, ".*", generateRandomIdNumber())

		VerifyUtils.verifyTextByXPath(
				"//label[@for='idCardIssueAuthority']",
				"Who issued this identification? (example: State of Maryland, United States, etc.)")

		By householdSpouseIssuerInput = By.xpath("//input[@id='idCardIssueAuthority']")
		FormFieldUtils.populateOrAssertTextField(driver, householdSpouseIssuerInput, ".*", generateRandomIssuer())


		String householdSpouseIdDateQuestion =
				"//p[contains(@class,'input-label') " +
				"and normalize-space()='Does this proof of ID have an issue date and expiration date?']"

		String householdSpouseIdDateYesXpath = householdSpouseIdDateQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='No']"

		String householdSpouseIdDateSelectedYesXpath = householdSpouseIdDateQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='No']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdSpouseIdDateYesXpath,
				householdSpouseIdDateSelectedYesXpath,
				"No")




		// Issue Date
		/*By issueDateInput = By.xpath("(//input[@id='date-picker-dialog'])[1]")
		 DateOfBirthUtils.enterDateDirectly(issueDateInput, "03/10/2019", TIMEOUT)
		 // Expiration Date
		 By expirationDateInput = By.xpath("(//input[@id='date-picker-dialog'])[2]")
		 DateOfBirthUtils.enterDateDirectly(expirationDateInput, "03/10/2029", TIMEOUT)*/

		// Issue Date calendar
		/*VerifyUtils.verifyTextByXPath("//label[@class='clsFormDateLabel' and normalize-space(.)='* Issue Date']", "* Issue Date")
		 // Common calendar locators
		 By calendarPopup = By.cssSelector("div.MuiDialog-root")
		 By issueSelectedDay = By.xpath("//button[contains(@class,'MuiPickersDay-daySelected')]")
		 By okButton = By.xpath("//button[.//span[normalize-space()='OK']]")
		 // Issue Date calendar
		 VerifyUtils.verifyTextByXPath(
		 "//label[@class='clsFormDateLabel' and normalize-space(.)='* Issue Date']",
		 "* Issue Date"
		 )
		 By issueDateIcon = By.xpath("//label[normalize-space(.)='* Issue Date']/following::button[1]")
		 ClickUtils.waitAndClick(issueDateIcon, TIMEOUT)
		 wait.until(ExpectedConditions.visibilityOfElementLocated(calendarPopup))
		 wait.until(ExpectedConditions.elementToBeClickable(issueSelectedDay)).click()
		 wait.until(ExpectedConditions.elementToBeClickable(okButton)).click()
		 /*By householdSpouseIdIssueDate = By.xpath("//input[@id='date-picker-dialog']")
		 DateOfBirthUtils.enterDateDirectly(householdSpouseIdIssueDate, "03/10/2019", TIMEOUT)*/

		// Expiration Date calendar -> click next day
		/*VerifyUtils.verifyTextByXPath(
		 "//label[@class='clsFormDateLabel' and normalize-space(.)='* Expiration Date']",
		 "* Expiration Date"
		 )
		 By expIcon = By.xpath("//button[@aria-label='Expiration Date']")
		 ClickUtils.waitAndClick(expIcon, TIMEOUT)
		 wait.until(ExpectedConditions.visibilityOfElementLocated(calendarPopup))
		 By expCurrentDay = By.xpath("//button[contains(@class,'MuiPickersDay-daySelected')]//p")
		 WebElement expDayEl = wait.until(ExpectedConditions.visibilityOfElementLocated(expCurrentDay))
		 int current = Integer.parseInt(expDayEl.getText().trim())
		 int next = current + 1
		 By expNextDayBtn = By.xpath("//button[contains(@class,'MuiPickersDay-day') and not(@disabled)]//p[normalize-space(.)='${next}']/ancestor::button")
		 wait.until(ExpectedConditions.elementToBeClickable(expNextDayBtn)).click()
		 wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[normalize-space()='OK']]"))).click()*/

		/*By householdSpouseIdExpirationDate = By.xpath("//input[@id='date-picker-dialog']")
		 DateOfBirthUtils.enterDateDirectly(householdSpouseIdExpirationDate, "03/10/2029", TIMEOUT)*/



		// Upload Proof of Identity
		/*VerifyUtils.verifyTextByXPath(
		 "//p[contains(@class,'doc-head') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Upload Proof of Identity']]",
		 "* Upload Proof of Identity")
		 String hhAdultProofIdentityFile = RunConfiguration.getProjectDir() + "/Data Files/HouseholdAdultProofOfIdentity.pdf"
		 DataFileUploadUtils.uploadDocumentBySectionLabel(
		 driver,
		 "Upload Proof of Identity",
		 hhAdultProofIdentityFile,
		 TIMEOUT)*/

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)


		TestObject householdSpouseInsuranceMandatoryMessage = new TestObject('Household Spouse Insurance mandatory message')

		householdSpouseInsuranceMandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		WebUI.verifyElementText(
				householdSpouseInsuranceMandatoryMessage,
				'Fields marked with * are mandatory')

		String householdSpouseHealthInsuranceQuestion =
				"//p[contains(@class,'input-label') " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='Does this adult have health insurance?']]"


		String householdSpouseHealthInsuranceYesXpath = householdSpouseHealthInsuranceQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdSpouseHealthInsuranceSelectedYesXpath = householdSpouseHealthInsuranceQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdSpouseHealthInsuranceYesXpath,
				householdSpouseHealthInsuranceSelectedYesXpath,
				"Yes")

		WebUI.delay(15)

		String selectTypeOfInsuranceLabel =
				"//label[contains(@class,'input-label') " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='Select type of insurance']]"

		VerifyUtils.verifyTextByXPath(selectTypeOfInsuranceLabel,"* Select type of insurance")

		By chpHouseHoldSpouseInsuranceDropdown = By.xpath("//div[@id='mui-component-select-insuranceCompanyName']")
		DropdownUtils.selectDropDownOption(chpHouseHoldSpouseInsuranceDropdown, "Insurance through an employer", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		/*By chpHouseholdHeadInsuranceId = By .xpath("//input[@id='InsuranceID']")
		 FormFieldUtils.populateOrAssertTextField(driver, chpHouseholdHeadInsuranceId, "regex:.+", "InsuranceThroughEmployer-123")
		 //CHP Spouse Insurance Save & Next
		 ButtonClickUtils.clickEnabledSaveNext(driver, wait)*/

		By chpHouseholdHeadInsuranceId = By.xpath("//input[@id='InsuranceID']")

		WaitUtils.waitForLoadingOverlayToDisappear(driver, 5)

		FormFieldUtils.populateOrAssertTextField(
				driver,
				chpHouseholdHeadInsuranceId,
				"regex:.+",
				"InsuranceThroughEmployer-123"
				)

		WaitUtils.waitForLoadingOverlayToDisappear(driver, 5)

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)

		// Disability / Blind / Accommodations

		String householdSpouseDisabilityQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Is this adult disabled?']]"

		String householdSpouseDisabilityYesXpath = householdSpouseDisabilityQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdSpouseDisabilitySelectedYesXpath = householdSpouseDisabilityQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdSpouseDisabilityYesXpath,
				householdSpouseDisabilitySelectedYesXpath,
				"Yes")


		String householdSpouseBlindQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Is this adult blind?']]"

		String householdSpouseBlindYesXpath = householdSpouseBlindQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdSpouseBlindSelectedYesXpath = householdSpouseBlindQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdSpouseBlindYesXpath,
				householdSpouseBlindSelectedYesXpath,
				"Yes")


		String householdSpouseAccommodationsQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Does this adult need any accommodations?']]"

		String householdSpouseAccommodationYesXpath = householdSpouseAccommodationsQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdSpouseAccommodationSelectedYesXpath = householdSpouseAccommodationsQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdSpouseAccommodationYesXpath,
				householdSpouseAccommodationSelectedYesXpath,
				"Yes")

		// Accommodation type (select first visible option)
		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* What kind of accommodations does this adult need?']",
				"* What kind of accommodations does this adult need?")



		By householdAdultAccommodationsTypeOption = By.xpath("//div[@id='mui-component-select-accommodationType']")
		RaceUtils.clearAndSelectTwoOrThreeRaceOptions(householdAdultAccommodationsTypeOption, TIMEOUT)
		//CloseDropdownUtils.closeMuiDropdown(driver, wait)


		By chpHouseholdSpouseSaveBtn = By.xpath(
				"//div[contains(@class,'nav-modal-btns')]//button[contains(@class,'btn-primary') " +
				"and not(@disabled) " +
				"and .//span[normalize-space()='Save']]")

		ClickUtils.waitAndClick(chpHouseholdSpouseSaveBtn, TIMEOUT)
		WebUI.delay(3)
	}


	/*public static void uploadProofOfIdentity(WebDriver driver, WebDriverWait wait, int timeoutSeconds = 15) {
	 By uploadBtn = By.xpath("//button[@type='button' and .//img[@alt='add']]")
	 CustomKeywords.'core.ClickUtils.waitAndClick'(uploadBtn, timeoutSeconds)
	 String identityFile = RunConfiguration.getProjectDir() + "/Data Files/testimage1.png"
	 By fileInputBy = By.id("contained-button-file")
	 wait.until(ExpectedConditions.presenceOfElementLocated(fileInputBy))
	 driver.findElement(fileInputBy).sendKeys(identityFile)
	 By uploadConfirm = By.xpath("//button[contains(@class,'btn-primary') and @title='Upload']")
	 CustomKeywords.'core.ClickUtils.waitAndClick'(uploadConfirm, timeoutSeconds)
	 WebUI.comment("File uploaded successfully")
	 }*/


	/*public static void uploadProofOfEnrollment(WebDriver driver, WebDriverWait wait, int timeoutSeconds = 15) {
	 By uploadBtn = By.xpath("//button[@type='button' and .//img[@alt='add']]")
	 CustomKeywords.'core.ClickUtils.waitAndClick'(uploadBtn, timeoutSeconds)
	 String enrollmentFile = RunConfiguration.getProjectDir() + "/Data Files/qirutest1.pdf"
	 By fileInputBy = By.id("contained-button-file")
	 wait.until(ExpectedConditions.presenceOfElementLocated(fileInputBy))
	 driver.findElement(fileInputBy).sendKeys(enrollmentFile)
	 By uploadConfirm = By.xpath("//button[contains(@class,'btn-primary') and @title='Upload']")
	 CustomKeywords.'core.ClickUtils.waitAndClick'(uploadConfirm, timeoutSeconds)
	 WebUI.comment("File uploaded successfully")
	 }*/


	// -------------------------
	// Helper Reusable Methods
	// -------------------------

	private static void clickEnabledSaveNext(WebDriver driver, WebDriverWait wait) {
		By saveNext = By.xpath("//button[.//span[normalize-space()='Save & Next'] and not(contains(@class,'btn-disabled'))]")
		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveNext))
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn)
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", btn)
	}

	private static void selectYesIfNotSelected(WebDriver driver, WebDriverWait wait, String yesXpath, String selectedXpath) {

		By yesOption = By.xpath(yesXpath)
		By yesSelected = By.xpath(selectedXpath)

		JavascriptExecutor js = (JavascriptExecutor) driver

		// If already selected → just assert
		if (!driver.findElements(yesSelected).isEmpty()) {
			println("'Yes' is already selected.")
		} else {

			println("'Yes' is not selected. Clicking it now.")

			WebElement yesEl = wait.until(ExpectedConditions.elementToBeClickable(yesOption))
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", yesEl)

			try {
				yesEl.click()
			} catch (Exception e) {
				// JS fallback (helps with MUI toggle issues)
				js.executeScript("arguments[0].click();", yesEl)
			}

			// Wait until it becomes selected
			wait.until(ExpectedConditions.presenceOfElementLocated(yesSelected))
		}

		// Final assertion
		if (driver.findElements(yesSelected).isEmpty()) {
			throw new AssertionError("ASSERTION FAILED — 'Yes' not selected for: " + yesXpath)
		}

		println("ASSERTION PASSED — 'Yes' is selected.")
	}

	private static void selectFirstVisibleListboxOption(WebDriver driver, WebDriverWait wait) {
		By options = By.xpath("//ul[@role='listbox']//li[@role='option' and not(contains(@style,'display: none'))]")
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@role='listbox']")))

		List<WebElement> opts = driver.findElements(options)
		if (opts == null || opts.isEmpty()) {
			throw new AssertionError("No visible listbox options found.")
		}
		WebElement first = opts.get(0)
		first.click()
	}

	private static String generateRandomName() {
		String[] names = [
			"Ccss",
			"Chp",
			"Rap",
			"Hip",
			"Iccs",
			"Tdi",
			"Montogmery",
			"Person"
		]
		return names[new Random().nextInt(names.length)]
	}

	private static String generateRandomSSN() {
		List<String> ssns = ["615-67-8765", "987-76-3456"]
		return ssns[new Random().nextInt(ssns.size())]
	}

	private static String generateRandomMobile() {
		List<String> phones = [
			"301-709-8765",
			"202-760-3456",
			"610-772-3456"
		]
		return phones[new Random().nextInt(phones.size())]
	}

	private static String generateRandomIdNumber() {
		List<String> ids = [
			"MD-8797676",
			"MD-76432123456"
		]
		return ids[new Random().nextInt(ids.size())]
	}

	private static String generateRandomIssuer() {
		List<String> issuers = [
			"State of Maryland",
			"United States"
		]
		return issuers[new Random().nextInt(issuers.size())]
	}

	private static void closeMuiOverlays(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver
		try {
			new Actions(driver)
					.sendKeys(Keys.ESCAPE).pause(Duration.ofMillis(150))
					.sendKeys(Keys.ESCAPE).perform()
		} catch (Exception ignore) {}

		try {
			List<WebElement> backdrops = driver.findElements(By.cssSelector(".MuiBackdrop-root"))
			if (backdrops != null && !backdrops.isEmpty()) {
				js.executeScript("arguments[0].click();", backdrops.get(0))
			}
		} catch (Exception ignore) {}

		try {
			js.executeScript("document.body.click();")
		} catch (Exception ignore) {}
	}
}
