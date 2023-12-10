package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXX
import kr.co.gamja.study_hub.databinding.WrittenstudyItemBinding

class WrittenStudyAdapter(private val context: Context) :PagingDataAdapter<ContentXXX,WrittenStudyAdapter.WrittenStudyHolder>(DIFF_CALLBACK){
    companion object{
        private val DIFF_CALLBACK= object: DiffUtil.ItemCallback<ContentXXX>(){
            // 리스트 아이템이 같은지
            override fun areItemsTheSame(oldItem: ContentXXX, newItem: ContentXXX): Boolean {
                return oldItem.postId == newItem.postId
            }
            // 리스트 아이템 안 객체의 내용이 같은지 - 내용 변경 확인
            override fun areContentsTheSame(oldItem: ContentXXX, newItem: ContentXXX): Boolean {
                return oldItem==newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrittenStudyHolder {
        val binding = WrittenstudyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WrittenStudyHolder(binding)
    }

    override fun onBindViewHolder(holder: WrittenStudyHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.bind(it) }
    }

    inner class WrittenStudyHolder(val binding: WrittenstudyItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(content : ContentXXX){
            binding.txtMajor.text=content.major
            binding.txtTitle.text=content.title
            binding.txtSub.text=content.content
            binding.txtRemainingSeats.text=context.getString(R.string.txt_RemainingSeats, content.remainingSeat)
        }
    }
}