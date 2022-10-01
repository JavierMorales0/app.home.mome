package com.mome.homemome

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mome.homemome.databinding.ActivityDashboardBinding
import com.mome.homemome.services.HomeApi
import com.mome.homemome.services.models.AuthResponse
import com.mome.homemome.services.models.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var googleSignInClient: GoogleSignInClient

    // constants
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
        private const val ID_CLIENT = "738156526521-2di2il9u6t2g5j51ef13o5okge3nd04e.apps.googleusercontent.com"
    }

    // SHARED PREFERENCES
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CONFIGURE SHARED PREFERENCES
        sharedPreferences = getSharedPreferences("HOME_MOME", MODE_PRIVATE)

        // CONFIGURE THE  GOOGLE SIGN IN
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(DashboardActivity.ID_CLIENT)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        
    }



    private fun logout(){
        deleteTokenOnSharedPreferences()
        notify("Sesión cerrada")
        startActivity(Intent(this@DashboardActivity, MainActivity::class.java))
        finish()
    }

    private fun deleteTokenOnSharedPreferences(){
        Log.d(TAG, "deleteTokenOnSharedPreferences: Deleting token")
        sharedPreferences.edit().remove("tokenId").commit()
        return
    }

    private fun signOutFromGoogle(){
        googleSignInClient.signOut()
        notify("Desconectado de Google")
    }

    private fun notify(message:String){
        Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val action = item.itemId
        if (action == R.id.action_profile){
            startActivity(Intent(this@DashboardActivity, ProfileActivity::class.java))
        }
        if (action == R.id.action_menu_logout) {
            logout()
            return true
        }
        if (action == R.id.action_menu_logout_google) {
            signOutFromGoogle()
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}