package com.example.mobileapplication

import com.example.mobileapplication.repository.ForegetRepoImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AuthUnitTest {
    @Mock
    private lateinit var mockAuth: FirebaseAuth



    @Mock
    private lateinit var mockTask: Task<Void>

    private lateinit var forgetRepo: ForegetRepoImpl

    @Captor
    private lateinit var captor: ArgumentCaptor<OnCompleteListener<Void>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        forgetRepo = ForegetRepoImpl(mockAuth)
    }

    @Test
    fun testRegister_Successful() {
        val email = "test@example.com"
        var expectedResult = "Initial Value" // Define the initial value

        // Mocking task to simulate successful registration
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockAuth.sendPasswordResetEmail(any()))
            .thenReturn(mockTask)

        // Define a callback that updates the expectedResult
        val callback = { success: Boolean, message: String? ->
            expectedResult = message ?: "Callback message is null"
        }

        // Call the function under test
        forgetRepo.forgetpassword(email,  callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        // Assert the result
        assertEquals("Reset mail sent to $email", expectedResult)
    }
}