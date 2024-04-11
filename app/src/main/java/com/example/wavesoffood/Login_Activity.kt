package com.example.wavesoffood

import android.app.Activity
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wavesoffood.databinding.ActivityLoginBinding
import com.example.wavesoffood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login_Activity : AppCompatActivity() {
    private  var username:String?=null
private lateinit var email:String
private lateinit var password:String
private lateinit var auth:FirebaseAuth
private lateinit var database: DatabaseReference
private lateinit var googleSignInClient: GoogleSignInClient


    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // Initialize of firebase Auth
        auth= Firebase.auth
        //Initialize of firebase Database
        database=Firebase.database.reference
        //Initialize of Google
        googleSignInClient=GoogleSignIn.getClient(this,googleSignInOptions)


        //Login with email and password
        binding.LoginButton.setOnClickListener {

            //get data from text field
            email=binding.emaillogin.text.toString().trim()
            password=binding.passwordlogin.text.toString().trim()
            if (email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Please Enter All Details",Toast.LENGTH_SHORT).show()

            }else
            {
                createuser()
                Toast.makeText(this,"Login SuccessFull",Toast.LENGTH_SHORT).show()
            }

        }
        binding.donthavebutton.setOnClickListener {
            val intent= Intent(this,Singup::class.java)
            startActivity(intent)

        }

        //Google Sign-In
        binding.googlebuttonlogin.setOnClickListener {
            val signInIntent:Intent=googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }
private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
    if (result.resultCode== Activity.RESULT_OK){
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful){
            val account: GoogleSignInAccount?=task.result
            val credential: AuthCredential =   GoogleAuthProvider.getCredential(account?.idToken,null)
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


    private fun createuser() {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if (task.isSuccessful){

                val user:FirebaseUser? = auth.currentUser
                if (user != null) {
                    updateUi(user)
                }
            }else
            {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        saveuserdata()
                        val user:FirebaseUser?=auth.currentUser
                        if (user != null) {
                            updateUi(user)
                        }else
                        {
                            Toast.makeText(this,"Sign-in failed",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }


    }

    private fun saveuserdata() {

        email=binding.emaillogin.text.toString().trim()
        password=binding.passwordlogin.text.toString().trim()

        val user=UserModel(username,email,password)
        val userId:String =FirebaseAuth.getInstance().currentUser!!.uid
        //save data in to Database
        database.child("user").child(userId).setValue(user)
    }

    override fun onStart() {
        super.onStart()
        val curentuser:FirebaseUser?= auth.currentUser
        if (curentuser!=null){

            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun updateUi(user: FirebaseUser) {
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}