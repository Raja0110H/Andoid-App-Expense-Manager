package com.expense.manager.smith;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public String i0() {
        return getBaseActivity().getCurrencySymbol();
    }

    private BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
