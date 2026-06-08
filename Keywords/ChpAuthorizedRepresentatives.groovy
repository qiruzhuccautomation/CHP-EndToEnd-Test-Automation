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
import com.kms.katalon.core.webui.driver.DriverFactory

import core.RaceUtils
import core.CheckboxUtils
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

public class ChpAuthorizedRepresentatives {
	private static final int TIMEOUT = 15
	private static int timeoutSeconds = 10

	@Keyword
	static void chpAddAuthorizedRepresentatives() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)

		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Tell us about your authorized representative(s) (If Relevant)']",
				"Tell us about your authorized representative(s) (If Relevant)")

		String chpAuthorizedRepresentativesQuestion = "//p[span[contains(@class,'labelStar')] and span[normalize-space()='Do you wish to add any authorized representatives to your application?']]"

		String chpAuthorizedRepresentativesYesXpath = chpAuthorizedRepresentativesQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpAuthorizedRepresentativesSelectedYesXpath = chpAuthorizedRepresentativesQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpAuthorizedRepresentativesYesXpath,
				chpAuthorizedRepresentativesSelectedYesXpath,
				"Yes")


		By chpAddAuthorizedRepresentativeBtn=By.xpath("//button[.//span[normalize-space()='Add Authorized Representative']]")
		ClickUtils.waitAndClick(chpAddAuthorizedRepresentativeBtn, TIMEOUT)

		VerifyUtils.verifyTextByXPath("//h4[normalize-space()='Authorized Representatives']", "Authorized Representatives")

		VerifyUtils.verifyTextByXPath("//h5[normalize-space()=\"Enter the authorized representative's information.\"]", "Enter the authorized representative's information.")


		VerifyUtils.verifyTextByXPath(
				"//div[normalize-space(.)='Fields marked with * are mandatory']",
				"Fields marked with * are mandatory")

		By chpAssistApplicationCheckbox = By.xpath(
				"(//*[contains(normalize-space(.),'I want my authorized representative to do the following:')]" +
				"/following::input[@type='checkbox'])[1]")

		By chpSpeakCountyCheckbox = By.xpath(
				"(//*[contains(normalize-space(.),'I want my authorized representative to do the following:')]" +
				"/following::input[@type='checkbox'])[2]")

		By chpReportChangesCheckbox = By.xpath(
				"(//*[contains(normalize-space(.),'I want my authorized representative to do the following:')]" +
				"/following::input[@type='checkbox'])[3]")

		CheckboxUtils.selectCheckboxIfNotSelected(chpAssistApplicationCheckbox, TIMEOUT)
		WebUI.verifyEqual(driver.findElement(chpAssistApplicationCheckbox).isSelected(), true)

		CheckboxUtils.selectCheckboxIfNotSelected(chpSpeakCountyCheckbox, TIMEOUT)
		WebUI.verifyEqual(driver.findElement(chpSpeakCountyCheckbox).isSelected(), true)

		CheckboxUtils.selectCheckboxIfNotSelected(chpReportChangesCheckbox, TIMEOUT)
		WebUI.verifyEqual(driver.findElement(chpReportChangesCheckbox).isSelected(), true)


		VerifyUtils.verifyTextByXPath("//label[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='What is your relationship with the authorized representative?']]",
				"* What is your relationship with the authorized representative?")

		By chpAuthorizedRepresentativeRelationDropdown = By.xpath("//div[@id='mui-component-select-relationshipToHHH']")
		ClickUtils.waitAndClick(chpAuthorizedRepresentativeRelationDropdown, TIMEOUT)
		By chpAuthorizedRepresentativeRelationOption = By.xpath("//ul[@role='listbox']//li[@role='option'][1]")
		ClickUtils.waitAndClick(chpAuthorizedRepresentativeRelationOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath(
				"//label[@for='firstName' " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='First Name Legally as it appears on their ID']]",
				"* First Name Legally as it appears on their ID")

		By chpRepresentativeFirstNameField = By.xpath("//input[@id='firstName']")
		String chpRepresentativeFirstName = generateRandomFirstAuthRepName()
		FormFieldUtils.populateOrAssertTextField(driver, chpRepresentativeFirstNameField, '.*', chpRepresentativeFirstName)

		VerifyUtils.verifyTextByXPath(
				"//label[@for='lastName' " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='Last Name Legally as it appears on their ID']]",
				"* Last Name Legally as it appears on their ID")
		By chpRepresentativeLastNameField = By.xpath("//input[@id='lastName']")
		String chpRepresentativeLastName = generateRandomLastAuthRepName()
		FormFieldUtils.populateOrAssertTextField(driver, chpRepresentativeLastNameField, '.*', chpRepresentativeLastName)

		By chpAuthRepSuffixDropdown = By.xpath("//div[@id='mui-component-select-suffix' and @role='button']")
		ClickUtils.waitAndClick(chpAuthRepSuffixDropdown, TIMEOUT)
		By chpAuthRepSuffixOption = By.xpath("//ul[@role='listbox']//li[@role='option'][5]")
		ClickUtils.waitAndClick(chpAuthRepSuffixOption, TIMEOUT)
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		String chpAuthorizedRepresentativesAgeQuestion = "//p[contains(@class,'input-label') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Is this person at least 18 years of age?']]"

		String chpAuthorizedRepresentativesAgeYesXpath = chpAuthorizedRepresentativesAgeQuestion + "/following::div[contains(@class,'gender')][1]//li[normalize-space()='Yes']"

		String chpAuthorizedRepresentativesAgeSelectedYesXpath = chpAuthorizedRepresentativesAgeQuestion + "/following::div[contains(@class,'gender')][1]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				chpAuthorizedRepresentativesAgeYesXpath,
				chpAuthorizedRepresentativesAgeSelectedYesXpath,
				"Yes")



		VerifyUtils.verifyTextByXPath(
				"//label[.//span[normalize-space()='Gender'] and .//span[contains(@class,'labelStar')]]",
				"* Gender")

		By chpAuthRepGenderDropdown = By.xpath("//div[@id='mui-component-select-gender']")
		ClickUtils.waitAndClick(chpAuthRepGenderDropdown, 10)
		DropdownUtils.selectDropDownOption(chpAuthRepGenderDropdown, "Male")
		CloseDropdownUtils.closeMuiDropdown(driver, wait)

		VerifyUtils.verifyTextByXPath("//p[contains(@class,'doc-head') and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Upload Proof of Identity']]",
				"* Upload Proof of Identity")

		String chpAuthRepIdentityFile = RunConfiguration.getProjectDir() + "/Data Files/ChpAuthRepIdentity.pdf"

		DataFileUploadUtils.uploadDocumentBySectionLabel(
				driver,
				"Upload Proof of Identity",
				chpAuthRepIdentityFile,
				TIMEOUT)


		By chpAuthRepSaveNextBtn = By.xpath("//button[.//span[normalize-space()='Save & Next']]")
		ClickUtils.waitAndClick(chpAuthRepSaveNextBtn, 10)
		WebUI.delay(5)

		VerifyUtils.verifyTextByXPath(
				"//label[normalize-space()='* Mobile Number']",
				"* Mobile Number")
		String chpAuthRepPhoneNumber = generateRandomMobileNumber()
		By chpAuthRepPhoneNumberField = By.xpath("//input[@id='mobilePhone' and @name='mobilePhone']")
		FormFieldUtils.populateOrAssertTextField(driver, chpAuthRepPhoneNumberField, ".*", chpAuthRepPhoneNumber)

		VerifyUtils.verifyTextByXPath(
				"//label[@id='emailAddress1-label' and .//span[contains(@class,'labelStar')] and .//span[normalize-space()='Email Address']]",
				"* Email Address")

		String chpAuthRepEmailAddress = generateRandomEmailAddress()
		By chpAuthRepEmailAddressField = By.xpath("//input[@id='emailAddress1']")
		FormFieldUtils.populateOrAssertTextField(driver, chpAuthRepEmailAddressField, ".*", chpAuthRepEmailAddress)

		By chpAuthorizedRepNextBtn = By.xpath(
				"//button[@type='button' " +
				"and contains(@class,'btn-primary') " +
				"and .//span[normalize-space()='Next']]")
		ClickUtils.waitAndClick(chpAuthorizedRepNextBtn, TIMEOUT)


		core.VerifyUtils.verifyTextByXPath(
				"//div[normalize-space()='Fields marked with * are mandatory']",
				"Fields marked with * are mandatory")

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
		address1el.sendKeys("606")

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


		/*By chpAuthRepAddressPageSaveButton = By.xpath(
		 "//button[@type='button' " +
		 "and contains(@class,'btn-primary') " +
		 "and .//span[normalize-space()='Save']]"
		 )*/

		By chpAuthRepAddressPageSaveButton = By.xpath(
				"//div[contains(@class,'nav-modal-btns')]//button[@type='button' " +
				"and contains(@class,'btn-primary') " +
				"and .//span[normalize-space()='Save']]"
				)

		ClickUtils.waitAndClick(chpAuthRepAddressPageSaveButton, TIMEOUT)

		By chpAuthRepMainSaveButton = By.xpath(
				"//button[@type='button' " +
				"and contains(@class,'btn-primary') " +
				"and .//img[@alt='Save'] " +
				"and .//span[normalize-space()='Save']]"
				)

		ClickUtils.waitAndClick(chpAuthRepMainSaveButton, TIMEOUT)

		WebUI.delay(5)

		By chpAuthRepToNextStepYesButton = By.xpath("//button[contains(@class,'ajs-ok') and normalize-space()='Yes']")
		ClickUtils.waitAndClick(chpAuthRepToNextStepYesButton, TIMEOUT)
	}

	/*static void uploadProofOfAuthRepIdentity(WebDriver driver, WebDriverWait wait, int timeoutSeconds = 15) {
	 By uploadAuthorizedRepresentativesBtn = By.xpath("//button[@type='button' and .//img[@alt='add']]")
	 ClickUtils.waitAndClick(uploadAuthorizedRepresentativesBtn, timeoutSeconds)
	 String authorizedRepresentativesIdentityFile = RunConfiguration.getProjectDir() + "/Data Files/ProofofRepresentativesIdentity.pdf"
	 By authorizedRepresentativesIdentityInputBy = By.id("contained-button-file")
	 wait.until(ExpectedConditions.presenceOfElementLocated(authorizedRepresentativesIdentityInputBy))
	 driver.findElement(authorizedRepresentativesIdentityInputBy).sendKeys(authorizedRepresentativesIdentityFile)
	 By authorizedRepresentativesIdentityConfirm = By.xpath("//button[contains(@class,'btn-primary') and @title='Upload']")
	 ClickUtils.waitAndClick(authorizedRepresentativesIdentityConfirm, timeoutSeconds)
	 WebUI.comment("File uploaded successfully")
	 }*/

	// -------------------------
	// Helper Methods
	// -------------------------

	private static void clickEnabledSaveNext(WebDriver driver, WebDriverWait wait) {
		By saveNext = By.xpath("//button[.//span[normalize-space()='Save & Next'] and not(contains(@class,'btn-disabled'))]")
		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveNext))
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn)
		try {
			btn.click()
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn)
		}
	}

	private static void clickEnabledSaveButton(WebDriver driver, WebDriverWait wait) {
		By saveButton = By.xpath("//button[contains(@class,'btn-primary') and .//span[normalize-space()='Save']]")
		WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton))
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", saveBtn)
		try {
			saveBtn.click()
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn)
		}
	}

	private static void clickNoneCustodialParentMainYesBtn(WebDriver driver, WebDriverWait wait) {
		By noneCustodialParentMainYesButton = By.xpath("//button[contains(@class,'ajs-ok') and normalize-space()='Yes']")
		WebElement noneCustodialParentMainYesBtn = wait.until(ExpectedConditions.elementToBeClickable(noneCustodialParentMainYesButton))
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", noneCustodialParentMainYesBtn)
		try {
			noneCustodialParentMainYesBtn.click()
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", noneCustodialParentMainYesBtn)
		}
	}

	By nonCustodialParentMainYesBtn = By.xpath("//button[contains(@class,'ajs-ok') and normalize-space()='Yes']")

	private static void clickWithScroll(WebDriver driver, WebDriverWait wait, By locator) {
		WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator))
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el)
		try {
			el.click()
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", el)
		}
	}

	private static void selectFirstVisibleListboxOption(WebDriver driver, WebDriverWait wait) {
		By listbox = By.xpath("//ul[@role='listbox']")
		By options = By.xpath("//ul[@role='listbox']//li[@role='option']")

		wait.until(ExpectedConditions.visibilityOfElementLocated(listbox))
		List<WebElement> opts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(options))

		WebElement firstVisibleEnabled = opts.find { WebElement el ->
			el.isDisplayed() && el.isEnabled() && !isMuiDisabled(el) && (el.getText()?.trim())
		}

		if (firstVisibleEnabled == null) {
			throw new AssertionError("No visible enabled listbox options found.")
		}

		try {
			firstVisibleEnabled.click()
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstVisibleEnabled)
		}
	}

	private static String generateRandomFirstAuthRepName() {
		String[] firstRepNames = [
			"RepFirst",
			"RepresentativeFirst",
			"CaseWorkerFirst",
			"LawyerFirst",
			"RulerFirst",
			"LawFirst"
		]
		return firstRepNames[new Random().nextInt(firstRepNames.length)]
	}

	private static String generateRandomLastAuthRepName() {
		String[] lastRepNames = [
			"RepLast",
			"RepresentativeLast",
			"CaseWorkerLast",
			"LawyerLast",
			"RulerLast",
			"LawLast"
		]
		return lastRepNames[new Random().nextInt(lastRepNames.length)]
	}


	private static String generateRandomSocialSecurityNumber() {
		List<String> childSsns = ["615-67-8765", "987-76-3456"]
		return childSsns[new Random().nextInt(childSsns.size())]
	}

	static String generateRandomMobileNumber() {
		Random random = new Random()
		int prefix = 200 + random.nextInt(700)
		int middle = 100 + random.nextInt(900)
		int last = 1000 + random.nextInt(9000)

		return "${prefix}-${middle}-${last}"
	}

	/*private static String generateRandomMobileNumber() {
	 List<String> authRepMobileNumber = [
	 "301-906-8765",
	 "202-769-3456"
	 ]
	 return authRepMobileNumber[new Random().nextInt(authRepMobileNumber.size())]
	 }*/

	private static String generateRandomEmailAddress() {
		List<String> authRepEmailAddress = [
			"legalaid123@gmail.com",
			"casehelper123@gmail.com"
		]
		return authRepEmailAddress[new Random().nextInt(authRepEmailAddress.size())]
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
