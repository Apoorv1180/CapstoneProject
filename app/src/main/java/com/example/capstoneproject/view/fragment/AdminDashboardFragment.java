package com.example.capstoneproject.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.capstoneproject.R;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.google.firebase.auth.FirebaseUser;

public class AdminDashboardFragment extends Fragment {

    private SendMessages sendMessages;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendMessages = (AdminDashboardFragment.SendMessages) context;
    }

    // Interface - fragment to activity
    public interface SendMessages {
        void sendAction(int actionItem);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tvCountApporved;
    CardView cardApproved;
    TextView tvCountOpen;
    CardView cardOpen;
    LinearLayout mainGrid;
    TextView tvCountClose;
    CardView cardClose;
    TextView dashboardItemValueTv;
    TextView dashboardItemNameTv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDashboardFragment newInstance(String param1, String param2) {
        AdminDashboardFragment fragment = new AdminDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        tvCountApporved = view.findViewById(R.id.tv_count_apporved);
        cardApproved = view.findViewById(R.id.card_plan);
        mainGrid = view.findViewById(R.id.mainGrid);
        tvCountClose = view.findViewById(R.id.tv_count_close);
        cardClose = view.findViewById(R.id.card_article);
        dashboardItemValueTv = view.findViewById(R.id.dashboard_item_value_tv);
        dashboardItemNameTv = view.findViewById(R.id.dashboard_item_name_tv);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkUserLoggedInStatus(view, savedInstanceState);
        initViewListeners();
    }

    private void initViewListeners() {
        cardClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessages.sendAction(1);
            }
        });
        cardApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessages.sendAction(0);
            }
        });
    }

    private void checkUserLoggedInStatus(View view, Bundle savedInstanceState) {
        final CheckUserLoggedInViewModel viewModelLoggedInStatus =
                ViewModelProviders.of(this)
                        .get(CheckUserLoggedInViewModel.class);
        observeViewModelLoggedInStatus(viewModelLoggedInStatus, view, savedInstanceState);
    }

    private void observeViewModelLoggedInStatus(CheckUserLoggedInViewModel viewModelLoggedInStatus, final View view, Bundle savedInstanceState) {

        viewModelLoggedInStatus.isAlreadyLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser result) {
                if (result != null) {
                }
            }
        });
    }
}
