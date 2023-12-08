package kr.co.gamja.study_hub.feature.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.UsersErrorResponse
import kr.co.gamja.study_hub.data.model.UsersResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.global.Functions

class MyInfoViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName
    private val functions = Functions()
    private val _emailData = MutableLiveData<String>()
    val emailData: LiveData<String> get() = _emailData

    private val _nicknameData = MutableLiveData<String>()
    val nicknameData: LiveData<String> get() = _nicknameData

    // 회원 비회원 여부
    private val _isNicknameData = MutableLiveData<Boolean>()
    val isNicknameData: LiveData<Boolean> get() = _isNicknameData

    private val _majorData = MutableLiveData<String>()
    val majorData: LiveData<String> get() = _majorData

    // 회원 비회원 여부
    private val _isMajorData = MutableLiveData<Boolean>()
    val isMajorData: LiveData<Boolean> get() = _isMajorData

    private val _genderData = MutableLiveData<String>()
    val genderData: LiveData<String> get() = _genderData

    private val _imgData = MutableLiveData<Any?>()
    val imgData: LiveData<Any?> get() = _imgData

    // 회원 비회원 여부
    private val _isImgData = MutableLiveData<Boolean>()
    val isImgData: LiveData<Boolean> get() = _isImgData

    private lateinit var onClickListener: MyInfoCallbackListener
    fun setOnClickListener(listener: MyInfoCallbackListener) {
        onClickListener = listener
    }

    // 초기화
    fun init() {
        _isImgData.value = false
        _isMajorData.value = false
        _isNicknameData.value = false
        _imgData.value = null
    }

    // 회원조회
    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getUserInfo()
                if (response.isSuccessful) {
                    val result = response.body() as UsersResponse
                    Log.d(tag, "회원조회 성공 code" + response.code().toString())
                    _emailData.value = result.email
                    _nicknameData.value = result.nickname
                    _isNicknameData.value = true
                    val koreanMajor = functions.convertToKoreanMajor(result.major)
                    _majorData.value = koreanMajor
                    _isMajorData.value = true
                    val koreanGender = functions.convertToKoreanGender(result.gender)
                    _genderData.value = koreanGender
                    _imgData.value = result.imageUrl
                    _isImgData.value = true
                    onClickListener.myInfoCallbackResult(true)
                } else {
                    Log.e(tag, "회원조회 실패")
                    onClickListener.myInfoCallbackResult(false)
                    val errorResponse: UsersErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), UsersErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val status = errorResponse.status
                        Log.e(tag, status.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "회원조회 Excep: ${e.message}")
            }
        }
    }

    // 유저 사진 삭제
    fun deleteImg() {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.deleteUserImg()
                if (response.isSuccessful) {
                    _imgData.value = null
                    Log.d(tag, "유저 사진 삭제 성공 code : " + response.code().toString())
                } else {
                    Log.e(tag, "유저 사진 삭제 실패 code : " + response.code().toString())
                }
            } catch (e: Exception) {
                Log.e(tag, "유저 사진 삭제 Excep: ${e.message}")
            }
        }
    }

}

interface MyInfoCallbackListener {
    fun myInfoCallbackResult(isSuccess: Boolean = false)
}
