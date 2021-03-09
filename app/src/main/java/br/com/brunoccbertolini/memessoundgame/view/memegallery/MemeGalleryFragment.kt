package br.com.brunoccbertolini.memessoundgame.view.memegallery


import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.databinding.MemesgalleryFragmentBinding
import br.com.brunoccbertolini.memessoundgame.extension.navigateWithAnimations
import br.com.brunoccbertolini.memessoundgame.model.AppDatabase
import br.com.brunoccbertolini.memessoundgame.model.MemeDao
import br.com.brunoccbertolini.memessoundgame.repository.DatabaseDataSource
import br.com.brunoccbertolini.memessoundgame.repository.MemeRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.recycler_item.view.*
import java.io.IOException


class MemeGalleryFragment : Fragment(R.layout.memesgallery_fragment),
    MemeGalleryAdapter.OnItemClickListener {
    lateinit var soundPool: SoundPool
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    lateinit var memeGalleryAdapter: MemeGalleryAdapter


    private var _binding: MemesgalleryFragmentBinding? = null
    private val binding:MemesgalleryFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MemesgalleryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: MemeGalleryViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val memeDao: MemeDao =
                    AppDatabase.getInstance(requireContext()).memeDao

                val repository: MemeRepository = DatabaseDataSource(memeDao)
                return MemeGalleryViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSdkVersion()
        configureNavListeners()
        observerViewModelEvents()
    }


    private fun checkSdkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
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
            if (audioUrl.isDigitsOnly()) {
                mediaPlayer = MediaPlayer.create(context, audioUrl.toInt())
            } else {
                mediaPlayer = MediaPlayer().apply {
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
            .setNegativeButton(resources.getString(R.string.decline)){_,_ ->}
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                viewModel.deleteMeme(id)
                viewModel.memeRemoveEvent.observe(viewLifecycleOwner){
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


