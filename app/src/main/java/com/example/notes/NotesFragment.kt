package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.databinding.FragmentNotesBinding
import com.example.notes.models.NoteResponse
import com.example.notes.utils.NetworkResult
import com.example.notes.viewmodels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding : FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClicked) // passing fun to adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        noteViewModel.getNotes()
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        binding.addNoteBtn.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_editNoteFragment)
        }
    }

    private fun observer() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            // hide progress bar
            when(it) {
                is NetworkResult.Loading -> {
                    // show progress bar
                }
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onNoteClicked(noteResponse : NoteResponse) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_notesFragment_to_editNoteFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}