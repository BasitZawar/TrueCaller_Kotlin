package com.lads.truecaller.activities

import android.Manifest
import android.annotation.TargetApi
import android.app.role.RoleManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lads.truecaller.Fragments.ContactsFragment
import com.lads.truecaller.Fragments.FavouriteFragment
import com.lads.truecaller.Fragments.KeypadFragment
import com.lads.truecaller.Fragments.RecentFragment
import com.lads.truecaller.R
import com.lads.truecaller.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var telecomManager: TelecomManager

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Call_Phone permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 0)
        }
//        checkDefaultDialer()
        if (isDefaultDialer()) {
            launchSetDefaultDialerIntent()
//            Toast.makeText(this, "if part is running", Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(this, "else part is running", Toast.LENGTH_SHORT).show()
        }

        binding.bottomNavigatinView.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.favourite -> {
                    loadFragment(FavouriteFragment())
                    Toast.makeText(this, "Favourite", Toast.LENGTH_SHORT).show()
                }
                R.id.recent -> {
                    loadFragment(RecentFragment())
                    Toast.makeText(this, "Recent", Toast.LENGTH_SHORT).show()
                }
                R.id.contact -> {
                    loadFragment(ContactsFragment())
                    Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show()
                }
                R.id.keypad -> {
                    loadFragment(KeypadFragment())
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()
//        checkDefaultDialer()
//        if (isDefaultDialer()) {
//            Toast.makeText(this, "if part is running", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "else part is running", Toast.LENGTH_SHORT).show()
//        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    val REQUEST_CODE_SET_DEFAULT_DIALER = 200

//    private fun checkDefaultDialer() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
//            return
//
//        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
//        val isAlreadyDefaultDialer = packageName == telecomManager.defaultDialerPackage
//        if (isAlreadyDefaultDialer)
//            return
//        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
//            .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
//        startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            REQUEST_CODE_SET_DEFAULT_DIALER -> checkSetDefaultDialerResult(resultCode)
//        }
//    }

//    private fun checkSetDefaultDialerResult(resultCode: Int) {
//        val message = when (resultCode) {
//            RESULT_OK -> "User accepted request to become default dialer"
//            RESULT_CANCELED -> "User declined request to become default dialer"
//            else -> "Unexpected result code $resultCode"
//        }
//
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//
//    }
//
//    companion object {
//        private const val REQUEST_CODE_SET_DEFAULT_DIALER = 123
//    }

    @TargetApi(Build.VERSION_CODES.M)
    fun Context.isDefaultDialer(): Boolean {
        return if (!packageName.startsWith("com.lads.truecaller.id.contacts") && !packageName.startsWith(
                "com.lads.truecaller.id.dialer"
            )
        ) {
            true
        } else if ((packageName.startsWith("com.lads.truecaller.id.contacts") || packageName.startsWith(
                "com.lads.truecaller.id.dialer"
            )) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        ) {
            val roleManager = getSystemService(RoleManager::class.java)
            roleManager!!.isRoleAvailable(RoleManager.ROLE_DIALER) && roleManager.isRoleHeld(
                RoleManager.ROLE_DIALER
            )
        } else {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && telecomManager.defaultDialerPackage == packageName
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun launchSetDefaultDialerIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager!!.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(
                    RoleManager.ROLE_DIALER
                )
            ) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                startActivityForResult(intent, 1010)

            }
        } else {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).putExtra(
                TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                packageName
            ).apply {
                try {
                    startActivityForResult(this, 1010)
                } catch (e: ActivityNotFoundException) {

                } catch (e: Exception) {

                }
            }
        }
    }

}