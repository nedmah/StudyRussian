package com.example.stydyrussian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.stydyrussian.databinding.ActivityMainBinding
import com.example.stydyrussian.notification.NotifyWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment(PracticeFragment.newInstance(null, null))
        binding.navigation.selectedItemId = R.id.practiceFragment

        val workRequest = PeriodicWorkRequestBuilder<NotifyWorker>(7,TimeUnit.DAYS).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "weekly_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        binding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.theoryFragment -> {
                    openFragment(TheoryFragment.newInstance(null, null))
                }

                R.id.practiceFragment -> {
                    openFragment(PracticeFragment.newInstance(null, null))
                }

                R.id.profileFragment -> {
                    openFragment(ProfileFragment.newInstance(null, null))
                }
            }
            true
        }
    }
}

