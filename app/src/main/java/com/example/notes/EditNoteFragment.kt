package com.example.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notes.databinding.FragmentEditNoteBinding
import com.example.notes.models.NoteRequest
import com.example.notes.models.NoteResponse
import com.example.notes.utils.NetworkResult
import com.example.notes.viewmodels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteFragment : Fragment() {

    private var _binding : FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private var noteResponse : NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialValues()
        bindHandler()
        bindObserver()
    }

    private fun bindObserver() {
        noteViewModel.status.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Error -> {
                    //Toast.makeText(context)
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun bindHandler() {
        binding.submitBtn.setOnClickListener {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()
            val note = NoteRequest(description, title)
            if(noteResponse != null) {
                noteViewModel.updateNote(noteResponse!!._id, note)
            }
            else {
                noteViewModel.insertNote(note)
            }
        }

        binding.delete.setOnClickListener {
            noteResponse?.let {
                noteViewModel.deleteNote(it._id)
            }
        }
    }

    private fun setInitialValues() {
        val json = arguments?.getString("note")
        if(json != null) {
            noteResponse = Gson().fromJson(json, NoteResponse::class.java)
            noteResponse?.let {
                binding.title.setText(it.title)
                binding.description.setText(it.description)
            }
        }
        else {
            binding.whichOne.text = "Add Note"
        }
    }

}