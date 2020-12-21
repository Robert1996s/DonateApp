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
class NewExampleUnitTest {

    val sut = LogInScreen()


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun LogginTrue() {

            assert(testThatLoginWontLetUsHAveEmptyField())

    }

    fun testThatLoginWontLetUsHAveEmptyField(): Boolean {

       if (sut.checkLoggedIn("jgheryfjfkdodkdksjeufhjfjfurkdidjskwjekdjskeudhjdksjhre","geegrg") == false){
           println("den lät oss inte logga in, den fångade upp att vi inte angav alla värden")
           return true
       } else{

           return false

    }

    }

}


