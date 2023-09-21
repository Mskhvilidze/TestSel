import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteBatch {
    public static String path = "";

    public void executeBatchFile() throws InterruptedException, IOException {
        System.out.println("Path " + path);
        String runStatus = "";
       // Process process = Runtime.getRuntime()
        //        .exec(new String[]{"cmd.exe", "/c", "start", path + "createZugferdInvoice/create_Zugferd_20221019.bat"});
        ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd.exe", "/c", "start", path + "createZugferdInvoice/create_Zugferd_20221019.bat"});
        builder.inheritIO();
        Process process = builder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null){
            System.out.println(line);
            output.append(line + "\n");
        }
        int exitVal = process.waitFor();
        if (exitVal == 0){
            System.out.println(output);
            runStatus = "SUCCESS";
        }else {
            runStatus = "ABNORMAL";
        }
        System.out.println(runStatus);
    }
}
