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
import android.widget.TextView;

import in.icho.ui.activities.MainActivity;

import in.icho.R;

public class LoginFragment extends Fragment {
    private String type;
    private Button closeLogin;
    private Button loginButton;
    private Button registerButton;
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
                return true; }
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

        final Fragment loginFragmnet = (Fragment)this;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validLoginClick(currentView)){
                    showLoginTextViews(currentView);
                }else{

                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllViews(currentView);
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
//        TextView email = (TextView) v.findViewById(R.id.email);
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
