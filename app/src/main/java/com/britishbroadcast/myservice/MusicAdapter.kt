package com.britishbroadcast.myservice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.myservice.databinding.MusicItemLayoutBinding
import com.britishbroadcast.myservice.model.Song
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso

class MusicAdapter(val playList: List<Song>, val songDelegate: SongDelegate) : RecyclerView.Adapter<MusicAdapter.SongViewHolder>() {

    inner class SongViewHolder(val binding: MusicItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    interface SongDelegate {
        fun playSong(song: Song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val viewBinding = MusicItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return SongViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = playList.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = playList[position]

        Picasso.get()
                .load(song.albumArt)
                .resize(80,80)
                .centerCrop()
                .into(holder.binding.albumArt)
//        Glide.with(holder.itemView)
//                .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
//                .load(song.albumArt)
//                .into(holder.binding.albumArt)

        holder.binding.songTitleTextview.text = song.songInfo

        holder.itemView.setOnClickListener {
            songDelegate.playSong(song)
        }

    }
}