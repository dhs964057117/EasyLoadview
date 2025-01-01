package com.dhs.tools.loadview

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dhs.tools.loadview.databinding.ActivityMainBinding
import com.dhs.tools.shimmer.Shimmer
import com.dhs.tools.shimmer.ShimmerImageView
import com.dhs.tools.shimmer.ShimmerTextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentPreset = -1
    private var toast: Toast? = null
    private var isColorBuilder = true
    private var shimmerEnable = false
    private var loadingEnable = false
    private val btnLoadingText by lazy { getString(R.string.press_to_loading_finish) }
    private val btnLoadedText by lazy { getString(R.string.press_to_loading) }
    private var isLoading = true
    private lateinit var presetButtons: Array<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnList.setOnClickListener {
            RecyclerViewActivity.start(
                this@MainActivity, RecyclerViewActivity.TYPE_LINEAR
            )
        }
        binding.btnGrid.setOnClickListener {
            RecyclerViewActivity.start(
                this@MainActivity, RecyclerViewActivity.TYPE_GRID
            )
        }
        binding.btnLoad.setOnClickListener {
            if (!isLoading) {
                if (shimmerEnable) {
                    showOrHideShimmerStyle(true)
                }
                if (loadingEnable) {
                    showOrHideLoadingStyle(true)
                }
            } else {
                if (shimmerEnable) {
                    showOrHideShimmerStyle(false)
                }
                if (loadingEnable) {
                    showOrHideLoadingStyle(false)
                }
            }
            isLoading = !isLoading
            binding.btnLoad.text = if (isLoading) btnLoadingText else btnLoadedText
            val current = currentPreset
            selectPreset(currentPreset + 1, false)
            selectPreset(current, false)
        }
        presetButtons =
            arrayOf(
                binding.presetButton0,
                binding.presetButton1,
                binding.presetButton2,
                binding.presetButton3,
                binding.presetButton4,
                binding.presetButton5,
                binding.presetButton6
            )
        presetButtons.forEach { it.setOnClickListener(this@MainActivity) }
        selectPreset(0, false)
        binding.color.setOnCheckedChangeListener { buttonView, isChecked ->
            isColorBuilder = isChecked
            val current = currentPreset
            selectPreset(currentPreset + 1, false)
            selectPreset(current, false)
        }
        binding.alpha.setOnCheckedChangeListener { buttonView, isChecked ->
            isColorBuilder = !isChecked
            val current = currentPreset
            selectPreset(currentPreset + 1, false)
            selectPreset(current, false)
        }
        binding.shimmer.setOnCheckedChangeListener { buttonView, isChecked ->
            shimmerEnable = isChecked
            val current = currentPreset
            selectPreset(currentPreset + 1, false)
            selectPreset(current, false)
        }
        binding.load.setOnCheckedChangeListener { buttonView, isChecked ->
            loadingEnable = isChecked
            val current = currentPreset
            selectPreset(currentPreset + 1, false)
            selectPreset(current, false)
        }
        binding.color.isChecked = true
        binding.load.isChecked = true
        showOrHideLoadingStyle(true)
    }

    private fun selectPreset(preset: Int, showToast: Boolean) {
        if (!isLoading) return
        if (currentPreset == preset) {
            return
        }

        if (currentPreset >= 0) {
            presetButtons[currentPreset].setBackgroundResource(R.color.preset_button_background)
        }
        currentPreset = preset
        presetButtons[currentPreset].setBackgroundResource(R.color.preset_button_background_selected)

        // If a toast is already showing, hide it
        toast?.cancel()
        val shimmerBuilder =
            if (isColorBuilder)
                Shimmer.ColorHighlightBuilder().setBaseColor(Color.parseColor("#16000000"))
                    .setHighlightColor(Color.parseColor("#ffffff"))
                    .setDuration(1000)
            else Shimmer.AlphaHighlightBuilder().setDuration(1000)
        val shimmer =
            when (preset) {
                1 -> {
                    // Slow and reverse
                    toast = Toast.makeText(this, "Slow and reverse", Toast.LENGTH_SHORT)
                    shimmerBuilder.setDuration(5000L).setRepeatMode(ValueAnimator.REVERSE)
                }

                2 -> {
                    // Thin, straight and transparent
                    toast =
                        Toast.makeText(this, "Thin, straight and transparent", Toast.LENGTH_SHORT)
                    shimmerBuilder.setBaseAlpha(0.1f).setDropoff(0.1f).setTilt(0f)
                }

                3 -> {
                    // Sweep angle 90
                    toast = Toast.makeText(this, "Sweep angle 90", Toast.LENGTH_SHORT)
                    shimmerBuilder.setDirection(Shimmer.Direction.TOP_TO_BOTTOM).setTilt(0f)
                }

                4 -> {
                    // Spotlight
                    toast = Toast.makeText(this, "Spotlight", Toast.LENGTH_SHORT)
                    shimmerBuilder
                        .setBaseAlpha(0f)
                        .setDuration(2000L)
                        .setDropoff(0.1f)
                        .setIntensity(0.35f)
                        .setShape(Shimmer.Shape.RADIAL)
                }

                5 -> {
                    // Spotlight angle 45
                    toast = Toast.makeText(this, "Spotlight angle 45", Toast.LENGTH_SHORT)
                    shimmerBuilder
                        .setBaseAlpha(0f)
                        .setDuration(2000L)
                        .setDropoff(0.1f)
                        .setIntensity(0.35f)
                        .setTilt(45f)
                        .setShape(Shimmer.Shape.RADIAL)
                }

                6 -> {
                    // Off
                    toast = Toast.makeText(this, "Off", Toast.LENGTH_SHORT)
                    null
                }

                else -> {
                    toast = Toast.makeText(this, "Default", Toast.LENGTH_SHORT)
                    shimmerBuilder
                }
            }?.build()
        binding.example.tvTitle.setShimmer(shimmer)
        binding.example.tvContent.setShimmer(shimmer)
        binding.example.tvTime.setShimmer(shimmer)
        binding.example.imgNews.setShimmer(shimmer)
        Log.d("ShimmerTextView", "shimmerEnable:$shimmerEnable loadingEnable:$loadingEnable")
        if (binding.shimmer.isChecked) {
            showOrHideLoadingStyle(loadingEnable)
            showOrHideShimmerStyle(shimmerEnable)
        } else {
            showOrHideShimmerStyle(shimmerEnable)
            showOrHideLoadingStyle(loadingEnable)
        }
        // Show toast describing the chosen preset, if necessary
        if (showToast) {
            toast?.show()
        }
    }

    private fun showOrHideShimmerStyle(show: Boolean) {
        if (show) {
            binding.example.tvTitle.showShimmer(true)
            binding.example.tvContent.showShimmer(true)
            binding.example.tvTime.showShimmer(true)
            binding.example.imgNews.showShimmer(true)
        } else {
            binding.example.tvTitle.hideShimmer()
            binding.example.tvContent.hideShimmer()
            binding.example.tvTime.hideShimmer()
            binding.example.imgNews.hideShimmer()
        }
    }

    private fun showOrHideLoadingStyle(show: Boolean) {
        if (show) {
            binding.example.tvTitle.showLoading()
            binding.example.tvContent.showLoading()
            binding.example.tvTime.showLoading()
            binding.example.imgNews.showLoading()
        } else {
            binding.example.tvTitle.hideLoading()
            binding.example.tvContent.hideLoading()
            binding.example.tvTime.hideLoading()
            binding.example.imgNews.hideLoading()
        }
    }

    override fun onClick(v: View?) {
        selectPreset(presetButtons.indexOf(v as Button), true)
    }
}
