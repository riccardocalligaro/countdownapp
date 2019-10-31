package com.riccardocalligaro.countdownapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog

import com.riccardocalligaro.countdownapp.R
import com.riccardocalligaro.countdownapp.adapters.CountdownAdapter
import com.riccardocalligaro.countdownapp.adapters.FinishedCountdownAdapter
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import com.riccardocalligaro.countdownapp.utils.Costants.TRUE
import com.riccardocalligaro.countdownapp.views.CountdownViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import org.jetbrains.anko.toast
import java.util.*
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExploreFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    // private lateinit var countdownViewModelQuick: CountdownViewModel
    private lateinit var countdownViewModel: CountdownViewModel


    // fun getLayoutId(): Int = R.layout.recycler_view_main_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countdownViewModel = ViewModelProviders.of(this).get(CountdownViewModel::class.java)

        val countdownsRecyclerViewManager = LinearLayoutManager(
            this.context, RecyclerView.VERTICAL, false
        )

        history_recycler_view.layoutManager = countdownsRecyclerViewManager

        history_recycler_view.setHasFixedSize(true)
        history_recycler_view.isNestedScrollingEnabled = false

        countdownViewModel.getAllCountdowns.observe(viewLifecycleOwner, Observer { countdowns ->
            val finishedCountdowns = ArrayList<Countdown>()
            countdowns.forEach { if (it.finished == TRUE) finishedCountdowns.add(it) }
            history_recycler_view.adapter = FinishedCountdownAdapter(context!!, finishedCountdowns, clickListener = {
                countdownClick(it, countdowns, history_recycler_view.adapter as FinishedCountdownAdapter)
            }, longClickListener = {
                longCountdownClick(it, countdowns, history_recycler_view.adapter as FinishedCountdownAdapter)
            })


        })
    }

    private fun countdownClick(position: Int, countdowns: List<Countdown>, adapter: FinishedCountdownAdapter) {
        val mills = System.currentTimeMillis() - countdowns[position].date.time
        var diffInSecText = TimeUnit.MILLISECONDS.toSeconds(mills)
        val seconds = diffInSecText % 60
        diffInSecText /= 60
        val minutes = diffInSecText % 60
        diffInSecText /= 60
        val hours = diffInSecText % 24
        diffInSecText /= 24
        val days = diffInSecText

        when {
            days > 0 -> activity?.toast(
                getString(R.string.time_passed) + " " + days.toString() + " " + getString(R.string.days) + " " + hours.toString() + " " + getString(
                    R.string.hours
                ) + " " + minutes.toString() + " " + getString(R.string.minutes) + " " + seconds.toString() + " " + getString(
                    R.string.seconds
                )
            )
            hours > 0 -> activity?.toast(
                getString(R.string.time_passed) + " " + hours.toString() + " " + getString(
                    R.string.hours
                ) + " " + minutes.toString() + " " + getString(R.string.minutes) + " " + seconds.toString() + " " + getString(
                    R.string.seconds
                )
            )
            minutes > 0 -> activity?.toast(
                getString(R.string.time_passed) + " " + minutes.toString() + " " + getString(R.string.minutes) + " " + seconds.toString() + " " + getString(
                    R.string.seconds
                )
            )
            seconds > 0 -> activity?.toast(
                getString(R.string.time_passed) + " " + seconds.toString() + " " + getString(
                    R.string.seconds
                )
            )
        }

    }

    private fun longCountdownClick(position: Int, countdowns: List<Countdown>, adapter: FinishedCountdownAdapter) {
        MaterialDialog(context!!).show {
            title(R.string.delete_countdown_title)
            positiveButton(R.string.delete) {
                DBHelper.database.countdownDao().delete(countdowns[position])
            }
            negativeButton(R.string.no) {
                activity!!.toast(getString(R.string.operation_cancelled))
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
