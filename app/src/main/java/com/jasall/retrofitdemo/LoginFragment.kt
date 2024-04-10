package com.jasall.retrofitdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jasall.retrofitdemo.databinding.FragmentLoginBinding
import com.jasall.retrofitdemo.retrofit.AuthRequest
import com.jasall.retrofitdemo.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mainApi: MainApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()

        binding.apply {
            checkUserButton.setOnClickListener {
                auth(
                    AuthRequest(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                )
            }
            buttonFirst.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            }
        }
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        mainApi = retrofit.create(MainApi::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            val user = response.body()
            val message = response.errorBody()?.string()?.let { JSONObject(it).getString("message") }
            requireActivity().runOnUiThread{
                binding.errorMessage.text = message
                if (user != null) {
                    Picasso.get().load(user.image).into(binding.avatarIV)
                    binding.firstnameTV.text = user.firstName
                    viewModel.token.value = user.token
                    binding.buttonFirst.visibility = View.VISIBLE
                }
            }


        }
    }

}