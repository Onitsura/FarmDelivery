package com.onitsura12.farmdel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.FragmentMainBinding
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.initFirebase


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel = MainViewModel()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initFirebase()
            val bottomNavigationView = mainBottomNavigationView
            val navController = (childFragmentManager.findFragmentById(R.id.mainContainerView) as
                    NavHostFragment).navController
            NavigationUI.setupWithNavController(bottomNavigationView, navController =
            navController)
        }
    }




}