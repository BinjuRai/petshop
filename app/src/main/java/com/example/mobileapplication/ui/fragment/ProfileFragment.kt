package com.example.mobileapplication.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.FragmentProfileBinding
import com.example.mobileapplication.repository.AuthRepo
import com.example.mobileapplication.repository.AuthRepoImpl
import com.example.mobileapplication.ui.activity.EditProfileActivity
import com.example.mobileapplication.ui.activity.LoginActivity
import com.example.mobileapplication.viewmodel.AuthViewModel


class ProfileFragment : Fragment() {
    lateinit var profileBinding:FragmentProfileBinding
    lateinit var authViewModel: AuthViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        profileBinding=FragmentProfileBinding.inflate(inflater,container,false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var repo = AuthRepoImpl()
        authViewModel= AuthViewModel(repo)

        var currentUser =authViewModel.getCurrentUser()
        currentUser.let{
            authViewModel.fetchUserData(currentUser?.uid.toString())
        }
        profileBinding.editProfileCard.setOnClickListener{
            var intent=Intent(requireContext(),EditProfileActivity::class.java)
            startActivity(intent)
        }
        authViewModel.userData.observe(requireActivity()){
            users->
            users.let{
                if (users?.imageUrl==null || users?.imageUrl.isEmpty()){
                    profileBinding.profileImage.setImageResource(R.drawable.dummyprofilepic)
                    profileBinding.progressBarImage.visibility=View.GONE
                }
                profileBinding.EmailProfile.text=users?.email.toString()
                profileBinding.FullNameProfile.text=users?.name.toString()

            }
        }

      profileBinding.editLogout.setOnClickListener{
          authViewModel.logout{
              success,message->
              if (success){
                  var intent=Intent(requireContext(),LoginActivity::class.java)
                  startActivity(intent)
                  requireActivity()
                      .finish()
              }else{
                  Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
              }
          }
      }
    }



}