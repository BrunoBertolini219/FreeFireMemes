package br.com.brunoccbertolini.memessoundgame.view.memegallery


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import br.com.brunoccbertolini.memessoundgame.databinding.RecyclerItemBinding
import br.com.brunoccbertolini.memessoundgame.model.MemeEntity
import com.bumptech.glide.Glide

class MemeGalleryAdapter(
    private val memes: List<MemeEntity>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<MemeGalleryAdapter.MemeGalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeGalleryViewHolder {
        val itemBinding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeGalleryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MemeGalleryViewHolder, position: Int) {
        holder.bindView(memes[position])
    }

    override fun getItemCount() = memes.size

    inner class MemeGalleryViewHolder(viewBinding: RecyclerItemBinding) : RecyclerView.ViewHolder(viewBinding.root),
        View.OnClickListener, View.OnLongClickListener {

        private val textViewMemeTitle: TextView = viewBinding.textMemeItem
        private val imageViewMemeSrc: ImageView = viewBinding.imageMemeItem
        private val soundSrc = viewBinding.soundMemeItem


        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bindView(meme: MemeEntity) {
            textViewMemeTitle.text = meme.title
            soundSrc.text = meme.audioUrl
            Log.e("Audio INT:", "${meme.audioUrl}")

            if (!meme.imgUrl.isNullOrEmpty()) {
                Log.e("Img URL Adapter INT", "${meme.title}:  ${meme.imgUrl}")
                if (meme.imgUrl.isDigitsOnly()) {
                    Glide.with(itemView.context)
                        .load(meme.imgUrl.toInt())
                        .centerCrop()
                        .into(imageViewMemeSrc)
                } else {
                    Log.e("Img URL Adapter STRING", "${meme.imgUrl}")
                    Glide.with(itemView.context)
                        .load(meme.imgUrl)
                        .centerCrop()
                        .into(imageViewMemeSrc)
                }
            }

        }


        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                memes[position].audioUrl?.let { listener.onItemClick(it) }
            }
        }


        override fun onLongClick(view: View): Boolean {
            Log.i("TAG_DELETE_ADAPTER", "delete")
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                memes[position].id.let { listener.onLongItemClick(it) }

            }
           return true
        }
    }

    interface OnItemClickListener {
        fun onItemClick(audioUrl: String)
        fun onLongItemClick(id: Long)

    }

}