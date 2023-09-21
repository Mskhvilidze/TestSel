import model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();

        map.put("A", "B");
        map.put("C", "D");
        map.put("E", "F");

        int i = 5;
        final String[] con = {""};
        map.forEach((key, value) ->{
            con[0] += key + "|";
        });
        System.out.println(con[0].substring(0, con[0].length() - 1));
    }
}
