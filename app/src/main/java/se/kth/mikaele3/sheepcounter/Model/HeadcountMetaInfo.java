package se.kth.mikaele3.sheepcounter.Model;

/**
 * Created by Mikael on 2015-03-31.
 */
public class HeadcountMetaInfo {

    String headcountIdentifier;
    String startTime;
    String createdBy;

    public String getHeadcountIdentifier() {
        return headcountIdentifier;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public HeadcountMetaInfo(String headcountIdentifier, String startTime, String createdBy) {

        this.headcountIdentifier = headcountIdentifier;
        this.startTime = startTime;
        this.createdBy = createdBy;
    }
}
