package com.example.okaytravel.activities.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.TripActivity
import com.example.okaytravel.adapters.NotTakenThingsRecyclerViewAdapter
import com.example.okaytravel.adapters.TakenThingsRecyclerViewAdapter
import com.example.okaytravel.adapters.thingitems.ThingItem
import com.example.okaytravel.adapters.thingitems.ThingListItem
import com.example.okaytravel.models.ThingModel
import com.example.okaytravel.presenters.ThingsPresenter
import com.example.okaytravel.views.ThingsView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_thing_add.view.*
import kotlinx.android.synthetic.main.fragment_things.*

class ThingsFragment: BaseFragment(false), ThingsView {

    override val fragmentNameResource: Int
        get() = R.string.things

    @ProvidePresenter
    fun provideThingsPresenter(): ThingsPresenter {
        return ThingsPresenter(this.requireContext(), (activity as TripActivity).trip)
    }

    @InjectPresenter
    lateinit var thingsPresenter: ThingsPresenter

    private val takenThingsData: MutableList<ThingListItem> = mutableListOf()
    private val notTakenThingsData: MutableList<ThingListItem> = mutableListOf()
    private val changeThingsList: MutableList<ThingModel> = mutableListOf()
    private lateinit var takenThingsAdapter: TakenThingsRecyclerViewAdapter
    private lateinit var notTakenThingsAdapter: NotTakenThingsRecyclerViewAdapter

    private var saveThingsEditSnackbar: Snackbar? = null
    private var addThingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_things, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notTakenThingsAdapter = NotTakenThingsRecyclerViewAdapter(
            notTakenThingsData,
            onAddThingButtonClickedListener,
            onNotTakenThingItemClickedListener
        )
        notTakenThingsRecyclerView.adapter = notTakenThingsAdapter
        takenThingsAdapter = TakenThingsRecyclerViewAdapter(
            takenThingsData,
            onTakenThingItemClickedListener
        )
        takenThingsRecyclerView.adapter = takenThingsAdapter

    }

    override fun showThings() {
        hideThingsLoading()
        showNotTakenThings()
        if (takenThingsData.isNotEmpty())
            showTakenThings()
        else
            hideTakenThings()
    }

    override fun showThingsLoading() {
        loading.visibility = View.VISIBLE
        hideNotTakenThings()
        hideTakenThings()
    }

    override fun hideThingsLoading() {
        loading.visibility = View.GONE
    }

    override fun hideTakenThings() {
        takenThingsRecyclerView.visibility = View.GONE
        takenThingsText.visibility = View.GONE
    }

    override fun showTakenThings() {
        takenThingsRecyclerView.visibility = View.VISIBLE
        takenThingsText.visibility = View.VISIBLE
    }

    override fun hideNotTakenThings() {
        notTakenThingsRecyclerView.visibility = View.GONE
        notTakenThingsText.visibility = View.GONE
    }

    override fun showNotTakenThings() {
        notTakenThingsRecyclerView.visibility = View.VISIBLE
        notTakenThingsText.visibility = View.VISIBLE
    }

    override fun updateThingsItems(notTakenItems: MutableList<ThingListItem>, takenItems: MutableList<ThingListItem>) {
        takenThingsData.clear()
        takenThingsData.addAll(takenItems)
        takenThingsAdapter.notifyDataSetChanged()

        notTakenThingsData.clear()
        notTakenThingsData.addAll(notTakenItems)
        notTakenThingsAdapter.notifyDataSetChanged()
    }

    private fun createConfirmThingsEditSnackbar() {
        if (changeThingsList.isNotEmpty()) return
        val snackbar = Snackbar.make(this.requireView(), R.string.confirmActionAsk, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.save) {
                val changedItems = emptyMap<ThingModel, Boolean>().toMutableMap()
                changeThingsList.forEach { changedItems += Pair(it, it.taken!!) }
                thingsPresenter.editThings(changedItems)
                changeThingsList.clear()
                saveThingsEditSnackbar?.dismiss()
            }
        saveThingsEditSnackbar = snackbar
        snackbar.show()
    }

    override fun onDetach() {
        super.onDetach()
        saveThingsEditSnackbar?.dismiss()
        saveThingsEditSnackbar = null
        changeThingsList.clear()
    }

    private val onAddThingButtonClickedListener = View.OnClickListener { openAddThingDialog() }

    private val baseOnThingClickedListener = fun(thingItem: ThingListItem) {
        createConfirmThingsEditSnackbar()
        val thing = (thingItem as ThingItem).thing
        thing.taken = !thing.taken!!
        if (!changeThingsList.contains(thing)) {
            changeThingsList.add(thing)
        }
    }
    private val onTakenThingItemClickedListener = object : TakenThingsRecyclerViewAdapter.ThingViewHolder.OnThingItemClickListener {
        override fun onThingItemClicked(thingItem: ThingListItem, position: Int) {
            takenThingsData.remove(thingItem)
            notTakenThingsData.add(thingItem)
            takenThingsAdapter.notifyDataSetChanged()
            notTakenThingsAdapter.notifyItemInserted(notTakenThingsData.size + 1)
            showThings()
            baseOnThingClickedListener(thingItem)
        }
    }
    private val onNotTakenThingItemClickedListener = object: NotTakenThingsRecyclerViewAdapter.ThingViewHolder.OnThingItemClickListener {
        override fun onThingItemClicked(thingItem: ThingListItem, position: Int) {
            notTakenThingsData.remove(thingItem)
            takenThingsData.add(thingItem)
            notTakenThingsAdapter.notifyDataSetChanged()
            takenThingsAdapter.notifyItemInserted(takenThingsData.size)
            showThings()
            baseOnThingClickedListener(thingItem)
        }
    }

    override fun openAddThingDialog() {
        activity?.let {
            val view = layoutInflater.inflate(R.layout.dialog_thing_add, null)
            val dialog = AlertDialog.Builder(context!!)
                .setMessage(R.string.addThingBtn)
                .setView(view)
                .setPositiveButton(R.string.addButton, null)
                .setNegativeButton(R.string.cancelButton) { _: DialogInterface, _: Int -> }
                .create()
            addThingDialog = dialog
            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    thingsPresenter.addThing(view.thingName.text.toString())
                }
            }
            dialog.show()
        }
    }

    override fun dismissAddThingDialog() {
        addThingDialog?.dismiss()
    }

    override fun update() {
        showThingsLoading()
        thingsPresenter.updateAll()
    }
}