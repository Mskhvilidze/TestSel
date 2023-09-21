import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebSelenium {
    private final ExecuteBatch batch;
    private List<String> notFoundNumbers = new ArrayList<>();

    public WebSelenium() {
        this.batch = new ExecuteBatch();
    }

    public void downloadPDFFile(List<String> list, String mandant) throws InterruptedException, IOException {
        // WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("plugins.always_open_pdf_externally", true);

        chromePrefs.put("download.default_directory", FileSearch.DEST);
        options.setExperimentalOption("prefs", chromePrefs);
        //options.addArguments("--no-sandbox"); // Bypass OS security model
        //options.addArguments("--disable-dev-shm-usage");// overcome limited resource problems
        options.addArguments("--headless");
        //For Remote Dekstop
        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\svc_see\\driver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver(options);


        for (int i = 0; i < list.size(); i++) {
            driver.get(
                    "http://easyviewer:18080/easyviewer-1.0/search?system=dds&view=" + mandant + "_DMSMail&q_Mandant=" +
                            mandant + "&q_ReNr=" + list.get(i));
            //driver.get("http://easyviewer:18080/easyviewer-1.0/search?system=eex2&view=eInvoiceOriginal&q_Mandant=" + mandant +"&q_ReNr='" + list.get(i) + "'");
            Thread.sleep(5000);
            if (driver.findElement(By.xpath("/html/body/div/div[1]")).getText()
                    .equals("Die Suchanfrage wurde erfolgreich verarbeitet") ||
                    driver.findElement(By.xpath("/html/body/div/div[1]")).getText()
                            .equals("Oops, es ist ein Fehler aufgetreten!")) {
                notFoundNumbers.add("Invoice Number " + list.get(i) + " PDF Not Found");
                continue;
            }

            WebElement table = driver.findElement(By.xpath("/html/body/div[2]/table"));
            List<WebElement> rows = table.findElements(By.tagName("tr"));
            String filename = "";
            for (int j = 2; j <= rows.size(); j++) {
                System.out.println(
                        driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[" + j + "]/td/a")).getText());
                if (driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[" + j + "]/td/a")).getAccessibleName()
                        .length() > 15) {
                    if (driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[" + j + "]/td/a"))
                            .getAccessibleName().substring(
                                    driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[" + j + "]/td/a"))
                                            .getAccessibleName().length() - 3).equals("pdf")) {
                        filename = driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[" + j + "]/td/a"))
                                .getText();
                        if (FileSearch.removeFilePDF(filename)) {
                            System.out.println("Old PDF removed " + filename);
                        } else {
                            System.out.println("new File " + filename + " write");
                        }
                        driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[" + j + "]/td/a")).click();
                    }
                }
            }
            System.out.println("Filename: " + filename);
            if (FileSearch.fileSearch(mandant, getRecord(filename)[1], getRecord(filename)[2],
                    getRecord(filename)[3])) {
                FileSearch.batchReadAndWrite(filename.substring(0, filename.length() - 4));
                batch.executeBatchFile();
            } else {
                notFoundNumbers.add("Invoice Number " + list.get(i) + " XML Not Found");
            }
        }
    }

    private String[] getRecord(String record) {
        return record.split("-");
    }

    public List<String> getNotFoundNumbers() {
        return this.notFoundNumbers;
    }
}
