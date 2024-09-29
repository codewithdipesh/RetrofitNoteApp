package com.example.notesappretrofit.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

//@Singleton
//class HiltWorkerFactory @Inject constructor(
//    private val workerFactories:Map<Class<out ListenableWorker>,
//            Provider<ChildWorkerFactory>>
//) :WorkerFactory()
//{
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker? {
//        val workerClass = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
//        val factoryProvider = workerFactories[workerClass] ?: return null
//        return factoryProvider.get().create(appContext,workerParameters)
//    }
//}
//
//interface ChildWorkerFactory {
//    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
//}
