package com.example.android.geofence;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.android.geofence.R;
import org.json.JSONArray;
import org.json.JSONException;

public class FriendsDialog extends DialogFragment {

    private final JSONArray friendsArray;
    ListView friendsListView;
    ContactsListener onConfirmListener;
    ContactsListener onCancelListener;
    boolean cancelled = true; // Only goes false once confirm button has been pressed

    public FriendsDialog(JSONArray friends) {
        super();
        this.friendsArray = friends;
    }

    @Override
    public void onCreate(Bundle savedInstance)   {
        super.onCreate(savedInstance);
        this.setRetainInstance(true);
        this.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup containter,Bundle savedInstance) {
        View view = inflater.inflate(R.layout.friends_dialog,null);
        friendsListView = (ListView) view.findViewById(R.id.friends_listView_friendsDialog);
        friendsListView.setAdapter(new FriendsAdapter(this.friendsArray));
        view.findViewById(R.id.confirm_button_friendsdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelled = false;
                onDestroyView();
            }
        });
        view.findViewById(R.id.cancel_button_friendsdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
            }
        });
        return view;
    }

    /*
        TO USE FRIEND DIALOG
            Fragment Transaction ft = getFragmentManager().beginTransaction();
            FriendsDialog fd = new FriendsDialog(JSONArray friends);
            fd.show(ft, some string for dialog tag);
     */

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        if (cancelled && onCancelListener != null)
            onCancelListener.getContacts(null);
        else if (onConfirmListener != null)
            onConfirmListener.getContacts( ((FriendsAdapter)friendsListView.getAdapter()).getContacts() );
        super.onDestroyView();
    }

    private class FriendsAdapter extends BaseAdapter {

        private JSONArray friends;
        boolean[] selected;

        public FriendsAdapter(JSONArray friends) {
            this.friends = friends;
            this.selected = new boolean[this.friends.length()];

        }

        @Override
        public int getCount() {
            return this.friends.length();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int i) {
            return i;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int i) {
            return i;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final TextView tv = new TextView(getActivity());
            try {
                tv.setText(this.friends.getJSONObject(i).getString("username"));
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                tv.setBackgroundColor(Color.YELLOW);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selected[i]) {
                            tv.setBackgroundColor(Color.WHITE);
                            selected[i] = false;
                        } else {
                            tv.setBackgroundColor(Color.YELLOW);
                            selected[i] = true;
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tv;
        }

        public JSONArray getContacts() {
            try {
                JSONArray contacts = new JSONArray();
                for (int i = 0, contactsi = 0; i < this.selected.length; i++) {
                    if (this.selected[i]) {
                        contacts.put(contactsi, this.friends.getJSONObject(i));
                        contactsi++;
                    }
                }
                return contacts;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void setOnConfirmListener(ContactsListener listener) {
        this.onConfirmListener = listener;
    }

    public void setOnCancelListener(ContactsListener listener) {
        this.onCancelListener = listener;
    }

    public interface ContactsListener {
        public void getContacts(JSONArray contacts);
    }

}
