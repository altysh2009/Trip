package com.project.altysh.firebaseloginandsaving.ui.main.mainFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;

import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String seach;
    SwipeController swipeController = null;
    AlertDialog diaBox;
    boolean delete = false;
    Trip_DTO tripDto;
    TextView textView;
    ArrayList<String> tripNotes = new ArrayList<String>();
    private RecyclerView recyclerView;
    private NewRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FireBaseConnection fireBaseConnection;
    private String mParam1;
    private String mParam2;
    private List<Trip_DTO> trips = new ArrayList<>();


    public HomeScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.requestFocus();
        setupRecyclerView();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        textView = view.findViewById(R.id.no_date);
        Context context = view.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NewRVAdapter(trips, getActivity());
        recyclerView.setAdapter(adapter);
        textView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fireBaseConnection = FireBaseConnection.getInstance(getContext());
    }

    public void setDate(List<Trip_DTO> list) {
        if (list != null && list.size() > 0) {
            textView.setVisibility(View.GONE);
            adapter.setTrips(list);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setTrips(new ArrayList<Trip_DTO>());
            adapter.notifyDataSetChanged();
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {


                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")
                        .setIcon(R.mipmap.delete)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                fireBaseConnection.deleteTrip(adapter.getTrips().get(position));
//                                ((RVAdapter) adapter).getTrips().remove(position);
//                                adapter.notifyItemRemoved(position);
//                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                delete = false;
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                delete = false;
                                dialog.dismiss();

                            }
                        })
                        .create();


                myQuittingDialogBox.show();
            }
        });
    }

}
