package com.critx.shwemiAdmin.workerManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.auth.RefreshTokenUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect

@HiltWorker
class RefreshTokenWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val localDatabase: LocalDatabase
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val REFRESH_TOKEN_WORK = "com.unity.cityhr:refresh-token-work"
    }

    override suspend fun doWork(): Result {
        Log.i("refresh-token-work", "do work reached")
        var isWorkFinished :Boolean?= null
          refreshTokenUseCase(localDatabase.getToken().orEmpty()).collect { result->
               when(result){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        localDatabase.deleteToken()
                        localDatabase.saveToken(result.data?.token.orEmpty())
                        Log.i("refresh-token-work", "token refreshed")
                        isWorkFinished = true
//                        Result.success()
                    }
                    is Resource.Error->{
                        Log.i("refresh-token-work", "token refresh failed-> ${result.message}")
                        isWorkFinished = false
//                        Result.retry()
                    }
                }
            }
        return isWorkFinished?.let {
            if (it){
                Result.success()
            }else{
                Result.retry()
            }
        }?:Result.retry()
    }

}