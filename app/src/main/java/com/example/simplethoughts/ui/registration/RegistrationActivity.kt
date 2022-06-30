package com.example.simplethoughts.ui.registration

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplethoughts.R
import com.example.simplethoughts.database.DBHandler
import com.example.simplethoughts.ui.login.LoginActivity
import com.example.simplethoughts.ui.main.UsersActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_registration.*
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class RegistrationActivity : AppCompatActivity() {
    private val db = DBHandler(this)

    private val PASSWORD_REGEX: Pattern =
        Pattern.compile("^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{6,16}" +
                "$")



    private val USERNAME_REGEX: Pattern =
        Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")

    private val EMAIL_REGEX: Pattern =
        Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")

    private val PHONE_REGEX: Pattern =
        Pattern.compile("^[0-9]{10}\$")

    private val FULL_NAME_REGEX: Pattern =
        Pattern.compile("^[A-Za-z ]{5,30}")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        // Add User
        register_btn_view.setOnClickListener {
            if (validate()) {
                val username: String = username_etview.editText?.text.toString()
                val fullName: String = full_name_etview.editText?.text.toString()
                val email: String = email_etview.editText?.text.toString()
                val phoneNumber: String = phone_etview.editText?.text.toString()
                val password: String = password_etview.editText?.text.toString()

                val profilePicture:ByteArray = db.convertImage(upload_btn_view)

                val insertMessage: String? = db.addUser(
                    username,
                    fullName,
                    email,
                    phoneNumber,
                    password,
                    profilePicture
                )
                if (insertMessage.equals("User Registered")) {
                    clearField()

                    val loggedIn = intent.getBooleanExtra("loggedIn", false)

                    // Intent to Login Page so that created user or existing user can login
                    if (loggedIn) {
                        val myIntent: Intent = Intent(this, UsersActivity::class.java)
                        startActivity(myIntent)
                    } else {
                        val myIntent: Intent = Intent(this, LoginActivity::class.java)
                        startActivity(myIntent)
                    }

                    Toast.makeText(this, "Your account is created! Please Log In", Toast.LENGTH_SHORT).show()

                } else if (insertMessage.toString().contains("${db.getTableName()}.${db.getPhoneNumberColName()}")) {
                    phone_etview.editText?.text = null
                    phone_etview.error = "Phone number already exists."
                } else if (insertMessage.toString().contains("${db.getTableName()}.${db.getUsernameColName()}")) {
                    username_etview.editText?.text = null
                    username_etview.error = "This username is taken."
                } else {
                    Toast.makeText(this, "An error occured please try again later.", Toast.LENGTH_SHORT).show()
                    clearField()
                }
//


            }
        }

        // Upload Image
        upload_btn_view.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        //Login Button Click Listener
        login_activity_btn_view.setOnClickListener {
            val myIntent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val uri: Uri = intent?.data!!
        upload_btn_view.setImageURI(uri)

        upload_picture_helper_text_view.visibility = View.INVISIBLE
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                val profilePicture = intent.extras!!.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                profilePicture.compress(Bitmap.CompressFormat.PNG, 30, stream)
                val byteArray = stream.toByteArray()
            }
        }
    }

    private fun validateUsername(): Boolean {
        val usernameInput: String = username_etview.editText?.text.toString().trim()
        if (usernameInput.isEmpty()) {
            username_etview.error = "Username cannot be empty"
            return false
        } else if (!USERNAME_REGEX.matcher(usernameInput).matches()) {
            username_etview.error = "Please enter a valid username"
            return false
        } else {
            username_etview.error = null
            return true
        }
    }

    private fun validateEmail(): Boolean {
        val emailInput: String = email_etview.editText?.text.toString().trim()
        if (emailInput.isEmpty()) {
            email_etview.error = "Email cannot be empty"
            return false
        } else if (!EMAIL_REGEX.matcher(emailInput).matches()) {
            email_etview.error = "Please enter a valid email address"
            return false
        } else {
            email_etview.error = null
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput: String = password_etview.editText?.text.toString().trim()
        val confirmPasswordInput: String = confirm_password_etview.editText?.text.toString().trim()

        if (passwordInput.isEmpty()) {
            password_etview.error = "Password cannot be empty"
            return false
        } else if (!PASSWORD_REGEX.matcher(passwordInput).matches()) {
            password_etview.error = "Password must contain 1 uppercase, 1 special character, 1 number and should be atleast 8 characters long."
            return false
        } else {
            if (passwordInput != confirmPasswordInput) {
                confirm_password_etview.editText?.text = null
                confirm_password_etview.error = "Password Doesn't match"
                return false
            } else {
                password_etview.error = null
                return true
            }

        }
    }

    private fun validatePhone(): Boolean {
        val phoneInput:String = phone_etview.editText?.text.toString().trim()
        if (phoneInput.isEmpty()) {
            phone_etview.error = "Phone Number cannot be empty"
            return false
        } else if (!PHONE_REGEX.matcher(phoneInput).matches()) {
            phone_etview.error = "Please enter a valid Phone number"
            return false
        } else {
            phone_etview.error = null
            return true
        }
    }

    private fun validateFullName(): Boolean {
        val fullNameInput:String = full_name_etview.editText?.text.toString().trim()
        if (fullNameInput.isEmpty()) {
            full_name_etview.error = "Name cannot be empty"
            return false
        } else if (!FULL_NAME_REGEX.matcher(fullNameInput).matches()) {
            full_name_etview.error = "Please enter a valid name"
            return false
        } else {
            full_name_etview.error = null
            return true
        }
    }

    private fun validate(): Boolean {
        validateUsername()
        validateEmail()
        validatePhone()
        validatePassword()
        validateFullName()

        return validateUsername() &&
                validateEmail() &&
                validatePhone() &&
                validatePassword() &&
                validateFullName()
    }

    private fun clearField() {
        username_etview.editText?.text = null
        full_name_etview.editText?.text = null
        email_etview.editText?.text = null
        phone_etview.editText?.text = null
        password_etview.editText?.text = null
        confirm_password_etview.editText?.text = null
    }
}