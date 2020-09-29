package com.guuidea.component.encrypt;

import java.util.Arrays;

public class SecurityEncryptionTest extends  SecurityEncryption{
    private static void customCode(){
        System.out.println(Arrays.toString(getEncryptVal()));
        setEncryptVal(generateBytes());
        System.out.println(Arrays.toString(getEncryptVal()));
    }

    public static void main(String[] args) {
        customCode();
    }
}
