package com.dhs.tools.loadview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhs.tools.loadview.adapter.NewsAdapter
import com.dhs.tools.loadview.adapter.PersonAdapter
import com.dhs.tools.loadview.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {
    private var mType: String? = null

    private val binding by lazy { ActivityRecyclerviewBinding.inflate(layoutInflater) }
    private val list by lazy {
        val data = mutableListOf<String>()
        for (i in 0..9) {
            data.add(i.toString())
        }
        data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mType = intent.getStringExtra(PARAMS_TYPE)
        init()
    }


    private fun init() {
        binding.apply {
            if (TYPE_LINEAR == mType) {
                recyclerView.layoutManager =
                    LinearLayoutManager(
                        this@RecyclerViewActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                val adapter = NewsAdapter()
                recyclerView.adapter = adapter
                recyclerView.postDelayed({
                    adapter.notifyDataChanged(list)
                }, 3000)
                return
            }
            if (TYPE_GRID == mType) {
                recyclerView.layoutManager = GridLayoutManager(this@RecyclerViewActivity, 2)
                val adapter = PersonAdapter()
                recyclerView.adapter = adapter
                recyclerView.postDelayed({
                    adapter.notifyDataChanged(list)
                }, 3000)
            }
        }
    }

    companion object {
        private const val PARAMS_TYPE = "params_type"
        const val TYPE_LINEAR: String = "type_linear"
        const val TYPE_GRID: String = "type_grid"
        fun start(context: Context, type: String?) {
            val intent = Intent(context, RecyclerViewActivity::class.java)
            intent.putExtra(PARAMS_TYPE, type)
            context.startActivity(intent)
        }
    }
}

