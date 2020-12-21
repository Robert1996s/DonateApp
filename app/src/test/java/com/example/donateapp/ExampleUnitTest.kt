package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import com.google.common.base.Predicates.equalTo
import org.junit.Test
import org.junit.Assert.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val sut = LogInScreen() // SUT = System Under Test (Unit Test)


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
