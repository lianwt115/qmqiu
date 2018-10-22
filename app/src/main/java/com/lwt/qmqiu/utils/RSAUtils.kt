package com.lwt.qmqiu.utils


import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class RSAUtils {

    companion object {

        private  val  RSA = "RSA"

        /**
         * 随机生成RSA密钥对
         *
         * @param keyLength
         *            密钥长度，范围：512～2048<br>
         *            一般1024
         * @return
         */
        fun generateRSAKeyPair(keyLength:Int = 1024):KeyPair?{

            try
            {
                var  kpg = KeyPairGenerator.getInstance(RSA)
                kpg.initialize(keyLength)
                return kpg.genKeyPair()
            } catch (e: NoSuchAlgorithmException)
            {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 用公钥加密 <br>
         * 每次加密的字节数，不能超过密钥的长度值除以 8 再减去 11，所以采取分段加密的方式规避
         *
         * @param data 需加密数据的byte数据
         * @param publicKey 公钥
         * @return 加密后的byte型数据
         */

        fun encryptData(data:ByteArray , publicKey: PublicKey):ByteArray?{

            try
            {
                var cipher = Cipher.getInstance(RSA)
                // 编码前设定编码方式及密钥
                cipher.init(Cipher.ENCRYPT_MODE, publicKey)

                var rsaPublicKey =  publicKey as RSAPublicKey
                // 模长
                var keyLen = rsaPublicKey.getModulus().bitLength() / 8

                var maxEncryptBlock = keyLen - 11

                //如果明文长度大于模长-11则要分组加密
                var inputLen = data.size
                var out =  ByteArrayOutputStream()
                var offSet = 0
                var temp:ByteArray
                var i = 0
                // 对数据分段加密
                while (inputLen - offSet > 0) {
                    temp = if (inputLen - offSet > maxEncryptBlock) {
                        cipher.doFinal(data, offSet, maxEncryptBlock)
                    } else {
                        cipher.doFinal(data, offSet, inputLen - offSet)
                    }
                    out.write(temp, 0, temp.size)
                    i++
                    offSet = i * maxEncryptBlock
                }
                var encryptedData = out.toByteArray()
                out.close()
                // 传入编码数据并返回编码结果
                return encryptedData
            } catch ( e:Exception)
            {
                e.printStackTrace()
                return null
            }
        }


        /**
         * 用私钥解密
         *
         * @param encryptedData 经过encryptedData()加密返回的byte数据
         * @param privateKey 私钥
         * @return
         */

        fun decryptData(encryptedData:ByteArray,privateKey: PrivateKey):ByteArray?{

            try
            {
                var cipher = Cipher.getInstance(RSA)
                cipher.init(Cipher.DECRYPT_MODE, privateKey)

                var rsaPrivateKey =  privateKey as RSAPrivateKey
                // 模长
                var keyLen = rsaPrivateKey.getModulus().bitLength() / 8
                var maxDecryptBlock = keyLen //不用减11

                //如果密文长度大于模长则要分组解密
                var inputLen = encryptedData.size
                var out =  ByteArrayOutputStream()
                var offSet = 0
                var temp:ByteArray
                var i = 0
                // 对数据分段解密
                while (inputLen - offSet > 0) {
                    if (inputLen - offSet > maxDecryptBlock) {
                        temp = cipher.doFinal(encryptedData, offSet, maxDecryptBlock)
                    } else {
                        temp = cipher.doFinal(encryptedData, offSet, inputLen - offSet)
                    }
                    out.write(temp, 0, temp.size)
                    i++
                    offSet = i * maxDecryptBlock
                }
                var decryptedData = out.toByteArray()
                out.close()

                return decryptedData
            } catch (e:Exception)
            {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
         *
         * @param keyBytes
         * @return
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         */

        fun getPublicKey(keyBytes:ByteArray):PublicKey {

            var keySpec =  X509EncodedKeySpec(keyBytes)
            var keyFactory = KeyFactory.getInstance(RSA)
            var publicKey = keyFactory.generatePublic(keySpec)
            return publicKey
        }

        /**
         * 通过私钥byte[]将私钥还原，适用于RSA算法
         *
         * @param keyBytes
         * @return
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         */

        fun getPrivateKey(keyBytes:ByteArray):PrivateKey{

            var keySpec =  PKCS8EncodedKeySpec(keyBytes)
            var keyFactory = KeyFactory.getInstance(RSA)
            var privateKey = keyFactory.generatePrivate(keySpec)
            return privateKey
        }

        /**
         * 从字符串中加载公钥
         *
         * @param publicKeyStr
         *            公钥数据字符串
         * @throws Exception
         *             加载公钥时产生的异常
         */

        fun loadPublicKey(publicKeyStr:String):PublicKey{
            try
            {





                var buffer = Base64.decode(publicKeyStr,0)
                var keyFactory = KeyFactory.getInstance(RSA)
                var keySpec =  X509EncodedKeySpec(buffer)
                return  keyFactory.generatePublic(keySpec) as RSAPublicKey
            } catch ( e:NoSuchAlgorithmException)
            {
                throw  Exception("无此算法")
            } catch ( e: InvalidKeySpecException)
            {
                throw  Exception("公钥非法")
            } catch ( e:NullPointerException)
            {
                throw Exception("公钥数据为空")
            }

        }
        /**
         * 从字符串中加载私钥<br>
         * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
         *
         * @param privateKeyStr
         * @return
         * @throws Exception
         */

        fun loadPrivateKey(privateKeyStr:String):PrivateKey{
            try
            {
                var buffer = Base64.decode(privateKeyStr,0)
                //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
                var keySpec =  PKCS8EncodedKeySpec(buffer)
                var keyFactory = KeyFactory.getInstance(RSA)
                return  keyFactory.generatePrivate(keySpec) as RSAPrivateKey
            } catch ( e:NoSuchAlgorithmException)
            {
                throw  Exception("无此算法")
            } catch ( e:InvalidKeySpecException)
            {
                throw  Exception("私钥非法")
            } catch ( e:NullPointerException)
            {
                throw Exception("私钥数据为空")
            }
        }
    }
}