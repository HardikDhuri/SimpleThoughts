package com.example.simplethoughts.data
import android.graphics.Bitmap

data class User (var fullName: String,
                 var phoneNumber: String,
                 var email: String,
                 var username: String,
                 var profilePicture: Bitmap) {
}