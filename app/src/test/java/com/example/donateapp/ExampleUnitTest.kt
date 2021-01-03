package com.example.donateapp

import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val sut = LogInScreen() // SUT = System Under Test (Unit Test)
    internal val encrypt = Encryption()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun checkForEmptyFieldBlock() {

        assert(testThatLoginWontLetUsHAveEmptyField())

    }

    @Test
    fun checkForTooLongFieldBlock() {

        assert(testThatLoginWontLetUsHAveTooLongField())

    }

    /*
    fun encryptionCheck () {
        val testString = "Test String to encrypt"
        val encryptedString = Encryption.encrypt(testString)


        assertNotEquals(testString, encryptedString)

        val decryptedString = Encryption.decrypt(encryptedString)
        assertEquals(testString, decryptedString)

       // println("???${encryptedString}")
    }
     */



    fun testThatLoginWontLetUsHAveEmptyField(): Boolean {

       if (sut.checkLoggedIn("","") == false){
           println("den lät oss inte logga in, den fångade upp att vi inte angav alla värden")
           return true
       } else{

           return false

    }

    }

    fun testThatLoginWontLetUsHAveTooLongField(): Boolean {

        var testvariable = "fffffffffffffffffffffffffffffffffffffffffffffjoifwewe"

        if (sut.checkLoggedIn(testvariable,"grg") == false){
            println("den lät oss inte logga in, den fångade upp att vi inte angav alla värden")
            return true
        } else{

            return false

        }

    }









}
