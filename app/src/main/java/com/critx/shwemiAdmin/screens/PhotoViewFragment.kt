package com.critx.shwemiAdmin.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.loadImageUrlPhotoView
import com.critx.shwemiAdmin.databinding.FragmentPhotoViewBinding
import com.github.chrisbanes.photoview.PhotoView

class PhotoViewFragment : Fragment() {

    private lateinit var binding: FragmentPhotoViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPhotoViewBinding.inflate(inflater,container,false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImageUrlPhotoView(binding.photoView,PhotoViewFragmentArgs.fromBundle(requireArguments()).imageUrl)
    }
}