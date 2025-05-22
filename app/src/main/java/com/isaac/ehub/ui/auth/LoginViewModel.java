package com.isaac.ehub.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isaac.ehub.domain.usecase.auth.CheckAuthenticatedUserUseCase;
import com.isaac.ehub.domain.usecase.auth.GetAuthenticatedUserUseCase;
import com.isaac.ehub.domain.usecase.auth.LoginWithGoogleUseCase;
import com.isaac.ehub.domain.usecase.home.user.CreateUserUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final LoginWithGoogleUseCase loginWithGoogleUseCase;
    private final CheckAuthenticatedUserUseCase checkAuthenticatedUserUseCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final CreateUserUseCase createUserUseCase;

    public final MutableLiveData<LoginViewState> loginViewState = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LoginWithGoogleUseCase loginWithGoogleUseCase,
                          CheckAuthenticatedUserUseCase checkAuthenticatedUserUseCase,
                          CreateUserUseCase createUserUseCase,
                          GetAuthenticatedUserUseCase getAuthenticatedUserUseCase){
        this.loginWithGoogleUseCase = loginWithGoogleUseCase;
        this.checkAuthenticatedUserUseCase = checkAuthenticatedUserUseCase;
        this.createUserUseCase = createUserUseCase;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    public LiveData<LoginViewState> getLoginViewState(){
        return loginViewState;
    }

    public boolean isUserAuthenticated(){
        return checkAuthenticatedUserUseCase.execute();
    }

    public void loginWithGoogle(String tokenId) {
        loginViewState.setValue(LoginViewState.loading());
        loginWithGoogleUseCase.execute(tokenId).observeForever(resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    createUserUseCase.execute(getAuthenticatedUserUseCase.execute());
                    loginViewState.setValue(LoginViewState.success());
                    break;
                case ERROR:
                    loginViewState.setValue(LoginViewState.error(resource.getMessage()));
                    break;
                case LOADING:
                    loginViewState.setValue(LoginViewState.loading());
                    break;
            }
        });
    }
}
