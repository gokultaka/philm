package app.philm.in.fragments;

import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import app.philm.in.PhilmApplication;
import app.philm.in.R;
import app.philm.in.controllers.MainController;
import app.philm.in.controllers.MainController.MainControllerUi;
import app.philm.in.controllers.MainController.MainControllerUiCallbacks;
import app.philm.in.controllers.MainController.SideMenuItem;
import app.philm.in.model.PhilmUserProfile;

public class SideMenuFragment extends Fragment implements MainControllerUi, View.OnClickListener {

    private SideMenuItem[] mSideMenuItems;

    private MainControllerUiCallbacks mCallbacks;

    private LinearLayout mSideItemsLayout;
    private Button mAccountButton;
    private ImageView mAvatarImageView;

    private PhilmUserProfile mUserProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSideItemsLayout = (LinearLayout) view.findViewById(R.id.side_items_layout);

        mAccountButton = (Button) view.findViewById(R.id.btn_account);
        mAccountButton.setOnClickListener(this);

        mAvatarImageView = (ImageView) view.findViewById(R.id.imageview_account_avatar);
    }

    @Override
    public void onResume() {
        super.onResume();
        getController().attachUi(this);
    }

    @Override
    public void onPause() {
        getController().detachUi(this);
        super.onPause();
    }

    @Override
    public void setSideMenuItems(SideMenuItem[] items) {
        mSideMenuItems = items;

        if (mSideItemsLayout != null) {
            populateSideItems();
        }
    }

    @Override
    public void showAddAccountButton() {
        mAvatarImageView.setVisibility(View.GONE);
        mAccountButton.setText(R.string.button_add_account);
    }

    @Override
    public void showUserProfile(PhilmUserProfile profile) {
        mUserProfile = profile;

        mAvatarImageView.setVisibility(View.VISIBLE);

        Picasso.with(getActivity())
                .load(profile.getAvatarUrl())
                .resizeDimen(R.dimen.drawer_account_avatar_width,
                        R.dimen.drawer_account_avatar_height)
                .centerCrop()
                .into(mAvatarImageView);

        mAccountButton.setText(profile.getUsername());
    }

    @Override
    public void onClick(View view) {
        if (mCallbacks != null) {
            if (view == mAccountButton) {
                if (mUserProfile != null) {
                    // TODO: Show profile or something
                } else {
                    mCallbacks.addAccountRequested();
                }
            } else if (view.getTag() instanceof SideMenuItem) {
                mCallbacks.onSideMenuItemSelected((SideMenuItem) view.getTag());
            }
        }
    }

    private void populateSideItems() {
        // TODO: Re-use these Views
        mSideItemsLayout.removeAllViews();

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        for (SideMenuItem item : mSideMenuItems) {
            Button button = (Button) inflater.inflate(R.layout.item_drawer, mSideItemsLayout, false);
            button.setText(item.getTitle());
            button.setTag(item);
            button.setOnClickListener(this);
            mSideItemsLayout.addView(button);
        }
    }

    @Override
    public void setCallbacks(MainControllerUiCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    private MainController getController() {
        return PhilmApplication.from(getActivity()).getMainController();
    }

}
