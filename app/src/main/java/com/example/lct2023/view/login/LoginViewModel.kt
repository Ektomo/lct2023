package com.example.lct2023.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lct2023.LctDataStore
import com.example.lct2023.StartPosition
import com.example.lct2023.gate.LctGate
import com.example.lct2023.gate.model.RegisterRequestBody
import com.example.lct2023.view.ViewStateClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginData(
    var pass: String,
    var name: String
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: LctDataStore,
    val gate: LctGate
) : ViewModel() {
//    val login = MutableStateFlow("")
//    val pass = MutableStateFlow("")

    private val _state: MutableStateFlow<ViewStateClass<LoginData>> = MutableStateFlow(
        ViewStateClass.Data(LoginData("",""))
    )
    val state = _state.asStateFlow()

    private val _loginState: MutableStateFlow<StartPosition> =
        MutableStateFlow(StartPosition.NotLogin)
    val loginState = _loginState.asStateFlow()


    fun goToRegister(){
        _loginState.update {
            StartPosition.Register
        }
    }

    fun goToLogin(){
        _loginState.update {
            StartPosition.NotLogin
        }
    }

    fun doRegister(
        userName: String = "user",
        firstName: String = "user",
        lastName: String = "userovskiy",
        pass: String = "123"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //Тут должна быть логика с логином
            try {
                _loginState.update {
                    StartPosition.Loading
                }

//                var registeredUser = gate.registerUser(
//                    RegisterRequestBody(
//                        userName,
//                        firstName,
//                        lastName,
//                        pass
//                    )
//                )
                _state.update {
                    ViewStateClass.Data(LoginData(pass, userName))
                }
                _loginState.update {
                    StartPosition.NotLogin
                }
            } catch (e: Exception) {
                _state.update{
                    ViewStateClass.Error(e)
                }
                _loginState.update {
                    StartPosition.NotLogin
                }
            }

        }
    }

    fun doLogin(userName: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Тут должна быть логика с логином
                _loginState.update {
                    StartPosition.Loading
                }
//                if (_state.value is ViewStateClass.Data<*>){
//                    val data = (_state.value as ViewStateClass.Data<LoginData>).data
                    gate.setComponents(
//                    "a", "12345678"
                        userName, pass
                    )

//                    val user = gate.login(
////                    "a"
//                        data.name
//                    )

//                    dataStore.saveUserId(user.id.toString())

//                    if (user.role == "user") {
//                        _loginState.update {
//                            StartPosition.LoginUser
//                        }
//                    } else {
//                        _loginState.update {
//                            StartPosition.LoginInspector
//                        }
//                    }
                    if (userName == "") {
                        _loginState.update {
                            StartPosition.LoginUser
                        }
                    } else {
                        _loginState.update {
                            StartPosition.LoginInspector
                        }
                    }
//                }


            } catch (e: Exception) {
                _state.update{
                    ViewStateClass.Error(e)
                }
                _loginState.update {
                    StartPosition.NotLogin
                }
            }

        }
    }

}