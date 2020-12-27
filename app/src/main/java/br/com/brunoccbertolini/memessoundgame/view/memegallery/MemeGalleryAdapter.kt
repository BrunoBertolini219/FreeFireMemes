package br.com.brunoccbertolini.memessoundgame.view.memegallery


import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.RecyclerView
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.model.MemeEntity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.add_meme_fragment.view.*
import kotlinx.android.synthetic.main.recycler_item.view.*

class MemeGalleryAdapter(
    private val memes: List<MemeEntity>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MemeGalleryAdapter.MemeGalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeGalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)

        return MemeGalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemeGalleryViewHolder, position: Int) {
        holder.bindView(memes[position])
    }

    override fun getItemCount() = memes.size

    inner class MemeGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val textViewMemeTitle: TextView = itemView.text_meme_item
        private val imageViewMemeSrc: ImageView = itemView.image_meme_item
        private val soundSrc = itemView.sound_meme_item


        init {
            itemView.setOnClickListener(this)
        }

        fun bindView(meme: MemeEntity) {
            textViewMemeTitle.text = meme.title
            soundSrc.text =meme.audioUrl
            Log.e("Audio INT:", "${meme.audioUrl}")

            if (!meme.imgUrl.isNullOrEmpty()) {
                Log.e("Img URL Adapter INT", "${meme.title}:  ${meme.imgUrl}")
                if (meme.imgUrl.isDigitsOnly()) {
                    Glide.with(itemView.context)
                        .load(meme.imgUrl.toInt())
                        .centerCrop()
                        .into(imageViewMemeSrc)
                }else {
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
    }

    interface OnItemClickListener {
        fun onItemClick(audioUrl: String)

    }

}