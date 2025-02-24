package com.khadar3344.myshop.ui.home.screens.profile_screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.core.content.ContextCompat
import com.khadar3344.myshop.R
import com.khadar3344.myshop.components.CustomAppBar
import com.khadar3344.myshop.components.CustomDefaultBtn
import com.khadar3344.myshop.model.User
import com.khadar3344.myshop.telephony.TelephonyManager
import com.khadar3344.myshop.multimedia.MediaManager
import com.khadar3344.myshop.ui.home.component.Error
import com.khadar3344.myshop.ui.home.component.Loading
import com.khadar3344.myshop.util.Dimensions
import com.khadar3344.myshop.util.Resource
import java.io.File

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    telephonyManager: TelephonyManager,
    mediaManager: MediaManager,
    logout: () -> Unit,
    onBackBtnClick: () -> Unit,
    onMapClick: () -> Unit
) {
    val profileState by viewModel.userInfo.collectAsState()
    val context = LocalContext.current
    ProfileScreenContent(
        profileState = profileState,
        telephonyManager = telephonyManager,
        mediaManager = mediaManager,
        context = context,
        logout = {
            viewModel.logout()
            logout()
        },
        updateData = { user ->
            viewModel.updateUserInfoFirebase(user = user)
            Toast.makeText(context, "Update user data successfully", Toast.LENGTH_SHORT).show()
        },
        onBackBtnClick = onBackBtnClick,
        onMapClick = onMapClick
    )
}

@Composable
fun ProfileScreenContent(
    profileState: Resource<User>?,
    telephonyManager: TelephonyManager,
    mediaManager: MediaManager,
    context: android.content.Context,
    logout: () -> Unit,
    updateData: (User) -> Unit,
    onBackBtnClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (profileState) {
            is Resource.Success -> {
                SuccessScreen(
                    profileState = profileState.data,
                    telephonyManager = telephonyManager,
                    mediaManager = mediaManager,
                    context = context,
                    logout = logout,
                    updateData = updateData,
                    onBackBtnClick = onBackBtnClick,
                    onMapClick = onMapClick
                )
            }
            is Resource.Failure<*> -> {
                Error(message = profileState.exception.toString())
            }
            is Resource.Loading -> {
                Loading()
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(
    profileState: User,
    telephonyManager: TelephonyManager,
    mediaManager: MediaManager,
    context: android.content.Context,
    logout: () -> Unit,
    updateData: (User) -> Unit,
    onBackBtnClick: () -> Unit,
    onMapClick: () -> Unit
) {
    var name: String by remember { mutableStateOf(profileState.name) }
    var phone: String by remember { mutableStateOf(profileState.phone) }
    var address: String by remember { mutableStateOf(profileState.address) }
    val nameErrorState = remember { mutableStateOf(false) }
    val phoneNumberErrorState = remember { mutableStateOf(false) }
    val addressErrorState = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    
    // Media states
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var recordedFile by remember { mutableStateOf<File?>(null) }

    // Permission states
    var hasCallPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    ) }
    var hasAudioPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    ) }

    // Permission launchers
    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCallPermission = isGranted
        if (isGranted && phone.isNotEmpty()) {
            telephonyManager.makePhoneCall(phone)
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasAudioPermission = isGranted
        if (isGranted) {
            val file = File(context.cacheDir, "voice_note.mp3")
            mediaManager.startRecording(file)
            recordedFile = file
            isRecording = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spacing_medium)
            .verticalScroll(scrollState)
    ) {
        CustomAppBar(
            onBackBtnClick = onBackBtnClick,
            appBarTitle = "Profile"
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spacing_large))

        Text(
            text = profileState.email,
            modifier = Modifier.padding(Dimensions.spacing_small),
            fontSize = Dimensions.text_large,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_medium))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name", fontSize = Dimensions.text_medium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Name"
                )
            },
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            isError = nameErrorState.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_small))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(text = "Phone Number", fontSize = Dimensions.text_medium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = "Phone",
                    modifier = Modifier.clickable {
                        if (phone.isNotEmpty()) {
                            if (hasCallPermission) {
                                telephonyManager.makePhoneCall(phone)
                            } else {
                                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        } else {
                            Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            },
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            isError = phoneNumberErrorState.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_small))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(text = "Address", fontSize = Dimensions.text_medium) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.location_point),
                    contentDescription = "Address",
                    modifier = Modifier.clickable { onMapClick() }
                )
            },
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            isError = addressErrorState.value,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacing_large))

        // Voice Note Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing_small)
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimensions.spacing_medium)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Voice Notes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = Dimensions.spacing_small)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Record Button
                    IconButton(
                        onClick = {
                            if (!isRecording) {
                                if (hasAudioPermission) {
                                    val file = File(context.cacheDir, "voice_note.mp3")
                                    mediaManager.startRecording(file)
                                    recordedFile = file
                                    isRecording = true
                                } else {
                                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            } else {
                                mediaManager.stopRecording()
                                isRecording = false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MicNone,
                            contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                            tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }

                    // Play Button
                    IconButton(
                        onClick = {
                            if (!isPlaying && recordedFile != null) {
                                mediaManager.startPlaying(Uri.fromFile(recordedFile))
                                isPlaying = true
                            } else {
                                mediaManager.stopPlaying()
                                isPlaying = false
                            }
                        },
                        enabled = recordedFile != null
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.StopCircle else Icons.Filled.PlayCircle,
                            contentDescription = if (isPlaying) "Stop Playing" else "Start Playing"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.spacing_medium))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing_medium)
        ) {
            CustomDefaultBtn(shapeSize = 50f, btnText = "Update") {
                val isNameValid = name.isEmpty() || name.length < 3
                val isPhoneValid = phone.isEmpty() || phone.length < 4
                val isAddressValid = address.isEmpty() || address.length < 5
                nameErrorState.value = isNameValid
                phoneNumberErrorState.value = isPhoneValid
                addressErrorState.value = isAddressValid
                if (!isNameValid && !isPhoneValid && !isAddressValid) {
                    updateData(
                        User(
                            name = name,
                            phone = phone,
                            address = address,
                            email = profileState.email
                        )
                    )
                }
            }

            CustomDefaultBtn(shapeSize = 50f, btnText = "View on Map") {
                onMapClick()
            }

            CustomDefaultBtn(shapeSize = 50f, btnText = "Logout") {
                logout()
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.spacing_large))
    }
}
