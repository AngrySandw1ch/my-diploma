package ru.netology.mydiploma.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.CardPostBinding
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.enumeration.AttachmentType
import ru.netology.mydiploma.util.FormatUtils

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            authorName.text = post.author
            published.text = FormatUtils.formatDate(post.published)
            content.text = post.content
            link.text = post.link ?: "www.testlink.com"
            playButton.icon =
                if (post.audioPlaying)
                    AppCompatResources.getDrawable(root.context, R.drawable.ic_play_24)
                else
                    AppCompatResources.getDrawable(root.context, R.drawable.ic_stop_circle_24)
            like.iconTint =
                if (post.likedByMe)
                    AppCompatResources.getColorStateList(root.context, R.color.red)
                else
                    AppCompatResources.getColorStateList(root.context, R.color.grey2)
            like.text = if(post.likeOwnerIds.isEmpty()) "" else FormatUtils.formatNum(post.likeOwnerIds.size)

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }


            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.GONE
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    getMenu().setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.item_edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            R.id.item_remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            Glide.with(postAvatar)
                .load(post.authorAvatar)
                .placeholder(R.drawable.ic_cloud_download_24)
                .error(R.drawable.ic_person_24)
                .circleCrop()
                .timeout(30_000)
                .into(postAvatar)

            when (post.attachment?.type) {
                AttachmentType.IMAGE -> {
                    imageAttachment.visibility = View.VISIBLE
                    Glide.with(imageAttachment)
                        .load(post.attachment.url)
                        .timeout(30_000)
                        .into(imageAttachment)
                }
                AttachmentType.VIDEO -> mediaAttachment.visibility = View.VISIBLE
                AttachmentType.AUDIO -> playButton.visibility = View.VISIBLE
                null -> false
            }
        }
    }
}


class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}