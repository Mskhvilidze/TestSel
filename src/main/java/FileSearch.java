import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class FileSearch {
    public static final String PATH = "E:\\data\\zugferd\\destination";
    public static final String DEST = "C:\\z\\createZugferdInvoice\\";
    private static final String fileBatPath = "C:\\z\\createZugferdInvoice\\create_Zugferd_20221019.bat";

    private FileSearch() {

    }

    private static void move(String path) {
        try {
            File file = new File(path);
            Files.copy(file.toPath(), new FileOutputStream(DEST + "ZUGFeRD-invoice.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFILENAME(String client, String number, String invoice, String datum) {
        return client + "_" + number + "_" + invoice + "_" + "20" + datum + "-030.xml";
    }

    public static boolean fileSearch(String client, String number, String invoice, String datum) {
        System.out.println("Searched file: " + getFILENAME(client, number, invoice, datum));
        File file = new File(PATH);
        File[] files = file.listFiles();
        File removeFile = new File(DEST + "ZUGFeRD-invoice.xml");
        if (removeFile.exists()) {
            System.out.println("File removed");
            removeFile.delete();
        }

        for (int i = files.length - 1; i >= 0; i--) {
            if (files[i].getName().equals(getFILENAME(client, number, invoice, datum))) {
                move(files[i].getPath());
                System.out.println("SUCCESSFUL");
                return true;
            }
        }
        return false;
    }

    public static void batchReadAndWrite(String filename) throws IOException {
        FileOutputStream writer = null;
        try {
            List<String> list = Files.readAllLines(Paths.get(fileBatPath), StandardCharsets.UTF_8);
            list.add(0, list.get(0).split("=")[0] + "=" + filename);
            list.remove(1);
            StringBuilder builder = new StringBuilder();
            writer = new FileOutputStream(new File(fileBatPath));
            for (String text : list) {
                builder.append(text + System.lineSeparator());
            }
            writer.write(builder.toString().getBytes());
        } catch (Exception e) {
            System.err.println("Batch kann nicht gelesen werden!");
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    public static boolean removeFilePDF(String filename){
        File file = new File(DEST + filename);
        return file.exists() ? file.delete() : false;
    }
}
