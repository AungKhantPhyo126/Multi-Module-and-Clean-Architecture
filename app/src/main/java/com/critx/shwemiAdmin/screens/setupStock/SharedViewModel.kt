package com.critx.shwemiAdmin.screens.setupStock

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.critx.shwemiAdmin.uiModel.orderStock.BookMarkStockUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import javax.inject.Inject

class SharedViewModel : ViewModel() {
    var firstCat: JewelleryTypeUiModel? = null
    var secondCat: JewelleryQualityUiModel? = null
    var thirdCat: ChooseGroupUIModel? = null
    var fourthCat: JewelleryCategoryUiModel? = null

//    var sampleTakeScreenUIState: String? = null

//    var orderItem = MutableLiveData<String>()
//    var orderQty = MutableLiveData<String>()
//    var weightK = MutableLiveData<String>()
//    var weightP = MutableLiveData<String>()
//    var weightY = MutableLiveData<String>()
//    var goldBoxId = MutableLiveData<String>()
//    var goldWeight = MutableLiveData<String>()
//    var gemWeight = MutableLiveData<String>()
//    var goldAndGemWeight = MutableLiveData<String>()
//    var wastageK = MutableLiveData<String>()
//    var wastageP = MutableLiveData<String>()
//    var wastageY = MutableLiveData<String>()
//    var dueDate = MutableLiveData<String>()

//    //adding RecommendCat
//    var firstCatForRecommendCat: JewelleryTypeUiModel? = null
//    var secondCatForRecomendCat: JewelleryQualityUiModel? = null
//    var thirdCatForRecommendCat: ChooseGroupUIModel? = null
//
//    fun resetForRecommendCat(){
//        firstCatForRecommendCat = null
//        secondCatForRecomendCat = null
//        thirdCatForRecommendCat = null
//    }

    var hasRemoveRecord :Boolean = false

//    var recommendCatList = MutableLiveData<MutableList<JewelleryCategoryUiModel?>>(mutableListOf(null))
//    fun addRecommendCat(item:JewelleryCategoryUiModel){
//        recommendCatList.value?.add(item)
//        recommendCatList.value=recommendCatList.value!!.toSet().toMutableList()
//    }
//    fun addRecommendCatBatch(itemList:List<JewelleryCategoryUiModel?>){
//        recommendCatList.value?.addAll(itemList)
//        recommendCatList.value=recommendCatList.value!!.toSet().toMutableList()
//    }
//
//    fun removeRecommendCat(itemList:JewelleryCategoryUiModel){
//        hasRemoveRecord = true
//        recommendCatList.value?.remove(itemList)
//        recommendCatList.value=recommendCatList.value!!.toSet().toMutableList()
//    }
//
//    fun resetRecommendCat(){
//        hasRemoveRecord = false
//        recommendCatList.value?.removeAll(recommendCatList.value!!)
//        recommendCatList.value?.add(null)
//        recommendCatList.value=recommendCatList.value
//    }

//    //order stock
//    var selectedBookMark :BookMarkStockUiModel? = null
//    var bookMarkAvgKyat : MultipartBody.Part? = null
//    var bookMarkAvgPae : MultipartBody.Part? = null
//    var bookMarkAvgYwae : MultipartBody.Part? = null
//    var orderGoldQuality : MultipartBody.Part? = null
//    var orderGoldSmith : MultipartBody.Part? = null
//    var bookMarkId : MultipartBody.Part? = null
//    var equivalent_pure_gold_weight_kpy : MultipartBody.Part? = null
//    var jewellery_type_size_id : MutableList<MultipartBody.Part> = mutableListOf()
//    var order_qty : MutableList<MultipartBody.Part> = mutableListOf()
//    var sample_id : MutableList<MultipartBody.Part> = mutableListOf()
//
//    fun resetOrderStockData(){
//        selectedBookMark = null
//        bookMarkAvgKyat = null
//        bookMarkAvgPae = null
//        bookMarkAvgYwae = null
//        orderGoldQuality = null
//        orderGoldSmith = null
//        bookMarkId = null
//        equivalent_pure_gold_weight_kpy = null
//        jewellery_type_size_id = mutableListOf()
//        order_qty = mutableListOf()
//        sample_id = mutableListOf()
//    }

}