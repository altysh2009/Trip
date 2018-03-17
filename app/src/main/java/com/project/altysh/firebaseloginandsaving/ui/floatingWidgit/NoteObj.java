package com.project.altysh.firebaseloginandsaving.ui.floatingWidgit;

/**
 * Created by Altysh on 3/13/2018.
 */

public class NoteObj {
    boolean selected;
    String item;

    public NoteObj(boolean selected, String item) {
        this.selected = selected;
        this.item = item;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
