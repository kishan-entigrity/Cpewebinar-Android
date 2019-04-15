package com.entigrity.model.webinar_details_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WebinarDetail {

    @SerializedName("subject_area")
    private String subjectArea;

    @SerializedName("course_level")
    private String courseLevel;

    @SerializedName("program_description")
    private String programDescription;

    @SerializedName("advance_preparation")
    private String advancePreparation;

    @SerializedName("shareable_link")
    private String shareableLink;

    @SerializedName("webinar_date")
    private String webinarDate;

    @SerializedName("duration")
    private String duration;

    @SerializedName("Learning_objective")
    private String learningObjective;

    @SerializedName("like_dislike_status")
    private String likeDislikeStatus;

    @SerializedName("instructional_method")
    private String instructionalMethod;

    @SerializedName("faq")
    private String faq;

    @SerializedName("webinar_title")
    private String webinarTitle;

    @SerializedName("credit")
    private String credit;

    @SerializedName("about_presententer")
    private AboutPresententer aboutPresententer;

    @SerializedName("cost")
    private String cost;

    @SerializedName("prerequisite")
    private String prerequisite;

    @SerializedName("who_should_attend")
    private List<Object> whoShouldAttend;

    @SerializedName("webinar_thumbnail")
    private String webinarThumbnail;

    @SerializedName("webinar_video_url")
    private String webinarVideoUrl;

    @SerializedName("presentation_handout")
    private String presentationHandout;

    @SerializedName("series")
    private String series;

    @SerializedName("refund_and_cancelation_policy")
    private String refundAndCancelationPolicy;

    @SerializedName("nasba_approved")
    private NasbaApproved nasbaApproved;

    @SerializedName("webinar_status")
    private String webinarStatus;

    @SerializedName("status")
    private String status;

    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public String getSubjectArea() {
        return subjectArea;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setAdvancePreparation(String advancePreparation) {
        this.advancePreparation = advancePreparation;
    }

    public String getAdvancePreparation() {
        return advancePreparation;
    }

    public void setShareableLink(String shareableLink) {
        this.shareableLink = shareableLink;
    }

    public String getShareableLink() {
        return shareableLink;
    }

    public void setWebinarDate(String webinarDate) {
        this.webinarDate = webinarDate;
    }

    public String getWebinarDate() {
        return webinarDate;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setLearningObjective(String learningObjective) {
        this.learningObjective = learningObjective;
    }

    public String getLearningObjective() {
        return learningObjective;
    }

    public void setLikeDislikeStatus(String likeDislikeStatus) {
        this.likeDislikeStatus = likeDislikeStatus;
    }

    public String getLikeDislikeStatus() {
        return likeDislikeStatus;
    }

    public void setInstructionalMethod(String instructionalMethod) {
        this.instructionalMethod = instructionalMethod;
    }

    public String getInstructionalMethod() {
        return instructionalMethod;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getFaq() {
        return faq;
    }

    public void setWebinarTitle(String webinarTitle) {
        this.webinarTitle = webinarTitle;
    }

    public String getWebinarTitle() {
        return webinarTitle;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCredit() {
        return credit;
    }

    public void setAboutPresententer(AboutPresententer aboutPresententer) {
        this.aboutPresententer = aboutPresententer;
    }

    public AboutPresententer getAboutPresententer() {
        return aboutPresententer;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCost() {
        return cost;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setWhoShouldAttend(List<Object> whoShouldAttend) {
        this.whoShouldAttend = whoShouldAttend;
    }

    public List<Object> getWhoShouldAttend() {
        return whoShouldAttend;
    }

    public void setWebinarThumbnail(String webinarThumbnail) {
        this.webinarThumbnail = webinarThumbnail;
    }

    public String getWebinarThumbnail() {
        return webinarThumbnail;
    }

    public void setWebinarVideoUrl(String webinarVideoUrl) {
        this.webinarVideoUrl = webinarVideoUrl;
    }

    public String getWebinarVideoUrl() {
        return webinarVideoUrl;
    }

    public void setPresentationHandout(String presentationHandout) {
        this.presentationHandout = presentationHandout;
    }

    public String getPresentationHandout() {
        return presentationHandout;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeries() {
        return series;
    }

    public void setRefundAndCancelationPolicy(String refundAndCancelationPolicy) {
        this.refundAndCancelationPolicy = refundAndCancelationPolicy;
    }

    public String getRefundAndCancelationPolicy() {
        return refundAndCancelationPolicy;
    }

    public void setNasbaApproved(NasbaApproved nasbaApproved) {
        this.nasbaApproved = nasbaApproved;
    }

    public NasbaApproved getNasbaApproved() {
        return nasbaApproved;
    }

    public void setWebinarStatus(String webinarStatus) {
        this.webinarStatus = webinarStatus;
    }

    public String getWebinarStatus() {
        return webinarStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "WebinarDetail{" +
                        "subject_area = '" + subjectArea + '\'' +
                        ",course_level = '" + courseLevel + '\'' +
                        ",program_description = '" + programDescription + '\'' +
                        ",advance_preparation = '" + advancePreparation + '\'' +
                        ",shareable_link = '" + shareableLink + '\'' +
                        ",webinar_date = '" + webinarDate + '\'' +
                        ",duration = '" + duration + '\'' +
                        ",learning_objective = '" + learningObjective + '\'' +
                        ",like_dislike_status = '" + likeDislikeStatus + '\'' +
                        ",instructional_method = '" + instructionalMethod + '\'' +
                        ",faq = '" + faq + '\'' +
                        ",webinar_title = '" + webinarTitle + '\'' +
                        ",credit = '" + credit + '\'' +
                        ",about_presententer = '" + aboutPresententer + '\'' +
                        ",cost = '" + cost + '\'' +
                        ",prerequisite = '" + prerequisite + '\'' +
                        ",who_should_attend = '" + whoShouldAttend + '\'' +
                        ",webinar_thumbnail = '" + webinarThumbnail + '\'' +
                        ",webinar_video_url = '" + webinarVideoUrl + '\'' +
                        ",presentation_handout = '" + presentationHandout + '\'' +
                        ",series = '" + series + '\'' +
                        ",refund_and_cancelation_policy = '" + refundAndCancelationPolicy + '\'' +
                        ",nasba_approved = '" + nasbaApproved + '\'' +
                        ",webinar_status = '" + webinarStatus + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}