package com.example.kotlinfirebaselearn.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinfirebaselearn.R
import com.example.kotlinfirebaselearn.Utils
import com.example.kotlinfirebaselearn.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var  firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        registerEvents()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerEvents (){
        binding.textviewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.edittextLoginEmail.text.toString()
            val password = binding.edittextLoginPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (Utils.isValidEmail(email)) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }else
                    Toast.makeText(this, R.string.warning_email_invalid, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, R.string.warning_empty_fields, Toast.LENGTH_SHORT).show()
        }
    }
}