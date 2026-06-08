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

import internal.GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webui.driver.DriverFactory
import java.util.List
import java.time.Duration
import java.util.Random

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



public class ChpJobIncomeAndOtherIncomeSource {
	private static final int TIMEOUT = 15
	private static int timeoutSeconds = 10

	@Keyword
	static Void chpAddJobIncome() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		VerifyUtils.verifyTextByXPath(
				"//h3[normalize-space()='Tell us about your household Income']",
				"Tell us about your household Income")

		TestObject chpJobIncomeMandatoryMessage = new TestObject('Household Job Income mandatory message')

		chpJobIncomeMandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//span[normalize-space(.)='Fields marked with * are mandatory']")

		WebUI.waitForElementVisible(chpJobIncomeMandatoryMessage, 15)

		WebUI.verifyElementText(
				chpJobIncomeMandatoryMessage,
				'Fields marked with * are mandatory')


		String householdJobIncomeQuestion = "//p[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Do you or anyone in your household have income from one or more jobs?']]"

		String householdJobIncomeYesXpath = householdJobIncomeQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdJobIncomeSelectedYesXpath = householdJobIncomeQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdJobIncomeYesXpath,
				householdJobIncomeSelectedYesXpath,
				"Yes")


		By addJobIncomeBtn = By.xpath("//button[@type='button' and contains(@class,'add-btn') and normalize-space()='Add Job Income']")
		ClickUtils.waitAndClick(addJobIncomeBtn, timeoutSeconds)

		TestObject chpEmploymentMandatoryMessage = new TestObject('Household Employment mandatory message')

		chpEmploymentMandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		WebUI.verifyElementText(
				chpEmploymentMandatoryMessage,
				'Fields marked with * are mandatory')

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Choose the household member for whom you wish to enter employment information.']",
				"* Choose the household member for whom you wish to enter employment information.")

		By employmentInformationDropdown = By.xpath("//div[@id='mui-component-select-memberName']")
		ClickUtils.waitAndClick(employmentInformationDropdown, TIMEOUT)

		By firstMemberOption = By.xpath("//ul[@role='listbox']//li[@role='option'][1]")
		ClickUtils.waitAndClick(firstMemberOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Employer Name']",
				"* Employer Name")

		String employerName = generateEmployerName()
		By employerNameField = By.xpath("//input[@id='employerName']")
		FormFieldUtils.populateOrAssertTextField(driver, employerNameField, ".*", employerName)


		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Employer Phone Number']",
				"* Employer Phone Number")

		String employerPhoneNumber = generateRandomMobile()
		By employerPhoneNumberField = By.xpath("//input[@id='workPhone']")
		FormFieldUtils.populateOrAssertTextField(driver, employerPhoneNumberField, ".*", employerPhoneNumber)


		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='Employer Email Address']",
				"Employer Email Address")

		String employerEmailAddress = generateRandomEmail()
		By employerEmailAddressField = By.xpath("//input[@id='emailAddress1']")
		FormFieldUtils.populateOrAssertTextField(driver, employerEmailAddressField, ".*", employerEmailAddress)
		//clickEnabledSaveNext(driver, wait)
		ButtonClickUtils.clickEnabledSaveNext(driver, wait)


		//Employer Address
		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Address Line 1']",
				"* Address Line 1")

		By addressLine1Field = By.xpath("//input[@name='addressLine1' or @id='addressLine1']")
		WebElement address1el = wait.until(ExpectedConditions.elementToBeClickable(addressLine1Field))
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", address1el)

		address1el.click()
		address1el.sendKeys(Keys.chord(Keys.CONTROL, "a"))
		address1el.sendKeys(Keys.DELETE)
		address1el.sendKeys("2345")

		By firstAddressAutoFillOption = By.xpath("(//div[contains(@class,'pac-item') or @role='option' or self::li][normalize-space()!=''])[1]")
		WebElement firstOption = wait.until(ExpectedConditions.elementToBeClickable(firstAddressAutoFillOption))
		firstOption.click()

		By cityField = By.xpath("//input[@id='city']")
		By stateField = By.xpath("//input[@id='state']")
		By zipCodeField = By.xpath("//input[@id='zipCode']")

		wait.until {
			driver.findElement(cityField).getAttribute("value")?.trim() &&
					driver.findElement(stateField).getAttribute("value")?.trim() &&
					driver.findElement(zipCodeField).getAttribute("value")?.trim()
		}

		assert driver.findElement(cityField).getAttribute("value").trim() != ""
		assert driver.findElement(stateField).getAttribute("value").trim() != ""
		assert driver.findElement(zipCodeField).getAttribute("value").trim() != ""

		By saveAndNextButton = By.xpath("(//button[.//span[normalize-space()='Save & Next'] and not(@disabled)])[last()]")
		ClickUtils.waitAndClick(saveAndNextButton, TIMEOUT)
		//Employment
		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Employment Type']",
				"* Employment Type")

		By employmentTypeDropdown = By.xpath("//div[@id='mui-component-select-employementType']")
		DropdownUtils.selectDropDownOption(employmentTypeDropdown, "Full-Time", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		//VerifyUtils.verifyTextByXPath("//label[normalize-space()='* Approximately when did the household member begin working at this job? (You must provide a date if the start date is in the future.)']",
		//"* Approximately when did the household member begin working at this job? (You must provide a date if the start date is in the future.)")

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Approximately when did the household member begin working at this job? (You must provide a date if the start date is in the future.)']",
				"* Approximately when did the household member begin working at this job? (You must provide a date if the start date is in the future.)")

		By jobStartDateField = By.xpath("//input[@id='date-picker-dialog']")
		DateOfBirthUtils.enterDateDirectly(jobStartDateField, "07/10/2020", TIMEOUT)

		/*BirthDateUtils.selectDateFromCalendar(
		 driver,
		 wait,
		 "//label[contains(normalize-space(.), 'Approximately when did the household member begin working at this job?')]",
		 By.xpath("//button[@type='button' and contains(@aria-label,'Approximately when did the household member begin working at this job?')]"),
		 By.xpath("//input[@id='date-picker-dialog']"),
		 "* Approximately when did the household member begin working at this job? (You must provide a date if the start date is in the future.)",
		 "2020",
		 "July 2020",
		 "10",
		 TIMEOUT)*/


		VerifyUtils.verifyTextByXPath(
				"//p[normalize-space()='* Is the household member still working at this job?']",
				"* Is the household member still working at this job?")

		String householdStillWorkingQuestion = "//p[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Is the household member still working at this job?']]"

		String householdStillWorkingYesXpath = householdStillWorkingQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String householdStillWorkingSelectedYesXpath = householdStillWorkingQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"


		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				householdStillWorkingYesXpath,
				householdStillWorkingSelectedYesXpath,
				"Yes")

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* How many hours does the household member work at this job each week?']",
				"* How many hours does the household member work at this job each week?")

		By weeklyWorkHours = By.xpath("//input[@id='hours-input']")

		FormFieldUtils.populateOrAssertTextField(driver, weeklyWorkHours, '.*', "40")

		VerifyUtils.verifyTextByXPath("//label[.//span[normalize-space()='*'] and .//span[normalize-space()='How often is the household member paid?']]", "* How often is the household member paid?")

		By payingScheduleDropdown = By.xpath("//div[@id='mui-component-select-paySchedule']")
		DropdownUtils.selectDropDownOption(payingScheduleDropdown, "Monthly", TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath("//label[.//span[normalize-space()='*'] and .//span[normalize-space()='Gross Income Amount Received Before Taxes']]", '* Gross Income Amount Received Before Taxes')

		By grossIncomeBeforeTaxes = By.xpath("//input[@id='grossIncome']")

		FormFieldUtils.populateOrAssertTextField(driver, grossIncomeBeforeTaxes, '.*', "4798.00")

		VerifyUtils.verifyTextByXPath("//p[@class='doc-head' and .//span[normalize-space()='*'] and .//span[normalize-space()='Upload Proof of Earned Income']]",
				"* Upload Proof of Earned Income")

		String proofOfEmploymentEarnedIncomeFile = RunConfiguration.getProjectDir() + "/Data Files/ChpProofOfEmploymentIncomeCopy1.pdf"

		DataFileUploadUtils.uploadDocumentBySectionLabel(
				driver,
				"Upload Proof of Earned Income",
				proofOfEmploymentEarnedIncomeFile,
				TIMEOUT)

		By employmentEarnedIncomeSaveBtn = By.xpath("(//button[@type='button' and .//span[normalize-space()='Save'] and not(@disabled)])[last()]")
		ClickUtils.waitAndClick(employmentEarnedIncomeSaveBtn, TIMEOUT)

		chpAddOtherIncomeSource()
	}

	@Keyword
	static Void chpAddOtherIncomeSource() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		/*TestObject chpOtherIncomeSourceMandatoryMessage = new TestObject('Household Employment mandatory message')
		 chpOtherIncomeSourceMandatoryMessage.addProperty(
		 'xpath',
		 ConditionType.EQUALS,
		 "//div[normalize-space(.)='Fields marked with * are mandatory']")
		 WebUI.verifyElementText(
		 chpOtherIncomeSourceMandatoryMessage,
		 'Fields marked with * are mandatory')*/

		String notJobOtherIncomeSourcesQuestion = "//p[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Do you or anyone in your household have income from one or more sources other than a job?']]"

		String notJobOtherIncomeSourcesYesXpath = notJobOtherIncomeSourcesQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String notJobOtherIncomeSourcesSelectedYesXpath = notJobOtherIncomeSourcesQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				notJobOtherIncomeSourcesYesXpath,
				notJobOtherIncomeSourcesSelectedYesXpath,
				"Yes")


		By addOtherIncomeSourceBtn = By.xpath("//button[contains(@class,'add-btn') and .//span[contains(normalize-space(), 'Add Other Income Source')]]")
		ClickUtils.waitAndClick(addOtherIncomeSourceBtn, TIMEOUT)

		TestObject OtherIncomeSourceMandatoryMessage = new TestObject('Other Income Source Mandatory message')

		OtherIncomeSourceMandatoryMessage.addProperty(
				'xpath',
				ConditionType.EQUALS,
				"//div[normalize-space(.)='Fields marked with * are mandatory']")

		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='Which household member is receiving the other income?']]",
				"* Which household member is receiving the other income?")

		By otherSourceIncomeHouseholdMemberDropdown = By.xpath("//div[@id='mui-component-select-memberType']")
		ClickUtils.waitAndClick(otherSourceIncomeHouseholdMemberDropdown, TIMEOUT)

		By otherSourceIncomeHouseholdMemberOption = By.xpath("(//ul[@role='listbox' and not(@aria-hidden='true')]//li[@role='option'])[1]")
		ClickUtils.waitAndClick(otherSourceIncomeHouseholdMemberOption, TIMEOUT)

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Income Type']",
				"* Income Type")

		By otherSourceIncomeTypeDropdown = By.xpath("//div[@id='mui-component-select-incomeSource']")
		ClickUtils.waitAndClick(otherSourceIncomeTypeDropdown, TIMEOUT)

		By otherSourceIncomeTypeOption = By.xpath("(//ul[@role='listbox' and not(@aria-hidden='true')]//li[@role='option'])[6]")
		ClickUtils.waitAndClick(otherSourceIncomeTypeOption, TIMEOUT)

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space(.)='* How often does the household member receive this income?']",
				"* How often does the household member receive this income?")

		By otherSourceIncomeFrequencyDropdown = By.xpath("//div[@id='mui-component-select-paySchedule']")
		ClickUtils.waitAndClick(otherSourceIncomeFrequencyDropdown, TIMEOUT)

		By otherSourceIncomeFrequencyOption = By.xpath("(//ul[@role='listbox' and not(@aria-hidden='true')]//li[@role='option'])[5]")
		ClickUtils.waitAndClick(otherSourceIncomeFrequencyOption, TIMEOUT)


		VerifyUtils.verifyTextByXPath(
				"//label[@for='childSupportAmount' and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='How much does the household member receive?']]",
				"* How much does the household member receive?")
		By otherSourceIncomeAmount = By.xpath("//input[@name='amount' and @type='text' and @aria-label='amount']")
		FormFieldUtils.populateOrAssertTextField(driver, otherSourceIncomeAmount, '.*', "250.00")

		VerifyUtils.verifyTextByXPath(
				"//label[contains(@class,'clsFormDateLabel') and .//span[contains(@class,'labelStar')] and contains(normalize-space(.), 'When did the household member start receiving this income?')]",
				"* When did the household member start receiving this income?")

		By jobStartDateField = By.xpath("//input[@id='date-picker-dialog']")
		DateOfBirthUtils.enterDateDirectly(jobStartDateField, "06/13/2019", TIMEOUT)


		VerifyUtils.verifyTextByXPath(
				"//p[contains(@class,'doc-head') and contains(@class,'inc_head') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Upload Proof of Unearned Income']]",
				"* Upload Proof of Unearned Income")

		//uploadProofOfUnearnedOtherSourceIncome(driver, wait, TIMEOUT)

		String proofOfOtherUnearnedIncomeFile = RunConfiguration.getProjectDir() + "/Data Files/ChpOtherSourceIncomeCopy1.pdf"

		DataFileUploadUtils.uploadDocumentBySectionLabel(
				driver,
				"Upload Proof of Unearned Income",
				proofOfOtherUnearnedIncomeFile,
				TIMEOUT)



		By unearnedOtherSourceIncomeSaveBtn = By.xpath("//button[@type='button' and contains(@class,'create-accnt') and .//span[normalize-space()='Save']]")

		ClickUtils.waitAndClick(unearnedOtherSourceIncomeSaveBtn, TIMEOUT)

		By chpIncomeMainSaveButton = By.xpath("//button[@type='button' and contains(@class,'btn-primary') and .//span[normalize-space()='Save']]")
		ClickUtils.waitAndClick(chpIncomeMainSaveButton, TIMEOUT)

		By chpIncomeMainYesButton = By.xpath("//button[contains(@class,'ajs-ok') and normalize-space()='Yes']")
		ClickUtils.waitAndClick(chpIncomeMainYesButton, TIMEOUT)
	}

	// -------------------------
	// Helper Reusable Methods
	// -------------------------
	private static void pressEsc(WebDriver driver) {
		driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE)
	}

	private static void clickWithScroll(WebDriver driver, WebDriverWait wait, By locator) {
		WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator))
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el)
		el.click()
	}

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
			"John",
			"Amy",
			"Chris",
			"Sophia",
			"Mark",
			"Lisa",
			"David",
			"Emma"
		]
		return names[new Random().nextInt(names.length)]
	}

	private static String generateEmployerName() {
		String[] employerNames = [
			"ABC Company",
			"MSG Company",
			"LMN Company",
			"MAR Company",
			"LIS Company",
			"DAV Company",
			"SOH Company"
		]
		return employerNames[new Random().nextInt(employerNames.length)]
	}

	private static final Random RANDOM = new Random()

	private static final String[] EMPLOYER_EMAILS = [
		"abcemployer@gmail.com",
		"msgemployer@gmail.com",
		"lmnemployer@gmail.com",
		"maremployer@gmail.com",
		"lisemployer@gmail.com",
		"davemployer@gmail.com",
		"sohemployer@gmail.com"
	]

	private static String generateRandomEmail() {
		return EMPLOYER_EMAILS[RANDOM.nextInt(EMPLOYER_EMAILS.length)]
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

	private static void closeMuiDropdown(WebDriver driver, WebDriverWait wait) {
		By listbox = By.xpath("//ul[@role='listbox']")

		try {
			WebElement active = driver.switchTo().activeElement()
			active.sendKeys(Keys.ESCAPE)
			wait.until(ExpectedConditions.invisibilityOfElementLocated(listbox))
			return
		} catch (Exception ignore) {
		}

		try {
			WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")))
			body.click()
			wait.until(ExpectedConditions.invisibilityOfElementLocated(listbox))
			return
		} catch (Exception ignore) {
		}

		try {
			((JavascriptExecutor) driver).executeScript("document.body.click();")
			wait.until(ExpectedConditions.invisibilityOfElementLocated(listbox))
		} catch (Exception ignore) {
		}
	}

	private static void safeClick(JavascriptExecutor js, WebDriverWait wait, By by) {
		WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(by))
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", el)
		try {
			el.click()
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", el)
		}
	}
}
