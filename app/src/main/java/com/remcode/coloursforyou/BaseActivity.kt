package com.remcode.coloursforyou

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentContainerView

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colours)
    }

    @SuppressLint("InflateParams")
    override fun setContentView(layoutResID: Int) {
        val coordinatorLayout: CoordinatorLayout = layoutInflater.inflate(R.layout.activity_base,
            null) as CoordinatorLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.activity_container)
        progressBar = coordinatorLayout.findViewById(R.id.progress_bar) as ProgressBar

        layoutInflater.inflate(layoutResID, activityContainer, true)

        super.setContentView(coordinatorLayout)
    }
}
