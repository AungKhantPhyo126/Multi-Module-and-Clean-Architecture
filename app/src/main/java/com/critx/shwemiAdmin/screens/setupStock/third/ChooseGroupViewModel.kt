package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseGroupViewModel @Inject constructor() : ViewModel(){
    private var _groupImages= MutableLiveData<MutableList<ChooseGroupUIModel>>()
    val groupImages : LiveData<MutableList<ChooseGroupUIModel>>
        get() = _groupImages
    private val groupImageList = mutableListOf<ChooseGroupUIModel>()

    fun setImageList(list:MutableList<ChooseGroupUIModel>){
        groupImageList.addAll(list)
        _groupImages.value=groupImageList
    }

    fun selectImage(id:String){
        groupImageList.find {
            it.id==id
        }?.isChecked = groupImageList.find {
            it.id==id
        }?.isChecked!!.not()

        groupImageList.filter {
            it.id != id
        }.forEach {
            it.isChecked = false
        }

        _groupImages.value=groupImageList
    }
}