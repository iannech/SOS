package com.example.sos.layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sos.R;

import java.util.List;

public class ContactItemAdapter extends
		RecyclerView.Adapter<ContactItemAdapter.ListItemViewHolder> {

	private List<ContactItem> listItemList;
    private Selectable selectable;

	public ContactItemAdapter(List<ContactItem> listItemList, Selectable selectable) {
		this.listItemList = listItemList;
        this.selectable = selectable;
	}

	@Override
	public int getItemCount() {
		return listItemList.size();
	}

	@Override
	public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int i) {
		ContactItem listItem = listItemList.get(i);

		listItemViewHolder.titleTextView.setText(listItem.getTitle());

		listItemViewHolder.decriptionTextView
				.setText(listItem.getDescription());

	}

	@Override
	public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.contact_item, viewGroup, false);

		return new ListItemViewHolder(itemView, selectable);
	}

	public static class ListItemViewHolder extends RecyclerView.ViewHolder{
		protected ImageView avatarImageView;
		protected TextView titleTextView;
		protected TextView decriptionTextView;
        protected ImageButton imgBtnRemoveContact;

		public ListItemViewHolder(View v, final Selectable selectable) {
			super(v);
			avatarImageView = (ImageView) v.findViewById(R.id.avatarImageView);
			titleTextView = (TextView) v.findViewById(R.id.titleTextView);
			decriptionTextView = (TextView) v
					.findViewById(R.id.descriptionTextView);
            imgBtnRemoveContact = (ImageButton) v.findViewById(R.id.imgBtnRemoveContact);
            imgBtnRemoveContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectable.selected(getPosition());
                }
            });
		}


    }
}