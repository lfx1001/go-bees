package com.davidmiguel.gobees.apiaries;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.utils.BaseViewHolder;
import com.davidmiguel.gobees.utils.ItemTouchHelperViewHolder;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Apiaries list adapter.
 */
class ApiariesAdapter extends RecyclerView.Adapter<ApiariesAdapter.ViewHolder> {

    private MenuInflater menuInflater;
    private List<Apiary> apiaries;
    private ApiaryItemListener listener;

    ApiariesAdapter(MenuInflater menuInflater, List<Apiary> apiaries, ApiaryItemListener listener) {
        this.menuInflater = menuInflater;
        this.apiaries = checkNotNull(apiaries);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apiaries_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(apiaries.get(position));
    }

    @Override
    public int getItemCount() {
        return apiaries == null ? 0 : apiaries.size();
    }

    void replaceData(List<Apiary> apiaries) {
        this.apiaries = checkNotNull(apiaries);
        notifyDataSetChanged();
    }

    interface ApiaryItemListener {
        void onApiaryClick(Apiary clickedApiary);

        void onApiaryDelete(Apiary clickedApiary);

        void onOpenMenuClick(View view);
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements BaseViewHolder<Apiary>, View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,
            ItemTouchHelperViewHolder {

        private View viewHolder;
        private CardView card;
        private TextView apiaryName;
        private TextView numHives;
        private ImageView moreIcon;

        private Drawable background;

        ViewHolder(View itemView) {
            super(itemView);

            // Get views
            viewHolder = itemView;
            card = (CardView) itemView.findViewById(R.id.card);
            apiaryName = (TextView) itemView.findViewById(R.id.apiary_name);
            numHives = (TextView) itemView.findViewById(R.id.num_hives);
            moreIcon = (ImageView) itemView.findViewById(R.id.more_icon);

            // Set listeners
            viewHolder.setOnClickListener(this);
            viewHolder.setOnCreateContextMenuListener(this);
            moreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open Menu
                    listener.onOpenMenuClick(viewHolder);
                }
            });

            background = card.getBackground();
        }

        public void bind(@NonNull Apiary apiary) {
            apiaryName.setText(apiary.getName());
            if(apiary.getHives() != null) {
                numHives.setText(Integer.toString(apiary.getHives().size()));
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            // Inflate menu
            menuInflater.inflate(R.menu.apiary_item_menu, contextMenu);
            // Set click listener
            for (int i = 0; i < contextMenu.size(); i++) {
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            listener.onApiaryClick(apiaries.get(getAdapterPosition()));
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_edit:
                    // TODO
                    return true;
                case R.id.menu_delete:
                    listener.onApiaryDelete(apiaries.get(getAdapterPosition()));
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onItemSelected() {
            card.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            card.setBackground(background);
        }
    }
}
