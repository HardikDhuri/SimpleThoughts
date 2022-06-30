package com.example.simplethoughts.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplethoughts.R
import com.example.simplethoughts.ui.main.UsersActivity
import com.example.simplethoughts.database.DBHandler
import com.example.simplethoughts.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private  lateinit var prefEditor: SharedPreferences.Editor

    private val db = DBHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = getSharedPreferences("login_details", MODE_PRIVATE)
        prefEditor = sharedPref.edit()

        login_userid.editText?.setText(sharedPref.getString("id", ""))
        login_password.editText?.setText(sharedPref.getString("password", ""))

        create_new_account_btn.setOnClickListener {
            val myIntent: Intent = Intent(this, RegistrationActivity::class.java)
            startActivity(myIntent)
        }

        login_btn_view.setOnClickListener {
            login_userid.error = null
            login_password.error = null
            val id = login_userid.editText?.text.toString()
            val password = login_password.editText?.text.toString()
            if (id == "") {
                login_userid.error = "Required*"
            } else if (password == "") {
                login_password.error = "Required*"
            } else {
                // If user is validated
                if (db.validateUser(id, password) == 1) {
                    // If user want to save login info
                    if (remember_me_cbview.isChecked) {
                        sharedPref = getSharedPreferences("login_details", MODE_PRIVATE)
                        prefEditor.putString("id", id)
                        prefEditor.putString("password", password)
                        prefEditor.apply()
                    } else {
                        prefEditor.remove("id")
                        prefEditor.remove("password")
                        prefEditor.apply()
                    }

                    // Intent to UserDetails Activity
                    val myIntent: Intent = Intent(this, UsersActivity::class.java)
                    myIntent.putExtra("phone_number", id)
                    startActivity(myIntent)
                } else {
                    checkUser(id, password)
                }
            }
        }

    }

    private fun checkUser(id:String, password: String) {
        if (db.validateUser(id, password) == 0) {
            login_userid.editText?.text = null
            login_password.editText?.text = null
            Toast.makeText(
                this,
                "User does not exist. Please create an account",
                Toast.LENGTH_SHORT
            ).show()
//        } else if (db.validateUser(id, password) == 1) {
//            login_userid.editText?.text = null
//            login_password.editText?.text = null
//            Toast.makeText(this, "User Validated", Toast.LENGTH_SHORT).show()
        } else {
            login_password.editText?.text = null
            login_password.error = "Incorrect Password"
            login_userid.error = null
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
        }
    }
}
