package br.com.brunoccbertolini.memessoundgame.view.addmeme


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.databinding.AddMemeFragmentBinding
import br.com.brunoccbertolini.memessoundgame.extension.hideKeyboard
import br.com.brunoccbertolini.memessoundgame.extension.navigateWithAnimations
import br.com.brunoccbertolini.memessoundgame.model.AppDatabase
import br.com.brunoccbertolini.memessoundgame.model.MemeDao
import br.com.brunoccbertolini.memessoundgame.repository.DatabaseDataSource
import br.com.brunoccbertolini.memessoundgame.repository.MemeRepository
import com.google.android.material.snackbar.Snackbar


class AddMemeFragment : Fragment(R.layout.add_meme_fragment) {

    private val IMAGE_GALLERY_REQUEST_CODE: Int = 100
    private val MEDIA_CONTEXT_REQUEST_CODE: Int = 80
    private val AUDIO_REQUEST_CODE: Int = 90
    private var imageUri: Uri? = null
    private var audioUri: Uri? = null

    private var _binding: AddMemeFragmentBinding? = null
    private val binding: AddMemeFragmentBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddMemeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: AddMemeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                val memeDao: MemeDao =
                    AppDatabase.getInstance(requireContext()).memeDao

                val repository: MemeRepository = DatabaseDataSource(memeDao)
                return AddMemeViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerEvents()
        setListeners()
        setImage()
        setAudio()
    }

    private fun observerEvents() {
        viewModel.memeStateEventData.observe(viewLifecycleOwner) { memeState ->
            when (memeState) {
                is AddMemeViewModel.MemeState.Inserted -> {
                    clearFields()
                    hideKeyBoard()
                    requireView().requestFocus()
                    findNavController().popBackStack()
                }
            }
        }
        viewModel.messegeEventData.observe(viewLifecycleOwner) {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        binding.addTitleMeme.text?.clear()

    }

    private fun hideKeyBoard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                checkSelfPermission(
                    this.requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this.requireContext(), "permission granted", Toast.LENGTH_LONG)
                        .show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )

                else -> ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this.requireContext())

        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@AddMemeFragment.requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setImage() {
        binding.addImage.setOnClickListener {
            checkForPermissions(
                    android.Manifest.permission.MEDIA_CONTENT_CONTROL,
                    "Gallery Content",
                    IMAGE_GALLERY_REQUEST_CODE
            )
                openGallery()

        }
    }
    private fun setAudio() {
        binding.addAudio.setOnClickListener {

                checkForPermissions(
                    android.Manifest.permission.MEDIA_CONTENT_CONTROL,
                    "Audio Content",
                    MEDIA_CONTEXT_REQUEST_CODE
                )
                openAudio()
        }
    }

    private fun openAudio() {
        Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI).apply {
            type = "audio/*"
            startActivityForResult(this, AUDIO_REQUEST_CODE)
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
        }
    }

    private fun setListeners() {
        binding.addMemeButton.setOnClickListener {
            val title = binding.addTitleMeme.text.toString()
            val image = "$imageUri"
            val audio = "$audioUri"

            if (title.isEmpty() || image.isEmpty()) {
                Toast.makeText(requireContext(), "Fill out all field, please.", Toast.LENGTH_LONG)
                    .show()
            } else {
                viewModel.getAppSpecificAlbumStorageDir(requireContext(), title)
                viewModel.addMeme(title, image, audio)
                findNavController().navigateWithAnimations(R.id.action_addMemeFragment_to_memeGalleryFragment)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "$name permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        when (requestCode) {
            IMAGE_GALLERY_REQUEST_CODE -> innerCheck("Gallery Content")

            AUDIO_REQUEST_CODE -> innerCheck("Audio Content")
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.data != null) {
            if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                imageUri = data.data

                binding.addImage.setImageURI(Uri.parse(imageUri.toString()))

            } else if (requestCode == AUDIO_REQUEST_CODE) {
                audioUri = data.data
                val contentSolver = requireContext().applicationContext.contentResolver
                val takeFlags: Int =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                audioUri?.let { contentSolver.takePersistableUriPermission(it, takeFlags) }

            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

