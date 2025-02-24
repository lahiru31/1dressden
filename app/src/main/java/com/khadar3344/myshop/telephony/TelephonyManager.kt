package com.khadar3344.myshop.telephony

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class TelephonyManager @Inject constructor(private val context: Context) {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private var telephonyCallback: TelephonyCallback? = null

    fun makePhoneCall(phoneNumber: String) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } else {
            Log.e("TelephonyManager", "Call permission not granted")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun startCallStateMonitoring() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            telephonyCallback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int) {
                    when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {
                            Log.d("TelephonyManager", "Call State: IDLE")
                        }
                        TelephonyManager.CALL_STATE_RINGING -> {
                            Log.d("TelephonyManager", "Call State: RINGING")
                        }
                        TelephonyManager.CALL_STATE_OFFHOOK -> {
                            Log.d("TelephonyManager", "Call State: OFFHOOK")
                        }
                    }
                }
            }
            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                telephonyCallback as TelephonyCallback
            )
        } else {
            Log.e("TelephonyManager", "Read phone state permission not granted")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun stopCallStateMonitoring() {
        telephonyCallback?.let {
            telephonyManager.unregisterTelephonyCallback(it)
            telephonyCallback = null
        }
    }

    @Deprecated("Use alternative device identification methods")
    fun getDeviceId(): String? {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telephonyManager.imei
                } else {
                    @Suppress("DEPRECATION")
                    telephonyManager.deviceId
                }
            } catch (e: SecurityException) {
                Log.e("TelephonyManager", "Error getting device ID", e)
                null
            }
        } else {
            Log.e("TelephonyManager", "Read phone state permission not granted")
            null
        }
    }
}
