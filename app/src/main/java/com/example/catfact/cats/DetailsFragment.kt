package com.example.catfact.cats

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.catfact.MainActivity

import com.example.catfact.R
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToUI()
    }

    private fun subscribeToUI() {
        more_details_button.setOnClickListener {
            viewModel.details()
        }
    }
}
