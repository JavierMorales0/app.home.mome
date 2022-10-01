package com.mome.homemome

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.widget.Toast
import com.mome.homemome.databinding.ActivityDashboardBinding
import com.mome.homemome.databinding.ActivityProfileBinding
import com.mome.homemome.services.HomeApi
import com.mome.homemome.services.models.UserProfileResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    // SHARED PREFERENCES
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // CONFIGURE SHARED PREFERENCES
        sharedPreferences = getSharedPreferences("HOME_MOME", MODE_PRIVATE)
        // CONFIGURE THE BACK BUTTON AND TITLE
        val actionBar = supportActionBar
        actionBar?.title="Perfil"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // GET THE PROFILE INFO FROM API
        getProfile()

        // ADD THE LISTENER ON BUTTON LOGOUT
        binding.btnLogout.setOnClickListener {
            deleteTokenOnSharedPreferences()
            notify("Sesión cerrada")
            startActivity(
                Intent(this@ProfileActivity, MainActivity::class.java)
            )
            finishAffinity()
        }
    }
    private fun getProfile()
    {
        // INSTANCE THE LOGIN METHOD OF THE API
        val tokenId = sharedPreferences.getString("tokenId", "")
        val _API : Call<UserProfileResponse> = HomeApi.retrofitService.getProfile(
            "Bearer $tokenId"
            );
        // PROMISE OF THE API RESPONSE
        _API.enqueue(object : Callback<UserProfileResponse>
        {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                val profileResponse = response.body()
                val profile = profileResponse!!.data
                Picasso.get().load(profile.avatar).into(binding.avatar)
                binding.name.text = "${profile.firstName} ${profile.lastName}"
                binding.email.text= profile.email
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun deleteTokenOnSharedPreferences(){
        sharedPreferences.edit().remove("tokenId").commit()
        return
    }
    private fun notify(message:String){
        Toast.makeText(this@ProfileActivity, message, Toast.LENGTH_SHORT).show()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}