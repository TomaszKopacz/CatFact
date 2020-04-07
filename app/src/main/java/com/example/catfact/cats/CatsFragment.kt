package com.example.catfact.cats


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.catfact.MainActivity
import com.example.catfact.R
import com.example.catfact.data.Result
import kotlinx.android.synthetic.main.fragment_cats.*
import javax.inject.Inject

class CatsFragment : Fragment() {

    @Inject
    lateinit var viewModel: CatsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).catFactsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBar()

        subscribeToUI()
        subscribeToViewModel()
    }

    private fun subscribeToUI() {
        more_facts_button.setOnClickListener {
            showProgressBar()
            viewModel.downloadCats()
        }
    }

    private fun subscribeToViewModel() {
        viewModel.catFactsResult().observe(this, Observer { result ->

            when (result) {
                is Result.Success -> {
                    for (fact in result.data) {
                        Log.d("CatFact", fact.id)
                    }
                }

                is Result.Failure -> {

                }
            }

            hideProgressBar()

        })
    }

    private fun navigateToDetailsScreen() {
        val direction = CatsFragmentDirections.actionCatsFragmentToDetailsFragment()
        findNavController().navigate(direction)
    }

    private fun showProgressBar() {
        Log.d("CatFact", "Show Progress Bar")
    }

    private fun hideProgressBar() {
        Log.d("CatFact", "Hide Progress Bar")
    }
}
