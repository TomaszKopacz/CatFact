package com.example.catfact.cats


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catfact.MainActivity
import com.example.catfact.R
import com.example.catfact.data.Result
import com.example.catfact.dialogs.ProgressDialog
import com.example.catfact.model.CatFact
import kotlinx.android.synthetic.main.fragment_cats.*
import javax.inject.Inject

class CatsFragment : Fragment() {

    @Inject
    lateinit var viewModel: CatsViewModel

    private val catsIdsAdapter = CatsIdsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).catFactsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hideBackButton()

        return inflater.inflate(R.layout.fragment_cats, container, false)
    }

    private fun hideBackButton() {
        setHasOptionsMenu(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        subscribeToUI()
        subscribeToViewModel()
    }

    private fun initRecyclerView() {
        cats_ids_list.layoutManager = LinearLayoutManager(context)
        cats_ids_list.adapter = catsIdsAdapter
    }

    private fun subscribeToUI() {
        setListAdapterListener()
        setButtonListener()
    }

    private fun setListAdapterListener() {
        catsIdsAdapter.setOnItemClickListener(object : CatsIdsAdapter.OnItemClickListener {
            override fun onItemClick(catFact: CatFact) {
                viewModel.onCatFactChosen(catFact)
                navigateToDetailsScreen()
            }
        })
    }

    private fun setButtonListener() {
        more_facts_button.setOnClickListener {
            viewModel.downloadCatFacts()
        }
    }

    private fun subscribeToViewModel() {
        setCatFactsObserver()
    }

    private fun setCatFactsObserver() {
        viewModel.catFactsObservable().observe(this, catFactObserver)
    }

    private val catFactObserver = Observer<Result<List<CatFact>>> { result ->

        when (result) {
            is Result.Loading -> {
                showProgressBar()
            }

            is Result.Success -> {
                catsIdsAdapter.loadCatFacts(result.data)
                hideProgressBar()
            }

            is Result.Warning -> {
                showMessage(result.message.text)
            }

            is Result.Failure -> {
                showMessage(result.message.text)
                hideProgressBar()
            }
        }
    }

    private fun navigateToDetailsScreen() {
        val direction = CatsFragmentDirections.actionCatsFragmentToDetailsFragment()
        findNavController().navigate(direction)
    }

    private fun showProgressBar() {
        ProgressDialog.show(context!!)
    }

    private fun hideProgressBar() {
        ProgressDialog.hide()
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
