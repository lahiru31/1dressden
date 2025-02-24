package com.khadar3344.myshop.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.khadar3344.myshop.data.network.repository.NetworkRepository
import com.khadar3344.myshop.notifications.NotificationHelper
import com.khadar3344.myshop.util.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val networkRepository: NetworkRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("SyncDataWorker", "Starting sync operation")
            
            // Perform background sync operations
            when (val result = networkRepository.getProductsListFromApi()) {
                is Resource.Success -> {
                    // Show notification on successful sync
                    notificationHelper.showNotification(
                        "Sync Complete",
                        "Products data has been updated successfully",
                        SYNC_NOTIFICATION_ID
                    )
                    Result.success()
                }
                is Resource.Failure<*> -> {
                    Log.e("SyncDataWorker", "Error during sync", result.exception as? Throwable)
                    Result.retry()
                }
                is Resource.Loading -> {
                    Log.d("SyncDataWorker", "Loading data...")
                    Result.retry()
                }
                is Resource.Idle -> {
                    Log.d("SyncDataWorker", "Worker is idle")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Log.e("SyncDataWorker", "Error during sync", e)
            Result.retry()
        }
    }

    companion object {
        private const val SYNC_NOTIFICATION_ID = 100
    }
}
