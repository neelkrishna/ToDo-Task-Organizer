package com.neelkrishna.todotaskorganizer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by neelkrishna
 */
public class ToDoItemFragment extends Fragment{
    private static final String ARG_ID = "id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    //private static final int REQUEST_PHOTO= 2;

    private Callbacks mCallbacks;
    private ToDoItem mToDoItem;
    //private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mCompletedCheckbox;
    private ImageButton mPhotoButton;
    //private ImageView mPhotoView;

    public interface Callbacks{
        void onToDoItemUpdated(ToDoItem toDoItem);
    }

    public static ToDoItemFragment newInstance(UUID id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, id);
        ToDoItemFragment fragment = new ToDoItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getArguments().getSerializable(ARG_ID);
        mToDoItem = Singleton.get(getActivity()).getToDoItem(id);
        if(mToDoItem == null) mToDoItem = new ToDoItem();
//        mPhotoFile = Singleton.get(getActivity()).getPhotoFile(mToDoItem);
    }

    @Override
    public void onPause() {
        super.onPause();

        Singleton.get(getActivity()).updateToDoItem(mToDoItem);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todoitem, container, false);

        mTitleField = (EditText) v.findViewById(R.id.todoitem_title);
        mTitleField.setText(mToDoItem.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getActivity() == null) {
                    return;
                }
                mToDoItem.setTitle(s.toString());
                updateToDoItem();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.todoitem_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mToDoItem.getDueDate());
                dialog.setTargetFragment(ToDoItemFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mCompletedCheckbox = (CheckBox) v.findViewById(R.id.todoitem_completed);
        mCompletedCheckbox.setChecked(mToDoItem.isCompleted());
        mCompletedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mToDoItem.setCompleted(isChecked);
                updateToDoItem();
            }
        });

//        PackageManager packageManager = getActivity().getPackageManager();
//
//        mPhotoButton = (ImageButton) v.findViewById(R.id.camera);
//        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        boolean canTakePhoto = mPhotoFile != null &&
//                captureImage.resolveActivity(packageManager) != null;
//        mPhotoButton.setEnabled(canTakePhoto);
//
//        if (canTakePhoto) {
//            Uri uri = Uri.fromFile(mPhotoFile);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }
//
//        mPhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(captureImage, REQUEST_PHOTO);
//            }
//        });
//
//        mPhotoView = (ImageView) v.findViewById(R.id.photo);
//        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mToDoItem.setDueDate(date);
            updateToDoItem();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
            };
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver
                    .query(contactUri, queryFields, null, null, null);

            try {
                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();

                updateToDoItem();
            } finally {
                c.close();
            }
        }
//         else if (requestCode == REQUEST_PHOTO) {
//            updateToDoItem();
//            updatePhotoView();
//        }
    }

    private void updateToDoItem() {
        Singleton.get(getActivity()).updateToDoItem(mToDoItem);
        mCallbacks.onToDoItemUpdated(mToDoItem);
    }

    private void updateDate() {
        mDateButton.setText(mToDoItem.getDueDate().toString());
    }

//    private void updatePhotoView() {
//        if (mPhotoFile == null || !mPhotoFile.exists()) {
//            mPhotoView.setImageDrawable(null);
//        } else {
//            Bitmap bitmap = PictureUtils.getScaledBitmap(
//                    mPhotoFile.getPath(), getActivity());
//            mPhotoView.setImageBitmap(bitmap);
//        }
//    }
}
