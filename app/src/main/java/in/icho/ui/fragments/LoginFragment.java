package in.icho.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.icho.API.IchoService;
import in.icho.model.User;
import in.icho.model.Token;
import in.icho.utils.URLStore;

import in.icho.R;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {
    private String type;
    private Button closeLogin;
    private Button loginButton;
    private Button registerButton;
    private EditText username;
    private EditText password;
    private EditText email;
    View loginView;
    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginView = inflater.inflate(R.layout.fragment_login, container, false);

        // consumes the touch event when this fragment is on top
        loginView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return loginView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // edge case here for onpause on fragment
        final View currentView = view;
        hideLoadingAnimation(currentView);
        hideTextViews(view);
        super.onViewCreated(view, savedInstanceState);
        closeLogin = (Button) view.findViewById(R.id.closeLogin);
        loginButton = (Button) view.findViewById(R.id.login);
        registerButton = (Button) view.findViewById(R.id.register);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        email = (EditText) view.findViewById(R.id.email);
        final Fragment loginFragmnet = (Fragment) this;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validLoginClick(currentView)) {
                    showLoginTextViews(currentView);
                } else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URLStore.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    IchoService service = retrofit.create(IchoService.class);
                    User myUser = new User(email.getText().toString(), password.getText().toString());
                    retrofit2.Call<Token> call = service.logUserIn(myUser);
                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.isSuccess()) {
                                Token t = response.body();
                                Log.d("TOKEN",t.getToken());
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {

                        }
                    });
                    Log.d("LOGIN", "LOGIN DONE");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllViews(currentView);
                if (!validRegistrationClick(currentView)) {
                    showAllViews(currentView);
                } else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URLStore.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    IchoService service = retrofit.create(IchoService.class);
                    User myUser = new User(username.getText().toString(),
                            email.getText().toString(), password.getText().toString());
                    retrofit2.Call<User> call = service.registerUser(myUser);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccess()) {
                                User u = response.body();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });

    closeLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getFragmentManager()
                    .beginTransaction().remove(loginFragmnet).commit();
        }
    });
    }

    public Boolean validLoginClick(View v){
        String username = ((TextView) v.findViewById(R.id.username)).getText().toString();
        String password = ((TextView) v.findViewById(R.id.password)).getText().toString();

        // they could click just to go to login from register, maybe use a flag to know if that is the case too
        // initially I was using email.getVisibility == View.VISIBLE to say that it is in registration mode and wants to revert
        // back to login
        if(username.length() == 0 || password.length() == 0) {
            return false;
        }else {
            return true;
        }
    }

    public Boolean validRegistrationClick(View v){
        String email = ((TextView) v.findViewById(R.id.email)).getText().toString();
        String username = ((TextView) v.findViewById(R.id.username)).getText().toString();
        String password = ((TextView) v.findViewById(R.id.password)).getText().toString();

        if(username.length() == 0 || password.length() == 0 || email.length() == 0) {
            return false;
        }else {
            return true;
        }

    }

    public void hideFragment(View loginFragment) {
        loginFragment.setVisibility(View.GONE);
    }

    public void hideTextViews(View v) {
        v.findViewById(R.id.username).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.password).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.email).setVisibility(View.INVISIBLE);
    }

    public void showLoginTextViews(View v) {
        hideTextViews(v);
        v.findViewById(R.id.username).setVisibility(View.VISIBLE);
        v.findViewById(R.id.password).setVisibility(View.VISIBLE);
    }

    public void showAllViews(View v) {
        v.findViewById(R.id.username).setVisibility(View.VISIBLE);
        v.findViewById(R.id.password).setVisibility(View.VISIBLE);
        v.findViewById(R.id.email).setVisibility(View.VISIBLE);
    }

    public void hideLoadingAnimation(View v){
        v.findViewById(R.id.loginLoading).setVisibility(View.INVISIBLE);
    }

    public void showLoadingAnimation(View v){
        v.findViewById(R.id.loginLoading).setVisibility(View.VISIBLE);
    }

    public void setType(String type){
        this.type = type;
    }
}
