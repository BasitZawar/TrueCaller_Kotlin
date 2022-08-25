package com.lads.truecaller.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lads.truecaller.PageAdapter
import com.lads.truecaller.databinding.ActivityRecentBinding
import com.lads.truecaller.model.RecentContactsModel


class RecentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentBinding
    private var recentContactArrayList: ArrayList<RecentContactsModel>? = null


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
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    recentContactArrayList?.let {
//                        adapter!!.submitList(it)
                    }
                    return
                }
//                GlobalScope.launch(Dispatchers.Main) {
//                    recentContactArrayList?.filter {
//                        it.recentContactName?.lowercase(Locale.getDefault())
//                            ?.contains(s.toString().lowercase(Locale.getDefault())) == true
//                    }
//                        .apply {
//                            adapter.submitList(this?.toMutableList() as ArrayList<RecentContactsModel>)
//                        }
//                }
                Toast.makeText(applicationContext, s.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}