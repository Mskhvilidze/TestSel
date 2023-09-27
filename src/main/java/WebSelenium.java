import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebSelenium {
    private final ExecuteBatch batch;
    private final List<String> notFoundNumbers = new ArrayList<>();
    private static final int BUFFER_SIZE = 4096;

    public WebSelenium() {
        this.batch = new ExecuteBatch();
    }

    public void downloadPDFFile(List<String> list, String mandant) {
        InputStream inputStream;
        HttpURLConnection httpConn;
        URL url;
        FileOutputStream outputStream;
        for (String s : list) {
            try {
                url =
                        new URL("http://easyviewer:18080/easyviewer-1.0/search?system=eex2&view=eInvoiceOriginal&q_Mandant=" +
                                mandant + "&q_ReNr=%27" + s + "%27&mimeType=application/pdf");
                System.out.println("URL: " + url.toURI());
                httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();
                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10,
                                    disposition.length() - 1);
                        }
                        System.out.println("Content-Type = " + contentType);
                        System.out.println("Content-Disposition = " + disposition);
                        System.out.println("Content-Length = " + contentLength);
                        if (fileName.endsWith(".PDF") || fileName.endsWith(".pdf")) {
                            fileName = fileName.substring(0, fileName.length() - 4);
                        }
                        System.out.println("fileName = " + fileName);
                        //Wenn die Datei existiert, dann lösche
                        if (FileSearch.removeFilePDF(fileName)) {
                            System.out.println("Old PDF removed " + fileName);
                        } else {
                            System.out.println("new File " + fileName + " write");
                        }
                        // opens input stream from the HTTP connection
                        inputStream = httpConn.getInputStream();
                        String saveFilePath =
                                FileSearch.DEST + File.separator + fileName +
                                        ".pdf";
                        // opens an output stream to save into file
                        outputStream = new FileOutputStream(saveFilePath);
                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        outputStream.close();
                        inputStream.close();
                        System.out.println("File downloaded");
                        httpConn.disconnect();
                        if (FileSearch.fileSearch(mandant, getRecord(fileName)[1], getRecord(fileName)[2],
                                getRecord(fileName)[3])) {
                            fileName = fileName + ".pdf";
                            FileSearch.batchReadAndWrite(fileName.substring(0, fileName.length() - 4));
                            batch.executeBatchFile();
                        } else {
                            notFoundNumbers.add("Invoice Number " + s + " XML Not Found");
                        }
                    } else {
                        System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                        notFoundNumbers.add(
                                "No file to download " + s + " Server replied HTTP code " + responseCode);
                    }
                }
            } catch (IOException | URISyntaxException | InterruptedException e) {
                e.printStackTrace();
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
