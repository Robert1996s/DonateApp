package com.example.donateapp


import android.util.Base64
import org.junit.Test
import org.junit.Assert.*
import com.example.donateapp.Encryption

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val sut = LogInScreen() // SUT = System Under Test (Unit Test)
  
    internal val encrypt = Encryption()

    val sut1 = Encryption()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkForEmptyFieldBlock() {
        assert(testThatLoginWontLetUsHAveEmptyField())
    }

    @Test
    fun myEncryptionTest() {
        encryptionCheck()
    }

    /*@Test
    fun checkForTooLongFieldBlock() {

        assert(testThatLoginWontLetUsHAveTooLongField())
    } */



    @Test
    fun encryptionCheck() {

        val testString = "Test String to encrypt"

        val encryptedString = Encryption().encrypt(testString.toByteArray(), testString.toCharArray())

        assertNotEquals(testString, encryptedString)

        val decryptedString = Encryption().decrypt(encryptedString, testString.toCharArray())

        assertEquals(testString, decryptedString)

    }


    fun testThatLoginWontLetUsHAveEmptyField(): Boolean {

        if (sut.checkLoggedIn("", "") == false) {
            println("den lät oss inte logga in, den fångade upp att vi inte angav alla värden")
            return true
        } else {

            return false
        }
    }

    fun checkMyEncrypt(toEncode: String, password: String): Boolean {


        var map = Encryption().encrypt(toEncode.toByteArray(), password.toCharArray())


        val base64Encrypted = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)
        val base64Iv = Base64.encodeToString(map["iv"], Base64.NO_WRAP)
        val base64Salt = Base64.encodeToString(map["salt"], Base64.NO_WRAP)

        println("!!!" +base64Encrypted)

        val encrypted = Base64.decode(base64Encrypted, Base64.NO_WRAP)
        val iv = Base64.decode(base64Iv, Base64.NO_WRAP)
        val salt = Base64.decode(base64Salt, Base64.NO_WRAP)

        val decrypted = Encryption().decrypt(
            hashMapOf("iv" to iv, "salt" to salt, "encrypted" to encrypted),
            password.toCharArray())

        var decryptedMessage: String? = null
        decrypted?.let {
            decryptedMessage = String(it, Charsets.UTF_8)
        }

        if (decryptedMessage == toEncode) {
            return true
        } else {
            return false
        }
    }

    /*fun testThatLoginWontLetUsHAveTooLongField(): Boolean {

        var testvariable = "fffffffffffffffffffffffffffffffffffffffffffffjoifwewe"

        if (sut.checkLoggedIn(testvariable,"grg") == false){
            println("den lät oss inte logga in, den fångade upp att vi inte angav alla värden")
            return true
        } else{
            return false
        }
    } */

}
