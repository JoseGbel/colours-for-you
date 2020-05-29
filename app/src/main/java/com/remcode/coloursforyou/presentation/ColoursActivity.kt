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
import com.remcode.coloursforyou.business.ColoursViewModel
import com.remcode.coloursforyou.utils.capitalize
import kotlinx.android.synthetic.main.activity_colours.*


class ColoursActivity : AppCompatActivity() {

    private lateinit var viewModel : ColoursViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colours)

        viewModel = ViewModelProvider(this).get(ColoursViewModel::class.java)

        val (soundPool, splatSoundId) = buildSoundPool()

        fab.setOnClickListener {
            val randomColour = viewModel.getRandomHexColour()

            viewModel.getRandomWord()
            observeWordChanges()

            observeLoadingStatus()

            val splatFx = viewModel.playSplatFx
            splatFx.observe(this, Observer { playSound ->
                if (playSound) {
                    soundPool.play(splatSoundId, 1f, 1f, 1, 0,1f)
                }
            })

            performAnimation()
            paint_and_brush_layout.visibility = View.VISIBLE
            // change the view colours
            splat.setColorFilter(Color.parseColor(randomColour))
            paint.backgroundTintList = ColorStateList.valueOf(Color.parseColor(randomColour))
            fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(randomColour))
        }
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
}
