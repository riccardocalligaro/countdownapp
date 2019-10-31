package com.riccardocalligaro.countdownapp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.riccardocalligaro.countdownapp.R
import com.riccardocalligaro.countdownapp.activities.EditCountdown
import com.riccardocalligaro.countdownapp.adapters.CountdownAdapter
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import com.riccardocalligaro.countdownapp.utils.Costants.COUNTDOWN_FINISHED
import com.riccardocalligaro.countdownapp.utils.Costants.COUNTDOWN_ID
import com.riccardocalligaro.countdownapp.utils.Costants.FALSE
import com.riccardocalligaro.countdownapp.utils.Costants.TRUE
import com.riccardocalligaro.countdownapp.views.CountdownViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CountdownsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CountdownsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var countdownViewModel: CountdownViewModel

    //fun getLayoutId(): Int = R.layout.recycler_view_main_item

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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countdownViewModel = ViewModelProviders.of(this).get(CountdownViewModel::class.java)

        val countdownsRecyclerViewManager = LinearLayoutManager(
            this.context, RecyclerView.VERTICAL, false
        )

        main_recycler_view.layoutManager = countdownsRecyclerViewManager

        main_recycler_view.setHasFixedSize(true)
        main_recycler_view.isNestedScrollingEnabled = false


        countdownViewModel.getAllCountdowns.observe(viewLifecycleOwner, Observer { countdowns ->

            //val mCountdownQuickAdapter = CountdownQuickAdapter(getLayoutId(), countdowns)
            //val emptyView =
            layoutInflater.inflate(R.layout.empty_view, main_recycler_view.parent as ViewGroup, false)

            val notFinishedCountdowns = ArrayList<Countdown>()
            countdowns.forEach {
                if (it.finished != TRUE) {
                    notFinishedCountdowns.add(it)
                }
            }

            if (notFinishedCountdowns.isEmpty()) {
                recycler_view_empty_image.visibility = View.VISIBLE
            } else {
                recycler_view_empty_image.visibility = View.GONE
            }

            main_recycler_view.adapter = CountdownAdapter(context!!, countdowns, clickListener = {
                countdownClick(it, countdowns, main_recycler_view.adapter as CountdownAdapter)
            }, longClickListener = {
                longCountdownClick(it, countdowns, main_recycler_view.adapter as CountdownAdapter)
            })
        })

    }

    private fun countdownClick(position: Int, countdowns: List<Countdown>, adapter: CountdownAdapter) {
        if (countdowns[position].expand == FALSE && countdowns[position].finished != COUNTDOWN_FINISHED) {
            DBHelper.database.countdownDao().updateExpand(1, countdowns[position].id)
        } else if (countdowns[position].expand == TRUE && countdowns[position].finished != COUNTDOWN_FINISHED) {
            DBHelper.database.countdownDao().updateExpand(0, countdowns[position].id)
        }
        adapter.notifyDataSetChanged()
    }

    private fun longCountdownClick(position: Int, countdowns: List<Countdown>, adapter: CountdownAdapter) {
        if (countdowns[position].finished != TRUE) {
            val intent = Intent(activity, EditCountdown::class.java)
            intent.putExtra(COUNTDOWN_ID, countdowns[position].id)

            startActivity(intent)
        } else {
            MaterialDialog(context!!).show {
                title(R.string.delete_countdown_title)
                positiveButton(R.string.delete) {
                    DBHelper.database.countdownDao().delete(countdowns[position])
                    adapter.notifyDataSetChanged()
                }
                negativeButton(R.string.no) {
                    activity!!.toast(getString(R.string.operation_cancelled))
                }
            }

        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
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
         * @return A new instance of fragment CountdownsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
