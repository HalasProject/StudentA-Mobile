package fr.halas.loginhalas.helper;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

import fr.halas.loginhalas.R;

public class Adapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<HashMap<String, String>> items;
    static LayoutInflater inflater =null;
    String module;
    String years;
    String groupe;
    String section;


    public Adapter(Context context, ArrayList<HashMap<String, String>> items,String module,String years,String groupe, String section) {
        this.context = context;
        this.items = items;
        this.module = module;
        this.years = years;
        this.groupe = groupe;
        this.section = section;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View affichage = convertView;

        if (affichage == null){

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            affichage = inflater.inflate(R.layout.affichage,parent,false);
        }

        //Button btnDelete = affichage.findViewById(R.id.buttonDelete);
        ImageView avatar = affichage.findViewById(R.id.avatar);

        TextView Text1 = affichage.findViewById(R.id.textModule);
        TextView Text2 = affichage.findViewById(R.id.years);
        TextView Text3 = affichage.findViewById(R.id.groupe);
        TextView Text4 = affichage.findViewById(R.id.sections);

        Text1.setText(module);
        Text2.setText(years);
        Text3.setText(groupe);
        Text4.setText(section);


        return null;
    }
}
