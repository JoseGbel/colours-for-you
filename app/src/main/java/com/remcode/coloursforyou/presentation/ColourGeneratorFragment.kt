package com.remcode.coloursforyou.presentation

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.remcode.coloursforyou.R
import com.remcode.coloursforyou.business.ColourGeneratorViewModel
import com.remcode.coloursforyou.business.ColourGeneratorViewModelFactory
import com.remcode.coloursforyou.utils.NetworkStatus
import com.remcode.coloursforyou.utils.NetworkStatusLiveData
import com.remcode.coloursforyou.utils.capitalize
import kotlinx.android.synthetic.main.fragment_colour_generator.*

class ColourGeneratorFragment : Fragment() {

    private var isFirstSplat: Boolean = true
    private var connected: Boolean = false
    private lateinit var viewModel: ColourGeneratorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_colour_generator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, ColourGeneratorViewModelFactory(
                requireActivity().application
            )
        ).get(ColourGeneratorViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val (soundPool, splatSoundId) = buildSoundPool()

        observeLoadingStatus()

        observeCommands(soundPool, splatSoundId)

        observeNetworkConnectivity()

        observeColourLiveData()

        fab.setOnClickListener {
            val randomColour = viewModel.generateRandomHexColour()

            if (connected) {
                val name = viewModel.fetchNewColour(randomColour)

                performAnimation()

                changeViewColours(randomColour)
            } else {
                displayUnableToConnectDialog()
            }
        }
    }

    private fun observeColourLiveData() {
        viewModel.colourLiveData.observe(viewLifecycleOwner, Observer { colour ->
            colour_name.text = getString(R.string.colour_sample, colour.name.capitalize())
            viewModel.insert(colour)
        })
    }

    private fun changeViewColours(randomColour: String?) {
        // change the view colours
        splat.setColorFilter(Color.parseColor(randomColour))
        paint.backgroundTintList = ColorStateList.valueOf(Color.parseColor(randomColour))
        fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(randomColour))
    }

    private fun observeCommands(soundPool: SoundPool, splatSoundId: Int) {
        viewModel.command.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ColourGeneratorViewModel.Command.PlaySoundEffect ->
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
        val splatSoundId = soundPool.load(context, R.raw.splat_fx, 1)
        return Pair(soundPool, splatSoundId)
    }

    private fun observeLoadingStatus() {
        val loading = viewModel.loadingLiveData
        loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                fab.isClickable = false
                splat.visibility = View.INVISIBLE
                colour_name.visibility = View.INVISIBLE
            } else {
                if (!isFirstSplat) {
                    fab.isClickable = true
                    splat.visibility = View.VISIBLE
                    colour_name.visibility = View.VISIBLE
                }
                isFirstSplat = false
            }
        })
    }

    private fun performAnimation() {
        paint_and_brush_layout.visibility = View.VISIBLE
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
        NetworkStatusLiveData.observe(viewLifecycleOwner, Observer {
            if (it == NetworkStatus.AVAILABLE) connected = true
            if (it == NetworkStatus.UNAVAILABLE) connected = false
            if (it == NetworkStatus.LOST) connected = false
        })
    }

    private fun displayUnableToConnectDialog() {
        FailedConnectionDialogFragment()
            .show(requireActivity().supportFragmentManager, "UnableToConnect")
    }
}