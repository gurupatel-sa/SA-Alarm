package com.sa.alarm

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.splash.DummyActivity
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest.*
import java.security.NoSuchAlgorithmException
import java.util.*

class MainActivity : BaseActivity() {

    var TAG: String = this.javaClass.getSimpleName()

    lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

//        registerNetworkReciver()
        LogUtils.d(TAG, "onCreate : init")

        click.setOnClickListener {
            startActivity(Intent(this, DummyActivity::class.java))
        }

        callbackManager = CallbackManager.Factory.create();

        fb_button.setOnClickListener {
            LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d(TAG, "onSuccess")
                    val token = result?.accessToken
                    Log.d(TAG, "onSuccess" + result?.accessToken)

                    handleFacebookAccessToken(token!!)

                }

                override fun onCancel() {
                    Log.d(TAG, "onCancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.d(TAG, "onError" + error?.message)
                    error?.printStackTrace()
                }

            })

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        }

        generateHashKey()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                }

            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }

    private fun generateHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.sa.alarm",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtils.d(TAG , "ONACTVTYRESULT")
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }
}
