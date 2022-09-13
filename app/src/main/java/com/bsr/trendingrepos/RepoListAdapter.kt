package com.bsr.trendingrepos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsr.trendingrepos.databinding.RepoItemBinding

class RepoListAdapter : RecyclerView.Adapter<RepoListAdapter.RepoHolder>() {

    private var mList: List<RepoResponse>? = null

    interface OnClickListener {
        fun onItemClick()
    }

    private var mOnClickListener: OnClickListener? = null

    fun setItemClickListener(listener: OnClickListener) {
        mOnClickListener = listener
    }

    fun addItems(list: List<RepoResponse>) {
        mList = list
        notifyDataSetChanged()
    }

    inner class RepoHolder(val binding: RepoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutItem.setOnClickListener {
                if (adapterPosition > -1) {
                    mList?.get(adapterPosition)?.let {
                        it.selected = !it.selected
                        notifyItemChanged(adapterPosition)
                    }

                    mOnClickListener?.onItemClick()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val binding =
            RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        with(holder.binding) {
            mList?.get(position)?.let {
                tvName.text = "Name: ${it.username}"
                tvRepo.text = "Repository: ${it.repositoryName}"
                tvDescription.text = "Description: ${it.description}"
                tvLanguage.text = "Language: ${it.language}"
                checkbox.isChecked = it.selected
            }
        }
    }

    override fun getItemCount(): Int = mList?.size ?: 0

}