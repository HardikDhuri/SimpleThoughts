package com.example.simplethoughts.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplethoughts.R
import com.example.simplethoughts.adapters.AllUsersAdapter
import com.example.simplethoughts.data.User
import com.example.simplethoughts.database.DBHandler
import com.example.simplethoughts.ui.login.LoginActivity
import com.example.simplethoughts.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.fragment_display_users.*
import kotlinx.android.synthetic.main.fragment_user_detail.*


class UsersActivity : AppCompatActivity() {
    private var loggedIn: Boolean = false
    private val db = DBHandler(this)
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<AllUsersAdapter.AllUsersViewHolder>? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val users: List<User> = db.getAllUsers()
        layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        } else {
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        }
        display_users_frame_rv.layoutManager = layoutManager
        adapter = AllUsersAdapter(users)
        display_users_frame_rv.adapter = adapter

        logout_btn_view.setOnClickListener {
            val i: Intent = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        add_new_user_button.setOnClickListener {
            loggedIn = true
            val i: Intent = Intent(this, RegistrationActivity::class.java)
            i.putExtra("loggedIn", loggedIn)
            startActivity(i)
        }
    }

}