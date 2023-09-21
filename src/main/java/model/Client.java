package model;

import java.util.Arrays;

public class Client {
    private final String[] clients;
    private static final Client newInstance = new Client();

    private Client() {
        clients = new String[]{
                "AG",
                "AR", "BF", "BG", "BO", "BP", "BW", "DG", "DM", "FF", "FH", "FK", "FL", "FN", "FZ", "GA", "GB", "GC", "GD",
                "GE", "GF", "GG", "GH", "GI", "GL", "GN", "GP", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "HF", "HG", "HL",
                "HP", "HV", "HY", "KA", "KB", "KC", "KD", "KE", "KF", "KG", "KH", "KI", "KN", "KP", "KR", "KS", "KT", "KV",
                "KW", "KX", "LM", "LS", "LT", "ML", "MP", "NK", "PG", "PH", "PV", "SB", "TG", "TV", "VE", "VV", "WF", "ZP",
        };
    }

    public String[] getClients() {
        Arrays.sort(clients);
        return clients;
    }

    public static Client getInstance() {
        return newInstance;
    }
}
