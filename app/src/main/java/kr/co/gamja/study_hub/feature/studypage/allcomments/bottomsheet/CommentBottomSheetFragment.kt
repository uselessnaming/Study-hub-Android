package kr.co.gamja.study_hub.feature.studypage.allcomments.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.databinding.FragmentCommentBottomSheetBinding
import kr.co.gamja.study_hub.feature.studypage.allcomments.AllCommentViewModelFactory
import kr.co.gamja.study_hub.feature.studypage.allcomments.AllCommentsViewModel
import kr.co.gamja.study_hub.feature.studypage.studyContent.ContentViewModel
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener

// 댓글 관련 모달 싯트
class CommentBottomSheetFragment : BottomSheetDialogFragment() {
    private val msg = this.javaClass.simpleName
    private lateinit var binding: FragmentCommentBottomSheetBinding
    private lateinit var viewModel: AllCommentsViewModel
    private val contentViewModel: ContentViewModel by activityViewModels()
    private lateinit var userComment: String  // 수정시 사용할 comment
    private lateinit var whatPage: String // 어떤 페이지에서 왔는지 (댓글 미리보기랑, 댓글 전체보기 페이지 중 하나)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comment_bottom_sheet, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = AllCommentViewModelFactory(AuthRetrofitManager.api)
        viewModel = ViewModelProvider(requireActivity(), factory)[AllCommentsViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner

        val receiveBundle = arguments
        if (receiveBundle != null) {
            userComment = receiveBundle.getString("comment").toString()
            whatPage = receiveBundle.getString("page").toString()
            Log.d(msg, "comment : $userComment")
        }

        // 스터디 글 삭제 누를 시
        binding.btnDelete.setOnClickListener {
            val head = requireContext().resources.getString(R.string.commentBottomsheetHead)
            val no = requireContext().resources.getString(R.string.btn_no)
            val yes = requireContext().resources.getString(R.string.btn_delete)
            val dialog = CustomDialog(requireContext(), head, null, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() {

                    when (whatPage) {
                        "contentFragment" -> { // 댓글 미리보기에 알림
                            contentViewModel.setDelete(true)
                            dismiss()
                        }
                        "AllCommentsFragment" -> {
                            viewModel.setDelete(true) // 삭제임을 AllCommentsViewModel에 알림
                            dismiss()
                        }
                    }
                }
            })
        }
        // 닫기 누를 시
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // 댓글 수정하기
        binding.btnModify.setOnClickListener {

            when (whatPage) {
                "contentFragment" -> { // 댓글 미리보기에 알림
                    contentViewModel.setModify(true) // 수정임을 댓글 미리보기 창에 알림
                    contentViewModel.studyComment.value = userComment
                    dismiss()
                }
                "AllCommentsFragment" -> {
                    viewModel.setModify(true) // 수정임을 AllCommentsViewModel에 알림
                    viewModel.comment.value = userComment
                    dismiss()
                }
            }
        }
    }

}