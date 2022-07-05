package com.example.online.presentation.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.online.R
import com.example.online.data.model.Entity
import com.example.online.data.model.Horoscope
import com.example.online.presentation.viewModels.UserViewModel
import com.example.online.databinding.FragmentProfileBinding
import com.example.online.domain.network.RetrofitInstance
import com.example.online.domain.utils.Constants.DATABASE_NAME
import com.example.online.domain.utils.Constants.INTENT_TYPE
import com.example.online.domain.utils.Constants.USER_AGE
import com.example.online.domain.utils.Constants.USER_AVATAR
import com.example.online.domain.utils.Constants.USER_HEIGHT
import com.example.online.domain.utils.Constants.USER_NAME
import com.example.online.domain.utils.Constants.USER_QUERY
import com.example.online.domain.utils.Constants.USER_WEIGHT
import com.example.online.domain.utils.resources
import com.example.online.presentation.adapters.HoroscopeAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(list.isEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) { binding.spinner.visibility = View.VISIBLE }

                for(entity in Entity.values()) {
                    val horoscope = RetrofitInstance.getData(entity.name)
                    list.add(Horoscope(
                        entity.name,
                        horoscope?.date_range,
                        horoscope?.description
                    ))
                }

                withContext(Dispatchers.Main) {
                    binding.spinner.visibility = View.INVISIBLE
                    binding.horoscope.adapter = HoroscopeAdapter(list)
                }
            }
        }
        else
            binding.horoscope.adapter = HoroscopeAdapter(list)

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.name.setText(it.name)
            binding.email.text = it.email
            binding.age.setText(it.age)
            binding.height.setText(it.height)
            binding.weight.setText(it.weight)

            Glide.with(binding.root.context)
                .load(Uri.parse(it?.avatar))
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.avatar)
        }

        binding.weight.addTextChangedListener { checkWeight() }
        binding.height.addTextChangedListener { checkWeight() }

        binding.avatar.setOnClickListener {
            val intent = Intent()
            intent.type = INTENT_TYPE
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, EMPTY), 10)
        }
    }

    private fun checkWeight() {
        val height = binding.height.text.toString()
        val weight = binding.weight.text.toString()

        if(height.isNotEmpty() && weight.isNotEmpty() && height.toInt() > 0) {
            when ((weight.toDouble() / (height.toDouble() * height.toDouble() / 10000)).toInt()) {
                in 0..18 -> binding.result.text = activity?.resources(R.string.below_the_norm)
                in 18..24 -> binding.result.text = activity?.resources(R.string.normal)
                in 25..29 -> binding.result.text = activity?.resources(R.string.overweight)
                in 30..34 -> binding.result.text = activity?.resources(R.string.obesity_1)
                in 35..39 -> binding.result.text = activity?.resources(R.string.obesity_2)
                else -> binding.result.text = activity?.resources(R.string.obesity_3)
            }
        }
        else
            binding.result.text = EMPTY
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 10 && resultCode == Activity.RESULT_OK && data != null) {
            val imageData = data.data
            userViewModel.user.value?.avatar = imageData.toString()

            Glide.with(binding.root.context)
                .load(imageData)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.avatar)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val user = userViewModel.user.value
        val userMap = mutableMapOf(
            USER_AVATAR to user?.avatar.toString()
        )

        if(binding.age.text.isNotEmpty() && binding.age.text.toString().toInt() > 0
            && binding.age.text.toString().toInt() < 100 && user?.age != binding.age.text.toString()) {
            user?.age = binding.age.text.toString()
            userMap[USER_AGE] = binding.age.text.toString()
        }

        if(binding.name.text.isNotEmpty() && user?.name != binding.name.text.toString()) {
            user?.name = binding.name.text.toString()
            userMap[USER_NAME] = binding.name.text.toString()
        }

        if(binding.weight.text.isNotEmpty() && binding.weight.text.toString().toInt() > 0
            && binding.weight.text.toString().toInt() < 300 && user?.weight != binding.weight.text.toString()) {
            user?.weight = binding.weight.text.toString()
            userMap[USER_WEIGHT] = binding.weight.text.toString()
        }

        if(binding.height.text.isNotEmpty() && binding.height.text.toString().toInt() > 0
            && binding.height.text.toString().toInt() < 300 && user?.height != binding.height.text.toString()) {
            user?.height = binding.height.text.toString()
            userMap[USER_HEIGHT] = binding.height.text.toString()
        }

        Firebase.firestore.collection(DATABASE_NAME).whereEqualTo(USER_QUERY, user?.id).get()
            .addOnCompleteListener { result ->
                val documentId = result.result.documents[0].id
                Firebase.firestore.collection(DATABASE_NAME).document(documentId)
                    .update(userMap as Map<String, Any>)
            }
    }

    companion object {
        private val list = mutableListOf<Horoscope>()
        private const val EMPTY = ""
    }
}