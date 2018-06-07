package edu.pdx.ekbotecetolafinalpi.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.pdx.ekbotecetolafinalpi.managers.FirestoreManager;
import edu.pdx.ekbotecetolafinalpi.uart.Command;
import edu.pdx.ekbotecetolafinalpi.uart.DataPacket;
import edu.pdx.ekbotecetolafinalpi.uart.Response;

public class MessageDaoImpl implements MessageDao {
    private static final String TAG = "MessageDaoImpl";
    private FirestoreManager dbManager;
    private FirebaseFirestore db;

    public MessageDaoImpl(FirestoreManager dbManager) {
        this.dbManager = dbManager;
        db = dbManager.getDatabase();
    }

    @Override
    public void saveCommand(final Command cmd, OnSuccessListener<DocumentReference> result) {
        db.collection(Command.COLLECTION)
                .add(cmd)
                .addOnSuccessListener(result)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public Command getCommand(String id) {
        DocumentReference docRef = db.collection(Command.COLLECTION).document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Command cmd = document.toObject(Command.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return null;
    }

    @Override
    public void saveResponse(Response rsp, OnSuccessListener<DocumentReference> result) {
        db.collection(Response.COLLECTION)
                .add(rsp)
                .addOnSuccessListener(result)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void saveDataPacket(DataPacket dp) {
        //stub
    }

    @Override
    public DocumentReference getCommandRef(String id) {
        return dbManager.getDatabase().collection(Command.COLLECTION).document(id);
    }
}
