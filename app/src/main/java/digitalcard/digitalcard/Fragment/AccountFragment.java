package digitalcard.digitalcard.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import digitalcard.digitalcard.Database.AccountDB;
import digitalcard.digitalcard.MainActivity;
import digitalcard.digitalcard.Model.UserAccount;
import digitalcard.digitalcard.Module.EditDialog;
import digitalcard.digitalcard.Module.Toolbar;
import digitalcard.digitalcard.R;
import digitalcard.digitalcard.Util.Utilities;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class AccountFragment extends Fragment implements View.OnClickListener{
    View rootView;
    Toolbar toolbar;
    EditDialog editDialog;

    AccountDB accountDB;
    List<UserAccount> accountArrayList = new ArrayList<>();
    GoogleSignInClient googleSignInClient;

    SignInButton signInButton;
    Button signOutButton, backupButton;
    TextView tvTitle, tvAccountName, tvAccountEmail, tvAccountDOB, tvAccountIdentityNumber, tvAccountAddress, tvAccountPhoneNumber;
    LinearLayout llNotConnected, llConnected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build();
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getContext()), gso);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);
        editDialog = new EditDialog(getActivity());

        toolbar = rootView.findViewById(R.id.toolbar);

        tvTitle = toolbar.getTxtTitle();
        signInButton = rootView.findViewById(R.id.btn_sign_in);
        backupButton = rootView.findViewById(R.id.btn_backup);
        signOutButton = rootView.findViewById(R.id.btn_sign_out);
        llConnected = rootView.findViewById(R.id.ll_connected_account);
        llNotConnected = rootView.findViewById(R.id.ll_no_connected_account);
        tvAccountName = rootView.findViewById(R.id.account_name);
        tvAccountEmail = rootView.findViewById(R.id.account_email);
        tvAccountDOB = rootView.findViewById(R.id.account_dob);
        tvAccountIdentityNumber = rootView.findViewById(R.id.account_identity_number);
        tvAccountAddress = rootView.findViewById(R.id.account_address);
        tvAccountPhoneNumber = rootView.findViewById(R.id.account_phone_number);

        tvTitle.setText("Account");

        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        backupButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_backup:
                saveSQLite();
                break;

            case R.id.account_dob:
                new DatePickerDialog(getContext(), R.style.DatePickerDialogTheme, date, 1990, 1, 1).show();
                break;
        }
    }

    public void saveSQLite() {
        Toast.makeText(getActivity(), "You choose backup", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount((getActivity()));
        if (account != null)
            updateUI(account);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    int id;
    public void updateUI(GoogleSignInAccount account){
        llNotConnected.setVisibility(View.GONE);
        llConnected.setVisibility(View.VISIBLE);

        // load user account
        accountDB = new AccountDB(getContext());
        List<UserAccount> userAccounts = accountDB.getAccount();
        if (!userAccounts.isEmpty()) {
            for (UserAccount data : userAccounts) {
                accountArrayList.add(new UserAccount(data.getName(), data.getEmail(), data.getDob(), data.getIdentityNumber(), data.getAddress(), data.getPhoneNumber()));
                id = data.getId();
            }
            insertData(accountArrayList.get(0), id);

        } else {
            accountDB.addAccount(new UserAccount(account.getDisplayName(), account.getEmail(), "", "", "", ""));
            List<UserAccount> userAccounts1 = accountDB.getAccount();
            for (UserAccount data : userAccounts1) {
                accountArrayList.add(new UserAccount(data.getId(), data.getName(), data.getEmail(), data.getDob(), data.getIdentityNumber(), data.getAddress(), data.getPhoneNumber()));
                id = data.getId();
            }
            insertData(accountArrayList.get(0), id);
        }

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utilities.CHECK_ACCOUNT, MODE_PRIVATE).edit();
        editor.putBoolean(Utilities.LOGIN_STATUS, true);
        editor.apply();
    }

    public void insertData(final UserAccount userAccount, final int id) {
        tvAccountName.setText(userAccount.getName());
        tvAccountEmail.setText(userAccount.getEmail());
        if (!userAccount.getDob().equals(""))
            tvAccountDOB.setText(userAccount.getDob());
        if (!userAccount.getIdentityNumber().equals(""))
            tvAccountIdentityNumber.setText(userAccount.getIdentityNumber());
        if (!userAccount.getAddress().equals(""))
            tvAccountAddress.setText(userAccount.getAddress());
        if (!userAccount.getPhoneNumber().equals(""))
            tvAccountPhoneNumber.setText(userAccount.getPhoneNumber());

        tvAccountDOB.setOnClickListener(this);
        tvAccountIdentityNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton(userAccount, "identityNumber", id);
            }
        });
        tvAccountAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton(userAccount, "address", id);
            }
        });
        tvAccountPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton(userAccount, "phoneNumber", id);
            }
        });
    }

    public void editButton(final UserAccount userAccount, final String editSection, final int id) {
        editDialog.show();
        if (editDialog.isShowing()) {
            TextView tvTitle = editDialog.getTvTitle();
            final EditText etEdit = editDialog.getEtEdit();
            Button btnDone = editDialog.getBtnDone();
            Button btnCancel = editDialog.getBtnCancel();
            etEdit.setText("");

            switch (editSection) {
                case "identityNumber":
                    tvTitle.setText("Input new identity number here");
                    break;

                case "address":
                    tvTitle.setText("Input new address here");
                    break;

                case "phoneNumber":
                    tvTitle.setText("Input new phone number");
                    break;
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editDialog.dismiss();
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getEdited = etEdit.getText().toString();
                    switch (editSection) {
                        case "DOB":
                            accountDB.updateAccount(new UserAccount(id, userAccount.getName(), userAccount.getEmail(), getEdited, userAccount.getIdentityNumber(), userAccount.getAddress(), userAccount.getPhoneNumber()));
                            tvAccountDOB.setText(getEdited);
                            break;
                        case "identityNumber":
                            accountDB.updateAccount(new UserAccount(id, userAccount.getName(), userAccount.getEmail(), userAccount.getDob(), getEdited, tvAccountAddress.getText().toString(), tvAccountPhoneNumber.getText().toString()));
                            tvAccountIdentityNumber.setText(getEdited);
                            break;
                        case "address":
                            accountDB.updateAccount(new UserAccount(id, userAccount.getName(), userAccount.getEmail(), userAccount.getDob(), tvAccountIdentityNumber.getText().toString(), getEdited, tvAccountPhoneNumber.getText().toString()));
                            tvAccountAddress.setText(getEdited);
                            break;
                        case "phoneNumber":
                            accountDB.updateAccount(new UserAccount(id, userAccount.getName(), userAccount.getEmail(), userAccount.getDob(), tvAccountIdentityNumber.getText().toString(), tvAccountAddress.getText().toString(), getEdited));
                            tvAccountPhoneNumber.setText(getEdited);
                            break;
                    }
                    editDialog.dismiss();
                }
            });
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 2);
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        accountDB.deleteAccount(id);
                        llNotConnected.setVisibility(View.VISIBLE);
                        llConnected.setVisibility(View.GONE);
                        ((MainActivity) getContext()).getSlidingPanel().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                });
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

    Calendar calendar = Calendar.getInstance();
    String dateFormat = "dd/MM/yyyy";
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_YEAR, dayOfMonth);
            updateDate();
        }
    };

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        tvAccountDOB.setText(sdf.format(calendar.getTime()));
        accountDB.updateAccount(new UserAccount(id, tvAccountName.getText().toString(), tvAccountEmail.getText().toString(), sdf.format(calendar.getTime()), tvAccountIdentityNumber.getText().toString(), tvAccountAddress.getText().toString(), tvAccountPhoneNumber.getText().toString()));
    }
}
