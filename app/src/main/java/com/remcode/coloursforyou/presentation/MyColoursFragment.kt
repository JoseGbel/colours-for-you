package com.remcode.coloursforyou.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.remcode.coloursforyou.R
import com.remcode.coloursforyou.business.MyColoursViewModel
import com.remcode.coloursforyou.business.MyColoursViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_colours.*

class MyColoursFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ColourListAdapter

    private lateinit var viewModel: MyColoursViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MyColoursViewModelFactory(
            requireActivity().application)).get(MyColoursViewModel::class.java)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)

        adapter = ColourListAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_colours, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.allColours.observe(viewLifecycleOwner, Observer { colours ->
            colours.let { adapter.setColours(it) }
        })

        delete_btn.setOnClickListener {
            viewModel.deleteAll()
        }

    }
}