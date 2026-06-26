package com.sargmike228.core.security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RSAEncryptionManager @Inject constructor() {
    
    init {
        Security.addProvider(BouncyCastleProvider())
    }
    
    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }
    
    fun encryptWithPublicKey(data: ByteArray, publicKey: java.security.PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data)
    }
    
    fun decryptWithPrivateKey(encryptedData: ByteArray, privateKey: java.security.PrivateKey): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(encryptedData)
    }
}