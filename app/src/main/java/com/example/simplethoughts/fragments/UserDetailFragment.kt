package com.example.simplethoughts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.simplethoughts.R
import com.example.simplethoughts.data.User
import kotlinx.android.synthetic.main.fragment_user_detail.*

class UserDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.logout_btn_view)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserDetailFragment().apply {

            }
    }

    fun setUser(user: User) {

        user_profile_picture.setImageBitmap(user.profilePicture)
        user_username.text = user.username
        user_email.text = user.email
        user_phone.text = user.phoneNumber
        user_name.text = user.fullName
    }
}