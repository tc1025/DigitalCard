package digitalcard.digitalcard.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.Task;

import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class AccountFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Toolbar toolbar;

    GoogleSignInClient googleSignInClient;

    SignInButton signInButton;
    TextView tvTitle, tvAccountName;
    LinearLayout llNotConnected, llConnected, btnBack;

    boolean loginStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getActivity().getSharedPreferences(Utilities.CHECK_ACCOUNT, MODE_PRIVATE);
        String restoredText = prefs.getString("getPref", null);
        if (restoredText != null) {
            loginStatus = prefs.getBoolean(Utilities.LOGIN_STATUS, false);//"No name defined" is the default value.
        } else {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utilities.CHECK_ACCOUNT, MODE_PRIVATE).edit();
            editor.putBoolean(Utilities.LOGIN_STATUS, false);
            editor.apply();
            loginStatus = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);

        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).getSlidingPanel().setTouchEnabled(false);
        }

        toolbar = rootView.findViewById(R.id.toolbar);

        tvTitle = toolbar.getTxtTitle();
        tvTitle.setText("Account");
        btnBack = toolbar.getBtnBack();

        signInButton = rootView.findViewById(R.id.btn_sign_in);
        llConnected = rootView.findViewById(R.id.ll_connected_account);
        llNotConnected = rootView.findViewById(R.id.ll_no_connected_account);
        tvAccountName = rootView.findViewById(R.id.sign_in_name);

        if (!loginStatus) {
            llConnected.setVisibility(View.GONE);
            llNotConnected.setVisibility(View.VISIBLE);
        } else {
            llConnected.setVisibility(View.VISIBLE);
            llNotConnected.setVisibility(View.GONE);
        }

        signInButton.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void updateUI(GoogleSignInAccount account){
        llNotConnected.setVisibility(View.GONE);
        llConnected.setVisibility(View.VISIBLE);
        tvAccountName.setText(account.getDisplayName());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utilities.CHECK_ACCOUNT, MODE_PRIVATE).edit();
        editor.putBoolean(Utilities.LOGIN_STATUS, true);
        editor.apply();
        loginStatus = true;
    }
}
