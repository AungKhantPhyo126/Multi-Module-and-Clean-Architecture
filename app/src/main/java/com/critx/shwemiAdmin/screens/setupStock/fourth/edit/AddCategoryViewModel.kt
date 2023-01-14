package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SetupStock.jewelleryCategory.CalculateKPY
import com.critx.domain.model.SetupStock.jewelleryCategory.DesignDomain
import com.critx.domain.useCase.SetUpStock.*
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.*
import com.critx.shwemiAdmin.uistate.DesignUiState
import com.critx.shwemiAdmin.uistate.JewelleryCategoryUiState
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val createJewelleryCategoryUseCase: CreateJewelleryCategoryUseCase,
    private val editJewelleryCategoryUseCase: EditJewelleryCategoryUseCase,
    private val calculateKPYUseCase: CalculateKPYUseCase,
    private val getDesignListUseCase: GetDesignListUseCase,
    private val getRelatedCategoryUseCase: GetRelatedCategoryUseCase,
    private val localDatabase: LocalDatabase,
    private val getJewelleryTypeUseCase: GetJewelleryTypeUseCase,
    private val getJewelleryQualityUseCase: GetJewelleryQualityUseCase,
    private val getJewelleryGroupUseCase: GetJewelleryGroupUseCase,
    private val getJewelleryCategoryUseCase: GetJewelleryCategoryUseCase,


    ) : ViewModel() {
    var selectedImgUri1: File? = null
    var selectedImgUri2: File? = null
    var selectedImgUri3: File? = null
    var selectedGifUri: File? = null
    var selectedVideoUri: File? = null


    private val _createJewelleryCategoryLiveData = MutableLiveData<Resource<JewelleryCategoryUiModel>>()
    val createJewelleryCategoryLiveData :LiveData<Resource<JewelleryCategoryUiModel>>
    get() = _createJewelleryCategoryLiveData
    fun resetCreateLiveData(){
        _createJewelleryCategoryLiveData.value = null
    }

    private val _getDesign =  MutableLiveData<Resource<List<DesignUiModel>>>()
    val getDesignLiveData :LiveData<Resource<List<DesignUiModel>>>
    get() = _getDesign

    private val _designInCatLiveData =  MutableLiveData<MutableList<DesignUiModel>>()
    val designInCatLiveDataLiveData :LiveData<MutableList<DesignUiModel>>
        get() = _designInCatLiveData

    fun addDesignWithList(list: MutableList<DesignUiModel>){
        _designInCatLiveData.value = list
    }
    fun addDesignByItem(item:DesignUiModel){
        _designInCatLiveData.value!!.add(item)
        _designInCatLiveData.value = _designInCatLiveData.value
    }
    fun removeDesignByItem(item:DesignUiModel){
        _designInCatLiveData.value!!.remove(item)
        _designInCatLiveData.value = _designInCatLiveData.value
    }

    private val _getRelatedCats = MutableLiveData<Resource<MutableList<JewelleryCategoryUiModel>>>()
    val getRelatedCats : LiveData<Resource<MutableList<JewelleryCategoryUiModel>>>
    get() = _getRelatedCats
    fun resetLiveDataForBackPress(){
        _getRelatedCats.value = null
        _getDesign.value = null
    }

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _editJewelleryCategoryState = MutableLiveData<Resource<String>>()
    val editJewelleryCategoryState :LiveData<Resource<String>>
    get() = _editJewelleryCategoryState
    fun resetEditLiveData(){
        _editJewelleryCategoryState.value = null
    }

    fun getRelatedCat(id:String){
        viewModelScope.launch {
            getRelatedCategoryUseCase(localDatabase.getToken().orEmpty(),id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getRelatedCats.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getRelatedCats.value = Resource.Success(result.data!!.map { it.asUiModel() }.toMutableList())
                    }
                    is Resource.Error -> {
                        _getRelatedCats.value = Resource.Error(result.message)
                    }
                }

            }
        }
    }

    fun removeRelatedCat(item:JewelleryCategoryUiModel){
        _getRelatedCats.value!!.data!!.remove(item)
        _getRelatedCats.value = _getRelatedCats.value
    }
    fun addRelatedCat(item:JewelleryCategoryUiModel){
        _getRelatedCats.value!!.data!!.add(item)
        _getRelatedCats.value = _getRelatedCats.value
    }


    fun createJewelleryCategory(
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody,
        recommendCat: MutableList<RequestBody>?

    ) {
        viewModelScope.launch {
            //calculate kyp
            createJewelleryCategoryUseCase(
                localDatabase.getToken().orEmpty(),
                jewellery_type_id,
                jewellery_quality_id,
                groupId,
                is_frequently_used,
                withGem,
                name,
                avgWeigh,
                avgKyat,
                avgPae,
                avgYwae,
                images,
                video,
                specification,
                design,
                orderToGs,
                recommendCat
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _createJewelleryCategoryLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _createJewelleryCategoryLiveData.value = Resource.Success(it.data!!.asUiModel())
                    }
                    is Resource.Error -> {
                        _createJewelleryCategoryLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }

    }

    fun editJewelleryCategory(
        categoryId: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem:RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody,
        recommendCat: MutableList<RequestBody>
    ) {
        val method = "PUT"
        val methodRequestBody = method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            //calculate kyp
            editJewelleryCategoryUseCase(
                localDatabase.getToken().orEmpty(),
                methodRequestBody,
                categoryId,
                jewellery_type_id,
                jewellery_quality_id,
                groupId,
                is_frequently_used,
                withGem,
                name,
                avgWeigh,
                avgKyat,
                avgPae,
                avgYwae,
                images,
                video,
                specification,
                design,
                orderToGs,
                recommendCat
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _editJewelleryCategoryState.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _editJewelleryCategoryState.value = Resource.Success(result.data!!.message)
                    }
                    is Resource.Error -> {
                        _editJewelleryCategoryState.value = Resource.Error(result.message)
                    }
                }
            }
        }


    }


    fun getDesign(jewelleryType:String) {
        viewModelScope.launch {
            getDesignListUseCase(localDatabase.getToken().orEmpty(),jewelleryType).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getDesign.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getDesign.value = Resource.Success(it.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error -> {
                        _getDesign.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    private val _jewelleryTypeLiveData = MutableLiveData<Resource<List<JewelleryTypeUiModel>>>()
    val jewelleryTypeState :LiveData<Resource<List<JewelleryTypeUiModel>>>
        get() = _jewelleryTypeLiveData

    fun getJewelleryType(){
        viewModelScope.launch {
            getJewelleryTypeUseCase(localDatabase.getToken().orEmpty()).collectLatest { result->
                when(result){
                    is Resource.Loading->{
                        _jewelleryTypeLiveData.value =Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewelleryTypeLiveData.value =Resource.Success(result.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error->{
                        _jewelleryTypeLiveData.value =Resource.Error(result.message)
                    }
                }
            }
        }
    }

    private val _jewlleryQualityLiveData =MutableLiveData<Resource<List<JewelleryQualityUiModel>>>()
    val jewelleryQualityLiveData :LiveData<Resource<List<JewelleryQualityUiModel>>>
        get() = _jewlleryQualityLiveData

    fun getJewelleryQuality(){
        viewModelScope.launch {
            getJewelleryQualityUseCase(localDatabase.getToken().orEmpty()).collectLatest { result->
                when(result){
                    is Resource.Loading->{
                        _jewlleryQualityLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewlleryQualityLiveData.value =Resource.Success(result.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error->{
                        _jewlleryQualityLiveData.value =Resource.Error(result.message)
                    }
                }
            }
        }
    }

    private val _getGroupLiveData = MutableLiveData<Resource<List<ChooseGroupUIModel>>>()
    val getGroupLiveData: LiveData<Resource<List<ChooseGroupUIModel>>>
        get() = _getGroupLiveData
    fun getJewelleryGroup(isFrequentlyUse: Int, firstCatId: Int, secondCatId: Int) {
        viewModelScope.launch {
            getJewelleryGroupUseCase(
                localDatabase.getToken().orEmpty(),
                isFrequentlyUse,
                firstCatId,
                secondCatId
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getGroupLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getGroupLiveData.value =
                            Resource.Success(result.data!!.data.map { it.asUiModel() })
                    }
                    is Resource.Error -> {
                        _getGroupLiveData.value = Resource.Error(result.message)
                    }
                }
            }
        }
    }

    private val _getJewelleryCategoryLiveData = MutableLiveData<Resource<List<JewelleryCategoryUiModel>>>()
    val getJewelleryCategoryLiveData :LiveData<Resource<List<JewelleryCategoryUiModel>>>
        get() = _getJewelleryCategoryLiveData
    fun getJewelleryCategory(isFrequentlyUse: Int,firstCatId:Int,secondCatId:Int,thirdCatId:Int) {
        viewModelScope.launch {
            getJewelleryCategoryUseCase(
                localDatabase.getToken().orEmpty(),
                isFrequentlyUse,
                firstCatId,
                secondCatId,
                thirdCatId
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getJewelleryCategoryLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getJewelleryCategoryLiveData.value =Resource.Success(result.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error -> {
                        _getJewelleryCategoryLiveData.value = Resource.Error(result.message)
                    }
                }
            }
        }
    }
    fun resetLiveData(){
        _jewelleryTypeLiveData.value=null
        _jewlleryQualityLiveData.value=null
        _getGroupLiveData.value=null
    }
}

data class SelectedImage(
    val file: File,
    val bitMap: Bitmap
)