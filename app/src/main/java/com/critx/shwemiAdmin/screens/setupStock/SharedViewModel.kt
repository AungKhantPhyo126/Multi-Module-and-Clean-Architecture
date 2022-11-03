package com.critx.shwemiAdmin.screens.setupStock

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedViewModel : ViewModel() {
    var firstCat: JewelleryTypeUiModel? = null
    var secondCat: JewelleryQualityUiModel? = null
    var thirdCat: ChooseGroupUIModel? = null
    var fourthCat: JewelleryCategoryUiModel? = null

    var sampleTakeScreenUIState: String? = null

    var selectedGoldBoxId =  MutableLiveData<String>()
    var selectedGoldSmith = MutableLiveData<String>()
    var orderItem = MutableLiveData<String>()
    var orderQty = MutableLiveData<String>()
    var weightK = MutableLiveData<String>()
    var weightP = MutableLiveData<String>()
    var weightY = MutableLiveData<String>()
    var goldBoxId = MutableLiveData<String>()
    var goldWeight = MutableLiveData<String>()
    var gemWeight = MutableLiveData<String>()
    var goldAndGemWeight = MutableLiveData<String>()
    var wastageK = MutableLiveData<String>()
    var wastageP = MutableLiveData<String>()
    var wastageY = MutableLiveData<String>()
    var dueDate = MutableLiveData<String>()

    //adding RecommendCat
    var firstCatForRecommendCat: JewelleryTypeUiModel? = null
    var secondCatForRecomendCat: JewelleryQualityUiModel? = null
    var thirdCatForRecommendCat: ChooseGroupUIModel? = null

    var hasRemoveRecord :Boolean = false

    var recommendCatList = MutableLiveData<MutableList<JewelleryCategoryUiModel?>>(mutableListOf(null))
    fun addRecommendCat(item:JewelleryCategoryUiModel){
        recommendCatList.value?.add(item)
        recommendCatList.value=recommendCatList.value!!.toSet().toMutableList()
    }
    fun addRecommendCatBatch(itemList:List<JewelleryCategoryUiModel?>){
        recommendCatList.value?.addAll(itemList)
        recommendCatList.value=recommendCatList.value!!.toSet().toMutableList()
    }

    fun removeRecommendCat(itemList:JewelleryCategoryUiModel){
        hasRemoveRecord = true
        recommendCatList.value?.remove(itemList)
        recommendCatList.value=recommendCatList.value!!.toSet().toMutableList()
    }

    fun resetRecommendCat(){
        hasRemoveRecord = false
        recommendCatList.value?.removeAll(recommendCatList.value!!)
        recommendCatList.value?.add(null)
        recommendCatList.value=recommendCatList.value
    }

}