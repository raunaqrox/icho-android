package in.icho.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import in.icho.ui.activities.MainActivity;

import in.icho.R;

public class LoginFragment extends Fragment {
    private String type;
    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button closeLogin = (Button) v.findViewById(R.id.closeLogin);

        return v;
    }
    public void setType(String type){
        this.type = type;
    }

}
