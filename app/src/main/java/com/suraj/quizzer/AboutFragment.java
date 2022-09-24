package com.suraj.quizzer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import com.suraj.quizzer.databinding.FragmentAboutBinding;

import java.util.Objects;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAboutBinding fragmentAboutBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAboutBinding = FragmentAboutBinding.inflate(inflater,container,false);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.1");
        View aboutPage = new AboutPage(getContext(),R.style.Widget_App_AboutPage)
                .isRTL(false)
                .setDescription(getString(R.string.about_app))
                // or Typeface
                .setImage(R.drawable.quizzer)
                .addItem(versionElement)
                .addGroup("Developer").addItem(new Element().setTitle("SURAJ PANDEY"))
                .addGroup("Connect with Us")
                .addEmail("sp9131602@gmail.com")
                .addFacebook("100022581911788")
                .addTwitter("__iamSuRaJ__")
                .addInstagram("surajpandey8528")
                .create();
        LinearLayout linearLayout = fragmentAboutBinding.aboutUs;
        linearLayout.addView(aboutPage);
        fragmentAboutBinding.aboutUs.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.splash_background));
        return fragmentAboutBinding.getRoot();
    }
}