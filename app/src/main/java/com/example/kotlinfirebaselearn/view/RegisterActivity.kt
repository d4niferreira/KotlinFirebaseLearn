package com.example.kotlinfirebaselearn.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinfirebaselearn.R
import com.example.kotlinfirebaselearn.User
import com.example.kotlinfirebaselearn.Utils
import com.example.kotlinfirebaselearn.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("DatasUsers")

        registerEvents()
    }

    private fun registerEvents (){
        binding.textviewSingin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonRegister.setOnClickListener {
            val firstname: String = binding.edittextRegisterName.text.toString()
            val lastname: String = binding.edittextRegisterLastname.text.toString()
            val email = binding.edittextRegisterEmail.text.toString()
            val pass = binding.edittextRegisterPassword.text.toString()
            val passConfirm = binding.edittextRegisterPasswordconfirm.text.toString()

            if (firstname.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && passConfirm.isNotEmpty()) {
                if (Utils.isValidEmail(email)) {
                    if (pass == passConfirm) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val user = User(firstname, lastname)
                                    val emailWithoutSpecialChars = email.replace(".", ",") // Removendo caracteres especiais do email
                                    database.child(emailWithoutSpecialChars).setValue(user)
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else
                                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                    } else
                        Toast.makeText(this, R.string.warning_password_match, Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, R.string.warning_email_invalid, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, R.string.warning_empty_fields, Toast.LENGTH_SHORT).show()
        }
    }
}