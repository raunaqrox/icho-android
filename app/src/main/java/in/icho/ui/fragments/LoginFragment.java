package in.icho.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import in.icho.ui.activities.MainActivity;

import in.icho.R;

public class LoginFragment extends Fragment {
    private String type;
    private Button closeLogin;
    View loginView;
    LoginFragment thisFragment;
    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginView = inflater.inflate(R.layout.fragment_login, container, false);
        return loginView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeLogin = (Button) view.findViewById(R.id.closeLogin);
        final Fragment f = (Fragment)this;
        closeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().remove(f).commit();
            }
        });

    }

    public void hideFragment(View loginFragment) {
        loginFragment.setVisibility(View.GONE);
    }

    public void hideTextViews(View loginFragment) {
        loginFragment.findViewById(R.id.username).setVisibility(View.INVISIBLE);
        loginFragment.findViewById(R.id.password).setVisibility(View.INVISIBLE);
        loginFragment.findViewById(R.id.email).setVisibility(View.INVISIBLE);
    }

    public void showLoginTextViews(View loginFragment) {
        hideTextViews(loginFragment);
        loginFragment.findViewById(R.id.username).setVisibility(View.VISIBLE);
        loginFragment.findViewById(R.id.password).setVisibility(View.VISIBLE);
    }

    public void showRegistrationTextViews(View loginFragment) {
        loginFragment.findViewById(R.id.username).setVisibility(View.VISIBLE);
        loginFragment.findViewById(R.id.password).setVisibility(View.VISIBLE);
        loginFragment.findViewById(R.id.email).setVisibility(View.VISIBLE);
    }

    public void removeFragment(){
//        this.removeFragment();
    }

    public void setType(String type){
        this.type = type;
    }
}
