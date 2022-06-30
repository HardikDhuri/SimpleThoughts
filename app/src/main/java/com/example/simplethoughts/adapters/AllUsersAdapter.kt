package com.example.simplethoughts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplethoughts.R
import com.example.simplethoughts.data.User
import com.example.simplethoughts.fragments.UserDetailFragment
import com.example.simplethoughts.ui.main.UsersActivity
import de.hdodenhof.circleimageview.CircleImageView

class AllUsersAdapter (var users: List<User>) : RecyclerView.Adapter<AllUsersAdapter.AllUsersViewHolder>() {

    inner class AllUsersViewHolder(userView: View): RecyclerView.ViewHolder (userView) {
        var userProfilePicture: CircleImageView



        init {
            userProfilePicture = userView.findViewById(R.id.user_profile_image_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_profile, parent, false)
        return AllUsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllUsersViewHolder, position: Int) {
        holder.userProfilePicture.setImageBitmap(users[position].profilePicture)

        holder.userProfilePicture.setOnClickListener { view: View ->
            val activity: UsersActivity = view.context as UsersActivity
            val user: User = users[position]
            val fragment: UserDetailFragment = activity.supportFragmentManager.findFragmentById(R.id.user_details_fragment) as UserDetailFragment
            fragment.setUser(user)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}