package com.example.assignment2

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    // Initialize Firebase Auth
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()


        val signUpButton = findViewById<Button>(R.id.signupButton)
        signUpButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.signupEmail).text.toString()
            val password = findViewById<EditText>(R.id.signupPassword).text.toString()
            signUp(email, password)
        }

        val alreadysignup = findViewById<TextView>(R.id.alreadysignup)
        val text = "Already have an account? Login"
        val spannable = SpannableString(text)
        val blueColor = Color.BLUE
        val clickableSpan = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        spannable.setSpan(
            ForegroundColorSpan(blueColor),
            text.indexOf("Login"),
            text.indexOf("Login") + "Login".length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            clickableSpan,
            text.indexOf("Login"),
            text.indexOf("Login") + "Login".length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        alreadysignup.text = spannable

        alreadysignup.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(email: String, password: String) {
        if (isNotEmpty(email) && isValidEmail(email) && isNotEmpty(password) && isValidPassword(password)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User registered successfully
                        val user = auth.currentUser
                        // You can perform additional actions here after successful registration
                        // For example, you might want to send a verification email to the user

                        // Send verification email
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    // Verification email sent successfully
                                    Toast.makeText(
                                        this,
                                        "You are now Registered Successfully!!! ${user.email}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Failed to send verification email
                                    Toast.makeText(
                                        this,
                                        "Failed to Registration!!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        // Handle registration failure
                        val errorMessage = task.exception?.message ?: "Registration failed"
                        // Display the error message to the user or handle the error
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNotEmpty(value: String): Boolean {
        return value.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        // Implement your password validation logic here
        // For example, ensure the password meets your required criteria
        return password.length >= 6 // Example: Minimum password length of 6 characters
    }
}