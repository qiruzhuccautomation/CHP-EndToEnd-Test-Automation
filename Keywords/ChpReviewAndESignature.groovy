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


public class ChpReviewAndESignature {


	private static final int TIMEOUT = 15
	private static int timeoutSeconds = 10

	@Keyword
	static void chpAddESignature() {
		WebDriver driver = DriverFactory.getWebDriver()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT))
		JavascriptExecutor js = (JavascriptExecutor) driver
		Actions actions = new Actions(driver)


		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Application Summary']", "Application Summary")

		/*VerifyUtils.verifyTextByXPath("//p[normalize-space()='The information you provided is below. Please use the edit icon to change the information. You will be directed to the section of the application to make the necessary changes. Any changes you make will be saved. DHHS will use the income you provided to determine your eligibility.']",
		 "The information you provided is below. Please use the edit icon to change the information. You will be directed to the section of the application to make the necessary changes. Any changes you make will be saved. DHHS will use the income you provided to determine your eligibility.")*/

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Head of Household']",
				"Head of Household"
				)

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Members (Adult)']",
				"Members (Adult)"
				)

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Members (Child)']",
				"Members (Child)"
				)

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Earned income']",
				"Earned income"
				)

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Unearned income']",
				"Unearned income"
				)

		VerifyUtils.verifyTextByXPath(
				"//span[normalize-space()='Authorized Representatives']",
				"Authorized Representatives"
				)


		By chpApplicationSummarySaveAndNextBtn = By.xpath(
				"//button[@type='button' " +
				"and contains(@class,'btn-primary') " +
				"and .//span[normalize-space()='Save & Next'] " +
				"and .//img[@alt='next']]")

		ClickUtils.waitAndClick(chpApplicationSummarySaveAndNextBtn, TIMEOUT)

		VerifyUtils.verifyTextByXPath("//h3[normalize-space()='Electronic Signature']", "Electronic Signature")

		VerifyUtils.verifyTextByXPath("//div[normalize-space()='Fields marked with * are mandatory']", "Fields marked with * are mandatory")

		String applicationTruthYesXpath =
		"//div[contains(@class,'input-block') and contains(@class,'gender-block')]//div[contains(@class,'gender')]//li[normalize-space()='Yes']"

		String applicationTruthSelectedYesXpath =
		"//div[contains(@class,'input-block') and contains(@class,'gender-block')]//div[contains(@class,'gender')]//li[contains(@class,'selected') and normalize-space()='Yes']"

		YesNoSelectionUtils.selectOptionIfNotSelected(
				driver,
				wait,
				applicationTruthYesXpath,
				applicationTruthSelectedYesXpath,
				"Yes")

		VerifyUtils.verifyTextByXPath(
				"//label[@for='applicantSignature' " +
				"and .//span[contains(@class,'labelStar') and normalize-space()='*'] " +
				"and .//span[normalize-space()='Signature of Applicant']]",
				"* Signature of Applicant")

		By signatureOfChpApplicant = By.xpath("//input[@id='applicantSignature']")
		FormFieldUtils.populateOrAssertTextField(driver, signatureOfChpApplicant, ".*", 'Test Auto')



		WebElement chpApplicationSubmitBtn = wait.until(
				ExpectedConditions.elementToBeClickable(
				By.xpath("//button[@type='button' and contains(@class,'btn-primary') and .//span[normalize-space()='Submit']]")))

		assert chpApplicationSubmitBtn.isEnabled() : "Submit button should be enabled"


		WebElement ccssApplicationSaveAndExitBtn = wait.until(
				ExpectedConditions.elementToBeClickable(
				By.xpath("//button[@type='button' and contains(@class,'btn-primary') and .//span[normalize-space()='Save & Exit']]")))

		assert ccssApplicationSaveAndExitBtn.isEnabled() : "Save & Exit button should be enabled"


		WebElement ccssApplicationDeleteBtn = wait.until(
				ExpectedConditions.elementToBeClickable(
				By.xpath("//button[@type='button' and contains(@class,'btn-profilecancel') and .//span[normalize-space()='Delete']]")))

		assert ccssApplicationDeleteBtn.isEnabled() : "Delete button should be enabled"
	}

	// -------------------------
	// Helper Methods
	// -------------------------
	private static void clickIfNotSelected(WebDriver driver, WebDriverWait wait, JavascriptExecutor js, By option, By selected, String label) {
		if (driver.findElements(selected).isEmpty()) {
			WebElement el = wait.until(ExpectedConditions.elementToBeClickable(option))
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", el)
			try {
				el.click()
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", el)
			}
			wait.until(ExpectedConditions.presenceOfElementLocated(selected))
		}

		if (driver.findElements(selected).isEmpty()) {
			throw new AssertionError("ASSERTION FAILED - '${label}' not selected.")
		}
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
	private static boolean isMuiDisabled(WebElement el) {
		String classValue = el.getAttribute("class") ?: ""
		return el.getAttribute("disabled") != null || classValue.contains("Mui-disabled")
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

	private static void selectYesIfNotSelected(WebDriver driver, WebDriverWait wait, String yesXpath, String selectedXpath) {
		By yesOption = By.xpath(yesXpath)
		By yesSelected = By.xpath(selectedXpath)
		JavascriptExecutor js = (JavascriptExecutor) driver

		if (driver.findElements(yesSelected).isEmpty()) {
			WebElement yesEl = wait.until(ExpectedConditions.elementToBeClickable(yesOption))
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", yesEl)
			try {
				yesEl.click()
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", yesEl)
			}
			wait.until(ExpectedConditions.presenceOfElementLocated(yesSelected))
		}

		if (driver.findElements(yesSelected).isEmpty()) {
			throw new AssertionError("ASSERTION FAILED - 'Yes' not selected for: ${yesXpath}")
		}
	}

	private static void selectNoIfNotSelected(WebDriver driver, WebDriverWait wait, String noXpath, String selectedXpath) {
		By noOption = By.xpath(noXpath)
		By noSelected = By.xpath(selectedXpath)
		JavascriptExecutor js = (JavascriptExecutor) driver

		if (driver.findElements(noSelected).isEmpty()) {
			WebElement noEl = wait.until(ExpectedConditions.elementToBeClickable(noOption))
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", noEl)
			try {
				noEl.click()
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", noEl)
			}
			wait.until(ExpectedConditions.presenceOfElementLocated(noSelected))
		}

		if (driver.findElements(noSelected).isEmpty()) {
			throw new AssertionError("ASSERTION FAILED - 'No' not selected for: ${noXpath}")
		}
	}
}
