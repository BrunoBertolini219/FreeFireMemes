package br.com.brunoccbertolini.memessoundgame.ui.addmeme


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.databinding.AddMemeFragmentBinding
import br.com.brunoccbertolini.memessoundgame.extension.hideKeyboard
import br.com.brunoccbertolini.memessoundgame.extension.navigateWithAnimations
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMemeFragment : androidx.fragment.app.Fragment(R.layout.add_meme_fragment) {


    private var imageUri: Uri? = null
    private var audioUri: Uri? = null

    private var _binding: AddMemeFragmentBinding? = null
    private val binding: AddMemeFragmentBinding get() = _binding!!

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            binding.addImage.setImageURI(uri)
        }

    private val getAudioContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            audioUri = uri
            val contentSolver = requireContext().applicationContext.contentResolver
            val takeFlags: Int =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            audioUri?.let { contentSolver.takePersistableUriPermission(it, takeFlags) }

        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddMemeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: AddMemeViewModel by viewModels()

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
                    findNavController().navigateWithAnimations(R.id.action_addMemeFragment_to_memeGalleryFragment)
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


    private fun setImage() {
        binding.addImage.setOnClickListener {
            checkForPermissions(
                android.Manifest.permission.MEDIA_CONTENT_CONTROL,
                "Image Content",
                IMAGE_REQUEST_CODE
            )
            openGallery()
        }
    }

    private fun setAudio() {
        binding.addAudio.setOnClickListener {

            if (checkForPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "Audio Content",
                    AUDIO_REQUEST_CODE
                )
            ) {
                openAudio()
            }
        }
    }

    private fun openAudio() {
        getAudioContent.launch(arrayOf("audio/*"))
    }

    private fun openGallery() {
        getImageContent.launch("image/*")
    }

    private fun setListeners() {
        binding.addMemeButton.setOnClickListener {
            val title = binding.addTitleMeme.text.toString()
            val image = imageUri
            val audio = audioUri

            if (title.isEmpty() || image == null || audio == null) {
                Toast.makeText(requireContext(), "Fill out all field, please.", Toast.LENGTH_LONG)
                    .show()
            } else {
                viewModel.getAppSpecificAlbumStorageDir(requireContext(), title)
                viewModel.addMeme(title, image.toString(), audio.toString())
                observerEvents()
                findNavController().navigateWithAnimations(R.id.action_addMemeFragment_to_memeGalleryFragment)
            }
        }
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                checkSelfPermission(
                    this.requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this.requireContext(), "permission granted", Toast.LENGTH_LONG)
                        .show()
                    return true
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
        return false
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this.requireContext())

        builder.apply {
            setMessage("Permissão para acessar $name é necessária")
            setTitle(getString(R.string.tituloPermisão))
            setPositiveButton(getString(R.string.positiveButton)) { dialog, which ->
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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val AUDIO_REQUEST_CODE: Int = 100
        private const val IMAGE_REQUEST_CODE: Int = 200
    }
}

