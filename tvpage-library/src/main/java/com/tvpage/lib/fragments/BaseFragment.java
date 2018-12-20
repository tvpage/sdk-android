package com.tvpage.lib.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tvpage.lib.R;
import com.tvpage.lib.TvPageGalleryActivity;

/**
 * Created by MTPC-110 on 11/30/2017.
 */

public class BaseFragment extends Fragment {
    public View rootView = null;

    public void pushFragment(final Fragment fragment, final Fragment parentFragment, boolean isAddToBackStack, boolean isJustAdd, final boolean shouldAnimate, final boolean ignorIfCurrent
    ) {
        if (fragment == null || parentFragment == null)
            return;


        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();


        // Find current visible fragment
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.fragment_container);

        if (ignorIfCurrent && fragmentCurrent != null) {
            if (fragment.getClass().getCanonicalName().equalsIgnoreCase(fragmentCurrent.getTag())) {
                return;
            }
        }


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
//            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            // fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        if (fragmentCurrent != null) {
            fragmentTransaction.hide(fragmentCurrent);
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getCanonicalName());
        }

        if (isJustAdd) {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName());
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName());
        }

        //hide keyboard
        hideKeyboard();

        fragmentTransaction.commitAllowingStateLoss();
    }

    //show keyboard
    public void showKeyboard(View view) {
        /*if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }*/
    }

    //hide keyboard
    public void hideKeyboard() {
        /*View view = getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    /**
     * push fragment
     *
     * @param fragment
     * @param isAddToBackStack
     * @param isJustAdd
     * @param shouldAnimate
     * @param ignorIfCurrent
     */
    protected void pushFragment(final Fragment fragment,
                              boolean isAddToBackStack, boolean isJustAdd,
                              final boolean shouldAnimate, final boolean ignorIfCurrent) {
        if (fragment == null)
            return;


        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


        // Find current visible fragment
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.fragment_container);

        if (ignorIfCurrent && fragmentCurrent != null) {
            if (fragment.getClass().getCanonicalName().equalsIgnoreCase(fragmentCurrent.getTag())) {
                return;
            }
        }


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
//            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            // fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        } else {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        if (fragmentCurrent != null) {
            fragmentTransaction.hide(fragmentCurrent);
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getCanonicalName());
        }

        if (isJustAdd) {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName());
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName());
        }

        //hide keyboard
        fragmentTransaction.commitAllowingStateLoss();

    }
}
