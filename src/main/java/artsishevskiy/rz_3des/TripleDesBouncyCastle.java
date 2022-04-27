package artsishevskiy.rz_3des;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TripleDesBouncyCastle {
    private static final String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/PKCS7Padding";
    private static final String ALGORITHM = "DESede";
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    private static void init()
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encode(String input, byte[] key)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException {

        init();
        SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher encrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
        encrypter.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        return new String(Base64.encodeBase64(encrypter.doFinal(inputBytes)));
    }

    public static String decode(String input, byte[] key)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException {

        init();
        SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher decrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
        decrypter.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] encryptedInput = Base64.decodeBase64(input.getBytes(StandardCharsets.UTF_8));

        return new String(decrypter.doFinal(encryptedInput));
    }
}