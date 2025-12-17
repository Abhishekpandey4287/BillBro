package com.example.billbro.screens.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.billbro.R
import com.example.billbro.databinding.ActivityMainBinding
import com.example.billbro.databinding.ActivitySettleBinding
import com.example.billbro.viewmodel.SettlementViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettleBinding

    private lateinit var vm: SettlementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[SettlementViewModel::class.java]
        binding.btnSettle.setOnClickListener {
            vm.settle(
                binding.etFrom.text.toString(),
                binding.etTo.text.toString(),
                binding.etAmount.text.toString().toDouble()
            )
            finish()
        }

    }
}