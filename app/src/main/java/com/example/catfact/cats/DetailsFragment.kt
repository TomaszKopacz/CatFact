package com.example.catfact.cats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.catfact.MainActivity
import com.example.catfact.R
import com.example.catfact.model.CatFact
import com.example.catfact.util.date.DateConverters
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject


class DetailsFragment : Fragment() {

    @Inject
    lateinit var viewModel: CatsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        injectDependencies()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        showBackButton()

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    private fun injectDependencies() {
        (activity as MainActivity).catFactsComponent.inject(this)
    }

    private fun subscribeToViewModel() {
        setChosenCatFactObserver()
    }

    private fun setChosenCatFactObserver() =
        viewModel.chosenCatFactObservable().observe(this, chosenCatObserver)


    private val chosenCatObserver = Observer<CatFact> { catFact ->
        showFact(catFact)
    }

    private fun showFact(catFact: CatFact) {
        details_text.text = catFact.text
        date_text.text = DateConverters().niceFormat(catFact.updatedAt)
    }

    private fun showBackButton() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
