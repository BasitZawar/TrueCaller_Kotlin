package com.lads.truecaller.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lads.truecaller.PageAdapter
import com.lads.truecaller.databinding.ActivityRecentBinding
import com.lads.truecaller.model.RecentContactsModel
import com.lads.truecaller.model.SearchViewModel


class RecentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentBinding
    private var recentContactArrayList: ArrayList<RecentContactsModel>? = null
    val searchViewModel: SearchViewModel by viewModels()

//    private fun viewModels(): ReadOnlyProperty<RecentActivity, SearchViewModel> {
//        return viewModels()
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageViewSearch.setOnClickListener {
            binding.layoutSearchBox.isVisible = true
            binding.imageViewDrawer.isVisible = false
            binding.tvTrueCaller.isVisible = false
        }
        binding.searchBox.setOnClickListener {
            binding.layoutSearchBox.isVisible = false
            binding.imageViewDrawer.isVisible = true
            binding.tvTrueCaller.isVisible = true
        }

        binding.viewPager.adapter = PageAdapter(supportFragmentManager)
        binding.tablayout.setupWithViewPager(binding.viewPager)

        binding.idFAB.setOnClickListener {
            val intent = Intent(applicationContext, DialpadActivity::class.java)
            startActivity(intent)
        }

        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    searchViewModel.searchQuery(it.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty()) {
                    searchViewModel.searchQuery("")
                }
            }
        })

    }
}