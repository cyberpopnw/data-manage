package com.bastion.cyber.utils;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

import static com.bastion.cyber.constant.Global.MESSAGE_PREFIX;
import static com.bastion.cyber.constant.Global.PERSONAL_MESSAGE_PREFIX;

/**
 * 以太坊签名消息校验工具
 */
public class Web3Util {


    /**
     * 对签名消息，原始消息，账号地址三项信息进行认证，判断签名是否有效
     *
     * @param signature
     * @param nonce
     * @param address
     * @return
     */
    public static boolean validate(String signature, String nonce, String address) {
        //参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
        // eth_sign
        // The sign method calculates an Ethereum specific signature with:
        //    sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message))).
        //
        // By adding a prefix to the message makes the calculated signature recognisable as an Ethereum specific signature.
        // This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to
        // impersonate the victim.
        String message = MESSAGE_PREFIX + nonce;
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        SignatureData sd = new SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);

                if (addressRecovered.equals(address)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }
}