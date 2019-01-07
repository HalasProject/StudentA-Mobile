package fr.halas.loginhalas.Filter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.halas.loginhalas.R;

public class AffichageAdapter extends FirestoreAdapter<AffichageAdapter.ViewHolder> {


    public interface OnAffichageSelectedListener {

        void onAffichageSelected(DocumentSnapshot affichage);

    }

    static private Context mContext;

    private OnAffichageSelectedListener mListener;

    public AffichageAdapter(Query query, OnAffichageSelectedListener listener, Context context) {
        super(query);
        mListener = listener;
        this.mContext = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.affichage, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        @BindView(R.id.avatar)
        ImageView imageView;

        @BindView(R.id.module)
        TextView moduleView;

        @BindView(R.id.imageViewDelete)
        ImageView imageDelete;

        @BindView(R.id.years)
        TextView yearView;

        @BindView(R.id.sections)
        TextView sectionView;

        @BindView(R.id.groupe)
        TextView groupeView;

        @BindView(R.id.creator)
        TextView creator;

        @BindView(R.id.date)
        TextView textdate;

        @BindView(R.id.deleting)
        RelativeLayout deleting;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final DocumentSnapshot snapshot, final OnAffichageSelectedListener listener) {

            final Affichage affichage = snapshot.toObject(Affichage.class);


            // Load image
            if (affichage.getYear().equals("Licence 1")) {
                imageView.setImageResource(R.drawable.license1);
            }
            if (affichage.getYear().equals("Licence 2")) {
                imageView.setImageResource(R.drawable.license2);
            }
            if (affichage.getYear().equals("Licence 3")) {
                imageView.setImageResource(R.drawable.license3);
            }
            if (affichage.getYear().equals("Master 1")) {
                imageView.setImageResource(R.drawable.master1);
            }
            if (affichage.getYear().equals("Master 2")) {
                imageView.setImageResource(R.drawable.master2);
            }

            //


//            textdate.setText(sfd.format(new Date(affichage.getDate().getSeconds()*1000)));
            creator.setText(affichage.getCreatorName());
            moduleView.setText(affichage.getModule());
            yearView.setText(affichage.getYear());
            groupeView.setText("Groupe: " + affichage.getGroupe());
            sectionView.setText("Section: " + affichage.getSection());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final String userID = affichage.getUserID();
            if (userID.equals(user.getUid())) {
                imageDelete.setVisibility(View.VISIBLE);
                imageDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final AlertDialog.Builder Realy = new AlertDialog.Builder(mContext);
                        Realy.setIcon(R.drawable.ic_delete);
                        Realy.setCancelable(true);
                        Realy.setTitle(R.string.AlertDialogue_Title);
                        Realy.setMessage(R.string.AlertDialogue_Message);
                        Realy.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("affichages").document(snapshot.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference storageRef = storage.getReference();
                                                StorageReference deleteFile = storageRef.child(affichage.getImage());
                                                deleteFile.delete();
                                                Log.d("DELETED", "DocumentSnapshot successfully deleted!" + storageRef);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("DELETED", "Error deleting document", e);
                                            }
                                        });
                            }
                        });
                        Realy.show();
                    }
                });
            } else
                imageDelete.setVisibility(View.GONE);


            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (listener != null) {
                        listener.onAffichageSelected(snapshot);

                    }
                }
            });
        }

    }

}
