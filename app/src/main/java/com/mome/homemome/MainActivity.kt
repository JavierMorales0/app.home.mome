package com.mome.homemome

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.mome.homemome.databinding.ActivityMainBinding
import com.mome.homemome.services.HomeApi
import com.mome.homemome.services.models.Credential
import com.mome.homemome.services.models.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    // view binding
    private lateinit var binding : ActivityMainBinding

    private lateinit var googleSignInClient:GoogleSignInClient

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CONFIGURE SHARED PREFERENCES
        sharedPreferences = getSharedPreferences("HOME_MOME", MODE_PRIVATE)

        // CONFIGURE THE  GOOGLE SIGN IN
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ID_CLIENT)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        // CHECK IF THE USER IS ALREADY LOGGED IN
        checkUser()
        // GOOGLE BUTTON SIGN IN, CLICK TO BEGIN GOOGLE SIGN IN
        binding.googleSignInBtn.setOnClickListener {
            // begin google sign in
            Log.d(TAG, "onCreate: begin Google sign in")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        // check if user is logged in or not
        val token = sharedPreferences.getString("tokenId", "")
        if(token != ""){
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // RESULT RETURNED FROM LAUNCHING INTENT  FROM GoogleSignInApi,getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG,"onActivityResult: Google Sign in intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                // Google sign in success
                val account  = accountTask.getResult(ApiException::class.java)
                // FETCH THE API TO FINISH LOGIN
                val credential = Credential(account.idToken!!)
                // INSTANCE THE LOGIN METHOD OF THE API
                val _API : Call<AuthResponse> = HomeApi.retrofitService.auth(credential);
                // PROMISE OF THE API RESPONSE
                _API.enqueue(object : Callback<AuthResponse>
                {
                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {
                        // SAVE THE RESPONSE
                        val response = response.body()
                        // SAVE THE TOKEN ON SHARED PREFERENCES
                        saveTokenOnSharedPreferences(response!!.data.tokenId)
                        // CHECK USER STATUS
                        checkUser()
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        // SHOW ERROR
                        Log.d(TAG, "onFailure: ${t.message}")
                        // NOTIFY ERROR
                        notify("Error al iniciar sesión")
                    }

                })
            } catch(e: Exception){
                // failed Google SignIn
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }


    private fun saveTokenOnSharedPreferences(token: String){
        val editSharedPreferences: SharedPreferences.Editor = sharedPreferences.edit();
        if (TextUtils.isEmpty(token)) return
        editSharedPreferences.putString("tokenId", token)
        editSharedPreferences.commit()
        return
    }

    private fun notify(message:String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

}
