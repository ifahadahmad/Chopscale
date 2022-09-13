package com.example.imagediff.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagediff.service.repository.SuperAPIServices
import com.example.imagediff.service.model.SuperModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SuperViewModel():ViewModel() {
    private val TAG="SuperViewModel"


    private val superAPIServices = SuperAPIServices()

    private val _super_response = MutableLiveData<SuperModel>()

    val super_response:LiveData<SuperModel>
    get() = _super_response

    private val _statusError = MutableLiveData<Boolean>()

    val statusError:LiveData<Boolean>
    get() = _statusError

    private val _error = MutableLiveData<ErrorClass>()

    val error:LiveData<ErrorClass>
    get() = _error

    private val _statusLoading = MutableLiveData<Boolean>()

    val statusLoading:LiveData<Boolean>
    get() = _statusLoading



    public fun sendDataToAPI(url:String,file:MultipartBody.Part){

        viewModelScope.launch() {

             _statusLoading.postValue(true)

             try {
                 val data = superAPIServices.sendDataServices(url,file)
                 if(data.error){
                     throw ErrorException(data.message,data.errorCode)
                 }
                 _statusError.postValue(false)
                 _statusLoading.postValue(false)
                 _super_response.postValue(data)
             }catch (error:ErrorException){
                 _statusError.postValue(true)
                 _error.postValue(ErrorClass(error.message,error.errorCode))
             } catch (error:Throwable){
                 _statusLoading.postValue(false)
                 _statusError.postValue(true)
                 _error.postValue(ErrorClass("Failed to Connect, make sure to have active internet connection",404))
             }


        }
    }

    data class ErrorClass(val errorMessage:String,val errorCode:Int)
    data class ErrorException(override val message:String,val errorCode:Int):Exception(message)

}