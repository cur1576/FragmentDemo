package com.example.fragmentdemo;




import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static class AuswahlFragment extends ListFragment {
        private static final String STR_ZULETZT_SELEKTIERT="zuletztSelektiert";
        boolean zweiSpaltenModus;
        int zuletztSelektiert = 0;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_activated_1,
                    new String[]{"eins","zwei","drei"}));

            View detailsFrame = getActivity().findViewById(R.id.details);
            zweiSpaltenModus = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
            
            if(savedInstanceState!=null){
                zuletztSelektiert=savedInstanceState.getInt(STR_ZULETZT_SELEKTIERT,0);
            }
            if(zweiSpaltenModus){
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                detailsAnzeigen(zuletztSelektiert);
            }
        }

        private void detailsAnzeigen(int zuletztSelektiert) {
            this.zuletztSelektiert=zuletztSelektiert;
            if(zweiSpaltenModus){
                getListView().setItemChecked(zuletztSelektiert,true);
                DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.details);
                if(details!=null || details.getIndex()!=zuletztSelektiert){
                    details = DetailFragment.newInstance(zuletztSelektiert);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.details,details);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),DetailsActivity.class);
                    intent.putExtra(DetailFragment.INDEX, zuletztSelektiert);
                    startActivity(intent);
                }
            }
        }
    }

    public static class DetailFragment extends Fragment {

        public static final String INDEX = "index";

        public static DetailFragment newInstance(int index) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putInt(INDEX,index);
            fragment.setArguments(args);
            return fragment;
        }

        public int getIndex() {
            return getArguments().getInt(INDEX,0);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ScrollView scrollView = null;
            if(container != null){
                scrollView = new ScrollView(getActivity());
                TextView textView = new TextView(getActivity());
                scrollView.addView(textView);
                textView.setText("Element #" + (1+getIndex())+ " ist sichtbar");
            }
            return scrollView;
        }
    }


    public static class DetailsActivity extends AppCompatActivity{
        
    }
}
