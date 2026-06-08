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

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.driver.DriverFactory
import internal.GlobalVariable

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


public class ChpHouseholdChildren {

	private static final By householdChildrenFirstNameField = By.id("firstName")
	private static final By householdChildrenLastNameField  = By.id("lastName")
	private static final int TIMEOUT = 15

	@Keyword
	static void addChpHouseholdChildren() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15))
		JavascriptExecutor js = (JavascriptExecutor) driver

		String householdChildMainQuestion = "//p[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Are you adding any children who are currently living in your household?']]"

		String householdChildMainYesXpath = householdChildMainQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdChildMainSelectedYesXpath = householdChildMainQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdChildMainYesXpath,
				householdChildMainSelectedYesXpath,
				"Yes")

		By addHouseholdChildBtn = By.xpath("//button[.//div[normalize-space()='Add Child']]")
		ClickUtils.waitAndClick(addHouseholdChildBtn, TIMEOUT)

		WebUI.delay(1)

		TestObject householdChildProfileMandatoryMessage = new TestObject('Household Child Profile mandatory message')

		householdChildProfileMandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		WebUI.verifyElementText(
				householdChildProfileMandatoryMessage,
				'Fields marked with * are mandatory')


		String childProgramQuestionXpath =
				"//label[contains(@style,'font-size: 14px') " +
				"and contains(@style,'font-weight: bold') " +
				"and normalize-space()='Which program(s) is this child applying for?']"

		VerifyUtils.verifyTextByXPath(
				childProgramQuestionXpath,
				"Which program(s) is this child applying for?"
				)

		By careForKidCheckbox = By.xpath("//input[@type='checkbox' and @name='isCareforkid']")

		WebElement careForKidCheckboxElement = wait.until(
				ExpectedConditions.presenceOfElementLocated(careForKidCheckbox))

		if (!careForKidCheckboxElement.isSelected()) {
			js.executeScript("arguments[0].click();", careForKidCheckboxElement)
		}

		WebUI.verifyEqual(driver.findElement(careForKidCheckbox).isSelected(), true)



		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='How is this child related to you?']]",
				"* How is this child related to you?")

		By householdChildRelationshipDropdown = By.xpath("//div[@id='mui-component-select-relationshipToHHH']")
		DropdownUtils.selectDropDownOption(householdChildRelationshipDropdown, "Child")
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		verifyAndPopulateHouseholdChildrenFirstName()
		verifyAndPopulateHouseholdChildrenLastName()
		fillSuffixDobGender()
		fillCountryOfBirthEthnicityRace()
		fillSsnLevelOfEducation()
		uploadChildBirthCertificate(15)
	}


	@Keyword
	static void verifyAndPopulateHouseholdChildrenFirstName() {
		WebDriver driver = DriverFactory.getWebDriver()

		VerifyUtils.verifyTextByXPath(
				"//label[@for='firstName']",
				"* First Name Legally as it appears on your ID")

		String childFirstName = generateRandomFirstName()
		FormFieldUtils.populateOrAssertTextField(driver, householdChildrenFirstNameField, ".*", childFirstName)
	}

	@Keyword
	static void verifyAndPopulateHouseholdChildrenLastName() {
		WebDriver driver = DriverFactory.getWebDriver()

		VerifyUtils.verifyTextByXPath(
				"//label[@for='lastName']",
				"* Last Name Legally as it appears on your ID")

		String childLastName = generateRandomLastName()
		FormFieldUtils.populateOrAssertTextField(driver, householdChildrenLastNameField, ".*", childLastName)
	}

	@Keyword
	static void fillSuffixDobGender() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10))
		JavascriptExecutor js = (JavascriptExecutor) driver

		VerifyUtils.verifyTextByXPath("//label[normalize-space()='Suffix']", "Suffix")

		By childSuffixDropdown = By.xpath("//div[@role='button' and @id='mui-component-select-suffix']")
		DropdownUtils.selectDropDownOption(childSuffixDropdown, "Jr.")
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		//Date of Birth
		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'clsFormDateLabel') and .//span[contains(@class,'labelStar')] and contains(normalize-space(.), 'Date of Birth')]",
				"* Date of Birth")

		BirthDateUtils.selectDateFromCalendar(
				driver,
				wait,
				"//label[@class='clsFormDateLabel' and .//span[normalize-space()='*'] and contains(normalize-space(.), 'Date of Birth')]",
				By.xpath("//button[@aria-label='Date of Birth']"),
				By.xpath("//input[@id='date-picker-dialog']"),
				"* Date of Birth",
				"2020",
				"June 2020",
				"10",
				TIMEOUT)


		// Gender
		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='Gender'] and .//span[contains(@class,'labelStar')]]", "* Gender")

		By childGenderDropdown = By.xpath("//div[@role='button' and contains(@id,'mui-component-select-gender')]")
		DropdownUtils.selectDropDownOption(childGenderDropdown, "Male")
		CloseDropdownUtils.closeMuiDropdown(driver, wait)
	}

	@Keyword
	static void fillCountryOfBirthEthnicityRace() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15))

		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()=\"What is this child’s country of birth?\"]]",
				"* What is this child’s country of birth?")

		By householdChildCountryDropdown = By.xpath("//input[@id='country-select-demo']")
		DropdownUtils.selectDropDownOption(householdChildCountryDropdown, "USA", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[contains(normalize-space(), \"* What is this child's ethnicity?\")]",
				"* What is this child's ethnicity?")
		//By childEthnicity = By.id("mui-component-select-ethinicity")
		By childEthnicity = By.xpath("//div[@id='mui-component-select-ethinicity']")
		DropdownUtils.selectDropDownOption(childEthnicity, "Not Hispanic or Latino")
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()=\"* What is this child's race?\"]",
				"* What is this child's race?")

		By householdChildRaceOption = By.xpath("//div[@id='mui-component-select-race']")
		RaceUtils.clearAndSelectTwoOrThreeRaceOptions(householdChildRaceOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)
	}

	//@Keyword
	/*static void fillSsnLevelOfEducation() {
	 WebDriver driver = DriverFactory.getWebDriver()
	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15))
	 JavascriptExecutor js = (JavascriptExecutor) driver
	 // Make sure no previous MUI dropdown is still open
	 new Actions(driver).sendKeys(Keys.ESCAPE).perform()
	 WebUI.delay(1)
	 String householdChildSsnQuestion = "//p[contains(@class,'input-label') and normalize-space()='Does this household member have a SSN?']"
	 String householdChildSsnYesXpath = householdChildSsnQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"
	 String householdChildSsnSelectedYesXpath = householdChildSsnQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"
	 YesNoSelectionUtils.selectOptionIfNotSelected(
	 driver,
	 wait,
	 householdChildSsnYesXpath,
	 householdChildSsnSelectedYesXpath,
	 "Yes")
	 VerifyUtils.verifyTextByXPath("//label[normalize-space()='SSN']", "SSN")
	 By householdChildSocialSecurityNumber = By.xpath("//input[@id='socialSecurityNumber']")
	 FormFieldUtils.populateOrAssertMaskedTextField(
	 driver,
	 householdChildSocialSecurityNumber,
	 "regex:\\d{3}-\\d{2}-\\d{4}",
	 generateRandomSocialSecurityNumber())
	 // Move focus out of SSN field
	 WebElement ssnInput = wait.until(
	 ExpectedConditions.presenceOfElementLocated(householdChildSocialSecurityNumber)
	 )
	 ssnInput.sendKeys(Keys.TAB)
	 // Close any dropdown/listbox that may still be open
	 new Actions(driver).sendKeys(Keys.ESCAPE).perform()
	 WebUI.delay(1)
	 // Optional: wait until MUI dropdown list disappears
	 try {
	 wait.until(ExpectedConditions.invisibilityOfElementLocated(
	 By.xpath("//ul[@role='listbox'] | //div[@role='presentation']//ul")
	 ))
	 } catch (Exception ignored) {
	 // Continue even if no dropdown was found
	 }
	 VerifyUtils.verifyTextByXPath(
	 "//label[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='What is the highest level of education that this child has?']]",
	 "* What is the highest level of education that this child has?")
	 By householdChildEducation = By.xpath("//div[@id='mui-component-select-educationLevel']")
	 DropdownUtils.selectDropDownOption(householdChildEducation, "Kindergarten")
	 CloseDropdownUtils.closeMuiDropdown(driver, wait)
	 }*/

	@Keyword
	static void fillSsnLevelOfEducation() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15))
		JavascriptExecutor js = (JavascriptExecutor) driver

		new Actions(driver).sendKeys(Keys.ESCAPE).perform()
		WebUI.delay(1)

		String householdChildSsnQuestion =
				"//p[contains(@class,'input-label') and normalize-space()='Does this household member have a SSN?']"

		String householdChildSsnYesXpath =
				householdChildSsnQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdChildSsnSelectedYesXpath =
				householdChildSsnQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdChildSsnYesXpath,
				householdChildSsnSelectedYesXpath,
				"Yes")

		VerifyUtils.verifyTextByXPath("//label[normalize-space()='SSN']", "SSN")

		By householdChildSocialSecurityNumber = By.xpath("//input[@id='socialSecurityNumber']")

		FormFieldUtils.populateOrAssertMaskedTextField(
				driver,
				householdChildSocialSecurityNumber,
				"regex:\\d{3}-\\d{2}-\\d{4}",
				generateRandomSocialSecurityNumber())

		// Important: do not call ssnInput.sendKeys(Keys.TAB)
		js.executeScript("if (document.activeElement) { document.activeElement.blur(); }")
		new Actions(driver).sendKeys(Keys.ESCAPE).perform()
		WebUI.delay(1)

		// Education
		/*VerifyUtils.verifyTextByXPath(
		 "//label[contains(@class,'input-label') " +
		 "and .//span[contains(@class,'labelStar')] " +
		 "and .//span[normalize-space()='What is the highest level of education that this child has?']]",
		 "* What is the highest level of education that this child has?")
		 // Close any previous MUI dropdown/listbox
		 new Actions(driver).sendKeys(Keys.ESCAPE).perform()
		 WebUI.delay(1)
		 By householdChildEducation = By.xpath("//div[@id='mui-component-select-educationLevel']")
		 WebElement educationDropdown = wait.until(
		 ExpectedConditions.presenceOfElementLocated(householdChildEducation))
		 js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", educationDropdown)
		 WebUI.delay(1)
		 try {
		 wait.until(ExpectedConditions.elementToBeClickable(householdChildEducation)).click()
		 } catch (Exception e) {
		 js.executeScript("arguments[0].click();", educationDropdown)
		 }
		 // Wait for the MUI dropdown list to open
		 By educationListbox = By.xpath("//ul[@role='listbox']")
		 wait.until(ExpectedConditions.visibilityOfElementLocated(educationListbox))
		 // Print available options for debugging
		 List<WebElement> educationOptions = driver.findElements(
		 By.xpath("//ul[@role='listbox']//li[@role='option']")
		 )
		 By kindergartenOption = By.xpath(
		 "//ul[@role='listbox']//li[@role='option' and contains(normalize-space(.),'Kindergarten')]"
		 )
		 WebElement kindergarten = wait.until(
		 ExpectedConditions.elementToBeClickable(kindergartenOption)
		 )
		 try {
		 kindergarten.click()
		 } catch (Exception e) {
		 js.executeScript("arguments[0].click();", kindergarten)
		 }
		 CloseDropdownUtils.closeMuiDropdown(driver, wait)*/

		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'input-label') " +
				"and .//span[contains(@class,'labelStar')] " +
				"and .//span[normalize-space()='What is the highest level of education that this child has?']]",
				"* What is the highest level of education that this child has?")

		// Close any dropdown that may still be open from Ethnicity/Race
		new Actions(driver).sendKeys(Keys.ESCAPE).perform()
		WebUI.delay(1)

		By householdChildEducation = By.xpath("//div[@id='mui-component-select-educationLevel']")

		WebElement educationDropdown = wait.until(
				ExpectedConditions.elementToBeClickable(householdChildEducation))

		js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", educationDropdown)

		DropdownUtils.selectDropDownOption(
				householdChildEducation,
				"Kindergarten",
				TIMEOUT)

		CloseDropdownUtils.closeMuiDropdown(driver, wait)
	}


	@Keyword
	static void uploadChildBirthCertificate(int timeoutSeconds = 15) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
		JavascriptExecutor js = (JavascriptExecutor) driver

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)

		/*By childSaveNextBtnDisabled = By.xpath("/html/body/div[4]/div[3]/div/div[3]/div/div[2]/div/div[2]/button[1]")
		 WebElement childSaveNextDisabledEl = wait.until(ExpectedConditions.presenceOfElementLocated(childSaveNextBtnDisabled))
		 js.executeScript("arguments[0].scrollIntoView({block:'center'});", childSaveNextDisabledEl)
		 try {
		 childSaveNextDisabledEl.click()
		 } catch(Exception e) {
		 js.executeScript("arguments[0].click();", childSaveNextDisabledEl)
		 }
		 // Click OK on validation modal if it appears
		 By okButtonBy = By.xpath("//button[normalize-space()='OK' or .//span[normalize-space()='OK']]")
		 WebElement okButtonEl = wait.until(ExpectedConditions.elementToBeClickable(okButtonBy))
		 js.executeScript("arguments[0].scrollIntoView({block:'center'});", okButtonEl)
		 okButtonEl.click()
		 // Verify Upload Birth Certificate label is visible
		 By childBirthCertificateBy = By.xpath("//*[normalize-space()='Upload Birth Certificate']")
		 WebElement childBirthCertificateEl = wait.until(ExpectedConditions.visibilityOfElementLocated(childBirthCertificateBy))
		 assert childBirthCertificateEl.isDisplayed() : "Upload Birth Certificate label is not visible"
		 // Click upload icon/button next to Upload Birth Certificate
		 By childBirthCertificateUploadButton = By.xpath("//button[@type='button' and .//img[@alt='add']]")
		 CustomKeywords.'core.ClickUtils.waitAndClick'(childBirthCertificateUploadButton, timeoutSeconds)
		 // Wait for file input in modal and upload file
		 String filePath = RunConfiguration.getProjectDir() + "/Data Files/ChildBirthCertificateCopy.pdf"
		 By fileInputBy = By.xpath("//input[@type='file']")
		 WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(fileInputBy))
		 fileInput.sendKeys(filePath)
		 // Wait for Upload button in modal to become enabled/clickable
		 By uploadConfirm = By.xpath("//button[contains(@class,'btn-primary') and @title='Upload']")
		 WebElement uploadBtn = wait.until(ExpectedConditions.elementToBeClickable(uploadConfirm))
		 js.executeScript("arguments[0].scrollIntoView({block:'center'});", uploadBtn)
		 try {
		 uploadBtn.click()
		 } catch (Exception e) {
		 js.executeScript("arguments[0].click();", uploadBtn)
		 }
		 WebUI.comment("File uploaded successfully")*/

		//INSURANCE (REQUIRED)



		TestObject householdChildrenInsuranceMandatoryMessage = new TestObject('Household Children Insurance mandatory message')

		householdChildrenInsuranceMandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		WebUI.verifyElementText(
				householdChildrenInsuranceMandatoryMessage,
				'Fields marked with * are mandatory')

		String householdChildrenHealthInsuranceQuestion =
				"//p[contains(@class,'input-label') " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='Does your child have health insurance?']]"

		String householdChildrenHealthInsuranceQuestionText =
				"* Does your child have health insurance?"

		String householdChildrenHealthInsuranceYesXpath = householdChildrenHealthInsuranceQuestion +
				"/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdChildrenHealthInsuranceSelectedYesXpath = householdChildrenHealthInsuranceQuestion +
				"/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdChildrenHealthInsuranceYesXpath,
				householdChildrenHealthInsuranceSelectedYesXpath,
				"Yes")


		String householdChildrenSelectTypeOfInsuranceLabel =
				"//label[contains(@class,'input-label') " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='Select type of insurance']]"

		VerifyUtils.verifyTextByXPath(householdChildrenSelectTypeOfInsuranceLabel,"* Select type of insurance")

		By chpHouseHoldSpouseInsuranceDropdown = By.xpath("//div[@id='mui-component-select-insuranceCompanyName']")
		DropdownUtils.selectDropDownOption(chpHouseHoldSpouseInsuranceDropdown, "Insurance through an employer", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		By chpHouseholdChildrenInsuranceId = By .xpath("//input[@id='InsuranceID']")

		FormFieldUtils.populateOrAssertTextField(driver, chpHouseholdChildrenInsuranceId, "regex:.+", "InsuranceThroughEmployer-123")


		WebUI.delay(5)

		ButtonClickUtils.clickEnabledSaveNext(driver, wait)


		// Household Children Disability(REQUIRED)
		//Disable
		String householdChildDisabilityQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Is this child disabled?']]"

		String householdChildDisabilityYesXpath = householdChildDisabilityQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdChildDisabilitySelectedYesXpath = householdChildDisabilityQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdChildDisabilityYesXpath,
				householdChildDisabilitySelectedYesXpath,
				"Yes")

		//Blind
		String householdChildBlindQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Is this child blind?']]"

		String householdChildBlindYesXpath = householdChildBlindQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdChildBlindSelectedYesXpath = householdChildBlindQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdChildBlindYesXpath,
				householdChildBlindSelectedYesXpath,
				"Yes")



		//Accommodations
		String householdChildAccommodationsQuestion = "//p[.//span[normalize-space()='*'] and .//span[normalize-space()='Does this child need any accommodations?']]"

		String householdChildAccommodationsYesXpath = householdChildAccommodationsQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdChildAccommodationsSelectedYesXpath = householdChildAccommodationsQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdChildAccommodationsYesXpath,
				householdChildAccommodationsSelectedYesXpath,
				"Yes")

		// Accommodation type (select first visible option)
		VerifyUtils.verifyTextByXPath("//label[.//span[normalize-space()='*'] and .//span[normalize-space()='What kind of accommodations does this child need?']]",
				"* What kind of accommodations does this child need?")
		By childAccommodationsTypeOption = By.xpath("//div[@id='mui-component-select-accommodationType']")
		RaceUtils.clearAndSelectTwoOrThreeRaceOptions(childAccommodationsTypeOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		// Save
		//By householdChildSaveBtn = By.xpath("//button[contains(@class,'btn-primary') and .//span[normalize-space()='Save']]")
		By householdChildSaveBtn = By.xpath("//button[@type='button' and contains(@class,'btn-primary') and .//img[@alt='save'] and .//span[normalize-space()='Save']]")
		ClickUtils.waitAndClick(householdChildSaveBtn, TIMEOUT)
		WebUI.delay(3)

		By chpStepTwoMainSaveBtn = By.xpath(
				"//button[@type='button' " +
				"and contains(@class,'btn-primary') " +
				"and .//img[@alt='next']]")
		ClickUtils.waitAndClick(chpStepTwoMainSaveBtn, TIMEOUT)

		WebUI.delay(3)
		By chpStepTwoMainYesBtn = By.xpath(
				"//button[contains(@class,'ajs-button') " +
				"and contains(@class,'ajs-ok') " +
				"and normalize-space()='Yes']")
		ClickUtils.waitAndClick(chpStepTwoMainYesBtn, TIMEOUT)
	}



	// -------------------------
	// Helper Methods
	// -------------------------
	private static WebElement waitForAnyFileInput(WebDriver driver, WebDriverWait wait) {
		List<By> uiAssortedValues = [
			By.id("contained-button-file"),
			By.cssSelector("input[type='file']#contained-button-file"),
			By.cssSelector("input[type='file']")
		]
		for (By by : uiAssortedValues) {
			try {
				return wait.until(ExpectedConditions.presenceOfElementLocated(by))
			} catch (Exception ignore) {}
		}
		throw new AssertionError("Could not locate any <input type='file'> after clicking upload button.")
	}

	private static void makeInteractable(JavascriptExecutor js, WebElement input) {
		try {
			js.executeScript(
					"arguments[0].style.display='block';" +
					"arguments[0].style.visibility='visible';" +
					"arguments[0].style.opacity=1;" +
					"arguments[0].removeAttribute('hidden');",
					input
					)
		} catch (Exception ignore) {}
	}

	private static void clickConfirmIfPresent(WebDriver driver, JavascriptExecutor js) {
		List<By> confirmBy = [
			By.xpath("//button[contains(@class,'btn-primary') and (@title='Upload' or normalize-space()='Upload')]"),
			By.xpath("//div[@role='dialog']//button[normalize-space()='Upload' or normalize-space()='OK' or normalize-space()='Confirm']")
		]
		for (By by : confirmBy) {
			List<WebElement> els = driver.findElements(by)
			if (els != null && !els.isEmpty()) {
				try {
					WebElement el = els.get(0)
					js.executeScript("arguments[0].scrollIntoView({block:'center'});", el)
					js.executeScript("arguments[0].click();", el)
				} catch (Exception ignore) {}
				return
			}
		}
	}
	private static String generateRandomFirstName() {
		String[] firstNames = [
			"childFirst",
			"childrenFirst",
			"infantFirst",
			"babyFirst",
			"childNameFirst",
			"ChildrenNameFirst",
			"infantNameFirst",
			"babyNameFirst"
		]
		return firstNames[new Random().nextInt(firstNames.length)]
	}

	private static String generateRandomLastName() {
		String[] lastNames = [
			"childLast",
			"childrenLast",
			"infantLast",
			"babyLast",
			"childNameLast",
			"ChildrenNameLast",
			"infantNameLast",
			"babyNameLast"
		]
		return lastNames[new Random().nextInt(lastNames.length)]
	}

	private static String generateRandomSocialSecurityNumber() {
		List<String> childSsns = ["615-67-8765", "987-76-3456"]
		return childSsns[new Random().nextInt(childSsns.size())]
	}
}
