/*
 * Copyright (C) 2011 www.itcsolutions.eu
 *
 * This file is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1, or (at your
 * option) any later version.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 *
 */

/**
 *
 * @author Catalin - www.itcsolutions.eu
 * @version 2011
 *
 */
package scaft;

/**
 *
 * @author Catalin
 */

/*
 * source
 * http://www.itcsolutions.eu/2011/08/24/how-to-encrypt-decrypt-files-in-java-with-aes-in-cbc-mode-using-bouncy-castle-api-and-netbeans-or-eclipse/
 */

/*
 * modified by KaramJabareen 07/06/2017
 */

import java.io.*;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.engines.AESEngine;

import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Base64;
import scaft.Util.Log;


/**
 * Implementation of AES
 * Bouncy Castle API installed as a library
 * CBC mode for encryption and decryption
 * @author Catalin Boja
 */

class BouncyCastleAPI_AES_CBC {
    PaddedBufferedBlockCipher encryptCipher = null;
    PaddedBufferedBlockCipher decryptCipher = null;

    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[32];              //input buffer
    byte[] obuf = new byte[1024];            //output buffer
    // The key
    byte[] key = null;
    // The initialization vector needed by the CBC mode
    byte[] IV =  null;

    // The default block size
    public static int blockSize = 16;

    public BouncyCastleAPI_AES_CBC(byte[] keyBytes, byte[] iv){
        //get the key

        key = new byte[keyBytes.length];
        System.arraycopy(keyBytes, 0 , key, 0, keyBytes.length);

        //get the IV
        IV = new byte[iv.length];
        System.arraycopy(iv, 0 , IV, 0, iv.length);
    }

    public void InitCiphers(){
        //create the ciphers
        // AES block cipher in CBC mode with padding

                encryptCipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()));




        decryptCipher =  new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()));

        //create the IV parameter
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
        ParametersWithIV parameterIV =
                new ParametersWithIV(new KeyParameter(key),ivParameterSpec.getIV());


        encryptCipher.init(true, parameterIV);
        decryptCipher.init(false, parameterIV);
    }

    public void ResetCiphers() {
        if(encryptCipher!=null)
            encryptCipher.reset();
        if(decryptCipher!=null)
            decryptCipher.reset();
    }

    public byte[] CBCEncrypt(byte[] in) throws Exception {
        return cipherData(encryptCipher, in);
        /*
        byte[] encryptedData = new byte[encryptCipher.getOutputSize(in.length)];
        int noOfBytes = encryptCipher.processBytes(in, 0, in.length, encryptedData, 0);

        byte[] encryptedConfigData = null;
        int last=encryptCipher.doFinal(encryptedData, 0);

        byte[] plain = new byte[noOfBytes + last];
        System.arraycopy(encryptedData, 0, plain, 0, plain.length);

        return plain;*/

        /*
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write("Salted__".getBytes());
            bos.write(salt);
            bos.write(encryptedData);
            encryptedConfigData = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (encryptedConfigData != null) {
            encryptedData = Base64.encode(encryptedConfigData);
            return new String(encryptedData);
        }

         */
    }


    public byte[] CBCDecrypt(byte[] in) throws Exception {
        return cipherData(decryptCipher, in);
        /*
        byte[] decryptedData = new byte[decryptCipher.getOutputSize(in.length)];
        int noOfBytes = decryptCipher.processBytes(in, 0, in.length, decryptedData, 0);

        int last= decryptCipher.doFinal(decryptedData,0);

        byte[] plain = new byte[noOfBytes + last];
        System.arraycopy(decryptedData, 0, plain, 0, plain.length);

        return plain;*/
    }

    private  byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data)
            throws Exception {
        int minSize = cipher.getOutputSize(data.length);
        byte[] outBuf = new byte[minSize];
        int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
        int length2 = cipher.doFinal(outBuf, length1);
        int actualLength = length1 + length2;
        byte[] result = new byte[actualLength];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

}
/*
class Test{
    public static void main(String[] args){
        String iv = "0123456789123456";
        BouncyCastleAPI_AES_CBC bc =
                new BouncyCastleAPI_AES_CBC
                        ((iv+iv).getBytes(),iv.getBytes());
        bc.InitCiphers();

        try {
            byte[] e=bc.CBCEncrypt("Karam kasdjhsdd fd,fg .,sdf gl/d f/jkl g'landg; d,n sdg d hglaikdfjgbsm dhslig h, nbkfd sfghldfg fgkdfbgs gkdfg  fgh".getBytes());

            System.out.println(new String(e));

            byte[] c = bc.CBCDecrypt(e);
            System.out.println(new String(c));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
*/
