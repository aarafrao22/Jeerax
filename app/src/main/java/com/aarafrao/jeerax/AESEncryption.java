package com.aarafrao.jeerax;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {
    private static final String SECRET_KEY = "vdhk vha] hguhlvdm 1357";
    private static final String SALTVALUE = "abcdefg";
    private static final String ALGORITHM = "AES";
    private static final String KEY = "YourSecretKeyValueHere";

    @RequiresApi(api = Build.VERSION_CODES.O)
    //generating the key dynamically using the generateKey method line 42
    public static String encryptWithAES(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESEncryption.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(encryptedByteValue);
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(AESEncryption.KEY.getBytes(), AESEncryption.ALGORITHM);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String strToEncrypt) {
        try {
            /* Declare a byte array. */
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            /* Create factory for secret keys. */
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            /* PBEKeySpec class implements KeySpec interface. */
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            /* Retruns encrypted value. */
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidAlgorithmParameterException | InvalidKeyException |
                 NoSuchAlgorithmException |
                 InvalidKeySpecException |
                 BadPaddingException |
                 IllegalBlockSizeException |
                 NoSuchPaddingException e) {
            System.out.println("Error occured during encryption: " + e.toString());
        }
        return null;
    }

}
