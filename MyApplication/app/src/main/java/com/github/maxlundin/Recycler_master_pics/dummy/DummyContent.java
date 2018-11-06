package com.github.maxlundin.Recycler_master_pics.dummy;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static int COUNT;

    public DummyContent(int size) {
        COUNT = size;
        for (int i = 0; i < COUNT; i++)
            ITEMS.add(new DummyItem("", new Pair<>("", "")));
    }

    public DummyItem get(int i) {
        return ITEMS.get(i);
    }

    private static void addItem(DummyItem item) {
        ITEM_MAP.put(item.id, item);
    }

    public void set(int pos, DummyItem dummyItem) {
        addItem(dummyItem);
        ITEMS.set(pos, dummyItem);
    }

    public int size() {
        return ITEMS.size();
    }


    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final Pair<String, String> content;

        public DummyItem(String id, Pair<String, String> content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content.first;
        }
    }
}
