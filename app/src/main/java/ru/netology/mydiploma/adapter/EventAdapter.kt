package ru.netology.mydiploma.adapter

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
import ru.netology.mydiploma.databinding.CardEventBinding
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.enumeration.AttachmentType
import ru.netology.mydiploma.util.FormatUtils

interface OnEventInteractionListener {
    fun onLike(event: Event)
    fun onJoin(event: Event)
    fun onRemove(event: Event)
}


class EventAdapter(
    private val onEventInteractionListener: OnEventInteractionListener
) : ListAdapter<Event, EventViewHolder>(EventDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onEventInteractionListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onEventInteractionListener: OnEventInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(event: Event) {
        with(binding) {

            //initialize parameters
            when (event.attachment?.type) {
                AttachmentType.IMAGE -> {
                    eventImage.visibility = View.VISIBLE
                    Glide.with(eventImage)
                        .load(event.attachment.url)
                        .error(R.drawable.ic_cloud_download_24)
                        .timeout(30_000)
                        .into(eventImage)
                }
                else -> eventImage.visibility = View.GONE
            }

            eventContent.text = event.content
            eventAuthor.text = event.author
            eventPublished.text = event.published?.let {
                FormatUtils.formatDate(it)
            }

            eventLikeButton.iconTint = if (event.likedByMe) {
                AppCompatResources.getColorStateList(root.context, R.color.white)
            } else {
                AppCompatResources.getColorStateList(root.context, R.color.red)
            }
            eventLikeButton.text =
                if (event.likeOwnerIds.isEmpty()) "" else FormatUtils.formatNum(event.likeOwnerIds.size)

            eventJoinButton.apply {
                when (event.participatedByMe) {
                    true -> {
                        this.icon =
                            AppCompatResources.getDrawable(root.context, R.drawable.ic_close_24)
                        this.text = root.context.getString(R.string.join_button_text_2)
                    }
                    false -> {
                        this.icon =
                            AppCompatResources.getDrawable(root.context, R.drawable.ic_check_24)
                        this.text = root.context.getString(R.string.join_button_text)
                    }
                }
            }

            eventDatetime.text =
                if (event.datetime.isNullOrBlank()) {
                    root.context.getString(R.string.date_is_being_specified)
                } else {
                    FormatUtils.formatDate(event.datetime)
                }

            eventType.text = event.attachment?.type.toString()


            //listeners
            eventLikeButton.setOnClickListener {
                onEventInteractionListener.onLike(event)
            }

            eventJoinButton.setOnClickListener {
                onEventInteractionListener.onJoin(event)
            }

            eventMenuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.event_menu)
                    menu.setGroupVisible(R.id.owned_event, event.ownedByMe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.item_remove_event -> {
                                onEventInteractionListener.onRemove(event)
                                true
                            }
                            R.id.item_edit -> {
                                //todo need to realize onEdit()
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }


        }
    }
}

class EventDiffCallBack : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

}