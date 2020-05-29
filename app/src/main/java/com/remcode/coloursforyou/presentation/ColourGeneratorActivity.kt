package com.remcode.coloursforyou.presentation

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.remcode.coloursforyou.R
import com.remcode.coloursforyou.business.ColourGeneratorViewModel
import com.remcode.coloursforyou.business.ViewModelFactory
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.utils.NetworkStatus
import com.remcode.coloursforyou.utils.NetworkStatusLiveData
import com.remcode.coloursforyou.utils.capitalize
import kotlinx.android.synthetic.main.activity_colour_generator.*


class ColourGeneratorActivity : AppCompatActivity() {

    private var connected: Boolean = false
    private lateinit var viewModel : ColourGeneratorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colour_generator)

        viewModel = ViewModelProvider(this, ViewModelFactory(this.application)).get(ColourGeneratorViewModel::class.java)

        // initialise the Network Status detector
        NetworkStatusLiveData.init(application)

        val (soundPool, splatSoundId) = buildSoundPool()

        observeWordChanges()

        observeLoadingStatus()

        observeSplatFxCue(soundPool, splatSoundId)

        observeNetworkConnectivity()

        fab.setOnClickListener {
            val randomColour = viewModel.getRandomHexColour()

            if(connected) {
                viewModel.getRandomWord()

                if(randomColour != null && viewModel.word.value != null)
                    viewModel.insert(Colour(randomColour, viewModel.word.value!![0]))
                else {
                    // do something
                }

                performAnimation()
                paint_and_brush_layout.visibility = View.VISIBLE
                // change the view colours
                splat.setColorFilter(Color.parseColor(randomColour))
                paint.backgroundTintList = ColorStateList.valueOf(Color.parseColor(randomColour))
                fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(randomColour))
            }else {
                displayUnableToConnectDialog()
            }
        }
    }

    private fun observeSplatFxCue(soundPool: SoundPool, splatSoundId: Int) {
        val splatFx = viewModel.playSplatFx
        splatFx.observe(this, Observer { playSound ->
            if (playSound) {
                soundPool.play(splatSoundId, 1f, 1f, 1, 0, 1f)
            }
        })
    }

    private fun buildSoundPool(): Pair<SoundPool, Int> {
        // build a sound pool
        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        // load a sound
        val splatSoundId = soundPool.load(this, R.raw.splat_fx, 1)
        return Pair(soundPool, splatSoundId)
    }

    private fun observeWordChanges() {
        val randomWord = viewModel.word
        randomWord.observe(this, Observer { words ->
            colour_name.text = getString(R.string.colour_sample, words[0].capitalize())
        })
    }

    private fun observeLoadingStatus() {
        val loading = viewModel.loading
        loading.observe(this, Observer { loading ->
            if (loading) {
                splat.visibility = View.INVISIBLE
                colour_name.visibility = View.INVISIBLE
            } else {
                splat.visibility = View.VISIBLE
                colour_name.visibility = View.VISIBLE
            }
        })
    }

    private fun performAnimation() {
        val animator = ValueAnimator.ofFloat(-0.54f, 0.8f)
        animator.duration = 2000L
        animator.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Float
            paint.translationX = -paint.width * (1.0f - progress)
            brush.translationX = paint.width * progress
        }
        animator.start()
    }

    private fun observeNetworkConnectivity() {
        NetworkStatusLiveData.observe(this, Observer {
            if (it == NetworkStatus.AVAILABLE) connected = true
            if (it == NetworkStatus.UNAVAILABLE) connected = false
            if (it == NetworkStatus.LOST) connected = false
        })
    }

    private fun displayUnableToConnectDialog() {
        FailedConnectionDialogFragment()
            .show(this.supportFragmentManager, "UnableToConnect")
    }
}
