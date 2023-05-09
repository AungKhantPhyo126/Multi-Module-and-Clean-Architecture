package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.flashSale.FlashDataSource
import com.critx.data.datasource.voucher.ConfirmVoucherDataSource
import com.critx.data.network.dto.asDomain
import com.critx.domain.model.CustomerIdDomain
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.StockInVoucherDomain
import com.critx.domain.model.flashSales.UserPointsDomain
import com.critx.domain.model.voucher.ScanVoucherToConfirmDomain
import com.critx.domain.model.voucher.UnConfirmVoucherDomain
import com.critx.domain.repository.ConfirmVoucherRepository
import com.critx.domain.repository.FlashSaleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ConfirmVoucherRepoImpl @Inject constructor(
    private val confirmVoucherDataSource: ConfirmVoucherDataSource
) : ConfirmVoucherRepository {
    override fun getVouchers(token: String, type: String): Flow<Resource<List<UnConfirmVoucherDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        confirmVoucherDataSource.getVouchers(
                            token,
                            type
                        ).map { it.asDomain() }
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun getStockInVoucher(token: String, voucherCode: String): Flow<Resource<List<StockInVoucherDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        confirmVoucherDataSource.getStockInVoucher(
                            token,
                            voucherCode
                        ).map { it.asDomain() }
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun confirmVoucher(token: String, voucherCode: String): Flow<Resource<String>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        confirmVoucherDataSource.confirmVoucher(
                            token,
                            voucherCode
                        )
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun scanDiscountVoucher(
        token: String,
        voucherCode: String
    ): Flow<Resource<DiscountVoucherScanDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        confirmVoucherDataSource.scanDiscountVoucher(
                            token,
                            voucherCode
                        ).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun scanVoucherToConfirm(
        token: String,
        voucherCode: String
    ): Flow<Resource<ScanVoucherToConfirmDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        confirmVoucherDataSource.scanVoucherToConfirm(
                            token,
                            voucherCode
                        ).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun addDiscount(
        token: String,
        voucherCodes: List<String>,
        amount: String
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        confirmVoucherDataSource.addDiscount(
                            token,
                            voucherCodes, amount
                        ).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }
}