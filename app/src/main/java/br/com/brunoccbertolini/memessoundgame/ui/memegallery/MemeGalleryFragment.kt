package br.com.brunoccbertolini.memessoundgame.ui.memegallery


import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.databinding.MemesgalleryFragmentBinding
import br.com.brunoccbertolini.memessoundgame.extension.navigateWithAnimations
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class MemeGalleryFragment : Fragment(R.layout.memesgallery_fragment),
    MemeGalleryAdapter.OnItemClickListener {
    lateinit var soundPool: SoundPool
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    lateinit var memeGalleryAdapter: MemeGalleryAdapter


    private var _binding: MemesgalleryFragmentBinding? = null
    private val binding: MemesgalleryFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MemesgalleryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: MemeGalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureNavListeners()
        observerViewModelEvents()
    }


    private fun observerViewModelEvents() {
        viewModel.allMemeEvent.observe(viewLifecycleOwner) { allMemes ->
            memeGalleryAdapter = MemeGalleryAdapter(allMemes, this)
            binding.recyclerView.apply {
                setHasFixedSize(true)
                adapter = memeGalleryAdapter
            }
        }
    }

    private fun configureNavListeners() {
        binding.fab.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_memeGalleryFragment_to_addMemeFragment)
        }
    }

    private fun mediaPlayerUri(audioUrl: String) {
        mediaPlayer?.stop()

        try {
            val myUri: Uri = Uri.parse(audioUrl)
            Log.e("audioUrl soundP:", myUri.toString())
            mediaPlayer = if (audioUrl.isDigitsOnly()) {
                MediaPlayer.create(context, audioUrl.toInt())
            } else {
                MediaPlayer().apply {
                    setDataSource(requireContext().applicationContext, myUri)
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    prepare()
                }
            }
            mediaPlayer?.start()
        } catch (e: IOException) {
            mediaPlayer = null

            mediaPlayer?.release()
        }
    }

    override fun onItemClick(audioUrl: String) {
        //soundPoolInstance(audioUrl)
        mediaPlayerUri(audioUrl)
    }

    override fun onLongItemClick(id: Long) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_title))
            .setMessage(resources.getString(R.string.delete_message))
            .setNegativeButton(resources.getString(R.string.decline)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                viewModel.deleteMeme(id)
                viewModel.memeRemoveEvent.observe(viewLifecycleOwner) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    override fun onResume() {
        viewModel.getMemes()
        super.onResume()

    }


    override fun onDestroy() {
        soundPool.release()
        _binding = null
        super.onDestroy()

    }
}


