package ru.netology.mydiploma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.CardJobBinding
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.util.FormatUtils

interface OnJobInteractionListener {
    fun onEdit(job: Job)
    fun onRemove(job: Job)
}

class JobAdapter(
    private val onJobInteractionListener: OnJobInteractionListener
    ) : ListAdapter<Job, JobViewHolder>(JobDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = CardJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding, onJobInteractionListener)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class JobViewHolder(
    private val binding: CardJobBinding,
    private val onJobInteractionListener: OnJobInteractionListener
): RecyclerView.ViewHolder(binding.root) {
    fun bind(job: Job) {
        with(binding) {
            name.text = job.name
            position.text = job.position
            start.text = binding.root.context.getString(R.string.start_from, FormatUtils.formatJustDate(job.start))
            job.finish?.let {
                end.text = binding.root.context.getString(R.string.end_to, FormatUtils.formatJustDate(job.finish))
            }
            compLink.text = job.link

            jobMenu.isVisible = job.ownedByMe

            jobMenu.setOnClickListener {
                PopupMenu(binding.root.context, it).apply {
                    inflate(R.menu.job_menu)

                    menu.setGroupVisible(R.id.job_owned, job.ownedByMe)
                    setOnMenuItemClickListener { menuItem ->
                        when(menuItem.itemId) {
                            R.id.job_item_edit -> {
                                onJobInteractionListener.onEdit(job)
                                true
                            }
                            R.id.job_item_remove -> {
                                onJobInteractionListener.onRemove(job)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
        }
    }
}

class JobDiffCallBack: DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }

}