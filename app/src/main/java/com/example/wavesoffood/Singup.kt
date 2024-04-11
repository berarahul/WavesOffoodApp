package com.example.wavesoffood

import android.app.Activity
import android.content.ContentProviderClient
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wavesoffood.databinding.ActivityLoginBinding
import com.example.wavesoffood.databinding.ActivitySingupBinding
import com.example.wavesoffood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Singup : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding : ActivitySingupBinding by lazy {
        ActivitySingupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("Singup", "Singup activity created")
    val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //Initialize Firebase auth
    auth= Firebase.auth
        //Initialize by Firebase Database
        database=Firebase.database.reference
        //Initialize by Firebase Database
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)
        binding.CreateAccountButton.setOnClickListener {
            username=binding.username.text.toString()
            email=binding.emailaddress.text.toString().trim()
            password=binding.password.text.toString().trim()

            if (email.isBlank()||password.isBlank()||username.isBlank()){
                Toast.makeText(this,"Please Fill The All Details",Toast.LENGTH_SHORT).show()
            }else
            {
                createAccount(email,password)

            }
        }


        binding.alreadyhavebutton.setOnClickListener{
            Log.d("Singup", "Already have account button clicked")
            val intent = Intent(this,Login_Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)

        }
        binding.googlebutton.setOnClickListener {
            val signInIntent:Intent=googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    //Launcher for google sign in
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode==Activity.RESULT_OK){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account:GoogleSignInAccount?=task.result
                val credential:AuthCredential =   GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Sign-In Successfully",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else
                    {
                        Toast.makeText(this,"Sign-In Failed ",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }else{
            Toast.makeText(this,"Sign-In Failed ",Toast.LENGTH_SHORT).show()
        }
    }


    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task->
            if (task.isSuccessful){
                Toast.makeText(this,"Account Create SuccessFully",Toast.LENGTH_SHORT).show()
                saveuserdata()

                startActivity(Intent(this,Login_Activity::class.java))

                finish()

            }else
            {
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                Log.d("Account","Create Account",task.exception)
            }
        }
    }

    private fun saveuserdata() {

        // retrive data from input fill
       username=binding.username.text.toString()
        email=binding.emailaddress.text.toString().trim()
        password=binding.password.text.toString().trim()


        val user=UserModel(username,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        //save data to Firebase Database
        database.child("user").child(userId).setValue(user)
    }
}