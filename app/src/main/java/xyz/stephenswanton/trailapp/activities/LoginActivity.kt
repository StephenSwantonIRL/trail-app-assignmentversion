package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityLoginBinding
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.User


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var app: MainApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        if(app!!.tempUserObject.findAll().isNotEmpty()) {
            Intent(this, MainActivity::class.java).also{
                startActivity(it)
            }
        }
        var user: User = User ("","")


        binding.btnLogin.setOnClickListener{
            user.username = binding.username.text.toString()
            user.password = binding.password.text.toString()
            if(user.username != "" || user.username.isNotEmpty()){
                var userExists = app!!.users.findByUsername(user.username) as User?
                if(userExists != null){
                    var passwordCheck = BCrypt.verifyer().verify(user.password.toCharArray(), userExists.password);
                    if(passwordCheck.verified){
                        val stayLoggedInDialog = AlertDialog.Builder(this)
                            .setTitle("Store Credentials")
                            .setMessage("Do you want to stay logged in?")
                            .setPositiveButton("Yes"){ _, _ ->
                                user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
                                app!!.tempUserObject.create(user.copy())
                                Intent(this, MainActivity::class.java).also{
                                    startActivity(it)
                            }
                            }
                            .setNegativeButton("No"){ _, _ ->
                                Intent(this, MainActivity::class.java).also{
                                    startActivity(it)
                                }
                            }
                        stayLoggedInDialog.show()

                    } else {
                        Snackbar.make(it, R.string.invalid_password, Snackbar.LENGTH_LONG)
                            .show()
                    }
                } else {
                    if(user.password != "" || user.password.isNotEmpty()){
                        user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
                        app!!.users.create(user.copy())
                        Snackbar.make(it, R.string.account_created, Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        Snackbar.make(it, R.string.enter_password, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            } else {
                Snackbar.make(it, R.string.enter_username, Snackbar.LENGTH_LONG)
                    .show()
            }
            if(user.password == "" || user.password.isEmpty()){
                Snackbar.make(it, R.string.enter_password, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

    }
}