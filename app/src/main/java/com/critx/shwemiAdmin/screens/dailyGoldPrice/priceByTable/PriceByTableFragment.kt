package com.critx.shwemiAdmin.screens.dailyGoldPrice.priceByTable

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.common.ui.validatePrice
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.databinding.FragmentPriceByTableBinding
import com.critx.shwemiAdmin.screens.dailyGoldPrice.DailyGoldPriceViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceByTableFragment : Fragment() {
    private lateinit var binding: FragmentPriceByTableBinding
    private val viewModel by viewModels<DailyGoldPriceViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPriceByTableBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getRebuyPrice()

        viewModel.getRebuyPriceLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.layoutSmallItems.edtDamageX.setText(it.data!!.small[0].price)
                    binding.layoutSmallItems.edtDamageY.setText(it.data!!.small[1].price)
                    binding.layoutSmallItems.edtDamageZ.setText(it.data!!.small[2].price)

                    binding.layoutSmallItems.edtGoodX.setText(it.data!!.small[3].price)
                    binding.layoutSmallItems.edtGoodY.setText(it.data!!.small[4].price)
                    binding.layoutSmallItems.edtGoodZ.setText(it.data!!.small[5].price)

                    binding.layoutSmallItems.edtNotToGoX.setText(it.data!!.small[6].price)
                    binding.layoutSmallItems.edtNotToGoY.setText(it.data!!.small[7].price)
                    binding.layoutSmallItems.edtNotToGoZ.setText(it.data!!.small[8].price)

                    binding.layoutLargeItems.edtDamageX.setText(it.data!!.large[0].price)
                    binding.layoutLargeItems.edtDamageY.setText(it.data!!.large[1].price)
                    binding.layoutLargeItems.edtDamageZ.setText(it.data!!.large[2].price)

                    binding.layoutLargeItems.edtGoodX.setText(it.data!!.large[3].price)
                    binding.layoutLargeItems.edtGoodY.setText(it.data!!.large[4].price)
                    binding.layoutLargeItems.edtGoodZ.setText(it.data!!.large[5].price)

                    binding.layoutLargeItems.edtNotToGoX.setText(it.data!!.large[6].price)
                    binding.layoutLargeItems.edtNotToGoY.setText(it.data!!.large[7].price)
                    binding.layoutLargeItems.edtNotToGoZ.setText(it.data!!.large[8].price)

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }
        //X-0 Y-0
        val horizontalOptionName: HashMap<String, String> = HashMap()
        val verticalOptionName: HashMap<String, String> = HashMap()
        val horizontalOptionLevel: HashMap<String, String> = HashMap()
        val verticalOptionLevel: HashMap<String, String> = HashMap()
        val size: HashMap<String, String> = HashMap()
        val price: HashMap<String, String> = HashMap()



        viewModel.updateRebuyPrice.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    viewModel.getRebuyPrice()
                    requireContext().showSuccessDialog(it.data!!) {

                    }

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }

        }


        binding.btnUpdate.setOnClickListener {
            if (validatePrice(binding.layoutSmallItems.edtDamageX,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtDamageY,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtDamageZ,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtGoodX,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtGoodY,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtGoodZ,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtNotToGoX,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtNotToGoY,requireContext()) &&
                validatePrice(binding.layoutSmallItems.edtNotToGoZ,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtDamageX,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtDamageY,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtDamageZ,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtGoodX,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtGoodY,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtGoodZ,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtNotToGoX,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtNotToGoY,requireContext()) &&
                validatePrice(binding.layoutLargeItems.edtNotToGoZ,requireContext())

            ) {
                repeat(19) {
                    when (it) {
                        1 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Damage"
                            verticalOptionName["data[$it][vertical_option_name]"] = "X"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "0"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "0"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtDamageX.text.toString()
                        }
                        2 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Damage"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Y"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "0"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "1"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtDamageY.text.toString()
                        }
                        3 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Damage"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Z"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "0"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "2"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtDamageZ.text.toString()
                        }
                        4 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Good"
                            verticalOptionName["data[$it][vertical_option_name]"] = "X"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "1"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "0"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtGoodX.text.toString()
                        }
                        5 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Good"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Y"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "1"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "1"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtGoodY.text.toString()
                        }
                        6 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Good"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Z"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "1"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "2"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtGoodZ.text.toString()
                        }
                        7 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "NotToGo"
                            verticalOptionName["data[$it][vertical_option_name]"] = "X"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "2"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "0"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtNotToGoX.text.toString()
                        }
                        8 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "NotToGo"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Y"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "2"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "1"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtNotToGoY.text.toString()
                        }
                        9 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "NotToGo"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Z"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "2"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "2"
                            size["data[$it][size]"] = "small"
                            price["data[$it][price]"] =
                                binding.layoutSmallItems.edtNotToGoZ.text.toString()
                        }
                        10 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Damage"
                            verticalOptionName["data[$it][vertical_option_name]"] = "X"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "0"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "0"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtDamageX.text.toString()
                        }
                        11 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Damage"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Y"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "0"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "1"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtDamageY.text.toString()
                        }
                        12 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Damage"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Z"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "0"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "2"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtDamageZ.text.toString()
                        }
                        13 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Good"
                            verticalOptionName["data[$it][vertical_option_name]"] = "X"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "1"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "0"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtGoodX.text.toString()
                        }
                        14 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Good"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Y"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "1"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "1"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtGoodY.text.toString()
                        }
                        15 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "Good"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Z"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "1"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "2"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtGoodZ.text.toString()
                        }
                        16 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "NotToGo"
                            verticalOptionName["data[$it][vertical_option_name]"] = "X"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "2"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "0"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtNotToGoX.text.toString()
                        }
                        17 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "NotToGo"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Y"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "2"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "1"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtNotToGoY.text.toString()
                        }
                        18 -> {
                            horizontalOptionName["data[$it][horizontal_option_name]"] = "NotToGo"
                            verticalOptionName["data[$it][vertical_option_name]"] = "Z"
                            horizontalOptionLevel["data[$it][horizontal_option_level]"] = "2"
                            verticalOptionLevel["data[$it][vertical_option_level]"] = "2"
                            size["data[$it][size]"] = "large"
                            price["data[$it][price]"] =
                                binding.layoutLargeItems.edtNotToGoZ.text.toString()
                        }

                    }
                }
                viewModel.updateRebuyPrice(
                    horizontalOptionName,
                    verticalOptionName,
                    horizontalOptionLevel,
                    verticalOptionLevel,
                    size,
                    price
                )
            } else {

            }


        }
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}