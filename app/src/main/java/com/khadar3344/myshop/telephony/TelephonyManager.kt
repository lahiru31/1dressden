package com.khadar3344.myshop.telephony

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TelephonyManager @Inject constructor(private val context: Context) {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private var telephonyCallback: TelephonyCallback? = null

    private val _callState = MutableStateFlow(CallState.IDLE)
    val callState: StateFlow<CallState> = _callState

    enum class CallState {
        IDLE,
        RINGING,
        OFFHOOK
    }

    fun makePhoneCall(phoneNumber: String): Boolean {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            try {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                true
            } catch (e: Exception) {
                Log.e("TelephonyManager", "Error making phone call", e)
                false
            }
        } else {
            Log.e("TelephonyManager", "Call permission not granted")
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun startCallStateMonitoring() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            telephonyCallback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int) {
                    val newState = when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {
                            Log.d("TelephonyManager", "Call State: IDLE")
                            CallState.IDLE
                        }
                        TelephonyManager.CALL_STATE_RINGING -> {
                            Log.d("TelephonyManager", "Call State: RINGING")
                            CallState.RINGING
                        }
                        TelephonyManager.CALL_STATE_OFFHOOK -> {
                            Log.d("TelephonyManager", "Call State: OFFHOOK")
                            CallState.OFFHOOK
                        }
                        else -> CallState.IDLE
                    }
                    _callState.value = newState
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
            _callState.value = CallState.IDLE
        }
    }

    fun isCallPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isPhoneStatePermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getActiveSubscriptionIds(): List<Int> {
        if (!isPhoneStatePermissionGranted()) {
            return emptyList()
        }

        return try {
            val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                subscriptionManager.activeSubscriptionInfoList?.map { subscription: SubscriptionInfo ->
                    subscription.subscriptionId
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("TelephonyManager", "Error getting subscription IDs", e)
            emptyList()
        }
    }

    fun getNetworkOperatorName(): String {
        return telephonyManager.networkOperatorName
    }

    fun isNetworkRoaming(): Boolean {
        return telephonyManager.isNetworkRoaming
    }

    fun getSimOperatorName(): String {
        return telephonyManager.simOperatorName
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getSignalStrength(): Int {
        return if (isPhoneStatePermissionGranted()) {
            try {
                telephonyManager.signalStrength?.level ?: -1
            } catch (e: Exception) {
                Log.e("TelephonyManager", "Error getting signal strength", e)
                -1
            }
        } else {
            -1
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getNetworkType(): String {
        return if (isPhoneStatePermissionGranted()) {
            when (telephonyManager.dataNetworkType) {
                TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA -> "3G"
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_GPRS -> "2G"
                else -> "Unknown"
            }
        } else {
            "Unknown"
        }
    }
}
