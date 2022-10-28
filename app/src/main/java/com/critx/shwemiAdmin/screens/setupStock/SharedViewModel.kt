package com.critx.shwemiAdmin.screens.setupStock

import androidx.lifecycle.AndroidViewModel
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

}