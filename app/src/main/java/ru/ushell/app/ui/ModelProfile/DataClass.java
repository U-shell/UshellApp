package ru.ushell.app.ui.ModelProfile;

public class DataClass {
    private int dataTitle;
    private int dataDesc;
    private int dataImage;

    public DataClass(int title,int dataDesc, int dataImage) {
        this.dataTitle = title;
        this.dataDesc = dataDesc;
        this.dataImage = dataImage;
    }

    public int getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(int dataTitle) {
        this.dataTitle = dataTitle;
    }

    public int getDataImage() {
        return dataImage;
    }

    public void setDataImage(int dataImage) {
        this.dataImage = dataImage;
    }


    public int getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(int dataDesc) {
        this.dataDesc = dataDesc;
    }
}
