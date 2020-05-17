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
import com.example.catfact.dialogs.ProgressDialog
import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import kotlinx.android.synthetic.main.fragment_cats.*
import javax.inject.Inject

class CatsFragment : Fragment() {

    @Inject
    lateinit var viewModel: CatsViewModel

    private val catsIdsAdapter = CatsIdsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        injectDependencies()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToViewModel()
        downloadCatFacts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hideBackButton()

        return inflater.inflate(R.layout.fragment_cats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeToUI()
    }

    private fun injectDependencies() {
        (activity as MainActivity).catFactsComponent.inject(this)
    }

    private fun subscribeToViewModel() {
        setCatFactsObserver()
        setLoadingObserver()
    }

    private fun setCatFactsObserver() {
        viewModel.randomCatFactsData().observe(this, randomCatFactsObserver)
    }

    private val randomCatFactsObserver = Observer<Result<List<CatFact>>> { result ->
        when (result) {
            is Result.Success -> {
                if (result.data != null)
                    catsIdsAdapter.loadCatFacts(result.data)
            }
            is Result.Failure -> showError(result.error.code)
        }
    }

    private fun setLoadingObserver() {
        viewModel.loadingStatusData().observe(this, loadingStatusObserver)
    }

    private val loadingStatusObserver = Observer<Boolean> { isLoading ->
        when (isLoading) {
            true -> showProgressBar()
            false -> hideProgressBar()
        }
    }

    private fun downloadCatFacts() {
        showProgressBar()
        viewModel.downloadCatFacts(NUM_OF_FACTS)
        hideProgressBar()
    }

    private fun hideBackButton() {
        setHasOptionsMenu(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initRecyclerView() {
        cats_ids_list.layoutManager = LinearLayoutManager(context)
        cats_ids_list.adapter = catsIdsAdapter
    }

    private fun subscribeToUI() {
        setListAdapterListener()
        setMoreButtonListener()
    }

    private fun setListAdapterListener() {
        catsIdsAdapter.setOnItemClickListener(object : CatsIdsAdapter.OnItemClickListener {
            override fun onItemClick(catFact: CatFact) {
                viewModel.onCatFactChosen(catFact)
                navigateToDetailsScreen()
            }
        })
    }

    private fun navigateToDetailsScreen() {
        val direction = CatsFragmentDirections.actionCatsFragmentToDetailsFragment()
        findNavController().navigate(direction)
    }

    private fun setMoreButtonListener() {
        more_facts_button.setOnClickListener {
            downloadCatFacts()
        }
    }

    private fun showProgressBar() = ProgressDialog.show(context!!)

    private fun hideProgressBar() = ProgressDialog.hide()

    private fun showError(code: Int) =
        Toast.makeText(context, "Error! Code: $code", Toast.LENGTH_LONG).show()

    companion object {
        private const val NUM_OF_FACTS: Int = 30
    }
}
