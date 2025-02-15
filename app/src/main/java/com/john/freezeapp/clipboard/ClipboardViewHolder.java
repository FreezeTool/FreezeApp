package com.john.freezeapp.clipboard;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.john.freezeapp.R;
import com.john.freezeapp.recyclerview.CardViewHolder;

public class ClipboardViewHolder extends CardViewHolder<ClipboardCardData> {

    public TextView tvCopy, tvContent;

    public static Creator<ClipboardCardData> CREATOR = (inflater, parent) -> new ClipboardViewHolder(inflater.inflate(R.layout.item_clipboard, parent, false));

    public ClipboardViewHolder(View itemView) {
        super(itemView);
        tvCopy = itemView.findViewById(R.id.tv_copy);
        tvContent = itemView.findViewById(R.id.tv_content);

    }

    @Override
    public void onBind() {
        super.onBind();
        ClipboardCardData data = getData();
        tvContent.setText(TextUtils.isEmpty(data.content) ? "" : data.content);
        tvCopy.setOnClickListener(v -> {
            boolean copy = Clipboard.copy(data.id);
            Toast.makeText(getContext(), copy ? "复制成功" : "复制失败", Toast.LENGTH_SHORT).show();
        });
    }
}
