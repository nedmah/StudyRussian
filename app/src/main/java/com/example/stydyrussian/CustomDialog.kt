package com.example.stydyrussian

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.example.stydyrussian.databinding.CustomDialogBinding

class CustomDialog(
    context: Context,
    private val imageId: Int,
    private val title: String,
    private val message: String? = null,
    private val isNeutralButtonVisible: Boolean = true,
    private val positiveButtonText: String = "Да",
    private val neutralButtonText: String = "Назад",
    private val onPositiveButtonCallback: () -> Unit = {}
) : Dialog(context) {

    private lateinit var binding: CustomDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.DialogImage.setImageResource(imageId)
        binding.title.text = title
        if (message != null) {
            binding.message.text = message
        } else {
            binding.message.visibility = View.GONE
        }

        binding.positiveButton.text = positiveButtonText
        binding.neutralButton.text = neutralButtonText

        binding.positiveButton.setOnClickListener {
            onPositiveButtonCallback.invoke()
            this.dismiss()
        }

        if (isNeutralButtonVisible) {
            binding.neutralButton.visibility = View.VISIBLE
            binding.neutralButton.setOnClickListener {
                this.dismiss()
            }
        } else {
            binding.neutralButton.visibility = View.GONE
        }

        this.setCancelable(false)
        this.setCanceledOnTouchOutside(true)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}