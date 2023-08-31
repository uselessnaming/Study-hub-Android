package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.User

import kr.co.gamja.study_hub.databinding.FragmentCreateAccount02Binding


class CreateAccountFragment02 : Fragment() {
    private var _binding: FragmentCreateAccount02Binding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCreateAccount02Binding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fca02TxtPagenumber.text=getString(R.string.txt_pagenumber,2)


        binding.fca02BtnOk.setOnClickListener{
            // 비번확인
            val txt_pass01=binding.fca02EditPassword.text.toString()
            val txt_pass02=binding.fca02EditPassword02.text.toString()
            Log.d("회원가입 - 비번1",txt_pass01)
            Log.d("회원가입 - 비번2",txt_pass02)

            if(txt_pass01.equals(txt_pass02)){
                User.password=txt_pass01
                binding.fca02BtnNext.isEnabled=true
                Log.d("회원가입 - User.Password",User.password.toString())
                Log.e("회원가입 - 비번 일치","")
            }
            else{
                Toast.makeText(requireContext(),"비밀번호 불일치", Toast.LENGTH_LONG).show()
                Log.e("회원가입 - 비번 불일치","")
            }
        }
        binding.fca02BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment02_to_createAccountFragment03,null)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}