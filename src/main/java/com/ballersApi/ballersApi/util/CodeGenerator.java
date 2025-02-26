package com.ballersApi.ballersApi.util;

import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerator {
    public static String generateCode() {

        // This generates a code between 111111 999999.
        int code = ThreadLocalRandom.current().nextInt(111111, 999999 + 1);

        return  String.valueOf(code);
    }
}
