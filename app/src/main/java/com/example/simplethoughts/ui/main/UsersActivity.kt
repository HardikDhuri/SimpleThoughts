package com.example.simplethoughts.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplethoughts.R
import com.example.simplethoughts.adapters.AllUsersAdapter
import com.example.simplethoughts.data.User
import com.example.simplethoughts.database.DBHandler
import com.example.simplethoughts.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_user_details.*


class UsersActivity : AppCompatActivity() {
    private val db = DBHandler(this)
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<AllUsersAdapter.AllUsersViewHolder>? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val users: List<User> = db.getAllUsers()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        all_users_rv.layoutManager = layoutManager
        adapter = AllUsersAdapter(users)
        all_users_rv.adapter = adapter

        logout_btn_view.setOnClickListener {
            val i: Intent = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }

}